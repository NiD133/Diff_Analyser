package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BufferedLine}.
 */
public class BufferedLineTest {

    /**
     * Tests the string representation of a degenerate BufferedLine,
     * which has zero length and a zero buffer.
     */
    @Test
    public void toString_forZeroLengthLineWithZeroBuffer_returnsCorrectFormat() {
        // Arrange
        final SpatialContext geoContext = SpatialContext.GEO;
        final Point origin = new PointImpl(0.0, 0.0, geoContext);
        
        // A line from a point to itself with no buffer is a degenerate case.
        final BufferedLine zeroLengthLine = new BufferedLine(origin, origin, 0.0, geoContext);
        
        final String expectedString = "BufferedLine(Pt(x=0.0,y=0.0), Pt(x=0.0,y=0.0) b=0.0)";

        // Act
        final String actualString = zeroLengthLine.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }
}