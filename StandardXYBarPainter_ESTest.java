package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;

/**
 * Test suite for StandardXYBarPainter functionality.
 * Tests bar painting and shadow painting with various configurations.
 */
public class StandardXYBarPainterTest {

    private static final int SERIES_INDEX = 0;
    private static final int ITEM_INDEX = 0;
    
    // Helper method to create a valid Graphics2D context for testing
    private Graphics2D createTestGraphics() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        return image.createGraphics();
    }

    // Helper method to create a simple rectangular bar shape
    private Rectangle2D createSimpleBar() {
        return new Rectangle2D.Double(10, 10, 50, 30);
    }

    @Test
    public void testPaintBarShadow_WithValidGraphicsAndRenderer_ShouldExecuteSuccessfully() {
        // Given
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Graphics2D graphics = createTestGraphics();
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        renderer.setShadowYOffset(10.0); // Set visible shadow offset
        Arc2D.Float barShape = new Arc2D.Float(Arc2D.PIE);
        RectangleEdge baseEdge = RectangleEdge.LEFT;
        
        // When & Then - Should not throw exception
        painter.paintBarShadow(graphics, renderer, SERIES_INDEX, ITEM_INDEX, 
                             barShape, baseEdge, false);
        
        // Verify renderer state is maintained
        assertTrue("Renderer should maintain default series visibility", 
                  renderer.getDefaultSeriesVisible());
    }

    @Test(expected = NullPointerException.class)
    public void testPaintBarShadow_WithNullGraphics_ShouldThrowNullPointerException() {
        // Given
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D barShape = createSimpleBar();
        
        // When
        painter.paintBarShadow(null, renderer, SERIES_INDEX, ITEM_INDEX, 
                             barShape, RectangleEdge.LEFT, false);
        
        // Then - Exception should be thrown
    }

    @Test(expected = NullPointerException.class)
    public void testPaintBarShadow_WithNullGraphicsAndBottomEdge_ShouldThrowNullPointerException() {
        // Given
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        RoundRectangle2D.Double complexBar = new RoundRectangle2D.Double(
            100.0, 100.0, 200, 50, 10.0, 20);
        Rectangle2D barBounds = complexBar.getBounds2D();
        
        // When
        painter.paintBarShadow(null, renderer, SERIES_INDEX, ITEM_INDEX, 
                             barBounds, RectangleEdge.BOTTOM, false);
        
        // Then - Exception should be thrown
    }

    @Test(expected = NullPointerException.class)
    public void testPaintBarShadow_WithZeroShadowOffset_ShouldStillThrowWithNullGraphics() {
        // Given
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        renderer.setShadowXOffset(0.0f); // No horizontal shadow
        Rectangle2D.Float barShape = new Rectangle2D.Float();
        
        // When
        painter.paintBarShadow(null, renderer, -1, 1, 
                             barShape, RectangleEdge.BOTTOM, false);
        
        // Then - Exception should be thrown even with zero offset
    }

    @Test
    public void testPaintBarShadow_WithNullEdgeParameter_ShouldExecuteSuccessfully() {
        // Given
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Graphics2D graphics = createTestGraphics();
        StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.0f);
        Rectangle2D.Double barShape = new Rectangle2D.Double();
        
        // When & Then - Should handle null edge gracefully
        painter.paintBarShadow(graphics, renderer, 100, -5, 
                             barShape, null, false);
        
        assertEquals("Renderer base should be maintained", 
                    0.0, renderer.getBase(), 0.01);
    }

    @Test
    public void testPaintBar_WithOutlineEnabled_ShouldRenderSuccessfully() {
        // Given
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Graphics2D graphics = createTestGraphics();
        StackedXYBarRenderer renderer = new StackedXYBarRenderer();
        renderer.setDrawBarOutline(true); // Enable outline drawing
        Arc2D.Float barShape = new Arc2D.Float(Arc2D.PIE);
        
        // When
        painter.paintBar(graphics, renderer, -10, 255, 
                        barShape, RectangleEdge.TOP);
        
        // Then - Verify shape properties are maintained
        assertEquals("Arc start angle should be zero", 
                    0.0f, barShape.start, 0.01f);
    }

    @Test
    public void testPaintBar_WithStackedRenderer_ShouldMaintainShapeProperties() {
        // Given
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Graphics2D graphics = createTestGraphics();
        StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.0f);
        Arc2D.Float barShape = new Arc2D.Float(Arc2D.PIE);
        
        // When
        painter.paintBar(graphics, renderer, 100, 0, 
                        barShape, RectangleEdge.TOP);
        
        // Then
        assertEquals("Arc width should be maintained", 
                    0.0f, barShape.width, 0.01f);
    }

    @Test
    public void testEquals_WithSameInstance_ShouldReturnTrue() {
        // Given
        StandardXYBarPainter painter = new StandardXYBarPainter();
        
        // When & Then
        assertTrue("Painter should equal itself", painter.equals(painter));
    }

    @Test
    public void testEquals_WithTwoNewInstances_ShouldReturnTrue() {
        // Given
        StandardXYBarPainter painter1 = new StandardXYBarPainter();
        StandardXYBarPainter painter2 = new StandardXYBarPainter();
        
        // When & Then
        assertTrue("Two new painters should be equal", painter1.equals(painter2));
    }

    @Test
    public void testEquals_WithDifferentClass_ShouldReturnFalse() {
        // Given
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Arc2D.Float differentObject = new Arc2D.Float(Arc2D.PIE);
        
        // When & Then
        assertFalse("Painter should not equal different class", 
                   painter.equals(differentObject));
    }

    @Test
    public void testHashCode_ShouldExecuteWithoutException() {
        // Given
        StandardXYBarPainter painter = new StandardXYBarPainter();
        
        // When & Then - Should not throw exception
        int hashCode = painter.hashCode();
        
        // Hash code should be consistent
        assertEquals("Hash code should be consistent", 
                    hashCode, painter.hashCode());
    }

    @Test
    public void testPaintBarShadow_WithPegShadowEnabled_ShouldHandleAllEdges() {
        // Given
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Graphics2D graphics = createTestGraphics();
        Rectangle barShape = new Rectangle(20, 20, 60, 40);
        
        // Test with different edge orientations and peg shadow enabled
        RectangleEdge[] edges = {
            RectangleEdge.RIGHT, RectangleEdge.LEFT, 
            RectangleEdge.TOP, RectangleEdge.BOTTOM
        };
        
        for (RectangleEdge edge : edges) {
            XYBarRenderer renderer = new XYBarRenderer();
            
            // When & Then - Should handle all edges with peg shadow
            try {
                painter.paintBarShadow(graphics, renderer, SERIES_INDEX, ITEM_INDEX, 
                                     barShape, edge, true);
                // Test passes if no exception is thrown
            } catch (NullPointerException e) {
                fail("Should handle edge " + edge + " without null pointer exception when graphics is valid");
            }
        }
    }
}