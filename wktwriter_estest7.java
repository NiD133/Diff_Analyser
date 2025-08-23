package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.BufferedLineString;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

// The test class name is kept for context, but in a real scenario, 
// it would be renamed to something like WKTWriterTest.
public class WKTWriter_ESTestTest7 {

    /**
     * Tests that WKTWriter correctly formats a ShapeCollection containing multiple
     * BufferedLine segments into a GEOMETRYCOLLECTION WKT string.
     *
     * This test case constructs a line string from three identical points,
     * which results in a collection of two identical, zero-length line segments.
     * It verifies that the writer produces the correct WKT representation for this complex shape.
     */
    @Test(timeout = 4000)
    public void toString_forGeometryCollectionOfBufferedLines_shouldGenerateCorrectWkt() {
        // ARRANGE
        // 1. Define the geometry to be written as WKT.
        SpatialContext spatialContext = SpatialContext.GEO;
        double bufferDistance = 1887.0;

        // A single point is used three times to create a line string with two identical segments.
        // Note: The x-coordinate (1456.34...) is intentionally outside the standard [-180, 180]
        // longitude range to test the writer's handling of non-normalized coordinates.
        Point point = spatialContext.makePoint(1456.3497532141264, -2.03979513747829);
        List<Point> points = Arrays.asList(point, point, point);

        // Create a buffered line string, which internally is a collection of buffered line segments.
        BufferedLineString lineString = new BufferedLineString(points, bufferDistance, spatialContext);
        ShapeCollection<BufferedLine> geometryCollection = lineString.getSegments();

        // 2. Define the expected WKT output for clarity and maintainability.
        String pointWkt = "Pt(x=1456.3497532141264,y=-2.03979513747829)";
        String bufferedLineWkt = "BufferedLine(" + pointWkt + ", " + pointWkt + " b=1887.0)";
        String expectedWkt = "GEOMETRYCOLLECTION (" + bufferedLineWkt + "," + bufferedLineWkt + ")";

        WKTWriter wktWriter = new WKTWriter();

        // ACT
        String actualWkt = wktWriter.toString(geometryCollection);

        // ASSERT
        assertEquals(expectedWkt, actualWkt);
    }
}