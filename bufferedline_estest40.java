package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link BufferedLine} class.
 *
 * Note: The original class name "BufferedLine_ESTestTest40" and its scaffolding
 * are artifacts of a test generation tool (EvoSuite). A more conventional
 * name would be "BufferedLineTest".
 */
public class BufferedLine_ESTestTest40 extends BufferedLine_ESTest_scaffolding {

    /**
     * Tests that a {@link BufferedLine} with a positive buffer distance
     * correctly reports that it has an area.
     */
    @Test
    public void hasArea_shouldReturnTrue_forLineWithPositiveBuffer() {
        // Arrange
        // A buffered line is defined by two points, a buffer distance, and a spatial context.
        SpatialContext spatialContext = SpatialContext.GEO;
        Point point = new PointImpl(1.5, 1.5, spatialContext);
        double positiveBuffer = 1.5;

        // In this specific test case, the start and end points are the same. This creates
        // a shape equivalent to a buffered point (a circle). The principle remains the same.
        BufferedLine bufferedLine = new BufferedLine(point, point, positiveBuffer, spatialContext);

        // Act
        // The hasArea() method indicates if the shape occupies a 2D space.
        boolean result = bufferedLine.hasArea();

        // Assert
        // Any line with a non-zero buffer will have an area.
        assertTrue("A BufferedLine with a positive buffer should be considered to have an area.", result);
    }
}