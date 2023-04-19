package it.pagopa.interop.probing.probingapi.util.logging;

import it.pagopa.interop.probing.probingapi.dtos.EserviceStateBE;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateFE;

import java.time.OffsetTime;
import java.util.List;
import java.util.UUID;

public interface Logger {

  void logMessageEserviceStateUpdated(UUID eserviceId, UUID versionId,
      EserviceStateBE eServiceState);

  void logMessageEserviceProbingStateUpdated(UUID eserviceId, UUID versionId,
      boolean probingEnabled);

  void logMessageEservicePollingConfigUpdated(UUID eserviceId, UUID versionId, OffsetTime startTime,
      OffsetTime endTime, int frequency);

  void logMessageSearchProducer(String producerName);

  void logMessageSearchEservice(Integer limit, Integer offset, String eserviceName,
      String producerName, Integer versionNumber, List<EserviceStateFE> state);
}
