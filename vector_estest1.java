package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * A small tolerance is needed for floating-point comparisons.
     */
    private static final float DELTA = 0.001f;

    @Test
    public void dot_withSelf_shouldReturnSquareOfMagnitude() {
        // Arrange
        // Define the components of the vector.
        float x = -528.75f;
        float y = -528.75f;
        float z = 1.0f;
        Vector vector = new Vector(x, y, z);

        // The dot product of a vector with itself is mathematically equivalent to the
        // square of its magnitude: V · V = |V|² = x² + y² + z².
        float expectedDotProduct = (x * x) + (y * y) + (z * z);

        // Act
        // Calculate the dot product of the vector with itself.
        float actualDotProduct = vector.dot(vector);

        // Assert
        // Verify that the calculated dot product matches the expected value within a small tolerance.
        assertEquals(expectedDotProduct, actualDotProduct, DELTA);
    }
}