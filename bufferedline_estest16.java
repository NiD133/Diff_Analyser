package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BufferedLine}.
 * This class focuses on improving the clarity and maintainability of tests for the BufferedLine class.
 */
public class BufferedLineTest {

    /**
     * Tests that the buffer value provided in the constructor is correctly returned by getBuf().
     * This test specifically covers the edge case of a degenerate line, where the start and
     * end points are identical, which is effectively a buffered point.
     */
    @Test
    public void getBufShouldReturnConstructorValueForDegenerateLine() {
        // Arrange: Set up the test context and input data.
        final SpatialContext ctx = SpatialContext.GEO;

        // A "degenerate" line is one where the start and end points are the same.
        final Point point = new PointImpl(10.0, 20.0, ctx);
        final double expectedBuffer = 5.0;

        // Create the BufferedLine instance to be tested.
        final BufferedLine bufferedLine = new BufferedLine(point, point, expectedBuffer, ctx);

        // Act: Call the method under test.
        final double actualBuffer = bufferedLine.getBuf();

        // Assert: Verify that the returned buffer matches the value provided to the constructor.
        // The delta is 0.0 because we expect an exact match for the stored double.
        assertEquals(expectedBuffer, actualBuffer, 0.0);
    }
}