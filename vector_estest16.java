package com.itextpdf.text.pdf.parser;

import org.junit.Test;

/**
 * Test suite for the {@link Vector} class.
 */
public class VectorTest {

    /**
     * Verifies that the subtract method throws a NullPointerException
     * when the argument is null. This is the expected behavior for
     * methods that do not explicitly handle null inputs.
     */
    @Test(expected = NullPointerException.class)
    public void subtract_shouldThrowNullPointerException_whenArgumentIsNull() {
        // Arrange: Create an instance of the Vector. The specific coordinates
        // are not relevant for this test case.
        Vector vector = new Vector(1.0f, 2.0f, 3.0f);

        // Act & Assert: Attempt to subtract a null vector.
        // The @Test(expected) annotation handles the assertion,
        // ensuring a NullPointerException is thrown.
        vector.subtract(null);
    }
}