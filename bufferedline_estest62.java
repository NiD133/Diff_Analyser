package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link BufferedLine} class.
 */
public class BufferedLineTest {

    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * Tests that the getBuf() method correctly returns the buffer value
     * that was provided in the constructor.
     */
    @Test
    public void getBufShouldReturnTheConstructedBufferValue() {
        // Arrange
        Point point = new PointImpl(0.0, 0.0, spatialContext);
        double expectedBuffer = 0.0;

        // A zero-length line is created here, but the test's focus is on the buffer.
        BufferedLine bufferedLine = new BufferedLine(point, point, expectedBuffer, spatialContext);

        // Act
        double actualBuffer = bufferedLine.getBuf();

        // Assert
        assertEquals(expectedBuffer, actualBuffer, 0.0);
    }
}