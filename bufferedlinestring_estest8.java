package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link BufferedLineString}.
 * Note: The original test class name and inheritance from scaffolding suggest it was
 * auto-generated. A more conventional name would be BufferedLineStringTest.
 */
public class BufferedLineString_ESTestTest8 extends BufferedLineString_ESTest_scaffolding {

    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * Tests that a BufferedLineString created with a single point and a positive
     * buffer is considered to have an area. Internally, this is treated as a
     * line segment from the point to itself, which when buffered, forms a shape
     * equivalent to a circle.
     */
    @Test
    public void hasArea_shouldReturnTrue_forSinglePointLineWithBuffer() {
        // Arrange
        double bufferDistance = 10.0;
        Point point = spatialContext.makePoint(0, 0);
        List<Point> singlePointList = Collections.singletonList(point);

        // A BufferedLineString with a single point is a special case. It's treated
        // as a line from the point to itself.
        BufferedLineString lineString = new BufferedLineString(singlePointList, bufferDistance, spatialContext);

        // Act
        boolean result = lineString.hasArea();

        // Assert
        // When buffered, this "line" (effectively a point) becomes a circle, which has an area.
        assertTrue("A single-point BufferedLineString with a positive buffer should have an area.", result);
    }
}