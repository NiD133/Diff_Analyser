package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.shape.Point;

import java.text.NumberFormat;

/**
 * Test suite for the {@link WKTWriter} class.
 * Note: The original test class name 'WKTWriter_ESTestTest5' has been simplified
 * to 'WKTWriterTest' to follow standard Java testing conventions.
 */
public class WKTWriterTest {

    /**
     * Verifies that the append method throws a NullPointerException when the Point argument is null.
     * This ensures the method correctly handles invalid input and prevents unexpected errors.
     */
    @Test(expected = NullPointerException.class)
    public void append_withNullPoint_shouldThrowNullPointerException() {
        // Arrange
        WKTWriter wktWriter = new WKTWriter();
        StringBuilder buffer = new StringBuilder();
        NumberFormat numberFormat = NumberFormat.getInstance();

        // Act & Assert
        // This call is expected to throw a NullPointerException because the point is null.
        wktWriter.append(buffer, null, numberFormat);
    }
}