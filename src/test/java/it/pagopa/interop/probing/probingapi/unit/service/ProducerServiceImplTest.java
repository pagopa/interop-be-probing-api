package it.pagopa.interop.probing.probingapi.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import it.pagopa.interop.probing.probingapi.client.ProducerClient;
import it.pagopa.interop.probing.probingapi.dtos.SearchProducerNameResponse;
import it.pagopa.interop.probing.probingapi.mapping.dto.impl.SearchProducerNameBEResponse;
import it.pagopa.interop.probing.probingapi.service.ProducerService;
import it.pagopa.interop.probing.probingapi.service.impl.ProducerServiceImpl;
import it.pagopa.interop.probing.probingapi.util.logging.Logger;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProducerServiceImplTest {

  @InjectMocks
  @Autowired
  ProducerService producerService = new ProducerServiceImpl();

  @Mock
  Logger logger;

  @Mock
  private ProducerClient producerClient;


  List<SearchProducerNameResponse> searchProducerNameResponseExpectedList;

  SearchProducerNameBEResponse searchProducerNameBEResponse;

  @BeforeEach
  void setup() {
    searchProducerNameBEResponse =
        SearchProducerNameBEResponse.builder().content(List.of("producerName-test")).build();
  }

  @Test
  @DisplayName("given a valid producer name, then returns a non-empty list")
  void testGetEservicesProducers_whenGivenValidProducerName_thenReturnsSearchProducerNameResponseList()
      throws Exception {

    Mockito.when(producerClient.getProducers(2, 0, "ProducerName"))
        .thenReturn(ResponseEntity.ok(searchProducerNameBEResponse));

    List<SearchProducerNameResponse> searchProducerNameResponseResponse =
        producerService.getEservicesProducers(2, 0, "ProducerName");

    assertThat(searchProducerNameResponseResponse.toString()).contains("value");
  }

  @Test
  @DisplayName("given a valid producer name with no matching records, then returns an empty list")
  void testGetEservicesProducers_whenGivenValidProducerName_thenReturnsSearchProducerNameResponseListEmpty()
      throws Exception {

    Mockito.when(producerClient.getProducers(2, 0, "ProducerName-Test-1"))
        .thenReturn(ResponseEntity.ok(new SearchProducerNameBEResponse()));

    List<SearchProducerNameResponse> searchProducerNameResponseResponse =
        producerService.getEservicesProducers(2, 0, "ProducerName-Test-1");

    assertThat(searchProducerNameResponseResponse).isEmpty();
  }
}
