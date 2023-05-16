package io.oth.xdsgenerator.configuration;

import io.oth.xdsgenerator.service.phmr.PhmrBuilderService;
import io.oth.xdsgenerator.service.phmr.PhmrClinicalDocumentSerializer;
import io.oth.xdsgenerator.service.phmr.PhmrParserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PhmrBuilderServiceConfiguration {
    @Bean
    public PhmrBuilderService phmrBuilderService() {
        PhmrBuilderService phmrBuilderService = new PhmrBuilderService("Hjemmemonitorering for ");
        return phmrBuilderService;
    }

    @Bean
    public PhmrClinicalDocumentSerializer phmrClinicalDocumentSerializer() {
        PhmrClinicalDocumentSerializer phmrClinicalDocumentSerializer = new PhmrClinicalDocumentSerializer();
        return phmrClinicalDocumentSerializer;
    }

//    @Bean
//    public PhmrRequestService phmrRequestService() {
//        PhmrRequestService phmrRequestService = new PhmrRequestService();
//        return phmrRequestService;
//    }

    @Bean
    public PhmrParserService phmrParserService() {
        PhmrParserService phmrParserService = new PhmrParserService();
        return phmrParserService;
    }

}
