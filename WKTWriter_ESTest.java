package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.shape.Circle;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.BufferedLineString;
import org.locationtech.spatial4j.shape.impl.PointImpl;

/**
 * Test suite for WKTWriter functionality.
 * Tests WKT (Well-Known Text) format writing for various spatial shapes.
 */
public class WKTWriterTest {

    // Test basic WKTWriter properties
    
    @Test
    public void shouldReturnDefaultNumberFormatPattern() {
        WKTWriter writer = new WKTWriter();
        
        DecimalFormat numberFormat = (DecimalFormat) writer.getNumberFormat();
        
        assertEquals("###0.######", numberFormat.toLocalizedPattern());
    }

    @Test
    public void shouldReturnWKTAsFormatName() {
        WKTWriter writer = new WKTWriter();
        
        String formatName = writer.getFormatName();
        
        assertEquals("WKT", formatName);
    }

    // Test point formatting with custom number formats
    
    @Test
    public void shouldFormatPointWithPercentageNumberFormat() {
        WKTWriter writer = new WKTWriter();
        StringBuilder buffer = new StringBuilder("WKT");
        SpatialContext context = createSpatialContext();
        Point point = new PointImpl(4883.694876, 4.0, context);
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        
        writer.append(buffer, point, percentFormat);
        
        assertEquals("WKT488,369% 400%", buffer.toString());
    }

    // Test error handling for null inputs
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenWriterIsNull() {
        WKTWriter wktWriter = new WKTWriter();
        Point point = new PointImpl(0.0, -1281.1040424765, null);
        
        wktWriter.write(null, point);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenPointIsNull() {
        WKTWriter writer = new WKTWriter();
        StringBuilder buffer = new StringBuilder();
        NumberFormat numberFormat = NumberFormat.getInstance();
        
        writer.append(buffer, null, numberFormat);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenShapeIsNull() {
        WKTWriter writer = new WKTWriter();
        
        writer.toString(null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void shouldThrowExceptionWithInvalidChoiceFormat() {
        WKTWriter writer = new WKTWriter();
        StringBuilder buffer = new StringBuilder("$+(8+");
        Point point = new PointImpl(-2288.759999701334, -2288.759999701334, SpatialContext.GEO);
        ChoiceFormat invalidFormat = new ChoiceFormat("$+(8+");
        
        writer.append(buffer, point, invalidFormat);
    }

    // Test shape collection formatting
    
    @Test
    public void shouldFormatNonEmptyGeometryCollection() {
        WKTWriter writer = new WKTWriter();
        ShapeCollection<BufferedLine> shapeCollection = createNonEmptyShapeCollection();
        
        String wktOutput = writer.toString(shapeCollection);
        
        assertTrue(wktOutput.startsWith("GEOMETRYCOLLECTION ("));
        assertTrue(wktOutput.contains("BufferedLine"));
    }

    @Test
    public void shouldFormatEmptyGeometryCollection() {
        WKTWriter writer = new WKTWriter();
        ShapeCollection<BufferedLine> emptyCollection = createEmptyShapeCollection();
        
        String wktOutput = writer.toString(emptyCollection);
        
        assertEquals("GEOMETRYCOLLECTION EMPTY", wktOutput);
    }

    @Test
    public void shouldWriteGeometryCollectionToWriter() throws Exception {
        WKTWriter writer = new WKTWriter();
        StringWriter stringWriter = new StringWriter();
        ShapeCollection<BufferedLine> shapeCollection = createNonEmptyShapeCollection();
        
        writer.write(stringWriter, shapeCollection);
        
        String result = stringWriter.toString();
        assertTrue(result.startsWith("GEOMETRYCOLLECTION ("));
        assertTrue(result.contains("BufferedLine"));
    }

    // Test buffered line string formatting
    
    @Test
    public void shouldFormatBufferedLineStringWithBuffer() {
        WKTWriter writer = new WKTWriter();
        BufferedLineString bufferedLineString = createBufferedLineStringWithPoints();
        
        String wktOutput = writer.toString(bufferedLineString);
        
        assertTrue(wktOutput.startsWith("BUFFER (LINESTRING"));
        assertTrue(wktOutput.endsWith("1887)"));
    }

    @Test
    public void shouldFormatEmptyLineString() {
        WKTWriter writer = new WKTWriter();
        BufferedLineString emptyLineString = createEmptyBufferedLineString();
        
        String wktOutput = writer.toString(emptyLineString);
        
        assertEquals("LINESTRING ()", wktOutput);
    }

    // Test circle formatting
    
    @Test
    public void shouldFormatCircleAsBufferedPoint() {
        WKTWriter writer = new WKTWriter();
        Point center = new PointImpl(Math.PI, Math.PI, SpatialContext.GEO);
        Circle circle = center.getBuffered(Math.PI, SpatialContext.GEO);
        
        String wktOutput = writer.toString(circle);
        
        assertEquals("BUFFER (POINT (3.141593 3.141593), 3.141593)", wktOutput);
    }

    // Test rectangle formatting
    
    @Test
    public void shouldFormatRectangleAsEnvelope() {
        WKTWriter writer = new WKTWriter();
        Rectangle rectangle = createRectangleFromBufferedLine();
        
        String wktOutput = writer.toString(rectangle);
        
        assertTrue(wktOutput.startsWith("ENVELOPE ("));
    }

    // Test point formatting
    
    @Test
    public void shouldFormatEmptyPoint() {
        WKTWriter writer = new WKTWriter();
        Point emptyPoint = createEmptyPoint();
        
        String wktOutput = writer.toString(emptyPoint);
        
        assertEquals("POINT EMPTY", wktOutput);
    }

    @Test
    public void shouldFormatPointWithInfiniteCoordinates() {
        WKTWriter writer = new WKTWriter();
        Point infinitePoint = new PointImpl(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, SpatialContext.GEO);
        
        String wktOutput = writer.toString(infinitePoint);
        
        assertEquals("POINT (-∞ -∞)", wktOutput);
    }

    // Helper methods for creating test objects
    
    private SpatialContext createSpatialContext() {
        SpatialContextFactory factory = new SpatialContextFactory();
        return new SpatialContext(factory);
    }

    private ShapeCollection<BufferedLine> createNonEmptyShapeCollection() {
        LinkedList<Point> points = new LinkedList<>();
        SpatialContext context = SpatialContext.GEO;
        PointImpl startPoint = new PointImpl(1887, 3671, context);
        points.add(startPoint);
        
        GeodesicSphereDistCalc.LawOfCosines calculator = new GeodesicSphereDistCalc.LawOfCosines();
        Point endPoint = calculator.pointOnBearing(startPoint, 274.25232505, 3671, context, startPoint);
        points.add(endPoint);
        points.offerLast(startPoint);
        
        BufferedLineString bufferedLineString = new BufferedLineString(points, 1887, context);
        return bufferedLineString.getSegments();
    }

    private ShapeCollection<BufferedLine> createEmptyShapeCollection() {
        LinkedList<Point> emptyPoints = new LinkedList<>();
        BufferedLineString emptyBufferedLineString = new BufferedLineString(emptyPoints, 3671, true, SpatialContext.GEO);
        return emptyBufferedLineString.getSegments();
    }

    private BufferedLineString createBufferedLineStringWithPoints() {
        LinkedList<Point> points = new LinkedList<>();
        SpatialContext context = SpatialContext.GEO;
        PointImpl startPoint = new PointImpl(1887, 3671, context);
        points.add(startPoint);
        
        GeodesicSphereDistCalc.LawOfCosines calculator = new GeodesicSphereDistCalc.LawOfCosines();
        Point endPoint = calculator.pointOnBearing(startPoint, 274.25232505, 3671, context, startPoint);
        points.add(endPoint);
        points.offerLast(endPoint);
        
        return new BufferedLineString(points, 1887, context);
    }

    private BufferedLineString createEmptyBufferedLineString() {
        LinkedList<Point> emptyPoints = new LinkedList<>();
        return new BufferedLineString(emptyPoints, -145.0981164, SpatialContext.GEO);
    }

    private Rectangle createRectangleFromBufferedLine() {
        LinkedList<Point> emptyPoints = new LinkedList<>();
        BufferedLineString bufferedLineString = new BufferedLineString(emptyPoints, 3671, true, SpatialContext.GEO);
        Point center = bufferedLineString.getCenter();
        BufferedLine bufferedLine = new BufferedLine(center, center, 1887, SpatialContext.GEO);
        return bufferedLine.getBoundingBox();
    }

    private Point createEmptyPoint() {
        LinkedList<Point> emptyPoints = new LinkedList<>();
        BufferedLineString bufferedLineString = new BufferedLineString(emptyPoints, 360.0, SpatialContext.GEO);
        return bufferedLineString.getCenter();
    }
}