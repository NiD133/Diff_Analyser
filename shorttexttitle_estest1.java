package org.jfree.chart.title;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Provides tests for the {@link ShortTextTitle} class.
 */
public class ShortTextTitleTest {

    /**
     * Verifies that a chart containing a ShortTextTitle can be rendered successfully
     * without throwing an exception. This acts as a basic integration and smoke test,
     * ensuring the title's drawing logic is sound.
     */
    @Test
    public void renderChartWithShortTextTitleSucceeds() {
        // Arrange: Create a chart and add a ShortTextTitle as a subtitle.
        // The original test used an empty string, which is a good edge case to keep.
        ShortTextTitle title = new ShortTextTitle("");
        JFreeChart chart = new JFreeChart("Chart Title", new XYPlot());
        chart.setSubtitles(Collections.singletonList(title));

        // Act: Render the chart to a small buffered image. The primary goal is to
        // ensure this operation completes without throwing an exception.
        final int imageWidth = 10;
        final int imageHeight = 10;
        BufferedImage chartImage = chart.createBufferedImage(imageWidth, imageHeight, null);

        // Assert: Confirm that a valid image was created with the correct dimensions.
        // This implicitly verifies that the rendering process completed successfully.
        assertNotNull("The rendered image should not be null.", chartImage);
        assertEquals("Image width should match the requested width.", imageWidth, chartImage.getWidth());
        assertEquals("Image height should match the requested height.", imageHeight, chartImage.getHeight());
    }
}