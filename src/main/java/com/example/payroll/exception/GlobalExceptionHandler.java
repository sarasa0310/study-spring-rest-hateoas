package com.example.payroll.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    ResponseEntity<String> handelEntityNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

//    @ExceptionHandler
//    ResponseEntity<String> handleEmployeeNotFound(EmployeeNotFoundException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(e.getMessage());
//    }
//
//    @ExceptionHandler
//    ResponseEntity<String> handleOrderNotFound(OrderNotFoundException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(e.getMessage());
//    }

}
