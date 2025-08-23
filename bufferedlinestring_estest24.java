package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BufferedLineString_ESTestTest24 extends BufferedLineString_ESTest_scaffolding {

    /**
     * Tests that the BufferedLineString constructor throws a RuntimeException
     * when a negative buffer is provided. A negative buffer results in an
     * invalid bounding box calculation (minY > maxY), which is rejected.
     */
    @Test
    public void constructor_withNegativeBuffer_throwsRuntimeException() {
        // Arrange
        // A single point is sufficient to trigger the internal creation of a BufferedLine.
        SpatialContext geoContext = SpatialContext.GEO;
        Point centerPoint = geoContext.makePoint(0, 0);
        List<Point> points = Collections.singletonList(centerPoint);

        // A negative buffer will cause an invalid bounding box where minY > maxY.
        double negativeBuffer = -10.0;

        // Act & Assert
        try {
            new BufferedLineString(points, negativeBuffer, geoContext);
            fail("Expected a RuntimeException to be thrown due to the negative buffer.");
        } catch (RuntimeException e) {
            // The exception is thrown by a dependency when creating the bounding box.
            // We verify the message to ensure it's the expected error.
            String expectedMessageFragment = "maxY must be >= minY";
            assertTrue(
                "Exception message should indicate an invalid bounding box. Got: " + e.getMessage(),
                e.getMessage().startsWith(expectedMessageFragment)
            );
        }
    }
}