package it.pagopa.interop.probing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.pagopa.interop.probing.model.Eservice;

@Repository
public interface EserviceRepository extends CrudRepository<Eservice, Integer> {
	
	Eservice findByEserviceIdAndVersionId(UUID serviceIdParam, UUID versionIdParam);
	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Eservice eService set eService.state = :newState WHERE eService.eserviceId = :eserviceId " +
            "AND eService.versionId = :versionId")
    void updateEserviceStateByEserviceIdAndVersionId(
            @Param("eserviceId") UUID eserviceId,
            @Param("versionId") UUID versionId,
            @Param("newState") String newState);
}
