package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * This test class contains improved tests for the {@link BufferedLineString} class.
 * The original test was auto-generated and has been refactored for better understandability.
 */
public class BufferedLineStringTest {

    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * Tests that the equals() method correctly returns false when comparing
     * a BufferedLineString to an object of an incompatible type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentObjectType() {
        // Arrange
        // Note: The constructor Javadoc suggests the buffer should be >= 0,
        // but the implementation accepts negative values. This test uses a negative
        // value as found in the original auto-generated test to verify this behavior.
        final double negativeBuffer = -2877.398196062;
        final List<Point> emptyPoints = Collections.emptyList();

        // Create an empty BufferedLineString.
        BufferedLineString lineString = new BufferedLineString(
                emptyPoints,
                negativeBuffer,
                true, // expandBufForLongitudeSkew
                spatialContext
        );

        // An object of a completely different type for comparison.
        Object otherObject = new Object();

        // Act
        boolean isEqual = lineString.equals(otherObject);

        // Assert
        // A BufferedLineString instance should never be equal to an object of a different class.
        assertFalse("equals() should return false for an object of a different type.", isEqual);

        // Also, verify the buffer was stored correctly, even if negative.
        assertEquals("The buffer value should be stored as provided.",
                negativeBuffer, lineString.getBuf(), 0.01);
    }
}