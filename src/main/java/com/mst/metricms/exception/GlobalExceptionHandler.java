//package com.mst.metricms.exception;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Global exception handler for the Metrics microservice.
// * Handles all exceptions and returns proper HTTP responses.
// */
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    /**
//     * Handle MetricNotFoundException
//     */
//    @ExceptionHandler(MetricNotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handleMetricNotFoundException(MetricNotFoundException ex) {
//        log.error("Metric not found: {}", ex.getMessage());
//        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
//    }
//
//    /**
//     * Handle DuplicateMetricException
//     */
//    @ExceptionHandler(DuplicateMetricException.class)
//    public ResponseEntity<Map<String, Object>> handleDuplicateMetricException(DuplicateMetricException ex) {
//        log.error("Duplicate metric: {}", ex.getMessage());
//        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
//    }
//
//    /**
//     * Handle validation errors
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
//        log.error("Validation error: {}", ex.getMessage());
//
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//
//        Map<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("timestamp", LocalDateTime.now());
//        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
//        errorResponse.put("error", "Validation Failed");
//        errorResponse.put("errors", errors);
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * Handle IllegalArgumentException
//     */
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
//        log.error("Illegal argument: {}", ex.getMessage());
//        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * Handle generic exceptions
//     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
//        log.error("Unexpected error: {}", ex.getMessage(), ex);
//        return buildErrorResponse("An unexpected error occurred: " + ex.getMessage(),
//                HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    /**
//     * Build standardized error response
//     */
//    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
//        Map<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("timestamp", LocalDateTime.now());
//        errorResponse.put("status", status.value());
//        errorResponse.put("error", status.getReasonPhrase());
//        errorResponse.put("message", message);
//        return new ResponseEntity<>(errorResponse, status);
//    }
//}
//
