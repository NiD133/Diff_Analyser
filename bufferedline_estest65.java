package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BufferedLine}.
 */
public class BufferedLineTest {

    private final SpatialContext geoContext = SpatialContext.GEO;

    /**
     * A BufferedLine created from two identical points represents a zero-length line.
     * This test verifies that for such a line:
     * 1. The center is the point itself.
     * 2. The buffer value is correctly stored.
     */
    @Test
    public void shouldCorrectlyHandleZeroLengthLine() {
        // Arrange: Create a zero-length line with a zero buffer, which is effectively a point.
        Point origin = new PointImpl(0.0, 0.0, geoContext);
        BufferedLine zeroLengthLine = new BufferedLine(origin, origin, 0.0, geoContext);

        // Act: Get the center of the line.
        Point center = zeroLengthLine.getCenter();

        // Assert: The center should be the original point, and the buffer should be zero.
        assertEquals("The center of a zero-length line should be the point itself.", origin, center);
        assertEquals("The buffer should be the value provided at construction.", 0.0, zeroLengthLine.getBuf(), 0.0);
    }
}