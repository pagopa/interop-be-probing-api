package it.pagopa.interop.probing.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EserviceDTO {

		private String names;
		private String eserviceId;
		private String versionId;
		private String type;
		private String state;
		private List<String> basePath;
		private String producerName;	
		
}
