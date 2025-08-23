package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link BufferedLineString}.
 * This class contains an improved version of a previously auto-generated test case.
 */
public class BufferedLineStringTest {

    /**
     * Verifies that calling {@code getBuffered} on a line string created with an invalid
     * latitude point throws a {@code RuntimeException}.
     *
     * The failure is triggered because the geographic context attempts to create a new
     * bounding box for the buffered shape. The invalid input latitude leads to an
     * impossible geometric state where the calculated maximum Y coordinate is less than
     * the minimum Y coordinate, causing the underlying shape factory to fail.
     */
    @Test
    public void getBufferedOnLineWithInvalidLatitudePointShouldThrowException() {
        // ARRANGE
        // The original test used this "magic number" to trigger a specific error.
        // We preserve it to validate the exact same behavior and exception message.
        final double magicNumber = 815.617824665349;

        // The key to this test is using a latitude value far outside the valid geographic range [-90, 90].
        final double invalidLatitude = magicNumber;
        final double invalidLongitude = magicNumber; // Also out of range [-180, 180].

        final SpatialContext geoContext = SpatialContext.GEO;
        Point pointWithInvalidCoords = new PointImpl(invalidLongitude, invalidLatitude, geoContext);
        List<Point> points = Collections.singletonList(pointWithInvalidCoords);

        // Create a BufferedLineString with the single invalid point and a large buffer.
        BufferedLineString lineString = new BufferedLineString(points, magicNumber, geoContext);

        // ACT & ASSERT
        try {
            // Attempting to apply another buffer triggers the internal calculation that fails.
            lineString.getBuffered(magicNumber, geoContext);
            fail("Expected a RuntimeException due to invalid coordinate calculations.");
        } catch (RuntimeException e) {
            // The original test verified this exact exception message, which is thrown when
            // trying to create a rectangle with an invalid min/max Y.
            // We assert the same message to ensure the behavior remains unchanged.
            final String expectedMessage = "maxY must be >= minY: 699.3844888966275 to 90.0";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}