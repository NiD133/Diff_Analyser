package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Verifies that the toString() method returns a string representation
     * of the vector's coordinates, separated by commas.
     */
    @Test
    public void toString_shouldReturnCommaSeparatedCoordinates() {
        // Arrange: Create a vector with specific float coordinates, including negative values.
        Vector vector = new Vector(-2905.637F, -2905.637F, -1.0F);
        String expectedString = "-2905.637,-2905.637,-1.0";

        // Act: Get the string representation of the vector.
        String actualString = vector.toString();

        // Assert: The actual string should match the expected format.
        assertEquals(expectedString, actualString);
    }
}