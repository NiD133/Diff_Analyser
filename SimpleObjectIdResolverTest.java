package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SimpleObjectIdResolverTest {

    @Test
    public void testObjectIdResolverBindingAndResolution() {
        // Initialize the ObjectIdResolver
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();

        // Create unique keys for testing
        ObjectIdGenerator.IdKey key1 = new ObjectIdGenerator.IdKey(String.class, null, "key1");
        ObjectIdGenerator.IdKey key2 = new ObjectIdGenerator.IdKey(String.class, null, "key2");

        // Bind unique items to the resolver and verify they can be resolved
        bindAndVerifyItem(resolver, key1, "value1");
        bindAndVerifyItem(resolver, key2, "value2");

        // Ensure the resolver contains exactly two items
        assertEquals(2, resolver._items.size(), "Expected two unique items to be bound");

        // Verify that rebinding the same key with the same value is allowed
        rebindingWithSameValueIsAllowed(resolver, key1, "value1");
        rebindingWithSameValueIsAllowed(resolver, key2, "value2");

        // Ensure the resolver still contains exactly two items
        assertEquals(2, resolver._items.size(), "Expected no additional items after rebinding with the same value");

        // Verify that rebinding a key with a different value throws an exception
        verifyRebindingWithDifferentValueThrowsException(resolver, key1, "value3");
    }

    private void bindAndVerifyItem(SimpleObjectIdResolver resolver, ObjectIdGenerator.IdKey key, String value) {
        resolver.bindItem(key, value);
        assertEquals(value, resolver.resolveId(key), "Resolved value did not match the expected value");
    }

    private void rebindingWithSameValueIsAllowed(SimpleObjectIdResolver resolver, ObjectIdGenerator.IdKey key, String value) {
        resolver.bindItem(key, value);
    }

    private void verifyRebindingWithDifferentValueThrowsException(SimpleObjectIdResolver resolver, ObjectIdGenerator.IdKey key, String newValue) {
        try {
            resolver.bindItem(key, newValue);
            fail("Expected an IllegalStateException for rebinding with a different value");
        } catch (IllegalStateException e) {
            String expectedMessage = "Object Id conflict: Id [ObjectId: key=key1, type=java.lang.String, scope=NONE]"
                    + " already bound to an Object (type: `java.lang.String`, value: \"value1\"):"
                    + " attempt to re-bind to a different Object (type: `java.lang.String`, value: \"value3\")";
            assertEquals(expectedMessage, e.getMessage(), "Exception message did not match the expected message");
        }
    }
}