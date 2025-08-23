package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link JsonIncludeProperties.Value} class, focusing on its configuration-merging logic.
 */
public class JsonIncludePropertiesValueTest {

    /**
     * Verifies that when the base {@code Value} is "undefined" (represented by {@code Value.ALL}),
     * the {@code withOverrides} method returns the overriding value instance.
     *
     * <p>According to the method's contract, overriding an "undefined" value should simply
     * adopt the configuration of the override value.</p>
     */
    @Test
    public void withOverrides_whenBaseIsUndefined_shouldReturnOverrideValue() {
        // Arrange
        // 1. Define a base value that is "undefined". Value.ALL represents this state,
        //    meaning no specific properties have been configured for inclusion.
        JsonIncludeProperties.Value baseValue = JsonIncludeProperties.Value.ALL;

        // 2. Define an override value. In this case, it is also "undefined".
        //    The original test created this using a mock, but using Value.ALL directly
        //    is clearer and achieves the same result, as Value.from() with a null
        //    or empty property array returns the singleton Value.ALL instance.
        JsonIncludeProperties.Value overrideValue = JsonIncludeProperties.Value.ALL;

        // Act
        // Apply the override to the "undefined" base value.
        JsonIncludeProperties.Value result = baseValue.withOverrides(overrideValue);

        // Assert
        // The result should be the exact same instance as the override value,
        // confirming that the override value was returned as-is.
        assertSame("Overriding an 'undefined' value should return the override value itself.",
                overrideValue, result);
    }
}