package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.title.ShortTextTitle;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that the 5-argument constructor correctly initializes all properties,
     * including setting the draw scale factor to its default value of 1.0.
     */
    @Test
    public void fiveArgumentConstructor_shouldInitializePropertiesCorrectly() {
        // Arrange
        Drawable drawable = new ShortTextTitle("Test Drawable");
        double x = 0.0;
        double y = 0.0;
        double displayWidth = 0.0;
        double displayHeight = -1062.3; // Using a negative value as in the original test
        double expectedDefaultDrawScaleFactor = 1.0;

        // Act
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(x, y, displayWidth, displayHeight, drawable);

        // Assert
        assertEquals("X-coordinate should be set correctly.", x, annotation.getX(), 0.01);
        assertEquals("Y-coordinate should be set correctly.", y, annotation.getY(), 0.01);
        assertEquals("Display width should be set correctly.", displayWidth, annotation.getDisplayWidth(), 0.01);
        assertEquals("Display height should be set correctly.", displayHeight, annotation.getDisplayHeight(), 0.01);
        assertEquals("Draw scale factor should default to 1.0.", expectedDefaultDrawScaleFactor, annotation.getDrawScaleFactor(), 0.01);
    }
}