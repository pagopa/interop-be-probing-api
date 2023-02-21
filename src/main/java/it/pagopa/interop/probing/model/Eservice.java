package it.pagopa.interop.probing.model;

import it.pagopa.interop.probing.interop_be_probing.model.EServiceState;
import it.pagopa.interop.probing.util.EserviceType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * The persistent class for the eservices database table.
 *
 */
@Entity
@Table(name="eservices", uniqueConstraints=@UniqueConstraint(columnNames={"eservice_id", "version_id"}))
@TypeDef(name = "basePathType", typeClass = CustomStringArrayType.class)
@Data
@NoArgsConstructor
public class Eservice implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(name="base_path", columnDefinition = "text[]")
    @Type(type = "basePathType")
    private String[] basePath;

    @Column(name="eservice_name")
    private String eserviceName;

    @Column(name="eservice_type")
    @Enumerated(EnumType.STRING)
    private EserviceType eserviceType;

    @NotNull
    @Column(name="eservice_id")
    private UUID eserviceId;

    @Column(name="last_request")
    private Timestamp lastRequest;

    @Column(name="response_received")
    private Timestamp responseReceived;

    @Column(name="polling_end_time")
    private Time pollingEndTime;

    @Column(name="polling_frequency", columnDefinition = "integer default 5")
    private Integer pollingFrequency;

    @Column(name="polling_start_time")
    private Time pollingStartTime;

    @Column(name="probing_enabled")
    private boolean probingEnabled;

    @Column(name="producer_name")
    private String producerName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar default 'ACTIVE'")
    private EServiceState state;

    @NotNull
    @Column(name="version_id")
    private UUID versionId;
}
