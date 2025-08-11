package org.locationtech.spatial4j.shape.impl;

import org.junit.Before;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.SpatialRelation;

import static org.junit.Assert.*;

/**
 * A set of tests for the BufferedLine class, focusing on understandability and maintainability.
 */
public class BufferedLineTest {

    private SpatialContext cartesianCtx;
    private SpatialContext geoCtx;

    @Before
    public void setUp() {
        // A simple Cartesian context for predictable geometric calculations.
        cartesianCtx = new SpatialContext(new SpatialContextFactory());
        // A geographic context (lat/lon) for geo-specific tests.
        geoCtx = SpatialContext.GEO;
    }

    // =================================================================
    // Constructor Tests
    // =================================================================

    @Test(expected = AssertionError.class)
    public void constructor_withNegativeBuffer_shouldThrowException() {
        // Arrange
        Point p1 = cartesianCtx.makePoint(0, 0);

        // Act & Assert
        new BufferedLine(p1, p1, -1.0, cartesianCtx);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullPoints_shouldThrowException() {
        // Act & Assert
        new BufferedLine(null, null, 1.0, cartesianCtx);
    }

    @Test(expected = RuntimeException.class)
    public void constructor_withInvalidGeoCoordinates_shouldThrowException() {
        // Arrange: Latitude 91 is invalid in a geographic context.
        Point invalidPoint = geoCtx.makePoint(0, 91);

        // Act & Assert
        new BufferedLine(invalidPoint, invalidPoint, 1.0, geoCtx);
    }

    // =================================================================
    // Accessor and Property Tests
    // =================================================================

    @Test
    public void accessors_shouldReturnCorrectValues() {
        // Arrange
        Point pA = cartesianCtx.makePoint(10, 20);
        Point pB = cartesianCtx.makePoint(30, 20);
        double buffer = 5.0;
        BufferedLine line = new BufferedLine(pA, pB, buffer, cartesianCtx);

        // Act & Assert
        assertEquals("getA() should return the starting point", pA, line.getA());
        assertEquals("getB() should return the ending point", pB, line.getB());
        assertEquals("getBuf() should return the buffer distance", buffer, line.getBuf(), 0.0);
        assertEquals("getCenter() should return the midpoint", cartesianCtx.makePoint(20, 20), line.getCenter());
        assertFalse("A buffered line should not be empty", line.isEmpty());
    }

    @Test
    public void hasArea_shouldBeTrueForPositiveBuffer() {
        // Arrange
        Point p = cartesianCtx.makePoint(0, 0);
        BufferedLine lineWithBuffer = new BufferedLine(p, p, 1.0, cartesianCtx);

        // Assert
        assertTrue(lineWithBuffer.hasArea());
    }

    @Test
    public void hasArea_shouldBeFalseForZeroBuffer() {
        // Arrange
        Point p = cartesianCtx.makePoint(0, 0);
        BufferedLine lineWithoutBuffer = new BufferedLine(p, p, 0.0, cartesianCtx);

        // Assert
        assertFalse(lineWithoutBuffer.hasArea());
    }

    @Test
    public void getArea_withZeroBuffer_shouldBeZero() {
        // Arrange
        Point pA = cartesianCtx.makePoint(0, 0);
        Point pB = cartesianCtx.makePoint(10, 0);
        BufferedLine line = new BufferedLine(pA, pB, 0, cartesianCtx);

        // Act
        double area = line.getArea(cartesianCtx);

        // Assert
        assertEquals(0.0, area, 0.0);
    }

    @Test
    public void getArea_forBufferedLine_shouldBeCalculatedCorrectly() {
        // Arrange
        Point pA = cartesianCtx.makePoint(0, 0);
        Point pB = cartesianCtx.makePoint(10, 0); // A line of length 10
        double buffer = 2.0;
        BufferedLine line = new BufferedLine(pA, pB, buffer, cartesianCtx);

        // Act
        double area = line.getArea(cartesianCtx);

        // Assert: The area of a stadium shape is a central rectangle plus a circle at the ends.
        // Area = (length * 2 * buffer) + (PI * buffer^2)
        double expectedArea = (10.0 * 2.0 * buffer) + (Math.PI * buffer * buffer);
        assertEquals(expectedArea, area, 0.01);
    }

    // =================================================================
    // Behavior Tests (relate, contains, etc.)
    // =================================================================

    @Test
    public void contains_shouldCorrectlyIdentifyPoints() {
        // Arrange
        Point pA = cartesianCtx.makePoint(0, 0);
        Point pB = cartesianCtx.makePoint(10, 0);
        BufferedLine line = new BufferedLine(pA, pB, 2.0, cartesianCtx);

        // Assert
        assertTrue("Point on the line segment should be contained", line.contains(cartesianCtx.makePoint(5, 0)));
        assertTrue("Point within buffer distance should be contained", line.contains(cartesianCtx.makePoint(5, 1.5)));
        assertTrue("Point near endpoint within buffer should be contained", line.contains(cartesianCtx.makePoint(10, 1.5)));
        assertFalse("Point outside buffer distance should not be contained", line.contains(cartesianCtx.makePoint(5, 3)));
        assertFalse("Point past the end of the segment should not be contained", line.contains(cartesianCtx.makePoint(15, 0)));
    }

    @Test
    public void relate_withContainedRectangle_shouldReturnContains() {
        // Arrange
        Point pA = cartesianCtx.makePoint(0, 0);
        Point pB = cartesianCtx.makePoint(20, 0);
        BufferedLine line = new BufferedLine(pA, pB, 5.0, cartesianCtx);
        Rectangle containedRect = cartesianCtx.makeRectangle(5, 15, 1, 2);

        // Act & Assert
        assertEquals(SpatialRelation.CONTAINS, line.relate(containedRect));
    }

    @Test
    public void relate_withContainingRectangle_shouldReturnWithin() {
        // Arrange
        Point pA = cartesianCtx.makePoint(10, 10);
        Point pB = cartesianCtx.makePoint(20, 10);
        BufferedLine line = new BufferedLine(pA, pB, 1.0, cartesianCtx);
        Rectangle containingRect = cartesianCtx.makeRectangle(0, 30, 0, 30);

        // Act & Assert
        assertEquals(SpatialRelation.WITHIN, line.relate(containingRect));
    }

    @Test
    public void relate_withIntersectingRectangle_shouldReturnIntersects() {
        // Arrange
        Point pA = cartesianCtx.makePoint(0, 0);
        Point pB = cartesianCtx.makePoint(10, 10);
        BufferedLine line = new BufferedLine(pA, pB, 2.0, cartesianCtx);
        Rectangle intersectingRect = cartesianCtx.makeRectangle(8, 12, 8, 12);

        // Act & Assert
        assertEquals(SpatialRelation.INTERSECTS, line.relate(intersectingRect));
    }

    @Test
    public void relate_withDisjointRectangle_shouldReturnDisjoint() {
        // Arrange
        Point pA = cartesianCtx.makePoint(0, 0);
        Point pB = cartesianCtx.makePoint(10, 0);
        BufferedLine line = new BufferedLine(pA, pB, 2.0, cartesianCtx);
        Rectangle disjointRect = cartesianCtx.makeRectangle(100, 110, 0, 10);

        // Act & Assert
        assertEquals(SpatialRelation.DISJOINT, line.relate(disjointRect));
    }

    @Test
    public void relate_withInfiniteBuffer_shouldContainRectangle() {
        // Arrange
        Point p = geoCtx.makePoint(0, 0);
        BufferedLine infiniteLine = new BufferedLine(p, p, Double.POSITIVE_INFINITY, geoCtx);
        Rectangle rect = geoCtx.makeRectangle(0, 10, 0, 10);

        // Act & Assert
        assertEquals(SpatialRelation.CONTAINS, infiniteLine.relate(rect));
    }

    // =================================================================
    // Bounding Box Tests
    // =================================================================

    @Test
    public void getBoundingBox_forDegenerateLine_isCorrect() {
        // Arrange: A degenerate line (a point) is effectively a circle.
        Point center = cartesianCtx.makePoint(10, 20);
        double buffer = 5.0;
        BufferedLine line = new BufferedLine(center, center, buffer, cartesianCtx);

        // Act
        Rectangle bbox = line.getBoundingBox();

        // Assert
        assertEquals(10 - buffer, bbox.getMinX(), 0.0);
        assertEquals(10 + buffer, bbox.getMaxX(), 0.0);
        assertEquals(20 - buffer, bbox.getMinY(), 0.0);
        assertEquals(20 + buffer, bbox.getMaxY(), 0.0);
    }

    @Test
    public void getBoundingBox_forHorizontalLine_isCorrect() {
        // Arrange
        Point pA = cartesianCtx.makePoint(10, 30);
        Point pB = cartesianCtx.makePoint(50, 30);
        double buffer = 5.0;
        BufferedLine line = new BufferedLine(pA, pB, buffer, cartesianCtx);

        // Act
        Rectangle bbox = line.getBoundingBox();

        // Assert
        assertEquals(10 - buffer, bbox.getMinX(), 0.0);
        assertEquals(50 + buffer, bbox.getMaxX(), 0.0);
        assertEquals(30 - buffer, bbox.getMinY(), 0.0);
        assertEquals(30 + buffer, bbox.getMaxY(), 0.0);
    }

    // =================================================================
    // `equals` and `hashCode` Tests
    // =================================================================

    @Test
    public void equalsAndHashCode_shouldAdhereToContract() {
        // Arrange
        Point p1 = cartesianCtx.makePoint(0, 0);
        Point p2 = cartesianCtx.makePoint(10, 10);
        Point p3 = cartesianCtx.makePoint(20, 20);

        BufferedLine lineA = new BufferedLine(p1, p2, 5.0, cartesianCtx);
        BufferedLine lineA_duplicate = new BufferedLine(p1, p2, 5.0, cartesianCtx);
        BufferedLine lineB_differentPoints = new BufferedLine(p1, p3, 5.0, cartesianCtx);
        BufferedLine lineC_differentBuffer = new BufferedLine(p1, p2, 10.0, cartesianCtx);
        BufferedLine lineD_swappedPoints = new BufferedLine(p2, p1, 5.0, cartesianCtx);

        // Assert equals
        assertEquals("A line should equal itself", lineA, lineA);
        assertEquals("Lines with same properties should be equal", lineA, lineA_duplicate);
        assertNotEquals("A line should not equal null", lineA, null);
        assertNotEquals("A line should not equal a different type", lineA, new Object());
        assertNotEquals("Lines with different points should not be equal", lineA, lineB_differentPoints);
        assertNotEquals("Lines with different buffers should not be equal", lineA, lineC_differentBuffer);
        assertNotEquals("Lines with swapped points should not be equal", lineA, lineD_swappedPoints);

        // Assert hashCode
        assertEquals("Hashcodes for equal objects must be the same", lineA.hashCode(), lineA_duplicate.hashCode());
    }

    // =================================================================
    // `toString` Test
    // =================================================================

    @Test
    public void toString_shouldReturnExpectedFormat() {
        // Arrange
        Point p = cartesianCtx.makePoint(0, 0);
        BufferedLine line = new BufferedLine(p, p, 0.0, cartesianCtx);

        // Act
        String result = line.toString();

        // Assert
        assertEquals("BufferedLine(Pt(x=0.0,y=0.0), Pt(x=0.0,y=0.0) b=0.0)", result);
    }
}