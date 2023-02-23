package it.pagopa.interop.probing.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import it.pagopa.interop.probing.dto.EserviceDTO;
import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import it.pagopa.interop.probing.model.Eservice;
import it.pagopa.interop.probing.repository.EserviceRepository;
import it.pagopa.interop.probing.service.EserviceService;
import it.pagopa.interop.probing.service.EserviceServiceImpl;

@SpringBootTest
 class EserviceServiceImplTest {

	@InjectMocks
	private EserviceService serviceImpl = new EserviceServiceImpl();

	@Mock
	EserviceRepository eserviceRepository;
	
	EserviceDTO serviceDTO;
	
    @BeforeEach
    void setup(){
    	serviceDTO = new EserviceDTO();
		serviceDTO.setEserviceId(UUID.randomUUID().toString());
		serviceDTO.setVersionId(UUID.randomUUID().toString());
		serviceDTO.setName("Service name");
		serviceDTO.setProducerName("Producer name");
		serviceDTO.setState("ACTIVE");
		serviceDTO.setType("REST");
		String[] basePathDTO = { "xxx.xxx/xxx", "yyy.yyy/xxx" };
		serviceDTO.setBasePath(basePathDTO);
    }
    
	
	@Test
	@DisplayName("The saveEservice method is executed if the Eservice doesn't exist")
	void testSaveEservice_whenDoesntExist_GivenValidEService_thenSaveEntityAndReturnId() {
		Mockito.when(eserviceRepository.findByEserviceIdAndVersionId(Mockito.any(), Mockito.any())).thenReturn(null);
		Eservice service = getEserviceEntity(serviceDTO.getEserviceId(), serviceDTO.getVersionId());
		Mockito.when(eserviceRepository.save(any(Eservice.class))).thenReturn(service);
		Long resultId = serviceImpl.saveService(serviceDTO);
		
		assertEquals(resultId, service.getId());
	}
	
	private Eservice getEserviceEntity(String eserviceId, String versionId) {
		Eservice serviceEntity = new Eservice();
    	serviceEntity.setId(10L);
    	serviceEntity.setState(EServiceState.INACTIVE);
    	serviceEntity.setEserviceId(UUID.fromString(eserviceId));
    	serviceEntity.setVersionId(UUID.fromString(versionId));
    	serviceEntity.setEserviceName("E-Service name");
    	serviceEntity.setBasePath(new String[]{"test-BasePath-1", "test-BasePath-2"});
    	serviceEntity.setPollingEndTime(Time.valueOf("00:00:00"));
    	serviceEntity.setPollingStartTime(Time.valueOf("00:00:00"));
    	serviceEntity.setPollingFrequency(5);
    	serviceEntity.setLastRequest(Timestamp.from(Instant.now()));
    	serviceEntity.setProducerName("Producer name");
    	serviceEntity.setProbingEnabled(true);
    	serviceEntity.setResponseReceived(Timestamp.from(Instant.now()));
    	return serviceEntity;
	}
}
