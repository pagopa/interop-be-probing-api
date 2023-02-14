package it.pagopa.interop.probing.rest;

import it.pagopa.interop.probing.interop_be_probing.api.EservicesApi;
import it.pagopa.interop.probing.interop_be_probing.model.ChangeEServiceStateDTO;
import it.pagopa.interop.probing.interop_be_probing.model.ChangeProbingFrequencyDTO;
import it.pagopa.interop.probing.interop_be_probing.model.ChangeProbingStateDTO;
import it.pagopa.interop.probing.service.EserviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
public class EserviceController implements EservicesApi {

    @Autowired
    EserviceService eserviceService;

    @Override
    public ResponseEntity<Void> updateEserviceFrequency(UUID eserviceId, UUID versionId, ChangeProbingFrequencyDTO changeProbingFrequencyDTO) throws Exception {
        return EservicesApi.super.updateEserviceFrequency(eserviceId, versionId, changeProbingFrequencyDTO);
    }

    @Override
    public ResponseEntity<Void> updateEserviceProbingState(UUID eserviceId, UUID versionId, ChangeProbingStateDTO changeProbingStateDTO) throws Exception {
        return EservicesApi.super.updateEserviceProbingState(eserviceId, versionId, changeProbingStateDTO);
    }

    @Override
    public ResponseEntity<Void> updateEserviceState(UUID eserviceId, UUID versionId, ChangeEServiceStateDTO changeEServiceStateDTO) throws Exception {
        eserviceService.updateEserviceState(eserviceId, versionId, changeEServiceStateDTO.geteServiceState());
        return ResponseEntity.noContent().build();
    }
}
