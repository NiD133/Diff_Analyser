package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.locationtech.spatial4j.shape.SpatialRelation;
import org.locationtech.spatial4j.shape.impl.BufferedLine;
import org.locationtech.spatial4j.shape.impl.BufferedLineString;
import org.locationtech.spatial4j.shape.impl.PointImpl;

/**
 * Test suite for BufferedLineString functionality including construction,
 * geometric operations, and spatial relationships.
 */
public class BufferedLineString_ESTest {

    private static final double BUFFER_DISTANCE = 10.0;
    private static final double DELTA = 0.01;
    
    // Test Data Setup Helpers
    
    private SpatialContext createDefaultSpatialContext() {
        return new SpatialContextFactory().newSpatialContext();
    }
    
    private List<Point> createEmptyPointList() {
        return new ArrayList<>();
    }
    
    private List<Point> createSinglePointList(double x, double y, SpatialContext ctx) {
        List<Point> points = new ArrayList<>();
        points.add(new PointImpl(x, y, ctx));
        return points;
    }
    
    private List<Point> createTwoPointList(double x1, double y1, double x2, double y2, SpatialContext ctx) {
        List<Point> points = new ArrayList<>();
        points.add(new PointImpl(x1, y1, ctx));
        points.add(new PointImpl(x2, y2, ctx));
        return points;
    }

    // Constructor Tests
    
    @Test
    public void testConstructor_WithEmptyPointList_CreatesEmptyLineString() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> emptyPoints = createEmptyPointList();
        
        BufferedLineString lineString = new BufferedLineString(emptyPoints, BUFFER_DISTANCE, ctx);
        
        assertTrue("LineString with empty points should be empty", lineString.isEmpty());
        assertEquals("Buffer distance should be preserved", BUFFER_DISTANCE, lineString.getBuf(), DELTA);
    }
    
    @Test
    public void testConstructor_WithSinglePoint_CreatesValidLineString() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> singlePoint = createSinglePointList(0.0, 0.0, ctx);
        
        BufferedLineString lineString = new BufferedLineString(singlePoint, BUFFER_DISTANCE, ctx);
        
        assertFalse("LineString with single point should not be empty", lineString.isEmpty());
        assertEquals("Should have one segment for single point", 1, lineString.getSegments().size());
    }
    
    @Test
    public void testConstructor_WithTwoPoints_CreatesLineSegment() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> twoPoints = createTwoPointList(0.0, 0.0, 1.0, 1.0, ctx);
        
        BufferedLineString lineString = new BufferedLineString(twoPoints, BUFFER_DISTANCE, ctx);
        
        assertEquals("Should have one segment for two points", 1, lineString.getSegments().size());
        assertTrue("LineString should have area when buffered", lineString.hasArea());
    }
    
    @Test
    public void testConstructor_WithLongitudeSkewExpansion_AdjustsBuffer() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> points = createTwoPointList(-180.0, 0.0, 180.0, 0.0, ctx);
        boolean expandForSkew = true;
        
        BufferedLineString lineString = new BufferedLineString(points, BUFFER_DISTANCE, expandForSkew, ctx);
        
        assertNotNull("LineString should be created successfully", lineString);
        assertEquals("Original buffer should be preserved", BUFFER_DISTANCE, lineString.getBuf(), DELTA);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_WithNullContext_ThrowsException() {
        List<Point> points = new ArrayList<>();
        new BufferedLineString(points, BUFFER_DISTANCE, null);
    }

    // Geometric Property Tests
    
    @Test
    public void testGetBuf_ReturnsCorrectBufferDistance() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> points = createEmptyPointList();
        double expectedBuffer = 25.5;
        
        BufferedLineString lineString = new BufferedLineString(points, expectedBuffer, ctx);
        
        assertEquals("Buffer distance should match constructor parameter", 
                    expectedBuffer, lineString.getBuf(), DELTA);
    }
    
    @Test
    public void testHasArea_WithPositiveBuffer_ReturnsTrue() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> points = createSinglePointList(0.0, 0.0, ctx);
        
        BufferedLineString lineString = new BufferedLineString(points, BUFFER_DISTANCE, ctx);
        
        assertTrue("LineString with positive buffer should have area", lineString.hasArea());
    }
    
    @Test
    public void testGetBoundingBox_WithSinglePoint_ReturnsValidRectangle() {
        SpatialContext ctx = SpatialContext.GEO;
        List<Point> points = createSinglePointList(10.0, 20.0, ctx);
        
        BufferedLineString lineString = new BufferedLineString(points, 5.0, ctx);
        Rectangle boundingBox = lineString.getBoundingBox();
        
        assertNotNull("Bounding box should not be null", boundingBox);
        assertFalse("Bounding box should not be empty", boundingBox.isEmpty());
    }
    
    @Test
    public void testGetArea_WithGeoContext_CalculatesCorrectArea() {
        SpatialContext geoContext = SpatialContext.GEO;
        List<Point> points = createSinglePointList(0.0, 0.0, geoContext);
        double bufferInDegrees = 1.0;
        
        BufferedLineString lineString = new BufferedLineString(points, bufferInDegrees, geoContext);
        double area = lineString.getArea(geoContext);
        
        assertTrue("Area should be positive for buffered line", area > 0);
    }

    // Spatial Relationship Tests
    
    @Test
    public void testRelate_WithSelf_ReturnsWithin() {
        SpatialContext ctx = SpatialContext.GEO;
        List<Point> points = createSinglePointList(10.0, 10.0, ctx);
        
        BufferedLineString lineString = new BufferedLineString(points, 5.0, ctx);
        SpatialRelation relation = lineString.relate(lineString);
        
        assertEquals("Shape should be within itself", SpatialRelation.WITHIN, relation);
    }
    
    @Test(expected = NullPointerException.class)
    public void testRelate_WithNullShape_ThrowsException() {
        SpatialContext ctx = SpatialContext.GEO;
        List<Point> points = createSinglePointList(0.0, 0.0, ctx);
        
        BufferedLineString lineString = new BufferedLineString(points, BUFFER_DISTANCE, ctx);
        lineString.relate(null);
    }

    // Buffering Tests
    
    @Test
    public void testGetBuffered_CreatesNewBufferedShape() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> points = createSinglePointList(0.0, 0.0, ctx);
        double originalBuffer = 5.0;
        double additionalBuffer = 3.0;
        
        BufferedLineString original = new BufferedLineString(points, originalBuffer, ctx);
        Shape buffered = original.getBuffered(additionalBuffer, ctx);
        
        assertNotNull("Buffered shape should not be null", buffered);
        assertNotEquals("Buffered shape should be different from original", original, buffered);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetBuffered_WithNullContext_ThrowsException() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> points = createEmptyPointList();
        
        BufferedLineString lineString = new BufferedLineString(points, BUFFER_DISTANCE, ctx);
        lineString.getBuffered(5.0, null);
    }

    // Collection and Access Tests
    
    @Test
    public void testGetSegments_WithMultiplePoints_ReturnsCorrectCount() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> points = new ArrayList<>();
        points.add(new PointImpl(0.0, 0.0, ctx));
        points.add(new PointImpl(1.0, 1.0, ctx));
        points.add(new PointImpl(2.0, 2.0, ctx));
        
        BufferedLineString lineString = new BufferedLineString(points, BUFFER_DISTANCE, ctx);
        ShapeCollection<BufferedLine> segments = lineString.getSegments();
        
        assertEquals("Should have n-1 segments for n points", 2, segments.size());
    }
    
    @Test
    public void testGetPoints_ReturnsOriginalPoints() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> originalPoints = createTwoPointList(1.0, 2.0, 3.0, 4.0, ctx);
        
        BufferedLineString lineString = new BufferedLineString(originalPoints, BUFFER_DISTANCE, ctx);
        List<Point> retrievedPoints = lineString.getPoints();
        
        assertNotNull("Retrieved points should not be null", retrievedPoints);
        assertEquals("Should return same number of points", originalPoints.size(), retrievedPoints.size());
    }

    // Equality and Hash Tests
    
    @Test
    public void testEquals_WithSameParameters_ReturnsTrue() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> points = createSinglePointList(1.0, 1.0, ctx);
        
        BufferedLineString lineString1 = new BufferedLineString(points, BUFFER_DISTANCE, ctx);
        BufferedLineString lineString2 = new BufferedLineString(points, BUFFER_DISTANCE, ctx);
        
        assertEquals("LineStrings with same parameters should be equal", lineString1, lineString2);
    }
    
    @Test
    public void testEquals_WithDifferentBuffer_ReturnsFalse() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> points = createSinglePointList(1.0, 1.0, ctx);
        
        BufferedLineString lineString1 = new BufferedLineString(points, 5.0, ctx);
        BufferedLineString lineString2 = new BufferedLineString(points, 10.0, ctx);
        
        assertNotEquals("LineStrings with different buffers should not be equal", lineString1, lineString2);
    }
    
    @Test
    public void testEquals_WithNull_ReturnsFalse() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> points = createEmptyPointList();
        
        BufferedLineString lineString = new BufferedLineString(points, BUFFER_DISTANCE, ctx);
        
        assertNotEquals("LineString should not equal null", lineString, null);
    }
    
    @Test
    public void testEquals_WithSelf_ReturnsTrue() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> points = createEmptyPointList();
        
        BufferedLineString lineString = new BufferedLineString(points, BUFFER_DISTANCE, ctx);
        
        assertEquals("LineString should equal itself", lineString, lineString);
    }
    
    @Test
    public void testHashCode_IsConsistent() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> points = createSinglePointList(0.0, 0.0, ctx);
        
        BufferedLineString lineString = new BufferedLineString(points, BUFFER_DISTANCE, ctx);
        int hash1 = lineString.hashCode();
        int hash2 = lineString.hashCode();
        
        assertEquals("Hash code should be consistent", hash1, hash2);
    }

    // String Representation Tests
    
    @Test
    public void testToString_ContainsBufferInformation() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> points = createSinglePointList(1.0, 2.0, ctx);
        double buffer = 15.5;
        
        BufferedLineString lineString = new BufferedLineString(points, buffer, ctx);
        String stringRepresentation = lineString.toString();
        
        assertNotNull("String representation should not be null", stringRepresentation);
        assertTrue("Should contain buffer information", 
                  stringRepresentation.contains("buf=" + buffer));
        assertTrue("Should contain class name", 
                  stringRepresentation.contains("BufferedLineString"));
    }

    // Edge Case and Error Condition Tests
    
    @Test(expected = RuntimeException.class)
    public void testConstructor_WithInvalidGeographicCoordinates_ThrowsException() {
        SpatialContext geoContext = SpatialContext.GEO;
        List<Point> points = createSinglePointList(0.0, -100.0, geoContext); // Invalid latitude
        
        new BufferedLineString(points, 10.0, geoContext);
    }
    
    @Test
    public void testGetCenter_WithEmptyLineString_ReturnsValidPoint() {
        SpatialContext ctx = createDefaultSpatialContext();
        List<Point> emptyPoints = createEmptyPointList();
        
        BufferedLineString lineString = new BufferedLineString(emptyPoints, BUFFER_DISTANCE, ctx);
        Point center = lineString.getCenter();
        
        assertNotNull("Center should not be null even for empty line string", center);
    }
}