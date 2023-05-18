package it.pagopa.interop.probing.probingapi.mapping.dto.impl;

import java.util.List;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.interop.probing.probingapi.mapping.dto.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchProducerNameBEResponse implements Response {

  private static final long serialVersionUID = 1L;

  @JsonProperty("content")
  @NotNull(message = "must not be null")
  private List<String> content;

}
