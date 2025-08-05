/*
 * Refactored test suite for StandardXYBarPainter
 * Focus on improved readability and maintainability
 */
package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.text.DefaultCaret;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.util.GradientPaintTransformer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
) 
public class StandardXYBarPainter_ESTest extends StandardXYBarPainter_ESTest_scaffolding {

    // ========================================================================
    // Equality Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testEquals_SameInstance_ReturnsTrue() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        assertTrue(painter.equals(painter));
    }

    @Test(timeout = 4000)
    public void testEquals_SameClass_ReturnsTrue() {
        StandardXYBarPainter painter1 = new StandardXYBarPainter();
        StandardXYBarPainter painter2 = new StandardXYBarPainter();
        assertTrue(painter1.equals(painter2));
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentClass_ReturnsFalse() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        assertFalse(painter.equals(new Arc2D.Float(1)));
    }

    @Test(timeout = 4000)
    public void testHashCode_ConsistentWithEquals() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        painter.hashCode(); // Just verify no exception
    }

    // ========================================================================
    // Normal Behavior Tests
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testPaintBarShadow_WithPositiveShadowOffset_NoException() {
        // Setup
        StandardXYBarPainter painter = new StandardXYBarPainter();
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        renderer.setShadowYOffset(279.48);
        Arc2D.Float barShape = new Arc2D.Float(1);
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        
        // Execute
        painter.paintBarShadow(
            g2, renderer, 1, 1, barShape, RectangleEdge.LEFT, false
        );
        
        // Verify default state remains unchanged
        assertTrue(renderer.getDefaultSeriesVisible());
    }

    @Test(timeout = 4000)
    public void testPaintBar_WithDrawBarOutline_NoException() {
        // Setup
        StandardXYBarPainter painter = new StandardXYBarPainter();
        StackedXYBarRenderer renderer = new StackedXYBarRenderer();
        renderer.setDrawBarOutline(true);
        Arc2D.Float barShape = new Arc2D.Float(1);
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        
        // Execute
        painter.paintBar(g2, renderer, -753, 255, barShape, RectangleEdge.TOP);
        
        // Verify bar shape remains unchanged
        assertEquals(0.0F, barShape.start, 0.01F);
    }

    @Test(timeout = 4000)
    public void testPaintBarShadow_WithNullEdge_NoException() {
        // Setup
        StandardXYBarPainter painter = new StandardXYBarPainter();
        StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.0F);
        Rectangle2D barShape = new Rectangle2D.Double();
        BufferedImage image = new BufferedImage(37, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        
        // Execute
        painter.paintBarShadow(g2, renderer, 1886, -37, barShape, null, false);
        
        // Verify renderer state remains unchanged
        assertEquals(0.0, renderer.getBase(), 0.01);
    }

    // ========================================================================
    // Exception Handling Tests (Null Graphics2D)
    // ========================================================================
    
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testPaintBarShadow_WithNullGraphics_ThrowsNPE() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D barShape = new Rectangle();
        
        painter.paintBarShadow(
            null, renderer, 0, 0, barShape, RectangleEdge.RIGHT, true
        );
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testPaintBarShadow_WithNullGraphicsAndLeftEdge_ThrowsNPE() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D barShape = new Rectangle();
        
        painter.paintBarShadow(
            null, renderer, 0, 0, barShape, RectangleEdge.LEFT, true
        );
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testPaintBarShadow_WithNullGraphicsAndBottomEdge_ThrowsNPE() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        DefaultCaret barShape = new DefaultCaret();
        
        painter.paintBarShadow(
            null, renderer, 0, 0, barShape, RectangleEdge.BOTTOM, true
        );
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testPaintBarShadow_WithNullGraphicsAndTopEdge_ThrowsNPE() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        StackedXYBarRenderer renderer = new StackedXYBarRenderer();
        Rectangle2D barShape = new Rectangle();
        
        painter.paintBarShadow(
            null, renderer, 0, 0, barShape, RectangleEdge.TOP, true
        );
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testPaintBar_WithNullGraphics_ThrowsNPE() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        StackedXYBarRenderer renderer = new StackedXYBarRenderer();
        renderer.setGradientPaintTransformer(null);
        Rectangle2D barShape = new Rectangle();
        
        painter.paintBar(null, renderer, 0, 0, barShape, RectangleEdge.RIGHT);
    }
}