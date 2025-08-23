package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonSetterTestTest4 {

    private final JsonSetter.Value EMPTY = JsonSetter.Value.empty();

    private final static class Bogus {

        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.SKIP)
        public int field;
    }

    @Test
    public void testConstruct() throws Exception {
        JsonSetter.Value v = JsonSetter.Value.construct(null, null);
        assertSame(EMPTY, v);
    }
}
