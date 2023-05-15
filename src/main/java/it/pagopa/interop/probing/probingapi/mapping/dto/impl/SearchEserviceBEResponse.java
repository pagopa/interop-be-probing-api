package it.pagopa.interop.probing.probingapi.mapping.dto.impl;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.interop.probing.probingapi.mapping.dto.Response;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchEserviceBEResponse implements Response {
  @JsonProperty("probingEnabled")
  private Boolean probingEnabled;

  @JsonProperty("content")
  @NotNull(message = "must not be null")
  @Valid
  private List<SearchEserviceBEContent> content;

  @JsonProperty("offset")
  private Integer offset;

  @JsonProperty("limit")
  private Integer limit;

  @JsonProperty("totalElements")
  @NotNull(message = "must not be null")
  private Long totalElements;
}
