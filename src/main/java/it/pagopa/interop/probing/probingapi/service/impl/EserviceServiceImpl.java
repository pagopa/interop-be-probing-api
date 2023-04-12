package it.pagopa.interop.probing.probingapi.service.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.pagopa.interop.probing.probingapi.client.EserviceClient;
import it.pagopa.interop.probing.probingapi.client.ProducerClient;
import it.pagopa.interop.probing.probingapi.dtos.ChangeEserviceStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingFrequencyRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateFE;
import it.pagopa.interop.probing.probingapi.dtos.SearchEserviceResponse;
import it.pagopa.interop.probing.probingapi.dtos.SearchProducerNameResponse;
import it.pagopa.interop.probing.probingapi.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.probingapi.service.EserviceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EserviceServiceImpl implements EserviceService {

  @Autowired
  private EserviceClient eserviceClient;

  @Autowired
  private ProducerClient producerClient;

  @Override
  public void updateEserviceState(UUID eserviceId, UUID versionId,
      ChangeEserviceStateRequest changeEserviceStateRequest) throws EserviceNotFoundException {
    operationsClient.updateEserviceState(eserviceId, versionId, changeEserviceStateRequest);
    log.info("EserviceState of eservice " + eserviceId + " with version " + versionId
        + " has been updated into " + changeEserviceStateRequest.geteServiceState());
  }

  @Override
  public void updateEserviceProbingState(UUID eserviceId, UUID versionId,
      ChangeProbingStateRequest changeProbingStateRequest) throws EserviceNotFoundException {
    operationsClient.updateEserviceProbingState(eserviceId, versionId, changeProbingStateRequest);
    log.info("EserviceProbingState of eservice " + eserviceId + " with version " + versionId
        + " has been updated into " + changeProbingStateRequest.getProbingEnabled());
  }

  @Override
  public void updateEserviceFrequency(UUID eserviceId, UUID versionId,
      ChangeProbingFrequencyRequest changeProbingFrequencyRequest)
      throws EserviceNotFoundException {
    operationsClient.updateEserviceFrequency(eserviceId, versionId, changeProbingFrequencyRequest);
    log.info("Eservice " + eserviceId + " with version " + versionId
        + " has been updated with startTime: " + changeProbingFrequencyRequest.getStartTime()
        + " and endTime: " + changeProbingFrequencyRequest.getEndTime() + " and frequency: "
        + changeProbingFrequencyRequest.getFrequency());
  }

  @Override
  public List<SearchProducerNameResponse> getEservicesProducers(String producerName) {
    log.info("Search producer name by producerName: " + producerName);
    return producerClient.getProducers(producerName).getBody();
  }

  @Override
  public SearchEserviceResponse searchEservices(Integer limit, Integer offset, String eserviceName,
      String producerName, Integer versionNumber, List<EserviceStateFE> state) {
    log.info("Search eservice by filters -> limit:" + limit + ", offset:" + offset
        + ", producerName:" + producerName + ", eserviceName:" + eserviceName + ", versionNumber:"
        + versionNumber + ", stateList:" + state);
    return operationsClient
        .searchEservices(limit, offset, eserviceName, producerName, versionNumber, state).getBody();
  }
}
