package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BufferedLineString} focusing on edge cases like empty inputs.
 */
public class BufferedLineStringTest {

    private final SpatialContext geoContext = SpatialContext.GEO;

    @Test
    public void shouldBeEmptyAndHaveNoAreaWhenCreatedWithNoPoints() {
        // Arrange
        // An empty list of points to define the line string.
        List<Point> emptyPoints = Collections.emptyList();
        double bufferDistance = 10.0; // A non-zero buffer.

        // Act
        // Create a BufferedLineString with no points.
        BufferedLineString emptyLineString = new BufferedLineString(
                emptyPoints, bufferDistance, false, geoContext);

        // Assert
        // The resulting shape should be considered empty.
        assertTrue("A line string with no points should be empty", emptyLineString.isEmpty());

        // Even with a positive buffer, an empty line string has no segments and thus no area.
        assertFalse("An empty line string should not have an area", emptyLineString.hasArea());

        // The buffer value should still be stored correctly.
        assertEquals("The buffer value should be retained",
                bufferDistance, emptyLineString.getBuf(), 0.0);
    }
}