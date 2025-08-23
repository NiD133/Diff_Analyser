package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link BufferedLine}.
 */
public class BufferedLineTest {

    private final SpatialContext context = SpatialContext.GEO;

    /**
     * Tests that a BufferedLine with a positive buffer correctly reports that it has an area.
     * This holds true even for a degenerate line where the start and end points are identical.
     */
    @Test
    public void lineWithPositiveBufferShouldHaveArea() {
        // Arrange
        // A degenerate line (start and end points are the same) is effectively a buffered point.
        Point point = context.makePoint(0, 0);
        double buffer = 10.0;
        BufferedLine bufferedLine = new BufferedLine(point, point, buffer, context);

        // Act
        boolean hasArea = bufferedLine.hasArea();

        // Assert
        assertTrue("A buffered line with a positive buffer should have an area.", hasArea);
    }
}