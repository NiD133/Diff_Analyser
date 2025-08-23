package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Verifies that the {@link SerializedString#getValue()} method
     * returns the original string value it was constructed with.
     * This test specifically uses an empty string as input.
     */
    @Test
    public void getValueShouldReturnTheOriginalEmptyString() {
        // Arrange: Create a SerializedString with an empty string.
        String expectedValue = "";
        SerializedString serializedString = new SerializedString(expectedValue);

        // Act: Retrieve the value from the SerializedString.
        String actualValue = serializedString.getValue();

        // Assert: The retrieved value should be identical to the original.
        assertEquals(expectedValue, actualValue);
    }
}