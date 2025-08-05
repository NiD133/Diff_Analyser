package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.Drawable;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;

/**
 * Test suite for XYDrawableAnnotation class.
 * Tests constructor validation, property getters, equality, hashing, and drawing functionality.
 */
public class XYDrawableAnnotation_ESTest {

    // Test data constants for better readability
    private static final double X_COORDINATE = 100.0;
    private static final double Y_COORDINATE = 200.0;
    private static final double WIDTH = 50.0;
    private static final double HEIGHT = 30.0;
    private static final double SCALE_FACTOR = 2.0;
    
    // Helper method to create a simple drawable for testing
    private Drawable createTestDrawable() {
        return new TextTitle("Test");
    }

    @Test(timeout = 4000)
    public void testConstructorWithBasicParameters() {
        Drawable drawable = createTestDrawable();
        
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, drawable);
        
        assertEquals("X coordinate should match constructor parameter", 
                     X_COORDINATE, annotation.getX(), 0.01);
        assertEquals("Y coordinate should match constructor parameter", 
                     Y_COORDINATE, annotation.getY(), 0.01);
        assertEquals("Width should match constructor parameter", 
                     WIDTH, annotation.getDisplayWidth(), 0.01);
        assertEquals("Height should match constructor parameter", 
                     HEIGHT, annotation.getDisplayHeight(), 0.01);
        assertEquals("Default scale factor should be 1.0", 
                     1.0, annotation.getDrawScaleFactor(), 0.01);
    }

    @Test(timeout = 4000)
    public void testConstructorWithScaleFactor() {
        Drawable drawable = createTestDrawable();
        
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, SCALE_FACTOR, drawable);
        
        assertEquals("X coordinate should match constructor parameter", 
                     X_COORDINATE, annotation.getX(), 0.01);
        assertEquals("Y coordinate should match constructor parameter", 
                     Y_COORDINATE, annotation.getY(), 0.01);
        assertEquals("Width should match constructor parameter", 
                     WIDTH, annotation.getDisplayWidth(), 0.01);
        assertEquals("Height should match constructor parameter", 
                     HEIGHT, annotation.getDisplayHeight(), 0.01);
        assertEquals("Scale factor should match constructor parameter", 
                     SCALE_FACTOR, annotation.getDrawScaleFactor(), 0.01);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testConstructorRejectsNullDrawable() {
        new XYDrawableAnnotation(X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, null);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testConstructorWithScaleFactorRejectsNullDrawable() {
        new XYDrawableAnnotation(X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, SCALE_FACTOR, null);
    }

    @Test(timeout = 4000)
    public void testGettersReturnCorrectValues() {
        Drawable drawable = createTestDrawable();
        double x = -100.5, y = 250.75, width = 80.25, height = 120.5, scale = 1.5;
        
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(x, y, width, height, scale, drawable);
        
        assertEquals("getX() should return constructor X value", x, annotation.getX(), 0.01);
        assertEquals("getY() should return constructor Y value", y, annotation.getY(), 0.01);
        assertEquals("getDisplayWidth() should return constructor width", width, annotation.getDisplayWidth(), 0.01);
        assertEquals("getDisplayHeight() should return constructor height", height, annotation.getDisplayHeight(), 0.01);
        assertEquals("getDrawScaleFactor() should return constructor scale", scale, annotation.getDrawScaleFactor(), 0.01);
    }

    @Test(timeout = 4000)
    public void testEqualsWithIdenticalAnnotations() {
        Drawable drawable = createTestDrawable();
        
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, SCALE_FACTOR, drawable);
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, SCALE_FACTOR, drawable);
        
        assertTrue("Annotations with identical parameters should be equal", 
                   annotation1.equals(annotation2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentXCoordinates() {
        Drawable drawable = createTestDrawable();
        
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
            100.0, Y_COORDINATE, WIDTH, HEIGHT, drawable);
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(
            200.0, Y_COORDINATE, WIDTH, HEIGHT, drawable);
        
        assertFalse("Annotations with different X coordinates should not be equal", 
                    annotation1.equals(annotation2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentYCoordinates() {
        Drawable drawable = createTestDrawable();
        
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
            X_COORDINATE, 100.0, WIDTH, HEIGHT, drawable);
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(
            X_COORDINATE, 200.0, WIDTH, HEIGHT, drawable);
        
        assertFalse("Annotations with different Y coordinates should not be equal", 
                    annotation1.equals(annotation2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentWidths() {
        Drawable drawable = createTestDrawable();
        
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, 50.0, HEIGHT, drawable);
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, 100.0, HEIGHT, drawable);
        
        assertFalse("Annotations with different widths should not be equal", 
                    annotation1.equals(annotation2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentHeights() {
        Drawable drawable = createTestDrawable();
        
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, 30.0, drawable);
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, 60.0, drawable);
        
        assertFalse("Annotations with different heights should not be equal", 
                    annotation1.equals(annotation2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentScaleFactors() {
        Drawable drawable = createTestDrawable();
        
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, 1.0, drawable);
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, 2.0, drawable);
        
        assertFalse("Annotations with different scale factors should not be equal", 
                    annotation1.equals(annotation2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentDrawables() {
        Drawable drawable1 = new TextTitle("First");
        Drawable drawable2 = new BlockContainer();
        
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, drawable1);
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, drawable2);
        
        assertFalse("Annotations with different drawables should not be equal", 
                    annotation1.equals(annotation2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithNonAnnotationObject() {
        Drawable drawable = createTestDrawable();
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, drawable);
        
        assertFalse("Annotation should not equal non-annotation object", 
                    annotation.equals("not an annotation"));
    }

    @Test(timeout = 4000)
    public void testEqualsWithSelf() {
        Drawable drawable = createTestDrawable();
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, drawable);
        
        assertTrue("Annotation should equal itself", annotation.equals(annotation));
    }

    @Test(timeout = 4000)
    public void testHashCodeConsistency() {
        Drawable drawable = createTestDrawable();
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, SCALE_FACTOR, drawable);
        
        int hashCode1 = annotation.hashCode();
        int hashCode2 = annotation.hashCode();
        
        assertEquals("Hash code should be consistent across multiple calls", 
                     hashCode1, hashCode2);
    }

    @Test(timeout = 4000)
    public void testCloneCreatesEqualObject() throws CloneNotSupportedException {
        Drawable drawable = createTestDrawable();
        XYDrawableAnnotation original = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, SCALE_FACTOR, drawable);
        
        XYDrawableAnnotation clone = (XYDrawableAnnotation) original.clone();
        
        assertTrue("Cloned annotation should equal original", original.equals(clone));
        assertNotSame("Cloned annotation should be different object", original, clone);
    }

    @Test(timeout = 4000)
    public void testDrawWithValidParameters() {
        Drawable drawable = createTestDrawable();
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, drawable);
        
        // Set up drawing context
        XYPlot plot = new XYPlot();
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 400, 300);
        ValueAxis domainAxis = new NumberAxis("Domain");
        ValueAxis rangeAxis = new NumberAxis("Range");
        
        // Create graphics context
        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        
        // This should not throw an exception
        annotation.draw(g2, plot, dataArea, domainAxis, rangeAxis, 0, plotInfo);
        
        g2.dispose();
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testDrawWithNullRangeAxis() {
        Drawable drawable = createTestDrawable();
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, drawable);
        
        XYPlot plot = new XYPlot();
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 400, 300);
        ValueAxis domainAxis = new NumberAxis("Domain");
        
        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        
        try {
            annotation.draw(g2, plot, dataArea, domainAxis, null, 0, plotInfo);
        } finally {
            g2.dispose();
        }
    }

    @Test(timeout = 4000)
    public void testDrawWithToolTipAndURL() {
        Drawable drawable = createTestDrawable();
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
            X_COORDINATE, Y_COORDINATE, WIDTH, HEIGHT, drawable);
        
        // Set tooltip and URL for entity creation
        annotation.setToolTipText("Test tooltip");
        annotation.setURL("http://test.url");
        
        XYPlot plot = new XYPlot();
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 400, 300);
        ValueAxis domainAxis = new NumberAxis("Domain");
        ValueAxis rangeAxis = new NumberAxis("Range");
        
        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        
        // This should not throw an exception and should handle tooltip/URL
        annotation.draw(g2, plot, dataArea, domainAxis, rangeAxis, 0, plotInfo);
        
        g2.dispose();
    }
}