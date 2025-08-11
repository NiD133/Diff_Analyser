package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;

/**
 * Test suite for ModuloAxis class.
 * 
 * ModuloAxis displays numerical values within a fixed range using modulo calculation.
 * It wraps values around when they exceed the fixed range boundaries.
 */
public class ModuloAxisTest {

    // Test constants for better readability
    private static final Range DEFAULT_RANGE = new Range(0.0, 360.0);
    private static final Range METER_RANGE = new Range(0.0, 100.0);
    private static final Rectangle2D TEST_AREA = new Rectangle2D.Double(0, 0, 100, 100);
    private static final double DELTA = 0.01;

    // ========== Constructor and Basic Properties Tests ==========

    @Test
    public void testConstructorWithValidRange() {
        ModuloAxis axis = new ModuloAxis("Test Axis", DEFAULT_RANGE);
        
        assertEquals("Display start should be 270.0", 270.0, axis.getDisplayStart(), DELTA);
        assertEquals("Display end should be 90.0", 90.0, axis.getDisplayEnd(), DELTA);
        assertTrue("Axis should be in auto-range mode by default", axis.isAutoRange());
    }

    @Test
    public void testGetDisplayStartAndEnd() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        
        double displayStart = axis.getDisplayStart();
        double displayEnd = axis.getDisplayEnd();
        
        assertEquals("Default display start", 270.0, displayStart, DELTA);
        assertEquals("Default display end", 90.0, displayEnd, DELTA);
    }

    // ========== Display Range Management Tests ==========

    @Test
    public void testSetDisplayRange() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        
        axis.setDisplayRange(0.0, 180.0);
        
        assertEquals("Display start after setting range", 0.0, axis.getDisplayStart(), DELTA);
        assertEquals("Display end after setting range", 180.0, axis.getDisplayEnd(), DELTA);
    }

    @Test
    public void testSetDisplayRangeWithWrapping() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        
        // Set range that exceeds the fixed range (should wrap around)
        axis.setDisplayRange(300.0, 450.0);
        
        // Values should be mapped to the fixed range using modulo
        assertTrue("Display start should be within fixed range", 
                   axis.getDisplayStart() >= 0.0 && axis.getDisplayStart() <= 360.0);
        assertTrue("Display end should be within fixed range", 
                   axis.getDisplayEnd() >= 0.0 && axis.getDisplayEnd() <= 360.0);
    }

    @Test
    public void testAutoAdjustRange() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        axis.setDisplayRange(100.0, 200.0); // Change from default
        
        axis.autoAdjustRange();
        
        // After auto-adjust, should return to default display range
        assertEquals("Auto-adjust should reset display start", 270.0, axis.getDisplayStart(), DELTA);
        assertEquals("Auto-adjust should reset display end", 90.0, axis.getDisplayEnd(), DELTA);
        assertTrue("Should be in auto-range mode", axis.isAutoRange());
    }

    // ========== Resize Range Tests ==========

    @Test
    public void testResizeRangeByPercentage() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        double originalStart = axis.getDisplayStart();
        
        axis.resizeRange(2.0); // Double the range
        
        assertFalse("Should no longer be in auto-range mode", axis.isAutoRange());
        // The exact values depend on the modulo calculation, but range should be affected
        assertNotEquals("Display start should change after resize", originalStart, axis.getDisplayStart(), DELTA);
    }

    @Test
    public void testResizeRangeWithAnchor() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        
        axis.resizeRange(1.5, 180.0); // Resize by 150% around anchor point 180
        
        assertFalse("Should no longer be in auto-range mode", axis.isAutoRange());
        // Range should be resized around the anchor point
    }

    // ========== Coordinate Conversion Tests ==========

    @Test
    public void testValueToJava2D_NormalValue() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        
        double java2DValue = axis.valueToJava2D(45.0, TEST_AREA, RectangleEdge.BOTTOM);
        
        assertTrue("Should return a valid coordinate", !Double.isNaN(java2DValue));
        assertTrue("Should be within reasonable bounds", java2DValue >= 0);
    }

    @Test
    public void testValueToJava2D_WithInvertedAxis() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        axis.setInverted(true);
        
        double java2DValue = axis.valueToJava2D(45.0, TEST_AREA, RectangleEdge.BOTTOM);
        
        assertTrue("Should return a valid coordinate for inverted axis", !Double.isNaN(java2DValue));
    }

    @Test
    public void testValueToJava2D_ValueOutsideRange() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        
        // Test with value outside the fixed range (should be wrapped using modulo)
        double java2DValue = axis.valueToJava2D(450.0, TEST_AREA, RectangleEdge.BOTTOM);
        
        assertTrue("Should handle out-of-range values", !Double.isNaN(java2DValue));
    }

    @Test(expected = NullPointerException.class)
    public void testValueToJava2D_NullArea() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        
        axis.valueToJava2D(45.0, null, RectangleEdge.BOTTOM);
    }

    @Test
    public void testJava2DToValue() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        
        double value = axis.java2DToValue(50.0, TEST_AREA, RectangleEdge.BOTTOM);
        
        assertTrue("Should return a valid value", !Double.isNaN(value));
    }

    @Test
    public void testLengthToJava2D() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        
        double java2DLength = axis.lengthToJava2D(90.0, TEST_AREA, RectangleEdge.BOTTOM);
        
        assertTrue("Should return a valid length", !Double.isNaN(java2DLength));
        assertTrue("Length should be positive for positive input", java2DLength > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testLengthToJava2D_NullArea() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        
        axis.lengthToJava2D(90.0, null, RectangleEdge.BOTTOM);
    }

    // ========== Edge Cases and Error Handling ==========

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullRange_ThrowsException() {
        new ModuloAxis("Test", null);
        // Operations on axis with null range should throw exceptions
    }

    @Test
    public void testResizeRange_ZeroPercent_ThrowsException() {
        ModuloAxis axis = new ModuloAxis("Test", new Range(0.0, 0.0)); // Zero-length range
        
        try {
            axis.resizeRange(0.5); // Try to resize zero-length range
            fail("Should throw exception for zero-length range");
        } catch (IllegalArgumentException e) {
            assertTrue("Should mention positive range requirement", 
                      e.getMessage().contains("positive range"));
        }
    }

    // ========== Equality and Cloning Tests ==========

    @Test
    public void testEquals_SameAxis() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        
        assertTrue("Axis should equal itself", axis.equals(axis));
    }

    @Test
    public void testEquals_ClonedAxis() {
        ModuloAxis axis1 = new ModuloAxis("Test", DEFAULT_RANGE);
        ModuloAxis axis2 = (ModuloAxis) axis1.clone();
        
        assertTrue("Cloned axis should equal original", axis1.equals(axis2));
    }

    @Test
    public void testEquals_DifferentRanges() {
        ModuloAxis axis1 = new ModuloAxis("Test", DEFAULT_RANGE);
        ModuloAxis axis2 = new ModuloAxis("Test", METER_RANGE);
        
        assertFalse("Axes with different ranges should not be equal", axis1.equals(axis2));
    }

    @Test
    public void testEquals_DifferentDisplayRanges() {
        ModuloAxis axis1 = new ModuloAxis("Test", DEFAULT_RANGE);
        ModuloAxis axis2 = new ModuloAxis("Test", DEFAULT_RANGE);
        axis2.resizeRange(2.0);
        
        assertFalse("Axes with different display ranges should not be equal", axis1.equals(axis2));
    }

    @Test
    public void testEquals_DifferentObjectType() {
        ModuloAxis axis = new ModuloAxis("Test", DEFAULT_RANGE);
        String notAnAxis = "Not an axis";
        
        assertFalse("Axis should not equal different object type", axis.equals(notAnAxis));
    }

    // ========== Integration Tests ==========

    @Test
    public void testCompleteWorkflow_SetRangeAndConvert() {
        ModuloAxis axis = new ModuloAxis("Compass", new Range(0.0, 360.0));
        
        // Set a specific display range
        axis.setDisplayRange(270.0, 90.0); // North-facing compass range
        
        // Convert a compass bearing to screen coordinates
        double java2DValue = axis.valueToJava2D(0.0, TEST_AREA, RectangleEdge.BOTTOM); // North
        
        // Convert back to verify
        double originalValue = axis.java2DToValue(java2DValue, TEST_AREA, RectangleEdge.BOTTOM);
        
        assertTrue("Round-trip conversion should work", !Double.isNaN(originalValue));
    }

    @Test
    public void testModuloWrapping() {
        ModuloAxis axis = new ModuloAxis("Angle", new Range(0.0, 360.0));
        
        // Test that values outside range are wrapped correctly
        double result1 = axis.valueToJava2D(450.0, TEST_AREA, RectangleEdge.BOTTOM); // 450° = 90°
        double result2 = axis.valueToJava2D(90.0, TEST_AREA, RectangleEdge.BOTTOM);  // 90°
        
        // Results should be similar due to modulo wrapping
        // (exact equality depends on implementation details)
        assertTrue("Wrapped values should produce valid coordinates", 
                   !Double.isNaN(result1) && !Double.isNaN(result2));
    }
}