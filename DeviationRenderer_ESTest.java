package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.SimpleTimeZone;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.DateAxis;
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

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class DeviationRenderer_ESTest extends DeviationRenderer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCloneAndEquality() throws Throwable {
        DeviationRenderer originalRenderer = new DeviationRenderer();
        DeviationRenderer clonedRenderer = (DeviationRenderer) originalRenderer.clone();
        
        assertEquals("Alpha value should be 0.5", 0.5F, clonedRenderer.getAlpha(), 0.01F);
        
        clonedRenderer.alpha = -212.571F;
        assertFalse("Cloned renderer with modified alpha should not be equal to the original", originalRenderer.equals(clonedRenderer));
    }

    @Test(timeout = 4000)
    public void testAlphaSetting() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        assertEquals("Initial alpha value should be 0.5", 0.5F, renderer.getAlpha(), 0.01F);
        
        renderer.setAlpha(1.0F);
        assertEquals("Alpha value should be set to 1.0", 1.0F, renderer.getAlpha(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testDeviationStepRendererAlpha() throws Throwable {
        DeviationStepRenderer stepRenderer = new DeviationStepRenderer(false, false);
        assertEquals("Initial alpha value should be 0.5", 0.5F, stepRenderer.getAlpha(), 0.01F);
        
        stepRenderer.setAlpha(0.0F);
        assertEquals("Alpha value should be set to 0.0", 0.0F, stepRenderer.getAlpha(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testAlphaDirectModification() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer();
        assertEquals("Initial alpha value should be 0.5", 0.5F, renderer.getAlpha(), 0.01F);
        
        renderer.alpha = 0.0F;
        assertEquals("Alpha value should be 0.0 after direct modification", 0.0F, renderer.getAlpha(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testFindRangeBoundsWithDefaultIntervalXYDataset() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer();
        DefaultIntervalXYDataset dataset = new DefaultIntervalXYDataset();
        
        renderer.findRangeBounds(dataset);
        
        assertTrue("Renderer should draw series line as path", renderer.getDrawSeriesLineAsPath());
        assertEquals("Alpha value should be 0.5", 0.5F, renderer.getAlpha(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testFindRangeBoundsWithDefaultHighLowDataset() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer();
        Date[] dates = new Date[3];
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = Locale.PRC;
        DateAxis dateAxis = new DateAxis("", timeZone, locale);
        
        dates[0] = dateAxis.DEFAULT_ANCHOR_DATE;
        dates[1] = dateAxis.DEFAULT_ANCHOR_DATE;
        dates[2] = dates[1];
        
        double[] values = new double[5];
        values[1] = -1.0;
        
        DefaultHighLowDataset dataset = new DefaultHighLowDataset(renderer.ZERO, dates, values, values, values, values, values);
        
        renderer.findRangeBounds(dataset);
        
        assertTrue("Renderer should draw series line as path", renderer.getDrawSeriesLineAsPath());
        assertEquals("Alpha value should be 0.5", 0.5F, renderer.getAlpha(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentAlpha() throws Throwable {
        DeviationRenderer originalRenderer = new DeviationRenderer();
        DeviationRenderer clonedRenderer = (DeviationRenderer) originalRenderer.clone();
        
        assertEquals("Alpha value should be 0.5", 0.5F, clonedRenderer.getAlpha(), 0.01F);
        
        clonedRenderer.alpha = 6.0F;
        assertFalse("Cloned renderer with modified alpha should not be equal to the original", originalRenderer.equals(clonedRenderer));
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentObject() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        Object differentObject = new Object();
        
        assertFalse("Renderer should not be equal to a different object", renderer.equals(differentObject));
        assertEquals("Alpha value should be 0.5", 0.5F, renderer.getAlpha(), 0.01F);
        assertTrue("Renderer should draw series line as path", renderer.getDrawSeriesLineAsPath());
    }

    @Test(timeout = 4000)
    public void testSelfEquality() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(true, true);
        
        assertTrue("Renderer should be equal to itself", renderer.equals(renderer));
        assertEquals("Alpha value should be 0.5", 0.5F, renderer.getAlpha(), 0.01F);
        assertTrue("Renderer should draw series line as path", renderer.getDrawSeriesLineAsPath());
    }

    @Test(timeout = 4000)
    public void testCloneEquality() throws Throwable {
        DeviationRenderer originalRenderer = new DeviationRenderer(false, false);
        DeviationRenderer clonedRenderer = (DeviationRenderer) originalRenderer.clone();
        
        assertTrue("Cloned renderer should be equal to the original", originalRenderer.equals(clonedRenderer));
        assertEquals("Alpha value should be 0.5", 0.5F, clonedRenderer.getAlpha(), 0.01F);
        assertTrue("Cloned renderer should draw series line as path", clonedRenderer.getDrawSeriesLineAsPath());
    }

    @Test(timeout = 4000)
    public void testIsLinePassTrue() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        
        assertTrue("Line pass should be true for pass index 1", renderer.isLinePass(1));
        assertEquals("Alpha value should be 0.5", 0.5F, renderer.getAlpha(), 0.01F);
        assertTrue("Renderer should draw series line as path", renderer.getDrawSeriesLineAsPath());
    }

    @Test(timeout = 4000)
    public void testIsLinePassFalse() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        
        assertFalse("Line pass should be false for pass index 3", renderer.isLinePass(3));
        assertEquals("Alpha value should be 0.5", 0.5F, renderer.getAlpha(), 0.01F);
        assertTrue("Renderer should draw series line as path", renderer.getDrawSeriesLineAsPath());
    }

    @Test(timeout = 4000)
    public void testDeviationStepRendererIsItemPass() throws Throwable {
        DeviationStepRenderer stepRenderer = new DeviationStepRenderer();
        
        assertTrue("Item pass should be true for pass index 2", stepRenderer.isItemPass(2));
        assertEquals("Alpha value should be 0.5", stepRenderer.getAlpha(), 0.01F);
        assertTrue("Renderer should draw series line as path", stepRenderer.getDrawSeriesLineAsPath());
    }

    @Test(timeout = 4000)
    public void testIsItemPassFalse() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        
        assertFalse("Item pass should be false for pass index 3", renderer.isItemPass(3));
        assertEquals("Alpha value should be 0.5", renderer.getAlpha(), 0.01F);
        assertTrue("Renderer should draw series line as path", renderer.getDrawSeriesLineAsPath());
    }

    @Test(timeout = 4000)
    public void testSetAlphaOutOfRangeException() throws Throwable {
        DeviationStepRenderer stepRenderer = new DeviationStepRenderer(false, true);
        
        try {
            stepRenderer.setAlpha(2255.623F);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.renderer.xy.DeviationRenderer", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetAlphaNegativeException() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        
        try {
            renderer.setAlpha(-2381);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.renderer.xy.DeviationRenderer", e);
        }
    }

    @Test(timeout = 4000)
    public void testInitialiseRendererState() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer();
        Rectangle2D.Double dataArea = new Rectangle2D.Double((double) renderer.ZERO, 90.0, 3048.34637057, 0.0);
        XYPlot<CategoryAnchor> plot = new XYPlot<>();
        SimpleTimeZone timeZone = new SimpleTimeZone(10, "w@^qxE!G 6g");
        TimeSeriesCollection<CategoryAnchor> dataset = new TimeSeriesCollection<>(timeZone);
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(chartInfo);
        
        XYItemRendererState state = renderer.initialise(null, dataArea, plot, dataset, plotInfo);
        
        assertTrue("Renderer should draw series line as path", renderer.getDrawSeriesLineAsPath());
        assertEquals("Alpha value should be 0.5", renderer.getAlpha(), 0.01F);
        assertFalse("State should not process visible items only", state.getProcessVisibleItemsOnly());
    }

    @Test(timeout = 4000)
    public void testFindRangeBoundsWithMatrixSeriesCollection() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(true, true);
        Short shortValue = 2396;
        MatrixSeries<Short> matrixSeries = new MatrixSeries<>(shortValue, 17, 28);
        MatrixSeriesCollection<Short> dataset = new MatrixSeriesCollection<>(matrixSeries);
        
        renderer.findRangeBounds(dataset);
        
        assertTrue("Renderer should draw series line as path", renderer.getDrawSeriesLineAsPath());
        assertEquals("Alpha value should be 0.5", renderer.getAlpha(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testSetDrawSeriesLineAsPath() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        
        renderer.setDrawSeriesLineAsPath(true);
        
        assertEquals("Alpha value should be 0.5", 0.5F, renderer.getAlpha(), 0.01F);
        assertTrue("Renderer should draw series line as path", renderer.getDrawSeriesLineAsPath());
    }

    @Test(timeout = 4000)
    public void testGetPassCount() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        
        assertEquals("Pass count should be 3", 3, renderer.getPassCount());
        assertTrue("Renderer should draw series line as path", renderer.getDrawSeriesLineAsPath());
        assertEquals("Alpha value should be 0.5", 0.5F, renderer.getAlpha(), 0.01F);
    }

    @Test(timeout = 4000)
    public void testGetAlpha() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        
        assertEquals("Alpha value should be 0.5", 0.5F, renderer.getAlpha(), 0.01F);
        assertTrue("Renderer should draw series line as path", renderer.getDrawSeriesLineAsPath());
    }
}