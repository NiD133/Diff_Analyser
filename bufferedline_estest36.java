package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link BufferedLine}.
 * This class contains the refactored test case.
 */
public class BufferedLine_ESTestTest36 extends BufferedLine_ESTest_scaffolding {

    /**
     * Tests that a BufferedLine instance is not equal to an object of a different type (e.g., a Point),
     * even if the line is geometrically equivalent to a point (i.e., zero-length and zero-buffer).
     * The equals() method contract requires objects to be of the same type for equality.
     */
    @Test
    public void bufferedLineShouldNotBeEqualToObjectOfDifferentType() {
        // Arrange
        SpatialContext context = SpatialContext.GEO;
        Point point = new PointImpl(0.0, 0.0, context);

        // Create a degenerate line that is geometrically a point:
        // it has zero length (start and end points are the same) and a zero buffer.
        BufferedLine zeroSizedLine = new BufferedLine(point, point, 0.0, context);

        // Act
        // The .equals() method should return false due to the type mismatch.
        boolean isEqual = zeroSizedLine.equals(point);

        // Assert
        assertFalse("A BufferedLine instance should not be equal to a Point instance.", isEqual);
        
        // Also, verify the buffer was correctly initialized as a sanity check.
        assertEquals("The buffer of the line should be 0.0.", 0.0, zeroSizedLine.getBuf(), 0.0);
    }
}