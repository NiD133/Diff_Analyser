package org.jfree.chart.renderer.xy;

import org.junit.Test;
import org.junit.runner.RunWith;
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

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true, 
    useVFS = true, 
    useVNET = true, 
    resetStaticState = true, 
    separateClassLoader = true
)
public class ClusteredXYBarRenderer_ESTest extends ClusteredXYBarRenderer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithNegativeInfinity() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(JLayeredPane.POPUP_LAYER);
        SimpleHistogramBin bin = new SimpleHistogramBin(Double.NEGATIVE_INFINITY, (double) renderer.ZERO);
        dataset.addBin(bin);

        Range range = renderer.findDomainBoundsWithOffset(dataset);

        assertEquals(Double.NEGATIVE_INFINITY, range.getCentralValue(), 0.01);
        assertNotNull(range);
        assertEquals(Double.NaN, range.getLength(), 0.01);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithMultipleBins() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(JLayeredPane.POPUP_LAYER);
        dataset.addBin(new SimpleHistogramBin(907.69, 1031.047, true, false));
        dataset.addBin(new SimpleHistogramBin(-370.1426, (double) renderer.ZERO));

        Range range = renderer.findDomainBoundsWithOffset(dataset);

        assertEquals(1524.5825, range.getLength(), 0.01);
        assertNotNull(range);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithXYSeries() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        XYSeries<Boolean> series = new XYSeries<>(Boolean.valueOf(""));
        series.add((double) renderer.ZERO, 30.0);
        XYSeriesCollection<Boolean> collection = new XYSeriesCollection<>(series);

        Range range = renderer.findDomainBoundsWithOffset(collection);

        assertNotNull(range);
        assertEquals(-0.5, range.getCentralValue(), 0.01);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithSingleBin() throws Throwable {
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(JLayeredPane.PALETTE_LAYER);
        dataset.addBin(new SimpleHistogramBin(1, 3));
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        Range range = renderer.findDomainBoundsWithOffset(dataset);

        assertEquals(1.0, range.getCentralValue(), 0.01);
        assertEquals(0.0, range.getLowerBound(), 0.01);
        assertNotNull(range);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithZeroBin() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(JLayeredPane.FRAME_CONTENT_LAYER);
        dataset.addBin(new SimpleHistogramBin((double) renderer.ZERO, 0.2));

        Range range = renderer.findDomainBoundsWithOffset(dataset);

        assertEquals(0.0, range.getCentralValue(), 0.01);
        assertNotNull(range);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithLargeBin() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(JLayeredPane.DRAG_LAYER);
        dataset.addBin(new SimpleHistogramBin(1249.0, 2514.4173606));

        Range range = renderer.findDomainBoundsWithOffset(dataset);

        assertEquals(1249.0, range.getCentralValue(), 0.01);
        assertNotNull(range);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithoutOffset() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(JLayeredPane.MODAL_LAYER);
        dataset.addBin(new SimpleHistogramBin(-901.5175700727494, -465.48054286));

        Range range = renderer.findDomainBounds(dataset);

        assertEquals(-683.4990564663747, range.getCentralValue(), 0.01);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithValidRange() throws Throwable {
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(JLayeredPane.PALETTE_LAYER);
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        dataset.addBin(new SimpleHistogramBin(0.0, 1.0F, true, true));

        Range range = renderer.findDomainBounds(dataset);

        assertFalse(range.isNaNRange());
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithOHLCData() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        OHLCDataItem[] dataItems = new OHLCDataItem[5];
        Locale locale = new Locale("}y&~*+ |^", " q~^A)mz{I%kS}vYl\"h");
        DateFormatSymbols dateFormatSymbols = DateFormatSymbols.getInstance(locale);
        MockSimpleDateFormat mockDateFormat = new MockSimpleDateFormat("", dateFormatSymbols);
        Date date = mockDateFormat.get2DigitYearStart();
        OHLCDataItem dataItem = new OHLCDataItem(date, 1641.62203, 1376.0317, 1376.0317, 1641.62203, 597.5355594430192);
        dataItems[0] = dataItem;
        dataItems[1] = dataItems[0];
        dataItems[2] = dataItems[1];
        dataItems[3] = dataItems[1];
        dataItems[4] = dataItems[0];
        DefaultOHLCDataset dataset = new DefaultOHLCDataset(renderer.ZERO, dataItems);

        Range range = renderer.findDomainBounds(dataset);

        assertEquals(1.39240928132E12, range.getLowerBound(), 0.01);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithNegativeBin() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(JLayeredPane.FRAME_CONTENT_LAYER);
        dataset.addBin(new SimpleHistogramBin(-673.5246, (double) renderer.ZERO));

        Range range = renderer.findDomainBounds(dataset);

        assertNotNull(range);
        assertEquals(0.0, range.getUpperBound(), 0.01);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithNullBin() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(JLayeredPane.DEFAULT_LAYER);
        dataset.addBin(null);

        try {
            renderer.findDomainBoundsWithOffset(dataset);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.data.statistics.SimpleHistogramDataset", e);
        }
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithNullDataset() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        try {
            renderer.findDomainBoundsWithOffset((IntervalXYDataset) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithNullBoxAndWhiskerDataset() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        DefaultBoxAndWhiskerXYDataset<Short> dataset = new DefaultBoxAndWhiskerXYDataset<>(null);

        try {
            renderer.findDomainBounds(dataset);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.data.general.AbstractSeriesDataset", e);
        }
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithInvalidDatasetType() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer(-3079.59, true);
        DefaultWindDataset dataset = new DefaultWindDataset();

        try {
            renderer.findDomainBounds(dataset);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.jfree.chart.renderer.xy.ClusteredXYBarRenderer", e);
        }
    }

    @Test(timeout = 4000)
    public void testDrawItemWithInvalidIndex() throws Throwable {
        CombinedRangeXYPlot<Short> plot = new CombinedRangeXYPlot<>();
        Rectangle2D.Double dataArea = new Rectangle2D.Double();
        PlotRenderingInfo info = new PlotRenderingInfo(null);
        CategoryCrosshairState<Short, Short> crosshairState = new CategoryCrosshairState<>();
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer(0, false);
        CyclicNumberAxis domainAxis = new CyclicNumberAxis(0, 0.0);
        XYSeriesCollection<TimeSeriesDataItem> dataset = new XYSeriesCollection<>();

        try {
            renderer.drawItem(null, null, dataArea, info, plot, domainAxis, domainAxis, dataset, 1, 10, crosshairState, -3574);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDrawItemWithInvalidDatasetType() throws Throwable {
        CombinedRangeXYPlot<Short> plot = new CombinedRangeXYPlot<>();
        Rectangle2D.Double dataArea = new Rectangle2D.Double();
        PlotRenderingInfo info = new PlotRenderingInfo(null);
        CategoryCrosshairState<Short, Short> crosshairState = new CategoryCrosshairState<>();
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer(2, false);
        CyclicNumberAxis domainAxis = new CyclicNumberAxis(0.0, 0.0);
        DefaultXYZDataset<Short> dataset = new DefaultXYZDataset<>();

        try {
            renderer.drawItem(null, null, dataArea, info, plot, domainAxis, domainAxis, dataset, 2, -3493, crosshairState, 2);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.jfree.chart.renderer.xy.ClusteredXYBarRenderer", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloneWithGradientPaintTransformer() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        DirectionalGradientPaintTransformer transformer = new DirectionalGradientPaintTransformer();
        renderer.setGradientPaintTransformer(transformer);

        try {
            renderer.clone();
            fail("Expecting exception: CloneNotSupportedException");
        } catch (CloneNotSupportedException e) {
            verifyException("org.jfree.chart.internal.CloneUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentRenderer() throws Throwable {
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer(10, true);

        boolean result = renderer2.equals(renderer1);

        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameRenderer() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        boolean result = renderer.equals(renderer);

        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentObjectType() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        Object obj = new Object();

        boolean result = renderer.equals(obj);

        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testDrawItemWithNullDataset() throws Throwable {
        CombinedRangeXYPlot<Short> plot = new CombinedRangeXYPlot<>();
        Rectangle2D.Double dataArea = new Rectangle2D.Double();
        PlotRenderingInfo info = new PlotRenderingInfo(null);
        CategoryCrosshairState<Short, Short> crosshairState = new CategoryCrosshairState<>();
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer(0, false);
        renderer.setUseYInterval(true);
        CyclicNumberAxis domainAxis = new CyclicNumberAxis(2);

        try {
            renderer.drawItem(null, null, dataArea, info, plot, domainAxis, domainAxis, null, 2, 0, crosshairState, 1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.xy.ClusteredXYBarRenderer", e);
        }
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithEmptyDataset() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset<Integer> dataset = new SimpleHistogramDataset<>(JLayeredPane.MODAL_LAYER);

        Range range = renderer.findDomainBoundsWithOffset(dataset);

        assertNull(range);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithEmptyCategoryTableDataset() throws Throwable {
        CategoryTableXYDataset dataset = new CategoryTableXYDataset();
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer(10, true);

        Range range = renderer.findDomainBounds(dataset);

        assertNull(range);
    }

    @Test(timeout = 4000)
    public void testFindDomainBoundsWithNullXYDataset() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        Range range = renderer.findDomainBounds((XYDataset) null);

        assertNull(range);
    }

    @Test(timeout = 4000)
    public void testGetPassCount() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        int passCount = renderer.getPassCount();

        assertEquals(2, passCount);
    }

    @Test(timeout = 4000)
    public void testCloneAndEquals() throws Throwable {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        Object clonedRenderer = renderer.clone();

        boolean result = renderer.equals(clonedRenderer);

        assertTrue(result);
    }
}