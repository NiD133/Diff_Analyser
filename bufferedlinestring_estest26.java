package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    /**
     * Tests that creating a BufferedLineString with an empty list of points
     * results in a valid, empty shape.
     */
    @Test
    public void constructorWithEmptyPointsListShouldCreateEmptyLineString() {
        // Arrange: Set up the test conditions
        final SpatialContext spatialContext = SpatialContext.GEO;
        final List<Point> emptyPoints = Collections.emptyList();
        final double buffer = 0.0;

        // Act: Create the BufferedLineString with the empty list
        BufferedLineString lineString = new BufferedLineString(emptyPoints, buffer, spatialContext);

        // Assert: Verify the properties of the resulting object
        assertTrue("A LineString created with no points should be empty.", lineString.isEmpty());
        assertTrue("getPoints() should return an empty list.", lineString.getPoints().isEmpty());
        assertEquals("The buffer should be correctly initialized.", buffer, lineString.getBuf(), 0.0);
    }
}