package io.oth.xdsgenerator.model.kih;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class AddressPostalTest {
   private static Logger log = LoggerFactory.getLogger(AddressPostalTest.class);

    @Test
    public void TestJacksonMarshalling(){
        AddressPostal ap = new AddressPostal();
        ap.StreetName ="streetname";
        ap.MunicipalityName = "by";
        ap.PostCodeIdentifier = "3333";

        ObjectMapper mapper = new ObjectMapper();
        try {
            String out = mapper.writeValueAsString(ap);
            log.info("Json Address"+ out);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void TestJacksonUnmarshalling() {
        String input = "{\"StreetName\":\"AdresseM33 33\",\"PostCodeIdentifier\":\"9998\",\"MunicipalityName\":\"ByM33\"}";
        AddressPostal ap = new AddressPostal();
        ap.StreetName ="streetname";
        ap.MunicipalityName = "by";
        ap.PostCodeIdentifier = "3333";

        ObjectMapper mapper = new ObjectMapper();
        try {
            ap =mapper.readValue(input,AddressPostal.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals(ap.StreetName,"AdresseM33 33");

    }
}