package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.jfree.chart.plot.compass.*;

/**
 * Test suite for MeterNeedle and its concrete implementations.
 * Tests core functionality including property management, drawing operations,
 * equality comparisons, and error handling.
 */
public class MeterNeedleTest {

    // Test Constants
    private static final double DELTA = 0.01;
    private static final double DEFAULT_ROTATE_X = 0.5;
    private static final double DEFAULT_ROTATE_Y = 0.5;
    private static final int DEFAULT_SIZE = 5;

    // ========== Property Management Tests ==========

    @Test
    public void testSetAndGetRotateX() {
        ArrowNeedle needle = new ArrowNeedle(false);
        double newRotateX = -9.0;
        
        needle.setRotateX(newRotateX);
        
        assertEquals("RotateX should be set correctly", newRotateX, needle.getRotateX(), DELTA);
    }

    @Test
    public void testSetAndGetRotateY() {
        PointerNeedle needle = new PointerNeedle();
        double newRotateY = 0.0;
        
        needle.setRotateY(newRotateY);
        
        assertEquals("RotateY should be set correctly", newRotateY, needle.getRotateY(), DELTA);
    }

    @Test
    public void testSetAndGetSize() {
        ShipNeedle needle = new ShipNeedle();
        int newSize = -1083;
        
        needle.setSize(newSize);
        
        assertEquals("Size should be set correctly", newSize, needle.getSize());
    }

    @Test
    public void testSetAndGetOutlinePaint() {
        PlumNeedle needle = new PlumNeedle();
        Color expectedColor = Color.BLACK;
        
        needle.setOutlinePaint(expectedColor);
        Paint actualPaint = needle.getOutlinePaint();
        
        assertNotNull("Outline paint should not be null", actualPaint);
        assertEquals("Outline paint should match expected color", expectedColor, actualPaint);
    }

    @Test
    public void testSetAndGetHighlightPaint() {
        PlumNeedle needle = new PlumNeedle();
        Color expectedColor = Color.WHITE;
        
        needle.setHighlightPaint(expectedColor);
        Paint actualPaint = needle.getHighlightPaint();
        
        assertNotNull("Highlight paint should not be null", actualPaint);
        assertEquals("Highlight paint should match expected color", expectedColor, actualPaint);
    }

    @Test
    public void testSetAndGetFillPaint() {
        WindNeedle needle = new WindNeedle();
        Color expectedColor = Color.BLUE;
        
        needle.setFillPaint(expectedColor);
        Paint actualPaint = needle.getFillPaint();
        
        assertNotNull("Fill paint should not be null", actualPaint);
        assertEquals("Fill paint should match expected color", expectedColor, actualPaint);
    }

    @Test
    public void testGetOutlineStroke() {
        LongNeedle needle = new LongNeedle();
        
        BasicStroke stroke = (BasicStroke) needle.getOutlineStroke();
        
        assertNotNull("Outline stroke should not be null", stroke);
        assertEquals("Default stroke width should be 2.0", 2.0F, stroke.getLineWidth(), 0.01F);
    }

    // ========== Default Values Tests ==========

    @Test
    public void testDefaultValues() {
        MiddlePinNeedle needle = new MiddlePinNeedle();
        
        assertEquals("Default size should be 5", DEFAULT_SIZE, needle.getSize());
        assertEquals("Default rotateX should be 0.5", DEFAULT_ROTATE_X, needle.getRotateX(), DELTA);
        assertEquals("Default rotateY should be 0.5", DEFAULT_ROTATE_Y, needle.getRotateY(), DELTA);
    }

    @Test
    public void testLongNeedleDefaultRotateY() {
        LongNeedle needle = new LongNeedle();
        
        assertEquals("LongNeedle should have default rotateY of 0.8", 0.8, needle.getRotateY(), DELTA);
        assertEquals("LongNeedle should have default rotateX of 0.5", DEFAULT_ROTATE_X, needle.getRotateX(), DELTA);
    }

    // ========== Drawing Operations Tests ==========

    @Test
    public void testDrawWithValidGraphicsAndRectangle() {
        LongNeedle needle = new LongNeedle();
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        Rectangle2D plotArea = new Rectangle(0, 0, 10, 10);
        
        // Should not throw exception
        needle.draw(graphics, plotArea, 0.0);
        
        graphics.dispose();
    }

    @Test
    public void testDrawWithNullPoint() {
        LongNeedle needle = new LongNeedle();
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        Rectangle2D plotArea = new Rectangle(0, 0, 10, 10);
        
        // Should handle null point gracefully
        needle.draw(graphics, plotArea, null, 0.0);
        
        graphics.dispose();
    }

    @Test
    public void testDrawWithDefaultDisplay() {
        ShipNeedle needle = new ShipNeedle();
        Rectangle2D.Float plotArea = new Rectangle2D.Float(0, 0, 100, 100);
        
        // Should not throw exception even with null graphics
        needle.defaultDisplay(null, plotArea);
    }

    // ========== Error Handling Tests ==========

    @Test(expected = NullPointerException.class)
    public void testDrawWithNullGraphics() {
        PointerNeedle needle = new PointerNeedle();
        Rectangle2D plotArea = new Rectangle2D.Float(0, 0, 100, 100);
        
        needle.draw(null, plotArea);
    }

    @Test(expected = NullPointerException.class)
    public void testDrawWithNullGraphicsAndAngle() {
        WindNeedle needle = new WindNeedle();
        
        needle.draw(null, null, 2350.6341);
    }

    @Test(expected = NullPointerException.class)
    public void testDrawWithNullGraphicsAndPoint() {
        MiddlePinNeedle needle = new MiddlePinNeedle();
        Rectangle2D plotArea = new Rectangle2D.Float(0, 0, 100, 100);
        Point2D rotationPoint = new Point2D.Float(0.0F, 0.0F);
        
        needle.draw(null, plotArea, rotationPoint, -1.0);
    }

    // ========== Equality Tests ==========

    @Test
    public void testEqualsSameInstance() {
        WindNeedle needle = new WindNeedle();
        
        assertTrue("Needle should equal itself", needle.equals(needle));
    }

    @Test
    public void testEqualsDifferentNeedleTypes() {
        WindNeedle windNeedle = new WindNeedle();
        PlumNeedle plumNeedle = new PlumNeedle();
        
        assertFalse("Different needle types should not be equal", windNeedle.equals(plumNeedle));
    }

    @Test
    public void testEqualsWithDifferentRotateX() {
        ShipNeedle needle1 = new ShipNeedle();
        PointerNeedle needle2 = new PointerNeedle();
        needle2.setRotateX(-3074.422);
        
        assertFalse("Needles with different rotateX should not be equal", needle1.equals(needle2));
    }

    @Test
    public void testEqualsWithDifferentRotateY() {
        PlumNeedle needle1 = new PlumNeedle();
        needle1.setRotateY(-2433.34177);
        ShipNeedle needle2 = new ShipNeedle();
        
        assertFalse("Needles with different rotateY should not be equal", needle2.equals(needle1));
    }

    @Test
    public void testEqualsWithDifferentSize() {
        ShipNeedle needle1 = new ShipNeedle();
        needle1.setSize(497);
        PlumNeedle needle2 = new PlumNeedle();
        
        assertFalse("Needles with different sizes should not be equal", needle1.equals(needle2));
    }

    @Test
    public void testEqualsWithDifferentPaints() {
        ShipNeedle needle1 = new ShipNeedle();
        PointerNeedle needle2 = new PointerNeedle();
        
        // Test different highlight paint
        needle2.setHighlightPaint(Color.DARK_GRAY);
        assertFalse("Needles with different highlight paint should not be equal", needle1.equals(needle2));
        
        // Reset and test different fill paint
        needle2.setHighlightPaint(null);
        needle1.setFillPaint(Color.BLUE);
        assertFalse("Needles with different fill paint should not be equal", needle1.equals(needle2));
        
        // Reset and test different outline paint
        needle1.setFillPaint(null);
        needle2.setOutlinePaint(Color.MAGENTA);
        assertFalse("Needles with different outline paint should not be equal", needle1.equals(needle2));
    }

    @Test
    public void testEqualsWithDifferentStroke() {
        MiddlePinNeedle needle1 = new MiddlePinNeedle();
        MiddlePinNeedle needle2 = new MiddlePinNeedle();
        
        assertTrue("Initially needles should be equal", needle1.equals(needle2));
        
        needle2.setOutlineStroke(new BasicStroke(5.0f));
        assertFalse("Needles with different strokes should not be equal", needle1.equals(needle2));
    }

    @Test
    public void testEqualsWithNonNeedleObject() {
        ShipNeedle needle = new ShipNeedle();
        String notANeedle = "I am not a needle";
        
        assertFalse("Needle should not equal non-needle object", needle.equals(notANeedle));
    }

    // ========== Hash Code Tests ==========

    @Test
    public void testHashCode() {
        MiddlePinNeedle needle = new MiddlePinNeedle();
        
        // Should not throw exception and should return consistent value
        int hashCode1 = needle.hashCode();
        int hashCode2 = needle.hashCode();
        
        assertEquals("Hash code should be consistent", hashCode1, hashCode2);
    }

    // ========== Null Handling Tests ==========

    @Test
    public void testSetNullPaints() {
        PointerNeedle needle = new PointerNeedle();
        
        // Should handle null paints gracefully
        needle.setHighlightPaint(null);
        needle.setFillPaint(null);
        needle.setOutlinePaint(null);
        
        // Verify getters don't throw exceptions
        needle.getHighlightPaint();
        needle.getFillPaint();
        needle.getOutlinePaint();
    }

    @Test
    public void testSetNullStroke() {
        ShipNeedle needle = new ShipNeedle();
        
        // Should handle null stroke gracefully
        needle.setOutlineStroke(null);
        
        // Verify getter doesn't throw exception
        needle.getOutlineStroke();
    }

    // ========== Utility Method Tests ==========

    @Test
    public void testGetTransform() {
        ShipNeedle needle = new ShipNeedle();
        
        assertNotNull("Transform should not be null", needle.getTransform());
    }

    @Test
    public void testDrawWithSpecificNeedleTypes() {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        Rectangle plotArea = new Rectangle(0, 0, 10, 10);
        
        PinNeedle pinNeedle = new PinNeedle();
        pinNeedle.draw(graphics, plotArea);
        
        graphics.dispose();
    }
}