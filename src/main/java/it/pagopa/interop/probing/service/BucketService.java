package it.pagopa.interop.probing.service;

import java.util.List;

import it.pagopa.interop.probing.dto.EserviceDTO;

public interface BucketService {
	
	List<EserviceDTO> readObject();
}
