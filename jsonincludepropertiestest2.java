package com.fasterxml.jackson.annotation;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonIncludePropertiesTestTest2 {

    private final JsonIncludeProperties.Value ALL = JsonIncludeProperties.Value.all();

    private Set<String> _set(String... args) {
        return new LinkedHashSet<String>(Arrays.asList(args));
    }

    @JsonIncludeProperties(value = { "foo", "bar" })
    private final static class Bogus {
    }

    @Test
    public void testFromAnnotation() {
        JsonIncludeProperties.Value v = JsonIncludeProperties.Value.from(Bogus.class.getAnnotation(JsonIncludeProperties.class));
        assertNotNull(v);
        Set<String> included = v.getIncluded();
        assertEquals(2, v.getIncluded().size());
        assertEquals(_set("foo", "bar"), included);
        String tmp = v.toString();
        boolean test1 = tmp.equals("JsonIncludeProperties.Value(included=[foo, bar])");
        boolean test2 = tmp.equals("JsonIncludeProperties.Value(included=[bar, foo])");
        assertTrue(test1 || test2);
        assertEquals(v, JsonIncludeProperties.Value.from(Bogus.class.getAnnotation(JsonIncludeProperties.class)));
    }
}
