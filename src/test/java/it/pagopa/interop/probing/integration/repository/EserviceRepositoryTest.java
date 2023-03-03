package it.pagopa.interop.probing.integration.repository;

import it.pagopa.interop.probing.interop_be_probing_api.model.EserviceState;
import it.pagopa.interop.probing.model.Eservice;
import it.pagopa.interop.probing.repository.EserviceRepository;
import it.pagopa.interop.probing.util.EserviceTechnology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
        eservice.setPollingEndTime(OffsetDateTime.of(2023, 12, 12, 1, 0, 0,
                0, ZoneOffset.UTC));
        eservice.setPollingStartTime(OffsetDateTime.of(2023, 12, 12, 1, 0, 0,
                0, ZoneOffset.UTC));
        eservice.setBasePath(new String[] {"test1", "test2"});
        eservice.setTechnology(EserviceTechnology.REST);
        eservice.setPollingFrequency(5);
        eservice.setProducerName("producer1");
        eservice.setProbingEnabled(true);
        eservice.setState(EserviceState.ACTIVE);
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