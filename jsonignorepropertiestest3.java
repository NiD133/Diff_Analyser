package com.fasterxml.jackson.annotation;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonIgnorePropertiesTestTest3 {

    private final JsonIgnoreProperties.Value EMPTY = JsonIgnoreProperties.Value.empty();

    private Set<String> _set(String... args) {
        return new LinkedHashSet<String>(Arrays.asList(args));
    }

    @JsonIgnoreProperties(value = { "foo", "bar" }, ignoreUnknown = true)
    private final static class Bogus {
    }

    @Test
    public void testFromAnnotation() throws Exception {
        JsonIgnoreProperties.Value v = JsonIgnoreProperties.Value.from(Bogus.class.getAnnotation(JsonIgnoreProperties.class));
        assertNotNull(v);
        assertFalse(v.getMerge());
        assertFalse(v.getAllowGetters());
        assertFalse(v.getAllowSetters());
        Set<String> ign = v.getIgnored();
        assertEquals(2, v.getIgnored().size());
        assertEquals(_set("foo", "bar"), ign);
    }
}
