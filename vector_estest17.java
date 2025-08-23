package com.itextpdf.text.pdf.parser;

import org.junit.Test;

/**
 * Unit tests for the {@link Vector} class, focusing on exception handling.
 */
public class VectorTest {

    /**
     * Verifies that the get() method throws an ArrayIndexOutOfBoundsException
     * when an index outside the valid range [0, 2] is provided.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void get_withInvalidIndex_shouldThrowArrayIndexOutOfBoundsException() {
        // Arrange: Create a vector. Its coordinate values are not relevant for this test.
        Vector vector = new Vector(10.0f, 20.0f, 30.0f);
        int invalidIndex = 3; // Any index > 2 or < 0 is invalid.

        // Act: Attempt to access a component with an out-of-bounds index.
        // Assert: The @Test(expected=...) annotation handles the assertion,
        // ensuring an ArrayIndexOutOfBoundsException is thrown.
        vector.get(invalidIndex);
    }
}