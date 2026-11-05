package com.renault.telemetry.exception;

import com.renault.telemetry.dto.response.ErrorResponse;
import com.renault.telemetry.enums.ApiErrorCode;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException exception,
            HttpServletRequest request) {

        ApiErrorCode errorCode = exception.getErrorCode();

        ErrorResponse response = new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage(),
                errorCode.getHttpStatus().value(),
                request.getRequestURI(),
                LocalDateTime.now(ZoneId.of("UTC"))
        );

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation error");

        ErrorResponse response = new ErrorResponse(
                ApiErrorCode.VALIDATION_ERROR.getCode(),
                message,
                ApiErrorCode.VALIDATION_ERROR.getHttpStatus().value(),
                request.getRequestURI(),
                LocalDateTime.now(ZoneId.of("UTC"))
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(
                ApiErrorCode.INTERNAL_ERROR.getCode(),
                ApiErrorCode.INTERNAL_ERROR.getMessage(),
                ApiErrorCode.INTERNAL_ERROR.getHttpStatus().value(),
                request.getRequestURI(),
                LocalDateTime.now(ZoneId.of("UTC"))
        );

        return ResponseEntity
                .internalServerError()
                .body(response);
    }
}