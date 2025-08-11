package org.jfree.chart.plot.compass;

import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.junit.Test;

/**
 * Readable, behavior-focused tests for MeterNeedle and common subclasses.
 *
 * Notes:
 * - These tests focus on clear Arrange/Act/Assert structure and meaningful names.
 * - Only behaviors that are stable and useful to verify are asserted.
 * - We avoid redundant assertions and EvoSuite-specific scaffolding.
 */
public class MeterNeedleTest {

    // Helpers
    private static Graphics2D newG2(int w, int h) {
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB).createGraphics();
    }

    private static Rectangle2D rect(double w, double h) {
        return new Rectangle2D.Double(0, 0, w, h);
    }

    // Defaults

    @Test
    public void shipNeedle_defaultsAreSize5_andRotateAtCenter() {
        ShipNeedle n = new ShipNeedle();

        assertEquals(5, n.getSize());
        assertEquals(0.5, n.getRotateX(), 0.0001);
        assertEquals(0.5, n.getRotateY(), 0.0001);
    }

    @Test
    public void longNeedle_hasDifferentDefaultRotateY() {
        LongNeedle n = new LongNeedle();

        assertEquals(5, n.getSize());
        assertEquals(0.5, n.getRotateX(), 0.0001);
        assertEquals(0.8, n.getRotateY(), 0.0001);
    }

    @Test
    public void outlineStroke_defaultsToWidth2() {
        LongNeedle n = new LongNeedle();

        BasicStroke stroke = (BasicStroke) n.getOutlineStroke();
        assertEquals(2.0f, stroke.getLineWidth(), 0.0001f);
    }

    // Getters/Setters

    @Test
    public void rotateX_canBeChangedToAnyDouble() {
        ArrowNeedle n = new ArrowNeedle(false);

        n.setRotateX(-9.0);
        assertEquals(-9.0, n.getRotateX(), 0.0001);
    }

    @Test
    public void rotateY_canBeChangedToAnyDouble() {
        LongNeedle n = new LongNeedle();

        n.setRotateY(-590.9257035);
        assertEquals(-590.9257035, n.getRotateY(), 0.0001);
    }

    @Test
    public void size_canBeChanged_evenToNegative() {
        ShipNeedle n = new ShipNeedle();

        n.setSize(-1083);
        assertEquals(-1083, n.getSize());
    }

    @Test
    public void paints_canBeSetAndRetrieved() {
        PlumNeedle n = new PlumNeedle();

        n.setOutlinePaint(Color.RED);
        n.setFillPaint(Color.BLUE);
        n.setHighlightPaint(Color.GREEN);

        assertSame(Color.RED, n.getOutlinePaint());
        assertSame(Color.BLUE, n.getFillPaint());
        assertSame(Color.GREEN, n.getHighlightPaint());
    }

    @Test
    public void paints_acceptNull() {
        PointerNeedle n = new PointerNeedle();

        n.setOutlinePaint(null);
        n.setFillPaint(null);
        n.setHighlightPaint(null);

        assertNull(n.getOutlinePaint());
        assertNull(n.getFillPaint());
        assertNull(n.getHighlightPaint());
    }

    // Drawing

    @Test
    public void draw_withValidGraphicsAndArea_doesNotThrow() {
        PinNeedle n = new PinNeedle();
        Graphics2D g2 = newG2(20, 20);
        Rectangle2D area = rect(10, 10);

        // No exception expected
        n.draw(g2, area);
        n.draw(g2, area, Math.toRadians(30));
        n.draw(g2, area, new Point2D.Double(5, 5), Math.toRadians(60));
    }

    @Test(expected = NullPointerException.class)
    public void draw_withNullGraphics_throwsNPE() {
        PointerNeedle n = new PointerNeedle();
        n.draw(null, rect(10, 10));
    }

    @Test(expected = NullPointerException.class)
    public void draw_withNullPlotArea_throwsNPE() {
        WindNeedle n = new WindNeedle();
        n.draw(newG2(10, 10), null, Math.toRadians(15));
    }

    @Test
    public void draw_withNullRotationPoint_isAllowed() {
        LongNeedle n = new LongNeedle();
        Graphics2D g2 = newG2(20, 20);
        Rectangle2D area = rect(10, 10);

        // No exception expected when rotate point is null
        n.draw(g2, area, null, Math.toRadians(10));
    }

    // defaultDisplay (protected) – accessible because test is in same package

    @Test(expected = NullPointerException.class)
    public void defaultDisplay_withNullGraphics_throwsNPE() {
        ShipNeedle n = new ShipNeedle();
        Rectangle2D shape = rect(5, 5); // Rectangle2D is a Shape

        n.defaultDisplay(null, shape);
    }

    // Equality and cloning

    @Test
    public void equals_isReflexive_andDifferentSubclassesNotEqual() {
        WindNeedle a = new WindNeedle();
        PlumNeedle b = new PlumNeedle();

        assertTrue(a.equals(a));
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_detectsChangeInOutlineStroke() throws Exception {
        MiddlePinNeedle original = new MiddlePinNeedle();
        MiddlePinNeedle copy = (MiddlePinNeedle) original.clone();

        assertTrue(original.equals(copy));

        copy.setOutlineStroke(new BasicStroke(3f));
        assertFalse(original.equals(copy));
        assertFalse(copy.equals(original));
    }

    // Misc

    @Test
    public void getTransform_returnsNonNullTransform() {
        ShipNeedle n = new ShipNeedle();

        assertNotNull(n.getTransform());
    }
}