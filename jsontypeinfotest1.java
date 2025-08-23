package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonTypeInfoTestTest1 {

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, visible = true, defaultImpl = JsonTypeInfo.class, requireTypeIdForSubtypes = OptBoolean.TRUE)
    private final static class Anno1 {
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.EXTERNAL_PROPERTY, property = "ext", defaultImpl = Void.class, requireTypeIdForSubtypes = OptBoolean.FALSE)
    private final static class Anno2 {
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.EXTERNAL_PROPERTY, property = "ext", defaultImpl = Void.class)
    private final static class Anno3 {
    }

    @Test
    public void testEmpty() {
        // 07-Mar-2017, tatu: Important to distinguish "none" from 'empty' value...
        assertNull(JsonTypeInfo.Value.from(null));
    }
}
