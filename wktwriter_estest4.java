package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.impl.PointImpl;

import java.io.Writer;

/**
 * Test suite for the {@link WKTWriter} class.
 *
 * Note: The original test class was likely auto-generated. This version has been
 * refactored for improved readability and adherence to standard testing practices.
 */
public class WKTWriterTest {

    /**
     * Verifies that calling the write() method with a null Writer instance
     * results in a NullPointerException, as the writer is a required argument.
     */
    @Test(expected = NullPointerException.class)
    public void write_withNullWriter_throwsNullPointerException() {
        // Arrange
        WKTWriter wktWriter = new WKTWriter();
        Point anyPoint = new PointImpl(0.0, -1281.1, null);
        Writer nullWriter = null;

        // Act & Assert
        // The following call is expected to throw a NullPointerException,
        // which is handled and asserted by the @Test(expected=...) annotation.
        wktWriter.write(nullWriter, anyPoint);
    }
}