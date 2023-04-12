package it.pagopa.interop.probing.probingapi.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.pagopa.interop.probing.probingapi.client.ProducerClient;
import it.pagopa.interop.probing.probingapi.dtos.SearchProducerNameResponse;
import it.pagopa.interop.probing.probingapi.service.ProducerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProducerServiceImpl implements ProducerService {

  @Autowired
  private ProducerClient producerClient;

  @Override
  public List<SearchProducerNameResponse> getEservicesProducers(String producerName) {
    log.info("Search producer name by producerName: " + producerName);
    return producerClient.getProducers(producerName).getBody();
  }

}
