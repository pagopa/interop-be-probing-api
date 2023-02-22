package it.pagopa.interop.probing.service;

import it.pagopa.interop.probing.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.mapstruct.dto.UpdateEserviceStateDto;

public interface EserviceService {

    /**
     * Updates the state of the e-service identified by the input eserviceId and versioneId
     * @param  inputData the input data DTO
     * @throws EserviceNotFoundException if the e-service isn't found in the database
     * */
    void updateEserviceState(UpdateEserviceStateDto inputData) throws EserviceNotFoundException;
}
