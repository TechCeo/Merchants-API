package com.portfolio.merchantapi.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Map;
import java.util.TreeMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationFailure(MethodArgumentNotValidException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Request validation failed"
        );
        problem.setTitle("Invalid request");
        problem.setType(URI.create("https://merchant-api/errors/invalid-request"));
        problem.setProperty("errors", validationErrors(exception));

        return problem;
    }

    @ExceptionHandler(MerchantNotFoundException.class)
    public ProblemDetail handleMerchantNotFound(MerchantNotFoundException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Merchant not found");
        problem.setType(URI.create("https://merchant-api/errors/merchant-not-found"));

        return problem;
    }

    @ExceptionHandler(DuplicateIdempotencyKeyException.class)
    public ProblemDetail handleDuplicateIdempotencyKey(DuplicateIdempotencyKeyException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        problem.setTitle("Duplicate idempotency key");
        problem.setType(URI.create("https://merchant-api/errors/duplicate-idempotency-key"));

        return problem;
    }

    @ExceptionHandler({InvalidIdempotencyKeyException.class, MissingRequestHeaderException.class})
    public ProblemDetail handleInvalidIdempotencyKey(Exception exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problem.setTitle("Invalid idempotency key");
        problem.setType(URI.create("https://merchant-api/errors/invalid-idempotency-key"));

        return problem;
    }

    private Map<String, String> validationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new TreeMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }
}
