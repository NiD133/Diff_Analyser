package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.impl.BBoxCalculator;
import org.locationtech.spatial4j.shape.impl.PointImpl;
import org.locationtech.spatial4j.shape.impl.RectangleImpl;

/**
 * Test suite for BBoxCalculator functionality.
 * Tests bounding box calculations for various spatial scenarios including
 * coordinate expansion, world wrapping, and edge cases.
 */
public class BBoxCalculatorTest {

    // Test constants for better readability
    private static final double LONGITUDE_MIN = -180.0;
    private static final double LONGITUDE_MAX = 180.0;
    private static final double LATITUDE_MIN = -90.0;
    private static final double LATITUDE_MAX = 90.0;
    private static final double DELTA = 0.01;

    // Helper method to create a standard spatial context
    private SpatialContext createSpatialContext() {
        return new SpatialContextFactory().newSpatialContext();
    }

    @Test
    public void testExpandXRange_WithNegativeInfinity_SetsCorrectMaxX() {
        SpatialContext context = createSpatialContext();
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        calculator.expandXRange(Double.NEGATIVE_INFINITY, 0.0);
        
        assertEquals("MaxX should be set to 0.0", 0.0, calculator.getMaxX(), DELTA);
    }

    @Test
    public void testExpandXRange_WithPositiveInfinity_UpdatesCorrectly() {
        SpatialContext context = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        calculator.expandXRange(1107.25, Double.POSITIVE_INFINITY);
        calculator.expandRange(-59.74, 6.28, 1.0, -180.0);
        
        assertEquals("MinY should be updated", 1.0, calculator.getMinY(), DELTA);
        assertEquals("MaxX should be updated", 6.28, calculator.getMaxX(), DELTA);
    }

    @Test
    public void testExpandRange_WithNormalCoordinates_UpdatesMinY() {
        SpatialContext context = createSpatialContext();
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        calculator.expandRange(2645.56, -1.0, 0.0, -388.0);
        
        assertEquals("MinY should be 0.0", 0.0, calculator.getMinY(), DELTA);
        assertEquals("MaxY should be -388.0", -388.0, calculator.getMaxY(), DELTA);
    }

    @Test
    public void testExpandRange_WithEqualCoordinates_SetsMinY() {
        SpatialContext context = createSpatialContext();
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        calculator.expandRange(1.0, 1.0, -388.0, 0.0);
        
        assertEquals("MinY should be -388.0", -388.0, calculator.getMinY(), DELTA);
    }

    @Test
    public void testExpandRange_UpdatesMinXCorrectly() {
        SpatialContext context = createSpatialContext();
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        calculator.expandRange(0.0, 1.0, 1.0, -388.0);
        
        assertEquals("MinX should be 0.0", 0.0, calculator.getMinX(), DELTA);
        assertEquals("MaxY should be -388.0", -388.0, calculator.getMaxY(), DELTA);
    }

    @Test
    public void testExpandRange_WithNegativeCoordinates_UpdatesMinX() {
        SpatialContext context = createSpatialContext();
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        calculator.expandRange(-388.0, 1.0, -388.0, -2148.3);
        
        assertEquals("MinX should be -388.0", -388.0, calculator.getMinX(), DELTA);
        assertEquals("MinY should be -388.0", -388.0, calculator.getMinY(), DELTA);
    }

    @Test
    public void testGetBoundary_WithValidRange_ReturnsRectangle() {
        SpatialContext context = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        calculator.expandRange(1.0, 488.83, -90.0, -1.0);
        calculator.expandXRange(-4.51, -90.0);
        
        Rectangle boundary = calculator.getBoundary();
        assertNotNull("Boundary should not be null", boundary);
        assertEquals("MinY should be -90.0", -90.0, calculator.getMinY(), DELTA);
    }

    @Test
    public void testGetBoundary_WithNaNValues_HandlesGracefully() {
        SpatialContext context = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        calculator.expandRange(Double.NaN, 0.0, 0.0, 0.0);
        
        Rectangle boundary = calculator.getBoundary();
        assertEquals("MaxX should be 0.0", 0.0, boundary.getMaxX(), DELTA);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBoundary_WithNullContext_ThrowsException() {
        BBoxCalculator calculator = new BBoxCalculator(null);
        calculator.getBoundary();
    }

    @Test(expected = NullPointerException.class)
    public void testExpandXRange_WithNullContext_ThrowsException() {
        BBoxCalculator calculator = new BBoxCalculator(null);
        calculator.expandXRange(-751.78, -751.78);
    }

    @Test(expected = NullPointerException.class)
    public void testExpandRange_WithNullRectangle_ThrowsException() {
        SpatialContext context = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(context);
        calculator.expandRange((Rectangle) null);
    }

    @Test
    public void testInitialState_HasCorrectDefaultValues() {
        SpatialContext context = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        assertEquals("Initial MaxX should be negative infinity", 
                    Double.NEGATIVE_INFINITY, calculator.getMaxX(), DELTA);
        assertEquals("Initial MinX should be positive infinity", 
                    Double.POSITIVE_INFINITY, calculator.getMinX(), DELTA);
        assertEquals("Initial MaxY should be negative infinity", 
                    Double.NEGATIVE_INFINITY, calculator.getMaxY(), DELTA);
        assertEquals("Initial MinY should be positive infinity", 
                    Double.POSITIVE_INFINITY, calculator.getMinY(), DELTA);
    }

    @Test
    public void testDoesXWorldWrap_WithNormalRange_ReturnsFalse() {
        SpatialContext context = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        calculator.expandRange(-180.0, -431.7, -180.0, -431.7);
        
        assertFalse("Should not wrap with normal range", calculator.doesXWorldWrap());
    }

    @Test
    public void testDoesXWorldWrap_WithWorldBounds_ReturnsTrue() {
        SpatialContext context = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(context);
        Rectangle worldBounds = context.getWorldBounds();
        
        calculator.expandRange(worldBounds);
        
        assertTrue("Should wrap with world bounds", calculator.doesXWorldWrap());
    }

    @Test
    public void testExpandXRange_WithComplexScenario_HandlesCorrectly() {
        SpatialContext context = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        calculator.expandXRange(-10.37, -10.37);
        calculator.expandXRange(685.72, -10.37);
        
        // Verify the calculator maintains state correctly
        assertEquals("MinY should remain positive infinity", 
                    Double.POSITIVE_INFINITY, calculator.getMinY(), DELTA);
        assertEquals("MaxY should remain negative infinity", 
                    Double.NEGATIVE_INFINITY, calculator.getMaxY(), DELTA);
    }

    @Test
    public void testExpandRange_WithGeodeticCalculation_WorksCorrectly() {
        SpatialContext context = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(context);
        GeodesicSphereDistCalc.LawOfCosines distCalc = new GeodesicSphereDistCalc.LawOfCosines();
        
        PointImpl startPoint = new PointImpl(0.621, 0.621, context);
        Point endPoint = distCalc.pointOnBearing(startPoint, 0.621, 0.621, context, startPoint);
        RectangleImpl rectangle = new RectangleImpl(startPoint, endPoint, context);
        
        calculator.expandRange(rectangle);
        calculator.expandXRange(0.621, 0.621);
        
        Rectangle boundary = calculator.getBoundary();
        assertNotNull("Boundary should be calculated", boundary);
    }

    @Test(expected = RuntimeException.class)
    public void testGetBoundary_WithInvalidInfinityRange_ThrowsException() {
        SpatialContext context = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        calculator.expandXRange(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        calculator.getBoundary(); // Should throw "maxY must be >= minY" exception
    }

    @Test
    public void testExpandXRange_WithInfinityValues_HandlesEdgeCases() {
        SpatialContext context = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        // Test various infinity combinations
        calculator.expandXRange(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        calculator.expandRange(-5743.75, -5743.75, Double.NEGATIVE_INFINITY, -5743.75);
        calculator.expandXRange(-5743.75, Double.NEGATIVE_INFINITY);
        
        assertEquals("MinY should be negative infinity", 
                    Double.NEGATIVE_INFINITY, calculator.getMinY(), DELTA);
    }

    @Test
    public void testExpandXRange_WithNaNValues_HandlesGracefully() {
        SpatialContext context = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(context);
        
        calculator.expandXRange(1198.23, Double.NaN);
        calculator.expandXRange(1198.23, Double.NaN);
        
        // Verify calculator maintains expected state with NaN values
        assertEquals("MinY should remain positive infinity", 
                    Double.POSITIVE_INFINITY, calculator.getMinY(), DELTA);
        assertEquals("MaxY should remain negative infinity", 
                    Double.NEGATIVE_INFINITY, calculator.getMaxY(), DELTA);
    }
}