package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link SimpleObjectIdResolver} class.
 */
public class SimpleObjectIdResolverTest {

    /**
     * Verifies that newForDeserialization() creates a new, distinct instance of
     * SimpleObjectIdResolver. This is important to ensure that each deserialization
     * process gets a fresh resolver without any shared state from previous operations.
     */
    @Test
    public void newForDeserialization_shouldReturnNewInstance() {
        // Arrange: Create an initial instance of the resolver.
        SimpleObjectIdResolver originalResolver = new SimpleObjectIdResolver();
        Object deserializationContext = null; // The context is not used in this implementation.

        // Act: Call the method to get a new resolver for a deserialization process.
        ObjectIdResolver newResolver = originalResolver.newForDeserialization(deserializationContext);

        // Assert: The returned resolver must be a new object, not the same instance.
        assertNotSame("The factory method should create a new resolver instance.", originalResolver, newResolver);
        assertTrue("The new resolver should be of type SimpleObjectIdResolver.", newResolver instanceof SimpleObjectIdResolver);
    }
}