package it.pagopa.interop.probing.probingapi.util.logging.impl;

import java.time.OffsetTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateBE;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateFE;
import it.pagopa.interop.probing.probingapi.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class LoggerImpl implements Logger {

  @Override
  public void logMessageEserviceStateUpdated(UUID eserviceId, UUID versionId,
      EserviceStateBE eServiceState) {
    log.info(
        "e-service has been updated to new state. eserviceId={}, versionId={}, eserviceState={}",
        eserviceId, versionId, eServiceState.toString());
  }

  @Override
  public void logMessageEserviceProbingStateUpdated(UUID eserviceId, UUID versionId,
      boolean probingEnabled) {
    log.info(
        "e-service probing state has been updated to new state. eserviceId={}, versionId={}, probingEnabled={}",
        eserviceId, versionId, probingEnabled);
  }

  @Override
  public void logMessageEservicePollingConfigUpdated(UUID eserviceId, UUID versionId,
      OffsetTime startTime, OffsetTime endTime, int frequency) {
    log.info(
        "e-service polling data have been updated. eserviceId={}, versioneId={}, startTime={}, endTime={}, frequency={}",
        eserviceId, versionId, startTime.toString(), endTime.toString(), frequency);
  }

  @Override
  public void logMessageSearchProducer(String producerName) {
    log.info("Searching producer, producerName={}", producerName);
  }

  @Override
  public void logMessageSearchEservice(Integer limit, Integer offset, String eserviceName,
      String producerName, Integer versionNumber, List<EserviceStateFE> state) {
    log.info(
        "Searching e-services, limit={}, offset={}, producerName={}, eserviceName={}, versionNumber={}, stateList={}",
        limit, offset, producerName, eserviceName, versionNumber,
        Arrays.toString(Objects.isNull(state) ? null : state.toArray()));
  }

  @Override
  public void logMessageException(Exception exception) {
    log.error(ExceptionUtils.getStackTrace(exception));
  }

  @Override
  public void logMessageGetEserviceMainData(Long eserviceRecordId) {
    log.info("Getting eservice main data. eserviceRecordId={}", eserviceRecordId);
  }

  @Override
  public void logMessageGetEserviceProbingData(Long eserviceRecordId) {
    log.info("Getting eservice probing data. eserviceRecordId={}", eserviceRecordId);
  }
}
