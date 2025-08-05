package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
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
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
) 
public class DeviationRenderer_ESTest extends DeviationRenderer_ESTest_scaffolding {

    // Test alpha property modification and equality
    @Test(timeout = 4000)
    public void clonedRendererWithModifiedAlphaShouldNotBeEqual() throws Throwable {
        DeviationRenderer original = new DeviationRenderer();
        DeviationRenderer clone = (DeviationRenderer) original.clone();
        clone.alpha = -212.571F;
        assertFalse(original.equals(clone));
    }

    // Test valid alpha value setting
    @Test(timeout = 4000)
    public void setAlphaToValidValueShouldSucceed() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        renderer.setAlpha(1.0F);
        assertEquals(1.0F, renderer.getAlpha(), 0.01F);
    }

    // Test setting alpha to zero
    @Test(timeout = 4000)
    public void setAlphaToZeroShouldBeAllowed() throws Throwable {
        DeviationStepRenderer renderer = new DeviationStepRenderer(false, false);
        renderer.setAlpha(0.0F);
        assertEquals(0.0F, renderer.getAlpha(), 0.01F);
    }

    // Test getting alpha after direct field modification
    @Test(timeout = 4000)
    public void getAlphaAfterDirectModificationShouldReturnCorrectValue() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer();
        renderer.alpha = 0.0F;
        assertEquals(0.0F, renderer.getAlpha(), 0.01F);
    }

    // Test getting alpha with negative value (invalid state)
    @Test(timeout = 4000)
    public void getAlphaWithNegativeValueShouldReturnDirectValue() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        renderer.alpha = -189.0F;
        assertEquals(-189.0F, renderer.getAlpha(), 0.01F);
    }

    // Test range bounds calculation with interval dataset
    @Test(timeout = 4000)
    public void findRangeBoundsWithIntervalDatasetShouldNotFail() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer();
        DefaultIntervalXYDataset dataset = new DefaultIntervalXYDataset();
        renderer.findRangeBounds(dataset);
        assertTrue(renderer.getDrawSeriesLineAsPath());
        assertEquals(0.5F, renderer.getAlpha(), 0.01F);
    }

    // Test range bounds calculation with high-low dataset
    @Test(timeout = 4000)
    public void findRangeBoundsWithHighLowDatasetShouldNotFail() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer();
        Date[] dates = new Date[3];
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = Locale.PRC;
        DateAxis axis = new DateAxis("", timeZone, locale);
        dates[0] = axis.DEFAULT_ANCHOR_DATE;
        dates[1] = axis.DEFAULT_ANCHOR_DATE;
        dates[2] = dates[1];
        double[] values = new double[5];
        values[1] = -1.0;
        DefaultHighLowDataset dataset = new DefaultHighLowDataset(renderer.ZERO, dates, values, values, values, values, values);
        renderer.findRangeBounds(dataset);
        assertTrue(renderer.getDrawSeriesLineAsPath());
        assertEquals(0.5F, renderer.getAlpha(), 0.01F);
    }

    // Test equality with modified alpha value
    @Test(timeout = 4000)
    public void clonedRendererWithIncreasedAlphaShouldNotBeEqual() throws Throwable {
        DeviationRenderer original = new DeviationRenderer();
        DeviationRenderer clone = (DeviationRenderer) original.clone();
        clone.alpha = 6.0F;
        assertFalse(original.equals(clone));
    }

    // Test equality with different object type
    @Test(timeout = 4000)
    public void equalsWithDifferentObjectTypeShouldReturnFalse() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        assertFalse(renderer.equals(new Object()));
    }

    // Test reflexive equality
    @Test(timeout = 4000)
    public void equalsWithSameObjectShouldReturnTrue() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(true, true);
        assertTrue(renderer.equals(renderer));
    }

    // Test equality with cloned object
    @Test(timeout = 4000)
    public void equalsWithClonedObjectShouldReturnTrue() throws Throwable {
        DeviationRenderer original = new DeviationRenderer(false, false);
        DeviationRenderer clone = (DeviationRenderer) original.clone();
        assertTrue(original.equals(clone));
    }

    // Test line pass identification for pass 1
    @Test(timeout = 4000)
    public void isLinePassForPass1ShouldReturnTrue() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        assertTrue(renderer.isLinePass(1));
    }

    // Test line pass identification for pass 3
    @Test(timeout = 4000)
    public void isLinePassForPass3ShouldReturnFalse() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        assertFalse(renderer.isLinePass(3));
    }

    // Test item pass identification for pass 2
    @Test(timeout = 4000)
    public void isItemPassForPass2ShouldReturnTrue() throws Throwable {
        DeviationStepRenderer renderer = new DeviationStepRenderer();
        assertTrue(renderer.isItemPass(2));
    }

    // Test item pass identification for pass 3
    @Test(timeout = 4000)
    public void isItemPassForPass3ShouldReturnFalse() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        assertFalse(renderer.isItemPass(3));
    }

    // Test invalid alpha value (too high)
    @Test(timeout = 4000)
    public void setAlphaAboveOneShouldThrowException() throws Throwable {
        DeviationStepRenderer renderer = new DeviationStepRenderer(false, true);
        try {
            renderer.setAlpha(2255.623F);
            fail("Expected IllegalArgumentException for alpha > 1.0");
        } catch (IllegalArgumentException e) {
            assertEquals("Requires 'alpha' in the range 0.0 to 1.0.", e.getMessage());
        }
    }

    // Test invalid alpha value (negative)
    @Test(timeout = 4000)
    public void setAlphaBelowZeroShouldThrowException() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        try {
            renderer.setAlpha(-2381);
            fail("Expected IllegalArgumentException for alpha < 0.0");
        } catch (IllegalArgumentException e) {
            assertEquals("Requires 'alpha' in the range 0.0 to 1.0.", e.getMessage());
        }
    }

    // Test renderer state initialization
    @Test(timeout = 4000)
    public void initialiseShouldReturnValidRendererState() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer();
        Rectangle2D dataArea = new Rectangle2D.Double(0.0, 90.0, 3048.346, 0.0);
        XYPlot<CategoryAnchor> plot = new XYPlot<>();
        SimpleTimeZone timeZone = new SimpleTimeZone(10, "test");
        TimeSeriesCollection<CategoryAnchor> dataset = new TimeSeriesCollection<>(timeZone);
        PlotRenderingInfo info = new PlotRenderingInfo(new ChartRenderingInfo());
        
        XYItemRendererState state = renderer.initialise(null, dataArea, plot, dataset, info);
        
        assertFalse(state.getProcessVisibleItemsOnly());
        assertEquals(0.5F, renderer.getAlpha(), 0.01F);
        assertTrue(renderer.getDrawSeriesLineAsPath());
    }

    // Test range bounds with matrix dataset
    @Test(timeout = 4000)
    public void findRangeBoundsWithMatrixSeriesShouldNotFail() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(true, true);
        MatrixSeries series = new MatrixSeries(new Short((short)2396), 17, 28);
        MatrixSeriesCollection dataset = new MatrixSeriesCollection(series);
        renderer.findRangeBounds(dataset);
        assertTrue(renderer.getDrawSeriesLineAsPath());
        assertEquals(0.5F, renderer.getAlpha(), 0.01F);
    }

    // Test enabling drawSeriesLineAsPath property
    @Test(timeout = 4000)
    public void setDrawSeriesLineAsPathShouldEnableProperty() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        renderer.setDrawSeriesLineAsPath(true);
        assertTrue(renderer.getDrawSeriesLineAsPath());
    }

    // Test pass count value
    @Test(timeout = 4000)
    public void getPassCountShouldReturnThree() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        assertEquals(3, renderer.getPassCount());
    }

    // Test default alpha value
    @Test(timeout = 4000)
    public void getAlphaShouldReturnDefaultValue() throws Throwable {
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        assertEquals(0.5F, renderer.getAlpha(), 0.01F);
    }
}