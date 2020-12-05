package com.careercenter.exception;

import com.careercenter.model.ErrorMessage;
import com.careercenter.model.ErrorResponse;
import com.careercenter.model.FieldError;
import com.careercenter.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(Exception ex, WebRequest request) {
        log.error(ex.getLocalizedMessage(), ex);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .error(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        log.error(ex.getLocalizedMessage(), ex);
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(er -> {
                    return FieldError.builder()
                            .message(er.getDefaultMessage())
                            .field(er.getField())
                            .rejectedValue(String.valueOf(er.getRejectedValue()))
                            .build();
                }).collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errors(fieldErrors)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        log.error(ex.getLocalizedMessage(), ex);
        return super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpHeaders headers,
                                                                      HttpStatus status,
                                                                      WebRequest request) {
        log.error(ex.getLocalizedMessage(), ex);
        return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorMessage> handleAllException(Exception ex, WebRequest request) {
        log.error(ex.getLocalizedMessage(), ex);
        ErrorMessage errorMessage = ErrorMessage.builder()
                .error(Utils.ServerError.getName())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
