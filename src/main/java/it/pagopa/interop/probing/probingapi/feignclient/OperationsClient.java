package it.pagopa.interop.probing.probingapi.feignclient;

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

import it.pagopa.interop.probing.probingapi.dtos.ChangeEserviceStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingFrequencyRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.EserviceState;
import it.pagopa.interop.probing.probingapi.dtos.SearchEserviceResponse;
import it.pagopa.interop.probing.probingapi.dtos.SearchProducerNameResponse;

@FeignClient(name = "operationsClient", url = "${api.operations.baseUrl}")
public interface OperationsClient {

	@GetMapping("/getProducers")
	ResponseEntity<List<SearchProducerNameResponse>> getProducers(
			@RequestParam(value = "producerName", required = false) String producerName);

	@GetMapping("/searchEservices")
	ResponseEntity<SearchEserviceResponse> searchEservices(
			@RequestParam(value = "limit", required = true) Integer limit,
			@RequestParam(value = "offset", required = true) Integer offset,
			@RequestParam(value = "eserviceName", required = false) String eserviceName,
			@RequestParam(value = "producerName", required = false) String producerName,
			@RequestParam(value = "versionNumber", required = false) Integer versionNumber,
			@RequestParam(value = "state", required = false) List<EserviceState> state);

	@PostMapping("/{eserviceId}/versions/{versionId}/updateFrequency")
	ResponseEntity<Void> updateEserviceFrequency(@PathVariable("eserviceId") UUID eserviceId,
			@PathVariable("versionId") UUID versionId,
			@Valid @RequestBody ChangeProbingFrequencyRequest changeProbingFrequencyRequest);

	@PostMapping("/{eserviceId}/versions/{versionId}/probing/updateState")
	ResponseEntity<Void> updateEserviceProbingState(@PathVariable("eserviceId") UUID eserviceId,
			@PathVariable("versionId") UUID versionId,
			@RequestBody ChangeProbingStateRequest changeProbingStateRequest);

	@PostMapping("/{eserviceId}/versions/{versionId}/updateState")
	ResponseEntity<Void> updateEserviceState(@PathVariable("eserviceId") UUID eserviceId,
			@PathVariable("versionId") UUID versionId,
			@RequestBody ChangeEserviceStateRequest changeEserviceStateRequest);

}