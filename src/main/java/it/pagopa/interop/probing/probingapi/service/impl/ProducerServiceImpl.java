package it.pagopa.interop.probing.probingapi.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import it.pagopa.interop.probing.probingapi.client.ProducerClient;
import it.pagopa.interop.probing.probingapi.dtos.SearchProducerNameResponse;
import it.pagopa.interop.probing.probingapi.mapping.dto.impl.SearchProducerNameBEResponse;
import it.pagopa.interop.probing.probingapi.service.ProducerService;
import it.pagopa.interop.probing.probingapi.util.logging.Logger;


@Service
public class ProducerServiceImpl implements ProducerService {

  @Autowired
  private Logger logger;

  @Autowired
  private ProducerClient producerClient;

  @Override
  public List<SearchProducerNameResponse> getEservicesProducers(Integer limit, Integer offset,
      String producerName) {
    logger.logMessageSearchProducer(producerName);
    SearchProducerNameBEResponse response =
        producerClient.getProducers(limit, offset, producerName).getBody();

    return response.getContent().stream().map(name -> {
          return SearchProducerNameResponse.builder().label(name).value(name).build();
        }).toList();
  }
}
