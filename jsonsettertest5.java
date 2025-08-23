package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonSetterTestTest5 {

    private final JsonSetter.Value EMPTY = JsonSetter.Value.empty();

    private final static class Bogus {

        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.SKIP)
        public int field;
    }

    @Test
    public void testFactories() throws Exception {
        JsonSetter.Value v = JsonSetter.Value.forContentNulls(Nulls.SET);
        assertEquals(Nulls.DEFAULT, v.getValueNulls());
        assertEquals(Nulls.SET, v.getContentNulls());
        assertEquals(Nulls.SET, v.nonDefaultContentNulls());
        JsonSetter.Value skip = JsonSetter.Value.forValueNulls(Nulls.SKIP);
        assertEquals(Nulls.SKIP, skip.getValueNulls());
        assertEquals(Nulls.DEFAULT, skip.getContentNulls());
        assertEquals(Nulls.SKIP, skip.nonDefaultValueNulls());
    }
}
