package it.pagopa.interop.probing.model;

import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import it.pagopa.interop.probing.util.EserviceType;
import lombok.*;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * The persistent class for the eservices database table.
 *
 */
@Entity
@Table(name="eservices")
@Getter
@Setter
public class Eservice implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(name="base_path", columnDefinition = "text[]")
    @Type(type = "it.pagopa.interop.probing.model.CustomStringArrayType")
    private String[] basePath;

    @Column(name="eservice_name")
    private String eserviceName;

    @Column(name="eservice_type")
    @Enumerated(EnumType.STRING)
    private EserviceType eserviceType;

    @Column(name="eservice_id")
    private UUID eserviceid;

    @Column(name="last_checked")
    private Timestamp lastChecked;

    @Column(name="polling_end_time")
    private Timestamp pollingEndTime;

    @Column(name="polling_frequency", columnDefinition = "integer default 5")
    private Integer pollingFrequency;

    @Column(name="polling_start_time")
    private Timestamp pollingStartTime;

    @Column(name="probing_enabled")
    private boolean probingEnabled;

    @Column(name="producer_name")
    private String producerName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar default 'ACTIVE'")
    private EServiceState state;

    @Column(name="version_id")
    private UUID versionid;
}
