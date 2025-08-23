package org.jfree.chart.renderer.xy;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.*;

/**
 * A set of tests for the {@link DeviationRenderer} class.
 */
public class DeviationRendererTest {

    /**
     * Verifies that the initialise() method returns a correctly configured state object
     * and confirms that the renderer's default properties are set as expected upon instantiation.
     */
    @Test
    public void initialise_shouldReturnCorrectStateAndConfirmDefaultProperties() {
        // Arrange: Set up the renderer and the necessary mock/dummy objects for the initialise method.
        DeviationRenderer renderer = new DeviationRenderer();
        Rectangle2D dataArea = new Rectangle2D.Double();
        XYPlot plot = new XYPlot();
        XYDataset dataset = new TimeSeriesCollection();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(new ChartRenderingInfo());
        
        // The Graphics2D context is not used in the initialise method, so null is acceptable.
        Graphics2D g2 = null;

        // Act: Call the initialise method to get the renderer state.
        XYItemRendererState state = renderer.initialise(g2, dataArea, plot, dataset, plotInfo);

        // Assert: Verify the default properties of the renderer and the returned state object.
        assertNotNull("The initialised state object should not be null.", state);

        // 1. Verify default properties of the DeviationRenderer instance.
        assertTrue("The 'drawSeriesLineAsPath' property should be true by default for DeviationRenderer.",
                renderer.getDrawSeriesLineAsPath());
        assertEquals("The default alpha transparency should be 0.5f.", 0.5F, renderer.getAlpha(), 0.001F);

        // 2. Verify a key property of the state object returned by initialise().
        //    DeviationRenderer requires processing all items to draw deviation bands correctly.
        assertFalse("The 'processVisibleItemsOnly' flag in the state should be false.",
                state.getProcessVisibleItemsOnly());
    }
}