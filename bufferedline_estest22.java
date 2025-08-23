package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

/**
 * Test suite for {@link BufferedLine}.
 * This class focuses on improving the clarity of a single, auto-generated test case.
 */
public class BufferedLineTest {

    /**
     * Verifies that getBuffered() throws a NullPointerException when passed a null context.
     * This is a standard contract for methods that require a context to perform their operations.
     */
    @Test(expected = NullPointerException.class)
    public void getBufferedWithNullContextShouldThrowNullPointerException() {
        // Arrange: Create a simple BufferedLine. The specific geometry is not critical
        // for this test, as we are only checking for null-handling of the context parameter.
        SpatialContext geoContext = SpatialContext.GEO;
        Point point = new PointImpl(0.0, 0.0, geoContext);
        BufferedLine bufferedLine = new BufferedLine(point, point, 0.0, geoContext);

        // Act: Call the method under test with a null context.
        // The @Test(expected) annotation will automatically handle the assertion.
        bufferedLine.getBuffered(0.0, null);
    }
}