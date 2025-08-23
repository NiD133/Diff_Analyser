package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Tests that transforming a zero vector by an identity matrix
     * results in the same zero vector.
     *
     * The Vector#cross(Matrix) method performs a vector-matrix transformation.
     * A default-constructed Matrix is an identity matrix.
     */
    @Test
    public void crossWithIdentityMatrix_withZeroVector_returnsZeroVector() {
        // Arrange: Create a zero vector and an identity matrix.
        Vector zeroVector = new Vector(0.0f, 0.0f, 0.0f);
        Matrix identityMatrix = new Matrix(); // Default constructor creates an identity matrix

        // Act: Transform the zero vector by the identity matrix.
        Vector transformedVector = zeroVector.cross(identityMatrix);

        // Assert: The result of transforming a zero vector should still be a zero vector.
        assertEquals(zeroVector, transformedVector);
    }
}