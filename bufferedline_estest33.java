package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Test for the {@link BufferedLine#equals(Object)} method.
 */
public class BufferedLineEqualsTest {

    private final SpatialContext geoContext = SpatialContext.GEO;

    /**
     * Tests that two BufferedLine objects are not considered equal if they are defined
     * by different endpoints, even if they share a common point and have the same buffer size.
     *
     * This test specifically compares a standard line with a "degenerate" line
     * (where start and end points are identical).
     */
    @Test
    public void equals_shouldReturnFalse_whenLinesHaveDifferentEndpoints() {
        // Arrange
        final double buffer = 45.0;
        Point pointA = geoContext.makePoint(45.0, 45.0);
        Point pointB = geoContext.makePoint(-60.0, -70.0);

        // Create a "degenerate" line where the start and end points are the same.
        // This is effectively a buffered point.
        BufferedLine degenerateLine = new BufferedLine(pointA, pointA, buffer, geoContext);

        // Create a standard line with distinct start and end points.
        BufferedLine standardLine = new BufferedLine(pointA, pointB, buffer, geoContext);

        // Sanity check: ensure buffer sizes are as expected before comparison.
        assertEquals(buffer, degenerateLine.getBuf(), 0.0);
        assertEquals(buffer, standardLine.getBuf(), 0.0);

        // Act & Assert
        // The two lines should not be equal because their underlying geometries are different.
        assertNotEquals("A degenerate line should not equal a standard line", degenerateLine, standardLine);

        // For completeness, explicitly test the boolean return value of equals().
        boolean areEqual = degenerateLine.equals(standardLine);
        assertFalse("equals() should return false for lines with different endpoints", areEqual);
    }
}