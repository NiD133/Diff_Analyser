package org.locationtech.spatial4j.shape.impl;

import org.junit.Before;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Rectangle;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    private SpatialContext geoCtx;
    private SpatialContext cartesianCtx;

    @Before
    public void setUp() {
        geoCtx = SpatialContext.GEO;
        cartesianCtx = new SpatialContext(false); // A standard Cartesian context
    }

    // =================================================================
    // Initial State Tests
    // =================================================================

    @Test
    public void initialState_hasInfiniteBounds() {
        BBoxCalculator calc = new BBoxCalculator(cartesianCtx);

        assertEquals("Initial minX should be positive infinity", Double.POSITIVE_INFINITY, calc.getMinX(), 0.0);
        assertEquals("Initial maxX should be negative infinity", Double.NEGATIVE_INFINITY, calc.getMaxX(), 0.0);
        assertEquals("Initial minY should be positive infinity", Double.POSITIVE_INFINITY, calc.getMinY(), 0.0);
        assertEquals("Initial maxY should be negative infinity", Double.NEGATIVE_INFINITY, calc.getMaxY(), 0.0);
        assertFalse("Should not wrap the world initially", calc.doesXWorldWrap());
    }

    // =================================================================
    // Cartesian Context Tests (simple, non-wrapping)
    // =================================================================

    @Test
    public void expandRange_withCartesianContext_updatesBoundsCorrectly() {
        BBoxCalculator calc = new BBoxCalculator(cartesianCtx);

        calc.expandRange(10, 20, 30, 40);

        assertEquals(10, calc.getMinX(), 0.0);
        assertEquals(20, calc.getMaxX(), 0.0);
        assertEquals(30, calc.getMinY(), 0.0);
        assertEquals(40, calc.getMaxY(), 0.0);
    }

    @Test
    public void expandRange_withMultipleCalls_accumulatesBounds() {
        BBoxCalculator calc = new BBoxCalculator(cartesianCtx);

        calc.expandRange(10, 20, 30, 40);
        calc.expandRange(5, 25, 20, 50); // Expand the existing box on all sides

        assertEquals("minX should be the minimum of all ranges", 5, calc.getMinX(), 0.0);
        assertEquals("maxX should be the maximum of all ranges", 25, calc.getMaxX(), 0.0);
        assertEquals("minY should be the minimum of all ranges", 20, calc.getMinY(), 0.0);
        assertEquals("maxY should be the maximum of all ranges", 50, calc.getMaxY(), 0.0);
    }

    // =================================================================
    // Geodetic (GEO) Context Tests
    // =================================================================

    @Test
    public void expandXRange_withDatelineCrossingRange_setsWorldWrapFlag() {
        BBoxCalculator calc = new BBoxCalculator(geoCtx);

        // A range from 170E to 170W crosses the dateline
        calc.expandXRange(170, -170);

        assertTrue("A range crossing the dateline should set the world wrap flag", calc.doesXWorldWrap());
    }

    @Test
    public void expandRange_withDatelineCrossingRectangle_setsWorldWrapFlag() {
        BBoxCalculator calc = new BBoxCalculator(geoCtx);
        Rectangle datelineCrossingRect = geoCtx.getShapeFactory().rect(160, -160, 0, 10);

        calc.expandRange(datelineCrossingRect);

        assertTrue("A rectangle crossing the dateline should set the world wrap flag", calc.doesXWorldWrap());
        assertEquals(160, calc.getMinX(), 0.0);
        assertEquals(-160, calc.getMaxX(), 0.0);
    }

    @Test
    public void getBoundary_withNonWrappingRanges_returnsCorrectBBox() {
        BBoxCalculator calc = new BBoxCalculator(geoCtx);

        // Two separate longitude ranges
        calc.expandRange(-20, -10, 0, 10);
        calc.expandRange(10, 20, 0, 10);

        Rectangle boundary = calc.getBoundary();
        assertEquals(-20, boundary.getMinX(), 0.0);
        assertEquals(20, boundary.getMaxX(), 0.0);
        assertFalse("Boundary should not wrap the dateline", boundary.getCrossesDateLine());
    }

    @Test
    public void getBoundary_withDatelineCrossingRanges_returnsSingleWrappingBBox() {
        BBoxCalculator calc = new BBoxCalculator(geoCtx);

        // Two ranges that both cross the dateline
        calc.expandXRange(170, -170); // 20 degrees of longitude width
        calc.expandXRange(160, -160); // 40 degrees of longitude width
        calc.expandRange(0, 0, -10, 10); // Set Y values

        Rectangle boundary = calc.getBoundary();
        assertEquals("The merged range should be the wider of the two", 160, boundary.getMinX(), 0.0);
        assertEquals(-160, boundary.getMaxX(), 0.0);
        assertTrue("Boundary should wrap the dateline", boundary.getCrossesDateLine());
    }

    @Test
    public void getBoundary_whenRangesCombineToWrap_returnsWrappingBBox() {
        BBoxCalculator calc = new BBoxCalculator(geoCtx);

        // Two ranges on opposite sides of the dateline that meet
        calc.expandXRange(170, 180);
        calc.expandXRange(-180, -170);
        calc.expandRange(0, 0, 0, 10); // Set Y values

        Rectangle boundary = calc.getBoundary();
        assertEquals(170, boundary.getMinX(), 0.0);
        assertEquals(-170, boundary.getMaxX(), 0.0);
        assertTrue("The combined ranges should wrap the dateline", boundary.getCrossesDateLine());
    }

    // =================================================================
    // Edge Cases and Error Handling
    // =================================================================

    @Test
    public void expandRange_withNaNCoordinates_ignoresNaNValues() {
        BBoxCalculator calc = new BBoxCalculator(cartesianCtx);
        calc.expandRange(10, 20, 30, 40);

        // Attempt to expand with NaN; it should have no effect
        calc.expandRange(Double.NaN, Double.NaN, Double.NaN, Double.NaN);

        assertEquals("NaN should not alter minX", 10, calc.getMinX(), 0.0);
        assertEquals("NaN should not alter maxX", 20, calc.getMaxX(), 0.0);
        assertEquals("NaN should not alter minY", 30, calc.getMinY(), 0.0);
        assertEquals("NaN should not alter maxY", 40, calc.getMaxY(), 0.0);
    }

    @Test
    public void getBoundary_whenOnlyXIsExpanded_throwsException() {
        BBoxCalculator calc = new BBoxCalculator(cartesianCtx);
        calc.expandXRange(0, 10); // Y remains uninitialized

        try {
            calc.getBoundary();
            fail("getBoundary should throw an exception if Y bounds are not set");
        } catch (RuntimeException e) {
            // Expected: "maxY must be >= minY: Infinity to -Infinity"
            assertTrue(e.getMessage().contains("maxY must be >= minY"));
        }
    }

    @Test
    public void getBoundary_whenNoRangesAdded_throwsException() {
        BBoxCalculator calc = new BBoxCalculator(cartesianCtx);

        try {
            calc.getBoundary();
            fail("getBoundary should throw an exception if no ranges have been added");
        } catch (RuntimeException e) {
            // Expected: "maxY must be >= minY: Infinity to -Infinity"
            assertTrue(e.getMessage().contains("maxY must be >= minY"));
        }
    }

    @Test
    public void constructor_withNullContext_failsOnMethodCall() {
        BBoxCalculator calc = new BBoxCalculator(null);

        try {
            calc.expandRange(0, 1, 2, 3);
            fail("Methods should fail if the context is null");
        } catch (NullPointerException e) {
            // This is the expected behavior
        }
    }
}