package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link BufferedLine}.
 */
public class BufferedLineTest {

    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * Tests the properties of the perpendicular line for a degenerate BufferedLine,
     * which is a line where the start and end points are the same. This effectively
     * represents a buffered point.
     *
     * The implementation treats this case by creating a perpendicular line that is
     * vertical and passes through the point.
     */
    @Test
    public void getLinePerp_forDegenerateLine_returnsVerticalLineWithCorrectProperties() {
        // Arrange
        // A degenerate line is created from a single point.
        double pointX = 10.0;
        double pointY = 20.0;
        double buffer = 5.0;
        Point singlePoint = new PointImpl(pointX, pointY, spatialContext);

        // Create a buffered line where the start and end points are identical.
        BufferedLine degenerateLine = new BufferedLine(singlePoint, singlePoint, buffer, spatialContext);

        // Act
        // Get the line that is perpendicular to the (zero-length) primary line.
        InfBufLine perpendicularLine = degenerateLine.getLinePerp();

        // Assert
        // For a degenerate line, the perpendicular line is vertical. Its "intercept"
        // is its constant x-coordinate.
        assertEquals("Intercept of a vertical perpendicular line should be the point's x-coordinate",
                pointX, perpendicularLine.getIntercept(), 0.0);

        // The perpendicular line should inherit the buffer from the original BufferedLine.
        assertEquals("Buffer of the perpendicular line should match the original buffer",
                buffer, perpendicularLine.getBuf(), 0.0);
        
        // A buffered point (degenerate line) is a circle and should have an area.
        assertTrue("A degenerate buffered line should have area", degenerateLine.hasArea());
    }
}