package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.title.ShortTextTitle;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that the constructor with five arguments correctly initializes all
     * properties and that the draw scale factor defaults to 1.0.
     */
    @Test
    public void constructor_withFiveArguments_initializesPropertiesCorrectly() {
        // Arrange: Define the parameters for the annotation.
        double x = 0.0;
        double y = 0.0;
        double displayWidth = 0.0;
        double displayHeight = -1062.3;
        Drawable drawable = new ShortTextTitle("");

        // Act: Create the annotation instance using the constructor under test.
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(x, y, displayWidth, displayHeight, drawable);

        // Assert: Verify that all properties were set as expected.
        assertEquals("The x-coordinate should be correctly initialized.", x, annotation.getX(), 0.01);
        assertEquals("The y-coordinate should be correctly initialized.", y, annotation.getY(), 0.01);
        assertEquals("The display width should be correctly initialized.", displayWidth, annotation.getDisplayWidth(), 0.01);
        assertEquals("The display height should be correctly initialized.", displayHeight, annotation.getDisplayHeight(), 0.01);
        assertEquals("The draw scale factor should default to 1.0.", 1.0, annotation.getDrawScaleFactor(), 0.01);
    }
}