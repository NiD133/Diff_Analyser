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
    private static final float FLOAT_PRECISION = 0.001f;

    /**
     * Verifies that the length of a zero vector (a vector at the origin) is calculated as zero.
     */
    @Test
    public void length_shouldReturnZero_forZeroVector() {
        // Arrange: Create a zero vector at the origin (0, 0, 0).
        Vector zeroVector = new Vector(0.0F, 0.0F, 0.0F);
        float expectedLength = 0.0F;

        // Act: Calculate the length of the vector.
        float actualLength = zeroVector.length();

        // Assert: The calculated length should be zero.
        assertEquals(expectedLength, actualLength, FLOAT_PRECISION);
    }
}