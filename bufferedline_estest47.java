package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Shape;

/**
 * Test suite for {@link BufferedLine}.
 */
public class BufferedLineTest {

    private final SpatialContext context = new SpatialContext(new SpatialContextFactory());

    /**
     * The `relate()` method in BufferedLine is not fully implemented and is expected
     * to throw an exception for unsupported shape types. This test verifies that
     * calling it with a null argument correctly throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void relateWithNullShapeShouldThrowUnsupportedOperationException() {
        // Arrange: Create a simple BufferedLine.
        // A line from a point to itself with a zero buffer is a valid, simple case.
        Point point = new PointImpl(0.0, 0.0, context);
        BufferedLine bufferedLine = new BufferedLine(point, point, 0.0, context);

        // Act: Call the relate() method with a null shape.
        bufferedLine.relate((Shape) null);

        // Assert: The test expects an UnsupportedOperationException.
        // This is handled by the `expected` parameter in the @Test annotation.
    }
}