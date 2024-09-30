package com.alipour.learning.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Data
public class Restaurant {

    @Id
    public Long id;

    private String name;

    private String location;

    @Column("type_name")
    private String type;
}
