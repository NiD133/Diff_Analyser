package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.distance.CartesianDistCalc;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.SpatialRelation;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.InfBufLine;
import org.locationtech.spatial4j.shape.impl.PointImpl;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;

public class BufferedLineTest {

    private static final double ORIGIN_X = 0.0;
    private static final double ORIGIN_Y = 0.0;
    private static final double ZERO_BUFFER = 0.0;
    private static final double SMALL_BUFFER = 1.5;
    private static final double LARGE_BUFFER = 100.0;

    // Helper methods for creating common test objects
    private SpatialContext createCartesianContext() {
        return new SpatialContext(new SpatialContextFactory());
    }

    private PointImpl createOriginPoint(SpatialContext context) {
        return new PointImpl(ORIGIN_X, ORIGIN_Y, context);
    }

    private PointImpl createPoint(double x, double y, SpatialContext context) {
        return new PointImpl(x, y, context);
    }

    // Tests for BufferedLine equality
    @Test
    public void testEquals_DifferentBufferSizes_ShouldNotBeEqual() {
        SpatialContext context = createCartesianContext();
        PointImpl origin = createOriginPoint(context);
        
        BufferedLine lineWithZeroBuffer = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        BufferedLine lineWithLargeBuffer = new BufferedLine(origin, origin, 90.0, context);
        
        assertFalse("Lines with different buffer sizes should not be equal", 
                   lineWithZeroBuffer.equals(lineWithLargeBuffer));
        assertFalse("Equality should be symmetric", 
                   lineWithLargeBuffer.equals(lineWithZeroBuffer));
    }

    @Test
    public void testEquals_SameLineProperties_ShouldBeEqual() {
        SpatialContext context = createCartesianContext();
        PointImpl origin = createOriginPoint(context);
        
        BufferedLine line1 = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        BufferedLine line2 = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        assertTrue("Lines with identical properties should be equal", line1.equals(line2));
    }

    @Test
    public void testEquals_SelfComparison_ShouldBeEqual() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = createPoint(2115.863165, 2115.863165, context);
        BufferedLine line = new BufferedLine(point, point, 2115.863165, context);
        
        assertTrue("Line should equal itself", line.equals(line));
    }

    @Test
    public void testEquals_NullComparison_ShouldNotBeEqual() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        BufferedLine line = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        assertFalse("Line should not equal null", line.equals(null));
    }

    @Test
    public void testEquals_DifferentObjectType_ShouldNotBeEqual() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        BufferedLine line = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        assertFalse("Line should not equal a Point", line.equals(origin));
    }

    // Tests for spatial relationships
    @Test
    public void testRelate_InfiniteBufferContainsAllRectangles() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        BufferedLine infiniteBufferLine = new BufferedLine(origin, origin, Double.POSITIVE_INFINITY, context);
        
        RectangleImpl testRectangle = new RectangleImpl(1869.69, 0.0, -548.92, Double.POSITIVE_INFINITY, context);
        
        SpatialRelation relation = infiniteBufferLine.relate(testRectangle);
        assertEquals("Infinite buffer line should contain any rectangle", 
                    SpatialRelation.CONTAINS, relation);
    }

    @Test
    public void testRelate_LineIntersectsRectangle() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = createPoint(0.017453292519943295, 0.017453292519943295, context);
        BufferedLine line = new BufferedLine(point, point, 0.017453292519943295, context);
        
        RectangleImpl rectangle = new RectangleImpl(-13.424214, 0.017453292519943295, 
                                                   0.017453292519943295, 0.017453292519943295, context);
        
        SpatialRelation relation = line.relate(rectangle);
        assertEquals("Line should intersect with overlapping rectangle", 
                    SpatialRelation.INTERSECTS, relation);
    }

    @Test
    public void testRelate_LineDisjointFromRectangle() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = createPoint(19.96, 19.96, context);
        BufferedLine line = new BufferedLine(point, point, 19.96, context);
        
        RectangleImpl distantRectangle = new RectangleImpl(314.0527, 648.9138053, 
                                                          19.96, 314.0527, context);
        
        SpatialRelation relation = line.relate(distantRectangle);
        assertEquals("Line should be disjoint from distant rectangle", 
                    SpatialRelation.DISJOINT, relation);
    }

    @Test
    public void testRelate_ZeroBufferLineWithinWorldBounds() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        BufferedLine zeroBufferLine = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        Rectangle worldBounds = context.getWorldBounds();
        SpatialRelation relation = zeroBufferLine.relate(worldBounds);
        
        assertEquals("Zero buffer line should be within world bounds", 
                    SpatialRelation.WITHIN, relation);
    }

    @Test
    public void testRelate_PointShape_ShouldContainOriginalPoint() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        BufferedLine line = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        SpatialRelation relation = line.relate((Shape) origin);
        assertEquals("Line should contain its defining point", 
                    SpatialRelation.CONTAINS, relation);
    }

    // Tests for point containment
    @Test
    public void testContains_OriginalPoint_ShouldBeContained() {
        SpatialContext context = createCartesianContext();
        PointImpl point = createPoint(0.0, 1774.45, context);
        BufferedLine line = new BufferedLine(point, point, 1774.45, context);
        
        assertTrue("Line should contain its defining point", line.contains(point));
    }

    @Test
    public void testContains_DistantPoint_ShouldNotBeContained() {
        SpatialContext context = createCartesianContext();
        PointImpl linePoint = createPoint(2.0, 2.0, context);
        PointImpl distantPoint = createPoint(6378.137, 2.0, context);
        BufferedLine line = new BufferedLine(linePoint, linePoint, 3958.76, context);
        
        assertFalse("Line should not contain distant point", line.contains(distantPoint));
    }

    // Tests for line geometry properties
    @Test
    public void testGetLinePrimary_VerticalLine_ShouldHaveInfiniteSlope() {
        SpatialContext context = createCartesianContext();
        PointImpl startPoint = createPoint(0.0, 0.0, context);
        PointImpl endPoint = createPoint(0.0, -3550.72, context);
        BufferedLine verticalLine = new BufferedLine(endPoint, startPoint, ZERO_BUFFER, context);
        
        InfBufLine primaryLine = verticalLine.getLinePrimary();
        assertEquals("Vertical line should have infinite slope", 
                    Double.POSITIVE_INFINITY, primaryLine.getSlope(), 0.01);
        assertEquals("Vertical line intercept should be x-coordinate", 
                    0.0, primaryLine.getIntercept(), 0.01);
    }

    @Test
    public void testGetLinePrimary_HorizontalLine_ShouldHaveZeroSlope() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = createPoint(4611.61, 4611.61, context);
        BufferedLine horizontalLine = new BufferedLine(point, point, 4611.61, context);
        
        InfBufLine primaryLine = horizontalLine.getLinePrimary();
        assertEquals("Point line should have zero slope", 
                    0.0, primaryLine.getSlope(), 0.01);
        assertEquals("Point line intercept should be y-coordinate", 
                    4611.61, primaryLine.getIntercept(), 0.01);
    }

    @Test
    public void testGetLinePerp_PointLine_ShouldHaveNaNDistDenomInv() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        BufferedLine pointLine = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        InfBufLine perpLine = pointLine.getLinePerp();
        assertEquals("Point line perpendicular should have NaN distance denominator inverse", 
                    Double.NaN, perpLine.getDistDenomInv(), 0.01);
    }

    // Tests for area calculations
    @Test
    public void testHasArea_ZeroBuffer_ShouldNotHaveArea() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        BufferedLine zeroBufferLine = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        assertFalse("Zero buffer line should not have area", zeroBufferLine.hasArea());
    }

    @Test
    public void testHasArea_NonZeroBuffer_ShouldHaveArea() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl point = createPoint(SMALL_BUFFER, SMALL_BUFFER, context);
        BufferedLine bufferedLine = new BufferedLine(point, point, SMALL_BUFFER, context);
        
        assertTrue("Non-zero buffer line should have area", bufferedLine.hasArea());
    }

    @Test
    public void testGetArea_ZeroBuffer_ShouldReturnZero() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        BufferedLine zeroBufferLine = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        double area = zeroBufferLine.getArea(context);
        assertEquals("Zero buffer line should have zero area", 0.0, area, 0.01);
    }

    @Test
    public void testGetArea_NonZeroBuffer_ShouldReturnPositiveArea() {
        SpatialContext context = SpatialContext.GEO;
        double bufferSize = 2115.863165;
        PointImpl point = createPoint(bufferSize, bufferSize, context);
        BufferedLine bufferedLine = new BufferedLine(point, point, bufferSize, context);
        
        double area = bufferedLine.getArea(context);
        assertTrue("Non-zero buffer line should have positive area", area > 0);
        assertEquals("Area should match expected calculation", 1.790750773201527E7, area, 0.01);
    }

    // Tests for buffer operations
    @Test
    public void testGetBuffered_SameBuffer_ShouldReturnEquivalentLine() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        BufferedLine originalLine = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        BufferedLine bufferedLine = (BufferedLine) originalLine.getBuffered(ZERO_BUFFER, context);
        assertEquals("Buffering with same size should maintain buffer size", 
                    ZERO_BUFFER, bufferedLine.getBuf(), 0.01);
    }

    @Test(expected = AssertionError.class)
    public void testGetBuffered_NegativeBuffer_ShouldThrowException() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        BufferedLine line = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        line.getBuffered(-3307.84, context);
    }

    @Test(expected = AssertionError.class)
    public void testConstructor_NegativeBuffer_ShouldThrowException() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        
        new BufferedLine(origin, origin, -512.59, context);
    }

    // Tests for longitude skew expansion
    @Test
    public void testExpandBufForLongitudeSkew_SamePoints_ShouldReturnOriginalBuffer() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        double originalBuffer = -43.39;
        
        double expandedBuffer = BufferedLine.expandBufForLongitudeSkew(origin, origin, originalBuffer);
        assertEquals("Same points should return original buffer", 
                    originalBuffer, expandedBuffer, 0.01);
    }

    @Test
    public void testExpandBufForLongitudeSkew_NegativeBuffer_ShouldExpandCorrectly() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        double negativeBuffer = -2439.52;
        
        double expandedBuffer = BufferedLine.expandBufForLongitudeSkew(origin, origin, negativeBuffer);
        assertEquals("Negative buffer should be expanded for longitude skew", 
                    80.48, expandedBuffer, 0.01);
    }

    // Tests for basic getters
    @Test
    public void testGetBuf_ShouldReturnCorrectBufferSize() {
        SpatialContext context = createCartesianContext();
        PointImpl origin = createOriginPoint(context);
        double expectedBuffer = 1461.7;
        BufferedLine line = new BufferedLine(origin, origin, expectedBuffer, context);
        
        assertEquals("getBuf should return the buffer size used in construction", 
                    expectedBuffer, line.getBuf(), 0.01);
    }

    @Test
    public void testToString_ShouldContainPointsAndBuffer() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        BufferedLine line = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        String result = line.toString();
        String expected = "BufferedLine(Pt(x=0.0,y=0.0), Pt(x=0.0,y=0.0) b=0.0)";
        assertEquals("toString should format line information correctly", expected, result);
    }

    // Tests for error conditions
    @Test(expected = NullPointerException.class)
    public void testRelate_NullRectangle_ShouldThrowException() {
        SpatialContext context = createCartesianContext();
        PointImpl origin = createOriginPoint(context);
        BufferedLine line = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        line.relate((Rectangle) null);
    }

    @Test(expected = NullPointerException.class)
    public void testContains_NullPoint_ShouldThrowException() {
        SpatialContext context = SpatialContext.GEO;
        PointImpl origin = createOriginPoint(context);
        BufferedLine line = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        line.contains(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRelate_UnsupportedShapeType_ShouldThrowException() {
        SpatialContext context = createCartesianContext();
        PointImpl origin = createOriginPoint(context);
        BufferedLine line = new BufferedLine(origin, origin, ZERO_BUFFER, context);
        
        line.relate((Shape) null);
    }
}