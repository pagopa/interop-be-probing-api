package it.pagopa.interop.probing.unit.model;

import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import it.pagopa.interop.probing.model.Eservice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
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
        eservice.setBasePath(new String[]{"test", "test"});
        eservice.setPollingEndTime(Time.valueOf("00:00:00"));
        eservice.setPollingStartTime(Time.valueOf("00:00:00"));
        eservice.setPollingFrequency(5);
        eservice.setLastRequest(Timestamp.from(Instant.now()));
        eservice.setProducerName("producer1");
        eservice.setProbingEnabled(true);
        eservice.setResponseReceived(Timestamp.from(Instant.now()));
    }

    @Test
    @DisplayName("e-service methods work")
    void testEserviceEntity_whenValidEserviceData_returnsEservice(){
        Eservice eserviceDuplicate = testEntityManager.persistAndFlush(eservice);
        Assertions.assertTrue(eserviceDuplicate.getId() > 0);
        Assertions.assertEquals(eservice.getEserviceId(), eserviceDuplicate.getEserviceId());
        Assertions.assertEquals(eservice.getEserviceType(), eserviceDuplicate.getEserviceType());
        Assertions.assertEquals(eservice.getEserviceName(), eserviceDuplicate.getEserviceName());
        Assertions.assertEquals(eservice.getBasePath(), eserviceDuplicate.getBasePath());
        Assertions.assertEquals(eservice.getLastRequest(), eserviceDuplicate.getLastRequest());
        Assertions.assertEquals(eservice.getPollingEndTime(), eserviceDuplicate.getPollingEndTime());
        Assertions.assertEquals(eservice.getPollingStartTime(), eserviceDuplicate.getPollingStartTime());
        Assertions.assertEquals(eservice.getPollingFrequency(), eserviceDuplicate.getPollingFrequency());
        Assertions.assertEquals(eservice.getProducerName(), eserviceDuplicate.getProducerName());
        Assertions.assertEquals(eservice.getVersionId(), eserviceDuplicate.getVersionId());
        Assertions.assertEquals(eservice.getResponseReceived(), eserviceDuplicate.getResponseReceived());
        Assertions.assertEquals(eservice.isProbingEnabled(), eserviceDuplicate.isProbingEnabled());
    }
}