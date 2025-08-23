package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link BufferedLine} class.
 */
public class BufferedLineTest {

    /**
     * The {@link BufferedLine#expandBufForLongitudeSkew(Point, Point, double)} method
     * is designed to compensate for the fact that a degree of longitude represents
     * a smaller distance as latitude increases away from the equator.
     *
     * This test verifies the base case: for points located on the equator (latitude 0),
     * where there is no longitudinal distortion, the method should return the original
     * buffer value without any modification.
     */
    @Test
    public void expandBufForLongitudeSkew_withPointsOnEquator_shouldReturnOriginalBuffer() {
        // Arrange
        // A geographic context is needed for creating points.
        final SpatialContext geoContext = SpatialContext.GEO;

        // The method's behavior depends on the latitude. At the equator (latitude 0),
        // no expansion should occur.
        final Point pointOnEquator = new PointImpl(0.0, 0.0, geoContext);
        final double originalBuffer = -43.390550600565;

        // Act
        // Calculate the expanded buffer for two identical points on the equator.
        final double expandedBuffer = BufferedLine.expandBufForLongitudeSkew(
                pointOnEquator, pointOnEquator, originalBuffer);

        // Assert
        // The returned buffer should be identical to the original.
        final double delta = 0.01;
        assertEquals(originalBuffer, expandedBuffer, delta);
    }
}