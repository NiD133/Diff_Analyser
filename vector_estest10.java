package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Vector} class.
 */
public class VectorTest {

    private static final float DELTA = 0.001f;

    /**
     * Tests that the subtract method correctly calculates the difference between two vectors.
     * The resulting vector's components should be the difference of the corresponding
     * components of the original vectors.
     */
    @Test
    public void subtract_shouldReturnVectorWithCorrectCoordinates() {
        // Arrange: Define the two vectors for the subtraction operation.
        // The minuend is the vector from which the other is subtracted.
        // The subtrahend is the vector being subtracted.
        Vector minuend = new Vector(0.0f, 0.0f, 0.0f);
        Vector subtrahend = new Vector(-1465.0f, -1465.0f, 1.0f);

        // Act: Perform the subtraction.
        Vector result = minuend.subtract(subtrahend);

        // Assert: Verify that each component of the resulting vector is correct.
        // Expected result: (0 - (-1465), 0 - (-1465), 0 - 1) -> (1465, 1465, -1)
        assertEquals(1465.0f, result.get(Vector.I1), DELTA);
        assertEquals(1465.0f, result.get(Vector.I2), DELTA);
        assertEquals(-1.0f, result.get(Vector.I3), DELTA);
    }
}