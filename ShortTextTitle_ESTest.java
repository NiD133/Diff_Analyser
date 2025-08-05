package org.jfree.chart.title;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.block.Size2D;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.pie.PiePlot;
import org.jfree.chart.title.Title;
import org.jfree.data.Range;

/**
 * Test suite for ShortTextTitle class functionality.
 * Tests layout arrangements, drawing, and error handling.
 */
public class ShortTextTitleTest {

    // Test helper methods
    private Graphics2D createTestGraphics() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        return image.createGraphics();
    }

    private JFreeChart createTestChart(ShortTextTitle title) {
        PiePlot plot = new PiePlot();
        JFreeChart chart = new JFreeChart(plot);
        if (title != null) {
            chart.setSubtitles(List.of(title));
        }
        return chart;
    }

    // Constructor tests
    @Test
    public void testConstructorWithValidText() {
        ShortTextTitle title = new ShortTextTitle("Test Title");
        assertNotNull("Title should be created successfully", title);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullText() {
        new ShortTextTitle(null);
    }

    // Layout arrangement tests - no constraints
    @Test
    public void testArrangeWithNoConstraints_EmptyText() {
        ShortTextTitle title = new ShortTextTitle("");
        Graphics2D g2 = createTestGraphics();
        
        Size2D size = title.arrangeNN(g2);
        
        assertTrue("Width should be non-negative", size.width >= 0);
        assertTrue("Height should be non-negative", size.height >= 0);
        assertEquals("Height should be default font height", 15.0, size.height, 0.01);
    }

    @Test
    public void testArrangeWithNoConstraints_NonEmptyText() {
        ShortTextTitle title = new ShortTextTitle("Sample Text");
        Graphics2D g2 = createTestGraphics();
        
        Size2D size = title.arrangeNN(g2);
        
        assertTrue("Width should be positive for non-empty text", size.width > 0);
        assertTrue("Height should be positive", size.height > 0);
    }

    // Layout arrangement tests - width range constraints
    @Test
    public void testArrangeWithWidthRange_EmptyText() {
        ShortTextTitle title = new ShortTextTitle("");
        Graphics2D g2 = createTestGraphics();
        Range widthRange = new Range(10, 100);
        
        Size2D size = title.arrangeRN(g2, widthRange);
        
        assertTrue("Width should be within range or zero", 
                   size.width == 0 || (size.width >= 10 && size.width <= 100));
        assertEquals("Height should be default font height", 15.0, size.height, 0.01);
    }

    @Test
    public void testArrangeWithWidthRange_TextFitsInRange() {
        ShortTextTitle title = new ShortTextTitle("Short");
        Graphics2D g2 = createTestGraphics();
        Range widthRange = new Range(50, 200);
        
        Size2D size = title.arrangeRN(g2, widthRange);
        
        assertTrue("Width should be positive when text fits", size.width > 0);
        assertTrue("Height should be positive when text fits", size.height > 0);
    }

    @Test
    public void testArrangeWithWidthRange_TextTooWide() {
        ShortTextTitle title = new ShortTextTitle("This is a very long text that should not fit");
        Graphics2D g2 = createTestGraphics();
        Range widthRange = new Range(1, 10); // Very narrow range
        
        Size2D size = title.arrangeRN(g2, widthRange);
        
        assertEquals("Width should be zero when text doesn't fit", 0.0, size.width, 0.01);
        assertEquals("Height should be zero when text doesn't fit", 0.0, size.height, 0.01);
    }

    // Layout arrangement tests - fixed width constraints
    @Test
    public void testArrangeWithFixedWidth_EmptyText() {
        ShortTextTitle title = new ShortTextTitle("");
        Graphics2D g2 = createTestGraphics();
        double fixedWidth = 50.0;
        
        Size2D size = title.arrangeFN(g2, fixedWidth);
        
        assertEquals("Width should match fixed width for empty text", 
                     fixedWidth, size.width, 0.01);
    }

    @Test
    public void testArrangeWithFixedWidth_VerySmallWidth() {
        ShortTextTitle title = new ShortTextTitle("Text");
        Graphics2D g2 = createTestGraphics();
        double verySmallWidth = 0.08;
        
        Size2D size = title.arrangeFN(g2, verySmallWidth);
        
        assertEquals("Should return zero size for very small width", 
                     0.0, size.width, 0.01);
        assertEquals("Should return zero size for very small width", 
                     0.0, size.height, 0.01);
    }

    // Layout arrangement tests - width and height range constraints
    @Test
    public void testArrangeWithBothRanges_EmptyText() {
        ShortTextTitle title = new ShortTextTitle("");
        Graphics2D g2 = createTestGraphics();
        Range widthRange = ValueAxis.DEFAULT_RANGE;
        Range heightRange = ValueAxis.DEFAULT_RANGE;
        
        Size2D size = title.arrangeRR(g2, widthRange, heightRange);
        
        assertEquals("Empty text should have zero width", 0.0, size.width, 0.01);
        assertEquals("Empty text should have zero height", 0.0, size.height, 0.01);
    }

    @Test
    public void testArrangeWithBothRanges_NonEmptyText() {
        ShortTextTitle title = new ShortTextTitle("Test Text");
        Graphics2D g2 = createTestGraphics();
        Range widthRange = new Range(50, 200);
        Range heightRange = new Range(10, 50);
        
        Size2D size = title.arrangeRR(g2, widthRange, heightRange);
        
        assertTrue("Width should be non-negative", size.width >= 0);
        assertTrue("Height should be non-negative", size.height >= 0);
    }

    // Rectangle constraint tests
    @Test
    public void testArrangeWithRectangleConstraint_ValidConstraint() {
        ShortTextTitle title = new ShortTextTitle("Test");
        Graphics2D g2 = createTestGraphics();
        Range validRange = new Range(0, 100);
        RectangleConstraint constraint = new RectangleConstraint(validRange, validRange);
        
        Size2D size = title.arrange(g2, constraint);
        
        assertTrue("Width should be non-negative", size.width >= 0);
        assertTrue("Height should be non-negative", size.height >= 0);
    }

    @Test
    public void testArrangeWithRectangleConstraint_WithMargins() {
        ShortTextTitle title = new ShortTextTitle("Test");
        title.setMargin(-10.0, 5.0, 20.0, 5.0); // top, left, bottom, right
        Graphics2D g2 = createTestGraphics();
        Range validRange = new Range(0, 100);
        RectangleConstraint constraint = new RectangleConstraint(validRange, validRange);
        
        Size2D size = title.arrange(g2, constraint);
        
        // Height should include margins (even negative ones)
        assertTrue("Size should account for margins", size.height != 0);
    }

    // Drawing tests
    @Test
    public void testDraw_ValidParameters() {
        ShortTextTitle title = new ShortTextTitle("Test Title");
        Graphics2D g2 = createTestGraphics();
        Rectangle2D area = new Rectangle2D.Double(10, 10, 100, 50);
        Object params = new Object();
        
        // Should not throw exception
        Object result = title.draw(g2, area, params);
        
        // Draw method typically returns null for titles
        assertNull("Draw should return null", result);
    }

    // Integration tests
    @Test
    public void testIntegrationWithChart_EmptyTitle() {
        ShortTextTitle title = new ShortTextTitle("");
        JFreeChart chart = createTestChart(title);
        
        // Should be able to create chart image without errors
        BufferedImage image = chart.createBufferedImage(100, 100);
        
        assertNotNull("Chart image should be created", image);
        assertEquals("Image should have correct width", 100, image.getWidth());
        assertEquals("Image should have correct height", 100, image.getHeight());
    }

    @Test
    public void testIntegrationWithChart_NonEmptyTitle() {
        ShortTextTitle title = new ShortTextTitle("Chart Title");
        JFreeChart chart = createTestChart(title);
        
        // Should be able to create chart image without errors
        BufferedImage image = chart.createBufferedImage(200, 150);
        
        assertNotNull("Chart image should be created", image);
        assertEquals("Image should have correct dimensions", 200, image.getWidth());
    }

    // Font handling tests
    @Test
    public void testWithCustomFont() {
        ShortTextTitle title = new ShortTextTitle("Custom Font Test");
        Font customFont = new Font("Arial", Font.BOLD, 14);
        title.setFont(customFont);
        Graphics2D g2 = createTestGraphics();
        
        Size2D size = title.arrangeNN(g2);
        
        assertTrue("Should handle custom font", size.width > 0);
        assertTrue("Should handle custom font", size.height > 0);
    }

    // Error handling tests
    @Test(expected = NullPointerException.class)
    public void testArrangeNN_NullGraphics() {
        ShortTextTitle title = new ShortTextTitle("Test");
        title.arrangeNN(null);
    }

    @Test(expected = NullPointerException.class)
    public void testArrangeRN_NullGraphics() {
        ShortTextTitle title = new ShortTextTitle("Test");
        Range range = new Range(10, 100);
        title.arrangeRN(null, range);
    }

    @Test(expected = NullPointerException.class)
    public void testArrangeFN_NullGraphics() {
        ShortTextTitle title = new ShortTextTitle("Test");
        title.arrangeFN(null, 50.0);
    }

    @Test(expected = NullPointerException.class)
    public void testArrangeRR_NullGraphics() {
        ShortTextTitle title = new ShortTextTitle("Test");
        Range range = new Range(10, 100);
        title.arrangeRR(null, range, range);
    }

    @Test(expected = NullPointerException.class)
    public void testDraw_NullGraphics() {
        ShortTextTitle title = new ShortTextTitle("Test");
        Rectangle2D area = new Rectangle2D.Double(0, 0, 100, 50);
        title.draw(null, area, new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrange_NullConstraint() {
        ShortTextTitle title = new ShortTextTitle("Test");
        Graphics2D g2 = createTestGraphics();
        title.arrange(g2, null);
    }

    // Edge case tests
    @Test(expected = RuntimeException.class)
    public void testArrange_UnsupportedConstraintType() {
        ShortTextTitle title = new ShortTextTitle("Not yet implemented.");
        Graphics2D g2 = createTestGraphics();
        // Create a constraint that triggers "Not yet implemented" path
        RectangleConstraint constraint = new RectangleConstraint(-158, -158);
        title.arrange(g2, constraint);
    }
}