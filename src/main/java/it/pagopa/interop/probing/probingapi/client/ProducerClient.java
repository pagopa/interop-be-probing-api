package it.pagopa.interop.probing.probingapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.pagopa.interop.probing.probingapi.mapping.dto.impl.SearchProducerNameBEResponse;

@FeignClient(name = "producerClient",
    url = "${api.operations.baseUrl}" + "${api.operations.producer.basePath}")
public interface ProducerClient {

  @GetMapping("/")
  ResponseEntity<SearchProducerNameBEResponse> getProducers(
      @RequestParam(value = "limit", required = true) Integer limit,
      @RequestParam(value = "offset", required = true) Integer offset,
      @RequestParam(value = "producerName", required = false) String producerName);
}
