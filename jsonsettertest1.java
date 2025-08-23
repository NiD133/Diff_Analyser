package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonSetterTestTest1 {

    private final JsonSetter.Value EMPTY = JsonSetter.Value.empty();

    private final static class Bogus {

        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.SKIP)
        public int field;
    }

    @Test
    public void testEmpty() {
        assertEquals(Nulls.DEFAULT, EMPTY.getValueNulls());
        assertEquals(Nulls.DEFAULT, EMPTY.getContentNulls());
        assertEquals(JsonSetter.class, EMPTY.valueFor());
        assertNull(EMPTY.nonDefaultValueNulls());
        assertNull(EMPTY.nonDefaultContentNulls());
    }
}
