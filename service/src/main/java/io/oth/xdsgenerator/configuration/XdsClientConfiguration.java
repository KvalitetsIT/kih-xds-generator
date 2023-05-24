package io.oth.xdsgenerator.configuration;

import io.oth.xdsgenerator.dgws.STSRequestHelper;
import io.oth.xdsgenerator.dgws.SosiInterceptor;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.XdsClientFactory;
import org.openehealth.ipf.commons.ihe.xds.iti41.Iti41PortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import java.security.cert.X509Certificate;

/**
 * Sets up XDS client for posting to xds repository
 */
@Configuration
public class XdsClientConfiguration {
    private static Logger log = LoggerFactory.getLogger(XdsClientConfiguration.class);

    @Autowired
    private ApplicationContext appContext;

    private TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }

    }};

    @Value("${xds.iti41.endpoint}")
    String xdsIti41Endpoint;

    @Value("${dgws.enabled}")
    Boolean dgwsEnabled;

    @Bean
    public SosiInterceptor getSosiInterceptor() {
        log.info("Creating sosi interceptor");
        return new SosiInterceptor();
    }

    @Bean
    public STSRequestHelper stsRequestHelper() {
        STSRequestHelper requestHelper = new STSRequestHelper();
        return requestHelper;
    }

    //
    @Bean(name = "dgwsTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Iti41PortType getDocumentRepositoryServiceIti41() {
        log.info("Creating Iti41PortType for url: " + xdsIti41Endpoint);

        XdsClientFactory xdsClientFactory = generateXdsRepositoryClientFactory("wsdl/iti41.wsdl", xdsIti41Endpoint,
                Iti41PortType.class);
        Iti41PortType client = (Iti41PortType) xdsClientFactory.getClient();

        initProxy(client);

        return client;
    }

    /**
     * Sets up factory classes for iti41
     */
    private XdsClientFactory generateXdsRepositoryClientFactory(String wsdl, String url, Class<?> clazz) {
        final WsTransactionConfiguration WS_CONFIG = new WsTransactionConfiguration(
                new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Service", "ihe"), clazz,
                new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Binding_Soap12", "ihe"), true, wsdl, true,
                false, false, false);

        XdsClientFactory xcf = new XdsClientFactory(WS_CONFIG, url, null, null, null);
        return xcf;
    }

    /**
     * Sets up proxy for logging
     */
    private void initProxy(Object o) {
        Client proxy = ClientProxy.getClient(o);
        if (log.isDebugEnabled()) {
            proxy.getOutInterceptors().add(new LoggingOutInterceptor());
            proxy.getInInterceptors().add(new LoggingInInterceptor());
        }

        // Add SOSI interceptors as well
        if (dgwsEnabled) {
            SosiInterceptor sosiInterceptor = appContext.getBean(SosiInterceptor.class);
            proxy.getOutInterceptors().add(sosiInterceptor);
        }
        HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
        TLSClientParameters tcp = new TLSClientParameters();
        tcp.setTrustManagers(trustAllCerts);
        tcp.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        conduit.setTlsClientParameters(tcp);
    }

}
