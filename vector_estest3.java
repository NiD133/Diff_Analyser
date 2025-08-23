package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains unit tests for vector operations in the {@link Vector} class.
 */
public class VectorOperationsTest {

    /**
     * Verifies that the length of a vector is calculated correctly after a chain of transformations:
     * 1. Subtraction of another vector.
     * 2. Transformation via a cross product with a matrix.
     * 3. Multiplication by a scalar.
     *
     * This test uses simple, predictable values to make the underlying mathematics easy to follow.
     */
    @Test
    public void lengthAfterChainedOperationsIsCalculatedCorrectly() {
        // --- Arrange ---
        // Define two simple, orthogonal vectors for a predictable starting point.
        Vector vectorA = new Vector(1.0f, 0.0f, 0.0f);
        Vector vectorB = new Vector(0.0f, 1.0f, 0.0f);

        // Define a 90-degree clockwise rotation matrix. This provides a simple, verifiable transformation.
        // For a vector (x, y), a 90-degree clockwise rotation results in (y, -x).
        // The iText Matrix(a, b, c, d, e, f) maps to:
        // x' = a*x + c*y + e*z
        // y' = b*x + d*y + f*z
        // To get x'=y and y'=-x, we need a=0, c=1, b=-1, d=0.
        Matrix rotationMatrix = new Matrix(0.0f, -1.0f, 1.0f, 0.0f, 0.0f, 0.0f);

        float scalarMultiplier = 5.0f;
        final float delta = 0.001f;

        // --- Act ---
        // 1. Subtract vectorB from vectorA: (1, 0, 0) - (0, 1, 0) = (1, -1, 0)
        Vector subtractedVector = vectorA.subtract(vectorB);

        // 2. Apply the rotation matrix: rotates (1, -1, 0) to (-1, -1, 0)
        Vector transformedVector = subtractedVector.cross(rotationMatrix);

        // 3. Scale the resulting vector: (-1, -1, 0) * 5.0 = (-5, -5, 0)
        Vector finalVector = transformedVector.multiply(scalarMultiplier);

        // 4. Get the length of the final vector
        float actualLength = finalVector.length();

        // --- Assert ---
        // The expected length is sqrt((-5)^2 + (-5)^2 + 0^2) = sqrt(25 + 25) = sqrt(50)
        float expectedLength = (float) Math.sqrt(50.0);
        assertEquals("The length of the final transformed vector is incorrect.", expectedLength, actualLength, delta);

        // Optional: Add assertions for intermediate steps to make debugging easier.
        assertEquals(new Vector(1.0f, -1.0f, 0.0f), subtractedVector);
        assertEquals(new Vector(-1.0f, -1.0f, 0.0f), transformedVector);
        assertEquals(new Vector(-5.0f, -5.0f, 0.0f), finalVector);
    }
}