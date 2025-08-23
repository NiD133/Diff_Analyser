package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Shape;

import java.util.Collections;
import java.util.List;

/**
 * Unit tests for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    private final SpatialContext geoContext = SpatialContext.GEO;

    /**
     * Tests that calling the relate() method with a null argument
     * correctly throws a NullPointerException. This is a standard contract test
     * for methods that do not permit null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void relateWithNullShapeShouldThrowNullPointerException() {
        // Arrange: Create a simple BufferedLineString with one point.
        // The constructor handles this by creating a zero-length line segment.
        Point point = geoContext.makePoint(10, 20);
        List<Point> points = Collections.singletonList(point);
        double bufferDistance = 5.0;
        BufferedLineString lineString = new BufferedLineString(points, bufferDistance, geoContext);

        // Act: Call the method under test with a null argument.
        // The @Test(expected) annotation will handle the assertion.
        lineString.relate((Shape) null);
    }
}