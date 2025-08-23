package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Verifies that the lengthSquared() method correctly calculates the sum of the squares
     * of the vector's components. The test uses negative coordinates to ensure they are
     * handled correctly.
     */
    @Test
    public void lengthSquared_shouldReturnSumOfSquaresOfComponents() {
        // Arrange: Define the vector's components and create the vector instance.
        float x = -1465.0F;
        float y = -1465.0F;
        float z = 1.0F;
        Vector vector = new Vector(x, y, z);

        // Calculate the expected result explicitly to make the test's logic transparent.
        // The expected squared length is x² + y² + z².
        float expectedLengthSquared = (x * x) + (y * y) + (z * z);

        // Act: Call the method under test.
        float actualLengthSquared = vector.lengthSquared();

        // Assert: Verify that the actual result matches the expected result.
        // A small delta is used for floating-point comparisons to account for precision errors.
        assertEquals(expectedLengthSquared, actualLengthSquared, 0.01F);
    }
}