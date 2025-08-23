package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link BufferedLine} class.
 */
public class BufferedLineTest {

    private final SpatialContext geoContext = SpatialContext.GEO;

    /**
     * Verifies that the getBuf() method correctly returns the buffer value
     * provided during the construction of a BufferedLine. This test specifically
     * checks the case where the buffer is zero.
     */
    @Test
    public void getBufShouldReturnZeroWhenConstructedWithZeroBuffer() {
        // Arrange
        // A BufferedLine with a zero buffer is essentially a line segment.
        // We use a degenerate case where the start and end points are the same,
        // effectively creating a buffered point with a zero-radius buffer.
        Point point = new PointImpl(0.0, 0.0, geoContext);
        double expectedBuffer = 0.0;

        // Act
        // Create the buffered line with the specified points and zero buffer.
        BufferedLine bufferedLine = new BufferedLine(point, point, expectedBuffer, geoContext);
        double actualBuffer = bufferedLine.getBuf();

        // Assert
        // The buffer value retrieved from the object must match the value used for its creation.
        assertEquals(expectedBuffer, actualBuffer, 0.01);
    }
}