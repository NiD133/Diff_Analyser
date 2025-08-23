package com.itextpdf.text.pdf.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Vector} class.
 * Note: The original test class name "Vector_ESTestTest7" and its scaffolding
 * are indicative of auto-generated code. It has been renamed to "VectorTest"
 * for clarity and convention.
 */
public class VectorTest {

    /**
     * Verifies that multiplying a vector by a scalar of 0.0F results in a new
     * zero-length vector, and confirms that the original vector is not modified (is immutable).
     */
    @Test
    public void multiplyByZero_returnsZeroVectorAndLeavesOriginalUnchanged() {
        // Arrange: Create a vector with a known length.
        Vector originalVector = new Vector(0.0F, 0.0F, 742.77F);
        float expectedOriginalLength = 742.77F;

        // Act: Multiply the vector by zero.
        Vector resultVector = originalVector.multiply(0.0F);

        // Assert: Check the properties of the new and original vectors.
        assertEquals("The resulting vector should have a length of zero.", 0.0F, resultVector.length(), 0.01F);
        assertEquals("The original vector should remain unchanged.", expectedOriginalLength, originalVector.length(), 0.01F);
    }
}