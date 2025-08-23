package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * A small tolerance for floating-point comparisons.
     */
    private static final float DELTA = 0.0001f;

    /**
     * Verifies that the length of a vector is calculated correctly after a sequence
     * of cross product and subtraction operations.
     *
     * This test uses standard basis vectors for clarity and ease of verification,
     * making the expected outcome intuitive.
     */
    @Test
    public void length_afterSubtractingCrossProduct_isCalculatedCorrectly() {
        // ARRANGE: Define two standard basis vectors, i and j.
        // These are simple, orthogonal vectors that make the math easy to follow.
        Vector vectorI = new Vector(1.0f, 0.0f, 0.0f);
        Vector vectorJ = new Vector(0.0f, 1.0f, 0.0f);

        // ACT:
        // 1. Calculate the cross product of i and j.
        //    In a right-handed coordinate system, i x j = k, where k is (0, 0, 1).
        Vector crossProduct = vectorI.cross(vectorJ);

        // 2. Subtract the cross product (k) from the original vector (i).
        //    The result should be (1, 0, 0) - (0, 0, 1) = (1, 0, -1).
        Vector resultVector = vectorI.subtract(crossProduct);

        // 3. Calculate the length of the resulting vector.
        float actualLength = resultVector.length();

        // ASSERT:
        // The expected length of vector (1, 0, -1) is sqrt(1^2 + 0^2 + (-1)^2) = sqrt(2).
        float expectedLength = (float) Math.sqrt(2.0);
        assertEquals(expectedLength, actualLength, DELTA);
    }
}