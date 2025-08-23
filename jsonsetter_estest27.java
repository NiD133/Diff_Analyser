package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonSetter.Value} class.
 */
public class JsonSetterValueTest {

    /**
     * Verifies that the toString() method on a JsonSetter.Value instance
     * returns a clear and accurate string representation of its state.
     */
    @Test
    public void toStringShouldReturnCorrectStringRepresentation() {
        // Arrange
        // Create an instance with default values for both valueNulls and contentNulls.
        JsonSetter.Value jsonSetterValue = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        String expectedString = "JsonSetter.Value(valueNulls=DEFAULT,contentNulls=DEFAULT)";

        // Act
        String actualString = jsonSetterValue.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }
}