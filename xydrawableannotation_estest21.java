package org.jfree.chart.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jfree.chart.Drawable;
import org.junit.Test;

/**
 * Tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the
     * 'drawable' argument is null. A null drawable is not permitted as it would
     * result in a NullPointerException during rendering.
     */
    @Test
    public void constructorShouldThrowIllegalArgumentExceptionForNullDrawable() {
        // Arrange: Define valid parameters for the constructor, with a null drawable.
        double x = 10.0;
        double y = 20.0;
        double displayWidth = 100.0;
        double displayHeight = 50.0;
        double drawScaleFactor = 1.0;
        Drawable nullDrawable = null;

        // Act & Assert
        try {
            new XYDrawableAnnotation(x, y, displayWidth, displayHeight, drawScaleFactor, nullDrawable);
            fail("Expected an IllegalArgumentException for a null 'drawable' argument, but none was thrown.");
        } catch (IllegalArgumentException e) {
            // Assert that the exception message is correct, confirming the reason for the failure.
            assertEquals("Null 'drawable' argument.", e.getMessage());
        }
    }
}