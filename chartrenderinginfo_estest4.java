package org.jfree.chart;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CombinedRangeCategoryPlot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link ChartRenderingInfo} class, focusing on its state after a chart rendering operation.
 */
public class ChartRenderingInfo_ESTestTest4 extends ChartRenderingInfo_ESTest_scaffolding {

    /**
     * Verifies that the entity collection within ChartRenderingInfo is populated
     * after a chart has been rendered.
     */
    @Test
    public void getEntityCollection_afterChartRendering_returnsPopulatedCollection() {
        // Arrange: Create a ChartRenderingInfo object and a simple chart to be rendered.
        ChartRenderingInfo renderingInfo = new ChartRenderingInfo();

        // A simple plot with one axis is sufficient to test entity creation.
        CombinedRangeCategoryPlot plot = new CombinedRangeCategoryPlot(new NumberAxis("Value"));
        JFreeChart chart = new JFreeChart("Test Chart", plot);

        int imageWidth = 100;
        int imageHeight = 100;

        // Act: Render the chart. This action should populate the renderingInfo object,
        // including the entity collection.
        chart.createBufferedImage(imageWidth, imageHeight, renderingInfo);

        // Assert: Check if the entity collection was created and contains the expected number of entities.
        EntityCollection entities = renderingInfo.getEntityCollection();
        assertNotNull("Entity collection should not be null after rendering", entities);

        // The rendering process creates entities for the chart background, the plot,
        // and the range axis.
        int expectedEntityCount = 3;
        assertEquals("The entity collection should contain the correct number of entities",
                expectedEntityCount, entities.getEntityCount());
    }
}