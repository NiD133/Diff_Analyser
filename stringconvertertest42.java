package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link StringConverter#toString()} method.
 * This test verifies that the converter's string representation is correct.
 */
public class StringConverterToStringTest {

    /**
     * Tests that the toString() method returns a descriptive string
     * identifying the converter and the type it supports.
     */
    @Test
    public void toString_shouldReturnConverterDescription() {
        // Arrange
        StringConverter converter = StringConverter.INSTANCE;
        String expectedDescription = "Converter[java.lang.String]";

        // Act
        String actualDescription = converter.toString();

        // Assert
        assertEquals(expectedDescription, actualDescription);
    }
}