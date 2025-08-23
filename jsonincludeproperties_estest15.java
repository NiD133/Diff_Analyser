package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JsonIncludeProperties.Value} class.
 */
public class JsonIncludePropertiesValueTest {

    /**
     * Tests that the toString() method for the constant {@code Value.ALL}
     * returns the expected string representation. This constant signifies that
     * no specific properties are filtered, which is represented internally by a
     * null set of included properties.
     */
    @Test
    public void toString_forAllValue_shouldReturnExpectedFormat() {
        // Arrange
        JsonIncludeProperties.Value allPropertiesValue = JsonIncludeProperties.Value.ALL;
        String expectedToString = "JsonIncludeProperties.Value(included=null)";

        // Act
        String actualToString = allPropertiesValue.toString();

        // Assert
        assertEquals(expectedToString, actualToString);
    }
}