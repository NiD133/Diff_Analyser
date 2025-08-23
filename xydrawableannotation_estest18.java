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
     * Verifies that the constructor correctly initializes the annotation's properties
     * and that the corresponding getter methods return these values.
     */
    @Test
    public void gettersShouldReturnValuesSetByConstructor() {
        // Arrange: Define the expected properties for the annotation.
        final double expectedX = 1.0;
        final double expectedY = -1.0;
        final double expectedWidth = -2061.18;
        final double expectedHeight = -3471.87;
        final Drawable dummyDrawable = new DateTitle();

        // Act: Create an instance of the annotation using the constructor under test.
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                expectedX, expectedY, expectedWidth, expectedHeight, dummyDrawable);

        // Assert: Verify that each property was set correctly.
        assertEquals("X-coordinate should match the constructor argument.",
                expectedX, annotation.getX(), DELTA);

        assertEquals("Y-coordinate should match the constructor argument.",
                expectedY, annotation.getY(), DELTA);

        assertEquals("Display width should match the constructor argument.",
                expectedWidth, annotation.getDisplayWidth(), DELTA);

        assertEquals("Display height should match the constructor argument.",
                expectedHeight, annotation.getDisplayHeight(), DELTA);

        // This constructor sets a default draw scale factor of 1.0.
        assertEquals("Default draw scale factor should be 1.0.",
                1.0, annotation.getDrawScaleFactor(), DELTA);
    }
}