package it.pagopa.interop.probing.mapstruct.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateEserviceStateDto {

    @JsonProperty("eserviceId")
    private UUID eserviceId;

    @JsonProperty("versionId")
    private UUID versionId;

    @JsonProperty("eServiceState")
    private EServiceState newEServiceState;
}
