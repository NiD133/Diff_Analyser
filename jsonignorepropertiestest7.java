package com.fasterxml.jackson.annotation;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonIgnorePropertiesTestTest7 {

    private final JsonIgnoreProperties.Value EMPTY = JsonIgnoreProperties.Value.empty();

    private Set<String> _set(String... args) {
        return new LinkedHashSet<String>(Arrays.asList(args));
    }

    @JsonIgnoreProperties(value = { "foo", "bar" }, ignoreUnknown = true)
    private final static class Bogus {
    }

    @Test
    public void testMergeIgnoreProperties() {
        JsonIgnoreProperties.Value v1 = EMPTY.withIgnored("a");
        JsonIgnoreProperties.Value v2 = EMPTY.withIgnored("b");
        JsonIgnoreProperties.Value v3 = EMPTY.withIgnored("c");
        JsonIgnoreProperties.Value merged = JsonIgnoreProperties.Value.mergeAll(v1, v2, v3);
        Set<String> all = merged.getIgnored();
        assertEquals(3, all.size());
        assertTrue(all.contains("a"));
        assertTrue(all.contains("b"));
        assertTrue(all.contains("c"));
    }
}
