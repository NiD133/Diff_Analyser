package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * This test suite focuses on the behavior of the {@link JsonTypeInfo.Value} class,
 * specifically its factory methods.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that calling {@code withInclusionType()} with the same value
     * returns the identical instance. This confirms an optimization that avoids
     * unnecessary object allocation when no change is made.
     */
    @Test
    public void withInclusionType_whenTypeIsUnchanged_returnsSameInstance() {
        // Arrange
        // The static JsonTypeInfo.Value.EMPTY instance has an inclusion type of As.PROPERTY by default.
        JsonTypeInfo.Value initialValue = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.As sameInclusionType = JsonTypeInfo.As.PROPERTY;

        // Act
        JsonTypeInfo.Value result = initialValue.withInclusionType(sameInclusionType);

        // Assert
        // The method should return the same instance as an optimization, not a new one.
        assertSame("Expected the same instance when the inclusion type is not changed.",
                initialValue, result);
    }
}