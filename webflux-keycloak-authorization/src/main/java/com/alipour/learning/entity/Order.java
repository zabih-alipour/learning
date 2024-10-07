package com.alipour.learning.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.List;

@Table("ORDERS")
@Data
public class Order {

    @Id
    public Long id;

    private Long restaurantId;

    private String userId;

    private BigDecimal total;

    @Transient
    private List<OrderItem> orderItems;
}
