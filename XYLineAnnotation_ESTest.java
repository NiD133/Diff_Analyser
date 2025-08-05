package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.time.chrono.ChronoLocalDate;
import javax.swing.text.DefaultCaret;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.CyclicNumberAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.PeriodAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedRangeXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.plot.WaferMapPlot;
import org.jfree.chart.plot.pie.PiePlot;
import org.jfree.chart.renderer.WaferMapRenderer;
import org.jfree.chart.renderer.xy.SamplingXYLineRenderer;
import org.jfree.data.time.Day;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class XYLineAnnotationTest extends XYLineAnnotation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testXYLineAnnotationHashCode() {
        XYLineAnnotation annotation = new XYLineAnnotation(-3139.81, -3139.81, -3139.81, -605.80);
        annotation.hashCode();
        assertEquals(-605.80, annotation.getY2(), 0.01);
        assertEquals(-3139.81, annotation.getX2(), 0.01);
        assertEquals(-3139.81, annotation.getX1(), 0.01);
        assertEquals(-3139.81, annotation.getY1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsDifferentStrokes() {
        CombinedRangeXYPlot<ChronoLocalDate> plot = new CombinedRangeXYPlot<>();
        XYLineAnnotation annotation1 = new XYLineAnnotation(10, 1.0, 1.0, 10, plot.DEFAULT_OUTLINE_STROKE, plot.DEFAULT_CROSSHAIR_PAINT);
        XYLineAnnotation annotation2 = new XYLineAnnotation(10, 1.0, 1.0, 1562.446, plot.DEFAULT_GRIDLINE_STROKE, plot.DEFAULT_CROSSHAIR_PAINT);
        boolean areEqual = annotation1.equals(annotation2);
        assertFalse(areEqual);
        assertEquals(1.0, annotation2.getY1(), 0.01);
        assertEquals(10.0, annotation2.getX1(), 0.01);
        assertEquals(1562.446, annotation2.getY2(), 0.01);
        assertEquals(1.0, annotation2.getX2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsDifferentCoordinates() {
        CombinedRangeXYPlot<ChronoLocalDate> plot = new CombinedRangeXYPlot<>();
        XYLineAnnotation annotation1 = new XYLineAnnotation(10, 1.0, 1.0, 10, plot.DEFAULT_OUTLINE_STROKE, plot.DEFAULT_CROSSHAIR_PAINT);
        Stroke stroke = plot.getDomainMinorGridlineStroke();
        XYLineAnnotation annotation2 = new XYLineAnnotation(10, 1.0, -14.36, 180.0, stroke, plot.DEFAULT_OUTLINE_PAINT);
        boolean areEqual = annotation1.equals(annotation2);
        assertFalse(areEqual);
        assertEquals(180.0, annotation2.getY2(), 0.01);
        assertEquals(10.0, annotation2.getX1(), 0.01);
        assertEquals(1.0, annotation2.getY1(), 0.01);
        assertEquals(-14.36, annotation2.getX2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsDifferentYCoordinates() {
        XYLineAnnotation annotation1 = new XYLineAnnotation(0.0, -7304.15, 1447.79, 1447.79);
        XYLineAnnotation annotation2 = new XYLineAnnotation(0.0, 2448.47, 0.0, 2448.47);
        boolean areEqual = annotation2.equals(annotation1);
        assertFalse(areEqual);
        assertEquals(-7304.15, annotation1.getY1(), 0.01);
        assertEquals(0.0, annotation1.getX1(), 0.01);
        assertEquals(1447.79, annotation1.getX2(), 0.01);
        assertEquals(1447.79, annotation1.getY2(), 0.01);
        assertEquals(2448.47, annotation2.getY2(), 0.01);
        assertEquals(0.0, annotation2.getX2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsDifferentXCoordinates() {
        CyclicNumberAxis axis = new CyclicNumberAxis(2327.16, -1.0);
        XYLineAnnotation annotation1 = new XYLineAnnotation(1.0, -222.60, 0.05, 500);
        SpiderWebPlot plot = new SpiderWebPlot();
        XYLineAnnotation annotation2 = new XYLineAnnotation(-1160.6, 1402.88, 1.0, 1.0, axis.DEFAULT_ADVANCE_LINE_STROKE, plot.DEFAULT_LABEL_BACKGROUND_PAINT);
        boolean areEqual = annotation2.equals(annotation1);
        assertFalse(areEqual);
        assertEquals(500.0, annotation1.getY2(), 0.01);
        assertEquals(1402.88, annotation2.getY1(), 0.01);
        assertEquals(0.05, annotation1.getX2(), 0.01);
        assertEquals(-1160.6, annotation2.getX1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationDrawNullGraphics() {
        CombinedRangeXYPlot<ChronoLocalDate> plot = new CombinedRangeXYPlot<>();
        DefaultCaret caret = new DefaultCaret();
        PlotOrientation orientation = PlotOrientation.HORIZONTAL;
        plot.setOrientation(orientation);
        DateAxis dateAxis = new DateAxis();
        XYLineAnnotation annotation = new XYLineAnnotation(10, 10.0, 5193.59, -1711.10, dateAxis.DEFAULT_AXIS_LINE_STROKE, dateAxis.DEFAULT_TICK_LABEL_PAINT);
        CyclicNumberAxis cyclicAxis = new CyclicNumberAxis(972.99, 138.72, null);
        try {
            annotation.draw(null, plot, caret, cyclicAxis, dateAxis, 0, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.annotations.XYLineAnnotation", e);
        }
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationDrawWithPolygonBounds() {
        CyclicNumberAxis axis = new CyclicNumberAxis(0.0, -1.0);
        CombinedRangeXYPlot<ChronoLocalDate> plot = new CombinedRangeXYPlot<>(axis);
        Stroke stroke = plot.getRangeGridlineStroke();
        XYLineAnnotation annotation = new XYLineAnnotation(-496.0, 1036.46, 1036.46, -1.0, stroke, axis.DEFAULT_TICK_MARK_PAINT);
        Polygon polygon = new Polygon();
        Rectangle rectangle = polygon.getBounds();
        Day day = new Day();
        PeriodAxis periodAxis = new PeriodAxis("", day, day);
        try {
            annotation.draw(null, plot, rectangle, periodAxis, axis, 0, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.annotations.XYLineAnnotation", e);
        }
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationGetY2() {
        SamplingXYLineRenderer renderer = new SamplingXYLineRenderer();
        XYLineAnnotation annotation = new XYLineAnnotation(2545.5, -909.0, -1423.67, renderer.ZERO, renderer.DEFAULT_OUTLINE_STROKE, renderer.DEFAULT_OUTLINE_PAINT);
        double y2 = annotation.getY2();
        assertEquals(-1423.67, annotation.getX2(), 0.01);
        assertEquals(-909.0, annotation.getY1(), 0.01);
        assertEquals(0.0, y2, 0.01);
        assertEquals(2545.5, annotation.getX1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationGetY2SameCoordinates() {
        XYLineAnnotation annotation = new XYLineAnnotation(-123.57, 1.0, 1.0, -123.57);
        double y2 = annotation.getY2();
        assertEquals(-123.57, y2, 0.01);
        assertEquals(-123.57, annotation.getX1(), 0.01);
        assertEquals(1.0, annotation.getX2(), 0.01);
        assertEquals(1.0, annotation.getY1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationGetY1() {
        SamplingXYLineRenderer renderer = new SamplingXYLineRenderer();
        XYLineAnnotation annotation = new XYLineAnnotation(renderer.ZERO, renderer.ZERO, -1423.74, renderer.ZERO, renderer.DEFAULT_OUTLINE_STROKE, renderer.DEFAULT_OUTLINE_PAINT);
        double y1 = annotation.getY1();
        assertEquals(0.0, annotation.getY2(), 0.01);
        assertEquals(0.0, annotation.getX1(), 0.01);
        assertEquals(0.0, y1, 0.01);
        assertEquals(-1423.74, annotation.getX2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationGetX2() {
        XYLineAnnotation annotation = new XYLineAnnotation(0.0, 2448.47, 0.0, 2448.47);
        double x2 = annotation.getX2();
        assertEquals(2448.47, annotation.getY1(), 0.01);
        assertEquals(0.0, x2, 0.01);
        assertEquals(2448.47, annotation.getY2(), 0.01);
        assertEquals(0.0, annotation.getX1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationGetX2SameCoordinates() {
        RingPlot plot = new RingPlot();
        Stroke stroke = plot.getLabelLinkStroke();
        XYLineAnnotation annotation = new XYLineAnnotation(-1758.77, -1758.77, -1758.77, -1758.77, stroke, plot.DEFAULT_LABEL_OUTLINE_PAINT);
        double x2 = annotation.getX2();
        assertEquals(-1758.77, x2, 0.01);
        assertEquals(-1758.77, annotation.getY1(), 0.01);
        assertEquals(-1758.77, annotation.getY2(), 0.01);
        assertEquals(-1758.77, annotation.getX1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationGetX1() {
        SamplingXYLineRenderer renderer = new SamplingXYLineRenderer();
        XYLineAnnotation annotation = new XYLineAnnotation(renderer.ZERO, renderer.ZERO, -1423.74, renderer.ZERO, renderer.DEFAULT_OUTLINE_STROKE, renderer.DEFAULT_OUTLINE_PAINT);
        double x1 = annotation.getX1();
        assertEquals(-1423.74, annotation.getX2(), 0.01);
        assertEquals(0.0, x1, 0.01);
        assertEquals(0.0, annotation.getY1(), 0.01);
        assertEquals(0.0, annotation.getY2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsWithPointerAnnotation() {
        XYPointerAnnotation pointerAnnotation = new XYPointerAnnotation("P'-0@M@(yUV", -1992.0, 0.0, 0.0);
        Stroke stroke = pointerAnnotation.getOutlineStroke();
        XYLineAnnotation annotation = new XYLineAnnotation(-1992.0, 1.0, 0.04, 1.0, stroke, pointerAnnotation.DEFAULT_PAINT);
        double x1 = annotation.getX1();
        assertEquals(1.0, annotation.getY1(), 0.01);
        assertEquals(1.0, annotation.getY2(), 0.01);
        assertEquals(-1992.0, x1, 0.01);
        assertEquals(0.04, annotation.getX2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationConstructorWithNullPaint() {
        BasicStroke stroke = (BasicStroke) CyclicNumberAxis.DEFAULT_ADVANCE_LINE_STROKE;
        try {
            new XYLineAnnotation(4572.49, 4572.49, 4572.49, 4572.49, stroke, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationConstructorWithInfiniteCoordinates() {
        try {
            new XYLineAnnotation(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsWithDifferentStrokeAndPaint() {
        XYLineAnnotation annotation1 = new XYLineAnnotation(1105.37, 1105.37, 1105.37, 1105.37);
        BasicStroke stroke = (BasicStroke) CategoryPlot.DEFAULT_GRIDLINE_STROKE;
        Color color = (Color) PiePlot.DEFAULT_LABEL_OUTLINE_PAINT;
        XYLineAnnotation annotation2 = new XYLineAnnotation(1105.37, 1105.37, 1105.37, 1105.37, stroke, color);
        boolean areEqual = annotation1.equals(annotation2);
        assertFalse(areEqual);
        assertEquals(1105.37, annotation2.getY2(), 0.01);
        assertEquals(1105.37, annotation2.getX1(), 0.01);
        assertEquals(1105.37, annotation2.getX2(), 0.01);
        assertEquals(1105.37, annotation2.getY1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsWithDifferentPaint() {
        XYLineAnnotation annotation1 = new XYLineAnnotation(-1433.66, -1433.66, -1433.66, -1433.66);
        BasicStroke stroke = (BasicStroke) WaferMapPlot.DEFAULT_CROSSHAIR_STROKE;
        WaferMapRenderer renderer = new WaferMapRenderer();
        Paint paint = renderer.getDefaultPaint();
        XYLineAnnotation annotation2 = new XYLineAnnotation(-1433.66, -1433.66, -1433.66, -1433.66, stroke, paint);
        boolean areEqual = annotation1.equals(annotation2);
        assertFalse(areEqual);
        assertEquals(-1433.66, annotation2.getY1(), 0.01);
        assertEquals(-1433.66, annotation2.getX1(), 0.01);
        assertEquals(-1433.66, annotation2.getY2(), 0.01);
        assertEquals(-1433.66, annotation2.getX2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsWithDifferentY2() {
        XYLineAnnotation annotation1 = new XYLineAnnotation(-1433.36, -1433.36, -1433.36, -1433.36);
        XYLineAnnotation annotation2 = new XYLineAnnotation(-1433.36, -1433.36, -1433.36, -2735.14);
        boolean areEqual = annotation1.equals(annotation2);
        assertFalse(areEqual);
        assertEquals(-1433.36, annotation1.getY2(), 0.01);
        assertEquals(-1433.36, annotation2.getX1(), 0.01);
        assertEquals(-1433.36, annotation2.getX2(), 0.01);
        assertEquals(-1433.36, annotation2.getY1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsWithDifferentX2() {
        XYLineAnnotation annotation1 = new XYLineAnnotation(-1433.61, -1433.61, -1433.61, -1433.61);
        CyclicNumberAxis axis = new CyclicNumberAxis(135.0);
        Stroke stroke = axis.getAdvanceLineStroke();
        XYLineAnnotation annotation2 = new XYLineAnnotation(-1433.61, -1433.61, 1505.07, 2995.80, stroke, axis.DEFAULT_TICK_LABEL_PAINT);
        boolean areEqual = annotation1.equals(annotation2);
        assertFalse(areEqual);
        assertEquals(2995.80, annotation2.getY2(), 0.01);
        assertEquals(-1433.61, annotation2.getX1(), 0.01);
        assertEquals(-1433.61, annotation2.getY1(), 0.01);
        assertEquals(1505.07, annotation2.getX2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsWithDifferentY1() {
        XYLineAnnotation annotation1 = new XYLineAnnotation(-1433.61, -680.09, -1433.61, -1433.61);
        XYLineAnnotation annotation2 = new XYLineAnnotation(-1433.61, -1433.61, -680.09, -680.09);
        boolean areEqual = annotation2.equals(annotation1);
        assertFalse(areEqual);
        assertEquals(-1433.61, annotation1.getX1(), 0.01);
        assertEquals(-1433.61, annotation2.getX1(), 0.01);
        assertEquals(-680.09, annotation1.getY1(), 0.01);
        assertEquals(-680.09, annotation2.getX2(), 0.01);
        assertEquals(-680.09, annotation2.getY2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsWithDifferentX1() {
        XYLineAnnotation annotation1 = new XYLineAnnotation(-53.47, -53.47, -53.47, -53.47);
        XYLineAnnotation annotation2 = new XYLineAnnotation(-38.86, -53.47, -53.47, -53.47);
        boolean areEqual = annotation2.equals(annotation1);
        assertFalse(areEqual);
        assertEquals(-53.47, annotation1.getX1(), 0.01);
        assertEquals(-53.47, annotation2.getY1(), 0.01);
        assertEquals(-53.47, annotation2.getX2(), 0.01);
        assertEquals(-53.47, annotation2.getY2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsWithDifferentObject() {
        XYPointerAnnotation pointerAnnotation = new XYPointerAnnotation("", -349.26, 2569.77, -349.26);
        Stroke stroke = pointerAnnotation.getOutlineStroke();
        XYLineAnnotation annotation = new XYLineAnnotation(4758.20, -1125.16, 2569.77, 1350.0, stroke, pointerAnnotation.DEFAULT_PAINT);
        boolean areEqual = annotation.equals(pointerAnnotation);
        assertFalse(areEqual);
        assertEquals(-1125.16, annotation.getY1(), 0.01);
        assertEquals(2569.77, annotation.getX2(), 0.01);
        assertEquals(1350.0, annotation.getY2(), 0.01);
        assertEquals(4758.20, annotation.getX1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsWithBufferedImage() {
        CombinedRangeXYPlot<ChronoLocalDate> plot = new CombinedRangeXYPlot<>();
        XYLineAnnotation annotation = new XYLineAnnotation(10, 1.0, 1.0, 10, plot.DEFAULT_OUTLINE_STROKE, plot.DEFAULT_CROSSHAIR_PAINT);
        PeriodAxis periodAxis = new PeriodAxis("rk<Dtq7=E");
        JFreeChart chart = new JFreeChart("org.jfree.chart.util.DefaultShadowenerator", periodAxis.DEFAULT_TICK_LABEL_FONT, plot, true);
        BufferedImage image = chart.createBufferedImage(10, 2718);
        boolean areEqual = annotation.equals(image);
        assertFalse(areEqual);
        assertEquals(1.0, annotation.getY1(), 0.01);
        assertEquals(1.0, annotation.getX2(), 0.01);
        assertEquals(10.0, annotation.getX1(), 0.01);
        assertEquals(10.0, annotation.getY2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationEqualsSameInstance() {
        XYLineAnnotation annotation = new XYLineAnnotation(-1433.61, -1433.61, -1433.61, -1433.61);
        boolean areEqual = annotation.equals(annotation);
        assertTrue(areEqual);
        assertEquals(-1433.61, annotation.getX2(), 0.01);
        assertEquals(-1433.61, annotation.getY2(), 0.01);
        assertEquals(-1433.61, annotation.getX1(), 0.01);
        assertEquals(-1433.61, annotation.getY1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationDrawWithGraphics() {
        CombinedRangeXYPlot<ChronoLocalDate> plot = new CombinedRangeXYPlot<>();
        XYLineAnnotation annotation = new XYLineAnnotation(10, 1.0, 1.0, 10, plot.DEFAULT_OUTLINE_STROKE, plot.DEFAULT_CROSSHAIR_PAINT);
        Rectangle rectangle = new Rectangle(10, 0, 10, 10);
        PeriodAxis periodAxis = new PeriodAxis("rk<Dtq7=E");
        annotation.setURL("rk<Dtq7=E");
        BufferedImage image = new BufferedImage(10, 10, 10);
        Graphics2D graphics = image.createGraphics();
        annotation.draw(graphics, plot, rectangle, periodAxis, periodAxis, 10, null);
        assertEquals(1.0, annotation.getY1(), 0.01);
        assertEquals(1.0, annotation.getX2(), 0.01);
        assertEquals(10.0, annotation.getX1(), 0.01);
        assertEquals(10.0, annotation.getY2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationDrawWithToolTip() {
        CombinedRangeXYPlot<ChronoLocalDate> plot = new CombinedRangeXYPlot<>();
        XYLineAnnotation annotation = new XYLineAnnotation(10, 1.0, 1.0, 10, plot.DEFAULT_OUTLINE_STROKE, plot.DEFAULT_CROSSHAIR_PAINT);
        Rectangle rectangle = new Rectangle(10, 10);
        PeriodAxis periodAxis = new PeriodAxis("rk<Dtq7=E");
        JFreeChart chart = new JFreeChart("org.jfree.chart.util.DefaultShadowenerator", periodAxis.DEFAULT_TICK_LABEL_FONT, plot, true);
        BufferedImage image = chart.createBufferedImage(10, 2718);
        annotation.setToolTipText("x2");
        Graphics2D graphics = image.createGraphics();
        annotation.draw(graphics, plot, rectangle, periodAxis, periodAxis, 10, null);
        assertEquals(10.0, annotation.getY2(), 0.01);
        assertEquals(10.0, annotation.getX1(), 0.01);
        assertEquals(1.0, annotation.getX2(), 0.01);
        assertEquals(1.0, annotation.getY1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationDrawWithResizedRange() {
        CombinedRangeXYPlot<ChronoLocalDate> plot = new CombinedRangeXYPlot<>();
        XYLineAnnotation annotation = new XYLineAnnotation(10, 1.0, 1.0, 10, plot.DEFAULT_OUTLINE_STROKE, plot.DEFAULT_CROSSHAIR_PAINT);
        Rectangle rectangle = new Rectangle(10, 0, 10, 10);
        PeriodAxis periodAxis = new PeriodAxis("rk<Dtq7=E");
        BufferedImage image = new BufferedImage(10, 10, 10);
        periodAxis.resizeRange(919.77);
        Graphics2D graphics = image.createGraphics();
        annotation.draw(graphics, plot, rectangle, periodAxis, periodAxis, 10, null);
        assertEquals(1.0, annotation.getY1(), 0.01);
        assertEquals(10.0, annotation.getY2(), 0.01);
        assertEquals(10.0, annotation.getX1(), 0.01);
        assertEquals(1.0, annotation.getX2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationGetY2WithDifferentCoordinates() {
        XYLineAnnotation annotation = new XYLineAnnotation(500, 1069.47, 0.25, 0.25);
        double y2 = annotation.getY2();
        assertEquals(0.25, y2, 0.01);
        assertEquals(0.25, annotation.getX2(), 0.01);
        assertEquals(1069.47, annotation.getY1(), 0.01);
        assertEquals(500.0, annotation.getX1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationGetX2WithDifferentCoordinates() {
        XYLineAnnotation annotation = new XYLineAnnotation(0.0, -7304.15, 1447.79, 1447.79);
        double x2 = annotation.getX2();
        assertEquals(1447.79, x2, 0.01);
        assertEquals(1447.79, annotation.getY2(), 0.01);
        assertEquals(0.0, annotation.getX1(), 0.01);
        assertEquals(-7304.15, annotation.getY1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationGetY1WithDifferentCoordinates() {
        XYLineAnnotation annotation = new XYLineAnnotation(500, 1069.47, 0.25, 0.25);
        double y1 = annotation.getY1();
        assertEquals(1069.47, y1, 0.01);
        assertEquals(500.0, annotation.getX1(), 0.01);
        assertEquals(0.25, annotation.getY2(), 0.01);
        assertEquals(0.25, annotation.getX2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationClone() {
        CombinedRangeXYPlot<ChronoLocalDate> plot = new CombinedRangeXYPlot<>();
        XYLineAnnotation annotation = new XYLineAnnotation(10, 1.0, 1.0, 10, plot.DEFAULT_OUTLINE_STROKE, plot.DEFAULT_CROSSHAIR_PAINT);
        XYLineAnnotation clonedAnnotation = (XYLineAnnotation) annotation.clone();
        boolean areEqual = annotation.equals(clonedAnnotation);
        assertTrue(areEqual);
        assertEquals(10.0, clonedAnnotation.getX1(), 0.01);
        assertEquals(10.0, clonedAnnotation.getY2(), 0.01);
        assertEquals(1.0, clonedAnnotation.getY1(), 0.01);
        assertEquals(1.0, clonedAnnotation.getX2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationGetX1WithDifferentCoordinates() {
        SamplingXYLineRenderer renderer = new SamplingXYLineRenderer();
        XYLineAnnotation annotation = new XYLineAnnotation(2545.5, -909.0, -1423.67, renderer.ZERO, renderer.DEFAULT_OUTLINE_STROKE, renderer.DEFAULT_OUTLINE_PAINT);
        double x1 = annotation.getX1();
        assertEquals(0.0, annotation.getY2(), 0.01);
        assertEquals(-1423.67, annotation.getX2(), 0.01);
        assertEquals(2545.5, x1, 0.01);
        assertEquals(-909.0, annotation.getY1(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationGetPaint() {
        XYLineAnnotation annotation = new XYLineAnnotation(500, 1069.47, 0.25, 0.25);
        annotation.getPaint();
        assertEquals(500.0, annotation.getX1(), 0.01);
        assertEquals(1069.47, annotation.getY1(), 0.01);
        assertEquals(0.25, annotation.getX2(), 0.01);
        assertEquals(0.25, annotation.getY2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXYLineAnnotationGetStroke() {
        XYLineAnnotation annotation = new XYLineAnnotation(500, 1069.47, 0.25, 0.25);
        annotation.getStroke();
        assertEquals(500.0, annotation.getX1(), 0.01);
        assertEquals(1069.47, annotation.getY1(), 0.01);
        assertEquals(0.25, annotation.getY2(), 0.01);
        assertEquals(0.25, annotation.getX2(), 0.01);
    }
}