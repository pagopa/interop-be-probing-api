package it.pagopa.interop.probing.probingapi.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import it.pagopa.interop.probing.probingapi.dtos.SearchProducerNameResponse;

@FeignClient(name = "producerClient", url = "${api.operations.baseUrl}" + "producers")
public interface ProducerClient {

  @GetMapping("/")
  ResponseEntity<List<SearchProducerNameResponse>> getProducers(
      @RequestParam(value = "producerName", required = false) String producerName);

}
