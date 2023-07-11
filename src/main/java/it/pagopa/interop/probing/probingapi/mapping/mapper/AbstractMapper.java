package it.pagopa.interop.probing.probingapi.mapping.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateFE;
import it.pagopa.interop.probing.probingapi.dtos.ProbingDataEserviceResponse;
import it.pagopa.interop.probing.probingapi.dtos.SearchEserviceContent;
import it.pagopa.interop.probing.probingapi.mapping.dto.impl.ProbingDataEserviceBEResponse;
import it.pagopa.interop.probing.probingapi.mapping.dto.impl.SearchEserviceBEContent;
import it.pagopa.interop.probing.probingapi.util.EnumUtilities;

@Mapper(componentModel = "spring")
public abstract class AbstractMapper {

  @Autowired
  EnumUtilities enumUtilities;

  @Mapping(target = "state", expression = "java(mapStatusProbing(beResponse))")
  @Mapping(target = "eserviceActive", expression = "java(isActive(beResponse))")
  public abstract ProbingDataEserviceResponse toProbingDataEserviceResponse(
      ProbingDataEserviceBEResponse beResponse);


  @Mapping(target = "state", expression = "java(mapStatusSearch(beResponse))")
  public abstract SearchEserviceContent toSearchEserviceContent(SearchEserviceBEContent beResponse);


  EserviceStateFE mapStatusProbing(ProbingDataEserviceBEResponse beResponse) {
    return enumUtilities.fromPdndToMonitorState(beResponse.getResponseStatus(),
        beResponse.getState(), beResponse.getProbingEnabled(), beResponse.getResponseReceived(),
        beResponse.getLastRequest(), beResponse.getPollingFrequency());
  }

  EserviceStateFE mapStatusSearch(SearchEserviceBEContent beResponse) {
    return enumUtilities.fromPdndToMonitorState(beResponse.getResponseStatus(),
        beResponse.getState(), beResponse.getProbingEnabled(), beResponse.getResponseReceived(),
        beResponse.getLastRequest(), beResponse.getPollingFrequency());
  }

  boolean isActive(ProbingDataEserviceBEResponse beResponse) {
    return enumUtilities.isActive(beResponse.getState());
  }
}
