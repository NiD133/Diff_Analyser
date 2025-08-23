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
    private static final float FLOAT_PRECISION_DELTA = 0.01f;

    /**
     * Tests that the dot product of a vector with itself is equal to the square of its magnitude.
     * The dot product of a vector V(x, y, z) with itself is mathematically defined as (x*x + y*y + z*z),
     * which is also the square of the vector's length.
     */
    @Test
    public void dot_withSelf_shouldReturnSquareOfMagnitude() {
        // Arrange: Create a vector with non-zero components.
        float x = 0.0f;
        float y = 0.0f;
        float z = 742.77f;
        Vector vector = new Vector(x, y, z);

        // The expected result is the sum of the squares of the components.
        float expectedDotProduct = (x * x) + (y * y) + (z * z);

        // Act: Calculate the dot product of the vector with itself.
        float actualDotProduct = vector.dot(vector);

        // Assert: The calculated dot product should match the expected value.
        assertEquals(expectedDotProduct, actualDotProduct, FLOAT_PRECISION_DELTA);
    }
}