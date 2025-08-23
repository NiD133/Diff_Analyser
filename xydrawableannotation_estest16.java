package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.title.DateTitle;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link XYDrawableAnnotation} class, focusing on its constructor and getters.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that the getDisplayWidth() method returns the exact value
     * that was provided to the constructor.
     */
    @Test
    public void getDisplayWidth_shouldReturnConstructorValue() {
        // Arrange: Define test data and create the annotation instance.
        // The specific values for other parameters are not relevant to this test.
        double expectedWidth = -1.0;
        Drawable dummyDrawable = new DateTitle();

        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                0.0,           // x
                0.0,           // y
                expectedWidth, // displayWidth
                0.0,           // displayHeight
                -3958.9,       // drawScaleFactor
                dummyDrawable
        );

        // Act: Call the method under test.
        double actualWidth = annotation.getDisplayWidth();

        // Assert: Check if the returned value matches the expected value.
        assertEquals(expectedWidth, actualWidth, 0.01);
    }

    /**
     * Verifies that the constructor correctly initializes all properties of the annotation.
     * This is an alternative to the focused test above, combining the assertions
     * from the original generated test into a single, clear state-verification test.
     */
    @Test
    public void constructor_shouldCorrectlyInitializeAllProperties() {
        // Arrange
        double expectedX = 0.0;
        double expectedY = 0.0;
        double expectedWidth = -1.0;
        double expectedHeight = 0.0;
        double expectedScaleFactor = -3958.9;
        Drawable dummyDrawable = new DateTitle();

        // Act
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(
                expectedX,
                expectedY,
                expectedWidth,
                expectedHeight,
                expectedScaleFactor,
                dummyDrawable
        );

        // Assert
        assertEquals("X-coordinate should match constructor argument", expectedX, annotation.getX(), 0.01);
        assertEquals("Y-coordinate should match constructor argument", expectedY, annotation.getY(), 0.01);
        assertEquals("Display width should match constructor argument", expectedWidth, annotation.getDisplayWidth(), 0.01);
        assertEquals("Display height should match constructor argument", expectedHeight, annotation.getDisplayHeight(), 0.01);
        assertEquals("Draw scale factor should match constructor argument", expectedScaleFactor, annotation.getDrawScaleFactor(), 0.01);
    }
}