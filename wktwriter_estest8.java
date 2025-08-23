package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.shape.Shape;

/**
 * This test class contains the refactored test case.
 * In a real-world scenario, the original EvoSuite-generated class and scaffolding
 * would be replaced or integrated into a human-written test suite like this.
 */
public class WKTWriterTest {

    /**
     * Verifies that the toString() method throws a NullPointerException
     * when the provided shape is null. This ensures the method correctly
     * handles invalid input by failing fast.
     */
    @Test(expected = NullPointerException.class)
    public void toString_withNullShape_shouldThrowNullPointerException() {
        // Arrange: Create a WKTWriter instance to test.
        WKTWriter wktWriter = new WKTWriter();
        Shape nullShape = null;

        // Act & Assert: Call the method with a null shape.
        // The @Test(expected=...) annotation handles the assertion,
        // failing the test if a NullPointerException is not thrown.
        wktWriter.toString(nullShape);
    }
}