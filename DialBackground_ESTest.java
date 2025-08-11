package org.jfree.chart.plot.dial;

import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.GradientPaintTransformer;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.junit.Test;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.*;

public class DialBackgroundTest {

    // Helper: lightweight Graphics2D to render into
    private static Graphics2D newGraphics() {
        BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        return img.createGraphics();
    }

    // ----------------------------------------------------------------------
    // Construction and defaults
    // ----------------------------------------------------------------------

    @Test
    public void defaultValues_areReasonable() {
        DialBackground bg = new DialBackground();

        // default paint is white
        assertEquals(Color.WHITE, bg.getPaint());

        // default gradient transformer is StandardGradientPaintTransformer(VERTICAL)
        GradientPaintTransformer t = bg.getGradientPaintTransformer();
        assertTrue(t instanceof StandardGradientPaintTransformer);
        assertEquals(GradientPaintTransformType.VERTICAL,
                ((StandardGradientPaintTransformer) t).getType());

        // background should be clipped to the dial window
        assertTrue(bg.isClippedToWindow());
    }

    @Test
    public void constructor_rejectsNullPaint() {
        try {
            new DialBackground(null);
            fail("Expected IllegalArgumentException for null paint");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    // ----------------------------------------------------------------------
    // Property setters/getters
    // ----------------------------------------------------------------------

    @Test
    public void setPaint_rejectsNull() {
        DialBackground bg = new DialBackground();
        try {
            bg.setPaint(null);
            fail("Expected IllegalArgumentException for null paint");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void setGradientPaintTransformer_rejectsNull() {
        DialBackground bg = new DialBackground();
        try {
            bg.setGradientPaintTransformer(null);
            fail("Expected IllegalArgumentException for null transformer");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    // ----------------------------------------------------------------------
    // Equality, hashCode, cloning
    // ----------------------------------------------------------------------

    @Test
    public void equals_and_hashCode_considerPaintAndTransformer() {
        DialBackground a = new DialBackground();
        DialBackground b = new DialBackground();

        // same defaults -> equal and same hash
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        // different paint -> not equal
        DialBackground differentPaint = new DialBackground(Color.RED);
        assertNotEquals(a, differentPaint);

        // different transformer -> not equal
        DialBackground differentTransformer = new DialBackground();
        differentTransformer.setGradientPaintTransformer(
                new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_HORIZONTAL));
        assertNotEquals(a, differentTransformer);

        // equals is reflexive
        assertEquals(a, a);

        // not equal to an unrelated type
        assertNotEquals(a, "not a DialBackground");
    }

    @Test
    public void clone_createsIndependentCopy() throws CloneNotSupportedException {
        DialBackground original = new DialBackground();
        DialBackground copy = (DialBackground) original.clone();

        assertNotSame(original, copy);
        assertEquals(original, copy); // same state after cloning

        // mutate original and ensure copies diverge
        original.setPaint(Color.BLACK);
        assertNotEquals(original, copy);
    }

    // ----------------------------------------------------------------------
    // Drawing behavior
    // ----------------------------------------------------------------------

    @Test
    public void draw_withSolidPaint_doesNotThrow() {
        DialBackground bg = new DialBackground(Color.WHITE);
        Graphics2D g2 = newGraphics();
        try {
            bg.draw(g2, new DialPlot(),
                    new Rectangle2D.Double(0, 0, 10, 10),
                    new Rectangle2D.Double(0, 0, 10, 10));
            // no exception expected
        } finally {
            g2.dispose();
        }
    }

    @Test
    public void draw_withGradientPaintAndTransformer_doesNotThrow() {
        DialBackground bg = new DialBackground(
                new GradientPaint(0f, 0f, Color.RED, 10f, 10f, Color.BLUE));
        bg.setGradientPaintTransformer(
                new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));

        Graphics2D g2 = newGraphics();
        try {
            bg.draw(g2, new DialPlot(),
                    new Rectangle2D.Double(0, 0, 10, 10),
                    new Rectangle2D.Double(0, 0, 10, 10));
            // no exception expected
        } finally {
            g2.dispose();
        }
    }

    @Test
    public void draw_rejectsNullGraphics() {
        DialBackground bg = new DialBackground();
        try {
            bg.draw(null, new DialPlot(),
                    new Rectangle2D.Double(0, 0, 10, 10),
                    new Rectangle2D.Double(0, 0, 10, 10));
            fail("Expected NullPointerException when Graphics2D is null");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void draw_rejectsNullView() {
        DialBackground bg = new DialBackground();
        Graphics2D g2 = newGraphics();
        try {
            bg.draw(g2, new DialPlot(),
                    new Rectangle2D.Double(0, 0, 10, 10),
                    null);
            fail("Expected NullPointerException when view is null");
        } catch (NullPointerException expected) {
            // expected
        } finally {
            g2.dispose();
        }
    }
}