package org.jfree.chart.renderer.category;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;

/**
 * Test suite for StandardBarPainter class.
 * Tests the core functionality of painting bars and bar shadows.
 */
public class StandardBarPainter_ESTest {

    private StandardBarPainter painter;
    private BarRenderer renderer;
    private Graphics2D graphics;
    private Rectangle2D barShape;
    
    @Before
    public void setUp() {
        painter = new StandardBarPainter();
        renderer = new BarRenderer();
        
        // Create a minimal graphics context for testing
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        graphics = image.createGraphics();
        
        // Create a standard bar shape
        barShape = new Rectangle2D.Double(10, 10, 50, 30);
    }

    // === Null Parameter Tests ===
    
    @Test(expected = NullPointerException.class)
    public void testPaintBarWithNullGraphics() {
        painter.paintBar(null, renderer, 0, 0, barShape, RectangleEdge.BOTTOM);
    }
    
    @Test(expected = NullPointerException.class)
    public void testPaintBarShadowWithNullGraphics() {
        painter.paintBarShadow(null, renderer, 0, 0, barShape, RectangleEdge.BOTTOM, false);
    }
    
    @Test(expected = NullPointerException.class)
    public void testPaintBarWithNullShape() {
        painter.paintBar(graphics, renderer, 0, 0, null, RectangleEdge.BOTTOM);
    }

    // === Successful Painting Tests ===
    
    @Test
    public void testPaintBarSuccessfully() {
        // Should complete without throwing exceptions
        painter.paintBar(graphics, renderer, 0, 0, barShape, RectangleEdge.BOTTOM);
        // Test passes if no exception is thrown
    }
    
    @Test
    public void testPaintBarShadowWithoutPegging() {
        painter.paintBarShadow(graphics, renderer, 0, 0, barShape, RectangleEdge.BOTTOM, false);
        // Test passes if no exception is thrown
    }
    
    @Test
    public void testPaintBarShadowWithPegging() {
        painter.paintBarShadow(graphics, renderer, 0, 0, barShape, RectangleEdge.BOTTOM, true);
        // Test passes if no exception is thrown
    }

    // === Different Rectangle Edges Tests ===
    
    @Test
    public void testPaintBarWithDifferentEdges() {
        // Test all rectangle edges to ensure they're handled properly
        RectangleEdge[] edges = {
            RectangleEdge.TOP, 
            RectangleEdge.BOTTOM, 
            RectangleEdge.LEFT, 
            RectangleEdge.RIGHT
        };
        
        for (RectangleEdge edge : edges) {
            painter.paintBar(graphics, renderer, 0, 0, barShape, edge);
            painter.paintBarShadow(graphics, renderer, 0, 0, barShape, edge, false);
        }
    }

    // === Shadow Configuration Tests ===
    
    @Test
    public void testPaintBarShadowWithZeroOffset() {
        renderer.setShadowXOffset(0.0);
        renderer.setShadowYOffset(0.0);
        
        painter.paintBarShadow(graphics, renderer, 0, 0, barShape, RectangleEdge.BOTTOM, false);
        // Test passes if no exception is thrown
    }
    
    @Test
    public void testPaintBarShadowWithNegativeOffsets() {
        renderer.setShadowXOffset(-5.0);
        renderer.setShadowYOffset(-3.0);
        
        painter.paintBarShadow(graphics, renderer, 0, 0, barShape, RectangleEdge.BOTTOM, false);
        // Test passes if no exception is thrown
    }

    // === Renderer Configuration Tests ===
    
    @Test
    public void testPaintBarWithOutlineEnabled() {
        renderer.setDrawBarOutline(true);
        
        painter.paintBar(graphics, renderer, 0, 0, barShape, RectangleEdge.BOTTOM);
        // Test passes if no exception is thrown
    }
    
    @Test
    public void testPaintBarWithShadowsVisible() {
        renderer.setShadowsVisible(true);
        
        painter.paintBar(graphics, renderer, 0, 0, barShape, RectangleEdge.BOTTOM);
        assertTrue("Shadows should be visible", renderer.getShadowsVisible());
    }

    // === Equality and Hash Code Tests ===
    
    @Test
    public void testEqualsWithSameInstance() {
        assertTrue("Same instance should be equal to itself", painter.equals(painter));
    }
    
    @Test
    public void testEqualsWithDifferentInstances() {
        StandardBarPainter otherPainter = new StandardBarPainter();
        assertTrue("Different instances should be equal", painter.equals(otherPainter));
    }
    
    @Test
    public void testEqualsWithNull() {
        assertFalse("Should not be equal to null", painter.equals(null));
    }
    
    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse("Should not be equal to different class", painter.equals("not a painter"));
    }
    
    @Test
    public void testHashCodeConsistency() {
        StandardBarPainter painter1 = new StandardBarPainter();
        StandardBarPainter painter2 = new StandardBarPainter();
        
        assertEquals("Equal objects should have equal hash codes", 
                    painter1.hashCode(), painter2.hashCode());
    }

    // === Edge Case Tests ===
    
    @Test
    public void testPaintBarWithZeroSizeShape() {
        Rectangle2D zeroSizeBar = new Rectangle2D.Double(0, 0, 0, 0);
        
        painter.paintBar(graphics, renderer, 0, 0, zeroSizeBar, RectangleEdge.BOTTOM);
        // Test passes if no exception is thrown
    }
    
    @Test
    public void testPaintBarWithNegativeIndices() {
        painter.paintBar(graphics, renderer, -1, -1, barShape, RectangleEdge.BOTTOM);
        // Test passes if no exception is thrown
    }
}