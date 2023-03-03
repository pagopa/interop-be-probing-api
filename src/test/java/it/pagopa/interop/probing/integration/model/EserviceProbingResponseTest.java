package it.pagopa.interop.probing.integration.model;

import it.pagopa.interop.probing.interop_be_probing_api.model.EserviceState;
import it.pagopa.interop.probing.model.Eservice;
import it.pagopa.interop.probing.model.EserviceProbingResponse;
import it.pagopa.interop.probing.util.EserviceTechnology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EserviceProbingResponseTest {

    @Autowired
    private TestEntityManager testEntityManager;

    EserviceProbingResponse probingResponse;
    Eservice eservice;

    @BeforeEach
    void setup(){
        eservice = new Eservice();
        eservice.setState(EserviceState.INACTIVE);
        eservice.setEserviceId(UUID.randomUUID());
        eservice.setVersionId(UUID.randomUUID());
        eservice.setEserviceName("e-service1");
        eservice.setBasePath(new String[] {"test1", "test2"});
        eservice.setTechnology(EserviceTechnology.REST);
        eservice.setProducerName("producer1");
        probingResponse = new EserviceProbingResponse();
        probingResponse.setResponseReceived(OffsetDateTime.of(2023, 12, 12,
                1, 0, 0, 0, ZoneOffset.UTC));
        probingResponse.setEservice(eservice);
    }

    @Test
    @DisplayName("Response is correctly saved")
    void testEserviceProbingResponseEntity_whenCorrectDataIsProvided_returnsEserviceProbingResponse(){
        EserviceProbingResponse probingResponseDuplicate = testEntityManager.persistAndFlush(probingResponse);
        assertEquals(OffsetDateTime.of(2023, 12, 12, 1, 0, 0,
                0, ZoneOffset.UTC), probingResponseDuplicate.getResponseReceived(),
                "Values should be equal");
        assertNotNull(probingResponseDuplicate.getEservice(), "Values should not be null");
        assertEquals("e-service1", probingResponseDuplicate.getEservice().getEserviceName(),
                "Values should be equal");
    }

    @Test
    @DisplayName("Response isn't saved due to null response received timestamp")
    void testEserviceProbingResponseEntity_whenResponseReceivedIsNull_throwsException(){
        probingResponse.setResponseReceived(null);
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.persistAndFlush(probingResponse),
                "Response should not be saved because response received shouldn't be null");
    }

    @Test
    @DisplayName("Response isn't saved due to null e-service reference value")
    void testEserviceProbingResponseEntity_whenAssociatedEserviceIsNull_throwsException(){
        probingResponse.setEservice(null);
        assertThrows(PersistenceException.class, () -> testEntityManager.persistAndFlush(probingResponse),
                "Response should not be saved because e-service reference shouldn't be null");
    }
    @Test
    @DisplayName("Response isn't saved due to duplicated e-service reference")
    void testEserviceProbingResponseEntity_whenEserviceReferenceAlreadyExists_throwsException(){
        testEntityManager.persistAndFlush(probingResponse);
        EserviceProbingResponse duplicatedProbingResponse = new EserviceProbingResponse();
        duplicatedProbingResponse.setResponseReceived(OffsetDateTime.of(OffsetDateTime.now().getYear(),
                OffsetDateTime.now().getMonthValue(), OffsetDateTime.now().getDayOfMonth(),
                23, 59, 0, 0, ZoneOffset.UTC));
        duplicatedProbingResponse.setEservice(eservice);
        assertThrows(PersistenceException.class, () -> testEntityManager.persistAndFlush(duplicatedProbingResponse),
                "Response should not be saved because duplicated e-service reference shouldn't be allowed");
    }
}