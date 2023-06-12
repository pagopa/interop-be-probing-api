package it.pagopa.interop.probing.probingapi.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import it.pagopa.interop.probing.probingapi.api.ProducersApi;
import it.pagopa.interop.probing.probingapi.dtos.SearchProducerNameResponse;
import it.pagopa.interop.probing.probingapi.service.ProducerService;

@RestController
public class ProducerController implements ProducersApi {

  @Autowired
  private ProducerService producerService;

  @Override
  public ResponseEntity<List<SearchProducerNameResponse>> getEservicesProducers(Integer limit,
      Integer offset, String producerName) {
    return ResponseEntity.ok(producerService.getEservicesProducers(limit, offset, producerName));
  }
}
