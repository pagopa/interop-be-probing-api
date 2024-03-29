package it.pagopa.interop.probing.probingapi.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import it.pagopa.interop.probing.probingapi.service.EserviceService;

@SpringBootTest
@AutoConfigureMockMvc
class EserviceControllerTest {

  @Value("${api.updateEserviceState.url}")
  private String updateEserviceStateUrl;

  @Value("${api.updateProbingState.url}")
  private String updateProbingStateUrl;

  @Value("${api.updateEserviceFrequency.url}")
  private String updateEserviceFrequencyUrl;

  @Value("${api.operations.eservice.basePath}")
  private String apiSearchEserviceUrl;

  @Value("${api.mainDataEservice.url}")
  private String apiGetMainDataEserviceUrl;

  @Value("${api.probingDataEservice.url}")
  private String apiGetProbingDataEserviceUrl;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EserviceService service;

  private ChangeEserviceStateRequest changeEserviceStateRequest;

  private ChangeProbingStateRequest changeProbingStateRequest;

  private ChangeProbingFrequencyRequest changeProbingFrequencyRequest;

  private SearchEserviceResponse expectedSearchEserviceResponse;

  private MainDataEserviceResponse mainDataEserviceResponse;

  private ProbingDataEserviceResponse probingDataEserviceResponse;

  private final UUID eServiceId = UUID.randomUUID();
  private final UUID versionId = UUID.randomUUID();
  private final Long eservicesRecordId = 1L;

  @BeforeEach
  void setup() {
    changeEserviceStateRequest =
        ChangeEserviceStateRequest.builder().eServiceState(EserviceStateBE.ACTIVE).build();

    changeProbingStateRequest = ChangeProbingStateRequest.builder().probingEnabled(true).build();

    changeProbingFrequencyRequest = ChangeProbingFrequencyRequest.builder().frequency(5)
        .startTime(OffsetTime.of(8, 0, 0, 0, ZoneOffset.UTC))
        .endTime(OffsetTime.of(20, 0, 0, 0, ZoneOffset.UTC)).build();

    expectedSearchEserviceResponse = SearchEserviceResponse.builder().limit(2).offset(0).build();

    SearchEserviceContent content =
        SearchEserviceContent.builder().eserviceName("Eservice-Name").versionNumber(1)
            .producerName("Eservice-Producer-Name").state(EserviceStateFE.OFFLINE).build();

    List<SearchEserviceContent> eservices = Arrays.asList(content);
    expectedSearchEserviceResponse.setContent(eservices);

    mainDataEserviceResponse = MainDataEserviceResponse.builder().eserviceName("service1")
        .producerName("producer1").versionNumber(1).build();

    probingDataEserviceResponse = ProbingDataEserviceResponse.builder().state(EserviceStateFE.N_D)
        .probingEnabled(false).eserviceActive(false).build();
  }

  @Test
  @DisplayName("e-service state gets updated")
  void testUpdateEserviceState_whenGivenValidEServiceIdAndVersionId_thenEServiceStateIsUpdated()
      throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(String.format(updateEserviceStateUrl, eServiceId, versionId))
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(changeEserviceStateRequest));
    Mockito.doNothing().when(service).updateEserviceState(eServiceId, versionId,
        changeEserviceStateRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("e-service state can't be updated because e-service does not exist")
  void testUpdateEserviceState_whenEserviceDoesNotExist_thenThrows404Exception() throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(String.format(updateEserviceStateUrl, eServiceId, versionId))
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(changeEserviceStateRequest));
    Mockito.doThrow(EserviceNotFoundException.class).when(service).updateEserviceState(eServiceId,
        versionId, changeEserviceStateRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("e-service state can't be updated because e-service id request parameter is missing")
  void testUpdateEserviceState_whenEserviceIdParameterIsMissing_thenThrows404Exception()
      throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post("/eservices/versions/" + versionId + "/updateState")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(changeEserviceStateRequest));
    Mockito.doThrow(EserviceNotFoundException.class).when(service).updateEserviceState(eServiceId,
        versionId, changeEserviceStateRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("e-service state can't be updated because e-service versione id request parameter is missing")
  void testUpdateEserviceState_whenVersionIdParameterIsMissing_thenThrows404Exception()
      throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post("/eservices/" + eServiceId + "/versions/updateState")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(changeEserviceStateRequest));
    Mockito.doThrow(EserviceNotFoundException.class).when(service).updateEserviceState(eServiceId,
        versionId, changeEserviceStateRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("e-service state can't be updated because request body is missing")
  void testUpdateEserviceState_whenRequestBodyIsMissing_thenThrows400Exception() throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(String.format(updateEserviceStateUrl, eServiceId, versionId))
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(null));
    Mockito.doThrow(EserviceNotFoundException.class).when(service).updateEserviceState(eServiceId,
        versionId, changeEserviceStateRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("e-service probing state gets updated")
  void testUpdateEserviceProbingState_whenGivenValidEServiceIdAndVersionId_thenEServiceProbingIsEnabled()
      throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(String.format(updateProbingStateUrl, eServiceId, versionId))
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(changeProbingStateRequest));
    Mockito.doNothing().when(service).updateEserviceProbingState(eServiceId, versionId,
        changeProbingStateRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("e-service probing state can't be updated because e-service does not exist")
  void testUpdateEserviceProbingState_whenEserviceDoesNotExist_thenThrows404Exception()
      throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(String.format(updateProbingStateUrl, eServiceId, versionId))
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(changeProbingStateRequest));
    Mockito.doThrow(EserviceNotFoundException.class).when(service)
        .updateEserviceProbingState(eServiceId, versionId, changeProbingStateRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("e-service probing state can't be updated because request body is missing")
  void testUpdateEserviceProbingState_whenRequestBodyIsMissing_thenThrows400Exception()
      throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post(String.format(updateProbingStateUrl, eServiceId, versionId))
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(null));
    Mockito.doThrow(EserviceNotFoundException.class).when(service)
        .updateEserviceProbingState(eServiceId, versionId, changeProbingStateRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("e-service frequency, polling stard date and end date get updated")
  void testUpdateEserviceFrequencyDto_whenGivenValidEServiceIdAndVersionId_thenEserviceFrequencyPollingStartDateAndEndDateAreUpdated()
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post(String.format(updateEserviceFrequencyUrl, eServiceId, versionId))
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(changeProbingFrequencyRequest));
    Mockito.doNothing().when(service).updateEserviceFrequency(eServiceId, versionId,
        changeProbingFrequencyRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("e-service frequency can't be updated because e-service does not exist")
  void testUpdateEserviceFrequencyDto_whenEserviceDoesNotExist_thenThrows404Exception()
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post(String.format(updateEserviceFrequencyUrl, eServiceId, versionId))
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(changeProbingFrequencyRequest));
    Mockito.doThrow(EserviceNotFoundException.class).when(service)
        .updateEserviceFrequency(eServiceId, versionId, changeProbingFrequencyRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("e-service frequency can't be updated because e-service id request parameter is missing")
  void testUpdateEserviceFrequencyDto_whenEserviceIdParameterIsMissing_thenThrows404Exception()
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post("/eservices/versions/" + versionId + "/updateState")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(changeProbingFrequencyRequest));
    Mockito.doThrow(EserviceNotFoundException.class).when(service)
        .updateEserviceFrequency(eServiceId, versionId, changeProbingFrequencyRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("e-service frequency can't be updated because e-service versione id request parameter is missing")
  void testUpdateEserviceFrequencyDto_whenVersionIdParameterIsMissing_thenThrows404Exception()
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.post("/eservices/" + eServiceId + "/versions/updateState")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(changeProbingFrequencyRequest));
    Mockito.doThrow(EserviceNotFoundException.class).when(service)
        .updateEserviceFrequency(eServiceId, versionId, changeProbingFrequencyRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("e-service frequency can't be updated because request body is missing")
  void testUpdateEserviceFrequencyDto_whenRequestBodyIsMissing_thenThrows400Exception()
      throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    RequestBuilder requestBuilder = MockMvcRequestBuilders
        .post(String.format(updateEserviceFrequencyUrl, eServiceId, versionId))
        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(null));
    Mockito.doThrow(EserviceNotFoundException.class).when(service)
        .updateEserviceFrequency(eServiceId, versionId, changeProbingFrequencyRequest);
    mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("the list of e-services has been retrieved")
  void testSearchEservice_whenGivenValidSizeAndPageNumber_thenReturnsSearchEserviceResponseWithContentNotEmpty()
      throws Exception {

    Mockito.when(service.searchEservices(2, 0, "Eservice-Name", "Eservice-Producer-Name", 1, null))
        .thenReturn(expectedSearchEserviceResponse);

    MockHttpServletResponse response =
        mockMvc
            .perform(get(apiSearchEserviceUrl).params(getMockRequestParamsUpdateEserviceState("2",
                "0", "Eservice-Name", "1", "Eservice-Producer-Name", null)))
            .andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isNotEmpty();
    assertThat(response.getContentAsString()).contains("totalElements");
    assertThat(response.getContentAsString()).contains("content");

    SearchEserviceResponse searchEserviceResponse =
        new ObjectMapper().readValue(response.getContentAsString(), SearchEserviceResponse.class);
    assertThat(searchEserviceResponse.getContent()).isNotEmpty();
    assertEquals(searchEserviceResponse, expectedSearchEserviceResponse);
  }

  @Test
  @DisplayName("the retrieved list of e-services is empty")
  void testSearchEservice_whenGivenValidSizeAndPageNumber_thenReturnsSearchEserviceResponseWithContentEmpty()
      throws Exception {

    expectedSearchEserviceResponse.setContent(new ArrayList<>());
    Mockito.when(service.searchEservices(2, 0, "Eservice-Name", "Eservice-Producer-Name", 1,
        Arrays.asList(EserviceStateFE.ONLINE))).thenReturn(expectedSearchEserviceResponse);

    MockHttpServletResponse responseSearchEservice =
        mockMvc
            .perform(get(apiSearchEserviceUrl).params(getMockRequestParamsUpdateEserviceState("2",
                "0", "Eservice-Name", "1", "Eservice-Producer-Name", "ONLINE")))
            .andReturn().getResponse();

    assertThat(responseSearchEservice.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(responseSearchEservice.getContentAsString()).isNotEmpty();
    assertThat(responseSearchEservice.getContentAsString()).contains("totalElements");
    assertThat(responseSearchEservice.getContentAsString()).contains("content");

    SearchEserviceResponse searchEserviceResponse = new ObjectMapper()
        .readValue(responseSearchEservice.getContentAsString(), SearchEserviceResponse.class);
    assertThat(searchEserviceResponse.getContent()).isEmpty();
    assertEquals(searchEserviceResponse, expectedSearchEserviceResponse);
  }

  @Test
  @DisplayName("bad request exception is thrown because size request parameter is missing")
  void testSearchEservice_whenSizeParameterIsMissing_thenThrows400Exception() throws Exception {
    Mockito.when(service.searchEservices(null, 0, "Eservice-Name", "Eservice-Producer-Name", 1,
        new ArrayList<>(Arrays.asList(EserviceStateFE.OFFLINE)))).thenThrow(BadRequest.class);
    mockMvc
        .perform(get(apiSearchEserviceUrl).params(getMockRequestParamsUpdateEserviceState(null, "0",
            "Eservice-Name", "1", "Eservice-Producer-Name", "OFFLINE")))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("bad request exception is thrown because pageNumber request parameter is missing")
  void testSearchEservice_whenVersionIdParameterIsMissing_thenThrows400Exception()
      throws Exception {
    Mockito.when(service.searchEservices(2, null, "Eservice-Name", "Eservice-Producer-Name", 1,
        new ArrayList<>(Arrays.asList(EserviceStateFE.ONLINE)))).thenThrow(BadRequest.class);
    mockMvc
        .perform(get(apiSearchEserviceUrl).params(getMockRequestParamsUpdateEserviceState("2", null,
            "Eservice-Name", "1", "Eservice-Producer-Name", "ONLINE")))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("e-service main data cant be retrieved because e-service does not exist")
  void testgetEserviceMainData_whenEserviceDoesNotExist_thenThrows404Exception() throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(String.format(apiGetMainDataEserviceUrl, eservicesRecordId));
    Mockito.doThrow(EserviceNotFoundException.class).when(service)
        .getEserviceMainData(eservicesRecordId);
    mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("e-service main data are retrieved successfully")
  void testgetEserviceMainData_whenEserviceEcist_thenMainDataAreReturned() throws Exception {
    Mockito.doReturn(mainDataEserviceResponse).when(service).getEserviceMainData(eservicesRecordId);
    MockHttpServletResponse response =
        mockMvc.perform(get(String.format(apiGetMainDataEserviceUrl, eservicesRecordId)))
            .andReturn().getResponse();

    assertEquals(response.getStatus(), HttpStatus.OK.value());
    assertTrue(response.getContentAsString().contains("eserviceName"));
  }

  @Test
  @DisplayName("e-service probing data cant be retrieved because e-service does not exist")
  void testgetEserviceProbingData_whenEserviceDoesNotExist_thenThrows404Exception()
      throws Exception {
    RequestBuilder requestBuilder =
        MockMvcRequestBuilders.get(String.format(apiGetProbingDataEserviceUrl, eservicesRecordId));
    Mockito.doThrow(EserviceNotFoundException.class).when(service)
        .getEserviceProbingData(eservicesRecordId);
    mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("e-service probing data are retrieved successfully")
  void testgetEserviceProbingData_whenEserviceEcist_thenProbingDataAreReturned() throws Exception {
    Mockito.doReturn(probingDataEserviceResponse).when(service)
        .getEserviceProbingData(eservicesRecordId);
    MockHttpServletResponse response =
        mockMvc.perform(get(String.format(apiGetProbingDataEserviceUrl, eservicesRecordId)))
            .andReturn().getResponse();

    assertEquals(response.getStatus(), HttpStatus.OK.value());
    assertTrue(response.getContentAsString().contains("probingEnabled"));
    assertTrue(response.getContentAsString().contains("state"));
    assertTrue(response.getContentAsString().contains("eserviceActive"));
  }

  private LinkedMultiValueMap<String, String> getMockRequestParamsUpdateEserviceState(String limit,
      String offset, String eserviceName, String versionNumber, String producerName,
      String eServiceState) {
    LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("offset", offset);
    requestParams.add("limit", limit);
    requestParams.add("eserviceName", eserviceName);
    requestParams.add("versionNumber", versionNumber);
    requestParams.add("producerName", producerName);
    requestParams.add("state", eServiceState);
    return requestParams;
  }

}
