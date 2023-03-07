package it.pagopa.interop.probingapi.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.interop.probingapi.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.interop_be_probing_api.model.ChangeEserviceStateRequest;
import it.pagopa.interop.probing.interop_be_probing_api.model.ChangeProbingStateRequest;
import it.pagopa.interop.probing.interop_be_probing_api.model.EserviceState;
import it.pagopa.interop.probingapi.mapstruct.dto.UpdateEserviceProbingStateDto;
import it.pagopa.interop.probingapi.mapstruct.dto.UpdateEserviceStateDto;
import it.pagopa.interop.probingapi.mapstruct.mapper.MapStructMapper;
import it.pagopa.interop.probingapi.service.EserviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class EserviceControllerTest {

    @Value("${api.updateEserviceState.url}")
    private String updateEserviceStateUrl;

    @Value("${api.updateProbingState.url}")
    private String updateProbingStateUrl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MapStructMapper mapstructMapper;

    @MockBean
    private EserviceService service;

    private ChangeEserviceStateRequest changeEserviceStateRequest;

    private ChangeProbingStateRequest changeProbingStateRequest;

    private UpdateEserviceStateDto updateEserviceStateDto;

    private UpdateEserviceProbingStateDto updateEserviceProbingStateDto;
    private final UUID eServiceId = UUID.randomUUID();
    private final UUID versionId = UUID.randomUUID();

    @BeforeEach
    void setup(){
        changeEserviceStateRequest = new ChangeEserviceStateRequest();
        changeEserviceStateRequest.seteServiceState(EserviceState.INACTIVE);
        updateEserviceStateDto = new UpdateEserviceStateDto();
        updateEserviceStateDto.setEserviceId(eServiceId);
        updateEserviceStateDto.setVersionId(versionId);
        updateEserviceStateDto.setNewEServiceState(changeEserviceStateRequest.geteServiceState());

        changeProbingStateRequest = new ChangeProbingStateRequest();
        changeProbingStateRequest.setProbingEnabled(true);
        updateEserviceProbingStateDto = new UpdateEserviceProbingStateDto();
        updateEserviceProbingStateDto.setEserviceId(eServiceId);
        updateEserviceProbingStateDto.setVersionId(versionId);
        updateEserviceProbingStateDto.setProbingEnabled(changeProbingStateRequest.getProbingEnabled());
    }

    @Test
    @DisplayName("e-service state gets updated")
    void testUpdateEserviceState_whenGivenValidEServiceIdAndVersionId_thenEServiceStateIsUpdated() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                String.format(updateEserviceStateUrl, eServiceId, versionId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changeEserviceStateRequest));
        Mockito.doNothing().when(service).updateEserviceState(updateEserviceStateDto);
        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("e-service state can't be updated because e-service does not exist")
    void testUpdateEserviceState_whenEserviceDoesNotExist_thenThrows404Exception() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        String.format(updateEserviceStateUrl, eServiceId, versionId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changeEserviceStateRequest));
        Mockito.doThrow(EserviceNotFoundException.class).when(service).updateEserviceState(updateEserviceStateDto);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("e-service state can't be updated because e-service id request parameter is missing")
    void testUpdateEserviceState_whenEserviceIdParameterIsMissing_thenThrows404Exception() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/eservices/versions/"+versionId+"/updateState")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changeEserviceStateRequest));
        Mockito.doThrow(EserviceNotFoundException.class).when(service).updateEserviceState(updateEserviceStateDto);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("e-service state can't be updated because e-service versione id request parameter is missing")
    void testUpdateEserviceState_whenVersionIdParameterIsMissing_thenThrows404Exception() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/eservices/"+eServiceId+"/versions/updateState")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changeEserviceStateRequest));
        Mockito.doThrow(EserviceNotFoundException.class).when(service).updateEserviceState(updateEserviceStateDto);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("e-service state can't be updated because request body is missing")
    void testUpdateEserviceState_whenRequestBodyIsMissing_thenThrows400Exception() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        String.format(updateEserviceStateUrl, eServiceId, versionId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(null));
        Mockito.doThrow(EserviceNotFoundException.class).when(service).updateEserviceState(updateEserviceStateDto);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("e-service probing state gets updated")
    void testUpdateEserviceProbingState_whenGivenValidEServiceIdAndVersionId_thenEServiceProbingIsEnabled() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        String.format(updateProbingStateUrl, eServiceId, versionId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changeProbingStateRequest));
        Mockito.doNothing().when(service).updateEserviceProbingState(updateEserviceProbingStateDto);
        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("e-service probing state can't be updated because e-service does not exist")
    void testUpdateEserviceProbingState_whenEserviceDoesNotExist_thenThrows404Exception() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        String.format(updateProbingStateUrl, eServiceId, versionId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changeProbingStateRequest));
        Mockito.doThrow(EserviceNotFoundException.class).when(service).updateEserviceProbingState(updateEserviceProbingStateDto);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("e-service probing state can't be updated because request body is missing")
    void testUpdateEserviceProbingState_whenRequestBodyIsMissing_thenThrows400Exception() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        String.format(updateProbingStateUrl, eServiceId, versionId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(null));
        Mockito.doThrow(EserviceNotFoundException.class).when(service).updateEserviceProbingState(updateEserviceProbingStateDto);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }
}