package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

/**
 * This test class contains tests for the {@link BufferedLine} class.
 * Note: The original test was auto-generated. This version has been refactored for clarity.
 */
public class BufferedLine_ESTestTest3 extends BufferedLine_ESTest_scaffolding {

    /**
     * Tests that {@link BufferedLine#expandBufForLongitudeSkew(Point, Point, double)}
     * throws an AssertionError when a point has a latitude outside the valid [-90, 90] range.
     *
     * The method performs geodetic calculations that are only meaningful for valid latitudes,
     * so it is expected to fail with an assertion when given invalid input.
     */
    @Test(expected = AssertionError.class)
    public void expandBufForLongitudeSkew_withInvalidLatitude_throwsAssertionError() {
        // ARRANGE
        // Use the standard geodetic context, where latitude validation is critical.
        SpatialContext geoContext = SpatialContext.GEO;

        // Define points for the line. One has a latitude that is intentionally
        // outside the valid geodetic range of [-90, 90] degrees.
        final double invalidLatitude = 100.0;
        final double validLatitude = 45.0;
        final double longitude = 0.0;
        final double buffer = 10.0;

        Point pointWithInvalidLatitude = new PointImpl(longitude, invalidLatitude, geoContext);
        Point pointWithValidLatitude = new PointImpl(longitude, validLatitude, geoContext);

        // ACT & ASSERT
        // This call is expected to throw an AssertionError because one of the points
        // has an invalid latitude, which should be caught by a precondition check.
        BufferedLine.expandBufForLongitudeSkew(pointWithInvalidLatitude, pointWithValidLatitude, buffer);
    }
}