package it.pagopa.interop.probing.exception;

import it.pagopa.interop.probing.interop_be_probing.model.Problem;
import it.pagopa.interop.probing.interop_be_probing.model.ProblemError;
import it.pagopa.interop.probing.util.constant.ErrorMessageConstants;
import it.pagopa.interop.probing.util.constant.LoggingConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Manages the {@link EserviceNotFoundException} creating a new {@link ResponseEntity} and sending it to the client
     * with error code 404 and information about the error
     * @param ex The intercepted exception
     * @return A new {@link ResponseEntity} with {@link Problem} body
     * */
    @ExceptionHandler(EserviceNotFoundException.class)
    protected ResponseEntity<Problem> handleLogExtractorException(EserviceNotFoundException ex) {
        log.error(ExceptionUtils.getStackTrace(ex));
        Problem problemResponse = createProblem(HttpStatus.NOT_FOUND,
                ErrorMessageConstants.ELEMENT_NOT_FOUND,
                ErrorMessageConstants.ELEMENT_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemResponse);
    }

    /**
     * Creates an instance of type {@link Problem} following the RFC 7807 standard
     * @param responseCode The response error code
     * @param titleMessage The response title message
     * @param detailMessage The response detail error message
     * @return A new instance of {@link Problem}
     * */
    private Problem createProblem(HttpStatus responseCode, String titleMessage, String detailMessage){
        Problem genericError = new Problem();
        genericError.setStatus(responseCode.value());
        genericError.setTitle(titleMessage);
        genericError.setDetail(detailMessage);
        genericError.setTraceId(MDC.get(LoggingConstants.TRACE_ID_PLACEHOLDER));
        ProblemError errorDetails = new ProblemError();
        errorDetails.setCode(responseCode.toString());
        errorDetails.setDetail(detailMessage);
        List<ProblemError> errorDetailsList = new ArrayList<>();
        errorDetailsList.add(errorDetails);
        genericError.setErrors(errorDetailsList);
        return genericError;
    }
}
