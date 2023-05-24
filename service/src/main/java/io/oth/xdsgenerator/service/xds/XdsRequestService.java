package io.oth.xdsgenerator.service.xds;

import io.oth.xdsgenerator.model.DocumentMetadata;
import io.oth.xdsgenerator.model.xds.ProvideAndRegisterDocumentSetRequest;
import io.oth.xdsgenerator.service.cda.CdaMetaDataFactory;

import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ObjectFactory;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.iti41.Iti41PortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XdsRequestService {
    private static Logger log = LoggerFactory.getLogger(XdsRequestService.class);

    private static final EbXMLFactory ebXMLFactory = new EbXMLFactory30();

    @Autowired
    XdsRequestBuilderService xdsRequestBuilderService;
    //
    // @Autowired
    // Iti18PortType iti18PortType;
    //
    @Autowired
    Iti41PortType iti41PortType;
    //
    // @Autowired
    // Iti43PortType iti43PortType;
    @Autowired
    CdaMetaDataFactory cdaMetaDataFactory;

    protected EbXMLFactory getEbXmlFactory() {
        return ebXMLFactory;
    }

    public String createAndRegisterCda(String externalId, String document, DocumentMetadata cdaMetadata)
            throws XdsException {
        // CdaDocumentMetadata cdaDocumentMetadata =
        // cdaMetaDataFactory.createFromCdaRegistrationRequest(cdaMetadata, document);
        return createAndRegisterDocument(externalId, document, cdaMetadata);
    }

    public String createAndRegisterDocument(String externalId, String document, DocumentMetadata documentMetadata)
            throws XdsException {
        long start = System.currentTimeMillis();
        log.debug("Starting createAndRegisterDocument");
        ProvideAndRegisterDocumentSetRequest provideAndRegisterDocumentSetRequest = xdsRequestBuilderService
                .buildProvideAndRegisterDocumentSetRequest(externalId, document, documentMetadata);
        provideAndRegisterDocumentSetRequest.getProvideAndRegisterDocumentSetRequestType();

        RegistryResponseType registryResponse = iti41PortType.documentRepositoryProvideAndRegisterDocumentSetB(
                provideAndRegisterDocumentSetRequest.getProvideAndRegisterDocumentSetRequestType());

        log.info("Received from XDS " + registryResponse.getStatus());

        if (registryResponse.getRegistryErrorList() == null
                || registryResponse.getRegistryErrorList().getRegistryError() == null
                || registryResponse.getRegistryErrorList().getRegistryError().isEmpty()) {
            log.debug("No errors reported");
        } else {
            log.error("Repository reported " + registryResponse.getRegistryErrorList().getHighestSeverity());

            XdsException e = new XdsException();

            List<RegistryError> errors = registryResponse.getRegistryErrorList().getRegistryError();
            for (RegistryError error : errors) {
                String err = "Error: " + error.getErrorCode() + " Value: " + error.getValue() + " Context: "
                        + error.getCodeContext() + " Severity: " + error.getSeverity();
                log.error(err);
                e.addError(err);
            }

            log.info("action=xds tt=" + (System.currentTimeMillis() - start) + " ms");
            throw e;
        }

        log.info("action=xds tt=" + (System.currentTimeMillis() - start) + " ms");
        return provideAndRegisterDocumentSetRequest.getExternalDocumentId();
    }

}
