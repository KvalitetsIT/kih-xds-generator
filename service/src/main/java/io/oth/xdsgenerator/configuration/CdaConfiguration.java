package io.oth.xdsgenerator.configuration;


import io.oth.xdsgenerator.model.xds.Codes;
import io.oth.xdsgenerator.model.xds.OrganisationIdAuthority;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CdaConfiguration {

    @Bean
    public OrganisationIdAuthority organisationIdAuthority() {
        return new OrganisationIdAuthority(Codes.DK_SOR_CLASSIFICAION_OID);
    }

}
