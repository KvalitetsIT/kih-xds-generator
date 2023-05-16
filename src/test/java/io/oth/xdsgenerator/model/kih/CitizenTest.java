package io.oth.xdsgenerator.model.kih;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class CitizenTest {
    private static Logger log = LoggerFactory.getLogger(CitizenTest.class);

    private Citizen readTestData(String file) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String content = new String(getClass().getClassLoader().getResourceAsStream(file).readAllBytes());
        Citizen retVal = mapper.readValue(content, Citizen.class);
        return retVal;
    }

    @Test
    public void TestMarshalling() {
        Citizen c = null;
        try {
            c = readTestData("citizen-json-test-1.json");
            log.info("Read citizen " + c);
        } catch (Exception e) {
            fail(e);
        }
        assertEquals(c.getPersonCivilRegistrationIdentifier(), "2303439995");
    }


}
