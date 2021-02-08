package wally.info.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({
    UserExistsException.class,
    InvalidTokenException.class,
    BadCredentialsException.class
  })
  public ResponseEntity<ErrorResponse> handleUserException(Exception ex) {
    var message = ex.getMessage();

    var apiResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
    return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    var apiResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Malformed JSON");
    return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(
      MissingServletRequestPartException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    var message = String.format("Param %s missing", ex.getRequestPartName());
    var apiResponse = new ErrorResponse(status.value(), message);
    return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    var errors = new HashMap<String, String>();

    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });

    var apiResponse = new ErrorResponse(400, "Constraints errors!", errors);

    return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
  }
}
