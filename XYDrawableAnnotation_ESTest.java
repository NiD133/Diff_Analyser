package org.jfree.chart.annotations;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.Drawable;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

/**
 * Readable tests for XYDrawableAnnotation focusing on:
 * - constructor validation
 * - getters
 * - equals/hashCode
 * - clone
 * - basic draw behavior
 */
public class XYDrawableAnnotationTest {

    private static Graphics2D newGraphics() {
        return new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).createGraphics();
    }

    private static Rectangle2D dataArea() {
        return new Rectangle2D.Double(0, 0, 100, 80);
    }

    private static XYDrawableAnnotation newAnnotation(double x, double y,
                                                      double width, double height,
                                                      double scale, Drawable drawable) {
        return new XYDrawableAnnotation(x, y, width, height, scale, drawable);
    }

    @Test
    public void constructorRejectsNullDrawable() {
        try {
            new XYDrawableAnnotation(1.0, 2.0, 10.0, 20.0, null);
            fail("Expected IllegalArgumentException for null drawable");
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().contains("Null 'drawable' argument"));
        }
    }

    @Test
    public void gettersReturnConstructorValues() {
        TextTitle drawable = new TextTitle("Title");
        XYDrawableAnnotation a = new XYDrawableAnnotation(1.5, -2.5, 10.0, 20.0, 3.0, drawable);

        assertEquals(1.5, a.getX(), 0.0001);
        assertEquals(-2.5, a.getY(), 0.0001);
        assertEquals(10.0, a.getDisplayWidth(), 0.0001);
        assertEquals(20.0, a.getDisplayHeight(), 0.0001);
        assertEquals(3.0, a.getDrawScaleFactor(), 0.0001);
    }

    @Test
    public void defaultScaleFactorIsOneForFiveArgConstructor() {
        TextTitle drawable = new TextTitle("Title");
        XYDrawableAnnotation a = new XYDrawableAnnotation(0.0, 0.0, 5.0, 6.0, drawable);

        assertEquals(1.0, a.getDrawScaleFactor(), 0.0001);
    }

    @Test
    public void equalsAndHashCode_whenAllFieldsMatch() {
        TextTitle drawable = new TextTitle("Same instance for equality");
        XYDrawableAnnotation a1 = newAnnotation(1, 2, 3, 4, 5, drawable);
        XYDrawableAnnotation a2 = newAnnotation(1, 2, 3, 4, 5, drawable);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    public void equals_isReflexiveAndNotEqualToDifferentType() {
        TextTitle drawable = new TextTitle("T");
        XYDrawableAnnotation a = newAnnotation(1, 2, 3, 4, 5, drawable);

        assertEquals(a, a);             // reflexive
        assertNotEquals(a, "not an annotation");
    }

    @Test
    public void notEqual_whenAnyFieldDiffers() {
        TextTitle drawable = new TextTitle("T");
        XYDrawableAnnotation base = newAnnotation(1, 2, 3, 4, 5, drawable);

        assertNotEquals(base, newAnnotation(9, 2, 3, 4, 5, drawable)); // x differs
        assertNotEquals(base, newAnnotation(1, 9, 3, 4, 5, drawable)); // y differs
        assertNotEquals(base, newAnnotation(1, 2, 9, 4, 5, drawable)); // width differs
        assertNotEquals(base, newAnnotation(1, 2, 3, 9, 5, drawable)); // height differs
        assertNotEquals(base, newAnnotation(1, 2, 3, 4, 9, drawable)); // scale differs
    }

    @Test
    public void cloneProducesEqualButDistinctObject() throws Exception {
        TextTitle drawable = new TextTitle("T");
        XYDrawableAnnotation original = newAnnotation(1, 2, 3, 4, 5, drawable);

        XYDrawableAnnotation copy = (XYDrawableAnnotation) original.clone();

        assertNotSame(original, copy);
        assertEquals(original, copy);
        assertEquals(original.hashCode(), copy.hashCode());
    }

    @Test
    public void draw_smokeTest_doesNotThrowWithValidInputs() {
        Graphics2D g2 = newGraphics();
        try {
            XYPlot plot = new XYPlot();
            Rectangle2D area = dataArea();
            ValueAxis domain = new NumberAxis("X");
            ValueAxis range = new NumberAxis("Y");
            PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());
            TextTitle drawable = new TextTitle("Hello");

            XYDrawableAnnotation a = newAnnotation(5, 6, 40, 30, 1.0, drawable);
            a.draw(g2, plot, area, domain, range, 0, info); // should not throw
        } finally {
            g2.dispose();
        }
    }

    @Test
    public void draw_throwsWhenRangeAxisIsNull() {
        Graphics2D g2 = newGraphics();
        try {
            XYPlot plot = new XYPlot();
            Rectangle2D area = dataArea();
            ValueAxis domain = new NumberAxis("X");
            ValueAxis range = null;
            PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());
            TextTitle drawable = new TextTitle("Hello");

            XYDrawableAnnotation a = newAnnotation(5, 6, 40, 30, 1.0, drawable);

            try {
                a.draw(g2, plot, area, domain, range, 0, info);
                fail("Expected NullPointerException when range axis is null");
            } catch (NullPointerException expected) {
                // expected
            }
        } finally {
            g2.dispose();
        }
    }

    @Test
    public void draw_withUrlAndTooltip_doesNotThrow() {
        Graphics2D g2 = newGraphics();
        try {
            XYPlot plot = new XYPlot();
            Rectangle2D area = dataArea();
            ValueAxis domain = new NumberAxis("X");
            ValueAxis range = new NumberAxis("Y");
            PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());
            TextTitle drawable = new TextTitle("Clickable");

            XYDrawableAnnotation a = newAnnotation(5, 6, 40, 30, 1.0, drawable);
            a.setURL("https://example.com");
            a.setToolTipText("tooltip");

            a.draw(g2, plot, area, domain, range, 0, info); // should not throw
        } finally {
            g2.dispose();
        }
    }
}