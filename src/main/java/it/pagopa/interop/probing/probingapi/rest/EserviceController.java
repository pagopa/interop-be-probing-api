package it.pagopa.interop.probing.probingapi.rest;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import it.pagopa.interop.probing.probingapi.api.EservicesApi;
import it.pagopa.interop.probing.probingapi.dtos.ChangeEserviceStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingFrequencyRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingStateRequest;
import it.pagopa.interop.probing.probingapi.mapstruct.mapper.MapStructMapper;
import it.pagopa.interop.probing.probingapi.service.EserviceService;

@RestController
public class EserviceController implements EservicesApi {

	@Autowired
	EserviceService eserviceService;

	@Autowired
	MapStructMapper mapstructMapper;

	@Override
	public ResponseEntity<Void> updateEserviceFrequency(UUID eserviceId, UUID versionId,
			ChangeProbingFrequencyRequest changeProbingFrequencyRequest) throws Exception {
		eserviceService.updateEserviceFrequency(
				mapstructMapper.toUpdateEserviceFrequencyDto(eserviceId, versionId, changeProbingFrequencyRequest));
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Void> updateEserviceProbingState(UUID eserviceId, UUID versionId,
			ChangeProbingStateRequest changeProbingStateRequest) throws Exception {
		eserviceService.updateEserviceProbingState(
				mapstructMapper.toUpdateEserviceProbingStateDto(eserviceId, versionId, changeProbingStateRequest));
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Void> updateEserviceState(UUID eserviceId, UUID versionId,
			ChangeEserviceStateRequest changeEserviceStateRequest) throws Exception {
		eserviceService.updateEserviceState(
				mapstructMapper.toUpdateEserviceStateDto(eserviceId, versionId, changeEserviceStateRequest));
		return ResponseEntity.noContent().build();
	}

}
