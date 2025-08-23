package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link BufferedLineString} class, focusing on its properties.
 */
public class BufferedLineStringTest {

    /**
     * Tests that the getBuf() method correctly returns the buffer value
     * that was provided in the constructor.
     */
    @Test
    public void getBufShouldReturnConstructorValue() {
        // Arrange
        final double expectedBuffer = 1580.1346;
        List<Point> emptyPoints = Collections.emptyList();
        SpatialContext spatialContext = SpatialContext.GEO; // Use a standard, simple context

        // Act
        BufferedLineString lineString = new BufferedLineString(emptyPoints, expectedBuffer, spatialContext);
        double actualBuffer = lineString.getBuf();

        // Assert
        assertEquals("The buffer value should match the one set in the constructor.",
                expectedBuffer, actualBuffer, 0.01);
    }
}