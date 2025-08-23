package com.fasterxml.jackson.annotation;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonIgnorePropertiesTestTest6 {

    private final JsonIgnoreProperties.Value EMPTY = JsonIgnoreProperties.Value.empty();

    private Set<String> _set(String... args) {
        return new LinkedHashSet<String>(Arrays.asList(args));
    }

    @JsonIgnoreProperties(value = { "foo", "bar" }, ignoreUnknown = true)
    private final static class Bogus {
    }

    @Test
    public void testSimpleMerge() {
        JsonIgnoreProperties.Value v1 = EMPTY.withIgnoreUnknown().withAllowGetters();
        JsonIgnoreProperties.Value v2a = EMPTY.withMerge().withIgnored("a");
        JsonIgnoreProperties.Value v2b = EMPTY.withoutMerge();
        // when merging, should just have union of things
        JsonIgnoreProperties.Value v3a = v1.withOverrides(v2a);
        assertEquals(Collections.singleton("a"), v3a.getIgnored());
        assertTrue(v3a.getIgnoreUnknown());
        assertTrue(v3a.getAllowGetters());
        assertFalse(v3a.getAllowSetters());
        // when NOT merging, simply replacing values
        JsonIgnoreProperties.Value v3b = JsonIgnoreProperties.Value.merge(v1, v2b);
        assertEquals(Collections.emptySet(), v3b.getIgnored());
        assertFalse(v3b.getIgnoreUnknown());
        assertFalse(v3b.getAllowGetters());
        assertFalse(v3b.getAllowSetters());
        // and effectively really just uses overrides as is
        assertEquals(v2b, v3b);
        assertSame(v2b, v2b.withOverrides(null));
        assertSame(v2b, v2b.withOverrides(EMPTY));
    }
}
