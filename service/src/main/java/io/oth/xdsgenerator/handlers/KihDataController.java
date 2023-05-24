package io.oth.xdsgenerator.handlers;

import dk.s4.hl7.cda.convert.PHMRXmlCodec;
import io.oth.xdsgenerator.model.DocumentMetadata;
import io.oth.xdsgenerator.service.cda.CdaMetaDataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.oth.xdsgenerator.model.Code;
import io.oth.xdsgenerator.model.kih.SelfMonitoringCollection;
import io.oth.xdsgenerator.model.kih.SelfMonitoringCollectionList;
import io.oth.xdsgenerator.model.phmr.PersonalHealthMonitoringReportRequest;
import io.oth.xdsgenerator.model.phmr.PhmrAndMetadata;
import io.oth.xdsgenerator.service.KihToPhmrMappingService;
import io.oth.xdsgenerator.service.ServiceException;
import io.oth.xdsgenerator.service.phmr.PhmrBuilderService;
import io.oth.xdsgenerator.service.phmr.PhmrClinicalDocumentSerializer;
import io.oth.xdsgenerator.service.xds.XdsException;
import io.oth.xdsgenerator.service.xds.XdsRequestBuilderService;
import io.oth.xdsgenerator.service.xds.XdsRequestService;

@RestController
public class KihDataController extends AbstractController {

  public static final Logger log = LoggerFactory.getLogger(KihDataController.class);

  @Autowired
  PhmrBuilderService phmrBuilderService;

  @Autowired
  PhmrClinicalDocumentSerializer phmrClinicalDocumentSerializer;

  @Autowired
  XdsRequestBuilderService xdsRequestBuilder;

  @Autowired
  CdaMetaDataFactory cdaMetaDataFactory;
  // @Autowired
  // CdaService cdaService;

  @Autowired
  KihToPhmrMappingService mappingService;

  @Autowired
  private XdsRequestService xdsRequestService;

  @Value("${log.document.content:false}")
  private boolean reqLogIsAllowed;

  @RequestMapping(produces = MediaType.APPLICATION_XML_VALUE, value = "/api/createphmr", method = RequestMethod.POST)
  public String createphmr(@RequestBody SelfMonitoringCollectionList collectionList) throws ServiceException {
    long start = System.currentTimeMillis();
    int measurements = 0;
    String responseString = "";

    log.debug("ID from reqeust {}", collectionList.DocumentUUID);

    for (SelfMonitoringCollection collection : collectionList.getSelfMonitoringCollection()) {
      if (reqLogIsAllowed) {
        log.debug("SelfMonitoringCollection: {}", collection);
      }

      log.debug("Handling monitoring data");
      log.debug("Mapping to PHMR format");
      PersonalHealthMonitoringReportRequest request = mappingService.map(collection);
      measurements += request.results.length + request.getVitalSigns().length;
      log.debug("Building CDA");
      PhmrAndMetadata documentResponse = phmrBuilderService.buildPhmrClinicalDocument(request);
      PHMRXmlCodec phmrXmlCodec = new PHMRXmlCodec();
      String phmrDocument = phmrXmlCodec.encode(documentResponse.getPhmr());

      log.debug("PHMR: " + phmrDocument);

      log.debug("Generating cda metadata for xds");
      DocumentMetadata cdaMetadata = cdaMetaDataFactory
          .createFromCdaRegistrationRequest(documentResponse.getDocumentMetadata(), phmrDocument);
      cdaMetadata.setUniqueId(collectionList.DocumentUUID);

      try {
        log.debug("Invoking XDS repository");
        responseString = xdsRequestService.createAndRegisterDocument(collectionList.DocumentUUID, phmrDocument,
            cdaMetadata);
      } catch (XdsException e) {
        StringBuffer buf = new StringBuffer();
        for (String err : e.getErrors()) {
          buf.append("\"" + err + "\", ");
        }

        log.info("action=xdsexport status=failure tt={} measurements={}", (System.currentTimeMillis() - start), measurements);
        throw new ServiceException("Error communicating with XDS: " + buf.toString());
      }
      log.debug("Returning DocumentID={}", responseString);

    }

    log.info("action=xdsexport status=success tt={} measurements={}", (System.currentTimeMillis() - start), measurements);
    return responseString;
  }

}
