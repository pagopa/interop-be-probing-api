package it.pagopa.interop.probing.probingapi.util.constant;

public class LoggingMessages {

  public static final String ESERVICE_STATE_UPDATED =
      "e-service has been updated to new state. eserviceId={}, versionId={}, eserviceState={}";
  public static final String ESERVICE_PROBING_STATE_UPDATED =
      "e-service probing state has been updated to new state. eserviceId={}, versionId={}, probingEnabled={}";

  public static final String ESERVICE_POLLING_CONFIG_UPDATED =
      "e-service polling data have been updated. eserviceId={}, versioneId={}, startTime={}, endTime={}, frequency={}";

  public static final String SEARCH_PRODUCER_BY_NAME = "Searching producer, producerName={}";
  public static final String SEARCH_ESERVICES_BY_FILTER =
      "Searching e-services, limit={}, offset={}, producerName={}, eserviceName={}, versionNumber={}, stateList={}";
}
