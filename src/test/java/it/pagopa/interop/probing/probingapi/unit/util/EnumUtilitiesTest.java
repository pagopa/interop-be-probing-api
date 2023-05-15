package it.pagopa.interop.probing.probingapi.unit.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateBE;
import it.pagopa.interop.probing.probingapi.dtos.EserviceStateFE;
import it.pagopa.interop.probing.probingapi.util.EnumUtilities;
import it.pagopa.interop.probing.probingapi.util.EserviceStatus;

@SpringBootTest
class EnumUtilitiesTest {

  @Autowired
  EnumUtilities enumUtilities;

  @Mock
  EnumUtilities enumMock;

  @Test
  @DisplayName("From BE to FE state return OFFLINE when checkND return false ")
  void testfromBEtoFEState_whenStateIsINACTIVE_thenReturnOFFLINEValue() {
    assertEquals(EserviceStateFE.OFFLINE,
        enumUtilities.fromPdndToMonitorState(EserviceStatus.KO, EserviceStateBE.ACTIVE, true,
            OffsetDateTime.of(2023, 3, 1, 1, 0, 0, 0, ZoneOffset.UTC),
            OffsetDateTime.of(2023, 3, 1, 0, 0, 0, 0, ZoneOffset.UTC), 5));
  }

  @Test
  @DisplayName("From BE to FE state return ONLINE when checkND return false ")
  void testfromBEtoFEState_whenStateIsACTIVE_thenReturnOLINEValue() {
    assertEquals(EserviceStateFE.ONLINE,
        enumUtilities.fromPdndToMonitorState(EserviceStatus.OK, EserviceStateBE.ACTIVE, true,
            OffsetDateTime.of(2023, 3, 1, 1, 0, 0, 0, ZoneOffset.UTC),
            OffsetDateTime.of(2023, 3, 1, 0, 0, 0, 0, ZoneOffset.UTC), 5));
  }

  @Test
  @DisplayName("CheckND returns true when probing is disabled")
  void testCheckND_whenProbingIsNotEnabled_thenReturnsTrue() {
    assertTrue(
        enumUtilities.checkND(false, OffsetDateTime.of(2023, 3, 1, 1, 0, 0, 0, ZoneOffset.UTC),
            OffsetDateTime.of(2023, 3, 1, 0, 0, 0, 0, ZoneOffset.UTC), 5));
  }

  @Test
  @DisplayName("CheckND returns true when response received is null")
  void testCheckND_whenResponseReceivedIsNull_thenReturnsTrue() {
    assertTrue(enumUtilities.checkND(false, null,
        OffsetDateTime.of(2023, 3, 1, 0, 0, 0, 0, ZoneOffset.UTC), 5));
  }

}
