package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.title.DateTitle;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    /**
     * A small tolerance for floating-point comparisons.
     */
    private static final double DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes all properties of the annotation.
     */
    @Test
    public void constructor_shouldSetAllPropertiesCorrectly() {
        // Arrange: Define the properties for the annotation.
        final double expectedX = 1.0;
        final double expectedY = 1.0;
        final double expectedDisplayWidth = 1.0;
        final double expectedDisplayHeight = 1.0;
        final double expectedDrawScaleFactor = -3471.8789344;
        // Use a concrete implementation of Drawable for the test.
        final Drawable drawable = new DateTitle();

        // Act: Create a new annotation instance using the constructor under test.
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                expectedX,
                expectedY,
                expectedDisplayWidth,
                expectedDisplayHeight,
                expectedDrawScaleFactor,
                drawable
        );

        // Assert: Check that each property was set to the value provided to the constructor.
        assertEquals("The x-coordinate should match the constructor argument.",
                expectedX, annotation.getX(), DELTA);
        assertEquals("The y-coordinate should match the constructor argument.",
                expectedY, annotation.getY(), DELTA);
        assertEquals("The display width should match the constructor argument.",
                expectedDisplayWidth, annotation.getDisplayWidth(), DELTA);
        assertEquals("The display height should match the constructor argument.",
                expectedDisplayHeight, annotation.getDisplayHeight(), DELTA);
        assertEquals("The draw scale factor should match the constructor argument.",
                expectedDrawScaleFactor, annotation.getDrawScaleFactor(), DELTA);
    }
}