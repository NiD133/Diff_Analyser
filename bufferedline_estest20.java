package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;

/**
 * Test suite for the {@link BufferedLine} class.
 */
public class BufferedLineTest {

    // A non-geodetic (i.e., Cartesian) spatial context is sufficient for this test.
    private final SpatialContext context = new SpatialContextFactory().newSpatialContext();

    /**
     * Verifies that the relate(Rectangle) method correctly throws a NullPointerException
     * when passed a null argument. This is a standard contract for methods that
     * do not explicitly support null inputs.
     */
    @Test(expected = NullPointerException.class)
    public void relateWithNullRectangleThrowsNPE() {
        // Arrange: Create a minimal BufferedLine instance. Its specific geometry
        // is not relevant for this null-check test.
        Point point = new PointImpl(0.0, 0.0, context);
        BufferedLine line = new BufferedLine(point, point, 0.0, context);
        Rectangle nullRectangle = null;

        // Act: Call the method under test with the null input.
        // The `relate` method is overloaded, so passing a typed null is necessary
        // to resolve the correct method signature.
        line.relate(nullRectangle);

        // Assert: The test framework will automatically pass if the expected
        // NullPointerException is thrown, and fail otherwise.
    }
}