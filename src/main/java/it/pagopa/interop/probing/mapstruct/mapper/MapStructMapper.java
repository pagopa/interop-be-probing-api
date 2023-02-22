package it.pagopa.interop.probing.mapstruct.mapper;

import it.pagopa.interop.probing.interop_be_probing.model.ChangeEServiceStateRequest;
import it.pagopa.interop.probing.mapstruct.dto.UpdateEserviceStateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(
        componentModel = "spring"
)
public interface MapStructMapper {


    @Mapping(source = "changeEServiceStateRequest.eServiceState", target = "newEServiceState")
    UpdateEserviceStateDto changeEServiceStateRequestDataToUpdateEserviceStateDto(
            UUID eserviceId,
            UUID versionId,
            ChangeEServiceStateRequest changeEServiceStateRequest);
}
