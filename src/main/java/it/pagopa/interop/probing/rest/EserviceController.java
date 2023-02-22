package it.pagopa.interop.probing.rest;

import it.pagopa.interop.probing.interop_be_probing.api.EservicesApi;
import it.pagopa.interop.probing.interop_be_probing.model.ChangeEServiceStateRequest;
import it.pagopa.interop.probing.interop_be_probing.model.ChangeProbingFrequencyRequest;
import it.pagopa.interop.probing.interop_be_probing.model.ChangeProbingStateRequest;
import it.pagopa.interop.probing.mapstruct.mapper.MapStructMapper;
import it.pagopa.interop.probing.service.EserviceService;
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
    public ResponseEntity<Void> updateEserviceProbingState(UUID eserviceId, UUID versionId, ChangeProbingStateRequest changeProbingStateRequest) throws Exception {
        return EservicesApi.super.updateEserviceProbingState(eserviceId, versionId, changeProbingStateRequest);
    }

    @Override
    public ResponseEntity<Void> updateEserviceState(UUID eserviceId, UUID versionId, ChangeEServiceStateRequest changeEServiceStateRequest) throws Exception {
        eserviceService.updateEserviceState(mapstructMapper.changeEServiceStateRequestDataToUpdateEserviceStateDto(
                eserviceId, versionId, changeEServiceStateRequest));
        return ResponseEntity.noContent().build();
    }
}
