package it.pagopa.interop.probing.service;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.pagopa.interop.probing.dto.EserviceDTO;
import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import it.pagopa.interop.probing.model.Eservice;
import it.pagopa.interop.probing.repository.EserviceRepository;
import it.pagopa.interop.probing.util.EserviceType;

@Service
@Transactional
public class EserviceServiceImpl implements EserviceService {

	@Autowired
	EserviceRepository eserviceRepository;

	@Override
	public void updateEserviceState(UUID eserviceId, UUID versionId, EServiceState newState) {
		eserviceRepository.updateEserviceStateByEserviceIdAndVersionId(eserviceId, versionId,
				newState.toString().toLowerCase());
	}

	public Long saveService(EserviceDTO eserviceNew) {

		UUID eserviceId = UUID.fromString(eserviceNew.getEserviceId());
		UUID versionId = UUID.fromString(eserviceNew.getVersionId());
		
		Eservice eservice = eserviceRepository.findByEserviceIdAndVersionId(eserviceId,
				versionId);
		if (eservice == null) {
			eservice = new Eservice();
			eservice.setVersionId(eserviceId);
			eservice.setEserviceId(versionId);
		}

		eservice.setEserviceName(eserviceNew.getName());
		eservice.setState(EServiceState.valueOf(eserviceNew.getState()));
		eservice.setProducerName(eserviceNew.getProducerName());
		eservice.setEserviceType(EserviceType.valueOf(eserviceNew.getType()));
		eservice.setBasePath(eserviceNew.getBasePath());
		eserviceRepository.save(eservice);
		
		return eservice.getId();
	}
}
