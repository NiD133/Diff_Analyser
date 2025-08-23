package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.ShapeCollection;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * The BufferedLineString constructor has special handling for a list containing a single point.
     * It should create a single line segment from that point to itself. This test verifies that
     * exactly one segment is created.
     */
    @Test
    public void whenConstructedWithSinglePoint_thenCreatesOneSegment() {
        // ARRANGE
        // A single point to construct the line string.
        Point point = spatialContext.makePoint(0, 0);
        List<Point> singlePointList = Collections.singletonList(point);
        double bufferDistance = 10.0;

        // ACT
        // Create the BufferedLineString with the single point.
        BufferedLineString lineString = new BufferedLineString(singlePointList, bufferDistance, false, spatialContext);
        ShapeCollection<BufferedLine> segments = lineString.getSegments();

        // ASSERT
        // Verify that the resulting shape contains exactly one segment.
        assertEquals(1, segments.size());

        // Further verify that the segment is a zero-length line from the point to itself.
        BufferedLine createdSegment = segments.get(0);
        assertEquals("Segment start point should match the input point", point, createdSegment.getA());
        assertEquals("Segment end point should match the input point", point, createdSegment.getB());
        assertEquals("Segment buffer should match the input buffer", bufferDistance, createdSegment.getBuf(), 0.0);
    }
}