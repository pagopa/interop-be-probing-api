package it.pagopa.interop.probing.unit.service;

import it.pagopa.interop.probing.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import it.pagopa.interop.probing.model.Eservice;
import it.pagopa.interop.probing.repository.EserviceRepository;
import it.pagopa.interop.probing.service.EserviceService;
import it.pagopa.interop.probing.service.EserviceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EserviceServiceImplTest {

    @Mock
    EserviceRepository eserviceRepository;

    @InjectMocks
    EserviceService service = new EserviceServiceImpl();

    private final EServiceState newState = EServiceState.ACTIVE;
    private final UUID eServiceId = UUID.randomUUID();
    private final UUID versionId = UUID.randomUUID();
    private Eservice testService;

    @BeforeEach
    void setup(){
        testService = new Eservice();
        testService.setState(EServiceState.ACTIVE);
    }

    @Test
    @DisplayName("e-service state correctly updated with new state")
    void testUpdateEserviceState_whenGivenCorrectEserviceIdAndVersionIdAndState_thenEserviceStateIsUpdated() throws EserviceNotFoundException {
        Mockito.when(eserviceRepository.findByEserviceIdAndVersionId(eServiceId, versionId))
                .thenReturn(Optional.of(testService));
        Mockito.when(eserviceRepository.save(Mockito.any(Eservice.class))).thenReturn(testService);

        service.updateEserviceState(eServiceId, versionId, EServiceState.INACTIVE);
        Assertions.assertEquals(EServiceState.INACTIVE, testService.getState(),
                () -> "e-service state should be INACTIVE");
    }

    @Test
    @DisplayName("e-service to update state not found")
    void testUpdateEserviceState_whenNoEServiceIsFound_thenThrowsNotFoundException() throws EserviceNotFoundException {
        Mockito.when(eserviceRepository.findByEserviceIdAndVersionId(eServiceId, versionId))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EserviceNotFoundException.class, () ->{
            service.updateEserviceState(eServiceId, versionId, EServiceState.INACTIVE);
        }, "e-service should not be found and an EserviceNotFoundException should be thrown");
    }
}