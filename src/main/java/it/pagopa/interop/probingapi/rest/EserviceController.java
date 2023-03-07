package it.pagopa.interop.probingapi.rest;

import it.pagopa.interop.probing.interop_be_probing_api.api.EservicesApi;
import it.pagopa.interop.probing.interop_be_probing_api.model.ChangeEserviceStateRequest;
import it.pagopa.interop.probing.interop_be_probing_api.model.ChangeProbingFrequencyRequest;
import it.pagopa.interop.probing.interop_be_probing_api.model.ChangeProbingStateRequest;
import it.pagopa.interop.probingapi.mapstruct.mapper.MapStructMapper;
import it.pagopa.interop.probingapi.service.EserviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
public class EserviceController implements EservicesApi {

    @Autowired
    EserviceService eserviceService;

    @Autowired
    MapStructMapper mapstructMapper;

    @Override
    public ResponseEntity<Void> updateEserviceFrequency(UUID eserviceId, UUID versionId, ChangeProbingFrequencyRequest changeProbingFrequencyRequest) throws Exception {
        return EservicesApi.super.updateEserviceFrequency(eserviceId, versionId, changeProbingFrequencyRequest);
    }

    @Override
    public ResponseEntity<Void> updateEserviceProbingState(UUID eserviceId,
                                                           UUID versionId,
                                                           ChangeProbingStateRequest changeProbingStateRequest) throws Exception {
        eserviceService.updateEserviceProbingState(mapstructMapper.toUpdateEserviceProbingStateDto(eserviceId,
                versionId, changeProbingStateRequest));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateEserviceState(UUID eserviceId, UUID versionId, ChangeEserviceStateRequest changeEserviceStateRequest) throws Exception {
        eserviceService.updateEserviceState(mapstructMapper.toUpdateEserviceStateDto(eserviceId,
                versionId, changeEserviceStateRequest));
        return ResponseEntity.noContent().build();
    }

}
