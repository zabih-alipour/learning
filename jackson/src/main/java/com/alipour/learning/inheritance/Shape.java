package com.alipour.learning.inheritance;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(property = "codeName",
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        use = JsonTypeInfo.Id.NAME)
public interface Shape {
    String getCodeName();
}
