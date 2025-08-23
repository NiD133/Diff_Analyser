package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonSetterTestTest6 {

    private final JsonSetter.Value EMPTY = JsonSetter.Value.empty();

    private final static class Bogus {

        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.SKIP)
        public int field;
    }

    @Test
    public void testSimpleMerge() {
        JsonSetter.Value v = EMPTY.withContentNulls(Nulls.SKIP);
        assertEquals(Nulls.SKIP, v.getContentNulls());
        v = v.withValueNulls(Nulls.FAIL);
        assertEquals(Nulls.FAIL, v.getValueNulls());
    }
}
