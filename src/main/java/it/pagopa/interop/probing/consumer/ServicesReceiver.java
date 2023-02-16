package it.pagopa.interop.probing.consumer;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import it.pagopa.interop.probing.dto.EserviceDTO;
import lombok.extern.slf4j.Slf4j;


/** The Constant log. */
@Slf4j
@Component
public class ServicesReceiver {

	/** The mapper. */
	@Autowired
	ObjectMapper mapper;
	
	/**
	 * Receive string message from a sqs queue which will be used for the services update/storage .
	 *
	 * @param message the message that contains the services
	 * @throws TemplateException 
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws Exception 
	 */
	@SqsListener(value = "${amazon.sqs.end-point.services-queue}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void receiveStringMessage(final String message) throws NoSuchAlgorithmException, IOException {
		EserviceDTO service = mapper.readValue(message, EserviceDTO.class);
		log.info(service.getEserviceId());
		}
	}
