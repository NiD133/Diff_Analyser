package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the static utility method {@link BufferedLine#expandBufForLongitudeSkew(Point, Point, double)}.
 * This method is designed to compensate for longitude distortion at higher latitudes.
 */
public class BufferedLineTest {

    private static final double DELTA = 1e-9;
    private final SpatialContext ctx = SpatialContext.GEO;

    /**
     * The formula for buffer expansion is: buf * (1 / cos(toRadians(max_latitude))).
     * At the equator (latitude 0), cos(0) is 1, so the buffer should not be expanded.
     */
    @Test
    public void expandBufForLongitudeSkew_atEquator_doesNotChangeBuffer() {
        // Arrange
        Point pointAtEquator = new PointImpl(10, 0, ctx);
        double buffer = 15.0;
        double expectedBuffer = 15.0;

        // Act
        double actualBuffer = BufferedLine.expandBufForLongitudeSkew(pointAtEquator, pointAtEquator, buffer);

        // Assert
        assertEquals(expectedBuffer, actualBuffer, DELTA);
    }

    /**
     * At higher latitudes, the buffer should be expanded. At 60 degrees, cos(60) is 0.5,
     * so the expansion factor is 1 / 0.5 = 2. The buffer should be doubled.
     */
    @Test
    public void expandBufForLongitudeSkew_atHighLatitude_returnsExpandedBuffer() {
        // Arrange
        Point pointAt60Degrees = new PointImpl(20, 60, ctx);
        double buffer = 10.0;
        // Expansion factor at 60 degrees latitude is 1/cos(60) = 2.0
        double expectedExpandedBuffer = buffer * 2.0;

        // Act
        double actualExpandedBuffer = BufferedLine.expandBufForLongitudeSkew(pointAt60Degrees, pointAt60Degrees, buffer);

        // Assert
        assertEquals(expectedExpandedBuffer, actualExpandedBuffer, DELTA);
    }

    /**
     * The method should use the latitude with the greatest absolute value from the two
     * provided points to calculate the expansion.
     */
    @Test
    public void expandBufForLongitudeSkew_usesMaximumLatitudeOfTwoPoints() {
        // Arrange
        Point lowerLatitudePoint = new PointImpl(10, -30, ctx); // abs(lat) = 30
        Point higherLatitudePoint = new PointImpl(20, 60, ctx);  // abs(lat) = 60
        double buffer = 10.0;
        // Expansion is based on the higher latitude (60 degrees), so factor is 2.0
        double expectedExpandedBuffer = buffer * 2.0;

        // Act
        double actualExpandedBuffer = BufferedLine.expandBufForLongitudeSkew(lowerLatitudePoint, higherLatitudePoint, buffer);

        // Assert
        assertEquals(expectedExpandedBuffer, actualExpandedBuffer, DELTA);
    }

    /**
     * The expansion logic should apply equally to negative buffer values.
     */
    @Test
    public void expandBufForLongitudeSkew_withNegativeBuffer_returnsNegativeExpandedBuffer() {
        // Arrange
        Point pointAt60Degrees = new PointImpl(20, 60, ctx);
        double negativeBuffer = -10.0;
        // Expansion factor at 60 degrees latitude is 2.0
        double expectedExpandedBuffer = negativeBuffer * 2.0; // -20.0

        // Act
        double actualExpandedBuffer = BufferedLine.expandBufForLongitudeSkew(pointAt60Degrees, pointAt60Degrees, negativeBuffer);

        // Assert
        assertEquals(expectedExpandedBuffer, actualExpandedBuffer, DELTA);
    }
}