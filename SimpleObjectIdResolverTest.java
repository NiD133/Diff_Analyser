package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleObjectIdResolverTest {

    private static ObjectIdGenerator.IdKey key(String id) {
        return new ObjectIdGenerator.IdKey(String.class, null, id);
    }

    @Test
    public void bindsDistinctIdsAndResolvesThem() {
        // Arrange
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey key1 = key("key1");
        ObjectIdGenerator.IdKey key2 = key("key2");

        // Act
        resolver.bindItem(key1, "value1");
        resolver.bindItem(key2, "value2");

        // Assert
        assertEquals("value1", resolver.resolveId(key1), "Should resolve the first id to its bound value");
        assertEquals("value2", resolver.resolveId(key2), "Should resolve the second id to its bound value");
        assertEquals(2, resolver._items.size(), "Two distinct ids should result in two stored entries");
    }

    @Test
    public void rebindingSameKeyWithSameValueIsNoOp() {
        // Arrange
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey key1 = key("key1");
        ObjectIdGenerator.IdKey key2 = key("key2");
        resolver.bindItem(key1, "value1");
        resolver.bindItem(key2, "value2");
        assertEquals(2, resolver._items.size(), "Sanity check: two entries after initial binding");

        // Act: bind the same keys to the exact same values again
        resolver.bindItem(key1, "value1");
        resolver.bindItem(key2, "value2");

        // Assert: values unchanged, no new entries
        assertEquals("value1", resolver.resolveId(key1));
        assertEquals("value2", resolver.resolveId(key2));
        assertEquals(2, resolver._items.size(), "Re-binding same key/value must not add entries");
    }

    @Test
    public void rebindingSameKeyWithDifferentValueThrows() {
        // Arrange
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        ObjectIdGenerator.IdKey key1 = key("key1");
        resolver.bindItem(key1, "value1");

        // Act + Assert
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> resolver.bindItem(key1, "value3"),
                "Re-binding a key to a different value must be rejected"
        );

        String expectedMessage =
                "Object Id conflict: Id [ObjectId: key=key1, type=java.lang.String, scope=NONE]"
                + " already bound to an Object (type: `java.lang.String`, value: \"value1\"):"
                + " attempt to re-bind to a different Object (type: `java.lang.String`, value: \"value3\")";
        assertEquals(expectedMessage, ex.getMessage());
    }
}