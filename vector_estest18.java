package com.itextpdf.text.pdf.parser;

import org.junit.Test;

/**
 * Unit tests for the {@link Vector} class, focusing on exception handling and invalid inputs.
 */
public class VectorTest {

    /**
     * Verifies that the dot() method throws a NullPointerException
     * when its argument is null.
     */
    @Test(expected = NullPointerException.class)
    public void dotShouldThrowNullPointerExceptionWhenArgumentIsNull() {
        // Arrange: Create an arbitrary vector instance. The specific coordinates are not
        // relevant for this test, as the method should fail before using them.
        Vector vector = new Vector(4, 3, 8);

        // Act & Assert: Call the dot() method with a null argument.
        // The @Test(expected) annotation handles the assertion, ensuring that a
        // NullPointerException is thrown.
        vector.dot(null);
    }
}