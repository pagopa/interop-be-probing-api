package it.pagopa.interop.probing.mapstruct.mapper;

import it.pagopa.interop.probing.interop_be_probing_api.model.ChangeEserviceStateRequest;
import it.pagopa.interop.probing.interop_be_probing_api.model.ChangeProbingStateRequest;
import it.pagopa.interop.probing.mapstruct.dto.UpdateEserviceProbingStateDto;
import it.pagopa.interop.probing.mapstruct.dto.UpdateEserviceStateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(
        componentModel = "spring"
)
public interface MapStructMapper {


    @Mapping(source = "changeEServiceStateRequest.eServiceState", target = "newEServiceState")
    UpdateEserviceStateDto toUpdateEserviceStateDto(
            UUID eserviceId,
            UUID versionId,
            ChangeEserviceStateRequest changeEServiceStateRequest);

    UpdateEserviceProbingStateDto toUpdateEserviceProbingStateDto(UUID eserviceId,
                                                                  UUID versionId,
                                                                  ChangeProbingStateRequest changeProbingStateRequest);
}
