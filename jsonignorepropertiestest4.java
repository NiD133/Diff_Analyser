package com.fasterxml.jackson.annotation;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonIgnorePropertiesTestTest4 {

    private final JsonIgnoreProperties.Value EMPTY = JsonIgnoreProperties.Value.empty();

    private Set<String> _set(String... args) {
        return new LinkedHashSet<String>(Arrays.asList(args));
    }

    @JsonIgnoreProperties(value = { "foo", "bar" }, ignoreUnknown = true)
    private final static class Bogus {
    }

    @Test
    public void testFactories() {
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoreUnknown(false));
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoredProperties());
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoredProperties(Collections.<String>emptySet()));
        JsonIgnoreProperties.Value v = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b");
        assertEquals(_set("a", "b"), v.getIgnored());
        JsonIgnoreProperties.Value vser = v.withAllowGetters();
        assertTrue(vser.getAllowGetters());
        assertFalse(vser.getAllowSetters());
        assertEquals(_set("a", "b"), vser.getIgnored());
        assertEquals(_set("a", "b"), vser.findIgnoredForDeserialization());
        assertEquals(_set(), vser.findIgnoredForSerialization());
        JsonIgnoreProperties.Value vdeser = v.withAllowSetters();
        assertFalse(vdeser.getAllowGetters());
        assertTrue(vdeser.getAllowSetters());
        assertEquals(_set("a", "b"), vdeser.getIgnored());
        assertEquals(_set(), vdeser.findIgnoredForDeserialization());
        assertEquals(_set("a", "b"), vdeser.findIgnoredForSerialization());
    }
}
