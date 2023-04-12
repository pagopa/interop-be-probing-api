package it.pagopa.interop.probing.probingapi.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import it.pagopa.interop.probing.probingapi.client.ProducerClient;
import it.pagopa.interop.probing.probingapi.dtos.SearchProducerNameResponse;
import it.pagopa.interop.probing.probingapi.service.EserviceService;
import it.pagopa.interop.probing.probingapi.service.impl.EserviceServiceImpl;

@SpringBootTest
class ProducerServiceImplTest {

  @InjectMocks
  EserviceService service = new EserviceServiceImpl();

  @Mock
  private ProducerClient producerClient;


  List<SearchProducerNameResponse> searchProducerNameResponseExpectedList;

  @Test
  @DisplayName("given a valid producer name, then returns a non-empty list")
  void testGetEservicesProducers_whenGivenValidProducerName_thenReturnsSearchProducerNameResponseList()
      throws Exception {
    SearchProducerNameResponse searchProducerNameResponse = SearchProducerNameResponse.builder()
        .label("ProducerName-Test-1").value("ProducerName-Test-1").build();

    searchProducerNameResponseExpectedList = Arrays.asList(searchProducerNameResponse);

    Mockito.when(producerClient.getProducers("ProducerName-Test-1"))
        .thenReturn(ResponseEntity.ok(searchProducerNameResponseExpectedList));

    List<SearchProducerNameResponse> searchProducerNameResponseResponse =
        service.getEservicesProducers("ProducerName-Test-1");

    assertThat(searchProducerNameResponseResponse.toString()).contains("value");
  }

  @Test
  @DisplayName("given a valid producer name with no matching records, then returns an empty list")
  void testGetEservicesProducers_whenGivenValidProducerName_thenReturnsSearchProducerNameResponseListEmpty()
      throws Exception {
    searchProducerNameResponseExpectedList = new ArrayList<>();

    Mockito.when(producerClient.getProducers("ProducerName-Test-1"))
        .thenReturn(ResponseEntity.ok(searchProducerNameResponseExpectedList));

    List<SearchProducerNameResponse> searchProducerNameResponseResponse =
        service.getEservicesProducers("ProducerName-Test-1");

    assertThat(searchProducerNameResponseResponse).isEmpty();
  }
}