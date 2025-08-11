package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleObjectIdResolverTest {
    private static final Class<String> STRING_TYPE = String.class;
    private static final String SCOPE = null;

    @Test
    void bindAndResolveItems() {
        // Setup: Create resolver with two distinct items
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey key1 = createKey("key1");
        ObjectIdGenerator.IdKey key2 = createKey("key2");

        // Action: Bind items
        resolver.bindItem(key1, "value1");
        resolver.bindItem(key2, "value2");

        // Verify: Items resolve correctly
        assertEquals("value1", resolver.resolveId(key1));
        assertEquals("value2", resolver.resolveId(key2));
        assertEquals(2, resolver._items.size());
    }

    @Test
    void rebindSameValueShouldNotChangeState() {
        // Setup: Bind initial value
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey key = createKey("testKey");
        resolver.bindItem(key, "value");

        // Action: Rebind same value
        resolver.bindItem(key, "value");

        // Verify: State remains consistent
        assertEquals("value", resolver.resolveId(key));
        assertEquals(1, resolver._items.size());
    }

    @Test
    void rebindDifferentValueShouldThrowException() {
        // Setup: Bind initial value
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey key = createKey("conflictKey");
        resolver.bindItem(key, "originalValue");

        // Action & Verify: Rebind different value throws
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> resolver.bindItem(key, "newValue"));

        String expectedMessage = 
            "Object Id conflict: Id [ObjectId: key=conflictKey, type=java.lang.String, scope=NONE] " +
            "already bound to an Object (type: `java.lang.String`, value: \"originalValue\"): " +
            "attempt to re-bind to a different Object (type: `java.lang.String`, value: \"newValue\")";
        assertEquals(expectedMessage, exception.getMessage());
    }

    private ObjectIdGenerator.IdKey createKey(String id) {
        return new ObjectIdGenerator.IdKey(STRING_TYPE, SCOPE, id);
    }
}