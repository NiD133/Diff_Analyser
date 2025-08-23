package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Verifies that the equals() method correctly returns false for two vectors
     * that differ only in their Z-coordinate.
     */
    @Test
    public void equals_shouldReturnFalse_whenOnlyZCoordinatesDiffer() {
        // Arrange: Create two vectors with the same X and Y coordinates, but different Z coordinates.
        Vector vectorA = new Vector(0.0F, 0.0F, 3.3516045F);
        Vector vectorB = new Vector(0.0F, 1.0F);

        // Act & Assert: Verify that the two vectors are not considered equal.
        assertFalse("Vectors with different Z coordinates should not be equal.", vectorA.equals(vectorB));
    }
}