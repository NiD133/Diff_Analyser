package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Vector} class, focusing on geometric calculations.
 */
public class VectorTest {

    /**
     * A small tolerance for floating-point comparisons.
     */
    private static final float DELTA = 0.01f;

    /**
     * Verifies that the dot product of two vectors is calculated correctly.
     * The dot product of A(ax, ay, az) and B(bx, by, bz) is ax*bx + ay*by + az*bz.
     */
    @Test
    public void dotShouldCalculateCorrectDotProduct() {
        // Arrange: Define two vectors for the operation.
        Vector vectorA = new Vector(0.0f, 0.0f, -1.0f);
        Vector vectorB = new Vector(0.0f, -1.0f, 2.0f);

        // The expected dot product is (0.0 * 0.0) + (0.0 * -1.0) + (-1.0 * 2.0) = -2.0
        float expectedDotProduct = -2.0f;

        // Act: Calculate the dot product.
        float actualDotProduct = vectorA.dot(vectorB);

        // Assert: Verify the result is as expected.
        assertEquals(expectedDotProduct, actualDotProduct, DELTA);
    }

    /**
     * Verifies that the squared length of a vector is calculated correctly.
     * The squared length of V(x, y, z) is x*x + y*y + z*z.
     */
    @Test
    public void lengthSquaredShouldCalculateCorrectSquareOfLength() {
        // Arrange: Define the vector whose squared length will be calculated.
        Vector vector = new Vector(0.0f, -1.0f, 2.0f);

        // The expected squared length is (0.0^2) + (-1.0^2) + (2.0^2) = 0 + 1 + 4 = 5.0
        float expectedLengthSquared = 5.0f;

        // Act: Calculate the squared length.
        float actualLengthSquared = vector.lengthSquared();

        // Assert: Verify the result is as expected.
        assertEquals(expectedLengthSquared, actualLengthSquared, DELTA);
    }
}