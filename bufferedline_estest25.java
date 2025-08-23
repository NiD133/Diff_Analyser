package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

/**
 * Unit tests for the {@link BufferedLine} class.
 */
public class BufferedLineTest {

    /**
     * Verifies that the constructor throws a NullPointerException when provided with null points.
     * The constructor must have valid start and end points to perform its calculations.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionForNullPoints() {
        // Arrange
        SpatialContext context = SpatialContext.GEO;
        double bufferDistance = 10.0; // The specific buffer value is not relevant for this test.

        // Act: Attempt to create a BufferedLine with null start and end points.
        // Assert: The test framework will assert that a NullPointerException is thrown.
        new BufferedLine(null, null, bufferDistance, context);
    }
}