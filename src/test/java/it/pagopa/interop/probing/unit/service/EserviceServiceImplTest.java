package it.pagopa.interop.probing.unit.service;

import it.pagopa.interop.probing.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import it.pagopa.interop.probing.mapstruct.dto.UpdateEserviceStateDto;
import it.pagopa.interop.probing.model.Eservice;
import it.pagopa.interop.probing.repository.EserviceRepository;
import it.pagopa.interop.probing.service.EserviceService;
import it.pagopa.interop.probing.service.EserviceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EserviceServiceImplTest {

    @Mock
    EserviceRepository eserviceRepository;

    @InjectMocks
    EserviceService service = new EserviceServiceImpl();
    private final UUID eServiceId = UUID.randomUUID();
    private final UUID versionId = UUID.randomUUID();
    private Eservice testService;
    private UpdateEserviceStateDto updateEserviceStateDto;

    @BeforeEach
    void setup(){
        testService = new Eservice();
        testService.setState(EServiceState.ACTIVE);
        updateEserviceStateDto = new UpdateEserviceStateDto();
        updateEserviceStateDto.setEserviceId(eServiceId);
        updateEserviceStateDto.setVersionId(versionId);
        updateEserviceStateDto.setNewEServiceState(EServiceState.fromValue("INACTIVE"));
    }

    @Test
    @DisplayName("e-service state correctly updated with new state")
    void testUpdateEserviceState_whenGivenCorrectEserviceIdAndVersionIdAndState_thenEserviceStateIsUpdated() throws EserviceNotFoundException {
        Mockito.when(eserviceRepository.findByEserviceIdAndVersionId(eServiceId, versionId))
                .thenReturn(Optional.of(testService));
        Mockito.when(eserviceRepository.save(Mockito.any(Eservice.class))).thenReturn(testService);

        service.updateEserviceState(updateEserviceStateDto);
        assertEquals(EServiceState.INACTIVE, testService.getState(),
                () -> "e-service state should be INACTIVE");
    }

    @Test
    @DisplayName("e-service to update state not found")
    void testUpdateEserviceState_whenNoEServiceIsFound_thenThrowsNotFoundException() {
        Mockito.when(eserviceRepository.findByEserviceIdAndVersionId(eServiceId, versionId))
                .thenReturn(Optional.empty());

        assertThrows(EserviceNotFoundException.class, () ->{
            service.updateEserviceState(updateEserviceStateDto);
        }, "e-service should not be found and an EserviceNotFoundException should be thrown");
    }
}