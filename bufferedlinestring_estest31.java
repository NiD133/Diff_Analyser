package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    /**
     * Tests that two BufferedLineString instances are not equal if their buffer sizes differ,
     * even if they are based on the same set of points.
     */
    @Test
    public void equals_shouldReturnFalse_whenBuffersDiffer() {
        // Arrange
        final SpatialContext spatialContext = SpatialContext.GEO;
        final List<Point> emptyPoints = Collections.emptyList();
        final double initialBuffer = 0.0;
        final double newBuffer = 1569.55;

        // Create an initial line string with a zero buffer.
        BufferedLineString initialLine = new BufferedLineString(emptyPoints, initialBuffer, spatialContext);

        // Act
        // Create a new line string by applying a new buffer to the initial one.
        BufferedLineString rebufferedLine = (BufferedLineString) initialLine.getBuffered(newBuffer, spatialContext);

        // Assert
        // First, verify the new line has the correct buffer size.
        assertEquals("The new buffer size should be correctly set.", newBuffer, rebufferedLine.getBuf(), 0.01);

        // The core assertion: the two lines should not be equal because their buffers are different.
        assertNotEquals("Line strings with different buffers should not be equal.", initialLine, rebufferedLine);
    }
}