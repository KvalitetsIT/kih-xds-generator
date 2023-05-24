package io.oth.xdsgenerator.service;

import io.oth.xdsgenerator.service.phmr.PhmrBuilderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.Properties;

@Configuration
public class TestConfiguration {
    private static final String defaultCode = "325421000016001";
    private static final String defaultName = "Medcom";

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
        final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Properties properties = new Properties();

	    // properties.setProperty("createdby.to.sor.code.mapping","TeleCare Nord=6071000016008;OpenTele Region Hovedstaden=6111000016004;OpenTele Region Midt=6081000016005");

	    // properties.setProperty("default.sor.code",defaultName + "=" + defaultCode );
	    properties.setProperty("sor.code", defaultCode );
        properties.setProperty("sor.name", defaultName );

        pspc.setProperties(properties);

        return pspc;
    }

    @Bean
    public KihToPhmrMappingService kihToPhmrMappingService() {
        return new KihToPhmrMappingService();
    }

    @Bean
    public PhmrBuilderService phmrBuilderService() {
        return new PhmrBuilderService();
    }
}
