package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.title.ShortTextTitle;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    private static final double DELTA = 0.01;

    /**
     * Verifies that the constructor correctly initializes the annotation's properties
     * and that the corresponding getter methods return the expected values.
     */
    @Test
    public void constructorShouldSetPropertiesCorrectly() {
        // Arrange
        final double expectedX = 0.0;
        final double expectedY = 0.0;
        final double expectedWidth = 0.0;
        final double expectedHeight = -1062.3;
        final double expectedScaleFactor = 1.0; // This constructor sets a default scale factor of 1.0
        final Drawable drawable = new ShortTextTitle("Test Drawable");

        // Act
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                expectedX, expectedY, expectedWidth, expectedHeight, drawable);

        // Assert
        assertEquals("X coordinate should match the constructor argument.",
                expectedX, annotation.getX(), DELTA);
        assertEquals("Y coordinate should match the constructor argument.",
                expectedY, annotation.getY(), DELTA);
        assertEquals("Display width should match the constructor argument.",
                expectedWidth, annotation.getDisplayWidth(), DELTA);
        assertEquals("Display height should match the constructor argument.",
                expectedHeight, annotation.getDisplayHeight(), DELTA);
        assertEquals("Draw scale factor should default to 1.0.",
                expectedScaleFactor, annotation.getDrawScaleFactor(), DELTA);
    }
}