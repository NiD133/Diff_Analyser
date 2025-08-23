package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BufferedLine}.
 */
public class BufferedLineTest {

    private final SpatialContext spatialContext = SpatialContext.GEO;

    @Test
    public void getBufShouldReturnBufferSizeProvidedInConstructor() {
        // Arrange
        final double expectedBuffer = 0.0;
        // Create a degenerate line where the start and end points are the same.
        Point point = new PointImpl(0.0, 0.0, spatialContext);
        BufferedLine bufferedLine = new BufferedLine(point, point, expectedBuffer, spatialContext);

        // Act
        double actualBuffer = bufferedLine.getBuf();

        // Assert
        // The getBuf() method should simply return the buffer value passed during construction.
        assertEquals(expectedBuffer, actualBuffer, 0.0);
    }
}