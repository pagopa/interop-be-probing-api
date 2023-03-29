package it.pagopa.interop.probing.probingapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import it.pagopa.interop.probing.probingapi.dtos.SearchProducerNameResponse;
import it.pagopa.interop.probing.probingapi.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.probingapi.mapstruct.dto.UpdateEserviceFrequencyDto;
import it.pagopa.interop.probing.probingapi.mapstruct.dto.UpdateEserviceProbingStateDto;
import it.pagopa.interop.probing.probingapi.mapstruct.dto.UpdateEserviceStateDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EserviceServiceImpl implements EserviceService {

	@Override
	public void updateEserviceState(UpdateEserviceStateDto inputData) throws EserviceNotFoundException {

//		TODO CHIAMATA REST
//		Optional<Eservice> queryResult = eserviceRepository.findByEserviceIdAndVersionId(inputData.getEserviceId(),
//				inputData.getVersionId());
//
//		Eservice eServiceToUpdate = queryResult
//				.orElseThrow(() -> new EserviceNotFoundException(ErrorMessages.ELEMENT_NOT_FOUND));
//
//		eServiceToUpdate.setState(inputData.getNewEServiceState());
//		eserviceRepository.save(eServiceToUpdate);
		log.info("EserviceState of eservice " + inputData.getEserviceId() + " with version " + inputData.getVersionId()
				+ " has been updated into " + inputData.getNewEServiceState());
	}

	@Override
	public void updateEserviceProbingState(UpdateEserviceProbingStateDto inputData) throws EserviceNotFoundException {

//		TODO CHIAMATA REST
//		Optional<Eservice> queryResult = eserviceRepository.findByEserviceIdAndVersionId(inputData.getEserviceId(),
//				inputData.getVersionId());
//
//		Eservice eServiceToUpdate = queryResult
//				.orElseThrow(() -> new EserviceNotFoundException(ErrorMessages.ELEMENT_NOT_FOUND));
//
//		eServiceToUpdate.setProbingEnabled(inputData.isProbingEnabled());
//		eserviceRepository.save(eServiceToUpdate);
		log.info("EserviceProbingState of eservice " + inputData.getEserviceId() + " with version "
				+ inputData.getVersionId() + " has been updated into " + inputData.isProbingEnabled());
	}

	@Override
	public void updateEserviceFrequency(UpdateEserviceFrequencyDto inputData) throws EserviceNotFoundException {

//		TODO CHIAMATA REST
		log.info("Eservice " + inputData.getEserviceId() + " with version " + inputData.getVersionId()
				+ " has been updated with startTime: " + inputData.getNewPollingStartTime() + " and endTime: "
				+ inputData.getNewPollingEndTime() + " and frequency: " + inputData.getNewPollingFrequency());

	}

	@Override
	public List<SearchProducerNameResponse> getEservicesProducers(String producerName) {
//		TODO CHIAMATA REST
		return null;
	}

}
