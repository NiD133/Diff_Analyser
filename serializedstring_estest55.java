package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Verifies that the toString() method returns the original string value
     * that the SerializedString was constructed with.
     */
    @Test
    public void toStringShouldReturnOriginalStringValue() {
        // Arrange
        String expectedValue = "";
        SerializedString serializedString = new SerializedString(expectedValue);

        // Act
        String actualValue = serializedString.toString();

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}