package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class, focusing on the getValue() method.
 */
public class SerializedStringTest {

    /**
     * Tests that the getValue() method correctly returns the original string
     * that was used to create the SerializedString instance.
     */
    @Test
    public void getValue_shouldReturnTheOriginalString() {
        // Arrange
        final String expectedValue = "TP1aO6Zd";
        SerializedString serializedString = new SerializedString(expectedValue);

        // Act
        String actualValue = serializedString.getValue();

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}