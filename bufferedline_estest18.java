package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the static utility methods in {@link BufferedLine}.
 */
public class BufferedLineTest {

    /**
     * Verifies that expanding a zero-sized buffer results in a zero-sized buffer,
     * regardless of the line's position.
     */
    @Test
    public void expandBufForLongitudeSkew_withZeroBuffer_shouldReturnZero() {
        // Arrange
        // A zero-length line at the equator is used as a simple test case.
        final SpatialContext geoContext = SpatialContext.GEO;
        final Point point = new PointImpl(0.0, 0.0, geoContext);
        final double initialBuffer = 0.0;

        // Act
        final double expandedBuffer = BufferedLine.expandBufForLongitudeSkew(point, point, initialBuffer);

        // Assert
        final double expectedBuffer = 0.0;
        assertEquals("Expanding a zero buffer should always result in a zero buffer.",
                expectedBuffer, expandedBuffer, 0.0);
    }
}