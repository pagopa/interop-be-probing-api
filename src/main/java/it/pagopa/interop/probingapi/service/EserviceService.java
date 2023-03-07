package it.pagopa.interop.probingapi.service;

import it.pagopa.interop.probingapi.exception.EserviceNotFoundException;
import it.pagopa.interop.probingapi.mapstruct.dto.UpdateEserviceProbingStateDto;
import it.pagopa.interop.probingapi.mapstruct.dto.UpdateEserviceStateDto;

public interface EserviceService {

    /**
     * Updates the state of the e-service identified by the input eserviceId and versioneId
     * @param  inputData the input data DTO containing the e-service id, version id and the probing new state
     * @throws EserviceNotFoundException if the e-service isn't found in the database
     * */
    void updateEserviceState(UpdateEserviceStateDto inputData) throws EserviceNotFoundException;

    /**
     * Updates the probing state of the e-service identified by the input eserviceId and versioneId
     * @param  inputData the input data DTO containing the e-service id, version id and the probing enabling/disabling
     * @throws EserviceNotFoundException if the e-service isn't found in the database
     * */
    void updateEserviceProbingState(UpdateEserviceProbingStateDto inputData) throws EserviceNotFoundException;
}
