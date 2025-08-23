package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    private final SpatialContext spatialContext = new SpatialContext(new SpatialContextFactory());

    /**
     * This test verifies the behavior of creating a BufferedLineString with an empty list of points.
     * It ensures that the resulting shape is correctly identified as empty and that the
     * buffer value is stored correctly, even if it's a negative value.
     */
    @Test
    public void givenEmptyPointList_whenCreatingLineString_thenShapeIsEmptyAndBufferIsSet() {
        // Arrange
        final double bufferDistance = -2877.398196062; // A negative buffer is a valid input.
        List<Point> emptyPoints = Collections.emptyList();

        // Act
        BufferedLineString lineString = new BufferedLineString(emptyPoints, bufferDistance, true, spatialContext);

        // Assert
        assertTrue("A line string created with no points should be empty.", lineString.isEmpty());
        assertEquals("The buffer value should be correctly stored.", bufferDistance, lineString.getBuf(), 0.01);
    }
}