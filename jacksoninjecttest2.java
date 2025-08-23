package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JacksonInjectTestTest2 {

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
    public void testFromAnnotation() throws Exception {
        // legal
        assertSame(EMPTY, JacksonInject.Value.from(null));
        JacksonInject ann = Bogus.class.getField("field").getAnnotation(JacksonInject.class);
        JacksonInject.Value v = JacksonInject.Value.from(ann);
        assertEquals("inject", v.getId());
        assertEquals(Boolean.FALSE, v.getUseInput());
        assertEquals("JacksonInject.Value(id=inject,useInput=false,optional=false)", v.toString());
        assertFalse(v.equals(EMPTY));
        assertFalse(EMPTY.equals(v));
        JacksonInject ann2 = Bogus.class.getField("vanilla").getAnnotation(JacksonInject.class);
        v = JacksonInject.Value.from(ann2);
        assertEquals(JacksonInject.Value.construct(null, null, null), v, "optional should be `null` by default");
        JacksonInject optionalField = Bogus.class.getField("optionalField").getAnnotation(JacksonInject.class);
        v = JacksonInject.Value.from(optionalField);
        assertEquals(JacksonInject.Value.construct(null, null, true), v);
    }
}
