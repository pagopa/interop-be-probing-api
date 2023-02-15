package it.pagopa.interop.probing.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.pagopa.interop.probing.dto.EserviceDTO;


/**
 * The Class DowntimeLogsSend.
 */
@Service
public class ServicesSend {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ServicesSend.class);

    /** The amazon SQS. */
    @Autowired
    @Qualifier("services")
    private AmazonSQS amazonSQS;


    /** The object mapper. */
    @Autowired
    private ObjectMapper objectMapper;
    
	/**
	 * Send a new message to the sqs queue which will be used for the legal fact generation.
	 *
	 * @param downtimeLogs the downtime logs which will be used for the creation of the message
	 * @param url the url of the sqs queue
	 * @throws JsonProcessingException the json processing exception
	 */
	public void sendMessage(EserviceDTO service, String url) throws JsonProcessingException {
		SendMessageRequest sendMessageRequest = null;
            sendMessageRequest = new SendMessageRequest().withQueueUrl(url)
                    .withMessageBody(objectMapper.writeValueAsString(service));
            amazonSQS.sendMessage(sendMessageRequest);
            LOGGER.info("Service has been published in SQS.");
	}
}
