package it.pagopa.interop.probing.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
    @GeneratedValue
    private Integer id;

    @Column(name="base_path")
    private String basePath;

    @Column(name="eservice_name")
    private String eserviceName;

    @Column(name="eservice_type")
    private String eserviceType;

    private String eserviceid;

    @Column(name="last_checked")
    private Timestamp lastChecked;

    @Column(name="polling_end_time")
    private Timestamp pollingEndTime;

    @Column(name="polling_frequency")
    private BigDecimal pollingFrequency;

    @Column(name="polling_start_time")
    private Timestamp pollingStartTime;

    @Column(name="probing_enabled")
    private Boolean probingEnabled;

    @Column(name="producer_name")
    private String producerName;

    private String state;

    private String versionid;
}
