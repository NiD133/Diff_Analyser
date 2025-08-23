package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    /**
     * Tests that the bounding box of a BufferedLineString created with a single point
     * and zero buffer is essentially the bounding box of that single point.
     *
     * According to the implementation, a line string with one point is treated as a
     * zero-length line segment from that point to itself.
     */
    @Test
    public void getBoundingBox_withSinglePointAndZeroBuffer_isCorrect() {
        // Arrange
        final SpatialContext geoContext = SpatialContext.GEO;
        final Point origin = new PointImpl(0.0, 0.0, geoContext);
        final List<Point> points = Collections.singletonList(origin);
        final double zeroBuffer = 0.0;

        // The 'expandBufForLongitudeSkew' parameter is not relevant here as the line has zero length.
        BufferedLineString lineString = new BufferedLineString(points, zeroBuffer, true, geoContext);

        // Act
        Rectangle boundingBox = lineString.getBoundingBox();

        // Assert
        // The bounding box for a single point should be a "point" rectangle (zero width and height).
        assertEquals("Min X should be the point's X coordinate", 0.0, boundingBox.getMinX(), 0.0);
        assertEquals("Max X should be the point's X coordinate", 0.0, boundingBox.getMaxX(), 0.0);
        assertEquals("Min Y should be the point's Y coordinate", 0.0, boundingBox.getMinY(), 0.0);
        assertEquals("Max Y should be the point's Y coordinate", 0.0, boundingBox.getMaxY(), 0.0);

        // Consequently, the bounding box should have no area.
        assertFalse("Bounding box of a single point should not have area", boundingBox.hasArea());
    }
}