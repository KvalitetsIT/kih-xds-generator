package io.oth.xdsgenerator.dgws;

import org.apache.cxf.binding.soap.Soap11;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.interceptor.CheckFaultInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;

import javax.xml.namespace.QName;


/**
 * Handles SOSI Generator
 */
public class SosiInterceptor extends AbstractSoapInterceptor {

    private static Logger log = LoggerFactory.getLogger(SosiInterceptor.class);

    @Autowired
    private SosiUtil sosiUtil;

    public SosiInterceptor() {
        super(Phase.PRE_STREAM);
        log.info("Initializing SOSI interceptor");
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        long start = System.currentTimeMillis();
        log.debug("Processing message ....");
		try {
			// DGWS is SOAP11
            message.setVersion(Soap11.getInstance());

			// Add the DGWS headers
			Document sosi = sosiUtil.getSosiDocument();
			NodeList children = sosi.getDocumentElement().getFirstChild().getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node element = children.item(i);
				QName qname = new QName(element.getNamespaceURI(), element.getLocalName());
				Header dgwsHeader = new Header(qname, element);
				message.getHeaders().add(dgwsHeader);
			}

		} catch (IOException e) {
			throw new Fault(e);
		}

        log.info("action=dgws tt="+(System.currentTimeMillis()-start) + "ms");
    }
}
