package org.jfree.chart;

import org.jfree.chart.plot.PlotRenderingInfo;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Contains focused tests for the {@link ChartRenderingInfo} class.
 */
public class ChartRenderingInfoTest {

    /**
     * Verifies that calling hashCode() on a ChartRenderingInfo instance throws a
     * StackOverflowError if its PlotRenderingInfo contains a circular reference.
     * This scenario occurs when a plot is added as its own subplot, creating an
     * infinite loop during hash code calculation.
     */
    @Test
    public void hashCode_withCircularPlotReference_throwsStackOverflowError() {
        // Arrange: Create a ChartRenderingInfo and set up a circular reference
        // where its PlotRenderingInfo is also its own subplot.
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();
        plotInfo.addSubplotInfo(plotInfo); // Create the cycle

        // Act & Assert: Verify that calling hashCode() causes a StackOverflowError.
        // The assertThrows method clearly expresses the expectation that an exception
        // will be thrown by the provided lambda expression.
        assertThrows(StackOverflowError.class, chartInfo::hashCode);
    }
}