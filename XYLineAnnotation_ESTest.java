package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.PlotOrientation;

/**
 * Test suite for XYLineAnnotation class functionality.
 * Tests coordinate getters, equality, hashCode, drawing, and validation.
 */
public class XYLineAnnotationTest {

    // Test data constants
    private static final double START_X = 10.0;
    private static final double START_Y = 20.0;
    private static final double END_X = 30.0;
    private static final double END_Y = 40.0;
    
    private static final double DELTA = 0.01; // For double comparisons

    @Test
    public void testBasicConstructor() {
        XYLineAnnotation annotation = new XYLineAnnotation(START_X, START_Y, END_X, END_Y);
        
        assertEquals("X1 coordinate should match", START_X, annotation.getX1(), DELTA);
        assertEquals("Y1 coordinate should match", START_Y, annotation.getY1(), DELTA);
        assertEquals("X2 coordinate should match", END_X, annotation.getX2(), DELTA);
        assertEquals("Y2 coordinate should match", END_Y, annotation.getY2(), DELTA);
    }

    @Test
    public void testFullConstructor() {
        Stroke customStroke = new BasicStroke(2.0f);
        Paint customPaint = Color.RED;
        
        XYLineAnnotation annotation = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y, customStroke, customPaint);
        
        assertEquals("X1 coordinate should match", START_X, annotation.getX1(), DELTA);
        assertEquals("Y1 coordinate should match", START_Y, annotation.getY1(), DELTA);
        assertEquals("X2 coordinate should match", END_X, annotation.getX2(), DELTA);
        assertEquals("Y2 coordinate should match", END_Y, annotation.getY2(), DELTA);
        assertEquals("Stroke should match", customStroke, annotation.getStroke());
        assertEquals("Paint should match", customPaint, annotation.getPaint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRejectsNullStroke() {
        new XYLineAnnotation(START_X, START_Y, END_X, END_Y, null, Color.BLACK);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRejectsNullPaint() {
        new XYLineAnnotation(START_X, START_Y, END_X, END_Y, new BasicStroke(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRejectsInfiniteCoordinates() {
        new XYLineAnnotation(Double.NEGATIVE_INFINITY, START_Y, END_X, END_Y);
    }

    @Test
    public void testHashCodeConsistency() {
        XYLineAnnotation annotation = new XYLineAnnotation(START_X, START_Y, END_X, END_Y);
        
        int firstHashCode = annotation.hashCode();
        int secondHashCode = annotation.hashCode();
        
        assertEquals("Hash code should be consistent", firstHashCode, secondHashCode);
    }

    @Test
    public void testEqualsSameInstance() {
        XYLineAnnotation annotation = new XYLineAnnotation(START_X, START_Y, END_X, END_Y);
        
        assertTrue("Annotation should equal itself", annotation.equals(annotation));
    }

    @Test
    public void testEqualsIdenticalAnnotations() {
        XYLineAnnotation annotation1 = new XYLineAnnotation(START_X, START_Y, END_X, END_Y);
        XYLineAnnotation annotation2 = new XYLineAnnotation(START_X, START_Y, END_X, END_Y);
        
        assertTrue("Identical annotations should be equal", annotation1.equals(annotation2));
        assertTrue("Equality should be symmetric", annotation2.equals(annotation1));
    }

    @Test
    public void testEqualsDifferentCoordinates() {
        XYLineAnnotation annotation1 = new XYLineAnnotation(START_X, START_Y, END_X, END_Y);
        XYLineAnnotation annotation2 = new XYLineAnnotation(START_X + 1, START_Y, END_X, END_Y);
        
        assertFalse("Annotations with different X1 should not be equal", 
                   annotation1.equals(annotation2));
    }

    @Test
    public void testEqualsDifferentStrokes() {
        Stroke stroke1 = new BasicStroke(1.0f);
        Stroke stroke2 = new BasicStroke(2.0f);
        
        XYLineAnnotation annotation1 = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y, stroke1, Color.BLACK);
        XYLineAnnotation annotation2 = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y, stroke2, Color.BLACK);
        
        assertFalse("Annotations with different strokes should not be equal", 
                   annotation1.equals(annotation2));
    }

    @Test
    public void testEqualsDifferentPaints() {
        XYLineAnnotation annotation1 = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y, new BasicStroke(), Color.RED);
        XYLineAnnotation annotation2 = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y, new BasicStroke(), Color.BLUE);
        
        assertFalse("Annotations with different paints should not be equal", 
                   annotation1.equals(annotation2));
    }

    @Test
    public void testEqualsWithNonAnnotationObject() {
        XYLineAnnotation annotation = new XYLineAnnotation(START_X, START_Y, END_X, END_Y);
        String notAnAnnotation = "not an annotation";
        
        assertFalse("Annotation should not equal non-annotation object", 
                   annotation.equals(notAnAnnotation));
    }

    @Test
    public void testCloning() throws CloneNotSupportedException {
        XYLineAnnotation original = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y, new BasicStroke(2.0f), Color.RED);
        
        XYLineAnnotation clone = (XYLineAnnotation) original.clone();
        
        assertNotSame("Clone should be different instance", original, clone);
        assertEquals("Clone should be equal to original", original, clone);
        assertEquals("Clone should have same coordinates", original.getX1(), clone.getX1(), DELTA);
        assertEquals("Clone should have same coordinates", original.getY1(), clone.getY1(), DELTA);
        assertEquals("Clone should have same coordinates", original.getX2(), clone.getX2(), DELTA);
        assertEquals("Clone should have same coordinates", original.getY2(), clone.getY2(), DELTA);
    }

    @Test
    public void testDrawingWithValidPlot() {
        XYLineAnnotation annotation = new XYLineAnnotation(START_X, START_Y, END_X, END_Y);
        
        // Create minimal plot setup for drawing
        NumberAxis domainAxis = new NumberAxis("X");
        NumberAxis rangeAxis = new NumberAxis("Y");
        XYPlot plot = new XYPlot(null, domainAxis, rangeAxis, null);
        
        Rectangle dataArea = new Rectangle(0, 0, 400, 300);
        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        
        // Should not throw exception
        annotation.draw(graphics, plot, dataArea, domainAxis, rangeAxis, 0, null);
        
        graphics.dispose();
    }

    @Test(expected = NullPointerException.class)
    public void testDrawingWithNullGraphics() {
        XYLineAnnotation annotation = new XYLineAnnotation(START_X, START_Y, END_X, END_Y);
        
        NumberAxis domainAxis = new NumberAxis("X");
        NumberAxis rangeAxis = new NumberAxis("Y");
        XYPlot plot = new XYPlot(null, domainAxis, rangeAxis, null);
        Rectangle dataArea = new Rectangle(0, 0, 400, 300);
        
        annotation.draw(null, plot, dataArea, domainAxis, rangeAxis, 0, null);
    }

    @Test
    public void testDrawingWithHorizontalOrientation() {
        XYLineAnnotation annotation = new XYLineAnnotation(START_X, START_Y, END_X, END_Y);
        annotation.setToolTipText("Test tooltip");
        annotation.setURL("http://test.url");
        
        NumberAxis domainAxis = new NumberAxis("X");
        NumberAxis rangeAxis = new NumberAxis("Y");
        XYPlot plot = new XYPlot(null, domainAxis, rangeAxis, null);
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        
        Rectangle dataArea = new Rectangle(0, 0, 400, 300);
        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        
        // Should handle horizontal orientation without exception
        annotation.draw(graphics, plot, dataArea, domainAxis, rangeAxis, 0, null);
        
        graphics.dispose();
    }

    @Test
    public void testCoordinateGetters() {
        double x1 = -100.5, y1 = 200.7, x2 = 300.3, y2 = -400.9;
        XYLineAnnotation annotation = new XYLineAnnotation(x1, y1, x2, y2);
        
        assertEquals("getX1() should return correct value", x1, annotation.getX1(), DELTA);
        assertEquals("getY1() should return correct value", y1, annotation.getY1(), DELTA);
        assertEquals("getX2() should return correct value", x2, annotation.getX2(), DELTA);
        assertEquals("getY2() should return correct value", y2, annotation.getY2(), DELTA);
    }

    @Test
    public void testStrokeAndPaintGetters() {
        Stroke expectedStroke = new BasicStroke(3.0f);
        Paint expectedPaint = Color.GREEN;
        
        XYLineAnnotation annotation = new XYLineAnnotation(
            START_X, START_Y, END_X, END_Y, expectedStroke, expectedPaint);
        
        assertEquals("getStroke() should return correct stroke", expectedStroke, annotation.getStroke());
        assertEquals("getPaint() should return correct paint", expectedPaint, annotation.getPaint());
    }
}