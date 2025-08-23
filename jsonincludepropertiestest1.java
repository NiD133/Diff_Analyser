package com.fasterxml.jackson.annotation;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonIncludePropertiesTestTest1 {

    private final JsonIncludeProperties.Value ALL = JsonIncludeProperties.Value.all();

    private Set<String> _set(String... args) {
        return new LinkedHashSet<String>(Arrays.asList(args));
    }

    @JsonIncludeProperties(value = { "foo", "bar" })
    private final static class Bogus {
    }

    @Test
    public void testAll() {
        assertSame(ALL, JsonIncludeProperties.Value.from(null));
        assertNull(ALL.getIncluded());
        assertEquals(ALL, ALL);
        assertEquals("JsonIncludeProperties.Value(included=null)", ALL.toString());
        assertEquals(0, ALL.hashCode());
    }
}
