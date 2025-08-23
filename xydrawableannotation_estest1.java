package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.title.TextTitle;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that the 5-argument constructor correctly initializes the annotation's
     * properties and sets the draw scale factor to its default value of 1.0.
     */
    @Test
    public void constructorWith5ArgsShouldSetPropertiesAndDefaultScaleFactor() {
        // Arrange
        double x = -2648.3;
        double y = -2648.3;
        double displayWidth = -1549.1585;
        double displayHeight = 2.0;
        Drawable drawable = new TextTitle("Test Drawable");
        double expectedDefaultScaleFactor = 1.0;

        // Act
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(x, y, displayWidth, displayHeight, drawable);

        // Assert
        assertEquals("X coordinate should match the constructor argument.", x, annotation.getX(), 0.0);
        assertEquals("Y coordinate should match the constructor argument.", y, annotation.getY(), 0.0);
        assertEquals("Display width should match the constructor argument.", displayWidth, annotation.getDisplayWidth(), 0.0);
        assertEquals("Display height should match the constructor argument.", displayHeight, annotation.getDisplayHeight(), 0.0);
        assertEquals("Draw scale factor should default to 1.0.", expectedDefaultScaleFactor, annotation.getDrawScaleFactor(), 0.0);
    }
}