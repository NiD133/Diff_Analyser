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

    private static final SpatialContext GEO_CONTEXT = SpatialContext.GEO;

    /**
     * Tests that a BufferedLine correctly reports a DISJOINT relationship
     * with a rectangle that is clearly outside of its bounds.
     *
     * This test case uses a BufferedLine with identical start and end points,
     * which effectively behaves like a buffered point (a circle).
     */
    @Test
    public void relateWithDisjointRectangleShouldReturnDisjoint() {
        // Arrange
        double bufferDistance = 10.0;
        Point centerPoint = GEO_CONTEXT.makePoint(20, 20);

        // A BufferedLine with the same start and end point is effectively a circle.
        // Its bounding box will be approximately (10, 30, 10, 30).
        BufferedLine bufferedPoint = new BufferedLine(centerPoint, centerPoint, bufferDistance, GEO_CONTEXT);

        // Create a rectangle that is far away and does not intersect the buffered point.
        Rectangle disjointRectangle = GEO_CONTEXT.makeRectangle(100, 110, 100, 110);

        // Act
        SpatialRelation relation = bufferedPoint.relate(disjointRectangle);

        // Assert
        assertEquals("The relation should be DISJOINT as the shapes do not overlap.",
                SpatialRelation.DISJOINT, relation);
        assertEquals("The buffer size should be correctly stored.",
                bufferDistance, bufferedPoint.getBuf(), 0.0);
    }
}