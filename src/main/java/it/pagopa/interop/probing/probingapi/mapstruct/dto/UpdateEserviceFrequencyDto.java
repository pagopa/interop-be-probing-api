package it.pagopa.interop.probing.probingapi.mapstruct.dto;

import java.time.OffsetTime;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdateEserviceFrequencyDto {

	@NotNull
	@JsonProperty("eserviceId")
	private UUID eserviceId;

	@NotNull
	@JsonProperty("versionId")
	private UUID versionId;

	@NotNull
	@JsonProperty("pollingEndTime")
	private OffsetTime newPollingEndTime;

	@NotNull
	@JsonProperty("pollingFrequency")
	private Integer newPollingFrequency;

	@NotNull
	@JsonProperty("pollingStartTime")
	private OffsetTime newPollingStartTime;
}