package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.title.ShortTextTitle;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes the annotation's properties,
     * and that the corresponding getter methods return the correct values.
     */
    @Test
    public void constructorShouldSetPropertiesCorrectly() {
        // Arrange: Define the parameters for the annotation.
        final double expectedX = 0.0;
        final double expectedY = 0.0;
        final double expectedWidth = 0.0;
        final double expectedHeight = -1062.3;
        final Drawable mockDrawable = new ShortTextTitle("Test");

        // Act: Create a new annotation instance.
        // This constructor uses a default drawScaleFactor of 1.0.
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                expectedX, expectedY, expectedWidth, expectedHeight, mockDrawable);

        // Assert: Verify that all properties were initialized as expected.
        assertEquals("The x-coordinate should match the value from the constructor.",
                expectedX, annotation.getX(), DELTA);

        assertEquals("The y-coordinate should match the value from the constructor.",
                expectedY, annotation.getY(), DELTA);

        assertEquals("The display width should match the value from the constructor.",
                expectedWidth, annotation.getDisplayWidth(), DELTA);

        assertEquals("The display height should match the value from the constructor.",
                expectedHeight, annotation.getDisplayHeight(), DELTA);

        assertEquals("The draw scale factor should default to 1.0 for this constructor.",
                1.0, annotation.getDrawScaleFactor(), DELTA);
    }
}