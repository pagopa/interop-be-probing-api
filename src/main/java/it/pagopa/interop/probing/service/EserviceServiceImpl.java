package it.pagopa.interop.probing.service;

import it.pagopa.interop.probing.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import it.pagopa.interop.probing.model.Eservice;
import it.pagopa.interop.probing.repository.EserviceRepository;
import it.pagopa.interop.probing.util.constant.ErrorMessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class EserviceServiceImpl implements EserviceService {

    @Autowired
    EserviceRepository eserviceRepository;

    @Override
    public void updateEserviceState(UUID eserviceId, UUID versionId, EServiceState newState) throws EserviceNotFoundException {
        Optional<Eservice> queryResult = eserviceRepository.findByEserviceIdAndVersionId(eserviceId, versionId);
        if(queryResult.isEmpty()){
            throw new EserviceNotFoundException(ErrorMessageConstants.ELEMENT_NOT_FOUND);
        }
        Eservice eServiceToUpdate = queryResult.get();
        eServiceToUpdate.setState(newState);
        eserviceRepository.save(eServiceToUpdate);
    }
}
