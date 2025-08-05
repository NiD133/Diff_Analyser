package org.jfree.chart.plot;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.SystemColor;
import org.jfree.data.Range;

/**
 * Test suite for the MeterInterval class.
 * Tests constructor behavior, getter methods, and equality comparisons.
 */
public class MeterInterval_ESTest {

    // Test data constants
    private static final String TEST_LABEL = "Test Interval";
    private static final String EMPTY_LABEL = "";
    private static final Range VALID_RANGE = new Range(0.0, 100.0);
    private static final Range SINGLE_VALUE_RANGE = new Range(50.0, 50.0);
    private static final Range NEGATIVE_RANGE = new Range(-100.0, -50.0);

    // ========== Constructor Tests ==========

    @Test
    public void testSimpleConstructor_WithValidParameters() {
        MeterInterval interval = new MeterInterval(TEST_LABEL, VALID_RANGE);
        
        assertEquals(TEST_LABEL, interval.getLabel());
        assertEquals(VALID_RANGE, interval.getRange());
        assertNotNull("Default outline paint should not be null", interval.getOutlinePaint());
        assertNotNull("Default outline stroke should not be null", interval.getOutlineStroke());
        assertNull("Default background paint should be null", interval.getBackgroundPaint());
    }

    @Test
    public void testFullConstructor_WithAllParameters() {
        Color backgroundColor = Color.BLUE;
        BasicStroke outlineStroke = new BasicStroke(3.0f);
        Color outlineColor = Color.RED;
        
        MeterInterval interval = new MeterInterval(TEST_LABEL, VALID_RANGE, 
                                                  outlineColor, outlineStroke, backgroundColor);
        
        assertEquals(TEST_LABEL, interval.getLabel());
        assertEquals(VALID_RANGE, interval.getRange());
        assertEquals(outlineColor, interval.getOutlinePaint());
        assertEquals(outlineStroke, interval.getOutlineStroke());
        assertEquals(backgroundColor, interval.getBackgroundPaint());
    }

    @Test
    public void testFullConstructor_WithNullOptionalParameters() {
        MeterInterval interval = new MeterInterval(TEST_LABEL, VALID_RANGE, 
                                                  null, null, null);
        
        assertEquals(TEST_LABEL, interval.getLabel());
        assertEquals(VALID_RANGE, interval.getRange());
        assertNull("Outline paint should be null when explicitly set", interval.getOutlinePaint());
        assertNull("Outline stroke should be null when explicitly set", interval.getOutlineStroke());
        assertNull("Background paint should be null when explicitly set", interval.getBackgroundPaint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleConstructor_WithNullRange_ThrowsException() {
        new MeterInterval(TEST_LABEL, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFullConstructor_WithNullRange_ThrowsException() {
        new MeterInterval(TEST_LABEL, null, Color.RED, new BasicStroke(), Color.BLUE);
    }

    // ========== Getter Method Tests ==========

    @Test
    public void testGetLabel_ReturnsCorrectLabel() {
        MeterInterval interval = new MeterInterval(TEST_LABEL, VALID_RANGE);
        assertEquals(TEST_LABEL, interval.getLabel());
    }

    @Test
    public void testGetLabel_WithEmptyString() {
        MeterInterval interval = new MeterInterval(EMPTY_LABEL, VALID_RANGE);
        assertEquals(EMPTY_LABEL, interval.getLabel());
    }

    @Test
    public void testGetRange_ReturnsCorrectRange() {
        MeterInterval interval = new MeterInterval(TEST_LABEL, VALID_RANGE);
        Range returnedRange = interval.getRange();
        
        assertEquals(VALID_RANGE.getLowerBound(), returnedRange.getLowerBound(), 0.001);
        assertEquals(VALID_RANGE.getUpperBound(), returnedRange.getUpperBound(), 0.001);
    }

    @Test
    public void testGetRange_WithSingleValueRange() {
        MeterInterval interval = new MeterInterval(TEST_LABEL, SINGLE_VALUE_RANGE);
        Range returnedRange = interval.getRange();
        
        assertEquals(50.0, returnedRange.getLowerBound(), 0.001);
        assertEquals(50.0, returnedRange.getUpperBound(), 0.001);
    }

    @Test
    public void testGetBackgroundPaint_WithDefaultConstructor() {
        MeterInterval interval = new MeterInterval(TEST_LABEL, VALID_RANGE);
        assertNull("Default background paint should be null", interval.getBackgroundPaint());
    }

    @Test
    public void testGetBackgroundPaint_WithCustomColor() {
        Color customColor = Color.GREEN;
        MeterInterval interval = new MeterInterval(TEST_LABEL, VALID_RANGE, 
                                                  Color.BLACK, new BasicStroke(), customColor);
        assertEquals(customColor, interval.getBackgroundPaint());
    }

    @Test
    public void testGetOutlineStroke_WithDefaultConstructor() {
        MeterInterval interval = new MeterInterval(TEST_LABEL, VALID_RANGE);
        Stroke stroke = interval.getOutlineStroke();
        
        assertNotNull("Default outline stroke should not be null", stroke);
        assertTrue("Default stroke should be BasicStroke", stroke instanceof BasicStroke);
    }

    @Test
    public void testGetOutlinePaint_WithDefaultConstructor() {
        MeterInterval interval = new MeterInterval(TEST_LABEL, VALID_RANGE);
        Paint paint = interval.getOutlinePaint();
        
        assertNotNull("Default outline paint should not be null", paint);
    }

    // ========== Equality Tests ==========

    @Test
    public void testEquals_SameInstance_ReturnsTrue() {
        MeterInterval interval = new MeterInterval(TEST_LABEL, VALID_RANGE);
        assertTrue("Same instance should be equal to itself", interval.equals(interval));
    }

    @Test
    public void testEquals_IdenticalIntervals_ReturnsTrue() {
        MeterInterval interval1 = new MeterInterval(TEST_LABEL, VALID_RANGE);
        MeterInterval interval2 = new MeterInterval(TEST_LABEL, VALID_RANGE);
        assertTrue("Identical intervals should be equal", interval1.equals(interval2));
    }

    @Test
    public void testEquals_DifferentLabels_ReturnsFalse() {
        MeterInterval interval1 = new MeterInterval("Label1", VALID_RANGE);
        MeterInterval interval2 = new MeterInterval("Label2", VALID_RANGE);
        assertFalse("Intervals with different labels should not be equal", interval1.equals(interval2));
    }

    @Test
    public void testEquals_DifferentRanges_ReturnsFalse() {
        Range range1 = new Range(0.0, 50.0);
        Range range2 = new Range(25.0, 75.0);
        MeterInterval interval1 = new MeterInterval(TEST_LABEL, range1);
        MeterInterval interval2 = new MeterInterval(TEST_LABEL, range2);
        assertFalse("Intervals with different ranges should not be equal", interval1.equals(interval2));
    }

    @Test
    public void testEquals_DifferentBackgroundPaints_ReturnsFalse() {
        MeterInterval interval1 = new MeterInterval(TEST_LABEL, VALID_RANGE, 
                                                   Color.RED, new BasicStroke(), Color.BLUE);
        MeterInterval interval2 = new MeterInterval(TEST_LABEL, VALID_RANGE, 
                                                   Color.RED, new BasicStroke(), Color.GREEN);
        assertFalse("Intervals with different background paints should not be equal", interval1.equals(interval2));
    }

    @Test
    public void testEquals_DifferentOutlinePaints_ReturnsFalse() {
        MeterInterval interval1 = new MeterInterval(TEST_LABEL, VALID_RANGE, 
                                                   Color.RED, new BasicStroke(), Color.BLUE);
        MeterInterval interval2 = new MeterInterval(TEST_LABEL, VALID_RANGE, 
                                                   Color.BLACK, new BasicStroke(), Color.BLUE);
        assertFalse("Intervals with different outline paints should not be equal", interval1.equals(interval2));
    }

    @Test
    public void testEquals_WithNonMeterIntervalObject_ReturnsFalse() {
        MeterInterval interval = new MeterInterval(TEST_LABEL, VALID_RANGE);
        assertFalse("MeterInterval should not equal a String", interval.equals(TEST_LABEL));
    }

    @Test
    public void testEquals_WithComplexPaints() {
        SystemColor systemColor = SystemColor.infoText;
        GradientPaint gradientPaint1 = new GradientPaint(0f, 0f, systemColor, 100f, 100f, systemColor, false);
        GradientPaint gradientPaint2 = new GradientPaint(0f, 0f, systemColor, 100f, 100f, Color.RED, false);
        BasicStroke stroke = new BasicStroke();
        
        MeterInterval interval1 = new MeterInterval(TEST_LABEL, VALID_RANGE, 
                                                   gradientPaint1, stroke, systemColor);
        MeterInterval interval2 = new MeterInterval(TEST_LABEL, VALID_RANGE, 
                                                   gradientPaint1, stroke, gradientPaint2);
        
        assertFalse("Intervals with different outline paints should not be equal", interval1.equals(interval2));
    }

    // ========== Edge Case Tests ==========

    @Test
    public void testWithNaNRange() {
        Range nanRange = new Range(Double.NaN, Double.NaN);
        MeterInterval interval1 = new MeterInterval(TEST_LABEL, nanRange);
        MeterInterval interval2 = new MeterInterval(TEST_LABEL, nanRange);
        
        // NaN ranges create special equality behavior
        Range retrievedRange = interval1.getRange();
        assertTrue("Upper bound should be NaN", Double.isNaN(retrievedRange.getUpperBound()));
        assertTrue("Lower bound should be NaN", Double.isNaN(retrievedRange.getLowerBound()));
        
        // Note: NaN equality is tricky - this documents the current behavior
        assertFalse("NaN ranges may cause equality issues", interval1.equals(interval2));
    }

    @Test
    public void testWithNegativeRange() {
        MeterInterval interval = new MeterInterval(TEST_LABEL, NEGATIVE_RANGE);
        Range retrievedRange = interval.getRange();
        
        assertEquals(-100.0, retrievedRange.getLowerBound(), 0.001);
        assertEquals(-50.0, retrievedRange.getUpperBound(), 0.001);
    }
}