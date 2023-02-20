package it.pagopa.interop.probing.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.interop.probing.exception.EserviceNotFoundException;
import it.pagopa.interop.probing.interop_be_probing.model.ChangeEServiceStateDTO;
import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import it.pagopa.interop.probing.rest.EserviceController;
import it.pagopa.interop.probing.service.EserviceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = EserviceController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EserviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EserviceService service;

    private ChangeEServiceStateDTO changeEServiceStateDTO;
    private final UUID eServiceId = UUID.randomUUID();
    private final UUID versionId = UUID.randomUUID();

    @BeforeEach
    void setup(){
        changeEServiceStateDTO = new ChangeEServiceStateDTO();
        changeEServiceStateDTO.seteServiceState(EServiceState.fromValue("ACTIVE"));
    }

    @Test
    @DisplayName("e-service state gets updated")
    void testUpdateEserviceState_whenGivenValidEServiceIdAndVersionId_thenEServiceStateIsUpdated() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/eservices/"+eServiceId +"/versions/"+versionId+"/updateState")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changeEServiceStateDTO));

        Mockito.doNothing().when(service).updateEserviceState(eServiceId, versionId, EServiceState.ACTIVE);

        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("e-service does not exist")
    void testUpdateEserviceState_whenGivenInvalidEServiceIdAndVersionId_thenThrowsException() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/eservices/"+eServiceId +"/versions/"+versionId+"/updateState")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changeEServiceStateDTO));

        Mockito.doThrow(EserviceNotFoundException.class).when(service).updateEserviceState(eServiceId, versionId, EServiceState.ACTIVE);

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }
}