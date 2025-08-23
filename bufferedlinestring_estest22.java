package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link BufferedLineString}.
 */
// Note: The original class name 'BufferedLineString_ESTestTest22' and scaffolding
// have been removed for clarity and to focus on standard testing practices.
public class BufferedLineStringTest {

    /**
     * Tests that the BufferedLineString constructor throws an exception when provided
     * with a negative buffer value. A negative buffer is invalid and leads to an
     * illegal internal state, specifically an invalid bounding box calculation.
     */
    @Test
    public void constructor_withNegativeBuffer_shouldThrowException() {
        // Arrange: Set up a spatial context, a single point, and an invalid negative buffer.
        SpatialContext geoContext = SpatialContext.GEO;
        List<Point> points = Collections.singletonList(geoContext.makePoint(-1.0, -1.0));
        double invalidNegativeBuffer = -1.0;

        // Act & Assert: Attempt to create the BufferedLineString and verify the correct exception is thrown.
        try {
            new BufferedLineString(points, invalidNegativeBuffer, true, geoContext);
            fail("Expected a RuntimeException because the buffer value cannot be negative.");
        } catch (RuntimeException e) {
            // The negative buffer causes an invalid bounding box where maxY < minY.
            // We assert that the exception message reflects this internal error.
            String expectedMessageFragment = "maxY must be >= minY";
            assertTrue(
                "Exception message should indicate an invalid bounding box. Got: " + e.getMessage(),
                e.getMessage().contains(expectedMessageFragment)
            );
        }
    }
}