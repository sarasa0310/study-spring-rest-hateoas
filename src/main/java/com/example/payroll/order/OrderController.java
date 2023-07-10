package com.example.payroll.order;

import com.example.payroll.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository repository;
    private final OrderModelAssembler assembler;

    @GetMapping("/{id}")
    EntityModel<Order> getOneOrder(@PathVariable Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return assembler.toModel(order);
    }

    @GetMapping
    CollectionModel<EntityModel<Order>> getAllOrders() {
        List<EntityModel<Order>> orderModels = repository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orderModels,
                linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel());
    }

    @PostMapping
    ResponseEntity<EntityModel<Order>> registerNewOrder(@RequestBody Order newOrder) {
        Order registeredOrder = repository.save(new Order(newOrder.getDescription()));

        EntityModel<Order> orderModel = assembler.toModel(registeredOrder);

        return ResponseEntity.created(
                orderModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(orderModel);
    }

    @PutMapping("/{id}/complete")
    ResponseEntity<?> completeOrder(@PathVariable Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Order.Status.IN_PROGRESS) {
            order.complete();
            return ResponseEntity.ok(assembler.toModel(repository.save(order)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can't complete an order that is in the " + order.getStatus() + " status"));
    }

    @DeleteMapping("/{id}/cancel")
    ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Order.Status.IN_PROGRESS) {
            order.cancel();
            return ResponseEntity.ok(assembler.toModel(repository.save(order)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can't cancel an order that is in the " + order.getStatus() + " status"));
    }

}
