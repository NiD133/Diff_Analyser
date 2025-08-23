package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonSetterTestTest2 {

    private final JsonSetter.Value EMPTY = JsonSetter.Value.empty();

    private final static class Bogus {

        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.SKIP)
        public int field;
    }

    @Test
    public void testStdMethods() {
        assertEquals("JsonSetter.Value(valueNulls=DEFAULT,contentNulls=DEFAULT)", EMPTY.toString());
        int x = EMPTY.hashCode();
        if (x == 0) {
            // no fixed value, but should not evaluate to 0
            fail();
        }
        assertEquals(EMPTY, EMPTY);
        assertFalse(EMPTY.equals(null));
        assertFalse(EMPTY.equals("xyz"));
    }
}
