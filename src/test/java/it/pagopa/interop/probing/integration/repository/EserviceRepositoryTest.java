package it.pagopa.interop.probing.integration.repository;

import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import it.pagopa.interop.probing.model.Eservice;
import it.pagopa.interop.probing.repository.EserviceRepository;
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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EserviceRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EserviceRepository eserviceRepository;

    private final UUID eServiceId = UUID.randomUUID();
    private final UUID versionId = UUID.randomUUID();

    @BeforeEach
    void setup() {
        Eservice eservice = new Eservice();
        eservice.setEserviceId(eServiceId);
        eservice.setVersionId(versionId);
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
        eservice.setState(EServiceState.ACTIVE);
        testEntityManager.persistAndFlush(eservice);
    }

    @DisplayName("Find e-service by correct eserviceId and versionId")
    @Test
    void testFindByEserviceIdAndVersionId_whenGivenCorrectEserviceIdAndVersionId_ReturnsEserviceEntity() {
        Optional<Eservice> queryResult = eserviceRepository.findByEserviceIdAndVersionId(eServiceId, versionId);

        assertNotNull(queryResult.get(), "e-service object shouldn't be null");
    }

    @DisplayName("No e-service found with incorrect eserviceId and versionId")
    @Test
    void testFindByEserviceIdAndVersionId_whenGivenIncorrectEserviceIdAndVersionId_ReturnsNoEntity() {
        final UUID wrongEServiceId = UUID.randomUUID();
        final UUID wrongVersionId = UUID.randomUUID();
        Optional<Eservice> queryResult = eserviceRepository.findByEserviceIdAndVersionId(wrongEServiceId, wrongVersionId);

        assertThrows(NoSuchElementException.class, () -> {
            queryResult.get();
        }, "There should be no e-service with eserviceId " + wrongVersionId + "and versionId " + wrongVersionId);
    }
}