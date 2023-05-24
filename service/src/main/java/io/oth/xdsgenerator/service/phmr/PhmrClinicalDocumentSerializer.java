package io.oth.xdsgenerator.service.phmr;

import dk.s4.hl7.cda.convert.PHMRXmlConverter;
import dk.s4.hl7.cda.model.phmr.PHMRDocument;
import org.springframework.util.Base64Utils;

public class PhmrClinicalDocumentSerializer {

	public String serializeToBase64EncodedXML(PHMRDocument phmrClinicalDocument) {	
		PHMRXmlConverter phmrXmlConverter = new PHMRXmlConverter();
		String xml = phmrXmlConverter.convert(phmrClinicalDocument);
		return new String(Base64Utils.encode(xml.getBytes()));
	}
}
