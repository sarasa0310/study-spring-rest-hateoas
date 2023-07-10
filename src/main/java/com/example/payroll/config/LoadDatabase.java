package com.example.payroll.config;

import com.example.payroll.employee.Employee;
import com.example.payroll.employee.EmployeeRepository;
import com.example.payroll.order.Order;
import com.example.payroll.order.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository,
                                   OrderRepository orderRepository) {
        return args -> {
            employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar"));
            employeeRepository.save(new Employee("Frodo", "Baggins", "thief"));

            employeeRepository.findAll()
                    .forEach(employee -> log.info("Preloaded " + employee));

            orderRepository.save(new Order("MacBook Pro"));
            orderRepository.save(new Order("iPhone"));

            orderRepository.findAll()
                    .forEach(order -> log.info("Preloaded " + order));
        };
    }

}
