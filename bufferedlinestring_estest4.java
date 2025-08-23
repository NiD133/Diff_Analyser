package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BufferedLineString_ESTestTest4 extends BufferedLineString_ESTest_scaffolding {

    /**
     * Tests the string representation of a BufferedLineString
     * constructed with points having NaN (Not-a-Number) coordinates.
     * This verifies that the toString() method handles invalid point data gracefully.
     */
    @Test(timeout = 4000)
    public void toStringShouldCorrectlyFormatLineStringWithNaNPoints() {
        // Arrange
        SpatialContext spatialContext = new SpatialContext(new SpatialContextFactory());
        final double buffer = -2877.398196062; // A negative buffer is used to match the original test case.

        // Create a point with NaN coordinates, which can result from operations on empty shapes.
        Point nanPoint = spatialContext.makePoint(Double.NaN, Double.NaN);
        List<Point> pointsWithNaN = Arrays.asList(nanPoint, nanPoint, nanPoint);

        // Create the line string with the list of NaN points.
        BufferedLineString lineStringWithNaNPoints = new BufferedLineString(pointsWithNaN, buffer, false, spatialContext);

        // Act
        String actualToString = lineStringWithNaNPoints.toString();

        // Assert
        String expectedToString = "BufferedLineString(buf=-2877.398196062 pts=NaN NaN, NaN NaN, NaN NaN)";
        assertEquals(expectedToString, actualToString);
    }
}