package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.title.TextTitle;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link XYDrawableAnnotation} class, focusing on constructor and getters.
 */
public class XYDrawableAnnotationTest {

    private static final double DELTA = 1e-8;

    /**
     * Verifies that the constructor correctly initializes all properties of the annotation,
     * and that the corresponding getter methods return these values.
     */
    @Test
    public void constructorShouldSetAllProperties() {
        // Arrange: Define the expected properties for the annotation.
        final double expectedX = -317.38;
        final double expectedY = -317.38;
        final double expectedWidth = -1.0;
        final double expectedHeight = 0.0;
        final double expectedScaleFactor = -1.0;

        // A simple Drawable implementation is sufficient for this test.
        // Using TextTitle is much simpler than constructing a full JFreeChart.
        Drawable mockDrawable = new TextTitle("Test Drawable");

        // Act: Create an instance of XYDrawableAnnotation using the constructor under test.
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                expectedX, expectedY, expectedWidth, expectedHeight,
                expectedScaleFactor, mockDrawable);

        // Assert: Confirm that each property was set correctly.
        assertEquals("X coordinate should match the constructor argument.",
                expectedX, annotation.getX(), DELTA);
        assertEquals("Y coordinate should match the constructor argument.",
                expectedY, annotation.getY(), DELTA);
        assertEquals("Display width should match the constructor argument.",
                expectedWidth, annotation.getDisplayWidth(), DELTA);
        assertEquals("Display height should match the constructor argument.",
                expectedHeight, annotation.getDisplayHeight(), DELTA);
        assertEquals("Draw scale factor should match the constructor argument.",
                expectedScaleFactor, annotation.getDrawScaleFactor(), DELTA);
    }
}