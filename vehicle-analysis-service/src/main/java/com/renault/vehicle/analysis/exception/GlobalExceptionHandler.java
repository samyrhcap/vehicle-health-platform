package com.renault.vehicle.analysis.exception;

import com.renault.vehicle.analysis.dto.response.ErrorResponse;
import com.renault.vehicle.analysis.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.time.ZoneId;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {

        ErrorCode errorCode = ex.getErrorCode();

        ErrorResponse response =
                new ErrorResponse(
                        errorCode.getCode(),
                        errorCode.getMessage(),
                        ex.getDetails(),
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
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation error");

        ErrorResponse response =
                new ErrorResponse(
                        ErrorCode.VALIDATION_ERROR.getCode(),
                        message,
                        null,
                        ErrorCode.VALIDATION_ERROR.getHttpStatus().value(),
                        request.getRequestURI(),
                        LocalDateTime.now(ZoneId.of("UTC"))
                );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse response =
                new ErrorResponse(
                        ErrorCode.INTERNAL_ERROR.getCode(),
                        ErrorCode.INTERNAL_ERROR.getMessage(),
                        ex.getMessage(),
                        ErrorCode.INTERNAL_ERROR.getHttpStatus().value(),
                        request.getRequestURI(),
                        LocalDateTime.now(ZoneId.of("UTC"))
                );

        return ResponseEntity
                .internalServerError()
                .body(response);
    }
}