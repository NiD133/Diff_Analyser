package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Tests that multiplying a vector by an identity matrix results in the original,
     * unchanged vector. This is a fundamental property of vector-matrix multiplication.
     * The 'cross' method in this context performs this multiplication.
     */
    @Test
    public void crossWithIdentityMatrix_shouldReturnEqualVector() {
        // Arrange: Create a sample vector and an identity matrix.
        // The default Matrix() constructor creates an identity matrix.
        Vector originalVector = new Vector(15.0f, -25.5f, 10.0f);
        Matrix identityMatrix = new Matrix();

        // Act: Perform the vector-matrix multiplication.
        Vector resultVector = originalVector.cross(identityMatrix);

        // Assert: The resulting vector should be equal to the original.
        assertEquals(originalVector, resultVector);

        // Also assert that a new instance is returned, not the original object.
        assertNotSame("The cross method should return a new Vector instance.", originalVector, resultVector);
    }
}