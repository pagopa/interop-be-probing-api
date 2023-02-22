package it.pagopa.interop.probing.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.interop.probing.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.interop_be_probing.model.ChangeEServiceStateRequest;
import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import it.pagopa.interop.probing.mapstruct.dto.UpdateEserviceStateDto;
import it.pagopa.interop.probing.mapstruct.mapper.MapStructMapper;
import it.pagopa.interop.probing.service.EserviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class EserviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MapStructMapper mapstructMapper;

    @MockBean
    private EserviceService service;

    private ChangeEServiceStateRequest changeEServiceStateRequest;
    private final UUID eServiceId = UUID.randomUUID();
    private final UUID versionId = UUID.randomUUID();

    private UpdateEserviceStateDto updateEserviceStateDto;

    @BeforeEach
    void setup(){
        changeEServiceStateRequest = new ChangeEServiceStateRequest();
        changeEServiceStateRequest.seteServiceState(EServiceState.fromValue("ACTIVE"));
        updateEserviceStateDto = new UpdateEserviceStateDto();
        updateEserviceStateDto.setEserviceId(eServiceId);
        updateEserviceStateDto.setVersionId(versionId);
        updateEserviceStateDto.setNewEServiceState(EServiceState.fromValue("ACTIVE"));
    }

    @Test
    @DisplayName("e-service state gets updated")
    void testUpdateEserviceState_whenGivenValidEServiceIdAndVersionId_thenEServiceStateIsUpdated() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/eservices/"+eServiceId +"/versions/"+versionId+"/updateState")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changeEServiceStateRequest));

        Mockito.doNothing().when(service).updateEserviceState(updateEserviceStateDto);

        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("e-service does not exist")
    void testUpdateEserviceState_whenGivenInvalidEServiceIdAndVersionId_thenThrowsException() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/eservices/"+eServiceId +"/versions/"+versionId+"/updateState")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changeEServiceStateRequest));

        Mockito.doThrow(EserviceNotFoundException.class).when(service).updateEserviceState(updateEserviceStateDto);

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }
}