package io.syebookstore;

import io.syemessenger.api.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {

  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<String> handleServiceException(ServiceException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.resolve(ex.errorCode()));
  }
}
