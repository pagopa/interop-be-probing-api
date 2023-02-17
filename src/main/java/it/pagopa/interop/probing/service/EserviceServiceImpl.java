package it.pagopa.interop.probing.service;

import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import it.pagopa.interop.probing.repository.EserviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class EserviceServiceImpl implements EserviceService {

    @Autowired
    EserviceRepository eserviceRepository;

    @Override
    public void updateEserviceState(UUID eserviceId, UUID versionId, EServiceState newState) {
        eserviceRepository.updateEserviceStateByEserviceIdAndVersionId(
                eserviceId, versionId, newState.toString().toLowerCase());
    }
}
