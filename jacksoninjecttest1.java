package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JacksonInjectTestTest1 {

    private final JacksonInject.Value EMPTY = JacksonInject.Value.empty();

    private final static class Bogus {

        @JacksonInject(value = "inject", useInput = OptBoolean.FALSE, optional = OptBoolean.FALSE)
        public int field;

        @JacksonInject
        public int vanilla;

        @JacksonInject(optional = OptBoolean.TRUE)
        public int optionalField;
    }

    @Test
    public void testEmpty() {
        assertNull(EMPTY.getId());
        assertNull(EMPTY.getUseInput());
        assertTrue(EMPTY.willUseInput(true));
        assertFalse(EMPTY.willUseInput(false));
        assertSame(EMPTY, JacksonInject.Value.construct(null, null, null));
        // also, "" gets coerced to null so
        assertSame(EMPTY, JacksonInject.Value.construct("", null, null));
    }
}
