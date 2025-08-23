package org.jfree.chart.renderer.xy;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.CyclicNumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedRangeXYPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This test class focuses on the behavior of the ClusteredXYBarRenderer.
 * The original test class name is preserved to maintain context, but in a
 * real-world scenario, it would be renamed to ClusteredXYBarRendererTest.
 */
public class ClusteredXYBarRenderer_ESTestTest16 {

    /**
     * Verifies that the drawItem() method throws a ClassCastException when
     * provided with a dataset that does not implement the IntervalXYDataset
     * interface. The renderer requires interval data to correctly calculate
     * bar positions and widths, and this test ensures it fails predictably
     * with incompatible data types.
     */
    @Test(timeout = 4000)
    public void drawItem_withNonIntervalDataset_shouldThrowClassCastException() {
        // Arrange: Create a renderer and a dataset that is NOT an IntervalXYDataset.
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        // DefaultXYZDataset is a valid XYDataset but does not provide interval information.
        XYDataset nonIntervalDataset = new DefaultXYZDataset();

        // Arrange: Set up dummy objects required by the drawItem() method signature.
        // These are not central to the test's logic but are necessary for the method call.
        XYPlot plot = new CombinedRangeXYPlot<>();
        Rectangle2D dataArea = new Rectangle2D.Double();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(new ChartRenderingInfo());
        ValueAxis domainAxis = new CyclicNumberAxis(0.0);
        ValueAxis rangeAxis = new CyclicNumberAxis(0.0);
        int series = 0;
        int item = 0;
        int pass = 0;

        // Act & Assert: Expect a ClassCastException when calling drawItem() with the
        // incompatible dataset.
        try {
            renderer.drawItem(
                    null,           // g2 (Graphics2D)
                    null,           // state (XYItemRendererState)
                    dataArea,
                    plotInfo,
                    plot,
                    domainAxis,
                    rangeAxis,
                    nonIntervalDataset,
                    series,
                    item,
                    null,           // crosshairState
                    pass
            );
            fail("A ClassCastException was expected because the dataset is not an IntervalXYDataset.");
        } catch (ClassCastException e) {
            // Success: The expected exception was caught.
            // Verify the exception message to confirm the cause of the failure.
            String expectedMessageFragment = "cannot be cast to class org.jfree.data.xy.IntervalXYDataset";
            assertTrue(
                "The exception message should clearly indicate the casting error.",
                e.getMessage().contains(expectedMessageFragment)
            );
        }
    }
}