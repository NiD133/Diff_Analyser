package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link JsonTypeInfo.Value} class.
 */
public class JsonTypeInfoValueTest {

    /**
     * Verifies that the static helper method {@code isEnabled()} correctly reports
     * the default {@code JsonTypeInfo.Value.EMPTY} instance as disabled.
     * <p>
     * Polymorphic type handling is considered "enabled" only if the type identifier
     * strategy is not {@code JsonTypeInfo.Id.NONE}. The EMPTY constant uses
     * {@code Id.NONE}, so it should be considered disabled.
     */
    @Test
    public void isEnabledShouldReturnFalseForEmptyConfiguration() {
        // Arrange: The EMPTY constant represents a default, disabled configuration.
        JsonTypeInfo.Value emptyConfig = JsonTypeInfo.Value.EMPTY;

        // Act: Check if this configuration is considered enabled.
        boolean isEnabled = JsonTypeInfo.Value.isEnabled(emptyConfig);

        // Assert: The result should be false, as the EMPTY value signifies no type info.
        assertFalse("The default EMPTY JsonTypeInfo.Value should be considered disabled.", isEnabled);
    }
}