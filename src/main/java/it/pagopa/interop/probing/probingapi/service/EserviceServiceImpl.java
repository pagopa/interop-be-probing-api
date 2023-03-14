package it.pagopa.interop.probing.probingapi.service;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.pagopa.interop.probing.probingapi.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.probingapi.mapstruct.dto.UpdateEserviceFrequencyDto;
import it.pagopa.interop.probing.probingapi.mapstruct.dto.UpdateEserviceProbingStateDto;
import it.pagopa.interop.probing.probingapi.mapstruct.dto.UpdateEserviceStateDto;
import it.pagopa.interop.probing.probingapi.model.Eservice;
import it.pagopa.interop.probing.probingapi.repository.EserviceRepository;
import it.pagopa.interop.probing.probingapi.util.constant.ErrorMessages;

@Service
@Transactional
public class EserviceServiceImpl implements EserviceService {

	@Autowired
	EserviceRepository eserviceRepository;

	@Autowired
	Validator validator;

	@Override
	public void updateEserviceState(UpdateEserviceStateDto inputData) throws EserviceNotFoundException {
		Optional<Eservice> queryResult = eserviceRepository.findByEserviceIdAndVersionId(inputData.getEserviceId(),
				inputData.getVersionId());

		Eservice eServiceToUpdate = queryResult
				.orElseThrow(() -> new EserviceNotFoundException(ErrorMessages.ELEMENT_NOT_FOUND));

		eServiceToUpdate.setState(inputData.getNewEServiceState());
		eserviceRepository.save(eServiceToUpdate);
	}

	@Override
	public void updateEserviceProbingState(UpdateEserviceProbingStateDto inputData) throws EserviceNotFoundException {

		Optional<Eservice> queryResult = eserviceRepository.findByEserviceIdAndVersionId(inputData.getEserviceId(),
				inputData.getVersionId());

		Eservice eServiceToUpdate = queryResult
				.orElseThrow(() -> new EserviceNotFoundException(ErrorMessages.ELEMENT_NOT_FOUND));

		eServiceToUpdate.setProbingEnabled(inputData.isProbingEnabled());
		eserviceRepository.save(eServiceToUpdate);
	}

	@Override
	public void updateEserviceFrequency(UpdateEserviceFrequencyDto inputData) throws EserviceNotFoundException {

		Optional<Eservice> queryResult = eserviceRepository.findByEserviceIdAndVersionId(inputData.getEserviceId(),
				inputData.getVersionId());

		Eservice eServiceToUpdate = queryResult
				.orElseThrow(() -> new EserviceNotFoundException(ErrorMessages.ELEMENT_NOT_FOUND));

		eServiceToUpdate.setPollingFrequency(inputData.getNewPollingFrequency());
		eServiceToUpdate.setPollingStartTime(inputData.getNewPollingStartTime());
		eServiceToUpdate.setPollingEndTime(inputData.getNewPollingEndTime());
		eserviceRepository.save(eServiceToUpdate);

	}

}
