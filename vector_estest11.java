package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * A small delta is used for floating-point comparisons to account for
     * potential precision errors.
     */
    private static final float DELTA = 0.001f;

    /**
     * Verifies that a Vector is constructed correctly and that its properties,
     * such as a specific coordinate and its squared length, are accurate.
     */
    @Test
    public void vectorProperties_shouldBeCorrectAfterConstruction() {
        // Arrange: Define vector components and create the vector.
        // Using simple, whole numbers makes the test's logic easy to verify.
        float x = 3.0f;
        float y = 4.0f;
        float z = 12.0f;
        Vector vector = new Vector(x, y, z);

        // Act: Retrieve a coordinate and calculate the squared length.
        float retrievedY = vector.get(Vector.I2); // Using the constant for the Y-coordinate index
        float actualLengthSquared = vector.lengthSquared();

        // Assert: Verify that the retrieved and calculated values are correct.
        // The expected squared length is x*x + y*y + z*z = 3*3 + 4*4 + 12*12 = 9 + 16 + 144 = 169.
        float expectedLengthSquared = 169.0f;

        assertEquals("The Y coordinate should be retrieved correctly.", y, retrievedY, DELTA);
        assertEquals("The squared length should be calculated correctly.",
                expectedLengthSquared, actualLengthSquared, DELTA);
    }
}