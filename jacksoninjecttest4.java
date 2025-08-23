package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JacksonInjectTestTest4 {

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
    public void testFactories() throws Exception {
        JacksonInject.Value v = EMPTY.withId("name");
        assertNotSame(EMPTY, v);
        assertEquals("name", v.getId());
        assertSame(v, v.withId("name"));
        JacksonInject.Value v2 = v.withUseInput(Boolean.TRUE);
        assertNotSame(v, v2);
        assertFalse(v.equals(v2));
        assertFalse(v2.equals(v));
        assertSame(v2, v2.withUseInput(Boolean.TRUE));
        JacksonInject.Value v3 = v.withOptional(Boolean.TRUE);
        assertNotSame(v, v3);
        assertFalse(v.equals(v3));
        assertFalse(v3.equals(v));
        assertSame(v3, v3.withOptional(Boolean.TRUE));
        assertTrue(v3.getOptional());
        int x = v2.hashCode();
        if (x == 0) {
            // no fixed value, but should not evaluate to 0
            fail();
        }
    }
}
