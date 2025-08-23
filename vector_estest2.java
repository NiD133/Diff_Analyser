package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * A small tolerance for floating-point comparisons.
     */
    private static final float DELTA = 0.01f;

    @Test
    public void dot_withZeroVector_shouldReturnZero() {
        // Arrange
        Vector zeroVector = new Vector(0.0f, 0.0f, 0.0f);
        Vector anyVector = new Vector(1103.0195f, 0.0f, 8.0f);

        // Act
        float dotProduct = zeroVector.dot(anyVector);

        // Assert
        assertEquals(0.0f, dotProduct, DELTA);
    }

    @Test
    public void length_shouldCalculateCorrectMagnitude() {
        // Arrange
        Vector vector = new Vector(1103.0195f, 0.0f, 8.0f);
        // Expected length is sqrt(1103.0195^2 + 0^2 + 8.0^2) ≈ 1103.0486
        float expectedLength = 1103.0486f;

        // Act
        float actualLength = vector.length();

        // Assert
        assertEquals(expectedLength, actualLength, DELTA);
    }
}