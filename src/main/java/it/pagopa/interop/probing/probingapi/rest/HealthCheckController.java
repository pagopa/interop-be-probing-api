package it.pagopa.interop.probing.probingapi.rest;

import it.pagopa.interop.probing.probingapi.api.StatusApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController implements StatusApi {

  @Override
  public ResponseEntity<Void> getHealthStatus() {
    return ResponseEntity.noContent().build();
  }
}
