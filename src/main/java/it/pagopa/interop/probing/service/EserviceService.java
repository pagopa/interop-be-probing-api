package it.pagopa.interop.probing.service;

import java.util.UUID;

import it.pagopa.interop.probing.dto.EserviceDTO;
import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;

public interface EserviceService {
		void updateEserviceState(UUID eserviceId, UUID versionId, EServiceState newState);
		Long saveService(EserviceDTO eservice);
}
