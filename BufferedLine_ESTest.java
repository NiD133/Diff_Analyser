package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.distance.CartesianDistCalc;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.SpatialRelation;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class BufferedLine_ESTest extends BufferedLine_ESTest_scaffolding {

    // Test equality of two BufferedLine objects with different buffer sizes
    @Test(timeout = 4000)
    public void testBufferedLineEqualityWithDifferentBuffers() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(factory);
        PointImpl point = new PointImpl(0.0, 0.0, context);
        BufferedLine line1 = new BufferedLine(point, point, 0.0, context);
        BufferedLine line2 = new BufferedLine(point, point, 90.0, context);

        assertFalse(line1.equals(line2));
        assertFalse(line2.equals(line1));
        assertTrue(line2.hasArea());
    }

    // Test relation of a BufferedLine with a Rectangle
    @Test(timeout = 4000)
    public void testBufferedLineRelateRectangle() throws Throwable {
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = new PointImpl(0.0, 0.0, context);
        RectangleImpl rectangle = new RectangleImpl(1869.69, 0.0, -548.92, Double.POSITIVE_INFINITY, context);
        BufferedLine line = new BufferedLine(point, point, Double.POSITIVE_INFINITY, context);

        SpatialRelation relation = line.relate(rectangle);

        assertEquals(SpatialRelation.CONTAINS, relation);
        assertEquals(Double.POSITIVE_INFINITY, line.getBuf(), 0.01);
    }

    // Test exception when expanding buffer for longitude skew
    @Test(timeout = 4000)
    public void testExpandBufferForLongitudeSkewException() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(factory);
        PointImpl point1 = new PointImpl(0.0, 1774.45, context);
        PointImpl point2 = new PointImpl(1774.45, -3282.38, context);

        try {
            BufferedLine.expandBufForLongitudeSkew(point1, point2, 261.91);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    // Test properties of a BufferedLine with zero buffer
    @Test(timeout = 4000)
    public void testBufferedLineZeroBuffer() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(factory);
        PointImpl point1 = new PointImpl(0.0, 0.0, context);
        PointImpl point2 = new PointImpl(0.0, -3550.72, context);
        BufferedLine line = new BufferedLine(point2, point1, 0.0, context);
        InfBufLine primaryLine = line.getLinePrimary();

        assertEquals(0.0, line.getBuf(), 0.01);
        assertEquals(0.0, primaryLine.getBuf(), 0.01);
        assertEquals(Double.POSITIVE_INFINITY, primaryLine.getSlope(), 0.01);
        assertEquals(0.0, primaryLine.getIntercept(), 0.01);
    }

    // Additional tests can be refactored similarly...

    // Test BufferedLine contains method with a point
    @Test(timeout = 4000)
    public void testBufferedLineContainsPoint() throws Throwable {
        SpatialContextFactory factory = new SpatialContextFactory();
        SpatialContext context = new SpatialContext(factory);
        PointImpl point = new PointImpl(0.0, 1774.45, context);
        BufferedLine line = new BufferedLine(point, point, 1774.45, context);

        assertTrue(line.contains(point));
        assertTrue(line.hasArea());
    }

    // Test BufferedLine equality with itself
    @Test(timeout = 4000)
    public void testBufferedLineEqualityWithItself() throws Throwable {
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = new PointImpl(2115.86, 2115.86, context);
        BufferedLine line = new BufferedLine(point, point, 2115.86, context);

        assertTrue(line.equals(line));
        assertTrue(line.hasArea());
    }

    // Test BufferedLine with null points
    @Test(timeout = 4000)
    public void testBufferedLineWithNullPoints() throws Throwable {
        SpatialContext context = SpatialContext.GEO;
        try {
            new BufferedLine(null, null, 2693.61, context);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test BufferedLine area calculation
    @Test(timeout = 4000)
    public void testBufferedLineAreaCalculation() throws Throwable {
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = new PointImpl(2115.86, 2115.86, context);
        BufferedLine line = new BufferedLine(point, point, 2115.86, context);

        double area = line.getArea(context);
        assertEquals(1.790750773201527E7, area, 0.01);
        assertTrue(line.hasArea());
    }

    // Test BufferedLine with negative buffer
    @Test(timeout = 4000)
    public void testBufferedLineNegativeBuffer() throws Throwable {
        SpatialContext context = SpatialContext.GEO;
        try {
            new BufferedLine(null, null, -512.59, context);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    // Test BufferedLine toString method
    @Test(timeout = 4000)
    public void testBufferedLineToString() throws Throwable {
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = new PointImpl(0.0, 0.0, context);
        BufferedLine line = new BufferedLine(point, point, 0.0, context);

        String description = line.toString();
        assertEquals("BufferedLine(Pt(x=0.0,y=0.0), Pt(x=0.0,y=0.0) b=0.0)", description);
    }
}