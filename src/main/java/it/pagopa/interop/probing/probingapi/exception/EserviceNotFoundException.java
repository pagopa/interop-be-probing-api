package it.pagopa.interop.probing.probingapi.exception;

public class EserviceNotFoundException extends Exception {

  private static final long serialVersionUID = 3431456002092605147L;

  public EserviceNotFoundException(String message) {
    super(message);
  }

  public EserviceNotFoundException() {
    super();
  }
}
