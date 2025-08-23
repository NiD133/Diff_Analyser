package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link BufferedLine} class, focusing on its basic properties
 * like equality and area.
 */
public class BufferedLineTest {

    // A shared spatial context for all tests in this class.
    // Using GEO, as in the original test, though a non-geo context would also work.
    private static final SpatialContext CONTEXT = SpatialContext.GEO;

    /**
     * Tests the reflexivity property of the equals method.
     * An object must always be equal to itself.
     */
    @Test
    public void equals_withSameInstance_shouldReturnTrue() {
        // Arrange: Create a BufferedLine. In this case, the start and end points
        // are the same, effectively creating a buffered point (a circle).
        Point point = new PointImpl(45, 45, CONTEXT);
        double buffer = 10.0;
        BufferedLine bufferedLine = new BufferedLine(point, point, buffer, CONTEXT);

        // Act: Check for equality with the same instance.
        boolean isEqual = bufferedLine.equals(bufferedLine);

        // Assert: The result should be true.
        assertTrue("A BufferedLine instance must be equal to itself.", isEqual);
    }

    /**
     * Tests that a BufferedLine reports having an area when created with a positive buffer.
     */
    @Test
    public void hasArea_withPositiveBuffer_shouldReturnTrue() {
        // Arrange: Create a standard BufferedLine with a positive buffer value.
        Point startPoint = new PointImpl(30, 30, CONTEXT);
        Point endPoint = new PointImpl(50, 50, CONTEXT);
        double positiveBuffer = 5.0;
        BufferedLine bufferedLine = new BufferedLine(startPoint, endPoint, positiveBuffer, CONTEXT);

        // Act: Check if the shape has an area.
        boolean hasArea = bufferedLine.hasArea();

        // Assert: The result should be true.
        assertTrue("A BufferedLine with a positive buffer should have an area.", hasArea);
    }
}