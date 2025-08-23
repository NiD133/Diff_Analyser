package org.jfree.chart.renderer.xy;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.CyclicNumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.fail;

// The test class name is retained from the original to show its context.
public class ClusteredXYBarRenderer_ESTestTest15 extends ClusteredXYBarRenderer_ESTest_scaffolding {

    /**
     * Verifies that drawItem() throws an IndexOutOfBoundsException when trying to
     * render an item from an empty dataset.
     * <p>
     * The method should perform bounds checking before attempting to access data
     * from the dataset.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void drawItem_withEmptyDataset_shouldThrowIndexOutOfBoundsException() {
        // Arrange: Set up the renderer and all required plotting context.
        // The key part of the setup is an empty dataset.
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        XYDataset emptyDataset = new XYSeriesCollection();

        // Mock the required arguments for the drawItem method. Most can be null or
        // default objects because the exception is expected before they are used.
        Graphics2D g2 = null;
        XYItemRendererState rendererState = null;
        Rectangle2D dataArea = new Rectangle2D.Double();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(new ChartRenderingInfo());
        XYPlot plot = new XYPlot();
        ValueAxis domainAxis = new CyclicNumberAxis(0.0);
        ValueAxis rangeAxis = new CyclicNumberAxis(0.0);
        CrosshairState crosshairState = new CrosshairState();
        
        int invalidSeriesIndex = 1; // Any index is invalid for an empty dataset.
        int invalidItemIndex = 10;  // Any index is invalid.
        int pass = 1;               // The pass number is not relevant for this exception.

        // Act: Call the method under test with invalid indices.
        // The @Test(expected=...) annotation will automatically handle the assertion,
        // causing the test to pass if an IndexOutOfBoundsException is thrown.
        renderer.drawItem(g2, rendererState, dataArea, plotInfo, plot,
                domainAxis, rangeAxis, emptyDataset, invalidSeriesIndex, invalidItemIndex,
                crosshairState, pass);
    }
}