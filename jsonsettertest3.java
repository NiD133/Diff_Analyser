package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonSetterTestTest3 {

    private final JsonSetter.Value EMPTY = JsonSetter.Value.empty();

    private final static class Bogus {

        @JsonSetter(nulls = Nulls.FAIL, contentNulls = Nulls.SKIP)
        public int field;
    }

    @Test
    public void testFromAnnotation() throws Exception {
        // legal
        assertSame(EMPTY, JsonSetter.Value.from(null));
        JsonSetter ann = Bogus.class.getField("field").getAnnotation(JsonSetter.class);
        JsonSetter.Value v = JsonSetter.Value.from(ann);
        assertEquals(Nulls.FAIL, v.getValueNulls());
        assertEquals(Nulls.SKIP, v.getContentNulls());
    }
}
