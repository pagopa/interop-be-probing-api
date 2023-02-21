package it.pagopa.interop.probing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.pagopa.interop.probing.dto.EserviceDTO;

@Service
public class BucketServiceImpl implements BucketService {

	@Autowired
	@Qualifier("bucket-s3")
	AmazonS3 s3Client;

	@Autowired
	ObjectMapper objectMapper;

	@Value("${amazon.bucketS3.name}")
	private String bucketName;

	@Value("${amazon.bucketS3.key}")
	private String bucketKey;

	public List<EserviceDTO> readObject() throws Exception {

		List<EserviceDTO> listProva = new ArrayList<>();
		S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, bucketKey));
		listProva = objectMapper.readValue(s3Object.getObjectContent(), new TypeReference<List<EserviceDTO>>() {
		});
		return listProva;
	}

}
