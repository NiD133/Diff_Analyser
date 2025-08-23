package com.fasterxml.jackson.annotation;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonIncludePropertiesTestTest4 {

    private final JsonIncludeProperties.Value ALL = JsonIncludeProperties.Value.all();

    private Set<String> _set(String... args) {
        return new LinkedHashSet<String>(Arrays.asList(args));
    }

    @JsonIncludeProperties(value = { "foo", "bar" })
    private final static class Bogus {
    }

    @Test
    public void testWithOverridesEmpty() {
        JsonIncludeProperties.Value v = JsonIncludeProperties.Value.from(Bogus.class.getAnnotation(JsonIncludeProperties.class));
        v = v.withOverrides(new JsonIncludeProperties.Value(Collections.<String>emptySet()));
        Set<String> included = v.getIncluded();
        assertEquals(0, included.size());
    }
}
