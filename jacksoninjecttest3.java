package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JacksonInjectTestTest3 {

    private final JacksonInject.Value EMPTY = JacksonInject.Value.empty();

    private final static class Bogus {

        @JacksonInject(value = "inject", useInput = OptBoolean.FALSE, optional = OptBoolean.FALSE)
        public int field;

        @JacksonInject
        public int vanilla;

        @JacksonInject(optional = OptBoolean.TRUE)
        public int optionalField;
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testStdMethods() {
        assertEquals("JacksonInject.Value(id=null,useInput=null,optional=null)", EMPTY.toString());
        int x = EMPTY.hashCode();
        if (x == 0) {
            // no fixed value, but should not evaluate to 0
            fail();
        }
        assertEquals(EMPTY, EMPTY);
        assertFalse(EMPTY.equals(null));
        assertFalse(EMPTY.equals("xyz"));
        JacksonInject.Value equals1 = JacksonInject.Value.construct("value", true, true);
        JacksonInject.Value equals2 = JacksonInject.Value.construct("value", true, true);
        JacksonInject.Value valueNull = JacksonInject.Value.construct(null, true, true);
        JacksonInject.Value useInputNull = JacksonInject.Value.construct("value", null, true);
        JacksonInject.Value optionalNull = JacksonInject.Value.construct("value", true, null);
        JacksonInject.Value valueNotEqual = JacksonInject.Value.construct("not equal", true, true);
        JacksonInject.Value useInputNotEqual = JacksonInject.Value.construct("value", false, true);
        JacksonInject.Value optionalNotEqual = JacksonInject.Value.construct("value", true, false);
        String string = "string";
        assertEquals(equals1, equals2);
        assertNotEquals(equals1, valueNull);
        assertNotEquals(equals1, useInputNull);
        assertNotEquals(equals1, optionalNull);
        assertNotEquals(equals1, valueNotEqual);
        assertNotEquals(equals1, useInputNotEqual);
        assertNotEquals(equals1, optionalNotEqual);
        assertNotEquals(equals1, string);
    }
}
