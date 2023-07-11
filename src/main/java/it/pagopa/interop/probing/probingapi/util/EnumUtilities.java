package it.pagopa.interop.probing.probingapi.util;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateBE;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateFE;

@Component
public class EnumUtilities {

  @Value("${toleranceMultiplierInMinutes}")
  private int toleranceMultiplierInMinutes;


  public EserviceStateFE fromPdndToMonitorState(EserviceStatus status, EserviceStateBE state,
      boolean probingEnabled, OffsetDateTime responseReceived, OffsetDateTime lastRequest,
      Integer pollingFrequency) {
    return switch (state) {
      case ACTIVE -> checkND(probingEnabled, responseReceived, lastRequest, pollingFrequency)
          ? EserviceStateFE.N_D
          : checkOFFLINE(status) ? EserviceStateFE.OFFLINE : EserviceStateFE.ONLINE;
      case INACTIVE -> checkND(probingEnabled, responseReceived, lastRequest, pollingFrequency)
          ? EserviceStateFE.N_D
          : EserviceStateFE.OFFLINE;
      default -> throw new IllegalArgumentException("Invalid state {}= " + state);
    };
  }

  public boolean isActive(EserviceStateBE viewState) {
    return viewState.equals(EserviceStateBE.ACTIVE);
  }

  public boolean checkND(boolean probingEnabled, OffsetDateTime responseReceived,
      OffsetDateTime lastRequest, Integer pollingFrequency) {
    return (!probingEnabled || Objects.isNull(lastRequest)
        || (isBeenToLongRequest(lastRequest, pollingFrequency)
            && isResponseReceivedBeforeLastRequest(responseReceived, lastRequest))
        || Objects.isNull(responseReceived));
  }

  public boolean checkOFFLINE(EserviceStatus status) {
    return Objects.nonNull(status) && status.equals(EserviceStatus.KO);
  }

  private boolean isResponseReceivedBeforeLastRequest(OffsetDateTime responseReceived,
      OffsetDateTime lastRequest) {
    OffsetDateTime defaultDate =
        Objects.isNull(responseReceived) ? OffsetDateTime.MAX : responseReceived;

    return defaultDate.isBefore(lastRequest);
  }

  private boolean isBeenToLongRequest(OffsetDateTime lastRequest, Integer pollingFrequency) {
    return Duration
        .between(lastRequest.withOffsetSameInstant(ZoneOffset.UTC),
            OffsetDateTime.now(ZoneOffset.UTC))
        .toMinutes() > (Long.valueOf(pollingFrequency * toleranceMultiplierInMinutes));
  }

}
