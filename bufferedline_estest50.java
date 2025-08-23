package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;

/**
 * Test suite for {@link BufferedLine}.
 */
public class BufferedLineTest {

    private final SpatialContext spatialContext = new SpatialContext(new SpatialContextFactory());

    /**
     * Tests that {@link BufferedLine#expandBufForLongitudeSkew(Point, Point, double)}
     * throws an AssertionError when a point has a latitude outside the valid [-90, 90] range.
     * The method is expected to validate its inputs, and an invalid latitude should trigger
     * a precondition failure.
     */
    @Test(expected = AssertionError.class)
    public void expandBufForLongitudeSkew_whenLatitudeIsInvalid_throwsAssertionError() {
        // Arrange
        final double longitude = 57.3; // An arbitrary valid longitude
        final double validLatitude = 45.0; // An arbitrary valid latitude
        final double invalidLatitude = 5181.5; // Well outside the valid [-90, 90] range
        final double buffer = Math.PI / 4;

        Point validPoint = new PointImpl(longitude, validLatitude, spatialContext);
        Point pointWithInvalidLatitude = new PointImpl(longitude, invalidLatitude, spatialContext);

        // Act & Assert
        // This call should fail because pointWithInvalidLatitude has an out-of-range Y-coordinate.
        BufferedLine.expandBufForLongitudeSkew(pointWithInvalidLatitude, validPoint, buffer);
    }
}