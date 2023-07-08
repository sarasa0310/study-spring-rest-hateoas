package com.example.payroll;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository repository;

    @GetMapping("/{id}")
    ResponseEntity<Employee> getOneEmployee(@PathVariable Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return ResponseEntity.ok().body(employee);
    }

    @GetMapping
    ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = repository.findAll();

        return ResponseEntity.ok().body(employees);
    }

    @PostMapping
    ResponseEntity<Employee> registerNewEmployee(@RequestBody Employee newEmployee) {
        Employee registeredEmployee = repository.save(newEmployee);

        return ResponseEntity.created(
                URI.create("/employees/" + registeredEmployee.getId()))
                .body(registeredEmployee);
    }

    @PutMapping("/{id}")
    ResponseEntity<Employee> replaceEmployee(@RequestBody Employee newEmployee,
                             @PathVariable Long id) {
        return repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return ResponseEntity.ok().body(repository.save(employee));
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return ResponseEntity.ok().body(repository.save(newEmployee));
                });
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
