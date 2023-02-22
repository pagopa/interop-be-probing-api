package it.pagopa.interop.probing.producer;

import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.pagopa.interop.probing.InteropProbingApplication;
import it.pagopa.interop.probing.dto.EserviceDTO;

@SpringBootTest(classes = InteropProbingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@TestPropertySource(properties = { "spring.quartz.properties.org.quartz.scheduler.skipUpdateCheck = true" })
 class ServicesSendTest {

	@Mock
	private AmazonSQS amazonSQS;

	@Mock
	private ObjectMapper mapper;

	@InjectMocks
	private ServicesSend servicesSend;

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
	@DisplayName("The sendMessage method of ServicesSend class is tested.")
	 void testSendMessage_whenGivenValidEServiceAndUrl_thenProducerWriteOnQueue() throws JsonProcessingException {
		String url = "http://localhost:9324/queue/test-queue";
		SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(url)
				.withMessageBody(mapper.writeValueAsString(eServiceDTO));
		servicesSend.sendMessage(eServiceDTO, url);
		verify(amazonSQS).sendMessage(sendMessageRequest);
	}
}
