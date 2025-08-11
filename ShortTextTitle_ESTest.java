package org.jfree.chart.title;

import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.block.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

public class ShortTextTitle_ReadableTest {

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private static Graphics2D newGraphics() {
        BufferedImage img = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB);
        return img.createGraphics();
    }

    // -----------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------

    @Test
    public void constructorRejectsNullText() {
        try {
            new ShortTextTitle(null);
            fail("Expected IllegalArgumentException for null text");
        } catch (IllegalArgumentException ex) {
            // From Args: "Null 'text' argument."
            assertTrue(ex.getMessage() == null || ex.getMessage().toLowerCase().contains("null"));
        }
    }

    // -----------------------------------------------------------------------
    // arrange(...) entry point
    // -----------------------------------------------------------------------

    @Test
    public void arrangeRejectsNullConstraint() {
        ShortTextTitle title = new ShortTextTitle("Title");
        try {
            title.arrange(newGraphics(), null);
            fail("Expected IllegalArgumentException when RectangleConstraint is null");
        } catch (IllegalArgumentException ex) {
            // In JFreeChart, Args typically says: "Null 'c' argument."
            assertTrue(ex.getMessage() == null || ex.getMessage().toLowerCase().contains("null"));
        }
    }

    // -----------------------------------------------------------------------
    // arrangeNN (no constraints)
    // -----------------------------------------------------------------------

    @Test
    public void arrangeNNWithNonEmptyTextProducesPositiveSize() {
        ShortTextTitle title = new ShortTextTitle("Short title");
        Size2D size = title.arrangeNN(newGraphics());

        assertNotNull(size);
        assertTrue("Width should be non-negative", size.getWidth() >= 0.0);
        assertTrue("Height should be positive for text", size.getHeight() > 0.0);
    }

    @Test
    public void arrangeNNWithEmptyTextProducesNonNegativeSize() {
        ShortTextTitle title = new ShortTextTitle("");
        Size2D size = title.arrangeNN(newGraphics());

        assertNotNull(size);
        // Width could be zero for empty text; height is at least line height (depends on font)
        assertTrue(size.getWidth() >= 0.0);
        assertTrue(size.getHeight() >= 0.0);
    }

    // -----------------------------------------------------------------------
    // arrangeFN (fixed width, no height bound)
    // -----------------------------------------------------------------------

    @Test
    public void arrangeFNUsesProvidedWidth() {
        ShortTextTitle title = new ShortTextTitle("Fixed width");
        double fixedWidth = 120.0;

        Size2D size = title.arrangeFN(newGraphics(), fixedWidth);

        assertNotNull(size);
        // For fixed width layout, the reported width should be the given width (or not exceed it).
        // Allow a small tolerance for defensive implementations.
        assertTrue("Width should not exceed the fixed width", size.getWidth() <= fixedWidth + 0.0001);
        assertTrue("Height should be non-negative", size.getHeight() >= 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void arrangeFNRejectsNullGraphics() {
        ShortTextTitle title = new ShortTextTitle("x");
        title.arrangeFN(null, 10.0);
    }

    // -----------------------------------------------------------------------
    // arrangeRN (width range, no height bound)
    // -----------------------------------------------------------------------

    @Test
    public void arrangeRNRespectsWidthRange() {
        ShortTextTitle title = new ShortTextTitle("Range width");
        Graphics2D g2 = newGraphics();
        Range widthRange = new Range(50.0, 150.0);

        Size2D size = title.arrangeRN(g2, widthRange);

        assertNotNull(size);
        assertTrue("Width should be within the provided range",
                size.getWidth() >= widthRange.getLowerBound() - 0.0001
                        && size.getWidth() <= widthRange.getUpperBound() + 0.0001);
        assertTrue("Height should be non-negative", size.getHeight() >= 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void arrangeRNRejectsNullGraphics() {
        ShortTextTitle title = new ShortTextTitle("x");
        title.arrangeRN(null, new Range(0, 10));
    }

    // -----------------------------------------------------------------------
    // arrangeRR (width and height ranges)
    // -----------------------------------------------------------------------

    @Test
    public void arrangeRRRespectsRanges() {
        ShortTextTitle title = new ShortTextTitle("Both ranges");
        Graphics2D g2 = newGraphics();
        Range widthRange = new Range(10.0, 300.0);
        Range heightRange = new Range(5.0, 100.0);

        Size2D size = title.arrangeRR(g2, widthRange, heightRange);

        assertNotNull(size);
        assertTrue(size.getWidth() >= widthRange.getLowerBound() - 0.0001);
        assertTrue(size.getWidth() <= widthRange.getUpperBound() + 0.0001);
        assertTrue(size.getHeight() >= heightRange.getLowerBound() - 0.0001);
        assertTrue(size.getHeight() <= heightRange.getUpperBound() + 0.0001);
    }

    @Test(expected = NullPointerException.class)
    public void arrangeRRRejectsNullGraphics() {
        ShortTextTitle title = new ShortTextTitle("x");
        title.arrangeRR(null, new Range(0, 10), new Range(0, 10));
    }

    // -----------------------------------------------------------------------
    // draw(...)
    // -----------------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void drawRejectsNullGraphics() {
        ShortTextTitle title = new ShortTextTitle("x");
        title.draw(null, new Rectangle2D.Double(0, 0, 100, 20), null);
    }

    @Test(expected = NullPointerException.class)
    public void drawRejectsNullArea() {
        ShortTextTitle title = new ShortTextTitle("x");
        title.draw(newGraphics(), null, null);
    }

    @Test
    public void drawWithValidArgsDoesNotThrowAndReturnsNull() {
        ShortTextTitle title = new ShortTextTitle("Visible");
        Graphics2D g2 = newGraphics();
        Rectangle2D area = new Rectangle2D.Double(0, 0, 200, 50);

        Object result = title.draw(g2, area, null);

        // Contract in Javadoc says: returns null.
        assertNull(result);
        // We only assert it didn't throw; whether 'area' is mutated is an implementation detail.
        assertNotNull(area);
    }

    // -----------------------------------------------------------------------
    // Visual properties impact
    // -----------------------------------------------------------------------

    @Test
    public void largerFontShouldNotReduceTextHeight() {
        ShortTextTitle title = new ShortTextTitle("A B C");
        Graphics2D g2 = newGraphics();

        // Measure with default font
        Size2D defaultSize = title.arrangeNN(g2);

        // Increase font size and measure again
        title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));
        Size2D largerSize = title.arrangeNN(g2);

        assertNotNull(defaultSize);
        assertNotNull(largerSize);
        assertTrue("Larger font should produce height >= default height",
                largerSize.getHeight() >= defaultSize.getHeight() - 0.0001);
    }

    @Test
    public void marginsIncreaseTotalHeight() {
        ShortTextTitle title = new ShortTextTitle("Margins");
        Graphics2D g2 = newGraphics();

        // Start with zero margins
        title.setMargin(0, 0, 0, 0);
        Size2D base = title.arrangeNN(g2);

        // Add top/bottom margins
        double top = 10.0;
        double bottom = 12.0;
        title.setMargin(top, 0, bottom, 0);
        Size2D withMargins = title.arrangeNN(g2);

        assertNotNull(base);
        assertNotNull(withMargins);
        assertTrue("Height with margins should be >= base height + (top + bottom)",
                withMargins.getHeight() + 1e-6 >= base.getHeight() + top + bottom);
    }

    // -----------------------------------------------------------------------
    // arrange(...) with RectangleConstraint variants
    // -----------------------------------------------------------------------

    @Test
    public void arrangeWithFixedWidthConstraintProducesNonNegativeSize() {
        ShortTextTitle title = new ShortTextTitle("Constrained");
        Graphics2D g2 = newGraphics();
        RectangleConstraint constraint = new RectangleConstraint(150.0, RectangleConstraint.NONE);

        Size2D size = title.arrange(g2, constraint);

        assertNotNull(size);
        assertTrue(size.getWidth() >= 0.0);
        assertTrue(size.getHeight() >= 0.0);
    }

    @Test
    public void arrangeWithRangeConstraintProducesNonNegativeSize() {
        ShortTextTitle title = new ShortTextTitle("Constrained by ranges");
        Graphics2D g2 = newGraphics();
        RectangleConstraint constraint = new RectangleConstraint(new Range(50, 100), new Range(10, 80));

        Size2D size = title.arrange(g2, constraint);

        assertNotNull(size);
        assertTrue(size.getWidth() >= 0.0);
        assertTrue(size.getHeight() >= 0.0);
    }
}