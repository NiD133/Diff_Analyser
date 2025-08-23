package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.SpatialRelation;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BufferedLine}.
 */
public class BufferedLineTest {

    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * Tests a degenerate case where a BufferedLine has zero length (start and end points are the same)
     * and a zero buffer. In this scenario, the line is effectively a single point. The test
     * verifies that this point-like line contains its own bounding box, which is also a point.
     */
    @Test
    public void whenLineIsZeroLengthAndBufferIsZero_thenItContainsItself() {
        // Arrange
        // A point at (0,0) which will serve as both start and end of the line.
        Point point = new PointImpl(0.0, 0.0, spatialContext);

        // A line with zero length (from the point to itself) and zero buffer.
        // This is effectively just the original point.
        BufferedLine zeroLengthZeroBufferLine = new BufferedLine(point, point, 0.0, spatialContext);

        // The bounding box of a point is a rectangle of zero width and height at the same location.
        Rectangle pointBoundingBox = point.getBoundingBox();

        // Act
        // Check the spatial relationship between the line and the point's bounding box.
        SpatialRelation relation = zeroLengthZeroBufferLine.relate(pointBoundingBox);

        // Assert
        // The line (which is a point) should contain its own bounding box (which is also the same point).
        assertEquals("A zero-length, zero-buffer line should contain itself.",
                SpatialRelation.CONTAINS, relation);
        
        // Sanity check that the buffer was correctly initialized.
        assertEquals("The buffer of the line should be 0.0.",
                0.0, zeroLengthZeroBufferLine.getBuf(), 0.01);
    }
}