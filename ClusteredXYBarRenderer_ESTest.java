package org.jfree.chart.renderer.xy;

import org.jfree.chart.axis.CyclicNumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.util.DirectionalGradientPaintTransformer;
import org.jfree.data.Range;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.DefaultWindDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

import java.awt.geom.Rectangle2D;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Tests for the {@link ClusteredXYBarRenderer} class, focusing on its
 * data handling, rendering logic, and object behavior.
 */
public class ClusteredXYBarRendererTest {

    @Test
    public void getPassCount_shouldReturnTwo() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        // Act
        int passCount = renderer.getPassCount();

        // Assert
        assertEquals("The renderer should require two passes (one for shadows, one for bars).", 2, passCount);
    }

    // --- findDomainBounds Tests ---

    @Test
    public void findDomainBounds_shouldReturnNull_forNullDataset() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        // Act
        Range result = renderer.findDomainBounds(null);

        // Assert
        assertNull("The domain bounds should be null for a null dataset.", result);
    }

    @Test
    public void findDomainBounds_shouldReturnNull_forEmptyDataset() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset emptyDataset = new SimpleHistogramDataset("key");

        // Act
        Range result = renderer.findDomainBounds(emptyDataset);

        // Assert
        assertNull("The domain bounds should be null for an empty dataset.", result);
    }

    @Test
    public void findDomainBounds_shouldReturnCorrectRange_forSingleBin() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset dataset = new SimpleHistogramDataset("key");
        dataset.addBin(new SimpleHistogramBin(-901.51, -465.48, true, true));

        // Act
        Range result = renderer.findDomainBounds(dataset);

        // Assert
        assertNotNull(result);
        assertEquals(-901.51, result.getLowerBound(), 0.01);
        assertEquals(-465.48, result.getUpperBound(), 0.01);
    }

    @Test
    public void findDomainBounds_shouldHandleTimeBasedDataset() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        // Use a specific timestamp for clarity, instead of complex DateFormat logic.
        long time = 1392409281320L;
        Date date = new Date(time);
        OHLCDataItem[] items = {new OHLCDataItem(date, 1.0, 2.0, 0.5, 1.5, 100)};
        DefaultOHLCDataset dataset = new DefaultOHLCDataset("key", items);

        // Act
        Range result = renderer.findDomainBounds(dataset);

        // Assert
        assertNotNull(result);
        // OHLCDataset uses the date's time in milliseconds as the x-value.
        assertEquals("The range should be centered on the item's timestamp.", (double) time, result.getCentralValue(), 0.01);
    }

    @Test(expected = ClassCastException.class)
    public void findDomainBounds_shouldThrowException_whenDatasetIsNotIntervalXYDataset() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        // DefaultWindDataset is not an IntervalXYDataset, which this method expects.
        XYDataset nonIntervalDataset = new DefaultWindDataset();

        // Act
        renderer.findDomainBounds(nonIntervalDataset);
        // Assert: Exception expected.
    }

    // --- findDomainBoundsWithOffset Tests ---

    @Test
    public void findDomainBoundsWithOffset_shouldReturnNull_forEmptyDataset() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset emptyDataset = new SimpleHistogramDataset("key");

        // Act
        Range result = renderer.findDomainBoundsWithOffset(emptyDataset);

        // Assert
        assertNull("The offset domain bounds should be null for an empty dataset.", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findDomainBoundsWithOffset_shouldThrowException_whenDatasetIsNull() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        // Act
        renderer.findDomainBoundsWithOffset(null);
        // Assert: Exception expected.
    }

    @Test
    public void findDomainBoundsWithOffset_shouldCenterRangeOnBinMidpoint() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset dataset = new SimpleHistogramDataset("key");
        // Bin from 1.0 to 3.0, width is 2.0. Offset is width/2 = 1.0.
        // The new range should be [1.0 - 1.0, 3.0 - 1.0] = [0.0, 2.0].
        dataset.addBin(new SimpleHistogramBin(1.0, 3.0, true, true));

        // Act
        Range result = renderer.findDomainBoundsWithOffset(dataset);

        // Assert
        assertNotNull(result);
        assertEquals("Lower bound should be offset by half the bin width.", 0.0, result.getLowerBound(), 0.001);
        assertEquals("Upper bound should be offset by half the bin width.", 2.0, result.getUpperBound(), 0.001);
    }

    @Test
    public void findDomainBoundsWithOffset_shouldCombineRangesFromMultipleBins() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        SimpleHistogramDataset dataset = new SimpleHistogramDataset("key");
        // Bin 1: [-10, 0], width 10. Offset range: [-15, -5]
        dataset.addBin(new SimpleHistogramBin(-10.0, 0.0, true, true));
        // Bin 2: [10, 30], width 20. Offset range: [0, 20]
        dataset.addBin(new SimpleHistogramBin(10.0, 30.0, true, true));

        // Act
        Range result = renderer.findDomainBoundsWithOffset(dataset);

        // Assert
        assertNotNull(result);
        // The combined range should be [-15, 20].
        assertEquals("Lower bound should be from the first offset bin.", -15.0, result.getLowerBound(), 0.001);
        assertEquals("Upper bound should be from the second offset bin.", 20.0, result.getUpperBound(), 0.001);
    }

    @Test
    public void findDomainBoundsWithOffset_shouldAccountForDefaultIntervalWidth_whenUsingXYSeriesCollection() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        XYSeries series = new XYSeries("key");
        series.add(0.0, 10.0);
        // XYSeriesCollection has a default interval width of 1.0 around the x-value.
        // So, the interval for x=0.0 is [-0.5, 0.5], with a width of 1.0.
        // The offset is width/2 = 0.5.
        // The new range should be [-0.5 - 0.5, 0.5 - 0.5] = [-1.0, 0.0].
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        // Act
        Range result = renderer.findDomainBoundsWithOffset(dataset);

        // Assert
        assertNotNull(result);
        assertEquals("Lower bound should be offset correctly.", -1.0, result.getLowerBound(), 0.001);
        assertEquals("Upper bound should be offset correctly.", 0.0, result.getUpperBound(), 0.001);
        assertEquals("Central value should be -0.5.", -0.5, result.getCentralValue(), 0.001);
    }

    // --- drawItem Tests ---

    @Test(expected = IndexOutOfBoundsException.class)
    public void drawItem_shouldThrowException_forInvalidSeriesIndex() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        XYPlot plot = new XYPlot();
        ValueAxis axis = new CyclicNumberAxis(10);
        XYSeriesCollection dataset = new XYSeriesCollection(new XYSeries("S1")); // Only 1 series (index 0)

        // Act
        renderer.drawItem(null, null, new Rectangle2D.Double(), null, plot, axis, axis, dataset,
                1, // Invalid series index
                0, null, 0);
        // Assert: Exception expected.
    }

    @Test(expected = ClassCastException.class)
    public void drawItem_shouldThrowException_whenDatasetIsNotIntervalXYDataset() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        XYPlot plot = new XYPlot();
        ValueAxis axis = new CyclicNumberAxis(10);
        // DefaultXYZDataset is not an IntervalXYDataset, which this method expects.
        XYDataset nonIntervalDataset = new DefaultXYZDataset();

        // Act
        renderer.drawItem(null, null, new Rectangle2D.Double(), null, plot, axis, axis, nonIntervalDataset, 0, 0, null, 0);
        // Assert: Exception expected.
    }

    @Test(expected = NullPointerException.class)
    public void drawItem_shouldThrowNPE_whenDatasetIsNullAndYIntervalsAreUsed() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        renderer.setUseYInterval(true);
        XYPlot plot = new XYPlot();
        ValueAxis axis = new CyclicNumberAxis(10);

        // Act
        renderer.drawItem(null, null, new Rectangle2D.Double(), null, plot, axis, axis,
                null, // Null dataset
                0, 0, null, 0);
        // Assert: Exception expected.
    }

    // --- equals() and clone() Tests ---

    @Test
    public void equals_shouldReturnTrue_forSameInstance() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        // Act & Assert
        assertTrue("A renderer should be equal to itself.", renderer.equals(renderer));
    }

    @Test
    public void equals_shouldReturnTrue_forEqualInstances() {
        // Arrange
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer(0.1, true);
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer(0.1, true);

        // Act & Assert
        assertTrue("Renderers with the same configuration should be equal.", renderer1.equals(renderer2));
    }

    @Test
    public void equals_shouldReturnFalse_forDifferentObjectType() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        Object otherObject = new Object();

        // Act & Assert
        assertFalse("A renderer should not be equal to an object of a different type.", renderer.equals(otherObject));
    }

    @Test
    public void equals_shouldReturnFalse_forRenderersWithDifferentMargin() {
        // Arrange
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer(0.1, false);
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer(0.2, false);

        // Act & Assert
        assertFalse("Renderers with different margins should not be equal.", renderer1.equals(renderer2));
    }

    @Test
    public void equals_shouldReturnFalse_forRenderersWithDifferentCenterBarAtStartValueFlag() {
        // Arrange
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer(0.1, false);
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer(0.1, true);

        // Act & Assert
        assertFalse("Renderers with different centerBarAtStartValue flags should not be equal.", renderer1.equals(renderer2));
    }

    @Test
    public void clone_shouldCreateEqualInstance() throws CloneNotSupportedException {
        // Arrange
        ClusteredXYBarRenderer original = new ClusteredXYBarRenderer(0.15, true);

        // Act
        ClusteredXYBarRenderer clone = (ClusteredXYBarRenderer) original.clone();

        // Assert
        assertNotSame("The clone should be a different instance.", original, clone);
        assertEquals("The clone should be equal to the original.", original, clone);
    }

    @Test(expected = CloneNotSupportedException.class)
    public void clone_shouldThrowException_whenGradientPaintTransformerIsNotCloneable() throws CloneNotSupportedException {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        // DirectionalGradientPaintTransformer does not implement Cloneable
        renderer.setGradientPaintTransformer(new DirectionalGradientPaintTransformer());

        // Act
        renderer.clone();
        // Assert: Exception expected.
    }
}