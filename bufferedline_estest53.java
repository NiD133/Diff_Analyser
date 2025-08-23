package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

/**
 * Test suite for the {@link BufferedLine} constructor.
 */
public class BufferedLineConstructorTest {

    /**
     * Tests that the BufferedLine constructor fails when provided with infinite coordinates
     * and an infinite buffer.
     * <p>
     * These non-finite inputs lead to NaN (Not-a-Number) results during internal
     * geometric calculations, which correctly trigger an internal assertion failure. This test
     * verifies that the constructor is not robust to such invalid inputs.
     */
    @Test(expected = AssertionError.class)
    public void constructor_withInfiniteInputs_throwsAssertionError() {
        // Arrange: Set up a spatial context and invalid inputs.
        SpatialContext context = SpatialContext.GEO;
        Point infinitePoint = new PointImpl(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, context);
        double infiniteBuffer = Double.POSITIVE_INFINITY;

        // Act: Attempt to create a BufferedLine with the invalid inputs.
        // Assert: An AssertionError is expected, as declared by the @Test annotation.
        new BufferedLine(infinitePoint, infinitePoint, infiniteBuffer, context);
    }
}