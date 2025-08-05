package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.DropMode;
import javax.swing.JScrollPane;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.util.MockGregorianCalendar;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ModuloAxis;
import org.jfree.chart.axis.TickType;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.legend.PaintScaleLegend;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.jfree.data.time.DateRange;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeries;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ModuloAxisTest extends ModuloAxis_ESTest_scaffolding {

    private static final double TOLERANCE = 0.01;
    private static final double DEFAULT_DISPLAY_START = 270.0;
    private static final double DEFAULT_DISPLAY_END = 90.0;

    @Test(timeout = 4000)
    public void testResizeRangeAndEquality() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis1 = new ModuloAxis("", defaultDateRange);
        axis1.resizeRange(1005.89236);
        ModuloAxis axis2 = new ModuloAxis("", axis1.DEFAULT_RANGE);
        axis1.equals(axis2);
        assertEquals(0.8662200000107987, axis1.getDisplayStart(), TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testLengthToJava2DWithNullRectangle() throws Throwable {
        DropMode dropMode = DropMode.ON_OR_INSERT;
        TimeSeries<DropMode> timeSeries = new TimeSeries<>(dropMode);
        TimePeriodAnchor anchor = TimePeriodAnchor.MIDDLE;
        TimeZone timeZone = TimeZone.getDefault();
        MockGregorianCalendar calendar = new MockGregorianCalendar(timeZone);
        Range range = timeSeries.findValueRange(null, anchor, calendar);
        ModuloAxis axis = new ModuloAxis("", range);
        RectangleEdge edge = RectangleEdge.BOTTOM;

        try {
            axis.lengthToJava2D(66.0, null, edge);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.axis.ModuloAxis", e);
        }
    }

    @Test(timeout = 4000)
    public void testResizeRangeAndLengthToJava2D() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        axis.resizeRange(1005.89236);
        Rectangle2D.Double rectangle = new Rectangle2D.Double();
        RectangleEdge edge = RectangleEdge.LEFT;
        axis.resizeRange(2.0, -5.439710113);
        double result = axis.lengthToJava2D(0.13377999998920131, rectangle, edge);
        assertFalse(axis.isAutoRange());
        assertEquals(0.0, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testLengthToJava2DWithNullRectangleInPaintScaleLegend() throws Throwable {
        MeterPlot meterPlot = new MeterPlot();
        Range range = meterPlot.getRange();
        ModuloAxis axis = new ModuloAxis("", range);
        axis.resizeRange(270.0);
        XYShapeRenderer renderer = new XYShapeRenderer();
        PaintScale paintScale = renderer.getPaintScale();
        PaintScaleLegend legend = new PaintScaleLegend(paintScale, axis);

        try {
            axis.lengthToJava2D(270.0, null, legend.DEFAULT_POSITION);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.axis.ModuloAxis", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetDisplayRangeAndResizeRange() throws Throwable {
        DefaultValueDataset dataset = new DefaultValueDataset();
        ThermometerPlot plot = new ThermometerPlot(dataset);
        Range range = plot.getDataRange(null);
        ModuloAxis axis = new ModuloAxis("", range);
        axis.setDisplayRange(0.0, 10);
        axis.resizeRange(1462.0995585, -2273.979600655645);
        assertEquals(36.5181918443559, axis.getDisplayEnd(), TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testJava2DToValue() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        axis.resizeRange(500.0);
        XYShapeRenderer renderer = new XYShapeRenderer();
        PaintScale paintScale = renderer.getPaintScale();
        PaintScaleLegend legend = new PaintScaleLegend(paintScale, axis);
        Rectangle2D bounds = legend.getBounds();
        double result = axis.java2DToValue(0.5, bounds, legend.DEFAULT_POSITION);
        assertEquals(0.5, axis.getDisplayEnd(), TOLERANCE);
        assertEquals(0.0, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2D() throws Throwable {
        DropMode dropMode = DropMode.ON_OR_INSERT;
        TimeSeries<DropMode> timeSeries = new TimeSeries<>(dropMode);
        TimePeriodAnchor anchor = TimePeriodAnchor.MIDDLE;
        TimeZone timeZone = TimeZone.getDefault();
        MockGregorianCalendar calendar = new MockGregorianCalendar(timeZone);
        Range range = timeSeries.findValueRange(null, anchor, calendar);
        ModuloAxis axis = new ModuloAxis("", range);
        Rectangle2D.Double rectangle = new Rectangle2D.Double();
        RectangleEdge edge = RectangleEdge.LEFT;
        double result = axis.valueToJava2D(19, rectangle, edge);
        assertEquals(Double.NaN, result, TOLERANCE);
        assertEquals(DEFAULT_DISPLAY_START, axis.getDisplayStart(), TOLERANCE);
        assertEquals(DEFAULT_DISPLAY_END, axis.getDisplayEnd(), TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithInvertedAxis() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("Plc=vQ!l", defaultDateRange);
        axis.setInverted(true);
        Rectangle2D.Double rectangle = new Rectangle2D.Double(0.13377999998920131, 500, 1.0E-8, 0.13377999998920131);
        RectangleEdge edge = RectangleEdge.RIGHT;
        double result = axis.valueToJava2D(0.05, rectangle, edge);
        assertEquals(499.9999626312849, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithInvertedAxisAndCustomRange() throws Throwable {
        Range range = new Range(0.043157868280104594, 946.7277932617);
        ModuloAxis axis = new ModuloAxis("", range);
        axis.setInverted(true);
        Rectangle2D.Double rectangle = new Rectangle2D.Double();
        RectangleEdge edge = RectangleEdge.TOP;
        double result = axis.valueToJava2D(2.0, rectangle, edge);
        assertEquals(0.0, result, TOLERANCE);
        assertEquals(DEFAULT_DISPLAY_START, axis.getDisplayStart(), TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithNegativeValue() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        axis.setInverted(true);
        axis.resizeRange(499.3112711);
        Rectangle2D.Double rectangle = new Rectangle2D.Double();
        RectangleEdge edge = RectangleEdge.BOTTOM;
        double result = axis.valueToJava2D(-2588.490989243, rectangle, edge);
        assertFalse(axis.isAutoRange());
        assertEquals(0.0, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithNegativeValueAndInvertedAxis() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        axis.resizeRange(2.0);
        axis.setInverted(true);
        Rectangle2D.Double rectangle = new Rectangle2D.Double(0.0F, 500, 2.0F, 0.05);
        RectangleEdge edge = RectangleEdge.LEFT;
        double result = axis.valueToJava2D(-4.0, rectangle, edge);
        assertFalse(axis.isAutoRange());
        assertEquals(500.05, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithLargeValue() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        axis.resizeRange(1005.89236);
        Rectangle2D.Double rectangle = new Rectangle2D.Double();
        Rectangle2D outsetRectangle = axis.DEFAULT_TICK_LABEL_INSETS.createOutsetRectangle(rectangle, true, true);
        RectangleEdge edge = RectangleEdge.LEFT;
        double result = axis.valueToJava2D(3676.6, outsetRectangle, edge);
        assertFalse(axis.isAutoRange());
        assertEquals(5.979967110664955, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithNegativeRectangle() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        axis.resizeRange(499.3112711);
        Rectangle2D.Double rectangle = new Rectangle2D.Double(-2588.490989243, -1638.3, 0.05, 1138.013594771);
        RectangleEdge edge = RectangleEdge.BOTTOM;
        double result = axis.valueToJava2D(-2588.490989243, rectangle, edge);
        assertFalse(axis.isAutoRange());
        assertEquals(-2588.552898269559, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithInvertedAxisAndCustomRange() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        axis.setInverted(true);
        axis.resizeRange(2.0);
        axis.resizeRange(2859.56, 2187.2);
        Rectangle2D.Double rectangle = new Rectangle2D.Double(4454.370088683, 4.0, -3835.51370066, 0.0F);
        RectangleEdge edge = RectangleEdge.TOP;
        double result = axis.valueToJava2D(2.0, rectangle, edge);
        assertFalse(axis.isAutoRange());
        assertEquals(-2257.7788874723888, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithSmallRange() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("Plc=vQ!l", defaultDateRange);
        axis.resizeRange(1.1565116986768456);
        Rectangle2D.Double rectangle = new Rectangle2D.Double(0.13377999998920131, 500, 1.0E-8, 0.13377999998920131);
        RectangleEdge edge = RectangleEdge.RIGHT;
        double result = axis.valueToJava2D(0.05, rectangle, edge);
        assertFalse(axis.isAutoRange());
        assertEquals(500.12804464945566, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithPolygon() throws Throwable {
        DefaultValueDataset dataset = new DefaultValueDataset();
        ThermometerPlot plot = new ThermometerPlot(dataset);
        Range range = plot.getDataRange(null);
        ModuloAxis axis = new ModuloAxis("", range);
        Polygon polygon = new Polygon();
        Rectangle rectangle = (Rectangle) polygon.getBounds2D();
        axis.setDisplayRange(0.0, 10);
        rectangle.setBounds(2, 97, 10, 2);
        RectangleEdge edge = RectangleEdge.BOTTOM;
        double result = axis.valueToJava2D(2239.4471173664856, rectangle, edge);
        assertEquals(10.0, axis.getDisplayEnd(), TOLERANCE);
        assertEquals(41.4471173664856, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithPaintScaleLegend() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        LookupPaintScale paintScale = new LookupPaintScale();
        PaintScaleLegend legend = new PaintScaleLegend(paintScale, axis);
        Rectangle2D bounds = legend.getBounds();
        axis.setDisplayRange(500, 2.0F);
        double result = axis.valueToJava2D(75.0, bounds, legend.DEFAULT_POSITION);
        assertEquals(0.0, axis.getDisplayStart(), TOLERANCE);
        assertEquals(Double.NaN, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithNullRectangle() throws Throwable {
        DropMode dropMode = DropMode.ON_OR_INSERT;
        TimeSeries<DropMode> timeSeries = new TimeSeries<>(dropMode);
        TimePeriodAnchor anchor = TimePeriodAnchor.MIDDLE;
        TimeZone timeZone = TimeZone.getDefault();
        MockGregorianCalendar calendar = new MockGregorianCalendar(timeZone);
        Range range = timeSeries.findValueRange(null, anchor, calendar);
        ModuloAxis axis = new ModuloAxis("", range);
        RectangleEdge edge = RectangleEdge.BOTTOM;

        try {
            axis.valueToJava2D(534.359951174859, null, edge);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.axis.ModuloAxis", e);
        }
    }

    @Test(timeout = 4000)
    public void testAutoAdjustRange() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        axis.autoAdjustRange();
        assertEquals(DEFAULT_DISPLAY_START, axis.getDisplayStart(), TOLERANCE);
        assertEquals(DEFAULT_DISPLAY_END, axis.getDisplayEnd(), TOLERANCE);
        assertTrue(axis.isAutoRange());
    }

    @Test(timeout = 4000)
    public void testSetDisplayRangeWithNegativeValues() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        Rectangle2D.Double rectangle = new Rectangle2D.Double();
        Rectangle2D outsetRectangle = axis.DEFAULT_TICK_LABEL_INSETS.createOutsetRectangle(rectangle, true, true);
        axis.setDisplayRange(-3112.0, -2578.194629652568);
        RectangleEdge edge = RectangleEdge.TOP;
        double result = axis.valueToJava2D(0.0, outsetRectangle, edge);
        assertEquals(1.0, axis.getDisplayStart(), TOLERANCE);
        assertEquals(-4.0, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testLengthToJava2DWithLargeNegativeValue() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        axis.resizeRange(1005.89236);
        Rectangle rectangle = new Rectangle(500, 1);
        RectangleEdge edge = RectangleEdge.BOTTOM;
        double result = axis.lengthToJava2D(-2.147483648E9, rectangle, edge);
        assertFalse(axis.isAutoRange());
        assertEquals(-4.013087995539962E12, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testJava2DToValueWithNegativeValueAndInvertedAxis() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("Plc=vQ!l", defaultDateRange);
        axis.setInverted(true);
        axis.resizeRange(1.1565116986768456);
        Rectangle rectangle = new Rectangle(500, 500);
        RectangleEdge edge = RectangleEdge.RIGHT;
        double result = axis.java2DToValue(-75.7781029047, rectangle, edge);
        assertEquals(0.0077970315776809684, axis.getDisplayStart(), TOLERANCE);
        assertEquals(-0.14139579718674034, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testGetDisplayStartAfterResize() throws Throwable {
        Range range = new Range(-3022.7454, -2241.3964950581735);
        ModuloAxis axis = new ModuloAxis("lFFFf+.F<AAFi", range);
        axis.resizeRange(3737.437, 2.0);
        double result = axis.getDisplayStart();
        assertFalse(axis.isAutoRange());
        assertEquals(-2514.145028011561, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testSetDisplayRangeWithLargeValues() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        axis.setDisplayRange(533.2898185733466, 500);
        double result = axis.getDisplayEnd();
        assertEquals(0.28981857334656524, axis.getDisplayStart(), TOLERANCE);
        assertEquals(0.0, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testGetDisplayEndAfterResize() throws Throwable {
        Range range = new Range(-3022.7454, -2241.3964950581735);
        ModuloAxis axis = new ModuloAxis("lFFFf+.F<AAFi", range);
        axis.resizeRange(3737.437, 2.0);
        double result = axis.getDisplayEnd();
        assertEquals(-2169.9484016396004, axis.getUpperBound(), TOLERANCE);
        assertEquals(-2951.2973065814267, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testSetDisplayRangeWithNullRange() throws Throwable {
        ModuloAxis axis = new ModuloAxis("", null);

        try {
            axis.setDisplayRange(-1402.04, -1402.04);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testResizeRangeWithNullRange() throws Throwable {
        ModuloAxis axis = new ModuloAxis(null, null);

        try {
            axis.resizeRange(142.0, 142.0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.axis.ModuloAxis", e);
        }
    }

    @Test(timeout = 4000)
    public void testResizeRangeWithZeroRange() throws Throwable {
        ModuloAxis axis = new ModuloAxis("HL/Z&k~q6y%", null);

        try {
            axis.resizeRange(0.0, 0.0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testResizeRangeWithSmallValue() throws Throwable {
        ModuloAxis axis = new ModuloAxis("jT(Ea9bn5b*o>jy", null);

        try {
            axis.resizeRange(1.0E-8);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.axis.ModuloAxis", e);
        }
    }

    @Test(timeout = 4000)
    public void testResizeRangeWithNegativeValue() throws Throwable {
        DefaultMultiValueCategoryDataset<DropMode, DropMode> dataset = new DefaultMultiValueCategoryDataset<>();
        Range range = dataset.getRangeBounds(false);
        ModuloAxis axis = new ModuloAxis(null, range);

        try {
            axis.resizeRange(-1471.70084);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.axis.ValueAxis", e);
        }
    }

    @Test(timeout = 4000)
    public void testJava2DToValueWithNullRectangle() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("scXVbH@CIro/NE^!ZZ[", defaultDateRange);
        axis.resizeRange(0.6155460372287962, 32.0);
        RectangleEdge edge = RectangleEdge.BOTTOM;

        try {
            axis.java2DToValue(0.6155460372287962, null, edge);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.axis.NumberAxis", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloneAndEquals() throws Throwable {
        ModuloAxis axis = new ModuloAxis("", null);
        Object clonedObject = axis.clone();

        try {
            axis.equals(clonedObject);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.axis.ModuloAxis", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetDisplayStart() throws Throwable {
        DefaultValueDataset dataset = new DefaultValueDataset();
        ThermometerPlot plot = new ThermometerPlot(dataset);
        Range range = plot.getDataRange(null);
        ModuloAxis axis = new ModuloAxis("", range);
        axis.setDisplayRange(0.0, 10);
        double result = axis.getDisplayStart();
        assertEquals(0.0, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentRanges() throws Throwable {
        ModuloAxis axis1 = new ModuloAxis("", null);
        ModuloAxis axis2 = new ModuloAxis("", axis1.DEFAULT_RANGE);
        boolean result = axis2.equals(axis1);
        assertFalse(result);
        assertEquals(DEFAULT_DISPLAY_END, axis2.getDisplayEnd(), TOLERANCE);
        assertEquals(DEFAULT_DISPLAY_START, axis2.getDisplayStart(), TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentLabels() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis1 = new ModuloAxis("TOKP_OR_LEFT", defaultDateRange);
        ModuloAxis axis2 = new ModuloAxis("TOKP_OR_LEFT", axis1.DEFAULT_RANGE);
        axis2.resizeRange(2.0);
        boolean result = axis1.equals(axis2);
        assertFalse(axis2.isAutoRange());
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentTypes() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        TickType tickType = TickType.MINOR;
        boolean result = axis.equals(tickType);
        assertEquals(DEFAULT_DISPLAY_START, axis.getDisplayStart(), TOLERANCE);
        assertFalse(result);
        assertEquals(DEFAULT_DISPLAY_END, axis.getDisplayEnd(), TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameObject() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        boolean result = axis.equals(axis);
        assertTrue(result);
        assertEquals(DEFAULT_DISPLAY_END, axis.getDisplayEnd(), TOLERANCE);
        assertEquals(DEFAULT_DISPLAY_START, axis.getDisplayStart(), TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testCloneAndEqualsWithSameObject() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis1 = new ModuloAxis("", defaultDateRange);
        ModuloAxis axis2 = (ModuloAxis) axis1.clone();
        boolean result = axis1.equals(axis2);
        assertEquals(DEFAULT_DISPLAY_END, axis2.getDisplayEnd(), TOLERANCE);
        assertTrue(result);
        assertEquals(DEFAULT_DISPLAY_START, axis2.getDisplayStart(), TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testLengthToJava2DWithSmallValue() throws Throwable {
        Range range = ValueAxis.DEFAULT_RANGE;
        ModuloAxis axis = new ModuloAxis("", range);
        JScrollPane scrollPane = new JScrollPane();
        Rectangle rectangle = scrollPane.getViewportBorderBounds();
        RectangleEdge edge = RectangleEdge.RIGHT;
        double result = axis.lengthToJava2D(1.0E-8, rectangle, edge);
        assertEquals(1.675977653631285E-10, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testResizeRangeWithSmallValues() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        axis.resizeRange(1005.89236, 0.13377999998920131);
        axis.resizeRange(0.8662200000107987, 0.13377999998920131);
        assertFalse(axis.isAutoRange());
    }

    @Test(timeout = 4000)
    public void testJava2DToValueWithNullRectangleAndTopEdge() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        RectangleEdge edge = RectangleEdge.TOP;
        double result = axis.java2DToValue(5.0, null, edge);
        assertEquals(DEFAULT_DISPLAY_START, axis.getDisplayStart(), TOLERANCE);
        assertEquals(0.0, result, TOLERANCE);
        assertEquals(DEFAULT_DISPLAY_END, axis.getDisplayEnd(), TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithNullEdge() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        Rectangle2D.Double rectangle = new Rectangle2D.Double();
        double result = axis.valueToJava2D(53843.88561595805, rectangle, null);
        assertEquals(DEFAULT_DISPLAY_START, axis.getDisplayStart(), TOLERANCE);
        assertEquals(0.0, result, TOLERANCE);
        assertEquals(DEFAULT_DISPLAY_END, axis.getDisplayEnd(), TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithNullEdgeAndSmallValue() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        axis.resizeRange(2.0);
        Rectangle2D.Double rectangle = new Rectangle2D.Double();
        double result = axis.valueToJava2D(0.7330000000001746, rectangle, null);
        assertFalse(axis.isAutoRange());
        assertEquals(0.0, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testValueToJava2DWithNullRectangleAndEdge() throws Throwable {
        Range range = ValueAxis.DEFAULT_RANGE;
        ModuloAxis axis = new ModuloAxis("horizontalAlignment", range);
        axis.resizeRange(82.21630182402, 0.05);
        double result = axis.valueToJava2D(90.57956035375857, null, null);
        assertEquals(0.6909867502108682, axis.getDisplayEnd(), TOLERANCE);
        assertEquals(0.0, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testJava2DToValueWithPositiveInfinity() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        axis.resizeRange(1005.89236, 0.13377999998920131);
        Line2D.Float line = new Line2D.Float();
        Rectangle2D rectangle = line.getBounds2D();
        RectangleEdge edge = RectangleEdge.RIGHT;
        double result = axis.java2DToValue(0.8662200000107987, rectangle, edge);
        assertEquals(0.7675599999784026, axis.getUpperBound(), TOLERANCE);
        assertEquals(Double.POSITIVE_INFINITY, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testAutoAdjustRangeWithNullRange() throws Throwable {
        ModuloAxis axis = new ModuloAxis(null, null);

        try {
            axis.autoAdjustRange();
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetDisplayEnd() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        double result = axis.getDisplayEnd();
        assertEquals(DEFAULT_DISPLAY_START, axis.getDisplayStart(), TOLERANCE);
        assertEquals(DEFAULT_DISPLAY_END, result, TOLERANCE);
    }

    @Test(timeout = 4000)
    public void testGetDisplayStart() throws Throwable {
        DateRange defaultDateRange = DateAxis.DEFAULT_DATE_RANGE;
        ModuloAxis axis = new ModuloAxis("", defaultDateRange);
        double result = axis.getDisplayStart();
        assertEquals(DEFAULT_DISPLAY_END, axis.getDisplayEnd(), TOLERANCE);
        assertEquals(DEFAULT_DISPLAY_START, result, TOLERANCE);
    }
}