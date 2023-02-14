package it.pagopa.interop.probing.service;

import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;

import java.util.UUID;

public interface EserviceService {
    void updateEserviceState(UUID eserviceId, UUID versionId, EServiceState newState);
}
