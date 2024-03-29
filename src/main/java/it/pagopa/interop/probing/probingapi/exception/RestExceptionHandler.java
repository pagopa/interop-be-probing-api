package it.pagopa.interop.probing.probingapi.exception;

import java.util.List;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.amazonaws.xray.AWSXRay;
import it.pagopa.interop.probing.probingapi.dtos.Problem;
import it.pagopa.interop.probing.probingapi.dtos.ProblemError;
import it.pagopa.interop.probing.probingapi.util.constant.ErrorMessages;
import it.pagopa.interop.probing.probingapi.util.logging.Logger;
import it.pagopa.interop.probing.probingapi.util.logging.LoggingPlaceholders;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired
  private Logger log;

  /**
   * Manages the {@link EserviceNotFoundException} creating a new {@link ResponseEntity} and sending
   * it to the client with error code 404 and information about the error
   *
   * @param ex The intercepted exception
   * @return A new {@link ResponseEntity} with {@link Problem} body
   */
  @ExceptionHandler(EserviceNotFoundException.class)
  protected ResponseEntity<Problem> handleEserviceNotFoundException(EserviceNotFoundException ex) {
    handleException(ex);
    Problem problemResponse = createProblem(HttpStatus.NOT_FOUND, ErrorMessages.ELEMENT_NOT_FOUND,
        ErrorMessages.ELEMENT_NOT_FOUND);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemResponse);
  }

  /**
   * Manages the {@link HttpMessageNotReadableException} creating a new {@link ResponseEntity} and
   * sending it to the client with error code 400 and information about the error
   *
   * @param ex The intercepted exception
   * @return A new {@link ResponseEntity} with {@link Problem} body
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    handleException(ex);
    Problem problemResponse =
        createProblem(HttpStatus.BAD_REQUEST, ErrorMessages.BAD_REQUEST, ErrorMessages.BAD_REQUEST);
    return ResponseEntity.status(status).body(problemResponse);
  }

  /**
   * Manages the {@link MethodArgumentNotValidException} creating a new {@link ResponseEntity} and
   * sending it to the client with error code 400 and information about the error
   *
   * @param ex The intercepted exception
   * @return A new {@link ResponseEntity} with {@link Problem} body
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    handleException(ex);
    Problem problemResponse =
        createProblem(HttpStatus.BAD_REQUEST, ErrorMessages.BAD_REQUEST, ErrorMessages.BAD_REQUEST);
    return ResponseEntity.status(status).body(problemResponse);
  }

  /**
   * Creates an instance of type {@link Problem} following the RFC 7807 standard
   *
   * @param responseCode The response error code
   * @param titleMessage The response title message
   * @param detailMessage The response detail error message
   * @return A new instance of {@link Problem}
   */
  private Problem createProblem(HttpStatus responseCode, String titleMessage,
      String detailMessage) {
    ProblemError errorDetails =
        ProblemError.builder().code(responseCode.toString()).detail(detailMessage).build();
    return Problem.builder().status(responseCode.value()).title(titleMessage).detail(detailMessage)
        .traceId(AWSXRay.getCurrentSegment().getTraceId().toString()).errors(List.of(errorDetails))
        .build();
  }

  private void handleException(Exception ex) {
    MDC.put(LoggingPlaceholders.TRACE_ID_XRAY_PLACEHOLDER,
        LoggingPlaceholders.TRACE_ID_XRAY_MDC_PREFIX
            + AWSXRay.getCurrentSegment().getTraceId().toString() + "]");
    log.logMessageException(ex);
  }
}
