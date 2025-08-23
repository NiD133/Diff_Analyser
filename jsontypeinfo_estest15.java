package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * This test suite focuses on the {@link JsonTypeInfo.Value} class, particularly its
 * behavior as an immutable value object.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that calling a 'with' method on an immutable {@link JsonTypeInfo.Value}
     * instance with its current value does not create a new object.
     * This is a common and important optimization pattern for immutable objects to reduce
     * unnecessary memory allocations.
     */
    @Test
    public void withRequireTypeIdForSubtypesShouldReturnSameInstanceWhenValueIsUnchanged() {
        // Arrange: The EMPTY instance has its 'requireTypeIdForSubtypes' property
        // set to null by default.
        final JsonTypeInfo.Value originalValue = JsonTypeInfo.Value.EMPTY;
        final Boolean unchangedValue = null;

        // Act: Call the 'with' method, providing the same value that the object already has.
        final JsonTypeInfo.Value result = originalValue.withRequireTypeIdForSubtypes(unchangedValue);

        // Assert: The method should return the original instance, not a new one.
        assertSame("Calling a 'with' method with an unchanged value should return the same instance.",
                originalValue, result);
    }
}