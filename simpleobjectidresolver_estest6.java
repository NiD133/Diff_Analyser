package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link SimpleObjectIdResolver} class.
 */
public class SimpleObjectIdResolverTest {

    /**
     * This test verifies that attempting to bind an ID that is already associated
     * with an object to a new, different object results in an IllegalStateException.
     * This prevents an ID from being accidentally reassigned.
     */
    @Test
    public void bindItem_whenIdIsAlreadyBound_throwsIllegalStateExceptionOnRebind() {
        // Arrange: Create a resolver, define a unique ID, and bind it to an initial object.
        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        Object initialObject = new Object();
        
        // Use distinct types for the key's components to make the test's intent clear.
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey(
                Object.class, // Type
                String.class, // Scope
                "test-id-123" // Key
        );

        // The first binding of the ID to an object should succeed.
        resolver.bindItem(idKey, initialObject);

        // Act & Assert: Attempt to re-bind the same ID to a different object (in this case, null).
        // This action is expected to throw an IllegalStateException.
        try {
            resolver.bindItem(idKey, null);
            fail("Expected an IllegalStateException because the ID is already bound to a different object.");
        } catch (IllegalStateException e) {
            // Verify that the exception and its message are correct.
            String actualMessage = e.getMessage();
            String expectedMessageStart = "Object Id conflict";

            assertTrue(
                "Exception message should indicate an ID conflict.",
                actualMessage.startsWith(expectedMessageStart)
            );
            assertTrue(
                "Exception message should contain the string representation of the conflicting ID key.",
                actualMessage.contains(idKey.toString())
            );
        }
    }
}