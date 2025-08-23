package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Tests that the cross product of a vector with itself results in the zero vector.
     * This is a fundamental mathematical property: v x v = 0.
     */
    @Test
    public void crossProductWithSelfShouldReturnZeroVector() {
        // Arrange: Create a non-trivial vector. The specific values are not important,
        // as this property holds for any vector.
        Vector vector = new Vector(15.0f, -4.5f, 8.0f);
        Vector expectedZeroVector = new Vector(0.0f, 0.0f, 0.0f);

        // Act: Compute the cross product of the vector with itself.
        Vector result = vector.cross(vector);

        // Assert: The resulting vector should be the zero vector.
        assertEquals(expectedZeroVector, result);

        // We can also assert that the length of the resulting vector is zero.
        assertEquals(0.0f, result.length(), 0.001f);
    }
}