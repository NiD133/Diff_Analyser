package org.jfree.chart.annotations;

import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.junit.Test;

/**
 * Readable and focused tests for XYLineAnnotation.
 *
 * These tests aim to:
 * - verify constructor argument validation,
 * - check getters,
 * - exercise equals/hashCode,
 * - verify clone behavior,
 * - cover draw() happy-path and null-argument error behavior.
 */
public class XYLineAnnotationTest {

    // Common, easy-to-read coordinates used throughout the tests
    private static final double X1 = 1.0;
    private static final double Y1 = 2.0;
    private static final double X2 = 3.0;
    private static final double Y2 = 4.0;

    // Helpers
    private static Graphics2D newGraphics() {
        BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        return img.createGraphics();
    }

    private static XYPlot newXYPlot() {
        ValueAxis domain = new NumberAxis("X");
        ValueAxis range = new NumberAxis("Y");
        return new XYPlot(null, domain, range, null);
    }

    // ---------------------------------------------------------------------
    // Constructor validation
    // ---------------------------------------------------------------------

    @Test
    public void constructorWithDefaults_populatesFields() {
        XYLineAnnotation a = new XYLineAnnotation(X1, Y1, X2, Y2);

        assertEquals(X1, a.getX1(), 0.0000001);
        assertEquals(Y1, a.getY1(), 0.0000001);
        assertEquals(X2, a.getX2(), 0.0000001);
        assertEquals(Y2, a.getY2(), 0.0000001);

        assertNotNull("Default stroke should not be null", a.getStroke());
        assertNotNull("Default paint should not be null", a.getPaint());
    }

    @Test
    public void constructor_withStrokeAndPaint_setsFields() {
        BasicStroke stroke = new BasicStroke(2f);
        Color paint = Color.BLUE;

        XYLineAnnotation a = new XYLineAnnotation(X1, Y1, X2, Y2, stroke, paint);

        assertSame(stroke, a.getStroke());
        assertSame(paint, a.getPaint());
    }

    @Test
    public void constructor_rejectsNullStroke() {
        try {
            new XYLineAnnotation(X1, Y1, X2, Y2, null, Color.BLACK);
            fail("Expected IllegalArgumentException for null stroke");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    @Test
    public void constructor_rejectsNullPaint() {
        try {
            new XYLineAnnotation(X1, Y1, X2, Y2, new BasicStroke(1f), null);
            fail("Expected IllegalArgumentException for null paint");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    @Test
    public void constructor_rejectsNonFiniteCoordinates() {
        try {
            new XYLineAnnotation(Double.NEGATIVE_INFINITY, Y1, X2, Y2);
            fail("Expected IllegalArgumentException for non-finite x1");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    // ---------------------------------------------------------------------
    // Getters
    // ---------------------------------------------------------------------

    @Test
    public void getters_returnConstructorValues() {
        BasicStroke stroke = new BasicStroke(1.5f);
        Color paint = Color.RED;
        XYLineAnnotation a = new XYLineAnnotation(X1, Y1, X2, Y2, stroke, paint);

        assertEquals(X1, a.getX1(), 0.0000001);
        assertEquals(Y1, a.getY1(), 0.0000001);
        assertEquals(X2, a.getX2(), 0.0000001);
        assertEquals(Y2, a.getY2(), 0.0000001);
        assertSame(stroke, a.getStroke());
        assertSame(paint, a.getPaint());
    }

    // ---------------------------------------------------------------------
    // equals() and hashCode()
    // ---------------------------------------------------------------------

    @Test
    public void equals_and_hashCode_sameValuesAndStyle() {
        BasicStroke s1 = new BasicStroke(1f);
        BasicStroke s2 = new BasicStroke(1f); // equal by value, not same reference
        Color c1 = Color.BLACK;
        Color c2 = Color.BLACK;

        XYLineAnnotation a1 = new XYLineAnnotation(X1, Y1, X2, Y2, s1, c1);
        XYLineAnnotation a2 = new XYLineAnnotation(X1, Y1, X2, Y2, s2, c2);

        assertTrue(a1.equals(a2));
        assertTrue(a2.equals(a1));
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    public void equals_differsByCoordinates() {
        XYLineAnnotation a1 = new XYLineAnnotation(X1, Y1, X2, Y2);
        XYLineAnnotation a2 = new XYLineAnnotation(X1, Y1, X2 + 1.0, Y2);

        assertFalse(a1.equals(a2));
        assertFalse(a2.equals(a1));
    }

    @Test
    public void equals_differsByStroke() {
        XYLineAnnotation a1 = new XYLineAnnotation(X1, Y1, X2, Y2, new BasicStroke(1f), Color.BLACK);
        XYLineAnnotation a2 = new XYLineAnnotation(X1, Y1, X2, Y2, new BasicStroke(2f), Color.BLACK);

        assertFalse(a1.equals(a2));
    }

    @Test
    public void equals_differsByPaint() {
        XYLineAnnotation a1 = new XYLineAnnotation(X1, Y1, X2, Y2, new BasicStroke(1f), Color.BLACK);
        XYLineAnnotation a2 = new XYLineAnnotation(X1, Y1, X2, Y2, new BasicStroke(1f), Color.RED);

        assertFalse(a1.equals(a2));
    }

    @Test
    public void equals_reflexiveAndNotEqualToOtherType() {
        XYLineAnnotation a = new XYLineAnnotation(X1, Y1, X2, Y2);
        assertTrue(a.equals(a)); // reflexive
        assertFalse(a.equals("not-an-annotation"));
    }

    // ---------------------------------------------------------------------
    // clone()
    // ---------------------------------------------------------------------

    @Test
    public void clone_returnsEqualIndependentCopy() throws Exception {
        XYLineAnnotation original = new XYLineAnnotation(X1, Y1, X2, Y2, new BasicStroke(2f), Color.GREEN);
        XYLineAnnotation copy = (XYLineAnnotation) original.clone();

        assertNotSame("Clone should be a different instance", original, copy);
        assertTrue("Clone should be equal to original", original.equals(copy));
    }

    // ---------------------------------------------------------------------
    // draw()
    // ---------------------------------------------------------------------

    @Test
    public void draw_withValidArgs_doesNotThrow() {
        XYLineAnnotation a = new XYLineAnnotation(X1, Y1, X2, Y2, new BasicStroke(1f), Color.BLACK);
        a.setToolTipText("line");
        a.setURL("https://example.invalid/line");

        Graphics2D g2 = newGraphics();
        XYPlot plot = newXYPlot();
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 100, 100);
        ValueAxis x = plot.getDomainAxis();
        ValueAxis y = plot.getRangeAxis();

        // Just verify no exception is thrown
        a.draw(g2, plot, dataArea, x, y, 0, (PlotRenderingInfo) null);
    }

    @Test(expected = NullPointerException.class)
    public void draw_withNullGraphics_throwsNPE() {
        XYLineAnnotation a = new XYLineAnnotation(X1, Y1, X2, Y2);
        XYPlot plot = newXYPlot();
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 100, 100);
        ValueAxis x = plot.getDomainAxis();
        ValueAxis y = plot.getRangeAxis();

        a.draw(null, plot, dataArea, x, y, 0, null);
    }
}