package com.alipour.learning.inheritance;

import lombok.Data;

@Data
class Triangle implements Shape {

    @Override
    public String getCodeName() {
        return "Triangle";
    }
}
