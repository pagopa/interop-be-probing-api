package it.pagopa.interop.probing.service;

import it.pagopa.interop.probing.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;

import java.util.UUID;

public interface EserviceService {

    /**
     * Updates the state of the e-service identified by the input eserviceId and versioneId
     * @param  eserviceId the e-service unique identifier
     * @param versionId the e-service version unique identifier
     * @param newState the e-service new state to set
     * @throws EserviceNotFoundException if the e-service isn't found in the database
     * */
    void updateEserviceState(UUID eserviceId, UUID versionId, EServiceState newState) throws EserviceNotFoundException;
}
