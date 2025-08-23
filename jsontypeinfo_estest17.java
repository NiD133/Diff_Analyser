package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * This test suite focuses on the behavior of the {@link JsonTypeInfo.Value} class.
 */
public class JsonTypeInfoValueTest {

    /**
     * Tests that calling `withIdVisible()` with the same value that the object
     * already possesses returns the original instance, not a new one.
     * This is an important optimization for immutable "wither" methods.
     */
    @Test
    public void withIdVisible_shouldReturnSameInstance_whenVisibilityIsUnchanged() {
        // Arrange: Create a JsonTypeInfo.Value instance with idVisible set to false.
        final boolean initialVisibility = false;
        final String propertyName = "testProperty";

        JsonTypeInfo.Value initialValue = JsonTypeInfo.Value.construct(
                JsonTypeInfo.Id.MINIMAL_CLASS,
                JsonTypeInfo.As.WRAPPER_OBJECT,
                propertyName,
                Object.class,
                initialVisibility,
                false // requireTypeIdForSubtypes
        );

        // Act: Call the wither method with the same visibility value.
        JsonTypeInfo.Value resultValue = initialValue.withIdVisible(initialVisibility);

        // Assert: The method should return the identical instance.
        assertSame(
            "Expected the same object instance when the visibility value is not changed.",
            initialValue,
            resultValue
        );
        
        // Sanity check to ensure other properties remain intact.
        assertEquals(propertyName, resultValue.getPropertyName());
    }
}