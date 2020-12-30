package wally.info.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GeneralExceptionHandler {

  @ExceptionHandler({
    UserExistsException.class,
    InvalidTokenException.class,
    BadCredentialsException.class,
    HttpMessageNotReadableException.class
  })
  public ResponseEntity<ApiResponse> handleUserExistsException(Exception ex) {
    var message = ex.getMessage();

    if (ex instanceof HttpMessageNotReadableException) {
      message = "Invalid body content !";
    }

    var apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST.value(), message);

    return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    var errors = new HashMap<String, String>();

    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });

    var apiResponse = new ApiResponse(400, "Constraints errors!", errors);

    return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
  }

  //  @ExceptionHandler(HttpMessageNotReadableException.class)
  //  public ResponseEntity<ApiResponse> invalidRequest(HttpMessageNotReadableException ex) {
  //    var apiResponse = new ApiResponse(400, "Invalid body!");
  //    return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
  //  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGeneralException(Exception ex) {
    var apiResponse = new ApiResponse(400, ex.getMessage());
    return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
  }
}
