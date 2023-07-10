package com.example.payroll.exception;

import jakarta.persistence.EntityNotFoundException;

public class EmployeeNotFoundException extends EntityNotFoundException {

    public EmployeeNotFoundException(Long id) {
        super("Could not find employee " + id);
    }

}
