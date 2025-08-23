package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    /**
     * Tests that the constructor correctly initializes a BufferedLineString
     * with an empty list of points and correctly stores the provided buffer value.
     * This also verifies that a negative buffer is accepted, even though the
     * documentation suggests otherwise.
     */
    @Test
    public void constructorWithEmptyPointsShouldStoreBufferCorrectly() {
        // Arrange
        final double expectedBuffer = -1498.3962;
        final double delta = 0.01;
        
        // Use a standard spatial context for simplicity.
        SpatialContext spatialContext = SpatialContext.GEO;
        List<Point> emptyPoints = Collections.emptyList();

        // Act
        BufferedLineString lineString = new BufferedLineString(emptyPoints, expectedBuffer, false, spatialContext);

        // Assert
        // Verify that the buffer value was stored correctly.
        assertEquals(expectedBuffer, lineString.getBuf(), delta);
    }
}