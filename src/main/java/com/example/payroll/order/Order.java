package com.example.payroll.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@Table(name = "ORDERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private Status status;

    public Order(String description) {
        this.description = description;
        this.status = Status.IN_PROGRESS;
    }

    enum Status {
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    void complete() {
        this.status = Status.COMPLETED;
    }

    void cancel() {
        this.status = Status.CANCELLED;
    }

}
