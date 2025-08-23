package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Vector} class, focusing on edge cases like the zero vector.
 */
public class VectorTest {

    private static final float FLOAT_PRECISION = 0.001f;

    /**
     * Verifies that normalizing a zero vector and then calculating its cross product
     * with any matrix correctly results in a zero vector.
     */
    @Test
    public void crossProductOfNormalizedZeroVectorWithMatrix_ShouldReturnZeroVector() {
        // Arrange: Create a zero vector and an arbitrary matrix.
        // The specific matrix values do not matter, as any cross product with a zero vector is zero.
        Vector zeroVector = new Vector(0.0F, 0.0F, 0.0F);
        Matrix arbitraryMatrix = new Matrix(135.0F, 0.0F, 2.0F, 0.0F, 0.0F, -1.0F);
        Vector expectedVector = new Vector(0.0F, 0.0F, 0.0F);

        // Act: Normalize the zero vector and then find its cross product with the matrix.
        // Normalizing a zero-length vector is expected to return another zero-length vector.
        Vector normalizedZeroVector = zeroVector.normalize();
        Vector resultVector = normalizedZeroVector.cross(arbitraryMatrix);

        // Assert: The final result should be a zero vector.
        assertEquals(expectedVector, resultVector);
    }

    /**
     * Verifies that the length of a zero vector (a vector at the origin) is zero.
     * This test isolates the second assertion from the original, unclear test case.
     */
    @Test
    public void length_OfZeroVector_ShouldBeZero() {
        // Arrange
        Vector zeroVector = new Vector(0.0F, 0.0F, 0.0F);

        // Act
        float length = zeroVector.length();

        // Assert
        assertEquals(0.0F, length, FLOAT_PRECISION);
    }
}