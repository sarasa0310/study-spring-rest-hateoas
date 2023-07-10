package com.example.payroll.employee;

import com.example.payroll.exception.EmployeeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

    @GetMapping("/{id}")
    EntityModel<Employee> getOneEmployee(@PathVariable Long id) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
    }

    @GetMapping
    CollectionModel<EntityModel<Employee>> getAllEmployees() {
        List<EntityModel<Employee>> employees = repository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
    }

    @PostMapping
    ResponseEntity<?> registerNewEmployee(@RequestBody Employee newEmployee) {
        EntityModel<Employee> registeredEmployee =
                assembler.toModel(repository.save(newEmployee));

        return ResponseEntity.created(
                registeredEmployee.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(registeredEmployee);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee,
                                      @PathVariable Long id) {
        Employee replacedEmployee = repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> repository.save(newEmployee));

        EntityModel<Employee> entityModel =
                assembler.toModel(replacedEmployee);

        return ResponseEntity.created(
                entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
