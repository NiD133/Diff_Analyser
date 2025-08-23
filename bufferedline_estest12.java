package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link BufferedLine} class.
 */
public class BufferedLineTest {

    /**
     * Tests that the getBuf() method correctly returns the buffer value
     * that was provided in the constructor.
     */
    @Test
    public void getBufShouldReturnConstructorValue() {
        // Arrange
        final double EXPECTED_BUFFER = 1461.7;
        final SpatialContext spatialContext = SpatialContext.GEO;

        // A BufferedLine where the start and end points are the same is a valid
        // use case, effectively representing a buffered point.
        final Point point = new PointImpl(0.0, 0.0, spatialContext);
        final BufferedLine bufferedLine = new BufferedLine(point, point, EXPECTED_BUFFER, spatialContext);

        // Act
        double actualBuffer = bufferedLine.getBuf();

        // Assert
        // The returned buffer should be exactly what was passed to the constructor.
        assertEquals(EXPECTED_BUFFER, actualBuffer, 0.0);
    }
}