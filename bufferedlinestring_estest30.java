package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Test for the equals() method of BufferedLineString.
 */
public class BufferedLineStringRefactoredTest {

    private final SpatialContext spatialContext = new SpatialContextFactory().newSpatialContext();

    /**
     * Tests that a BufferedLineString created with an empty list of points is not
     * equal to a BufferedLineString created with a single point.
     *
     * This test also implicitly verifies that the BufferedLineString constructor
     * makes a defensive copy of the input point list, as the original list is
     * modified after the first object's creation.
     */
    @Test
    public void equals_returnsFalse_whenComparingEmptyAndSinglePointLineStrings() {
        // Arrange
        final double buffer = -1827.67990034; // Using a negative buffer to test edge cases.
        List<Point> points = new LinkedList<>();

        // Create the first line string from an empty list of points.
        BufferedLineString emptyLineString = new BufferedLineString(points, buffer, false, spatialContext);

        // Now, modify the list to contain a single point (the center of the empty shape).
        Point centerOfEmptyShape = emptyLineString.getCenter();
        points.add(centerOfEmptyShape);

        // Create a second line string from the now single-element list.
        // Note: This uses a different constructor overload for completeness.
        BufferedLineString singlePointLineString = new BufferedLineString(points, buffer, spatialContext);

        // Act
        boolean areEqual = emptyLineString.equals(singlePointLineString);

        // Assert
        assertFalse("An empty line string should not be equal to a single-point line string", areEqual);

        // A more direct assertion:
        assertNotEquals(emptyLineString, singlePointLineString);
    }
}