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
import it.pagopa.interop.probing.probingapi.dtos.MainDataEserviceResponse;
import it.pagopa.interop.probing.probingapi.dtos.SearchEserviceResponse;
import it.pagopa.interop.probing.probingapi.dtos.SearchProducerNameResponse;
import it.pagopa.interop.probing.probingapi.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.probingapi.service.EserviceService;
import it.pagopa.interop.probing.probingapi.util.logging.Logger;

@Service
public class EserviceServiceImpl implements EserviceService {

  @Autowired
  private Logger logger;
  @Autowired
  private EserviceClient eserviceClient;

  @Autowired
  private ProducerClient producerClient;

  @Override
  public void updateEserviceState(UUID eserviceId, UUID versionId,
      ChangeEserviceStateRequest changeEserviceStateRequest) throws EserviceNotFoundException {
    eserviceClient.updateEserviceState(eserviceId, versionId, changeEserviceStateRequest);
    logger.logMessageEserviceStateUpdated(eserviceId, versionId,
        changeEserviceStateRequest.geteServiceState());
  }

  @Override
  public void updateEserviceProbingState(UUID eserviceId, UUID versionId,
      ChangeProbingStateRequest changeProbingStateRequest) throws EserviceNotFoundException {
    eserviceClient.updateEserviceProbingState(eserviceId, versionId, changeProbingStateRequest);
    logger.logMessageEserviceProbingStateUpdated(eserviceId, versionId,
        changeProbingStateRequest.getProbingEnabled());
  }

  @Override
  public void updateEserviceFrequency(UUID eserviceId, UUID versionId,
      ChangeProbingFrequencyRequest changeProbingFrequencyRequest)
      throws EserviceNotFoundException {
    eserviceClient.updateEserviceFrequency(eserviceId, versionId, changeProbingFrequencyRequest);
    logger.logMessageEservicePollingConfigUpdated(eserviceId, versionId,
        changeProbingFrequencyRequest.getStartTime(), changeProbingFrequencyRequest.getEndTime(),
        changeProbingFrequencyRequest.getFrequency());
  }

  @Override
  public List<SearchProducerNameResponse> getEservicesProducers(String producerName) {
    logger.logMessageSearchProducer(producerName);
    return producerClient.getProducers(producerName).getBody();
  }

  @Override
  public SearchEserviceResponse searchEservices(Integer limit, Integer offset, String eserviceName,
      String producerName, Integer versionNumber, List<EserviceStateFE> state) {
    logger.logMessageSearchEservice(limit, offset, producerName, eserviceName, versionNumber,
        state);
    return eserviceClient
        .searchEservices(limit, offset, eserviceName, producerName, versionNumber, state).getBody();
  }

  @Override
  public MainDataEserviceResponse getEserviceMainData(Long eserviceRecordId)
      throws EserviceNotFoundException {
    logger.logMessageGetEserviceMainData(eserviceRecordId);
    return eserviceClient.getEserviceMainData(eserviceRecordId).getBody();
  }
}
