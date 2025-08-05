package org.locationtech.spatial4j.shape.impl;

import org.junit.Before;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.SpatialRelation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link BufferedLineString}.
 */
public class BufferedLineString_ESTest {

    private SpatialContext ctx;

    @Before
    public void setUp() {
        // Use a standard geographic context for all tests.
        ctx = SpatialContext.GEO;
    }

    @Test
    public void emptyLineString_shouldHaveCorrectProperties() {
        // Arrange
        List<Point> points = Collections.emptyList();
        double buffer = 10.0;
        BufferedLineString emptyLine = new BufferedLineString(points, buffer, ctx);

        // Assert
        assertTrue("An empty line string should report as empty", emptyLine.isEmpty());
        assertFalse("An empty line string has no points, so it has no area", emptyLine.hasArea());
        assertEquals(0.0, emptyLine.getArea(ctx), 0.0);
        assertEquals(buffer, emptyLine.getBuf(), 0.0);
        assertTrue("Points list should be empty", emptyLine.getPoints().isEmpty());
        assertTrue("Segments list should be empty", emptyLine.getSegments().isEmpty());
        assertTrue("Bounding box of an empty shape should be empty", emptyLine.getBoundingBox().isEmpty());
        assertEquals("An empty shape relating to itself should be disjoint",
                SpatialRelation.DISJOINT, emptyLine.relate(emptyLine));
    }

    @Test
    public void singlePointLineString_shouldBehaveLikeBufferedPoint() {
        // Arrange
        Point point = ctx.makePoint(0, 0);
        List<Point> points = Collections.singletonList(point);
        double buffer = 10.0;
        BufferedLineString singlePointLine = new BufferedLineString(points, buffer, ctx);

        // Assert
        assertFalse(singlePointLine.isEmpty());
        assertTrue("A buffered point should have area", singlePointLine.hasArea());
        assertEquals(1, singlePointLine.getPoints().size());
        assertEquals(point, singlePointLine.getPoints().get(0));
        assertEquals("A single point is treated as a segment from the point to itself",
                1, singlePointLine.getSegments().size());

        // The bounding box should be a square centered on the point, with sides 2 * buffer.
        Rectangle bbox = singlePointLine.getBoundingBox();
        assertFalse(bbox.isEmpty());
        assertEquals(-10.0, bbox.getMinX(), 0.01);
        assertEquals(10.0, bbox.getMaxX(), 0.01);
        assertEquals(-10.0, bbox.getMinY(), 0.01);
        assertEquals(10.0, bbox.getMaxY(), 0.01);
    }

    @Test
    public void multiPointLineString_shouldHaveCorrectProperties() {
        // Arrange
        Point point1 = ctx.makePoint(0, 0);
        Point point2 = ctx.makePoint(10, 0);
        List<Point> points = Arrays.asList(point1, point2);
        double buffer = 5.0;
        BufferedLineString twoPointLine = new BufferedLineString(points, buffer, ctx);

        // Assert
        assertFalse(twoPointLine.isEmpty());
        assertTrue(twoPointLine.hasArea());
        assertEquals(2, twoPointLine.getPoints().size());
        assertEquals("One segment should exist between two points", 1, twoPointLine.getSegments().size());

        // The bounding box should enclose the entire buffered line.
        Rectangle bbox = twoPointLine.getBoundingBox();
        assertEquals("minX should be startX - buffer", -5.0, bbox.getMinX(), 0.01);
        assertEquals("maxX should be endX + buffer", 15.0, bbox.getMaxX(), 0.01);
        assertEquals("minY should be minY - buffer", -5.0, bbox.getMinY(), 0.01);
        assertEquals("maxY should be maxY + buffer", 5.0, bbox.getMaxY(), 0.01);
    }

    @Test
    public void equalsAndHashCode_shouldAdhereToContract() {
        // Arrange
        Point p1 = ctx.makePoint(0, 0);
        Point p2 = ctx.makePoint(10, 10);
        List<Point> points = Arrays.asList(p1, p2);
        double buffer = 5.0;

        BufferedLineString line1 = new BufferedLineString(points, buffer, ctx);
        BufferedLineString line2 = new BufferedLineString(Arrays.asList(p1, p2), buffer, ctx); // Identical
        BufferedLineString differentBuffer = new BufferedLineString(points, buffer + 1, ctx);
        BufferedLineString differentPoints = new BufferedLineString(Collections.singletonList(p1), buffer, ctx);

        // Assert
        assertEquals("An object must be equal to itself", line1, line1);
        assertEquals("Two identical objects must be equal", line1, line2);
        assertEquals("Hashcodes of equal objects must be equal", line1.hashCode(), line2.hashCode());

        assertNotEquals("An object should not be equal to null", null, line1);
        assertNotEquals("An object should not be equal to one of a different type", "a string", line1);
        assertNotEquals("Objects with different buffers should not be equal", line1, differentBuffer);
        assertNotEquals("Objects with different points should not be equal", line1, differentPoints);
    }

    @Test
    public void relate_withSelf_shouldReturnWithin() {
        // Arrange
        Point p1 = ctx.makePoint(0, 0);
        Point p2 = ctx.makePoint(10, 0);
        BufferedLineString line = new BufferedLineString(Arrays.asList(p1, p2), 5.0, ctx);

        // Act
        SpatialRelation relation = line.relate(line);

        // Assert
        assertEquals("A shape related to itself should be WITHIN", SpatialRelation.WITHIN, relation);
    }

    @Test
    public void relate_disjointShapes_shouldReturnDisjoint() {
        // Arrange
        BufferedLineString line1 = new BufferedLineString(Arrays.asList(ctx.makePoint(0, 0), ctx.makePoint(10, 0)), 2.0, ctx);
        BufferedLineString line2 = new BufferedLineString(Arrays.asList(ctx.makePoint(20, 0), ctx.makePoint(30, 0)), 2.0, ctx);

        // Act
        SpatialRelation relation = line1.relate(line2);

        // Assert
        assertEquals(SpatialRelation.DISJOINT, relation);
    }

    @Test
    public void getBuffered_shouldReturnShapeWithNewBuffer() {
        // Arrange
        Point p1 = ctx.makePoint(0, 0);
        List<Point> points = Collections.singletonList(p1);
        double originalBuffer = 5.0;
        BufferedLineString line = new BufferedLineString(points, originalBuffer, ctx);

        // Act
        double newBuffer = 10.0;
        Shape bufferedShape = line.getBuffered(newBuffer, ctx);

        // Assert
        assertTrue(bufferedShape instanceof BufferedLineString);
        BufferedLineString newBufferedLine = (BufferedLineString) bufferedShape;
        assertEquals(newBuffer, newBufferedLine.getBuf(), 0.0);
        assertEquals("Points should be the same in the new buffered shape", line.getPoints(), newBufferedLine.getPoints());
    }

    @Test
    public void toString_shouldReturnCorrectFormat() {
        // Arrange
        Point point1 = ctx.makePoint(10, 20);
        Point point2 = ctx.makePoint(30, 40);
        BufferedLineString line = new BufferedLineString(Arrays.asList(point1, point2), 5.0, ctx);

        // Act
        String result = line.toString();

        // Assert
        assertTrue("toString should include buffer information", result.startsWith("BufferedLineString(buf=5.0"));
        assertTrue("toString should include point coordinates", result.contains("10.0 20.0"));
        assertTrue("toString should include point coordinates", result.contains("30.0 40.0"));
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullContext_shouldThrowException() {
        // Arrange
        List<Point> points = Collections.singletonList(SpatialContext.GEO.makePoint(0, 0));
        
        // Act
        new BufferedLineString(points, 1.0, null);
    }

    @Test(expected = RuntimeException.class)
    public void constructor_withInvalidCoordinates_shouldThrowException() {
        // Arrange
        // Using a large latitude that, when buffered, exceeds 90 degrees in a GEO context.
        Point point = ctx.makePoint(0, 85);
        double largeBuffer = 10.0; // 85 + 10 > 90

        // Act & Assert
        // This is expected to fail during the internal bounding box calculation.
        new BufferedLineString(Collections.singletonList(point), largeBuffer, true, ctx);
    }
}