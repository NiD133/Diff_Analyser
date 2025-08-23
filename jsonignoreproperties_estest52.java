package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link JsonIgnoreProperties.Value} class.
 */
public class JsonIgnorePropertiesValueTest {

    /**
     * Verifies that calling {@code withIgnoreUnknown()} on an instance that already has
     * {@code ignoreUnknown=true} returns the same instance, not a new one.
     * This is an important optimization to avoid redundant object creation.
     */
    @Test
    public void withIgnoreUnknown_shouldReturnSameInstance_whenPropertyIsAlreadyTrue() {
        // Arrange: Create a Value instance where ignoreUnknown is already true.
        JsonIgnoreProperties.Value initialValue = JsonIgnoreProperties.Value.forIgnoreUnknown(true);

        // Act: Call the 'with' method, which should be idempotent in this case.
        JsonIgnoreProperties.Value resultValue = initialValue.withIgnoreUnknown();

        // Assert:
        // The primary check is for object identity, confirming the optimization.
        assertSame("Expected the same instance to be returned when the value is unchanged.",
                initialValue, resultValue);

        // Also, verify the properties of the returned object are correct.
        assertTrue("ignoreUnknown should remain true", resultValue.getIgnoreUnknown());
        assertFalse("allowGetters should retain its default value of false", resultValue.getAllowGetters());
        assertFalse("allowSetters should retain its default value of false", resultValue.getAllowSetters());
        assertTrue("merge should retain its default value of true", resultValue.getMerge());
    }
}