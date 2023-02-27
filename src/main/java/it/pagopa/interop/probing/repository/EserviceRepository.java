package it.pagopa.interop.probing.repository;

import it.pagopa.interop.probing.model.Eservice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EserviceRepository extends JpaRepository<Eservice, Long> {

    Optional<Eservice> findByEserviceIdAndVersionId(@Param("eserviceId") UUID eserviceId,
                                                           @Param("versionId") UUID versionId);
}
