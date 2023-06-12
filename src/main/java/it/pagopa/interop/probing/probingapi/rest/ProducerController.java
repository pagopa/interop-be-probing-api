package it.pagopa.interop.probing.probingapi.rest;

import java.util.List;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import it.pagopa.interop.probing.probingapi.api.ProducersApi;
import it.pagopa.interop.probing.probingapi.dtos.SearchProducerNameResponse;
import it.pagopa.interop.probing.probingapi.service.ProducerService;

@RestController
@XRayEnabled
public class ProducerController implements ProducersApi {

  @Autowired
  private ProducerService producerService;

  @Override
  public ResponseEntity<List<SearchProducerNameResponse>> getEservicesProducers(Integer limit,
      Integer offset, String producerName) {
    MDC.put("AWS-XRAY-TRACE-ID", AWSXRay.getCurrentSegment().getTraceId().toString());

    return ResponseEntity.ok(producerService.getEservicesProducers(limit, offset, producerName));
  }
}
