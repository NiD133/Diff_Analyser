package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Tests that the length of a vector with negative floating-point coordinates is calculated correctly.
     * The length should be the Euclidean norm: sqrt(x^2 + y^2 + z^2).
     */
    @Test
    public void lengthShouldBeCalculatedCorrectlyForVectorWithNegativeCoordinates() {
        // Arrange: Define the vector's components and create the vector object.
        // Using the values from the original test to preserve the specific scenario.
        float x = -2905.637f;
        float y = -2905.637f;
        float z = -1.0f;
        Vector vector = new Vector(x, y, z);

        // Calculate the expected length based on the Euclidean distance formula.
        // This makes the test's logic self-documenting.
        double expectedLength = Math.sqrt(x * x + y * y + z * z); // Approx. 4109.191

        // Act: Call the method under test.
        float actualLength = vector.length();

        // Assert: Verify the actual length matches the expected value within a small tolerance
        // for floating-point inaccuracies.
        assertEquals(
            "The vector length should be calculated correctly using the Euclidean norm.",
            (float) expectedLength,
            actualLength,
            0.001f
        );
    }
}