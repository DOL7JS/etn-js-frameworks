package cz.eg.hr.controller;

import cz.eg.hr.rest.errors.Errors;
import cz.eg.hr.rest.errors.ExceptionError;
import cz.eg.hr.rest.errors.ValidationError;
import cz.eg.hr.rest.exceptions.EntityAlreadyExistsException;
import cz.eg.hr.rest.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GeneralControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Errors> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<ValidationError> errorList = result.getFieldErrors().stream()
            .map(e -> new ValidationError(e.getField(), e.getDefaultMessage()))
            .toList();

        return ResponseEntity.badRequest().body(new Errors(errorList));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionError> handleEntityNotFoundException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionError(ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            Timestamp.valueOf(LocalDateTime.now()),
            request.getDescription(false)));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionError> handleEntityAlreadyExistsException(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionError(ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            Timestamp.valueOf(LocalDateTime.now()),
            request.getDescription(false)));
    }

}
