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
     * Tests that the getBuf() method correctly returns the buffer value
     * provided in the constructor. This test specifically uses a negative buffer
     * value and an empty list of points to ensure the getter is not
     * dependent on these inputs.
     */
    @Test
    public void getBufShouldReturnConstructorValue() {
        // Arrange
        final double expectedBuffer = -2877.398196062;
        final List<Point> emptyPoints = Collections.emptyList();
        final SpatialContext spatialContext = SpatialContext.GEO;

        BufferedLineString lineString = new BufferedLineString(
                emptyPoints,
                expectedBuffer,
                true, // expandBufForLongitudeSkew
                spatialContext
        );

        // Act
        double actualBuffer = lineString.getBuf();

        // Assert
        assertEquals(expectedBuffer, actualBuffer, 0.01);
    }
}