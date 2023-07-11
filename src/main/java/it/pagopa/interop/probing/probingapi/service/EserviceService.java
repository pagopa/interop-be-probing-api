package it.pagopa.interop.probing.probingapi.service;

import java.util.List;
import java.util.UUID;
import it.pagopa.interop.probing.probingapi.dtos.ChangeEserviceStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingFrequencyRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateFE;
import it.pagopa.interop.probing.probingapi.dtos.MainDataEserviceResponse;
import it.pagopa.interop.probing.probingapi.dtos.ProbingDataEserviceResponse;
import it.pagopa.interop.probing.probingapi.dtos.SearchEserviceResponse;
import it.pagopa.interop.probing.probingapi.exception.EserviceNotFoundException;

public interface EserviceService {

  /**
   * Updates the state of the e-service identified by the input eserviceId and versionId
   * 
   * @param inputData the input data DTO containing the e-service id, version id and the probing new
   *        state
   * @throws EserviceNotFoundException if the e-service isn't found in the database
   */
  void updateEserviceState(UUID eserviceId, UUID versionId,
      ChangeEserviceStateRequest changeEserviceStateRequest) throws EserviceNotFoundException;

  /**
   * Updates the probing state of the e-service identified by the input eserviceId and versionId
   * 
   * @param inputData the input data DTO containing the e-service id, version id and the probing
   *        enabling/disabling
   * @throws EserviceNotFoundException if the e-service isn't found in the database
   */
  void updateEserviceProbingState(UUID eserviceId, UUID versionId,
      ChangeProbingStateRequest changeProbingStateRequest) throws EserviceNotFoundException;

  /**
   * Updates the frequency, pollingStartTime and pollingStartTime of the e-service identified by the
   * input eserviceId and versionId
   * 
   * @param inputData the input data DTO containing the e-service id, version id, the new frequency
   *        and new time interval for polling
   * @throws EserviceNotFoundException if the e-service isn't found in the database
   */
  void updateEserviceFrequency(UUID eserviceId, UUID versionId,
      ChangeProbingFrequencyRequest changeProbingFrequencyRequest) throws EserviceNotFoundException;

  /**
   * Retrive the eservices by input filter.
   *
   * @param limit the limit
   * @param offset the offset
   * @param eserviceName the eservice name
   * @param producerName the eservice producer name
   * @param versionNumber the version number
   * @param state the e service state
   * @return the SearchEserviceResponse which contain eserviceList and pagination parameter
   */
  public SearchEserviceResponse searchEservices(Integer limit, Integer offset, String eserviceName,
      String producerName, Integer versionNumber, List<EserviceStateFE> state);

  /**
   * Get the main data of the selected service.
   *
   * @param eserviceRecordId the eservice record id
   * @return the eservice main data
   * @throws EserviceNotFoundException
   */
  MainDataEserviceResponse getEserviceMainData(Long eserviceRecordId)
      throws EserviceNotFoundException;

  /**
   * Get the probing data of the selected service.
   *
   * @param eserviceRecordId the eservice record id
   * @return the eservice probing data
   * @throws EserviceNotFoundException
   */
  ProbingDataEserviceResponse getEserviceProbingData(Long eserviceRecordId)
      throws EserviceNotFoundException;
}
