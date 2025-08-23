package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * This class contains tests for the {@link BufferedLine#equals(Object)} method.
 * Note: The original test class name "BufferedLine_ESTestTest34" and scaffolding
 * suggest it was auto-generated. This version has been refactored for clarity.
 */
public class BufferedLine_ESTestTest34 extends BufferedLine_ESTest_scaffolding {

    /**
     * Tests that the `equals` method returns false when comparing two `BufferedLine`
     * instances that have different line segments, even if their buffer sizes are identical.
     */
    @Test
    public void equals_shouldReturnFalse_whenLineSegmentsAreDifferent() {
        // Arrange
        SpatialContext spatialContext = new SpatialContext(new SpatialContextFactory());

        // Define points for two distinct lines.
        Point origin = new PointImpl(0.0, 0.0, spatialContext);
        Point distantPoint = new PointImpl(2040.083, 2040.083, spatialContext);

        // Create the first line, which is a zero-length line (effectively a point) at the origin.
        BufferedLine lineAtOrigin = new BufferedLine(origin, origin, 0.0, spatialContext);

        // Create a second line with a different segment but the same buffer size.
        BufferedLine differentLine = new BufferedLine(distantPoint, origin, 0.0, spatialContext);

        // Act
        // Compare the two BufferedLine objects for equality.
        boolean areEqual = differentLine.equals(lineAtOrigin);

        // Assert
        // The lines should not be equal because their underlying line segments differ.
        assertFalse("BufferedLines with different endpoints should not be equal.", areEqual);

        // Also, verify the buffer of the second line as a sanity check on the test setup.
        assertEquals("The buffer of the second line should be 0.0.", 0.0, differentLine.getBuf(), 0.01);
    }
}