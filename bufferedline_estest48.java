package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.SpatialRelation;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BufferedLine}.
 */
public class BufferedLineTest {

    private final SpatialContext geoContext = SpatialContext.GEO;

    /**
     * A BufferedLine with the same start and end point and a zero buffer
     * is a degenerate case that should behave like a single point. This test
     * verifies that such a line correctly reports that it contains its own point.
     */
    @Test
    public void relate_withZeroLengthAndZeroBuffer_shouldContainItself() {
        // Arrange: Create a degenerate line that is effectively a single point.
        // A zero-length line (start point equals end point) with a zero buffer.
        Point point = new PointImpl(0.0, 0.0, geoContext);
        BufferedLine pointAsLine = new BufferedLine(point, point, 0.0, geoContext);

        // Act: Check the spatial relationship of the line to its own point.
        SpatialRelation relation = pointAsLine.relate(point);

        // Assert: The line should contain the point.
        assertEquals("The buffer should be zero as specified", 0.0, pointAsLine.getBuf(), 0.0);
        assertEquals("A zero-length, zero-buffer line should contain its own point",
                SpatialRelation.CONTAINS, relation);
    }
}