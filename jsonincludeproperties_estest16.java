package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link JsonIncludeProperties.Value} class.
 * Note: The original test class name was auto-generated and has been renamed for clarity.
 */
public class JsonIncludePropertiesValueTest {

    /**
     * Verifies that calling {@link JsonIncludeProperties.Value#withOverrides} with a null
     * argument returns the original instance.
     * <p>
     * According to the method's contract, a null override is considered "undefined"
     * and should have no effect on the original value.
     */
    @Test
    public void withOverrides_whenGivenNull_shouldReturnSameInstance() {
        // Arrange: Create a base Value instance that represents including all properties.
        JsonIncludeProperties.Value initialValue = JsonIncludeProperties.Value.all();

        // Act: Attempt to apply a null override, which signifies no override configuration.
        JsonIncludeProperties.Value result = initialValue.withOverrides(null);

        // Assert: The method should return the exact same instance, confirming that a
        // null override is a no-op.
        assertSame("Overriding with null should not create a new instance but return the original.",
                initialValue, result);
    }
}