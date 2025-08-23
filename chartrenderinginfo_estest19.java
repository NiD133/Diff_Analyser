package org.jfree.chart;

import org.jfree.chart.plot.PlotRenderingInfo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * A test suite for the equals() method of the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoEqualsTest {

    /**
     * Verifies that the equals() method returns false when the PlotRenderingInfo
     * of one object is modified, making it different from the other.
     */
    @Test
    public void equals_shouldReturnFalse_whenPlotInfoIsModified() {
        // Arrange: Create two identical ChartRenderingInfo objects.
        ChartRenderingInfo info1 = new ChartRenderingInfo();
        ChartRenderingInfo info2 = new ChartRenderingInfo();

        // Sanity check: The two objects should be equal initially.
        assertEquals("Initially, the two ChartRenderingInfo objects should be equal.", info1, info2);

        // Act: Modify the plot information of the first object by adding a subplot.
        // This change should cause the parent ChartRenderingInfo objects to become unequal.
        PlotRenderingInfo plotInfoForInfo1 = info1.getPlotInfo();
        plotInfoForInfo1.addSubplotInfo(new PlotRenderingInfo(info1));

        // Assert: The modified object should no longer be equal to the original.
        assertNotEquals("After modification, the objects should no longer be equal.", info1, info2);
    }
}