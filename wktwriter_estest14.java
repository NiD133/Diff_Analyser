package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link WKTWriter}.
 * This refactored test focuses on a specific edge case from an auto-generated test.
 */
public class WKTWriterTest {

    private final SpatialContext spatialContext = SpatialContext.GEO;
    private final WKTWriter wktWriter = new WKTWriter();

    /**
     * Tests that writing a shape with NaN (Not-a-Number) coordinates
     * results in a WKT string where each NaN value is represented by the
     * Unicode replacement character (U+FFFD). This is an important test
     * for handling malformed or uninitialized geometric data gracefully.
     */
    @Test
    public void toString_withNaNCoordinates_shouldProduceReplacementCharacters() {
        // Arrange: Create a rectangle where all coordinate values are NaN.
        // This is a more direct way to create the test condition than the
        // original, which used a complex series of shape constructions.
        Rectangle rectangleWithNaN = spatialContext.getShapeFactory().rect(
                Double.NaN, Double.NaN, Double.NaN, Double.NaN);

        // Act: Convert the NaN-based rectangle to its WKT string representation.
        String actualWkt = wktWriter.toString((Shape) rectangleWithNaN);

        // Assert: The resulting string should use the Unicode replacement character '�'
        // for each of the NaN coordinate values in the ENVELOPE format.
        // The WKT format for a rectangle is ENVELOPE(minX, maxX, minY, maxY).
        String expectedWkt = "ENVELOPE (\uFFFD, \uFFFD, \uFFFD, \uFFFD)";
        assertEquals("WKT for a NaN rectangle should use replacement characters", expectedWkt, actualWkt);
    }
}