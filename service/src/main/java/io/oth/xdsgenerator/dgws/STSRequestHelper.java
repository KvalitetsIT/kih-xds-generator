package io.oth.xdsgenerator.dgws;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Handles the actual posting to NSP STS
 */
public class STSRequestHelper {

    private static Logger log = LoggerFactory.getLogger(STSRequestHelper.class);

    @Value("${dgws.sts.url}")
    private String stsUrl;

    @Autowired
    @Qualifier("dgwsTemplate")
    private RestTemplate restTemplate;

    public STSRequestHelper() {
    }

    public String sendRequest(String postBody) throws IOException {
        long start = System.currentTimeMillis();
        log.debug("Using sts url " + stsUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/xml; charset=utf-8");
        headers.set("SOAPAction", "\"Issue\"");

        HttpEntity<String> entity = new HttpEntity<>(postBody, headers);

        ResponseEntity<String> result = restTemplate.exchange(stsUrl, HttpMethod.POST, entity, String.class);

        int statusCode = result.getStatusCode().value();
        if (statusCode != 200) {
            log.info("action=sts tt=" + (System.currentTimeMillis() - start) + " ms");
            throw new IOException("HTTP POST failed (" + statusCode + "): " + result.getBody());
        }

        log.info("action=sts tt=" + (System.currentTimeMillis() - start) + " ms");
        return result.getBody();
    }
}
