package com.itextpdf.text.pdf.parser;

import org.junit.Test;

/**
 * Unit tests for the {@link Vector} class, focusing on its core functionalities.
 */
public class VectorTest {

    /**
     * Verifies that the cross() method correctly throws a NullPointerException
     * when it is called with a null argument. This ensures the method handles
     * invalid input gracefully by failing fast.
     */
    @Test(expected = NullPointerException.class)
    public void cross_shouldThrowNullPointerException_when_argumentIsNull() {
        // Arrange: Create an arbitrary vector instance. The specific values are not important for this test.
        Vector vector = new Vector(1.0f, 2.0f, 3.0f);

        // Act: Attempt to compute the cross product with a null vector.
        // The test will pass only if a NullPointerException is thrown.
        vector.cross(null);
    }
}