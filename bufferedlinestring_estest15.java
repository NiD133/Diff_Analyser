package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BufferedLineStringTest {

    private final SpatialContext ctx = SpatialContext.GEO;

    /**
     * Tests that the bounding box of a BufferedLineString created from a single point
     * with a negative buffer is simply the bounding box of that single point.
     * A negative or zero buffer should result in a zero-width line.
     */
    @Test
    public void getBoundingBox_withSinglePointAndNegativeBuffer_isCorrect() {
        // Arrange
        // A single point to define the line string.
        Point point = ctx.makePoint(10, 20);
        List<Point> points = Collections.singletonList(point);
        double negativeBuffer = -5.0; // A negative buffer should be treated as zero.

        // Act
        // Create a BufferedLineString from the single point.
        BufferedLineString lineWithSinglePoint = new BufferedLineString(points, negativeBuffer, ctx);
        Rectangle boundingBox = lineWithSinglePoint.getBoundingBox();

        // Assert
        // The bounding box of a single-point, zero-buffer line is just the point itself.
        assertEquals("minX should match point's x", 10.0, boundingBox.getMinX(), 0.0);
        assertEquals("maxX should match point's x", 10.0, boundingBox.getMaxX(), 0.0);
        assertEquals("minY should match point's y", 20.0, boundingBox.getMinY(), 0.0);
        assertEquals("maxY should match point's y", 20.0, boundingBox.getMaxY(), 0.0);
    }
}