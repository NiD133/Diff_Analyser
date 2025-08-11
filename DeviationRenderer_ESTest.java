package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.renderer.xy.DeviationStepRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.MatrixSeries;
import org.jfree.data.xy.MatrixSeriesCollection;

/**
 * Test suite for DeviationRenderer functionality including alpha transparency,
 * rendering passes, equality checks, and dataset bounds calculation.
 */
public class DeviationRendererTest {

    private static final float DEFAULT_ALPHA = 0.5f;
    private static final float DELTA = 0.01f;
    private static final int EXPECTED_PASS_COUNT = 3;

    // Alpha transparency tests
    
    @Test
    public void testDefaultAlphaValue() {
        DeviationRenderer renderer = new DeviationRenderer();
        
        assertEquals("Default alpha should be 0.5", DEFAULT_ALPHA, renderer.getAlpha(), DELTA);
    }

    @Test
    public void testSetValidAlphaValues() {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        
        // Test setting alpha to 1.0
        renderer.setAlpha(1.0f);
        assertEquals("Alpha should be set to 1.0", 1.0f, renderer.getAlpha(), DELTA);
        
        // Test setting alpha to 0.0
        renderer.setAlpha(0.0f);
        assertEquals("Alpha should be set to 0.0", 0.0f, renderer.getAlpha(), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAlphaAboveValidRange() {
        DeviationRenderer renderer = new DeviationRenderer();
        
        renderer.setAlpha(2255.623f); // Should throw exception for values > 1.0
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAlphaBelowValidRange() {
        DeviationRenderer renderer = new DeviationRenderer();
        
        renderer.setAlpha(-2381f); // Should throw exception for negative values
    }

    // Rendering pass tests
    
    @Test
    public void testGetPassCount() {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        
        assertEquals("DeviationRenderer should use 3 passes", EXPECTED_PASS_COUNT, renderer.getPassCount());
    }

    @Test
    public void testLinePassIdentification() {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        
        assertTrue("Pass 1 should be a line pass", renderer.isLinePass(1));
        assertFalse("Pass 3 should not be a line pass", renderer.isLinePass(3));
    }

    @Test
    public void testItemPassIdentification() {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        
        assertFalse("Pass 3 should not be an item pass for DeviationRenderer", renderer.isItemPass(3));
        
        // Test with DeviationStepRenderer which has different behavior
        DeviationStepRenderer stepRenderer = new DeviationStepRenderer();
        assertTrue("Pass 2 should be an item pass for DeviationStepRenderer", stepRenderer.isItemPass(2));
    }

    // Equality and cloning tests
    
    @Test
    public void testEqualityWithSameObject() {
        DeviationRenderer renderer = new DeviationRenderer(true, true);
        
        assertTrue("Renderer should equal itself", renderer.equals(renderer));
    }

    @Test
    public void testEqualityWithClonedObject() {
        DeviationRenderer original = new DeviationRenderer(false, false);
        DeviationRenderer cloned = (DeviationRenderer) original.clone();
        
        assertTrue("Original and cloned renderer should be equal", original.equals(cloned));
        assertEquals("Cloned renderer should have same alpha", DEFAULT_ALPHA, cloned.getAlpha(), DELTA);
    }

    @Test
    public void testInequalityWithDifferentAlpha() {
        DeviationRenderer renderer1 = new DeviationRenderer();
        DeviationRenderer renderer2 = (DeviationRenderer) renderer1.clone();
        
        // Modify alpha directly (simulating internal state change)
        renderer2.alpha = -212.571f;
        
        assertFalse("Renderers with different alpha values should not be equal", 
                   renderer1.equals(renderer2));
    }

    @Test
    public void testInequalityWithDifferentObjectType() {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        Object differentObject = new Object();
        
        assertFalse("Renderer should not equal object of different type", 
                   renderer.equals(differentObject));
    }

    // Dataset bounds tests
    
    @Test
    public void testFindRangeBoundsWithEmptyDataset() {
        DeviationRenderer renderer = new DeviationRenderer();
        DefaultIntervalXYDataset<DatasetRenderingOrder> emptyDataset = 
            new DefaultIntervalXYDataset<DatasetRenderingOrder>();
        
        // Should handle empty dataset without throwing exception
        renderer.findRangeBounds(emptyDataset);
        
        assertTrue("Renderer should maintain draw series line as path setting", 
                  renderer.getDrawSeriesLineAsPath());
    }

    @Test
    public void testFindRangeBoundsWithHighLowDataset() {
        DeviationRenderer renderer = new DeviationRenderer();
        
        // Create test data with dates and values
        Date[] dates = createTestDates();
        double[] values = {0.0, -1.0, 0.0, 0.0, 0.0};
        
        DefaultHighLowDataset dataset = new DefaultHighLowDataset(
            "Test Series", dates, values, values, values, values, values);
        
        // Should process dataset without throwing exception
        renderer.findRangeBounds(dataset);
        
        assertTrue("Renderer should maintain draw series line as path setting", 
                  renderer.getDrawSeriesLineAsPath());
    }

    @Test
    public void testFindRangeBoundsWithMatrixDataset() {
        DeviationRenderer renderer = new DeviationRenderer(true, true);
        
        Short seriesKey = Short.valueOf((short) 2396);
        MatrixSeries<Short> matrixSeries = new MatrixSeries<Short>(seriesKey, 17, 28);
        MatrixSeriesCollection<Short> dataset = new MatrixSeriesCollection<Short>(matrixSeries);
        
        // Should handle matrix dataset without throwing exception
        renderer.findRangeBounds(dataset);
        
        assertTrue("Renderer should maintain draw series line as path setting", 
                  renderer.getDrawSeriesLineAsPath());
    }

    // Initialization test
    
    @Test
    public void testRendererInitialization() {
        DeviationRenderer renderer = new DeviationRenderer();
        Rectangle2D dataArea = new Rectangle2D.Double(0.0, 90.0, 3048.34637057, 0.0);
        XYPlot<CategoryAnchor> plot = new XYPlot<CategoryAnchor>();
        
        SimpleTimeZone timeZone = new SimpleTimeZone(10, "TestTimeZone");
        TimeSeriesCollection<CategoryAnchor> dataset = 
            new TimeSeriesCollection<CategoryAnchor>(timeZone);
        
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(chartInfo);
        
        XYItemRendererState state = renderer.initialise(
            null, dataArea, plot, dataset, plotInfo);
        
        assertNotNull("Initialization should return a valid state object", state);
        assertFalse("State should not process visible items only by default", 
                   state.getProcessVisibleItemsOnly());
    }

    // Configuration test
    
    @Test
    public void testDrawSeriesLineAsPathConfiguration() {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        
        // This should not change the setting as it's fixed for DeviationRenderer
        renderer.setDrawSeriesLineAsPath(true);
        
        assertTrue("DeviationRenderer should always draw series line as path", 
                  renderer.getDrawSeriesLineAsPath());
    }

    // Helper methods
    
    private Date[] createTestDates() {
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = Locale.PRC;
        DateAxis dateAxis = new DateAxis("", timeZone, locale);
        
        Date[] dates = new Date[3];
        dates[0] = dateAxis.DEFAULT_ANCHOR_DATE;
        dates[1] = dateAxis.DEFAULT_ANCHOR_DATE;
        dates[2] = dates[1];
        
        return dates;
    }
}