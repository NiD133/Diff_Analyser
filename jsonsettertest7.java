package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonSetterTestTest7 {

    private final JsonSetter.Value EMPTY = JsonSetter.Value.empty();

    private final static class Bogus {

        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.SKIP)
        public int field;
    }

    @Test
    public void testWithMethods() {
        JsonSetter.Value v = EMPTY.withContentNulls(null);
        assertSame(EMPTY, v);
        v = v.withContentNulls(Nulls.FAIL);
        assertEquals(Nulls.FAIL, v.getContentNulls());
        assertSame(v, v.withContentNulls(Nulls.FAIL));
        JsonSetter.Value v2 = v.withValueNulls(Nulls.SKIP);
        assertEquals(Nulls.SKIP, v2.getValueNulls());
        assertFalse(v.equals(v2));
        assertFalse(v2.equals(v));
        JsonSetter.Value v3 = v2.withValueNulls(null, null);
        assertEquals(Nulls.DEFAULT, v3.getContentNulls());
        assertEquals(Nulls.DEFAULT, v3.getValueNulls());
        assertSame(v3, v3.withValueNulls(null, null));
        JsonSetter.Value merged = v3.withOverrides(v2);
        assertNotSame(v2, merged);
        assertEquals(merged, v2);
        assertEquals(v2, merged);
    }
}
