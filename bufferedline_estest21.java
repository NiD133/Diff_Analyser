package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

/**
 * Tests for {@link BufferedLine}.
 */
public class BufferedLineTest {

    /**
     * Tests that creating a new buffered line from an existing one throws an exception
     * when the geometry becomes invalid.
     *
     * This scenario tests a specific edge case:
     * 1. A base BufferedLine is created using coordinates far outside the valid geographic range (e.g., latitude > 90).
     * 2. The getBuffered() method is called with a negative buffer distance.
     *
     * This combination causes the internal bounding box calculation for the new shape
     * to produce a minimum latitude that is greater than the maximum latitude after being
     * clipped by the world boundaries, leading to a RuntimeException.
     */
    @Test(expected = RuntimeException.class)
    public void getBufferedWithNegativeDistanceOnOutOfBoundsLineThrowsException() {
        // Arrange
        // Use the geographic context, which has world boundaries of latitude -90 to 90.
        SpatialContext geoContext = SpatialContext.GEO;

        // Create a point with a latitude well outside the valid geographic range.
        // The line will be a zero-length line (effectively a point) at this location.
        Point outOfBoundsPoint = new PointImpl(0, 200, geoContext);

        // Create the initial buffered line. The constructor clips the bounding box, so this succeeds.
        double initialBuffer = 200.0;
        BufferedLine lineAtOutOfBoundsPoint = new BufferedLine(outOfBoundsPoint, outOfBoundsPoint, initialBuffer, geoContext);

        // Define a negative buffer that will shrink the original buffer.
        // The resulting buffer size (200 - 150 = 50) is still positive.
        double negativeBuffer = -150.0;

        // Act
        // Attempt to create a new, smaller buffered line from the original.
        // This is expected to throw a RuntimeException during the new shape's internal setup.
        //
        // Why it fails:
        //  - The new buffer size is calculated as: 200.0 (initial) - 150.0 (negative) = 50.0.
        //  - The new shape's bounding box minimum Y is calculated as: point.getY() - new_buffer = 200.0 - 50.0 = 150.0.
        //  - The context then clips this to the world's valid latitude range. The resulting minimum Y becomes
        //    max(-90, 150.0), which is 150.0. The maximum Y becomes min(90, 200.0 + 50.0), which is 90.0.
        //  - This results in an attempt to create a rectangle where minY (150.0) > maxY (90.0), which is invalid.
        lineAtOutOfBoundsPoint.getBuffered(negativeBuffer, geoContext);

        // Assert (implicit)
        // The test passes if a RuntimeException is thrown, as specified by the @Test annotation.
    }
}