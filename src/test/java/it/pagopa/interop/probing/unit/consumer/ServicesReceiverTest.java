package it.pagopa.interop.probing.unit.consumer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.pagopa.interop.probing.consumer.ServicesReceiver;
import it.pagopa.interop.probing.dto.EserviceDTO;
import it.pagopa.interop.probing.service.EserviceService;

@SpringBootTest
 class ServicesReceiverTest {

	@Mock
	private AmazonSQS amazonSQS;

	@Mock
	private ObjectMapper mapper;

	@InjectMocks
	private ServicesReceiver servicesReceiver;

	@Mock
	EserviceService eserviceService;

	EserviceDTO eServiceDTO;

    @BeforeEach
    void setup(){
    	eServiceDTO = new EserviceDTO();
    	eServiceDTO.setEserviceId(UUID.randomUUID().toString());
    	eServiceDTO.setVersionId(UUID.randomUUID().toString());
    	eServiceDTO.setName("Service Name");
    	eServiceDTO.setProducerName("Producer Name");
    	eServiceDTO.setState("ACTIVE");
    	eServiceDTO.setType("REST");
		String[] basePath = { "basePath1", "basePath2" };
		eServiceDTO.setBasePath(basePath);
    }
    
	@Test
	@DisplayName("The receiveStringMessage method is executed.")
	 void testReceiveStringMessage_whenGivenValidMessage_thenSaveEServiceEntity() throws NoSuchAlgorithmException, IOException {
		Mockito.when(mapper.readValue(anyString(), ArgumentMatchers.<Class<EserviceDTO>>any())).thenReturn(eServiceDTO);
		Mockito.when(eserviceService.saveService(Mockito.any())).thenReturn(10L);
		servicesReceiver.receiveStringMessage(eServiceDTO.toString());
		verify(mapper, times(1)).readValue(eServiceDTO.toString(), EserviceDTO.class);
	}
}
