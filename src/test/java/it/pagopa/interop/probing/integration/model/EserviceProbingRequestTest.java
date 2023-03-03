package it.pagopa.interop.probing.integration.model;

import it.pagopa.interop.probing.interop_be_probing_api.model.EserviceState;
import it.pagopa.interop.probing.model.Eservice;
import it.pagopa.interop.probing.model.EserviceProbingRequest;
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
class EserviceProbingRequestTest {

    @Autowired
    private TestEntityManager testEntityManager;

    EserviceProbingRequest probingRequest;
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
        probingRequest = new EserviceProbingRequest();
        probingRequest.setLastRequest(OffsetDateTime.of(2023, 12, 12,
                1, 0, 0, 0, ZoneOffset.UTC));
        probingRequest.setEservice(eservice);
    }

    @Test
    @DisplayName("Request is correctly saved")
    void testEserviceProbingResponseEntity_whenCorrectDataIsProvided_returnsEserviceProbingResponse(){
        EserviceProbingRequest probingResponseDuplicate = testEntityManager.persistAndFlush(probingRequest);
        assertEquals(OffsetDateTime.of(2023, 12, 12, 1, 0, 0, 0,
                ZoneOffset.UTC), probingResponseDuplicate.getLastRequest(), "Values should be equal");
        assertNotNull(probingResponseDuplicate.getEservice(), "Value should not be null");
        assertEquals("e-service1", probingResponseDuplicate.getEservice().getEserviceName(), "Values should be equal");
    }

    @Test
    @DisplayName("Request isn't saved due to null last request timestamp")
    void testEserviceProbingResponseEntity_whenLastRequestIsNull_throwsException(){
        probingRequest.setLastRequest(null);
        assertThrows(ConstraintViolationException.class, () -> testEntityManager.persistAndFlush(probingRequest),
                "Request should not be saved because response received shouldn't be null");
    }

    @Test
    @DisplayName("Request isn't saved due to null e-service reference value")
    void testEserviceProbingResponseEntity_whenAssociatedEserviceIsNull_throwsException(){
        probingRequest.setEservice(null);
        assertThrows(PersistenceException.class, () -> testEntityManager.persistAndFlush(probingRequest),
                "Request should not be saved because e-service reference shouldn't be null");
    }
    @Test
    @DisplayName("Request isn't saved due to duplicated e-service reference")
    void testEserviceProbingResponseEntity_whenEserviceReferenceAlreadyExists_throwsException(){
        testEntityManager.persistAndFlush(probingRequest);
        EserviceProbingRequest duplicatedProbingRequest = new EserviceProbingRequest();
        duplicatedProbingRequest.setLastRequest(OffsetDateTime.of(OffsetDateTime.now().getYear(),
                OffsetDateTime.now().getMonthValue(), OffsetDateTime.now().getDayOfMonth(),
                23, 59, 0, 0, ZoneOffset.UTC));
        duplicatedProbingRequest.setEservice(eservice);
        assertThrows(PersistenceException.class, () -> testEntityManager.persistAndFlush(duplicatedProbingRequest),
                "Request should not be saved because duplicated e-service reference shouldn't be allowed");
    }
}