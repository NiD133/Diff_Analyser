package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.title.ShortTextTitle;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes all properties and that
     * the corresponding getter methods return the expected values.
     */
    @Test
    public void constructor_ShouldInitializePropertiesCorrectly() {
        // Arrange: Define the input parameters for the annotation.
        final double expectedX = 10.0;
        final double expectedY = 20.0;
        final double expectedDisplayWidth = 100.0;
        final double expectedDisplayHeight = 50.0;
        final double expectedDrawScaleFactor = 1.5;
        final Drawable dummyDrawable = new ShortTextTitle("Test Drawable");

        // Act: Create an instance of the XYDrawableAnnotation.
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                expectedX,
                expectedY,
                expectedDisplayWidth,
                expectedDisplayHeight,
                expectedDrawScaleFactor,
                dummyDrawable
        );

        // Assert: Verify that each property was set correctly.
        assertEquals("X coordinate should match the constructor argument",
                expectedX, annotation.getX(), DELTA);
        assertEquals("Y coordinate should match the constructor argument",
                expectedY, annotation.getY(), DELTA);
        assertEquals("Display width should match the constructor argument",
                expectedDisplayWidth, annotation.getDisplayWidth(), DELTA);
        assertEquals("Display height should match the constructor argument",
                expectedDisplayHeight, annotation.getDisplayHeight(), DELTA);
        assertEquals("Draw scale factor should match the constructor argument",
                expectedDrawScaleFactor, annotation.getDrawScaleFactor(), DELTA);
    }
}