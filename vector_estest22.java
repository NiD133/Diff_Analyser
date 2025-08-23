package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Tests that the squared length of a zero vector (a vector at the origin) is zero.
     *
     * The length of a vector (x, y, z) is sqrt(x^2 + y^2 + z^2).
     * The squared length is x^2 + y^2 + z^2.
     * For a zero vector (0, 0, 0), the squared length is 0^2 + 0^2 + 0^2 = 0.
     */
    @Test
    public void lengthSquared_forZeroVector_shouldReturnZero() {
        // Arrange: Create a vector at the origin.
        Vector zeroVector = new Vector(0.0F, 0.0F, 0.0F);
        float expectedLengthSquared = 0.0F;

        // Act: Calculate the squared length of the vector.
        float actualLengthSquared = zeroVector.lengthSquared();

        // Assert: The result should be 0. A small delta is used for float comparison.
        assertEquals("The squared length of a zero vector must be 0.",
                     expectedLengthSquared,
                     actualLengthSquared,
                     0.001F);
    }
}