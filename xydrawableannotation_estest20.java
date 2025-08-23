package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the
     * 'drawable' argument is null. A drawable object is a mandatory component
     * for this annotation.
     */
    @Test
    public void constructor_whenDrawableIsNull_shouldThrowIllegalArgumentException() {
        // Arrange: Define valid coordinates and dimensions, but a null drawable.
        double x = 10.0;
        double y = 20.0;
        double width = 100.0;
        double height = 50.0;
        Drawable nullDrawable = null;

        // Act & Assert: Check that creating the annotation with a null drawable
        // throws the expected exception.
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new XYDrawableAnnotation(x, y, width, height, nullDrawable)
        );

        // Assert: Further verify the exception message to ensure it's the correct error.
        assertEquals("Null 'drawable' argument.", exception.getMessage());
    }
}