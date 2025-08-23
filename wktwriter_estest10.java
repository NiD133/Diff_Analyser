package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.BufferedLineString;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the WKTWriter class.
 */
public class WKTWriterTest {

    /**
     * Tests that a ShapeCollection containing BufferedLine segments is written
     * to the correct WKT-like "GEOMETRYCOLLECTION" format.
     */
    @Test
    public void write_shapeCollectionOfBufferedLines_producesCorrectWkt() throws IOException {
        // Arrange
        SpatialContext ctx = SpatialContext.GEO;
        WKTWriter wktWriter = new WKTWriter();
        StringWriter writer = new StringWriter();

        // Create a simple line string from P1 -> P2 -> P1, which will have two segments.
        Point p1 = ctx.makePoint(10, 20);
        Point p2 = ctx.makePoint(30, 40);
        List<Point> points = Arrays.asList(p1, p2, p1);

        // Create a BufferedLineString with a clear buffer distance.
        double buffer = 5.0;
        BufferedLineString lineString = new BufferedLineString(points, buffer, ctx);

        // The shape to be written is the collection of segments from the line string.
        ShapeCollection<BufferedLine> segments = lineString.getSegments();

        // Define the expected output string. It's now easy to see how the
        // input points and buffer value map to the output.
        String expectedWkt = "GEOMETRYCOLLECTION (" +
                "BufferedLine(Pt(x=10.0,y=20.0), Pt(x=30.0,y=40.0) b=5.0)," +
                "BufferedLine(Pt(x=30.0,y=40.0), Pt(x=10.0,y=20.0) b=5.0)" +
                ")";

        // Act
        wktWriter.write(writer, segments);
        String actualWkt = writer.toString();

        // Assert
        assertEquals(expectedWkt, actualWkt);
    }
}