package it.pagopa.interop.probing.probingapi.service.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import it.pagopa.interop.probing.probingapi.client.EserviceClient;
import it.pagopa.interop.probing.probingapi.dtos.ChangeEserviceStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingFrequencyRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateFE;
import it.pagopa.interop.probing.probingapi.dtos.MainDataEserviceResponse;
import it.pagopa.interop.probing.probingapi.dtos.ProbingDataEserviceResponse;
import it.pagopa.interop.probing.probingapi.dtos.SearchEserviceResponse;
import it.pagopa.interop.probing.probingapi.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.probingapi.mapping.dto.impl.SearchEserviceBEResponse;
import it.pagopa.interop.probing.probingapi.mapping.mapper.AbstractMapper;
import it.pagopa.interop.probing.probingapi.service.EserviceService;
import it.pagopa.interop.probing.probingapi.util.logging.Logger;

@Service
@XRayEnabled
public class EserviceServiceImpl implements EserviceService {

  @Autowired
  private Logger logger;
  @Autowired
  private EserviceClient eserviceClient;

  @Autowired
  private AbstractMapper mapper;

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
  public SearchEserviceResponse searchEservices(Integer limit, Integer offset, String eserviceName,
      String producerName, Integer versionNumber, List<EserviceStateFE> state) {
    logger.logMessageSearchEservice(limit, offset, producerName, eserviceName, versionNumber,
        state);
    SearchEserviceBEResponse response = eserviceClient
        .searchEservices(limit, offset, eserviceName, producerName, versionNumber, state).getBody();
    return SearchEserviceResponse.builder()
        .content(!CollectionUtils.isEmpty(response.getContent())
            ? response.getContent().stream().map(e -> mapper.toSearchEserviceContent(e)).toList()
            : List.of())
        .totalElements(response.getTotalElements()).build();
  }

  @Override
  public MainDataEserviceResponse getEserviceMainData(Long eserviceRecordId)
      throws EserviceNotFoundException {
    logger.logMessageGetEserviceMainData(eserviceRecordId);
    return eserviceClient.getEserviceMainData(eserviceRecordId).getBody();
  }

  @Override
  public ProbingDataEserviceResponse getEserviceProbingData(Long eserviceRecordId)
      throws EserviceNotFoundException {
    logger.logMessageGetEserviceProbingData(eserviceRecordId);
    return mapper.toProbingDataEserviceResponse(
        eserviceClient.getEserviceProbingData(eserviceRecordId).getBody());
  }
}
