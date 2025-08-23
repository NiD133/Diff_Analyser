package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BufferedLineString}.
 * This class contains the refactored test case.
 */
public class BufferedLineStringTest {

    /**
     * Tests that the area of a BufferedLineString created from a single point
     * is calculated correctly. This special case should behave like a buffered point
     * (i.e., a circle).
     */
    @Test
    public void getArea_forLineWithSinglePoint_isAreaOfBufferedPoint() {
        // Arrange
        // A BufferedLineString with a single point is treated as a buffered point (a circle).
        // This test calculates the area of such a shape in a geodetic context.
        SpatialContext geoContext = SpatialContext.GEO;

        // NOTE: The original auto-generated test used a latitude of 131.08, which is an
        // invalid value. We are keeping it here to preserve the test's original behavior,
        // but this may indicate a flaw in the class under test or the original test's logic.
        double longitude = 131.08;
        double latitude = 131.08;
        Point singlePoint = new PointImpl(longitude, latitude, geoContext);
        List<Point> points = Collections.singletonList(singlePoint);

        double bufferInDegrees = 5451.935;
        // The 'expandBufForLongitudeSkew' flag is set to true.
        BufferedLineString lineWithSinglePoint = new BufferedLineString(points, bufferInDegrees, true, geoContext);

        // Act
        double actualArea = lineWithSinglePoint.getArea(geoContext);

        // Assert
        double expectedArea = 41252.96124941927;
        double delta = 0.01;
        assertEquals(
                "The area of a single-point BufferedLineString should be the area of a circle with the given buffer.",
                expectedArea,
                actualArea,
                delta);
    }
}