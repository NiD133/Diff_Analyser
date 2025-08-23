package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Tests that subtracting a vector from itself results in a zero vector.
     * A zero vector is a vector where all components are zero.
     */
    @Test
    public void subtract_whenVectorIsSubtractedFromItself_shouldReturnZeroVector() {
        // Arrange: Create an arbitrary vector and the expected zero vector result.
        Vector vector = new Vector(10.5f, -20.0f, 30.1f);
        Vector expectedZeroVector = new Vector(0.0f, 0.0f, 0.0f);

        // Act: Subtract the vector from itself.
        Vector result = vector.subtract(vector);

        // Assert: The resulting vector should be the zero vector.
        assertEquals(expectedZeroVector, result);
    }
}