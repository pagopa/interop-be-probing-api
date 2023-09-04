package it.pagopa.interop.probing.probingapi.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import feign.Request;
import feign.Response;
import it.pagopa.interop.probing.probingapi.client.EserviceClient;
import it.pagopa.interop.probing.probingapi.dtos.ChangeEserviceStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingFrequencyRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateBE;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateFE;
import it.pagopa.interop.probing.probingapi.dtos.MainDataEserviceResponse;
import it.pagopa.interop.probing.probingapi.dtos.ProbingDataEserviceResponse;
import it.pagopa.interop.probing.probingapi.dtos.SearchEserviceContent;
import it.pagopa.interop.probing.probingapi.dtos.SearchEserviceResponse;
import it.pagopa.interop.probing.probingapi.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.probingapi.mapping.dto.impl.ProbingDataEserviceBEResponse;
import it.pagopa.interop.probing.probingapi.mapping.dto.impl.SearchEserviceBEContent;
import it.pagopa.interop.probing.probingapi.mapping.dto.impl.SearchEserviceBEResponse;
import it.pagopa.interop.probing.probingapi.service.EserviceService;
import it.pagopa.interop.probing.probingapi.service.impl.EserviceServiceImpl;
import it.pagopa.interop.probing.probingapi.util.logging.Logger;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EserviceServiceImplTest {

  @InjectMocks
  @Autowired
  private EserviceService service = new EserviceServiceImpl();

  @Mock
  private Logger logger;
  @Mock
  private EserviceClient eserviceClient;

  private ChangeEserviceStateRequest changeEserviceStateRequest;

  private ChangeProbingStateRequest changeProbingStateRequest;

  private ChangeProbingFrequencyRequest changeProbingFrequencyRequest;

  private SearchEserviceResponse searchEserviceResponse;

  private SearchEserviceBEResponse searchEserviceBEResponse;

  private MainDataEserviceResponse mainDataEserviceResponse;

  private ProbingDataEserviceResponse probingDataEserviceResponse;

  private ProbingDataEserviceBEResponse probingDataEserviceBEResponse;

  private final UUID eserviceId = UUID.randomUUID();
  private final UUID versionId = UUID.randomUUID();
  private final Long eservicesRecordId = 1L;

  @BeforeEach
  void setup() {
    changeEserviceStateRequest = ChangeEserviceStateRequest.builder()
        .eServiceState(EserviceStateBE.fromValue("INACTIVE")).build();

    changeProbingStateRequest = ChangeProbingStateRequest.builder().probingEnabled(false).build();

    changeProbingFrequencyRequest = ChangeProbingFrequencyRequest.builder().frequency(5)
        .startTime(OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC))
        .endTime(OffsetTime.of(20, 0, 0, 0, ZoneOffset.UTC)).build();

    SearchEserviceBEContent expectedEserviceViewDTO = SearchEserviceBEContent.builder()
        .eserviceName("Eservice-Name").producerName("Eservice-Producer-Name").versionNumber(1)
        .state(EserviceStateBE.INACTIVE).probingEnabled(true).build();

    List<SearchEserviceBEContent> eservicesViewDTOExpectedList =
        Arrays.asList(expectedEserviceViewDTO);
    searchEserviceBEResponse =
        SearchEserviceBEResponse.builder().content(eservicesViewDTOExpectedList).limit(2).offset(0)
            .totalElements(Long.valueOf(eservicesViewDTOExpectedList.size())).build();


    SearchEserviceContent expectedResponseDTO = SearchEserviceContent.builder()
        .eserviceName("Eservice-Name").producerName("Eservice-Producer-Name").versionNumber(1)
        .state(EserviceStateFE.OFFLINE).build();

    List<SearchEserviceContent> responseDTOExpectedList = Arrays.asList(expectedResponseDTO);
    searchEserviceResponse = SearchEserviceResponse.builder().content(responseDTOExpectedList)
        .limit(2).offset(0).totalElements(Long.valueOf(responseDTOExpectedList.size())).build();

    mainDataEserviceResponse = MainDataEserviceResponse.builder().eserviceName("service1")
        .producerName("producer1").versionNumber(1).build();

    probingDataEserviceResponse = ProbingDataEserviceResponse.builder().state(EserviceStateFE.N_D)
        .probingEnabled(false).eserviceActive(false).build();

    probingDataEserviceBEResponse = ProbingDataEserviceBEResponse.builder()
        .state(EserviceStateBE.ACTIVE).probingEnabled(false).pollingFrequency(5).build();
  }

  @Test
  @DisplayName("e-service state correctly updated with new state")
  void testUpdateEserviceState_whenGivenCorrectEserviceIdAndVersionIdAndState_thenEserviceStateIsUpdated()
      throws EserviceNotFoundException {
    Mockito
        .when(eserviceClient.updateEserviceState(eserviceId, versionId,
            changeEserviceStateRequest))
        .thenReturn(Response.builder().status(200).request(Request.create(Request.HttpMethod.POST,
            "foo/foo/bar/v1/delete-data-user", new HashMap<>(), null, null, null)).build());
    service.updateEserviceState(eserviceId, versionId, changeEserviceStateRequest);
    verify(eserviceClient).updateEserviceState(eserviceId, versionId, changeEserviceStateRequest);
  }

  @Test
  @DisplayName("e-service to update state of not found")
  void updateEserviceState_shouldThrowExceptionIfEserviceNotFound()
      throws EserviceNotFoundException {
    UUID eserviceIdRandom = UUID.randomUUID();
    UUID versionIdRandom = UUID.randomUUID();

    Mockito.doThrow(new EserviceNotFoundException("Eservice not found")).when(eserviceClient)
        .updateEserviceState(eserviceIdRandom, versionIdRandom, changeEserviceStateRequest);

    assertThrows(EserviceNotFoundException.class,
        () -> service.updateEserviceState(eserviceIdRandom, versionIdRandom,
            changeEserviceStateRequest),
        "e-service should not be found and an EserviceNotFoundException should be thrown");
  }

  @Test
  @DisplayName("e-service probing gets enabled")
  void testEserviceProbingState_whenGivenCorrectEserviceIdAndVersionId_thenEserviceProbingIsEnabled()
      throws EserviceNotFoundException {
    Mockito
        .when(eserviceClient.updateEserviceProbingState(eserviceId, versionId,
            changeProbingStateRequest))
        .thenReturn(Response.builder().status(200).request(Request.create(Request.HttpMethod.POST,
            "foo/foo/bar/v1/delete-data-user", new HashMap<>(), null, null, null)).build());
    service.updateEserviceProbingState(eserviceId, versionId, changeProbingStateRequest);
    verify(eserviceClient).updateEserviceProbingState(eserviceId, versionId,
        changeProbingStateRequest);
  }

  @Test
  @DisplayName("e-service to update probing state of not found")
  void testEserviceProbingState_whenNoEServiceIsFound_thenThrowsException()
      throws EserviceNotFoundException {
    UUID eserviceIdRandom = UUID.randomUUID();
    UUID versionIdRandom = UUID.randomUUID();

    Mockito.doThrow(new EserviceNotFoundException("Eservice not found")).when(eserviceClient)
        .updateEserviceProbingState(eserviceIdRandom, versionIdRandom, changeProbingStateRequest);

    assertThrows(EserviceNotFoundException.class,
        () -> service.updateEserviceProbingState(eserviceIdRandom, versionIdRandom,
            changeProbingStateRequest),
        "e-service should not be found and an EserviceNotFoundException should be thrown");
  }

  @Test
  @DisplayName("e-service frequency correctly updated with new state")
  void testUpdateEserviceFrequencyDto_whenGivenCorrectEserviceIdAndVersionIdAndState_thenEserviceStateIsUpdated()
      throws EserviceNotFoundException {
    Mockito
        .when(eserviceClient.updateEserviceFrequency(eserviceId, versionId,
            changeProbingFrequencyRequest))
        .thenReturn(Response.builder().status(200).request(Request.create(Request.HttpMethod.POST,
            "foo/foo/bar/v1/delete-data-user", new HashMap<>(), null, null, null)).build());
    service.updateEserviceFrequency(eserviceId, versionId, changeProbingFrequencyRequest);
    verify(eserviceClient).updateEserviceFrequency(eserviceId, versionId,
        changeProbingFrequencyRequest);
  }

  @Test
  @DisplayName("e-service to update frequency of not found")
  void testUpdateEserviceFrequencyDto_whenNoEServiceIsFound_thenThrowsException()
      throws EserviceNotFoundException {
    UUID eserviceIdRandom = UUID.randomUUID();
    UUID versionIdRandom = UUID.randomUUID();

    Mockito
        .when(eserviceClient.updateEserviceFrequency(eserviceId, versionId,
            changeProbingFrequencyRequest))
        .thenReturn(Response.builder().status(404).request(Request.create(Request.HttpMethod.POST,
            "foo/foo/bar/v1/delete-data-user", new HashMap<>(), null, null, null)).build());

    Response response = eserviceClient.updateEserviceFrequency(eserviceId, versionId,
        changeProbingFrequencyRequest);

    assertEquals(HttpStatus.SC_NOT_FOUND, response.status());
  }

  @Test
  @DisplayName("e-service frequency correctly updated with new state")
  void testSearchEservices_whenGivenCorrectEserviceIdAndVersionIdAndState_thenEserviceStateIsUpdated()
      throws EserviceNotFoundException {
    searchEserviceBEResponse.setContent(new ArrayList<SearchEserviceBEContent>());
    searchEserviceBEResponse.setTotalElements(0L);
    Mockito
        .when(eserviceClient.searchEservices(2, 0, "Eservice-Name", "Eservice-Producer-Name", 1,
            Arrays.asList(EserviceStateFE.OFFLINE)))
        .thenReturn(ResponseEntity.ok(searchEserviceBEResponse));

    SearchEserviceResponse searchEserviceResponseResult = service.searchEservices(2, 0,
        "Eservice-Name", "Eservice-Producer-Name", 1, Arrays.asList(EserviceStateFE.OFFLINE));
    assertEquals(0, searchEserviceResponseResult.getTotalElements());
  }

  @Test
  @DisplayName("service returns SearchEserviceResponse object with content not empty")
  void testSearchEservice_whenGivenValidSizeAndPageNumber_thenReturnsSearchEserviceResponseWithContentNotEmpty() {

    Mockito
        .when(eserviceClient.searchEservices(2, 0, "Eservice-Name", "Eservice-Producer-Name", 1,
            Arrays.asList(EserviceStateFE.OFFLINE)))
        .thenReturn(ResponseEntity.ok(searchEserviceBEResponse));

    SearchEserviceResponse searchEserviceResponseResult = service.searchEservices(2, 0,
        "Eservice-Name", "Eservice-Producer-Name", 1, Arrays.asList(EserviceStateFE.OFFLINE));

    assertEquals(searchEserviceResponse.getContent().size(),
        searchEserviceResponseResult.getContent().size());
    assertTrue(searchEserviceResponseResult.getTotalElements() > 0);
  }

  @Test
  @DisplayName("service returns MainDataEserviceResponse object with content not empty")
  void testGetEserviceMainData_whenGivenValidRecordId_thenReturnsMainDataEserviceResponseWithRequiredFields()
      throws EserviceNotFoundException {
    Mockito.when(eserviceClient.getEserviceMainData(eservicesRecordId))
        .thenReturn(ResponseEntity.ok(mainDataEserviceResponse));
    MainDataEserviceResponse mainDataEserviceResponseResult =
        service.getEserviceMainData(eservicesRecordId);

    assertEquals(mainDataEserviceResponse.getEserviceName(),
        mainDataEserviceResponseResult.getEserviceName());
    assertTrue(Objects.nonNull(mainDataEserviceResponseResult.getEserviceName()));
  }

  @Test
  @DisplayName("service returns ProbingDataEserviceResponse object with content not empty")
  void testGetEserviceProbingData_whenGivenValidRecordId_thenReturnsProbingDataEserviceResponseWithRequiredFields()
      throws EserviceNotFoundException {
    Mockito.when(eserviceClient.getEserviceProbingData(eservicesRecordId))
        .thenReturn(ResponseEntity.ok(probingDataEserviceBEResponse));
    ProbingDataEserviceResponse probingDataEserviceResponseResult =
        service.getEserviceProbingData(eservicesRecordId);

    assertEquals(probingDataEserviceResponse.getState(),
        probingDataEserviceResponseResult.getState());
    assertTrue(Objects.nonNull(probingDataEserviceResponseResult.getState()));
    assertTrue(Objects.nonNull(probingDataEserviceResponseResult.getEserviceActive()));
  }

}
