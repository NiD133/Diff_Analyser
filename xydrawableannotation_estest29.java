package org.jfree.chart.annotations;

import org.jfree.chart.title.TextTitle;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class, focusing on the equals() method.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that the equals() method returns false when comparing an
     * XYDrawableAnnotation instance with an object of a different type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange
        TextTitle dummyDrawable = new TextTitle("Test Drawable");
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(10.0, 20.0, 100.0, 50.0, dummyDrawable);
        Object otherObject = "A completely different object type";

        // Act
        boolean isEqual = annotation.equals(otherObject);

        // Assert
        assertFalse("An annotation should not be equal to an object of a different type.", isEqual);
    }
}