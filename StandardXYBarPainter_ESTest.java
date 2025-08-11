package org.jfree.chart.renderer.xy;

import static org.junit.Assert.*;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Test;

/**
 * Readable and focused tests for StandardXYBarPainter.
 *
 * Notes:
 * - These tests avoid EvoSuite-specific scaffolding and assertions.
 * - They focus on clearly exercising the public API and expected basic behaviors:
 *   equals/hashCode contracts and that painting methods handle valid and invalid inputs.
 */
public class StandardXYBarPainterTest {

    // Helpers

    private Graphics2D newGraphics() {
        return new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB).createGraphics();
    }

    private RectangularShape sampleBar() {
        return new Rectangle2D.Double(1, 2, 3, 4);
    }

    // equals / hashCode

    @Test
    public void equals_isReflexive() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        assertTrue(painter.equals(painter));
    }

    @Test
    public void equals_isSymmetricForTwoNewInstances() {
        StandardXYBarPainter a = new StandardXYBarPainter();
        StandardXYBarPainter b = new StandardXYBarPainter();
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_returnsFalseForDifferentTypeOrNull() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        assertFalse(painter.equals(null));
        assertFalse(painter.equals(new Object()));
    }

    @Test
    public void hashCode_isStable() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        int hc1 = painter.hashCode();
        int hc2 = painter.hashCode();
        assertEquals(hc1, hc2);
    }

    // paintBar

    @Test(expected = NullPointerException.class)
    public void paintBar_nullGraphics_throwsNullPointerException() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        painter.paintBar(null, renderer, 0, 0, sampleBar(), RectangleEdge.BOTTOM);
    }

    @Test
    public void paintBar_withValidArgs_doesNotThrow() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Graphics2D g2 = newGraphics();
        try {
            painter.paintBar(g2, renderer, 0, 0, sampleBar(), RectangleEdge.BOTTOM);
            painter.paintBar(g2, renderer, 1, 2, sampleBar(), RectangleEdge.TOP);
            painter.paintBar(g2, renderer, 3, 4, sampleBar(), RectangleEdge.LEFT);
            painter.paintBar(g2, renderer, 5, 6, sampleBar(), RectangleEdge.RIGHT);
        } finally {
            g2.dispose();
        }
    }

    // paintBarShadow

    @Test(expected = NullPointerException.class)
    public void paintBarShadow_nullGraphics_throwsNullPointerException() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        painter.paintBarShadow(null, renderer, 0, 0, sampleBar(), RectangleEdge.BOTTOM, false);
    }

    @Test
    public void paintBarShadow_withValidArgs_doesNotThrow_forAllEdgesAndPegOptions() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Graphics2D g2 = newGraphics();
        try {
            for (RectangleEdge edge : new RectangleEdge[] {
                    RectangleEdge.BOTTOM, RectangleEdge.TOP,
                    RectangleEdge.LEFT, RectangleEdge.RIGHT
            }) {
                painter.paintBarShadow(g2, renderer, 0, 0, sampleBar(), edge, false);
                painter.paintBarShadow(g2, renderer, 1, 1, sampleBar(), edge, true);
            }
        } finally {
            g2.dispose();
        }
    }
}