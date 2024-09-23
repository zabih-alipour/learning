package com.alipour.learning.inheritance;

import lombok.Data;

@Data
class Pentagon implements Shape {

    @Override
    public String getCodeName() {
        return "Pentagon";
    }
}
