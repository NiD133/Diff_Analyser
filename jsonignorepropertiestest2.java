package com.fasterxml.jackson.annotation;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonIgnorePropertiesTestTest2 {

    private final JsonIgnoreProperties.Value EMPTY = JsonIgnoreProperties.Value.empty();

    private Set<String> _set(String... args) {
        return new LinkedHashSet<String>(Arrays.asList(args));
    }

    @JsonIgnoreProperties(value = { "foo", "bar" }, ignoreUnknown = true)
    private final static class Bogus {
    }

    @Test
    public void testEquality() {
        assertEquals(EMPTY, EMPTY);
        // empty has "merge" set to 'true' so:
        assertSame(EMPTY, EMPTY.withMerge());
        JsonIgnoreProperties.Value v = EMPTY.withoutMerge();
        assertEquals(v, v);
        assertFalse(EMPTY.equals(v));
        assertFalse(v.equals(EMPTY));
    }
}
