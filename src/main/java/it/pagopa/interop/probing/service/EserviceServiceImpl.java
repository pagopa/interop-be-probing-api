package it.pagopa.interop.probing.service;

import it.pagopa.interop.probing.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.mapstruct.dto.UpdateEserviceStateDto;
import it.pagopa.interop.probing.model.Eservice;
import it.pagopa.interop.probing.repository.EserviceRepository;
import it.pagopa.interop.probing.util.constant.ErrorMessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class EserviceServiceImpl implements EserviceService {

    @Autowired
    EserviceRepository eserviceRepository;

    @Override
    public void updateEserviceState(UpdateEserviceStateDto inputData) throws EserviceNotFoundException {
        Optional<Eservice> queryResult = eserviceRepository.findByEserviceIdAndVersionId(
                inputData.getEserviceId(), inputData.getVersionId());
        if(queryResult.isEmpty()){
            throw new EserviceNotFoundException(ErrorMessageConstants.ELEMENT_NOT_FOUND);
        }
        Eservice eServiceToUpdate = queryResult.get();
        eServiceToUpdate.setState(inputData.getNewEServiceState());
        eserviceRepository.save(eServiceToUpdate);
    }
}
