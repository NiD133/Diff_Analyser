package org.jfree.chart.annotations;

import org.jfree.chart.title.TextTitle;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that the five-argument constructor correctly initializes the annotation's properties
     * and that the getter methods return the expected values.
     */
    @Test
    public void testFiveArgumentConstructorInitialization() {
        // Arrange: Define the properties for the annotation.
        // The five-argument constructor should use a default drawScaleFactor of 1.0.
        double expectedX = 10.0;
        double expectedY = 20.0;
        double expectedWidth = 100.0;
        double expectedHeight = 50.0;
        TextTitle dummyDrawable = new TextTitle("Test");

        // Act: Create the annotation using the constructor under test.
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                expectedX, expectedY, expectedWidth, expectedHeight, dummyDrawable);

        // Assert: Confirm that all properties were set as expected.
        assertEquals("X-coordinate should match the constructor argument",
                expectedX, annotation.getX(), DELTA);
        assertEquals("Y-coordinate should match the constructor argument",
                expectedY, annotation.getY(), DELTA);
        assertEquals("Display width should match the constructor argument",
                expectedWidth, annotation.getDisplayWidth(), DELTA);
        assertEquals("Display height should match the constructor argument",
                expectedHeight, annotation.getDisplayHeight(), DELTA);
        assertEquals("Draw scale factor should default to 1.0 for this constructor",
                1.0, annotation.getDrawScaleFactor(), DELTA);
    }
}