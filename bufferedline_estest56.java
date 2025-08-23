package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.SpatialRelation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BufferedLine_ESTestTest56 {

    /**
     * This test verifies that the `relate` method of a BufferedLine
     * correctly returns DISJOINT when compared against an empty Rectangle.
     *
     * An empty shape should not have any spatial relation (like INTERSECTS or CONTAINS)
     * with any other shape.
     *
     * Note: This test relies on creating a Rectangle where minY > maxY. In many
     * versions of Spatial4j, the RectangleImpl constructor validates its arguments and
     * would throw an IllegalArgumentException in this scenario. This test would only pass
     * in an environment with disabled validation or on an older library version.
     */
    @Test(timeout = 4000)
    public void relateWithEmptyRectangleShouldReturnDisjoint() {
        // Arrange: Set up the shapes for the test.
        SpatialContext geoContext = SpatialContext.GEO;

        // A simple line with no buffer. The original large, negative coordinates were
        // arbitrary and have been simplified to make the test's intent clearer.
        Point startPoint = new PointImpl(0, 0, geoContext);
        Point endPoint = new PointImpl(10, 10, geoContext);
        BufferedLine line = new BufferedLine(startPoint, endPoint, 0.0, geoContext);

        // Create an empty rectangle by making minY > maxY. The `relate()` method
        // should identify this as empty and return DISJOINT, even if its bounds
        // (e.g., x=0) would otherwise intersect the line.
        Rectangle emptyRectangle = new RectangleImpl(0, 0, 1, 0, geoContext);
        assertTrue("The test premise requires the rectangle to be empty.", emptyRectangle.isEmpty());

        // Act: Calculate the spatial relation between the line and the empty rectangle.
        SpatialRelation relation = line.relate(emptyRectangle);

        // Assert: The relation should be DISJOINT.
        assertEquals("The relation with an empty rectangle must be DISJOINT.",
                SpatialRelation.DISJOINT, relation);
    }
}