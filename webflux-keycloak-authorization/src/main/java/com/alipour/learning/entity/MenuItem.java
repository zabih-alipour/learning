package com.alipour.learning.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table
@Data
public class MenuItem {

    @Id
    public Long id;

    private Long menuId;

    private String name;

    private String description;

    @Column("type_name")
    private String type;

    @Column("group_name")
    private String group;

    private BigDecimal price;

}
