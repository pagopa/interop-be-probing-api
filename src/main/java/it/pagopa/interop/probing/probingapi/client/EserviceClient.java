package it.pagopa.interop.probing.probingapi.client;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import feign.Response;
import it.pagopa.interop.probing.probingapi.dtos.ChangeEserviceStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingFrequencyRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateFE;
import it.pagopa.interop.probing.probingapi.dtos.MainDataEserviceResponse;
import it.pagopa.interop.probing.probingapi.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.probingapi.mapping.dto.impl.ProbingDataEserviceBEResponse;
import it.pagopa.interop.probing.probingapi.mapping.dto.impl.SearchEserviceBEResponse;

@FeignClient(name = "eserviceClient",
    url = "${api.operations.baseUrl}" + "${api.operations.eservice.basePath}")
public interface EserviceClient {

  @GetMapping("/")
  ResponseEntity<SearchEserviceBEResponse> searchEservices(
      @RequestParam(value = "limit", required = true) Integer limit,
      @RequestParam(value = "offset", required = true) Integer offset,
      @RequestParam(value = "eserviceName", required = false) String eserviceName,
      @RequestParam(value = "producerName", required = false) String producerName,
      @RequestParam(value = "versionNumber", required = false) Integer versionNumber,
      @RequestParam(value = "state", required = false) List<EserviceStateFE> state);

  @PostMapping("/{eserviceId}/versions/{versionId}/updateFrequency")
  Response updateEserviceFrequency(@PathVariable("eserviceId") UUID eserviceId,
      @PathVariable("versionId") UUID versionId,
      @Valid @RequestBody ChangeProbingFrequencyRequest changeProbingFrequencyRequest);

  @PostMapping("/{eserviceId}/versions/{versionId}/probing/updateState")
  Response updateEserviceProbingState(@PathVariable("eserviceId") UUID eserviceId,
      @PathVariable("versionId") UUID versionId,
      @RequestBody ChangeProbingStateRequest changeProbingStateRequest)
      throws EserviceNotFoundException;

  @PostMapping("/{eserviceId}/versions/{versionId}/updateState")
  Response updateEserviceState(@PathVariable("eserviceId") UUID eserviceId,
      @PathVariable("versionId") UUID versionId,
      @RequestBody ChangeEserviceStateRequest changeEserviceStateRequest)
      throws EserviceNotFoundException;

  @GetMapping("/mainData/{eserviceRecordId}")
  ResponseEntity<MainDataEserviceResponse> getEserviceMainData(
      @PathVariable("eserviceRecordId") Long eserviceRecordId) throws EserviceNotFoundException;

  @GetMapping("/probingData/{eserviceRecordId}")
  ResponseEntity<ProbingDataEserviceBEResponse> getEserviceProbingData(
      @PathVariable("eserviceRecordId") Long eserviceRecordId) throws EserviceNotFoundException;
}
