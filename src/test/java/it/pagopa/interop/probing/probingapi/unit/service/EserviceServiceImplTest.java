package it.pagopa.interop.probing.probingapi.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import it.pagopa.interop.probing.probingapi.client.OperationsClient;
import it.pagopa.interop.probing.probingapi.dtos.ChangeEserviceStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingFrequencyRequest;
import it.pagopa.interop.probing.probingapi.dtos.ChangeProbingStateRequest;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateBE;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateFE;
import it.pagopa.interop.probing.probingapi.dtos.EserviceViewDTO;
import it.pagopa.interop.probing.probingapi.dtos.SearchEserviceResponse;
import it.pagopa.interop.probing.probingapi.dtos.SearchProducerNameResponse;
import it.pagopa.interop.probing.probingapi.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.probingapi.service.EserviceService;
import it.pagopa.interop.probing.probingapi.service.impl.EserviceServiceImpl;

@SpringBootTest
class EserviceServiceImplTest {

  @InjectMocks
  EserviceService service = new EserviceServiceImpl();

  @Mock
  private OperationsClient operationsClient;

  ChangeEserviceStateRequest changeEserviceStateRequest;

  ChangeProbingStateRequest changeProbingStateRequest;

  ChangeProbingFrequencyRequest changeProbingFrequencyRequest;

  List<SearchProducerNameResponse> searchProducerNameResponseExpectedList;

  SearchEserviceResponse searchEserviceResponse;
  private final UUID eserviceId = UUID.randomUUID();
  private final UUID versionId = UUID.randomUUID();

  @BeforeEach
  void setup() {
    changeEserviceStateRequest = ChangeEserviceStateRequest.builder()
        .eServiceState(EserviceStateBE.fromValue("INACTIVE")).build();

    changeProbingStateRequest = ChangeProbingStateRequest.builder().probingEnabled(false).build();

    changeProbingFrequencyRequest = ChangeProbingFrequencyRequest.builder().frequency(5)
        .startTime(OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC))
        .endTime(OffsetTime.of(20, 0, 0, 0, ZoneOffset.UTC)).build();

    EserviceViewDTO expectedEserviceViewDTO = EserviceViewDTO.builder()
        .eserviceName("Eservice-Name").producerName("Eservice-Producer-Name").versionNumber(1)
        .state(EserviceStateFE.OFFLINE).build();

    List<EserviceViewDTO> eservicesViewDTOExpectedList = Arrays.asList(expectedEserviceViewDTO);
    searchEserviceResponse =
        SearchEserviceResponse.builder().content(eservicesViewDTOExpectedList).limit(2).offset(0)
            .totalElements(Long.valueOf(eservicesViewDTOExpectedList.size())).build();
  }

  @Test
  @DisplayName("e-service state correctly updated with new state")
  void testUpdateEserviceState_whenGivenCorrectEserviceIdAndVersionIdAndState_thenEserviceStateIsUpdated()
      throws EserviceNotFoundException {
    Mockito
        .when(
            operationsClient.updateEserviceState(eserviceId, versionId, changeEserviceStateRequest))
        .thenReturn(ResponseEntity.ok(null));
    service.updateEserviceState(eserviceId, versionId, changeEserviceStateRequest);
    verify(operationsClient).updateEserviceState(eserviceId, versionId, changeEserviceStateRequest);
  }

  @Test
  @DisplayName("e-service to update state of not found")
  void updateEserviceState_shouldThrowExceptionIfEserviceNotFound()
      throws EserviceNotFoundException {
    UUID eserviceIdRandom = UUID.randomUUID();
    UUID versionIdRandom = UUID.randomUUID();

    Mockito.doThrow(new EserviceNotFoundException("Eservice not found")).when(operationsClient)
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
    Mockito.when(operationsClient.updateEserviceProbingState(eserviceId, versionId,
        changeProbingStateRequest)).thenReturn(ResponseEntity.ok(null));
    service.updateEserviceProbingState(eserviceId, versionId, changeProbingStateRequest);
    verify(operationsClient).updateEserviceProbingState(eserviceId, versionId,
        changeProbingStateRequest);
  }

  @Test
  @DisplayName("e-service to update probing state of not found")
  void testEserviceProbingState_whenNoEServiceIsFound_thenThrowsException()
      throws EserviceNotFoundException {
    UUID eserviceIdRandom = UUID.randomUUID();
    UUID versionIdRandom = UUID.randomUUID();

    Mockito.doThrow(new EserviceNotFoundException("Eservice not found")).when(operationsClient)
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
    Mockito.when(operationsClient.updateEserviceFrequency(eserviceId, versionId,
        changeProbingFrequencyRequest)).thenReturn(ResponseEntity.ok(null));
    service.updateEserviceFrequency(eserviceId, versionId, changeProbingFrequencyRequest);
    verify(operationsClient).updateEserviceFrequency(eserviceId, versionId,
        changeProbingFrequencyRequest);
  }

  @Test
  @DisplayName("e-service to update frequency of not found")
  void testUpdateEserviceFrequencyDto_whenNoEServiceIsFound_thenThrowsException()
      throws EserviceNotFoundException {
    UUID eserviceIdRandom = UUID.randomUUID();
    UUID versionIdRandom = UUID.randomUUID();

    Mockito.doThrow(new EserviceNotFoundException("Eservice not found")).when(operationsClient)
        .updateEserviceFrequency(eserviceIdRandom, versionIdRandom, changeProbingFrequencyRequest);

    assertThrows(EserviceNotFoundException.class,
        () -> service.updateEserviceFrequency(eserviceIdRandom, versionIdRandom,
            changeProbingFrequencyRequest),
        "e-service should not be found and an EserviceNotFoundException should be thrown");
  }

  @Test
  @DisplayName("e-service frequency correctly updated with new state")
  void testSearchEservices_whenGivenCorrectEserviceIdAndVersionIdAndState_thenEserviceStateIsUpdated()
      throws EserviceNotFoundException {
    searchEserviceResponse.setContent(new ArrayList<EserviceViewDTO>());
    searchEserviceResponse.setTotalElements(0L);
    Mockito
        .when(operationsClient.searchEservices(2, 0, "Eservice-Name", "Eservice-Producer-Name", 1,
            Arrays.asList(EserviceStateFE.OFFLINE)))
        .thenReturn(ResponseEntity.ok(searchEserviceResponse));

    SearchEserviceResponse searchEserviceResponseResult = service.searchEservices(2, 0,
        "Eservice-Name", "Eservice-Producer-Name", 1, Arrays.asList(EserviceStateFE.OFFLINE));
    assertEquals(0, searchEserviceResponseResult.getTotalElements());
  }

  @Test
  @DisplayName("service returns SearchEserviceResponse object with content not empty")
  void testSearchEservice_whenGivenValidSizeAndPageNumber_thenReturnsSearchEserviceResponseWithContentNotEmpty() {

    Mockito
        .when(operationsClient.searchEservices(2, 0, "Eservice-Name", "Eservice-Producer-Name", 1,
            Arrays.asList(EserviceStateFE.OFFLINE)))
        .thenReturn(ResponseEntity.ok(searchEserviceResponse));

    SearchEserviceResponse searchEserviceResponseResult = service.searchEservices(2, 0,
        "Eservice-Name", "Eservice-Producer-Name", 1, Arrays.asList(EserviceStateFE.OFFLINE));

    assertEquals(searchEserviceResponse.getContent().size(),
        searchEserviceResponseResult.getContent().size());
    assertTrue(searchEserviceResponseResult.getTotalElements() > 0);
  }

  @Test
  @DisplayName("given a valid producer name, then returns a non-empty list")
  void testGetEservicesProducers_whenGivenValidProducerName_thenReturnsSearchProducerNameResponseList()
      throws Exception {
    SearchProducerNameResponse searchProducerNameResponse = SearchProducerNameResponse.builder()
        .label("ProducerName-Test-1").value("ProducerName-Test-1").build();

    searchProducerNameResponseExpectedList = Arrays.asList(searchProducerNameResponse);

    Mockito.when(operationsClient.getProducers("ProducerName-Test-1"))
        .thenReturn(ResponseEntity.ok(searchProducerNameResponseExpectedList));

    List<SearchProducerNameResponse> searchProducerNameResponseResponse =
        service.getEservicesProducers("ProducerName-Test-1");

    assertThat(searchProducerNameResponseResponse.toString()).contains("value");
  }

  @Test
  @DisplayName("given a valid producer name with no matching records, then returns an empty list")
  void testGetEservicesProducers_whenGivenValidProducerName_thenReturnsSearchProducerNameResponseListEmpty()
      throws Exception {
    searchProducerNameResponseExpectedList = new ArrayList<>();

    Mockito.when(operationsClient.getProducers("ProducerName-Test-1"))
        .thenReturn(ResponseEntity.ok(searchProducerNameResponseExpectedList));

    List<SearchProducerNameResponse> searchProducerNameResponseResponse =
        service.getEservicesProducers("ProducerName-Test-1");

    assertThat(searchProducerNameResponseResponse).isEmpty();
  }
}
