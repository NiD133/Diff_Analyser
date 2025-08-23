package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.title.CompositeTitle;
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
    public void constructor_shouldSetAllProperties() {
        // Arrange: Define the expected properties for the annotation.
        final double expectedX = 979.56628;
        final double expectedY = 2102.65;
        final double expectedDisplayWidth = 1.0;
        final double expectedDisplayHeight = 2389.56649;
        final double expectedDrawScaleFactor = 1.0;
        final Drawable mockDrawable = new CompositeTitle();

        // Act: Create a new annotation instance using the constructor.
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                expectedX,
                expectedY,
                expectedDisplayWidth,
                expectedDisplayHeight,
                expectedDrawScaleFactor,
                mockDrawable
        );

        // Assert: Confirm that each property was initialized with the expected value.
        assertEquals("X-coordinate should match the constructor argument.",
                expectedX, annotation.getX(), DELTA);
        assertEquals("Y-coordinate should match the constructor argument.",
                expectedY, annotation.getY(), DELTA);
        assertEquals("Display width should match the constructor argument.",
                expectedDisplayWidth, annotation.getDisplayWidth(), DELTA);
        assertEquals("Display height should match the constructor argument.",
                expectedDisplayHeight, annotation.getDisplayHeight(), DELTA);
        assertEquals("Draw scale factor should match the constructor argument.",
                expectedDrawScaleFactor, annotation.getDrawScaleFactor(), DELTA);
    }
}