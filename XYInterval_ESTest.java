package org.jfree.data.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for XYInterval class.
 * Tests the getter methods and equals functionality of XYInterval objects.
 */
@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class XYInterval_ESTest extends XYInterval_ESTest_scaffolding {

    // Test data constants for better readability
    private static final double ZERO = 0.0;
    private static final double ONE = 1.0;
    private static final double NEGATIVE_ONE = -1.0;
    private static final double LARGE_POSITIVE = 2850.5914155;
    private static final double LARGE_NEGATIVE = -4479.95885;
    private static final double DELTA = 0.01;

    // ========== Getter Method Tests ==========

    @Test(timeout = 4000)
    public void testGetXLow_WithPositiveValues() {
        XYInterval interval = new XYInterval(LARGE_POSITIVE, LARGE_POSITIVE, LARGE_POSITIVE, LARGE_POSITIVE, -1461.548);
        
        double actualXLow = interval.getXLow();
        
        assertEquals("XLow should match constructor parameter", LARGE_POSITIVE, actualXLow, DELTA);
    }

    @Test(timeout = 4000)
    public void testGetXLow_WithNegativeValues() {
        XYInterval interval = new XYInterval(-2082.436, NEGATIVE_ONE, 1907.73, ZERO, ONE);
        
        double actualXLow = interval.getXLow();
        
        assertEquals("XLow should match constructor parameter", -2082.436, actualXLow, DELTA);
    }

    @Test(timeout = 4000)
    public void testGetXHigh_WithPositiveValues() {
        XYInterval interval = new XYInterval(LARGE_POSITIVE, LARGE_POSITIVE, LARGE_POSITIVE, LARGE_POSITIVE, -1461.548);
        
        double actualXHigh = interval.getXHigh();
        
        assertEquals("XHigh should match constructor parameter", LARGE_POSITIVE, actualXHigh, DELTA);
    }

    @Test(timeout = 4000)
    public void testGetXHigh_WithNegativeValues() {
        XYInterval interval = new XYInterval(-2082.436, NEGATIVE_ONE, 1907.73, ZERO, ONE);
        
        double actualXHigh = interval.getXHigh();
        
        assertEquals("XHigh should match constructor parameter", NEGATIVE_ONE, actualXHigh, DELTA);
    }

    @Test(timeout = 4000)
    public void testGetY_WithPositiveValue() {
        XYInterval interval = new XYInterval(LARGE_POSITIVE, LARGE_POSITIVE, LARGE_POSITIVE, LARGE_POSITIVE, -1461.548);
        
        double actualY = interval.getY();
        
        assertEquals("Y should match constructor parameter", LARGE_POSITIVE, actualY, DELTA);
    }

    @Test(timeout = 4000)
    public void testGetY_WithNegativeValue() {
        XYInterval interval = new XYInterval(-1607.64, -1607.64, -1607.64, ZERO, ZERO);
        
        double actualY = interval.getY();
        
        assertEquals("Y should match constructor parameter", -1607.64, actualY, DELTA);
    }

    @Test(timeout = 4000)
    public void testGetYLow_WithPositiveValue() {
        XYInterval interval = new XYInterval(LARGE_POSITIVE, LARGE_POSITIVE, LARGE_POSITIVE, LARGE_POSITIVE, -1461.548);
        
        double actualYLow = interval.getYLow();
        
        assertEquals("YLow should match constructor parameter", LARGE_POSITIVE, actualYLow, DELTA);
    }

    @Test(timeout = 4000)
    public void testGetYLow_WithNegativeValue() {
        XYInterval interval = new XYInterval(NEGATIVE_ONE, NEGATIVE_ONE, ZERO, -3333.32, NEGATIVE_ONE);
        
        double actualYLow = interval.getYLow();
        
        assertEquals("YLow should match constructor parameter", -3333.32, actualYLow, DELTA);
    }

    @Test(timeout = 4000)
    public void testGetYHigh_WithPositiveValue() {
        XYInterval interval = new XYInterval(ONE, ONE, ONE, ONE, ONE);
        
        double actualYHigh = interval.getYHigh();
        
        assertEquals("YHigh should match constructor parameter", ONE, actualYHigh, DELTA);
    }

    @Test(timeout = 4000)
    public void testGetYHigh_WithNegativeValue() {
        XYInterval interval = new XYInterval(ZERO, ZERO, ZERO, ZERO, LARGE_NEGATIVE);
        
        double actualYHigh = interval.getYHigh();
        
        assertEquals("YHigh should match constructor parameter", LARGE_NEGATIVE, actualYHigh, DELTA);
    }

    // ========== Equals Method Tests ==========

    @Test(timeout = 4000)
    public void testEquals_SameObject_ReturnsTrue() {
        XYInterval interval = new XYInterval(LARGE_NEGATIVE, LARGE_NEGATIVE, -446.52, -482.47, ONE);
        
        boolean result = interval.equals(interval);
        
        assertTrue("Object should equal itself", result);
    }

    @Test(timeout = 4000)
    public void testEquals_IdenticalValues_ReturnsTrue() {
        XYInterval interval1 = new XYInterval(ZERO, ZERO, ZERO, ZERO, ZERO);
        XYInterval interval2 = new XYInterval(ZERO, ZERO, ZERO, ZERO, ZERO);
        
        boolean result = interval1.equals(interval2);
        
        assertTrue("Intervals with identical values should be equal", result);
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentXLow_ReturnsFalse() {
        XYInterval interval1 = new XYInterval(ZERO, ZERO, ZERO, ZERO, ZERO);
        XYInterval interval2 = new XYInterval(ONE, ZERO, ZERO, ZERO, ZERO);
        
        boolean result = interval1.equals(interval2);
        
        assertFalse("Intervals with different xLow should not be equal", result);
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentXHigh_ReturnsFalse() {
        XYInterval interval1 = new XYInterval(ZERO, NEGATIVE_ONE, ZERO, ZERO, NEGATIVE_ONE);
        XYInterval interval2 = new XYInterval(ZERO, ZERO, ONE, ZERO, ZERO);
        
        boolean result = interval1.equals(interval2);
        
        assertFalse("Intervals with different xHigh should not be equal", result);
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentY_ReturnsFalse() {
        XYInterval interval1 = new XYInterval(ZERO, ZERO, ZERO, ZERO, ZERO);
        XYInterval interval2 = new XYInterval(ZERO, ZERO, -2810.0, 2884.61, -2810.0);
        
        boolean result = interval1.equals(interval2);
        
        assertFalse("Intervals with different y values should not be equal", result);
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentYLow_ReturnsFalse() {
        XYInterval interval1 = new XYInterval(ZERO, ZERO, ZERO, ZERO, ZERO);
        XYInterval interval2 = new XYInterval(ZERO, ZERO, ZERO, 3289.17, ZERO);
        
        boolean result = interval1.equals(interval2);
        
        assertFalse("Intervals with different yLow should not be equal", result);
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentYHigh_ReturnsFalse() {
        XYInterval interval1 = new XYInterval(ZERO, ZERO, ZERO, ZERO, ZERO);
        XYInterval interval2 = new XYInterval(ZERO, ZERO, ZERO, ZERO, -482.47);
        
        boolean result = interval1.equals(interval2);
        
        assertFalse("Intervals with different yHigh should not be equal", result);
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentObjectType_ReturnsFalse() {
        XYInterval interval = new XYInterval(ZERO, ZERO, ZERO, ZERO, ZERO);
        Object differentObject = new Object();
        
        boolean result = interval.equals(differentObject);
        
        assertFalse("XYInterval should not equal objects of different types", result);
    }

    @Test(timeout = 4000)
    public void testEquals_ComplexDifferentValues_ReturnsFalse() {
        XYInterval interval1 = new XYInterval(689.67, 689.67, 689.67, 689.67, 689.67);
        XYInterval interval2 = new XYInterval(689.67, NEGATIVE_ONE, NEGATIVE_ONE, 689.67, NEGATIVE_ONE);
        
        boolean result = interval1.equals(interval2);
        
        assertFalse("Intervals with multiple different values should not be equal", result);
    }

    // ========== Edge Case Tests ==========

    @Test(timeout = 4000)
    public void testAllGetters_WithZeroValues() {
        XYInterval interval = new XYInterval(ZERO, ZERO, ZERO, ZERO, ZERO);
        
        assertEquals("XLow should be zero", ZERO, interval.getXLow(), DELTA);
        assertEquals("XHigh should be zero", ZERO, interval.getXHigh(), DELTA);
        assertEquals("Y should be zero", ZERO, interval.getY(), DELTA);
        assertEquals("YLow should be zero", ZERO, interval.getYLow(), DELTA);
        assertEquals("YHigh should be zero", ZERO, interval.getYHigh(), DELTA);
    }
}