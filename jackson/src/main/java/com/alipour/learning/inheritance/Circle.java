package com.alipour.learning.inheritance;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@Data
@JsonTypeName("Circle")
class Circle implements Shape {
    @Override
    public String getCodeName() {
        return "Circle";
    }
}
