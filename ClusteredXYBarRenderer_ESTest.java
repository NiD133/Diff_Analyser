/*
 * Refactored for clarity and maintainability.
 * Tests for ClusteredXYBarRenderer focusing on key behaviors:
 *  - Domain range calculation with/without offset
 *  - Rendering and exception handling
 *  - Object equality and cloning
 */
package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.Locale;
import javax.swing.JLayeredPane;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.text.MockSimpleDateFormat;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.CyclicNumberAxis;
import org.jfree.chart.plot.CategoryCrosshairState;
import org.jfree.chart.plot.CombinedRangeXYPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.util.DirectionalGradientPaintTransformer;
import org.jfree.data.Range;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.DefaultWindDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ClusteredXYBarRenderer_ESTest extends ClusteredXYBarRenderer_ESTest_scaffolding {

    // Constants for repeated values
    private static final Integer POPUP_LAYER = JLayeredPane.POPUP_LAYER;
    private static final Integer MODAL_LAYER = JLayeredPane.MODAL_LAYER;
    private static final Integer PALETTE_LAYER = JLayeredPane.PALETTE_LAYER;
    private static final Integer FRAME_CONTENT_LAYER = JLayeredPane.FRAME_CONTENT_LAYER;
    private static final Integer DRAG_LAYER = JLayeredPane.DRAG_LAYER;

    //---------------- Tests for findDomainBoundsWithOffset --------------------

    @Test(timeout = 4000)
    public void findDomainBoundsWithOffset_WithSingleInfiniteBin_ReturnsValidRange() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(POPUP_LAYER);
        SimpleHistogramBin bin = new SimpleHistogramBin(Double.NEGATIVE_INFINITY, renderer.ZERO);
        dataset.addBin(bin);

        Range range = renderer.findDomainBoundsWithOffset(dataset);
        
        assertNotNull("Range should not be null", range);
        assertEquals("Central value should be negative infinity", 
            Double.NEGATIVE_INFINITY, range.getCentralValue(), 0.01);
        assertEquals("Range length should be NaN", 
            Double.NaN, range.getLength(), 0.01);
    }

    @Test(timeout = 4000)
    public void findDomainBoundsWithOffset_WithPositiveAndNegativeBins_ReturnsValidRange() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(POPUP_LAYER);
        dataset.addBin(new SimpleHistogramBin(907.69, 1031.047, true, false));
        dataset.addBin(new SimpleHistogramBin(-370.142, renderer.ZERO));

        Range range = renderer.findDomainBoundsWithOffset(dataset);
        
        assertNotNull("Range should not be null", range);
        assertEquals("Range length should match expected", 
            1524.582, range.getLength(), 0.01);
    }

    @Test(timeout = 4000)
    public void findDomainBoundsWithOffset_WithSinglePointSeries_ReturnsCenteredRange() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        XYSeries<Boolean> series = new XYSeries<>(Boolean.FALSE);
        series.add(renderer.ZERO, 30.0);
        XYSeriesCollection<Boolean> dataset = new XYSeriesCollection<>(series);

        Range range = renderer.findDomainBoundsWithOffset(dataset);
        
        assertNotNull("Range should not be null", range);
        assertEquals("Central value should be -0.5", 
            -0.5, range.getCentralValue(), 0.01);
    }

    @Test(timeout = 4000)
    public void findDomainBoundsWithOffset_WithBinStartingAtZero_ReturnsZeroCenteredRange() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(FRAME_CONTENT_LAYER);
        dataset.addBin(new SimpleHistogramBin(renderer.ZERO, 0.2));

        Range range = renderer.findDomainBoundsWithOffset(dataset);
        
        assertNotNull("Range should not be null", range);
        assertEquals("Central value should be 0", 
            0.0, range.getCentralValue(), 0.01);
    }

    @Test(timeout = 4000)
    public void findDomainBoundsWithOffset_WithPositiveBin_ReturnsPositiveRange() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(DRAG_LAYER);
        dataset.addBin(new SimpleHistogramBin(1249.0, 2514.417));

        Range range = renderer.findDomainBoundsWithOffset(dataset);
        
        assertNotNull("Range should not be null", range);
        assertEquals("Central value should be 1249", 
            1249.0, range.getCentralValue(), 0.01);
    }

    @Test(timeout = 4000)
    public void findDomainBoundsWithOffset_WithNullBin_ThrowsException() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(JLayeredPane.DEFAULT_LAYER);
        dataset.addBin(null); // Intentionally adding null bin

        try {
            renderer.findDomainBoundsWithOffset(dataset);
            fail("Expected NullPointerException due to null bin");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void findDomainBoundsWithOffset_WithNullDataset_ThrowsException() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        try {
            renderer.findDomainBoundsWithOffset(null);
            fail("Expected IllegalArgumentException for null dataset");
        } catch (IllegalArgumentException e) {
            assertEquals("Exception message should match", 
                "Null 'dataset' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void findDomainBoundsWithOffset_WithEmptyDataset_ReturnsNull() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(MODAL_LAYER);

        Range range = renderer.findDomainBoundsWithOffset(dataset);
        
        assertNull("Range should be null for empty dataset", range);
    }

    //------------------ Tests for findDomainBounds ----------------------------

    @Test(timeout = 4000)
    public void findDomainBounds_WithNegativeBin_ReturnsCorrectRange() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(MODAL_LAYER);
        dataset.addBin(new SimpleHistogramBin(-901.517, -465.480));

        Range range = renderer.findDomainBounds(dataset);
        
        assertEquals("Central value should match",
            -683.499, range.getCentralValue(), 0.01);
    }

    @Test(timeout = 4000)
    public void findDomainBounds_WithZeroToOneBin_ReturnsValidRange() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(PALETTE_LAYER);
        dataset.addBin(new SimpleHistogramBin(0.0, 1.0, true, true));

        Range range = renderer.findDomainBounds(dataset);
        
        assertTrue("Range should not be NaN", !range.isNaNRange());
    }

    @Test(timeout = 4000)
    public void findDomainBounds_WithOHLCDataset_ReturnsValidRange() throws Exception {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        OHLCDataItem[] items = new OHLCDataItem[5];
        MockSimpleDateFormat dateFormat = createMockDateFormat();
        Date date = dateFormat.get2DigitYearStart();
        OHLCDataItem item = new OHLCDataItem(date, 1641.622, 1376.031, 1376.031, 1641.622, 597.535);
        items[0] = item;
        items[1] = item;
        items[2] = item;
        items[3] = item;
        items[4] = item;
        DefaultOHLCDataset dataset = new DefaultOHLCDataset(renderer.ZERO, items);

        Range range = renderer.findDomainBounds(dataset);
        
        assertEquals("Lower bound should match timestamp", 
            1.39240928132E12, range.getLowerBound(), 0.01);
    }

    @Test(timeout = 4000)
    public void findDomainBounds_WithNegativeToZeroBin_ReturnsZeroUpperBound() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(FRAME_CONTENT_LAYER);
        dataset.addBin(new SimpleHistogramBin(-673.524, renderer.ZERO));

        Range range = renderer.findDomainBounds(dataset);
        
        assertNotNull("Range should not be null", range);
        assertEquals("Upper bound should be 0", 
            0.0, range.getUpperBound(), 0.01);
    }

    @Test(timeout = 4000)
    public void findDomainBounds_WithBoxAndWhiskerDataset_ThrowsException() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        DefaultBoxAndWhiskerXYDataset<Short> dataset = new DefaultBoxAndWhiskerXYDataset<>(null);

        try {
            renderer.findDomainBounds(dataset);
            fail("Expected NullPointerException due to null series key");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void findDomainBounds_WithIncompatibleDataset_ThrowsException() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer(-3079.59, true);
        DefaultWindDataset dataset = new DefaultWindDataset();

        try {
            renderer.findDomainBounds(dataset);
            fail("Expected ClassCastException due to incompatible dataset");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void findDomainBounds_WithEmptyCategoryDataset_ReturnsNull() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer(10, true);
        CategoryTableXYDataset dataset = new CategoryTableXYDataset();

        Range range = renderer.findDomainBounds(dataset);
        
        assertNull("Range should be null for empty dataset", range);
    }

    @Test(timeout = 4000)
    public void findDomainBounds_WithNullDataset_ReturnsNull() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        Range range = renderer.findDomainBounds(null);
        
        assertNull("Range should be null for null dataset", range);
    }

    //------------------ Tests for drawItem ------------------------------------

    @Test(timeout = 4000)
    public void drawItem_WithInvalidIndex_ThrowsIndexOutOfBounds() {
        setupDrawItemTestEnvironment();
        XYSeriesCollection<TimeSeriesDataItem> dataset = new XYSeriesCollection<>();

        try {
            clusteredXYBarRenderer0.drawItem(
                null, null, rectangle2D_Double0, plotRenderingInfo0, 
                combinedRangeXYPlot0, cyclicNumberAxis0, cyclicNumberAxis0, 
                dataset, 1, 10, categoryCrosshairState0, -3574
            );
            fail("Expected IndexOutOfBoundsException for invalid index");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void drawItem_WithIncompatibleDataset_ThrowsClassCastException() {
        setupDrawItemTestEnvironment();
        DefaultXYZDataset<Short> dataset = new DefaultXYZDataset<>();

        try {
            clusteredXYBarRenderer0.drawItem(
                null, null, rectangle2D_Double0, plotRenderingInfo0, 
                combinedRangeXYPlot0, cyclicNumberAxis0, cyclicNumberAxis0, 
                dataset, 2, -3493, categoryCrosshairState0, 2
            );
            fail("Expected ClassCastException for incompatible dataset");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void drawItem_WithNullDataset_ThrowsNullPointerException() {
        setupDrawItemTestEnvironment();
        clusteredXYBarRenderer0.setUseYInterval(true); // Alter renderer state

        try {
            clusteredXYBarRenderer0.drawItem(
                null, null, rectangle2D_Double0, plotRenderingInfo0, 
                combinedRangeXYPlot0, cyclicNumberAxis0, cyclicNumberAxis0, 
                null, 2, 0, categoryCrosshairState0, 1
            );
            fail("Expected NullPointerException for null dataset");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    //------------------ Tests for clone and equals ----------------------------

    @Test(timeout = 4000)
    public void equals_WithDifferentSettings_ReturnsFalse() {
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer(10, true);

        assertFalse("Renderers with different settings should not be equal", 
            renderer1.equals(renderer2));
    }

    @Test(timeout = 4000)
    public void equals_WithSameInstance_ReturnsTrue() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        assertTrue("Renderer should equal itself", 
            renderer.equals(renderer));
    }

    @Test(timeout = 4000)
    public void equals_WithDifferentObjectType_ReturnsFalse() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        assertFalse("Renderer should not equal different object type", 
            renderer.equals(new Object()));
    }

    @Test(timeout = 4000)
    public void clone_WithUncloneablePaintTransformer_ThrowsException() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        renderer.setGradientPaintTransformer(new DirectionalGradientPaintTransformer());

        try {
            renderer.clone();
            fail("Expected CloneNotSupportedException due to uncloneable transformer");
        } catch (CloneNotSupportedException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void clone_WithDefaultSettings_ReturnsEqualObject() throws Exception {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer clone = (ClusteredXYBarRenderer) renderer.clone();

        assertTrue("Clone should be equal to original", 
            renderer.equals(clone));
    }

    //------------------ Test for getPassCount ---------------------------------

    @Test(timeout = 4000)
    public void getPassCount_Always_ReturnsTwo() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        assertEquals("Pass count should always be 2", 
            2, renderer.getPassCount());
    }

    //------------------ Helper Methods ----------------------------------------

    private MockSimpleDateFormat createMockDateFormat() {
        Locale locale = new Locale("}y&~*+ |^", " q~^A)mz{I%kS}vYl\"h");
        DateFormatSymbols symbols = DateFormatSymbols.getInstance(locale);
        return new MockSimpleDateFormat("", symbols);
    }

    // Shared objects for drawItem tests
    private CombinedRangeXYPlot<Short> combinedRangeXYPlot0;
    private Rectangle2D.Double rectangle2D_Double0;
    private PlotRenderingInfo plotRenderingInfo0;
    private CategoryCrosshairState<Short, Short> categoryCrosshairState0;
    private ClusteredXYBarRenderer clusteredXYBarRenderer0;
    private CyclicNumberAxis cyclicNumberAxis0;

    private void setupDrawItemTestEnvironment() {
        combinedRangeXYPlot0 = new CombinedRangeXYPlot<>();
        rectangle2D_Double0 = new Rectangle2D.Double();
        plotRenderingInfo0 = new PlotRenderingInfo(null);
        categoryCrosshairState0 = new CategoryCrosshairState<>();
        clusteredXYBarRenderer0 = new ClusteredXYBarRenderer(0, false);
        cyclicNumberAxis0 = new CyclicNumberAxis(0, 0.0);
    }
}