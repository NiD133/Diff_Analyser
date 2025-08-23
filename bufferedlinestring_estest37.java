package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.SpatialRelation;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BufferedLineString}.
 * This class focuses on improving a single, auto-generated test case.
 */
public class BufferedLineStringTest {

    private static final SpatialContext GEO_CONTEXT = SpatialContext.GEO;

    /**
     * Tests that a BufferedLineString created from a single point correctly
     * reports its spatial relation as WITHIN when compared to itself.
     *
     * According to the constructor's logic, a line string with one point is
     * treated as a zero-length line segment (i.e., a point) with a buffer,
     * effectively creating a circle. A shape should always be within itself.
     */
    @Test
    public void relate_withSelfForSinglePointLine_shouldReturnWithin() {
        // Arrange
        final double coordinate = 16.1285;
        final double bufferDistance = 16.1285;

        Point singlePoint = GEO_CONTEXT.makePoint(coordinate, coordinate);
        List<Point> points = Collections.singletonList(singlePoint);

        BufferedLineString lineString = new BufferedLineString(points, bufferDistance, GEO_CONTEXT);

        // Act
        SpatialRelation relation = lineString.relate(lineString);

        // Assert
        assertEquals("A shape's relation to itself should be WITHIN", SpatialRelation.WITHIN, relation);
    }
}