package it.pagopa.interop.probing.probingapi.mapstruct.dto;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.pagopa.interop.probing.probingapi.dtos.EserviceState;
import lombok.Data;

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
