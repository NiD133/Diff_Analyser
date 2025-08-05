package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
        mockJVMNonDeterminism = true,
        useVFS = true,
        useVNET = true,
        resetStaticState = true,
        separateClassLoader = true)
public class WKTWriter_ESTest extends WKTWriter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testDecimalFormatPattern() {
        WKTWriter wktWriter = new WKTWriter();
        DecimalFormat decimalFormat = (DecimalFormat) wktWriter.getNumberFormat();
        assertEquals("###0.######", decimalFormat.toLocalizedPattern());
    }

    @Test(timeout = 4000)
    public void testFormatName() {
        WKTWriter wktWriter = new WKTWriter();
        assertEquals("WKT", wktWriter.getFormatName());
    }

    @Test(timeout = 4000)
    public void testAppendPointWithPercentFormat() {
        WKTWriter wktWriter = new WKTWriter();
        StringBuilder sb = new StringBuilder("WKT");
        SpatialContextFactory contextFactory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(contextFactory);
        PointImpl point = new PointImpl(4883.694876, 4.0, context);
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        wktWriter.append(sb, point, percentFormat);
        assertEquals("WKT488,369% 400%", sb.toString());
    }

    @Test(timeout = 4000)
    public void testWriteNullWriterThrowsException() {
        WKTWriter wktWriter = new WKTWriter();
        PointImpl point = new PointImpl(0.0, -1281.1040424765, null);
        try {
            wktWriter.write(null, point);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.io.WKTWriter", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendNullPointThrowsException() {
        WKTWriter wktWriter = new WKTWriter();
        StringBuilder sb = new StringBuilder();
        NumberFormat numberFormat = NumberFormat.getInstance();
        try {
            wktWriter.append(sb, null, numberFormat);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.io.WKTWriter", e);
        }
    }

    @Test(timeout = 4000)
    public void testAppendPointWithInvalidChoiceFormat() {
        WKTWriter wktWriter = new WKTWriter();
        StringBuilder sb = new StringBuilder("$+(8+");
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = new PointImpl(-2288.759999701334, -2288.759999701334, context);
        ChoiceFormat choiceFormat = new ChoiceFormat("$+(8+");
        try {
            wktWriter.append(sb, point, choiceFormat);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("java.text.ChoiceFormat", e);
        }
    }

    @Test(timeout = 4000)
    public void testToStringShapeCollection() {
        WKTWriter wktWriter = new WKTWriter();
        LinkedList<Point> points = new LinkedList<>();
        GeodesicSphereDistCalc.LawOfCosines distCalc = new GeodesicSphereDistCalc.LawOfCosines();
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = new PointImpl(1887, 3671, context);
        points.add(point);
        Point pointOnBearing = distCalc.pointOnBearing(point, 274.25232505, 3671, context, point);
        points.add(pointOnBearing);
        points.offerLast(point);
        BufferedLineString lineString = new BufferedLineString(points, 1887, context);
        ShapeCollection<BufferedLine> shapeCollection = lineString.getSegments();
        String result = wktWriter.toString(shapeCollection);
        assertEquals("GEOMETRYCOLLECTION (BufferedLine(Pt(x=1456.3497532141264,y=-2.03979513747829), Pt(x=1456.3497532141264,y=-2.03979513747829) b=1887.0),BufferedLine(Pt(x=1456.3497532141264,y=-2.03979513747829), Pt(x=1456.3497532141264,y=-2.03979513747829) b=1887.0))", result);
    }

    @Test(timeout = 4000)
    public void testToStringNullShapeThrowsException() {
        WKTWriter wktWriter = new WKTWriter();
        try {
            wktWriter.toString(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.locationtech.spatial4j.io.LegacyShapeWriter", e);
        }
    }

    @Test(timeout = 4000)
    public void testToStringEmptyShapeCollection() {
        WKTWriter wktWriter = new WKTWriter();
        LinkedList<Point> points = new LinkedList<>();
        SpatialContext context = SpatialContext.GEO;
        BufferedLineString lineString = new BufferedLineString(points, 3671, true, context);
        ShapeCollection<BufferedLine> shapeCollection = lineString.getSegments();
        String result = wktWriter.toString(shapeCollection);
        assertEquals("GEOMETRYCOLLECTION EMPTY", result);
    }

    @Test(timeout = 4000)
    public void testWriteShapeCollectionToWriter() throws Exception {
        WKTWriter wktWriter = new WKTWriter();
        StringWriter writer = new StringWriter(1887);
        LinkedList<Point> points = new LinkedList<>();
        GeodesicSphereDistCalc.LawOfCosines distCalc = new GeodesicSphereDistCalc.LawOfCosines();
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = new PointImpl(1887, 3671, context);
        points.add(point);
        Point pointOnBearing = distCalc.pointOnBearing(point, 274.25232505, 3671, context, point);
        points.add(pointOnBearing);
        points.offerLast(pointOnBearing);
        BufferedLineString lineString = new BufferedLineString(points, 1887, context);
        ShapeCollection<BufferedLine> shapeCollection = lineString.getSegments();
        wktWriter.write(writer, shapeCollection);
        assertEquals("GEOMETRYCOLLECTION (BufferedLine(Pt(x=1456.3497532141264,y=-2.03979513747829), Pt(x=1456.3497532141264,y=-2.03979513747829) b=1887.0),BufferedLine(Pt(x=1456.3497532141264,y=-2.03979513747829), Pt(x=1456.3497532141264,y=-2.03979513747829) b=1887.0))", writer.toString());
    }

    @Test(timeout = 4000)
    public void testToStringBufferedLineString() {
        WKTWriter wktWriter = new WKTWriter();
        LinkedList<Point> points = new LinkedList<>();
        GeodesicSphereDistCalc.LawOfCosines distCalc = new GeodesicSphereDistCalc.LawOfCosines();
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = new PointImpl(1887, 3671, context);
        points.add(point);
        Point pointOnBearing = distCalc.pointOnBearing(point, 274.25232505, 3671, context, point);
        points.add(pointOnBearing);
        points.offerLast(pointOnBearing);
        BufferedLineString lineString = new BufferedLineString(points, 1887, context);
        String result = wktWriter.toString(lineString);
        assertEquals("BUFFER (LINESTRING (1456.349753 -2.039795, 1456.349753 -2.039795, 1456.349753 -2.039795), 1887)", result);
    }

    @Test(timeout = 4000)
    public void testToStringEmptyBufferedLineString() {
        WKTWriter wktWriter = new WKTWriter();
        LinkedList<Point> points = new LinkedList<>();
        SpatialContext context = SpatialContext.GEO;
        BufferedLineString lineString = new BufferedLineString(points, -145.0981164, context);
        String result = wktWriter.toString(lineString);
        assertEquals("LINESTRING ()", result);
    }

    @Test(timeout = 4000)
    public void testToStringBufferedCircle() {
        WKTWriter wktWriter = new WKTWriter();
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = new PointImpl(3.141592653589793, 3.141592653589793, context);
        Circle circle = point.getBuffered(3.141592653589793, context);
        String result = wktWriter.toString(circle);
        assertEquals("BUFFER (POINT (3.141593 3.141593), 3.141593)", result);
    }

    @Test(timeout = 4000)
    public void testToStringRectangle() {
        WKTWriter wktWriter = new WKTWriter();
        LinkedList<Point> points = new LinkedList<>();
        SpatialContext context = SpatialContext.GEO;
        BufferedLineString lineString = new BufferedLineString(points, 3671, true, context);
        Point center = lineString.getCenter();
        BufferedLine bufferedLine = new BufferedLine(center, center, 1887, context);
        Rectangle rectangle = bufferedLine.getBoundingBox();
        String result = wktWriter.toString(rectangle);
        assertEquals("ENVELOPE (\uFFFD, \uFFFD, \uFFFD, \uFFFD)", result);
    }

    @Test(timeout = 4000)
    public void testToStringEmptyPoint() {
        WKTWriter wktWriter = new WKTWriter();
        LinkedList<Point> points = new LinkedList<>();
        SpatialContext context = SpatialContext.GEO;
        BufferedLineString lineString = new BufferedLineString(points, 360.0, context);
        Point center = lineString.getCenter();
        String result = wktWriter.toString(center);
        assertEquals("POINT EMPTY", result);
    }

    @Test(timeout = 4000)
    public void testToStringNegativeInfinityPoint() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = new PointImpl(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, context);
        WKTWriter wktWriter = new WKTWriter();
        String result = wktWriter.toString(point);
        assertEquals("WKT", wktWriter.getFormatName());
        assertEquals("POINT (-\u221E -\u221E)", result);
    }
}