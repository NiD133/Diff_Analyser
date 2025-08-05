package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link SimpleObjectIdResolver} to verify its core object identity management.
 * Each test focuses on a single behavior for clarity and maintainability.
 */
@DisplayName("SimpleObjectIdResolver")
class SimpleObjectIdResolverTest {

    private SimpleObjectIdResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new SimpleObjectIdResolver();
    }

    @Test
    @DisplayName("should correctly bind and resolve multiple different items")
    void shouldBindAndResolveMultipleItems() {
        // Arrange
        final ObjectIdGenerator.IdKey key1 = new ObjectIdGenerator.IdKey(String.class, null, "key1");
        final String object1 = "value1";
        final ObjectIdGenerator.IdKey key2 = new ObjectIdGenerator.IdKey(String.class, null, "key2");
        final String object2 = "value2";

        // Act
        resolver.bindItem(key1, object1);
        resolver.bindItem(key2, object2);

        // Assert
        assertEquals(object1, resolver.resolveId(key1), "Should resolve the first bound item.");
        assertEquals(object2, resolver.resolveId(key2), "Should resolve the second bound item.");
        // This assertion on internal state is acceptable for this simple data-holding class.
        assertEquals(2, resolver._items.size(), "Resolver should contain two distinct items.");
    }

    @Test
    @DisplayName("should be idempotent when binding the same item multiple times")
    void shouldBeIdempotentWhenBindingSameItemMultipleTimes() {
        // Arrange
        final ObjectIdGenerator.IdKey key = new ObjectIdGenerator.IdKey(String.class, null, "key1");
        final String object = "value1";
        resolver.bindItem(key, object);
        assertEquals(1, resolver._items.size(), "Resolver should contain one item after initial bind.");

        // Act: Re-bind the same key to the same object instance
        resolver.bindItem(key, object);

        // Assert: The state should not have changed
        assertEquals(1, resolver._items.size(), "Resolver size should not change on idempotent bind.");
        assertEquals(object, resolver.resolveId(key), "Resolver should still resolve to the same object.");
    }

    @Test
    @DisplayName("should throw an exception when re-binding an ID to a different object")
    void shouldFailToBindDifferentObjectToSameId() {
        // Arrange
        final ObjectIdGenerator.IdKey key = new ObjectIdGenerator.IdKey(String.class, null, "key1");
        final String initialObject = "value1";
        final String conflictingObject = "value3";
        resolver.bindItem(key, initialObject);

        // Act & Assert: Verify that an attempt to re-bind the key to a different object throws.
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            resolver.bindItem(key, conflictingObject);
        }, "Should throw IllegalStateException for an ID conflict.");

        // Assert: Verify the exception message for clarity and correctness.
        // Using a text block makes the expected multi-line message much more readable.
        final String expectedMessage = """
                Object Id conflict: Id [ObjectId: key=key1, type=java.lang.String, scope=NONE] already bound to an Object (type: `java.lang.String`, value: "value1"): attempt to re-bind to a different Object (type: `java.lang.String`, value: "value3")""";
        assertEquals(expectedMessage, exception.getMessage());
    }
}