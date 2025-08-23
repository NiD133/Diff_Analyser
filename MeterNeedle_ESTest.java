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
import javax.swing.JScrollPane;
import javax.swing.text.DefaultCaret;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class MeterNeedle_ESTest extends MeterNeedle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testPlumNeedleRotation() throws Throwable {
        PlumNeedle plumNeedle = new PlumNeedle();
        plumNeedle.setRotateY(-2433.34177);
        assertEquals(-2433.34177, plumNeedle.getRotateY(), 0.01);
        assertEquals(0.5, plumNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testShipNeedleRotateX() throws Throwable {
        ShipNeedle shipNeedle = new ShipNeedle();
        assertEquals(0.5, shipNeedle.getRotateX(), 0.01);
        shipNeedle.setRotateX(-1.0);
        assertEquals(5, shipNeedle.getSize());
    }

    @Test(timeout = 4000)
    public void testShipNeedleSize() throws Throwable {
        ShipNeedle shipNeedle = new ShipNeedle();
        shipNeedle.setSize(497);
        assertEquals(497, shipNeedle.getSize());
    }

    @Test(timeout = 4000)
    public void testLongNeedleDrawing() throws Throwable {
        LongNeedle longNeedle = new LongNeedle();
        BufferedImage bufferedImage = new BufferedImage(9, 9, 9);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        Rectangle2D.Float rectangle2D = new Rectangle2D.Float(9, 9, 0.0F, 9);
        Rectangle rectangle = rectangle2D.getBounds();
        longNeedle.draw(graphics2D, rectangle, 0.0);
        assertEquals(5, longNeedle.getSize());
        assertEquals(0.5, longNeedle.getRotateX(), 0.01);
        assertEquals(0.8, longNeedle.getRotateY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testShipNeedleDefaultDisplay() throws Throwable {
        ShipNeedle shipNeedle = new ShipNeedle();
        Rectangle2D.Float rectangle2D = new Rectangle2D.Float();
        shipNeedle.defaultDisplay(null, rectangle2D);
        assertEquals(0.5, shipNeedle.getRotateX(), 0.01);
        assertEquals(0.5, shipNeedle.getRotateY(), 0.01);
        assertEquals(5, shipNeedle.getSize());
    }

    @Test(timeout = 4000)
    public void testMiddlePinNeedleHashCode() throws Throwable {
        MiddlePinNeedle middlePinNeedle = new MiddlePinNeedle();
        middlePinNeedle.hashCode();
        assertEquals(5, middlePinNeedle.getSize());
        assertEquals(0.5, middlePinNeedle.getRotateY(), 0.01);
        assertEquals(0.5, middlePinNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testShipNeedleNegativeSize() throws Throwable {
        ShipNeedle shipNeedle = new ShipNeedle();
        shipNeedle.setSize(-1083);
        assertEquals(-1083, shipNeedle.getSize());
    }

    @Test(timeout = 4000)
    public void testPointerNeedleRotateY() throws Throwable {
        PointerNeedle pointerNeedle = new PointerNeedle();
        assertEquals(0.5, pointerNeedle.getRotateY(), 0.01);
        pointerNeedle.setRotateY(0.0);
        assertEquals(0.0, pointerNeedle.getRotateY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testLongNeedleRotateY() throws Throwable {
        LongNeedle longNeedle = new LongNeedle();
        longNeedle.setRotateY(-590.9257035);
        assertEquals(-590.9257035, longNeedle.getRotateY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testArrowNeedleRotateX() throws Throwable {
        ArrowNeedle arrowNeedle = new ArrowNeedle(false);
        arrowNeedle.setRotateX(-9.0);
        assertEquals(-9.0, arrowNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testPlumNeedleOutlinePaint() throws Throwable {
        PlumNeedle plumNeedle = new PlumNeedle();
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        Color color = renderer.getTextNonSelectionColor();
        plumNeedle.setOutlinePaint(color);
        Paint paint = plumNeedle.getOutlinePaint();
        assertEquals(0.5, plumNeedle.getRotateY(), 0.01);
        assertNotNull(paint);
        assertEquals(5, plumNeedle.getSize());
        assertEquals(0.5, plumNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testPlumNeedleHighlightPaint() throws Throwable {
        PlumNeedle plumNeedle = new PlumNeedle();
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        Color color = renderer.getBackgroundNonSelectionColor();
        plumNeedle.setHighlightPaint(color);
        Paint paint = plumNeedle.getHighlightPaint();
        assertEquals(5, plumNeedle.getSize());
        assertEquals(0.5, plumNeedle.getRotateY(), 0.01);
        assertEquals(0.5, plumNeedle.getRotateX(), 0.01);
        assertNotNull(paint);
    }

    @Test(timeout = 4000)
    public void testWindNeedleFillPaint() throws Throwable {
        WindNeedle windNeedle = new WindNeedle();
        Color color = Color.blue;
        windNeedle.setFillPaint(color);
        Paint paint = windNeedle.getFillPaint();
        assertEquals(0.5, windNeedle.getRotateX(), 0.01);
        assertEquals(0.5, windNeedle.getRotateY(), 0.01);
        assertEquals(5, windNeedle.getSize());
        assertNotNull(paint);
    }

    @Test(timeout = 4000)
    public void testWindNeedleEqualsItself() throws Throwable {
        WindNeedle windNeedle = new WindNeedle();
        assertTrue(windNeedle.equals(windNeedle));
        assertEquals(0.5, windNeedle.getRotateY(), 0.01);
        assertEquals(5, windNeedle.getSize());
        assertEquals(0.5, windNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testWindNeedleEqualsPlumNeedle() throws Throwable {
        WindNeedle windNeedle = new WindNeedle();
        PlumNeedle plumNeedle = new PlumNeedle();
        assertFalse(windNeedle.equals(plumNeedle));
        assertEquals(0.5, plumNeedle.getRotateY(), 0.01);
        assertEquals(5, plumNeedle.getSize());
        assertEquals(0.5, plumNeedle.getRotateX(), 0.01);
        assertEquals(0.5, windNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testMiddlePinNeedleDrawException() throws Throwable {
        Rectangle2D.Float rectangle2D = new Rectangle2D.Float();
        MiddlePinNeedle middlePinNeedle = new MiddlePinNeedle();
        Point2D.Float point2D = new Point2D.Float(0.0F, 0.0F);
        try {
            middlePinNeedle.draw(null, rectangle2D, point2D, -1.0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.plot.compass.MeterNeedle", e);
        }
    }

    @Test(timeout = 4000)
    public void testPointerNeedleDrawException() throws Throwable {
        PointerNeedle pointerNeedle = new PointerNeedle();
        Rectangle2D.Float rectangle2D = new Rectangle2D.Float();
        try {
            pointerNeedle.draw(null, rectangle2D);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.plot.compass.MeterNeedle", e);
        }
    }

    @Test(timeout = 4000)
    public void testPointerNeedleOutlinePaint() throws Throwable {
        PointerNeedle pointerNeedle = new PointerNeedle();
        pointerNeedle.getOutlinePaint();
        assertEquals(0.5, pointerNeedle.getRotateY(), 0.01);
        assertEquals(0.5, pointerNeedle.getRotateX(), 0.01);
        assertEquals(5, pointerNeedle.getSize());
    }

    @Test(timeout = 4000)
    public void testWindNeedleDrawException() throws Throwable {
        WindNeedle windNeedle = new WindNeedle();
        try {
            windNeedle.draw(null, null, 2350.6341);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.plot.compass.MeterNeedle", e);
        }
    }

    @Test(timeout = 4000)
    public void testPlumNeedleHighlightPaintRetrieval() throws Throwable {
        PlumNeedle plumNeedle = new PlumNeedle();
        plumNeedle.getHighlightPaint();
        assertEquals(5, plumNeedle.getSize());
        assertEquals(0.5, plumNeedle.getRotateY(), 0.01);
        assertEquals(0.5, plumNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testWindNeedleFillPaintRetrieval() throws Throwable {
        WindNeedle windNeedle = new WindNeedle();
        windNeedle.getFillPaint();
        assertEquals(0.5, windNeedle.getRotateX(), 0.01);
        assertEquals(5, windNeedle.getSize());
        assertEquals(0.5, windNeedle.getRotateY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testMiddlePinNeedleEqualsLongNeedle() throws Throwable {
        LongNeedle longNeedle = new LongNeedle();
        MiddlePinNeedle middlePinNeedle = new MiddlePinNeedle();
        assertFalse(middlePinNeedle.equals(longNeedle));
        assertEquals(0.5, middlePinNeedle.getRotateY(), 0.01);
        assertEquals(0.5, middlePinNeedle.getRotateX(), 0.01);
        assertEquals(5, middlePinNeedle.getSize());
        assertEquals(0.8, longNeedle.getRotateY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testPointerNeedleRotateX() throws Throwable {
        PointerNeedle pointerNeedle = new PointerNeedle();
        pointerNeedle.setRotateX(-3074.422);
        assertEquals(-3074.422, pointerNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testPointerNeedleHighlightPaint() throws Throwable {
        PointerNeedle pointerNeedle = new PointerNeedle();
        Color color = Color.darkGray;
        pointerNeedle.setHighlightPaint(color);
        assertEquals(5, pointerNeedle.getSize());
        assertEquals(0.5, pointerNeedle.getRotateY(), 0.01);
        assertEquals(0.5, pointerNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testShipNeedleFillPaint() throws Throwable {
        ShipNeedle shipNeedle = new ShipNeedle();
        Color color = Color.BLUE;
        shipNeedle.setFillPaint(color);
        PointerNeedle pointerNeedle = new PointerNeedle();
        assertFalse(shipNeedle.equals(pointerNeedle));
        assertEquals(5, pointerNeedle.getSize());
        assertEquals(0.5, pointerNeedle.getRotateX(), 0.01);
        assertEquals(0.5, pointerNeedle.getRotateY(), 0.01);
        assertEquals(5, shipNeedle.getSize());
    }

    @Test(timeout = 4000)
    public void testMiddlePinNeedleClone() throws Throwable {
        MiddlePinNeedle middlePinNeedle = new MiddlePinNeedle();
        MiddlePinNeedle clonedNeedle = (MiddlePinNeedle) middlePinNeedle.clone();
        assertTrue(clonedNeedle.equals(middlePinNeedle));

        BasicStroke stroke = new BasicStroke();
        clonedNeedle.setOutlineStroke(stroke);
        assertFalse(middlePinNeedle.equals(clonedNeedle));
    }

    @Test(timeout = 4000)
    public void testPointerNeedleOutlinePaint() throws Throwable {
        PointerNeedle pointerNeedle = new PointerNeedle();
        Color color = Color.MAGENTA;
        pointerNeedle.setOutlinePaint(color);
        assertEquals(5, pointerNeedle.getSize());
        assertEquals(0.5, pointerNeedle.getRotateY(), 0.01);
        assertEquals(0.5, pointerNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testShipNeedleEqualsAttribute() throws Throwable {
        ShipNeedle shipNeedle = new ShipNeedle();
        assertFalse(shipNeedle.equals(AttributedCharacterIterator.Attribute.LANGUAGE));
        assertEquals(0.5, shipNeedle.getRotateX(), 0.01);
        assertEquals(0.5, shipNeedle.getRotateY(), 0.01);
        assertEquals(5, shipNeedle.getSize());
    }

    @Test(timeout = 4000)
    public void testShipNeedleEqualsPointerNeedle() throws Throwable {
        ShipNeedle shipNeedle = new ShipNeedle();
        PointerNeedle pointerNeedle = new PointerNeedle();
        assertFalse(shipNeedle.equals(pointerNeedle));
        assertEquals(5, pointerNeedle.getSize());
        assertEquals(0.5, pointerNeedle.getRotateY(), 0.01);
        assertEquals(0.5, pointerNeedle.getRotateX(), 0.01);
        assertEquals(0.5, shipNeedle.getRotateY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testPointerNeedleHighlightPaintNull() throws Throwable {
        PointerNeedle pointerNeedle = new PointerNeedle();
        pointerNeedle.setHighlightPaint(null);
        assertEquals(5, pointerNeedle.getSize());
        assertEquals(0.5, pointerNeedle.getRotateY(), 0.01);
        assertEquals(0.5, pointerNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testWindNeedleDefaultDisplayException() throws Throwable {
        SystemColor systemColor = SystemColor.info;
        WindNeedle windNeedle = new WindNeedle();
        windNeedle.setFillPaint(systemColor);
        DefaultCaret caret = new DefaultCaret();
        try {
            windNeedle.defaultDisplay(null, caret);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.plot.compass.MeterNeedle", e);
        }
    }

    @Test(timeout = 4000)
    public void testPointerNeedleFillPaintNull() throws Throwable {
        PointerNeedle pointerNeedle = new PointerNeedle();
        pointerNeedle.setFillPaint(null);
        assertEquals(5, pointerNeedle.getSize());
        assertEquals(0.5, pointerNeedle.getRotateY(), 0.01);
        assertEquals(0.5, pointerNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testShipNeedleOutlineStrokeNull() throws Throwable {
        ShipNeedle shipNeedle = new ShipNeedle();
        shipNeedle.setOutlineStroke(null);
        assertEquals(5, shipNeedle.getSize());
        assertEquals(0.5, shipNeedle.getRotateY(), 0.01);
        assertEquals(0.5, shipNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testWindNeedleDefaultDisplayOutlinePaint() throws Throwable {
        SystemColor systemColor = SystemColor.windowText;
        WindNeedle windNeedle = new WindNeedle();
        windNeedle.setOutlinePaint(systemColor);
        DefaultCaret caret = new DefaultCaret();
        try {
            windNeedle.defaultDisplay(null, caret);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.plot.compass.MeterNeedle", e);
        }
    }

    @Test(timeout = 4000)
    public void testShipNeedleOutlinePaintNull() throws Throwable {
        ShipNeedle shipNeedle = new ShipNeedle();
        shipNeedle.setOutlinePaint(null);
        assertEquals(0.5, shipNeedle.getRotateX(), 0.01);
        assertEquals(5, shipNeedle.getSize());
        assertEquals(0.5, shipNeedle.getRotateY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testShipNeedleTransform() throws Throwable {
        ShipNeedle shipNeedle = new ShipNeedle();
        shipNeedle.getTransform();
        assertEquals(0.5, shipNeedle.getRotateX(), 0.01);
        assertEquals(5, shipNeedle.getSize());
        assertEquals(0.5, shipNeedle.getRotateY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testLongNeedleDrawWithPoint() throws Throwable {
        LongNeedle longNeedle = new LongNeedle();
        BufferedImage bufferedImage = new BufferedImage(9, 9, 9);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        Rectangle2D.Float rectangle2D = new Rectangle2D.Float(9, 9, 0.0F, 9);
        Rectangle rectangle = rectangle2D.getBounds();
        longNeedle.draw(graphics2D, rectangle, null, 0.0);
        assertEquals(0.5, longNeedle.getRotateX(), 0.01);
        assertEquals(0.8, longNeedle.getRotateY(), 0.01);
        assertEquals(5, longNeedle.getSize());
    }

    @Test(timeout = 4000)
    public void testLongNeedleOutlineStroke() throws Throwable {
        LongNeedle longNeedle = new LongNeedle();
        BasicStroke stroke = (BasicStroke) longNeedle.getOutlineStroke();
        assertEquals(2.0F, stroke.getLineWidth(), 0.01F);
        assertEquals(5, longNeedle.getSize());
        assertEquals(0.5, longNeedle.getRotateX(), 0.01);
        assertEquals(0.8, longNeedle.getRotateY(), 0.01);
    }

    @Test(timeout = 4000)
    public void testShipNeedleNegativeLargeSize() throws Throwable {
        ShipNeedle shipNeedle = new ShipNeedle();
        shipNeedle.setSize(-715827883);
        PointerNeedle pointerNeedle = new PointerNeedle();
        assertFalse(shipNeedle.equals(pointerNeedle));
        assertEquals(-715827883, shipNeedle.getSize());
    }

    @Test(timeout = 4000)
    public void testShipNeedleDefaultSize() throws Throwable {
        ShipNeedle shipNeedle = new ShipNeedle();
        assertEquals(5, shipNeedle.getSize());
        assertEquals(0.5, shipNeedle.getRotateY(), 0.01);
        assertEquals(0.5, shipNeedle.getRotateX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testPinNeedleDrawing() throws Throwable {
        PinNeedle pinNeedle = new PinNeedle();
        BufferedImage bufferedImage = new BufferedImage(1, 1, 1);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        JScrollPane scrollPane = new JScrollPane();
        Rectangle rectangle = scrollPane.getViewportBorderBounds();
        pinNeedle.draw(graphics2D, rectangle);
        assertEquals(0.5, pinNeedle.getRotateX(), 0.01);
        assertEquals(0.5, pinNeedle.getRotateY(), 0.01);
        assertEquals(5, pinNeedle.getSize());
    }

    @Test(timeout = 4000)
    public void testLongNeedleRotateYValue() throws Throwable {
        LongNeedle longNeedle = new LongNeedle();
        assertEquals(0.8, longNeedle.getRotateY(), 0.01);
        assertEquals(0.5, longNeedle.getRotateX(), 0.01);
        assertEquals(5, longNeedle.getSize());
    }

    @Test(timeout = 4000)
    public void testPointerNeedleRotateXValue() throws Throwable {
        PointerNeedle pointerNeedle = new PointerNeedle();
        assertEquals(0.5, pointerNeedle.getRotateX(), 0.01);
        assertEquals(5, pointerNeedle.getSize());
        assertEquals(0.5, pointerNeedle.getRotateY(), 0.01);
    }
}