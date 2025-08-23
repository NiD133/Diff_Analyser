package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonIncludeProperties.Value} class.
 */
public class JsonIncludePropertiesValueTest {

    /**
     * Tests that the `from()` factory method handles a null input gracefully
     * by returning the default "include all" instance.
     */
    @Test
    public void from_whenGivenNullAnnotation_shouldReturnDefaultInstance() {
        // Arrange: No setup needed as we are testing a static method with a null input.

        // Act
        JsonIncludeProperties.Value result = JsonIncludeProperties.Value.from(null);

        // Assert
        // The factory method should return a non-null instance for null input.
        assertNotNull("The result of from(null) should not be null.", result);

        // More specifically, it should return the singleton instance that represents
        // "include all properties", which is accessible via the all() method.
        assertSame("For a null input, the method should return the singleton 'ALL' instance.",
                JsonIncludeProperties.Value.all(), result);

        // The "include all" instance is defined as having a null set of included properties.
        assertNull("The default 'all' instance should have a null set for 'included'.",
                result.getIncluded());
    }
}