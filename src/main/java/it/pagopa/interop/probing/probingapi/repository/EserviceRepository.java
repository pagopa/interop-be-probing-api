package it.pagopa.interop.probing.probingapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.pagopa.interop.probing.probingapi.model.Eservice;

@Repository
public interface EserviceRepository extends JpaRepository<Eservice, Long> {

	Optional<Eservice> findByEserviceIdAndVersionId(@Param("eserviceId") UUID eserviceId,
			@Param("versionId") UUID versionId);
}
