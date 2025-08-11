package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.Locale;
import javax.swing.JLayeredPane;
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

/**
 * Test suite for ClusteredXYBarRenderer functionality.
 * Tests domain bounds calculation, rendering behavior, and object equality.
 */
public class ClusteredXYBarRenderer_ESTest {

    // Constants for better readability
    private static final double DELTA = 0.01;
    private static final int POPUP_LAYER = JLayeredPane.POPUP_LAYER;
    private static final int PALETTE_LAYER = JLayeredPane.PALETTE_LAYER;
    private static final int FRAME_CONTENT_LAYER = JLayeredPane.FRAME_CONTENT_LAYER;
    private static final int DRAG_LAYER = JLayeredPane.DRAG_LAYER;
    private static final int MODAL_LAYER = JLayeredPane.MODAL_LAYER;
    private static final int DEFAULT_LAYER = JLayeredPane.DEFAULT_LAYER;

    // Domain Bounds Tests with Offset

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithOffset_NegativeInfinityBin() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = createHistogramDataset(POPUP_LAYER);
        
        SimpleHistogramBin bin = new SimpleHistogramBin(Double.NEGATIVE_INFINITY, 0.0);
        dataset.addBin(bin);
        
        Range result = renderer.findDomainBoundsWithOffset(dataset);
        
        assertNotNull("Range should not be null", result);
        assertEquals("Central value should be negative infinity", 
                    Double.NEGATIVE_INFINITY, result.getCentralValue(), DELTA);
        assertEquals("Length should be NaN", Double.NaN, result.getLength(), DELTA);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithOffset_MultipleBins() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = createHistogramDataset(POPUP_LAYER);
        
        // Add two bins with different ranges
        dataset.addBin(new SimpleHistogramBin(907.69, 1031.047, true, false));
        dataset.addBin(new SimpleHistogramBin(-370.14, 0.0));
        
        Range result = renderer.findDomainBoundsWithOffset(dataset);
        
        assertNotNull("Range should not be null", result);
        assertEquals("Range length should be calculated correctly", 
                    1524.58, result.getLength(), 1.0); // Allowing larger delta for this calculation
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithOffset_XYSeriesCollection() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        
        XYSeries<Boolean> series = new XYSeries<>(Boolean.FALSE);
        series.add(0.0, 30.0);
        XYSeriesCollection<Boolean> dataset = new XYSeriesCollection<>(series);
        
        Range result = renderer.findDomainBoundsWithOffset(dataset);
        
        assertNotNull("Range should not be null", result);
        assertEquals("Central value should account for offset", 
                    -0.5, result.getCentralValue(), DELTA);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithOffset_PositiveRange() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = createHistogramDataset(PALETTE_LAYER);
        
        dataset.addBin(new SimpleHistogramBin(1, 3));
        
        Range result = renderer.findDomainBoundsWithOffset(dataset);
        
        assertNotNull("Range should not be null", result);
        assertEquals("Central value should be 1.0", 1.0, result.getCentralValue(), DELTA);
        assertEquals("Lower bound should be 0.0", 0.0, result.getLowerBound(), DELTA);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithOffset_ZeroStartBin() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = createHistogramDataset(FRAME_CONTENT_LAYER);
        
        dataset.addBin(new SimpleHistogramBin(0.0, 0.2));
        
        Range result = renderer.findDomainBoundsWithOffset(dataset);
        
        assertNotNull("Range should not be null", result);
        assertEquals("Central value should be 0.0", 0.0, result.getCentralValue(), DELTA);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithOffset_LargePositiveRange() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = createHistogramDataset(DRAG_LAYER);
        
        dataset.addBin(new SimpleHistogramBin(1249.0, 2514.42));
        
        Range result = renderer.findDomainBoundsWithOffset(dataset);
        
        assertNotNull("Range should not be null", result);
        assertEquals("Central value should be 1249.0", 1249.0, result.getCentralValue(), DELTA);
    }

    // Domain Bounds Tests without Offset

    @Test(timeout = 4000)
    public void testFindDomainBounds_NegativeRange() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = createHistogramDataset(MODAL_LAYER);
        
        dataset.addBin(new SimpleHistogramBin(-901.52, -465.48));
        
        Range result = renderer.findDomainBounds(dataset);
        
        assertNotNull("Range should not be null", result);
        assertEquals("Central value should be calculated correctly", 
                    -683.50, result.getCentralValue(), 1.0);
    }

    @Test(timeout = 4000)
    public void testFindDomainBounds_InclusiveBounds() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = createHistogramDataset(PALETTE_LAYER);
        
        dataset.addBin(new SimpleHistogramBin(0.0, 1.0F, true, true));
        
        Range result = renderer.findDomainBounds(dataset);
        
        assertNotNull("Range should not be null", result);
        assertFalse("Range should not be NaN", result.isNaNRange());
    }

    @Test(timeout = 4000)
    public void testFindDomainBounds_OHLCDataset() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        
        // Create OHLC dataset with mock date
        OHLCDataItem[] items = new OHLCDataItem[5];
        Date testDate = createMockDate();
        OHLCDataItem item = new OHLCDataItem(testDate, 1641.62, 1376.03, 1376.03, 1641.62, 597.54);
        
        // Fill array with same item for simplicity
        for (int i = 0; i < items.length; i++) {
            items[i] = item;
        }
        
        DefaultOHLCDataset dataset = new DefaultOHLCDataset("Test", items);
        Range result = renderer.findDomainBounds(dataset);
        
        assertNotNull("Range should not be null", result);
        assertTrue("Lower bound should be positive", result.getLowerBound() > 0);
    }

    @Test(timeout = 4000)
    public void testFindDomainBounds_NegativeToZeroRange() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = createHistogramDataset(FRAME_CONTENT_LAYER);
        
        dataset.addBin(new SimpleHistogramBin(-673.52, 0.0));
        
        Range result = renderer.findDomainBounds(dataset);
        
        assertNotNull("Range should not be null", result);
        assertEquals("Upper bound should be 0.0", 0.0, result.getUpperBound(), DELTA);
    }

    // Error Handling Tests

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithOffset_NullBin_ThrowsException() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = createHistogramDataset(DEFAULT_LAYER);
        dataset.addBin(null);
        
        try {
            renderer.findDomainBoundsWithOffset(dataset);
            fail("Should throw NullPointerException for null bin");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithOffset_NullDataset_ThrowsException() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        
        try {
            renderer.findDomainBoundsWithOffset(null);
            fail("Should throw IllegalArgumentException for null dataset");
        } catch (IllegalArgumentException e) {
            assertEquals("Error message should be specific", 
                        "Null 'dataset' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testFindDomainBounds_InvalidDatasetType_ThrowsException() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer(-3079.59, true);
        DefaultWindDataset dataset = new DefaultWindDataset();
        
        try {
            renderer.findDomainBounds(dataset);
            fail("Should throw ClassCastException for incompatible dataset type");
        } catch (ClassCastException e) {
            assertTrue("Error message should mention casting issue", 
                      e.getMessage().contains("cannot be cast"));
        }
    }

    // Drawing Tests

    @Test(timeout = 4000)
    public void testDrawItem_EmptyDataset_ThrowsException() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer(0, false);
        XYSeriesCollection<TimeSeriesDataItem> dataset = new XYSeriesCollection<>();
        
        try {
            drawItemWithTestParameters(renderer, dataset, 1, 10);
            fail("Should throw IndexOutOfBoundsException for empty dataset");
        } catch (IndexOutOfBoundsException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testDrawItem_IncompatibleDatasetType_ThrowsException() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer(2, false);
        DefaultXYZDataset<Short> dataset = new DefaultXYZDataset<>();
        
        try {
            drawItemWithTestParameters(renderer, dataset, 2, -3493);
            fail("Should throw ClassCastException for incompatible dataset");
        } catch (ClassCastException e) {
            assertTrue("Error should mention casting issue", 
                      e.getMessage().contains("cannot be cast"));
        }
    }

    @Test(timeout = 4000)
    public void testDrawItem_WithYInterval_NullDataset_ThrowsException() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer(0, false);
        renderer.setUseYInterval(true);
        
        try {
            drawItemWithTestParameters(renderer, null, 2, 0);
            fail("Should throw NullPointerException for null dataset with Y interval");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // Object Behavior Tests

    @Test(timeout = 4000)
    public void testClone_WithGradientTransformer_ThrowsException() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        renderer.setGradientPaintTransformer(new DirectionalGradientPaintTransformer());
        
        try {
            renderer.clone();
            fail("Should throw CloneNotSupportedException with gradient transformer");
        } catch (CloneNotSupportedException e) {
            assertTrue("Error message should mention cloning issue", 
                      e.getMessage().contains("clone"));
        }
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentRenderers_ReturnsFalse() throws Throwable {
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer(10, true);
        
        assertFalse("Different renderers should not be equal", renderer1.equals(renderer2));
    }

    @Test(timeout = 4000)
    public void testEquals_SameInstance_ReturnsTrue() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        
        assertTrue("Renderer should equal itself", renderer.equals(renderer));
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentObjectType_ReturnsFalse() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        Object other = new Object();
        
        assertFalse("Renderer should not equal different object type", renderer.equals(other));
    }

    @Test(timeout = 4000)
    public void testEquals_ClonedObject_ReturnsTrue() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        Object cloned = renderer.clone();
        
        assertTrue("Renderer should equal its clone", renderer.equals(cloned));
    }

    // Boundary Cases

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithOffset_EmptyDataset_ReturnsNull() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = createHistogramDataset(MODAL_LAYER);
        
        Range result = renderer.findDomainBoundsWithOffset(dataset);
        
        assertNull("Empty dataset should return null range", result);
    }

    @Test(timeout = 4000)
    public void testFindDomainBounds_EmptyDataset_ReturnsNull() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer(10, true);
        CategoryTableXYDataset dataset = new CategoryTableXYDataset();
        
        Range result = renderer.findDomainBounds(dataset);
        
        assertNull("Empty dataset should return null range", result);
    }

    @Test(timeout = 4000)
    public void testFindDomainBounds_NullDataset_ReturnsNull() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        
        Range result = renderer.findDomainBounds((XYDataset) null);
        
        assertNull("Null dataset should return null range", result);
    }

    @Test(timeout = 4000)
    public void testGetPassCount_ReturnsTwo() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        
        int passCount = renderer.getPassCount();
        
        assertEquals("Pass count should be 2", 2, passCount);
    }

    // Helper Methods

    private SimpleHistogramDataset<Integer> createHistogramDataset(Integer key) {
        return new SimpleHistogramDataset<>(key);
    }

    private Date createMockDate() {
        Locale locale = new Locale("test", "locale");
        DateFormatSymbols symbols = DateFormatSymbols.getInstance(locale);
        MockSimpleDateFormat format = new MockSimpleDateFormat("", symbols);
        return format.get2DigitYearStart();
    }

    private void drawItemWithTestParameters(ClusteredXYBarRenderer renderer, XYDataset dataset, 
                                          int series, int item) {
        CombinedRangeXYPlot<Short> plot = new CombinedRangeXYPlot<>();
        Rectangle2D.Double dataArea = new Rectangle2D.Double();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(null);
        CategoryCrosshairState<Short, Short> crosshairState = new CategoryCrosshairState<>();
        CyclicNumberAxis axis = new CyclicNumberAxis(0.0, 0.0);
        
        renderer.drawItem(null, null, dataArea, plotInfo, plot, axis, axis, 
                         dataset, series, item, crosshairState, 1);
    }
}