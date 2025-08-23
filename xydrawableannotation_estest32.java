package org.jfree.chart.annotations;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link XYDrawableAnnotation} class, focusing on the draw method's behavior.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that the draw() method correctly adds a ChartEntity to the
     * PlotRenderingInfo when a URL is set for the annotation. This entity is
     * crucial for features like clickable image maps in web applications.
     */
    @Test
    public void draw_whenUrlIsSet_shouldAddEntityToRenderingInfo() {
        // Arrange
        // 1. Define the annotation's properties with clear, simple values.
        final double x = 10.0;
        final double y = 20.0;
        final double width = 100.0;
        final double height = 50.0;
        final String urlText = "https://www.jfree.org/jfreechart/";
        final String toolTipText = "Visit the JFreeChart website";

        // 2. Create the annotation and set its interactive properties.
        TextTitle drawable = new TextTitle("Clickable Annotation");
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(x, y, width, height, drawable);
        annotation.setURL(urlText);
        annotation.setToolTipText(toolTipText);

        // 3. Set up the required objects for the draw() method.
        XYPlot plot = new XYPlot();
        Rectangle2D dataArea = new Rectangle2D.Double(0, 0, 400, 300);
        ValueAxis domainAxis = new NumberAxis("X");
        ValueAxis rangeAxis = new NumberAxis("Y");
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = chartInfo.getPlotInfo();

        // Create a graphics context to draw on.
        BufferedImage image = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        int rendererIndex = 0; // A standard renderer index.

        // Act
        // Call the method under test.
        annotation.draw(g2, plot, dataArea, domainAxis, rangeAxis, rendererIndex, plotInfo);

        // Assert
        // 1. Verify that one entity was added to the rendering information.
        assertEquals("An entity should have been added for the annotation.",
                1, chartInfo.getEntityCollection().getEntityCount());

        // 2. Retrieve the entity and verify its properties.
        ChartEntity entity = chartInfo.getEntityCollection().getEntity(0);
        assertNotNull("The retrieved entity should not be null.", entity);
        assertEquals("The entity's URL should match the one set on the annotation.",
                urlText, entity.getURLText());
        assertEquals("The entity's tool-tip text should match the one set on the annotation.",
                toolTipText, entity.getToolTipText());
    }
}