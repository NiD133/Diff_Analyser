package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Shape;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    private static final double COORD_AND_BUFFER = 16.128532143886176;

    @Test(timeout = 4000)
    public void relateOnBufferedShapeThrowsUnsupportedOperationException() {
        // ARRANGE
        final SpatialContext spatialContext = SpatialContext.GEO;

        // A BufferedLineString from a single point contains one BufferedLine segment
        // where the start and end points are the same.
        Point singlePoint = new PointImpl(COORD_AND_BUFFER, COORD_AND_BUFFER, spatialContext);
        List<Point> points = Collections.singletonList(singlePoint);
        BufferedLineString originalLineString = new BufferedLineString(points, COORD_AND_BUFFER, spatialContext);

        // Create a new shape by applying another buffer. The resulting shape is
        // still composed of BufferedLine segments.
        Shape bufferedShape = originalLineString.getBuffered(COORD_AND_BUFFER, spatialContext);

        // ACT & ASSERT
        // The relate() method is not implemented for the underlying BufferedLine segments.
        // Therefore, an UnsupportedOperationException is expected.
        try {
            bufferedShape.relate(originalLineString);
            fail("Expected an UnsupportedOperationException to be thrown, but it wasn't.");
        } catch (UnsupportedOperationException e) {
            // This is the expected outcome.
            // The original auto-generated test verified that the exception message is null.
            assertNull("The exception message was expected to be null.", e.getMessage());
        }
    }
}