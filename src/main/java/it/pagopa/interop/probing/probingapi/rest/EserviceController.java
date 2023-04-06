package it.pagopa.interop.probing.probingapi.rest;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import it.pagopa.interop.probing.probingapi.api.EservicesApi;
import it.pagopa.interop.probing.probingapi.dtos.ChangeEserviceStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingFrequencyRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateFE;
import it.pagopa.interop.probing.probingapi.dtos.SearchEserviceResponse;
import it.pagopa.interop.probing.probingapi.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.probingapi.service.EserviceService;

@RestController
public class EserviceController implements EservicesApi {

  @Autowired
  EserviceService eserviceService;

  @Override
  public ResponseEntity<Void> updateEserviceFrequency(UUID eserviceId, UUID versionId,
      ChangeProbingFrequencyRequest changeProbingFrequencyRequest)
      throws EserviceNotFoundException {
    eserviceService.updateEserviceFrequency(eserviceId, versionId, changeProbingFrequencyRequest);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> updateEserviceProbingState(UUID eserviceId, UUID versionId,
      ChangeProbingStateRequest changeProbingStateRequest) throws EserviceNotFoundException {
    eserviceService.updateEserviceProbingState(eserviceId, versionId, changeProbingStateRequest);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> updateEserviceState(UUID eserviceId, UUID versionId,
      ChangeEserviceStateRequest changeEserviceStateRequest) throws EserviceNotFoundException {
    eserviceService.updateEserviceState(eserviceId, versionId, changeEserviceStateRequest);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<SearchEserviceResponse> searchEservices(Integer limit, Integer offset,
      String eserviceName, String producerName, Integer versionNumber,
      List<EserviceStateFE> state) {
    return ResponseEntity.ok(eserviceService.searchEservices(limit, offset, eserviceName,
        producerName, versionNumber, state));
  }



}
