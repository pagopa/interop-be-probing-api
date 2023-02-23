package it.pagopa.interop.probing.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.pagopa.interop.probing.dto.EserviceDTO;
import it.pagopa.interop.probing.service.BucketService;
import it.pagopa.interop.probing.service.BucketServiceImpl;

@SpringBootTest
 class BucketServiceImplTest {

	@Mock
	private AmazonS3 s3Client;
	
	@InjectMocks
	private BucketService bucketServiceImpl = new BucketServiceImpl();
	
	List<EserviceDTO> listProva;
	
	@Mock
	ObjectMapper mockObjectMapper;
	
	@BeforeEach
    void setup(){
		listProva = new ArrayList<>();
		EserviceDTO eServiceDTO = new EserviceDTO();
		eServiceDTO.setEserviceId(UUID.randomUUID().toString());
    	eServiceDTO.setVersionId(UUID.randomUUID().toString());
    	eServiceDTO.setName("Service Name");
    	eServiceDTO.setProducerName("Producer Name");
    	eServiceDTO.setState("ACTIVE");
    	eServiceDTO.setType("REST");
		String[] basePath = { "xxx.xxx/xxx", "yyy.yyy/xxx" };
		eServiceDTO.setBasePath(basePath);
		listProva.add(eServiceDTO);
    }
	
	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("readObject method is executed")
	 void testReadObject_whenGivenValidS3Object_thenReturnValidObjectList() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		
		String stringList = objectMapper.writeValueAsString(listProva);
		
		S3Object s3Object = new S3Object();
		s3Object.setBucketName("bucket-name-test");
		s3Object.setKey("bucket-key-test");

		InputStream targetStream = new ByteArrayInputStream(stringList.getBytes());
		s3Object.setObjectContent(targetStream);

		Mockito.when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(s3Object);
		Mockito.when(mockObjectMapper.readValue(Mockito.any(S3ObjectInputStream.class), Mockito.any(TypeReference.class))).thenReturn(listProva);
		List<EserviceDTO> resp = bucketServiceImpl.readObject();
		assertEquals(listProva.size(), resp.size());
		assertEquals(stringList, objectMapper.writeValueAsString(resp));
	}

}
