package org.jfree.chart.plot;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.SystemColor;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.data.Range;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.HistogramType;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class MeterInterval_ESTest extends MeterInterval_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testRangeBoundsWithEmptyDataset() throws Throwable {
        DefaultBoxAndWhiskerCategoryDataset<HistogramType, HistogramType> dataset = new DefaultBoxAndWhiskerCategoryDataset<>();
        Range range = dataset.getRangeBounds(false);
        MeterInterval meterInterval = new MeterInterval("Test Interval", range);
        Range resultRange = meterInterval.getRange();
        assertEquals(Double.NaN, resultRange.getUpperBound(), 0.01);
    }

    @Test(timeout = 4000)
    public void testShiftedRange() throws Throwable {
        Range initialRange = new Range(-3041.91489, -3041.91489);
        Range shiftedRange = Range.shift(initialRange, 3388.0, true);
        MeterInterval meterInterval = new MeterInterval("Shifted Interval", shiftedRange);
        Range resultRange = meterInterval.getRange();
        assertNotSame(resultRange, initialRange);
    }

    @Test(timeout = 4000)
    public void testZeroRange() throws Throwable {
        Range zeroRange = new Range(0.0, 0.0);
        MeterInterval meterInterval = new MeterInterval("Zero Interval", zeroRange);
        Range resultRange = meterInterval.getRange();
        assertEquals(0.0, resultRange.getLowerBound(), 0.01);
    }

    @Test(timeout = 4000)
    public void testExpandRangeToInclude() throws Throwable {
        Range initialRange = new Range(-3041.91489, -3041.91489);
        Range expandedRange = Range.expandToInclude(initialRange, 3388.0);
        MeterInterval meterInterval = new MeterInterval("Expanded Interval", expandedRange);
        Range resultRange = meterInterval.getRange();
        assertNotSame(resultRange, initialRange);
    }

    @Test(timeout = 4000)
    public void testNullOutlineStroke() throws Throwable {
        Range range = new Range(-229.41607020985958, -229.41607020985958);
        Color color = Color.gray;
        MeterInterval meterInterval = new MeterInterval("Gray Interval", range, color, null, color);
        Stroke outlineStroke = meterInterval.getOutlineStroke();
        assertNull(outlineStroke);
    }

    @Test(timeout = 4000)
    public void testNullOutlinePaint() throws Throwable {
        Range range = Range.expandToInclude(null, -229.311906958652);
        BasicStroke stroke = new BasicStroke();
        MeterInterval meterInterval = new MeterInterval("No Paint Interval", range, null, stroke, null);
        Paint outlinePaint = meterInterval.getOutlinePaint();
        assertNull(outlinePaint);
    }

    @Test(timeout = 4000)
    public void testEmptyLabel() throws Throwable {
        Range range = Range.expandToInclude(null, -229.311906958652);
        MeterInterval meterInterval = new MeterInterval("", range);
        String label = meterInterval.getLabel();
        assertEquals("", label);
    }

    @Test(timeout = 4000)
    public void testBackgroundPaintColor() throws Throwable {
        DefaultBoxAndWhiskerCategoryDataset<HistogramType, HistogramType> dataset = new DefaultBoxAndWhiskerCategoryDataset<>();
        Range range = dataset.getRangeBounds(true);
        Color color = Color.darkGray;
        BasicStroke stroke = new BasicStroke(0.0F);
        MeterInterval meterInterval = new MeterInterval("Dark Gray Interval", range, color, stroke, color);
        Color backgroundColor = (Color) meterInterval.getBackgroundPaint();
        assertEquals(64, backgroundColor.getBlue());
    }

    @Test(timeout = 4000)
    public void testNullRangeThrowsException() throws Throwable {
        SystemColor color = SystemColor.controlHighlight;
        BasicStroke stroke = new BasicStroke();
        try {
            new MeterInterval("Invalid Interval", null, color, stroke, color);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullRangeThrowsExceptionWithLabel() throws Throwable {
        try {
            new MeterInterval("Invalid Interval", null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testDifferentBackgroundPaintsNotEqual() throws Throwable {
        Range range = new Range(-808.678, -808.678);
        SystemColor color = SystemColor.infoText;
        GradientPaint gradientPaint = new GradientPaint(657.688F, 657.688F, color, -89.9F, -89.9F, color, false);
        BasicStroke stroke = new BasicStroke();
        MeterInterval interval1 = new MeterInterval("Interval 1", range, gradientPaint, stroke, color);
        MeterInterval interval2 = new MeterInterval("Interval 1", range, gradientPaint, stroke, gradientPaint);
        assertFalse(interval1.equals(interval2));
    }

    @Test(timeout = 4000)
    public void testDifferentLabelsNotEqual() throws Throwable {
        Range range = new Range(-229.41607020985958, -229.41607020985958);
        MeterInterval interval1 = new MeterInterval("Label 1", range);
        MeterInterval interval2 = new MeterInterval("Label 2", range);
        assertFalse(interval1.equals(interval2));
    }

    @Test(timeout = 4000)
    public void testSameLabelAndRangeEqual() throws Throwable {
        Range range = new Range(-229.41607020985958, -229.41607020985958);
        MeterInterval interval1 = new MeterInterval("Same Label", range);
        MeterInterval interval2 = new MeterInterval("Same Label", range);
        assertTrue(interval1.equals(interval2));
    }

    @Test(timeout = 4000)
    public void testSameInstanceEqual() throws Throwable {
        Range range = new Range(-229.41607020985958, -229.41607020985958);
        MeterInterval interval = new MeterInterval("Self Interval", range);
        assertTrue(interval.equals(interval));
    }

    @Test(timeout = 4000)
    public void testDifferentObjectNotEqual() throws Throwable {
        Range range = new Range(-229.41607020985958, -229.41607020985958);
        MeterInterval interval = new MeterInterval("Interval", range);
        assertFalse(interval.equals("Some String"));
    }

    @Test(timeout = 4000)
    public void testOutlineStrokeDefaultsToBasicStroke() throws Throwable {
        Range range = new Range(-229.41607020985958, -229.41607020985958);
        MeterInterval interval = new MeterInterval("Stroke Interval", range);
        BasicStroke stroke = (BasicStroke) interval.getOutlineStroke();
        assertEquals(0, stroke.getLineJoin());
    }

    @Test(timeout = 4000)
    public void testRangeIsSameAsProvided() throws Throwable {
        Range range = Range.expandToInclude(null, -229.311906958652);
        MeterInterval interval = new MeterInterval("Range Interval", range);
        Range resultRange = interval.getRange();
        assertSame(resultRange, range);
    }

    @Test(timeout = 4000)
    public void testBackgroundPaintIsNull() throws Throwable {
        Range range = new Range(-229.41607020985958, -229.41607020985958);
        MeterInterval interval = new MeterInterval("No Background", range);
        Paint backgroundPaint = interval.getBackgroundPaint();
        assertNull(backgroundPaint);
    }

    @Test(timeout = 4000)
    public void testLabelRetrieval() throws Throwable {
        Range range = new Range(-808.678, -808.678);
        SystemColor color = SystemColor.infoText;
        GradientPaint gradientPaint = new GradientPaint(657.688F, 657.688F, color, -89.9F, -89.9F, color, false);
        BasicStroke stroke = new BasicStroke();
        MeterInterval interval = new MeterInterval("Label Test", range, gradientPaint, stroke, color);
        String label = interval.getLabel();
        assertEquals("Label Test", label);
    }
}