package it.pagopa.interop.probing.integration.model;

import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import it.pagopa.interop.probing.model.Eservice;
import it.pagopa.interop.probing.util.EserviceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EserviceTest {

    @Autowired
    private TestEntityManager testEntityManager;

    Eservice eservice;

    @BeforeEach
    void setup(){
        eservice = new Eservice();
        eservice.setState(EServiceState.INACTIVE);
        eservice.setEserviceId(UUID.randomUUID());
        eservice.setVersionId(UUID.randomUUID());
        eservice.setEserviceName("e-service1");
        eservice.setPollingEndTime(Time.valueOf("00:00:00"));
        eservice.setPollingStartTime(Time.valueOf("00:00:00"));
        eservice.setBasePath(new String[] {"test1", "test2"});
        eservice.setEserviceType(EserviceType.REST);
        eservice.setPollingFrequency(5);
        eservice.setLastRequest(Timestamp.from(Instant.now()));
        eservice.setProducerName("producer1");
        eservice.setProbingEnabled(true);
        eservice.setResponseReceived(Timestamp.from(Instant.now()));
    }

    @Test
    @DisplayName("e-service methods work")
    void testEserviceEntity_whenValidEserviceData_returnsEservice(){
        System.out.println("ciao " + eservice.getEserviceName());
        Eservice eserviceDuplicate = testEntityManager.persistAndFlush(eservice);
        assertTrue(eserviceDuplicate.getId() > 0);
        assertEquals(eservice.getEserviceId(), eserviceDuplicate.getEserviceId());
        assertEquals(eservice.getEserviceType(), eserviceDuplicate.getEserviceType());
        assertEquals(eservice.getEserviceName(), eserviceDuplicate.getEserviceName());
        assertEquals(eservice.getLastRequest(), eserviceDuplicate.getLastRequest());
        assertEquals(eservice.getBasePath(), eserviceDuplicate.getBasePath());
        assertEquals(eservice.getPollingEndTime(), eserviceDuplicate.getPollingEndTime());
        assertEquals(eservice.getPollingStartTime(), eserviceDuplicate.getPollingStartTime());
        assertEquals(eservice.getPollingFrequency(), eserviceDuplicate.getPollingFrequency());
        assertEquals(eservice.getProducerName(), eserviceDuplicate.getProducerName());
        assertEquals(eservice.getVersionId(), eserviceDuplicate.getVersionId());
        assertEquals(eservice.getResponseReceived(), eserviceDuplicate.getResponseReceived());
        assertEquals(eservice.isProbingEnabled(), eserviceDuplicate.isProbingEnabled());
    }
}