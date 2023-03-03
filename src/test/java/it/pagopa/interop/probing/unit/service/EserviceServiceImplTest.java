package it.pagopa.interop.probing.unit.service;

import it.pagopa.interop.probing.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.interop_be_probing_api.model.EserviceState;
import it.pagopa.interop.probing.mapstruct.dto.UpdateEserviceProbingStateDto;
import it.pagopa.interop.probing.mapstruct.dto.UpdateEserviceStateDto;
import it.pagopa.interop.probing.model.Eservice;
import it.pagopa.interop.probing.repository.EserviceRepository;
import it.pagopa.interop.probing.service.EserviceService;
import it.pagopa.interop.probing.service.EserviceServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
class EserviceServiceImplTest {

    @Mock
    EserviceRepository eserviceRepository;

    @InjectMocks
    EserviceService service = new EserviceServiceImpl();
    private final UUID eServiceId = UUID.randomUUID();
    private final UUID versionId = UUID.randomUUID();
    private Eservice testService;
    private UpdateEserviceStateDto updateEserviceStateDto;

    private UpdateEserviceProbingStateDto updateEserviceProbingStateDto;

    @BeforeEach
    void setup(){
        testService = new Eservice();
        testService.setState(EserviceState.ACTIVE);
        updateEserviceStateDto = new UpdateEserviceStateDto();
        updateEserviceStateDto.setEserviceId(eServiceId);
        updateEserviceStateDto.setVersionId(versionId);
        updateEserviceStateDto.setNewEServiceState(EserviceState.fromValue("INACTIVE"));

        updateEserviceProbingStateDto = new UpdateEserviceProbingStateDto();
        updateEserviceProbingStateDto.setProbingEnabled(false);
        updateEserviceProbingStateDto.setEserviceId(eServiceId);
        updateEserviceProbingStateDto.setVersionId(versionId);
    }

    @Test
    @DisplayName("e-service state correctly updated with new state")
    void testUpdateEserviceState_whenGivenCorrectEserviceIdAndVersionIdAndState_thenEserviceStateIsUpdated() throws EserviceNotFoundException {
        Mockito.when(eserviceRepository.findByEserviceIdAndVersionId(eServiceId, versionId))
                .thenReturn(Optional.of(testService));
        Mockito.when(eserviceRepository.save(Mockito.any(Eservice.class))).thenReturn(testService);
        service.updateEserviceState(updateEserviceStateDto);
        assertEquals(EserviceState.INACTIVE, testService.getState(), "e-service state should be INACTIVE");
    }

    @Test
    @DisplayName("e-service to update state of not found")
    void testUpdateEserviceState_whenNoEServiceIsFound_thenThrowsException() {
        Mockito.when(eserviceRepository.findByEserviceIdAndVersionId(eServiceId, versionId))
                .thenReturn(Optional.empty());
        assertThrows(EserviceNotFoundException.class, () -> service.updateEserviceState(updateEserviceStateDto),
                "e-service should not be found and an EserviceNotFoundException should be thrown");
    }

    @Test
    @DisplayName("e-service probing gets enabled")
    void testEserviceProbingState_whenGivenCorrectEserviceIdAndVersionId_thenEserviceProbingIsEnabled() throws EserviceNotFoundException {
        Mockito.when(eserviceRepository.findByEserviceIdAndVersionId(eServiceId, versionId))
                .thenReturn(Optional.of(testService));
        Mockito.when(eserviceRepository.save(Mockito.any(Eservice.class))).thenReturn(testService);
        updateEserviceProbingStateDto.setProbingEnabled(true);
        service.updateEserviceProbingState(updateEserviceProbingStateDto);
        assertTrue(testService.isProbingEnabled(), "e-service probing should be enabled");
    }

    @Test
    @DisplayName("e-service to update probing state of not found")
    void testEserviceProbingState_whenNoEServiceIsFound_thenThrowsException() {
        Mockito.when(eserviceRepository.findByEserviceIdAndVersionId(eServiceId, versionId))
                .thenReturn(Optional.empty());
        assertThrows(EserviceNotFoundException.class,
                () -> service.updateEserviceProbingState(updateEserviceProbingStateDto),
                "e-service should not be found and an EserviceNotFoundException should be thrown");
    }
}