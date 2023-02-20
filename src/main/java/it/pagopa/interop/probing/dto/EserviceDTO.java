package it.pagopa.interop.probing.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EserviceDTO {

		private String name;
		private String eserviceId;
		private String versionId;
		private String type;
		private String state;
		private String[] basePath;
		private String producerName;	
		
}
