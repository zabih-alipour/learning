package com.alipour.learning.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table
@Data
public class OrderItem {

    @Id
    public Long id;

    private Long orderId;

    private Long menuItemId;

    private BigDecimal price;
}
