package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link BufferedLineString} class.
 */
public class BufferedLineStringTest {

    // Use the standard geodetic context for clarity and simplicity.
    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * This test verifies the behavior of the getArea() method when the BufferedLineString
     * is constructed with a point that has an invalid latitude (far greater than 90).
     * The current implementation calculates a specific negative area in this edge case,
     * and this test asserts that behavior is preserved.
     */
    @Test
    public void getArea_whenConstructedWithInvalidLatitude_calculatesNegativeArea() {
        // ARRANGE

        // Define constants for magic numbers to improve readability and maintainability.
        final double smallCoordinateAndBuffer = 0.017453292519943295;
        final double invalidLatitude = 465.46032036; // A latitude far outside the valid [-90, 90] range.
        final double expectedArea = -65.33078137174249;
        final double delta = 0.01;

        // Create a list of points, one of which has an invalid latitude.
        // Using the context's factory method is preferred over direct instantiation.
        Point pointWithInvalidLat = spatialContext.makePoint(-180.0, invalidLatitude);
        Point validPoint = spatialContext.makePoint(smallCoordinateAndBuffer, smallCoordinateAndBuffer);
        List<Point> points = Arrays.asList(pointWithInvalidLat, validPoint);

        // This test enables a specific feature that adjusts the buffer based on longitude skew.
        boolean expandBufForLongitudeSkew = true;

        // Create the BufferedLineString with the specified points, buffer, and context.
        BufferedLineString lineString = new BufferedLineString(
                points,
                smallCoordinateAndBuffer, // buffer value
                expandBufForLongitudeSkew,
                spatialContext);

        // ACT
        // Calculate the area of the shape.
        double actualArea = lineString.getArea(spatialContext);

        // ASSERT
        // This test verifies that for the given invalid input, the system produces a specific
        // negative area. This captures the system's behavior in this edge case.
        assertEquals("The buffer value should be correctly stored.",
                smallCoordinateAndBuffer, lineString.getBuf(), 0.0);
        assertEquals("Area for a line with an invalid latitude should be the expected negative value.",
                expectedArea, actualArea, delta);
    }
}