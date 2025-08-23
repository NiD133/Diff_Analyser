package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * This test suite focuses on the {@link JacksonInject.Value} class.
 */
// The original class name and inheritance are preserved.
public class JacksonInject_ESTestTest28 extends JacksonInject_ESTest_scaffolding {

    /**
     * Tests that calling {@link JacksonInject.Value#withId(Object)} with the same ID
     * as the existing one returns the same instance, which is an expected optimization.
     */
    @Test
    public void withId_whenIdIsUnchanged_shouldReturnSameInstance() {
        // Arrange: Create an empty Value instance, which has a null ID.
        JacksonInject.Value emptyValue = JacksonInject.Value.empty();

        // Act: Call withId() with the same ID (null).
        JacksonInject.Value result = emptyValue.withId(null);

        // Assert: The method should return the identical instance, not a new one.
        assertSame("Calling withId() with an unchanged ID should return the same instance",
                emptyValue, result);
    }
}