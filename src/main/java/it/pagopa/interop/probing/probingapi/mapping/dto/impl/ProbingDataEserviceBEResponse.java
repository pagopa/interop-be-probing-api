package it.pagopa.interop.probing.probingapi.mapping.dto.impl;

import java.time.OffsetDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateBE;
import it.pagopa.interop.probing.probingapi.mapping.dto.Response;
import it.pagopa.interop.probing.probingapi.util.EserviceStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProbingDataEserviceBEResponse implements Response {
  @JsonProperty("probingEnabled")
  @NotNull(message = "must not be null")
  private Boolean probingEnabled;

  @JsonProperty("state")
  @NotNull(message = "must not be null")
  private EserviceStateBE state;

  @JsonProperty("responseReceived")
  @org.springframework.format.annotation.DateTimeFormat(
      iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime responseReceived;

  @JsonProperty("lastRequest")
  @org.springframework.format.annotation.DateTimeFormat(
      iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime lastRequest;

  @JsonProperty("responseStatus")
  private EserviceStatus responseStatus;

  @JsonProperty("pollingFrequency")
  @NotNull(message = "must not be null")
  @Min(value = 1, message = "must be at least 1")
  private Integer pollingFrequency;
}
