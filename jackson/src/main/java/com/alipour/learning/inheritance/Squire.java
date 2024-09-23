package com.alipour.learning.inheritance;

import lombok.Data;

@Data
class Squire implements Shape {

    @Override
    public String getCodeName() {
        return "Squire";
    }
}
