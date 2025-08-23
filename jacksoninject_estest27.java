package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * This test suite focuses on the {@link JacksonInject.Value} class.
 * The original class name "JacksonInject_ESTestTest27" was auto-generated.
 */
public class JacksonInject_ESTestTest27 extends JacksonInject_ESTest_scaffolding {

    /**
     * Verifies that calling `withId()` with the same ID that the `Value` object
     * already possesses returns the original instance, not a new one.
     * This behavior is an important optimization to avoid unnecessary object allocation.
     */
    @Test
    public void withId_whenIdIsUnchanged_shouldReturnSameInstance() {
        // Arrange: Create an ID and a JacksonInject.Value instance with that ID.
        Object injectionId = new Object();
        JacksonInject.Value initialValue = JacksonInject.Value.forId(injectionId);

        // Act: Call the withId() method with the exact same ID.
        JacksonInject.Value resultValue = initialValue.withId(injectionId);

        // Assert: The returned object should be the same instance as the original.
        assertSame("Expected withId() to return the same instance for an unchanged ID",
                initialValue, resultValue);
    }
}