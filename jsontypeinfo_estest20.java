package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * This test class evaluates the behavior of the {@link JsonTypeInfo.Value} class.
 * Note: The original test was auto-generated and has been rewritten for clarity.
 */
public class JsonTypeInfo_ESTestTest20 {

    /**
     * Verifies that calling {@code withIdType()} with the same {@code Id} value
     * returns the original {@code JsonTypeInfo.Value} instance.
     * This confirms an identity-based optimization where a new object is not
     * created if the state does not change.
     */
    @Test
    public void withIdType_shouldReturnSameInstance_whenIdTypeIsUnchanged() {
        // Arrange
        JsonTypeInfo.Id currentIdType = JsonTypeInfo.Id.MINIMAL_CLASS;
        JsonTypeInfo.Value initialValue = JsonTypeInfo.Value.construct(
                currentIdType,
                JsonTypeInfo.As.WRAPPER_OBJECT,
                "testProperty",
                Object.class, // defaultImpl
                false,        // idVisible
                false         // requireTypeIdForSubtypes
        );

        // Act
        // Call the 'wither' method with the same Id type it already has.
        JsonTypeInfo.Value resultValue = initialValue.withIdType(currentIdType);

        // Assert
        // The method should return the exact same instance, not a new one.
        assertSame("Expected withIdType to return the same instance for an unchanged value.",
                initialValue, resultValue);
    }
}