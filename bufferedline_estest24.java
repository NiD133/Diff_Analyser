package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

/**
 * Unit tests for the {@link BufferedLine} class.
 */
public class BufferedLineTest {

    /**
     * Tests that calling the contains() method with a null Point argument
     * results in a NullPointerException. This ensures the method correctly
     * handles invalid input as part of its public contract.
     */
    @Test(expected = NullPointerException.class)
    public void contains_whenPointIsNull_shouldThrowNullPointerException() {
        // Arrange: Create a minimal BufferedLine instance.
        // A degenerate line (start and end points are the same) is sufficient for this test.
        SpatialContext spatialContext = SpatialContext.GEO;
        Point point = new PointImpl(0.0, 0.0, spatialContext);
        BufferedLine line = new BufferedLine(point, point, 0.0, spatialContext);

        // Act: Call the method under test with a null argument.
        // The @Test(expected=...) annotation handles the assertion.
        line.contains(null);
    }
}