package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BufferedLine}.
 */
public class BufferedLineTest {

    /**
     * Tests that the getBuf() method correctly returns the buffer size
     * provided during the construction of a BufferedLine.
     */
    @Test
    public void getBufShouldReturnZeroWhenLineIsCreatedWithZeroBuffer() {
        // Arrange
        // Use a standard spatial context for simplicity.
        SpatialContext spatialContext = SpatialContext.GEO;

        // Create a degenerate line (a point) for the test. The coordinates are arbitrary
        // as they do not affect the buffer size property.
        Point startAndEndPoint = new PointImpl(10, 20, spatialContext);
        double expectedBuffer = 0.0;

        // Act
        // Create a BufferedLine with a zero buffer.
        BufferedLine line = new BufferedLine(startAndEndPoint, startAndEndPoint, expectedBuffer, spatialContext);
        double actualBuffer = line.getBuf();

        // Assert
        // Verify that the stored buffer is the one we provided.
        // A delta of 0.0 is used for an exact comparison.
        assertEquals(expectedBuffer, actualBuffer, 0.0);
    }
}