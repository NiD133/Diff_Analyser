package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.title.TextTitle;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the equals() method in the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationEqualsTest {

    /**
     * This test verifies that two XYDrawableAnnotation instances are not considered
     * equal if they have the same coordinates and drawable but differ in their
     * display dimensions (width, height) and draw scale factor.
     */
    @Test
    public void equals_shouldReturnFalse_whenAnnotationsHaveDifferentDimensions() {
        // Arrange
        // Create a common drawable object to be used by both annotations.
        Drawable drawable = new TextTitle("Test Annotation");

        // Create the first annotation using a constructor that sets a default scale factor (1.0).
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
                10.0, 20.0, 100.0, 50.0, drawable);

        // Create a second annotation with the same coordinates but different width,
        // height, and an explicit, different scale factor.
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(
                10.0, 20.0, 150.0, 75.0, 2.0, drawable);

        // Act & Assert
        // The two annotations should not be equal because their properties differ.
        // We use assertNotEquals for better failure messages and readability.
        assertNotEquals(annotation1, annotation2);
    }

    /**
     * This test is a more direct refactoring of the original test case,
     * demonstrating the same logic with improved clarity.
     */
    @Test
    public void equals_shouldReturnFalse_whenAnnotationsDiffer() {
        // Arrange
        Drawable sharedDrawable = new TextTitle("A");

        // The first annotation uses the constructor with a default scale factor.
        XYDrawableAnnotation baseAnnotation = new XYDrawableAnnotation(
                -2648.3, -2648.3, -1549.16, 2.0, sharedDrawable);

        // The second annotation has different width, height, and scale factor.
        XYDrawableAnnotation differentAnnotation = new XYDrawableAnnotation(
                -2648.3, -2648.3, -726.46, 189.63, 276.29, sharedDrawable);

        // Act
        boolean areEqual = baseAnnotation.equals(differentAnnotation);

        // Assert
        assertFalse("Annotations with different dimensions and scale factors should not be equal", areEqual);
    }
}