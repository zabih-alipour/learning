package com.alipour.learning.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table
@Data
public class Menu {

    @Id
    public Long id;

    private Long restaurantId;

    private Boolean active;

    @Transient
    private List<MenuItem> menuItems;
}
