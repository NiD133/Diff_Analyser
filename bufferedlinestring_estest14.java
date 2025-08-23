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
 * Tests for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    /**
     * The BufferedLineString constructor handles the special case of a single point by
     * creating a line segment from the point to itself. This test verifies that the
     * bounding box for such a line is a non-empty rectangle that is geometrically
     * equivalent to the point itself.
     */
    @Test
    public void getBoundingBox_forLineWithSinglePoint_shouldBeThePointItself() {
        // Arrange
        SpatialContext geoContext = SpatialContext.GEO;
        Point singlePoint = new PointImpl(0.0, -1.0, geoContext);
        List<Point> points = Collections.singletonList(singlePoint);
        double zeroBuffer = 0.0;

        BufferedLineString lineString = new BufferedLineString(points, zeroBuffer, geoContext);

        // Act
        Rectangle boundingBox = lineString.getBoundingBox();

        // Assert
        // A point-like rectangle is not considered "empty" in Spatial4j
        assertFalse("Bounding box of a single-point line should not be empty", boundingBox.isEmpty());

        // Verify the bounding box is effectively the point itself
        assertEquals("Min X should match point's X", 0.0, boundingBox.getMinX(), 0.0);
        assertEquals("Max X should match point's X", 0.0, boundingBox.getMaxX(), 0.0);
        assertEquals("Min Y should match point's Y", -1.0, boundingBox.getMinY(), 0.0);
        assertEquals("Max Y should match point's Y", -1.0, boundingBox.getMaxY(), 0.0);
    }
}