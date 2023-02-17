package it.pagopa.interop.probing.repository;

import it.pagopa.interop.probing.model.Eservice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EserviceRepository extends CrudRepository<Eservice, Integer> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Eservice eService set eService.state = :newState WHERE eService.eserviceid = :eserviceId " +
            "AND eService.versionid = :versionId")
    void updateEserviceStateByEserviceIdAndVersionId(
            @Param("eserviceId") UUID eserviceId,
            @Param("versionId") UUID versionId,
            @Param("newState") String newState);
}
