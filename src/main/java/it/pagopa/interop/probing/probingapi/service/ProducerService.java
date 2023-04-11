package it.pagopa.interop.probing.probingapi.service;

import java.util.List;
import it.pagopa.interop.probing.probingapi.dtos.SearchProducerNameResponse;

public interface ProducerService {

  /**
   * Get the list of eservices producers.
   *
   * @param producerName the eservice producer name
   * @return the eservices producers
   */
  List<SearchProducerNameResponse> getEservicesProducers(String producerName);
}
