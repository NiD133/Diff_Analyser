package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.SpatialRelation;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BufferedLine}.
 */
public class BufferedLineTest {

    /**
     * A BufferedLine with an infinite buffer should conceptually cover the entire space
     * and therefore contain any other shape.
     */
    @Test
    public void relate_withInfiniteBuffer_shouldReturnContains() {
        // Arrange
        // Use the geodetic context (though for this test, Cartesian would also work).
        SpatialContext ctx = SpatialContext.GEO;

        // A BufferedLine created from a single point is a degenerate case. The key is the buffer.
        Point point = ctx.makePoint(0, 0);
        double infiniteBuffer = Double.POSITIVE_INFINITY;
        BufferedLine infinitelyBufferedLine = new BufferedLine(point, point, infiniteBuffer, ctx);

        // An arbitrary, simple rectangle to test against.
        Rectangle anyRectangle = ctx.makeRectangle(10, 20, 30, 40);

        // Act
        // Check the spatial relationship between the infinitely buffered line and the rectangle.
        SpatialRelation relation = infinitelyBufferedLine.relate(anyRectangle);

        // Assert
        // The infinitely buffered line is expected to contain any other shape.
        assertEquals(SpatialRelation.CONTAINS, relation);
        
        // Also, verify the buffer size was set correctly.
        assertEquals("Buffer should be infinite", infiniteBuffer, infinitelyBufferedLine.getBuf(), 0.0);
    }
}