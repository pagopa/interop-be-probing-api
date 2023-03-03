package it.pagopa.interop.probing.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Entity
@Table(name="eservice_probing_requests")
@Data
@NoArgsConstructor
public class EserviceProbingRequest {

    @Id
    private long id;

    @Column(name="last_request", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @NotNull
    private OffsetDateTime lastRequest;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private Eservice eservice;
}
