package it.pagopa.interop.probing.probingapi.mapstruct.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.interop.probing.interop_be_probing_api.model.EserviceState;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class UpdateEserviceStateDto {

    @NotNull
    @JsonProperty("eserviceId")
    private UUID eserviceId;

    @NotNull
    @JsonProperty("versionId")
    private UUID versionId;

    @NotNull
    @JsonProperty("eServiceState")
    private EserviceState newEServiceState;
}
