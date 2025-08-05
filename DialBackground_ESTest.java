package org.jfree.chart.plot.dial;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.pie.PiePlot;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.GradientPaintTransformer;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.jfree.data.general.DefaultValueDataset;

/**
 * Test suite for DialBackground class functionality.
 * Tests cover construction, property setting, drawing, equality, and cloning.
 */
public class DialBackgroundTest {

    // Test Constants
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private static final Color TEST_COLOR = Color.BLUE;
    private static final int TEST_IMAGE_WIDTH = 100;
    private static final int TEST_IMAGE_HEIGHT = 100;

    // ========== Constructor Tests ==========

    @Test
    public void testDefaultConstructor_SetsWhiteBackground() {
        DialBackground background = new DialBackground();
        
        assertEquals("Default background should be white", 
                     Color.WHITE, background.getPaint());
        assertNotNull("Gradient paint transformer should not be null", 
                      background.getGradientPaintTransformer());
    }

    @Test
    public void testConstructorWithPaint_SetsSpecifiedColor() {
        DialBackground background = new DialBackground(TEST_COLOR);
        
        assertEquals("Background should use specified color", 
                     TEST_COLOR, background.getPaint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullPaint_ThrowsException() {
        new DialBackground(null);
    }

    // ========== Paint Property Tests ==========

    @Test
    public void testSetPaint_UpdatesBackgroundColor() {
        DialBackground background = new DialBackground();
        
        background.setPaint(TEST_COLOR);
        
        assertEquals("Paint should be updated to new color", 
                     TEST_COLOR, background.getPaint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPaint_WithNull_ThrowsException() {
        DialBackground background = new DialBackground();
        
        background.setPaint(null);
    }

    @Test
    public void testGetPaint_ReturnsCurrentPaint() {
        DialBackground background = new DialBackground(TEST_COLOR);
        
        Paint retrievedPaint = background.getPaint();
        
        assertEquals("Retrieved paint should match set paint", 
                     TEST_COLOR, retrievedPaint);
    }

    // ========== Gradient Paint Transformer Tests ==========

    @Test
    public void testGetGradientPaintTransformer_ReturnsDefaultTransformer() {
        DialBackground background = new DialBackground();
        
        GradientPaintTransformer transformer = background.getGradientPaintTransformer();
        
        assertNotNull("Transformer should not be null", transformer);
        assertTrue("Should be StandardGradientPaintTransformer", 
                   transformer instanceof StandardGradientPaintTransformer);
    }

    @Test
    public void testSetGradientPaintTransformer_UpdatesTransformer() {
        DialBackground background = new DialBackground();
        StandardGradientPaintTransformer newTransformer = 
            new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_HORIZONTAL);
        
        background.setGradientPaintTransformer(newTransformer);
        
        assertEquals("Transformer should be updated", 
                     newTransformer, background.getGradientPaintTransformer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetGradientPaintTransformer_WithNull_ThrowsException() {
        DialBackground background = new DialBackground();
        
        background.setGradientPaintTransformer(null);
    }

    // ========== Drawing Tests ==========

    @Test
    public void testDraw_WithValidParameters_CompletesSuccessfully() {
        DialBackground background = new DialBackground();
        Graphics2D graphics = createTestGraphics();
        DialPlot plot = createTestDialPlot();
        Rectangle frame = new Rectangle(10, 10, 100, 100);
        Rectangle2D view = new Rectangle2D.Double(0, 0, 100, 100);
        
        // Should complete without throwing exception
        background.draw(graphics, plot, frame, view);
        
        // Verify basic properties remain unchanged
        assertEquals("Paint should remain unchanged after drawing", 
                     DEFAULT_BACKGROUND_COLOR, background.getPaint());
    }

    @Test(expected = NullPointerException.class)
    public void testDraw_WithNullGraphics_ThrowsException() {
        DialBackground background = new DialBackground();
        DialPlot plot = createTestDialPlot();
        
        background.draw(null, plot, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testDraw_WithGradientPaintAndNullGraphics_ThrowsException() {
        DialBackground background = new DialBackground();
        GradientPaint gradientPaint = new GradientPaint(0f, 0f, Color.RED, 100f, 100f, Color.BLUE);
        background.setPaint(gradientPaint);
        DialPlot plot = createTestDialPlot();
        Rectangle2D view = new Rectangle2D.Float(0, 0, 100, 100);
        
        background.draw(null, plot, null, view);
    }

    // ========== Window Clipping Tests ==========

    @Test
    public void testIsClippedToWindow_ReturnsTrue() {
        DialBackground background = new DialBackground();
        
        assertTrue("Background should be clipped to window", 
                   background.isClippedToWindow());
    }

    // ========== Equality Tests ==========

    @Test
    public void testEquals_SameInstance_ReturnsTrue() {
        DialBackground background = new DialBackground();
        
        assertTrue("Instance should equal itself", 
                   background.equals(background));
    }

    @Test
    public void testEquals_TwoDefaultInstances_ReturnsTrue() {
        DialBackground background1 = new DialBackground();
        DialBackground background2 = new DialBackground();
        
        assertTrue("Two default instances should be equal", 
                   background1.equals(background2));
        assertTrue("Equality should be symmetric", 
                   background2.equals(background1));
    }

    @Test
    public void testEquals_DifferentPaint_ReturnsFalse() {
        DialBackground background1 = new DialBackground(Color.WHITE);
        DialBackground background2 = new DialBackground(Color.BLUE);
        
        assertFalse("Backgrounds with different paints should not be equal", 
                    background1.equals(background2));
    }

    @Test
    public void testEquals_DifferentGradientTransformer_ReturnsFalse() {
        DialBackground background1 = new DialBackground();
        DialBackground background2 = new DialBackground();
        
        StandardGradientPaintTransformer differentTransformer = 
            new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL);
        background2.setGradientPaintTransformer(differentTransformer);
        
        assertFalse("Backgrounds with different transformers should not be equal", 
                    background1.equals(background2));
    }

    @Test
    public void testEquals_WithDifferentClass_ReturnsFalse() {
        DialBackground background = new DialBackground();
        StandardGradientPaintTransformer otherObject = 
            new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_HORIZONTAL);
        
        assertFalse("Background should not equal different class instance", 
                    background.equals(otherObject));
    }

    @Test
    public void testEquals_WithNull_ReturnsFalse() {
        DialBackground background = new DialBackground();
        
        assertFalse("Background should not equal null", 
                    background.equals(null));
    }

    // ========== Hash Code Tests ==========

    @Test
    public void testHashCode_ConsistentResults() {
        DialBackground background = new DialBackground();
        
        int hashCode1 = background.hashCode();
        int hashCode2 = background.hashCode();
        
        assertEquals("Hash code should be consistent", hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_EqualObjectsHaveSameHashCode() {
        DialBackground background1 = new DialBackground();
        DialBackground background2 = new DialBackground();
        
        assertEquals("Equal objects should have same hash code", 
                     background1.hashCode(), background2.hashCode());
    }

    // ========== Cloning Tests ==========

    @Test
    public void testClone_CreatesIndependentCopy() throws CloneNotSupportedException {
        DialBackground original = new DialBackground(TEST_COLOR);
        
        DialBackground clone = (DialBackground) original.clone();
        
        assertNotSame("Clone should be different instance", original, clone);
        assertEquals("Clone should be equal to original", original, clone);
        assertEquals("Clone should have same paint", original.getPaint(), clone.getPaint());
    }

    // ========== Helper Methods ==========

    /**
     * Creates a Graphics2D instance for testing drawing operations.
     */
    private Graphics2D createTestGraphics() {
        BufferedImage image = new BufferedImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, 
                                                BufferedImage.TYPE_INT_RGB);
        return image.createGraphics();
    }

    /**
     * Creates a basic DialPlot for testing.
     */
    private DialPlot createTestDialPlot() {
        DefaultValueDataset dataset = new DefaultValueDataset(0.5);
        return new DialPlot(dataset);
    }
}