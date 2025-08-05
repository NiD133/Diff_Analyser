/*
 * Refactored test suite for XYLineAnnotation with improved understandability
 */
package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import javax.swing.text.DefaultCaret;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CyclicNumberAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.PeriodAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedRangeXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.plot.WaferMapPlot;
import org.jfree.chart.plot.pie.PiePlot;
import org.jfree.chart.renderer.WaferMapRenderer;
import org.jfree.chart.renderer.xy.SamplingXYLineRenderer;
import org.jfree.data.time.Day;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
) 
public class XYLineAnnotation_ESTest extends XYLineAnnotation_ESTest_scaffolding {

    // ========================================================================
    // TEST CONSTRUCTOR VALIDATION
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testConstructorThrowsExceptionForNullPaint() {
        BasicStroke stroke = (BasicStroke) CyclicNumberAxis.DEFAULT_ADVANCE_LINE_STROKE;
        try {
            new XYLineAnnotation(4572.497, 4572.497, 4572.497, 4572.497, stroke, null);
            fail("Expected IllegalArgumentException for null paint");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'paint' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testConstructorThrowsExceptionForNonFiniteCoordinate() {
        try {
            new XYLineAnnotation(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 
                                 Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
            fail("Expected IllegalArgumentException for non-finite x1");
        } catch (IllegalArgumentException e) {
            assertEquals("Require 'x1' (-Infinity) to be finite.", e.getMessage());
        }
    }

    // ========================================================================
    // TEST GETTER METHODS
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testGettersReturnCorrectValues() {
        XYLineAnnotation annotation = new XYLineAnnotation(2545.5, -909.0, -1423.676, 0.0);
        
        assertEquals("X1 should match constructor param", 2545.5, annotation.getX1(), 0.01);
        assertEquals("Y1 should match constructor param", -909.0, annotation.getY1(), 0.01);
        assertEquals("X2 should match constructor param", -1423.676, annotation.getX2(), 0.01);
        assertEquals("Y2 should match constructor param", 0.0, annotation.getY2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testGettersWithNegativeCoordinates() {
        XYLineAnnotation annotation = new XYLineAnnotation(-123.58, 1.0, 1.0, -123.58);
        
        assertEquals(-123.58, annotation.getX1(), 0.01);
        assertEquals(1.0, annotation.getY1(), 0.01);
        assertEquals(1.0, annotation.getX2(), 0.01);
        assertEquals(-123.58, annotation.getY2(), 0.01);
    }

    @Test(timeout = 4000)
    public void testGetStrokeAndPaintReturnDefaultsWhenNotSpecified() {
        XYLineAnnotation annotation = new XYLineAnnotation(500, 1069.473, 0.25, 0.25);
        
        assertNotNull("Stroke should not be null", annotation.getStroke());
        assertNotNull("Paint should not be null", annotation.getPaint());
    }

    // ========================================================================
    // TEST EQUALS AND HASHCODE
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testEqualsWithSameObject() {
        XYLineAnnotation annotation = createSampleAnnotation();
        assertTrue("Object should equal itself", annotation.equals(annotation));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentClass() {
        XYLineAnnotation annotation = createSampleAnnotation();
        assertFalse("Should not equal different class", annotation.equals(new Object()));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentCoordinates() {
        CombinedRangeXYPlot plot = new CombinedRangeXYPlot();
        Stroke stroke = plot.DEFAULT_OUTLINE_STROKE;
        Paint paint = plot.DEFAULT_CROSSHAIR_PAINT;
        
        XYLineAnnotation annotation1 = new XYLineAnnotation(10, 1.0, 1.0, 10, stroke, paint);
        XYLineAnnotation annotation2 = new XYLineAnnotation(10, 1.0, 1.0, 1562.446, stroke, paint);
        
        assertFalse("Annotations with different y2 should not be equal", annotation1.equals(annotation2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentStroke() {
        CombinedRangeXYPlot plot = new CombinedRangeXYPlot();
        Stroke stroke1 = plot.DEFAULT_OUTLINE_STROKE;
        Stroke stroke2 = plot.getDomainMinorGridlineStroke();
        Paint paint = plot.DEFAULT_CROSSHAIR_PAINT;
        
        XYLineAnnotation annotation1 = new XYLineAnnotation(10, 1.0, 1.0, 10, stroke1, paint);
        XYLineAnnotation annotation2 = new XYLineAnnotation(10, 1.0, -14.36, 180.0, stroke2, paint);
        
        assertFalse("Annotations with different strokes should not be equal", annotation1.equals(annotation2));
    }

    @Test(timeout = 4000)
    public void testHashCodeConsistency() {
        XYLineAnnotation annotation = new XYLineAnnotation(-3139.81, -3139.81, -3139.81, -605.8);
        int initialHash = annotation.hashCode();
        
        // Recalculate should be consistent
        assertEquals("Hashcode should be consistent", initialHash, annotation.hashCode());
    }

    // ========================================================================
    // TEST DRAWING FUNCTIONALITY
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testDrawThrowsNPEWhenGraphicsIsNull() {
        CombinedRangeXYPlot plot = new CombinedRangeXYPlot();
        DateAxis axis = new DateAxis();
        XYLineAnnotation annotation = new XYLineAnnotation(10, 10.0, 5193.6, -1711.1, 
            axis.DEFAULT_AXIS_LINE_STROKE, axis.DEFAULT_TICK_LABEL_PAINT);
        
        try {
            annotation.draw(null, plot, new Rectangle(), new CyclicNumberAxis(972.99), axis, 0, null);
            fail("Expected NullPointerException for null graphics");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testDrawWithTooltipAndURL() {
        CombinedRangeXYPlot plot = new CombinedRangeXYPlot();
        Stroke stroke = plot.DEFAULT_OUTLINE_STROKE;
        Paint paint = plot.DEFAULT_CROSSHAIR_PAINT;
        XYLineAnnotation annotation = new XYLineAnnotation(10, 1.0, 1.0, 10, stroke, paint);
        
        // Configure annotations
        annotation.setToolTipText("tooltip");
        annotation.setURL("http://example.com");
        
        // Setup drawing environment
        PeriodAxis axis = new PeriodAxis("Axis");
        JFreeChart chart = new JFreeChart("Title", axis.DEFAULT_TICK_LABEL_FONT, plot, true);
        BufferedImage image = chart.createBufferedImage(10, 10);
        Graphics2D g2 = image.createGraphics();
        
        // Should execute without exceptions
        annotation.draw(g2, plot, new Rectangle(10, 10), axis, axis, 0, null);
    }

    // ========================================================================
    // TEST CLONING FUNCTIONALITY
    // ========================================================================
    
    @Test(timeout = 4000)
    public void testCloneProducesEqualInstance() throws Exception {
        CombinedRangeXYPlot plot = new CombinedRangeXYPlot();
        Stroke stroke = plot.DEFAULT_OUTLINE_STROKE;
        Paint paint = plot.DEFAULT_CROSSHAIR_PAINT;
        XYLineAnnotation original = new XYLineAnnotation(10, 1.0, 1.0, 10, stroke, paint);
        
        XYLineAnnotation clone = (XYLineAnnotation) original.clone();
        assertNotSame("Clone should be different object", original, clone);
        assertEquals("Clone should be equal to original", original, clone);
    }

    // ========================================================================
    // HELPER METHODS
    // ========================================================================
    
    private XYLineAnnotation createSampleAnnotation() {
        return new XYLineAnnotation(-1433.615, -1433.615, -1433.615, -1433.615);
    }
}