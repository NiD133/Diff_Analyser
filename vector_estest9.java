package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Verifies that the length() method correctly calculates the Euclidean norm (magnitude)
     * of a vector.
     */
    @Test
    public void length_shouldCalculateCorrectMagnitudeForVector() {
        // Arrange: Create a vector with specific coordinates.
        float x = -1465.0f;
        float y = -1465.0f;
        float z = 1.0f;
        Vector vector = new Vector(x, y, z);

        // The expected length is the square root of the sum of the squares of the components.
        // sqrt((-1465.0)^2 + (-1465.0)^2 + 1.0^2) = sqrt(4292451) ≈ 2071.823
        double expectedLength = Math.sqrt(x * x + y * y + z * z);
        float delta = 0.01f; // A small tolerance is needed for floating-point comparisons.

        // Act: Calculate the length of the vector.
        float actualLength = vector.length();

        // Assert: The calculated length should match the expected value within the tolerance.
        assertEquals((float) expectedLength, actualLength, delta);
    }
}