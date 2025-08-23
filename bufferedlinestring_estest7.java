package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * The source code for BufferedLineString has a special case for a list containing a single point.
     * It creates a line segment from that point to itself, resulting in a non-empty shape.
     * This test verifies that behavior.
     */
    @Test
    public void isEmpty_shouldReturnFalse_whenConstructedWithASinglePoint() {
        // ARRANGE
        double bufferDistance = 10.0;
        Point singlePoint = spatialContext.makePoint(0, 0);
        List<Point> points = Collections.singletonList(singlePoint);

        BufferedLineString lineString = new BufferedLineString(points, bufferDistance, spatialContext);

        // ACT
        boolean isEmpty = lineString.isEmpty();

        // ASSERT
        assertFalse("A BufferedLineString created with one point should not be empty.", isEmpty);
    }
}