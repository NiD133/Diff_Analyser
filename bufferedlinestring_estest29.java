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
// Renamed the class to follow standard conventions.
public class BufferedLineStringTest {

    /**
     * Tests that constructing a BufferedLineString with an empty list of points
     * results in a valid, empty shape.
     */
    @Test
    // Renamed the test method to clearly describe the scenario under test.
    public void shouldCreateEmptyShapeWhenConstructedWithEmptyPointList() {
        // --- Arrange ---
        // Use descriptive variable names and standard, immutable empty list.
        List<Point> emptyPoints = Collections.emptyList();
        // Use a standard, convenient way to get a SpatialContext.
        SpatialContext geoContext = SpatialContext.GEO;
        double bufferDistance = 0.0;

        // --- Act ---
        // The action is the creation of the BufferedLineString.
        BufferedLineString lineString = new BufferedLineString(emptyPoints, bufferDistance, geoContext);

        // --- Assert ---
        // Assert the expected properties of an empty BufferedLineString.
        // The test now clearly verifies the object's state.
        assertTrue("A line string with no points should be considered empty.", lineString.isEmpty());
        assertTrue("The collection of segments should be empty.", lineString.getSegments().isEmpty());
        assertEquals("The buffer should be set to the value provided in the constructor.",
                bufferDistance, lineString.getBuf(), 0.0);
    }
}