package org.jfree.chart.renderer.category;

import static org.junit.Assert.*;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

/**
 * Readable tests for StandardBarPainter focusing on:
 * - equals/hashCode contract
 * - basic happy-path painting (no exceptions)
 * - clear failure behavior for null inputs
 */
public class StandardBarPainterTest {

    private static Graphics2D newGraphics2D(int w, int h) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        return img.createGraphics();
    }

    // equals/hashCode

    @Test
    public void equalsAndHashCode_contract() {
        StandardBarPainter a = new StandardBarPainter();
        StandardBarPainter b = new StandardBarPainter();

        // reflexive
        assertTrue(a.equals(a));
        // symmetric
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        // hashCode consistency for equal objects
        assertEquals(a.hashCode(), b.hashCode());
        // null and different type
        assertFalse(a.equals(null));
        assertFalse(a.equals("not a painter"));
    }

    // paintBar: happy path

    @Test
    public void paintBar_withValidInputs_doesNotThrow() {
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new BarRenderer();
        Rectangle2D bar = new Rectangle2D.Double(1, 1, 6, 8);

        Graphics2D g2 = newGraphics2D(20, 20);
        try {
            painter.paintBar(g2, renderer, 0, 0, bar, RectangleEdge.BOTTOM);
        } finally {
            g2.dispose();
        }
    }

    // paintBarShadow: happy path

    @Test
    public void paintBarShadow_withValidInputs_doesNotThrow() {
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new BarRenderer();
        Rectangle2D bar = new Rectangle2D.Double(2, 2, 5, 7);

        Graphics2D g2 = newGraphics2D(20, 20);
        try {
            painter.paintBarShadow(g2, renderer, 0, 0, bar, RectangleEdge.LEFT, true);
        } finally {
            g2.dispose();
        }
    }

    // paintBar: null handling

    @Test(expected = NullPointerException.class)
    public void paintBar_nullGraphics2D_throwsNPE() {
        new StandardBarPainter().paintBar(null, new BarRenderer(), 0, 0,
                new Rectangle2D.Double(), RectangleEdge.RIGHT);
    }

    @Test(expected = NullPointerException.class)
    public void paintBar_nullBar_throwsNPE() {
        StandardBarPainter painter = new StandardBarPainter();
        Graphics2D g2 = newGraphics2D(10, 10);
        try {
            painter.paintBar(g2, new BarRenderer(), 0, 0, null, RectangleEdge.TOP);
        } finally {
            g2.dispose();
        }
    }

    // paintBarShadow: null handling

    @Test(expected = NullPointerException.class)
    public void paintBarShadow_nullGraphics2D_throwsNPE() {
        new StandardBarPainter().paintBarShadow(null, new BarRenderer(), 0, 0,
                new Rectangle2D.Double(), RectangleEdge.BOTTOM, false);
    }

    @Test(expected = NullPointerException.class)
    public void paintBarShadow_nullBase_throwsNPE() {
        StandardBarPainter painter = new StandardBarPainter();
        Graphics2D g2 = newGraphics2D(10, 10);
        try {
            painter.paintBarShadow(g2, new BarRenderer(), 0, 0,
                    new Rectangle2D.Double(0, 0, 5, 5), null, true);
        } finally {
            g2.dispose();
        }
    }
}