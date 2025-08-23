package org.jfree.chart.annotations;

import org.jfree.chart.renderer.category.ScatterRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Tests for the equals() method in the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationEqualsTest {

    @Test
    public void equals_whenObjectsHaveDifferentProperties_shouldReturnFalse() {
        // Arrange
        // Create a primary annotation instance. This constructor defaults the drawScaleFactor to 1.0.
        TextTitle firstDrawable = new TextTitle("First Annotation");
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
                0.0, 0.0, 100.0, -50.0, firstDrawable);

        // Create a second annotation with different dimensions, scale factor, and a different drawable object.
        LegendTitle secondDrawable = new LegendTitle(new ScatterRenderer());
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(
                0.0, 0.0, 100.0, 75.0, 1.5, secondDrawable);

        // Act
        // Compare the two different annotation objects.
        boolean areEqual = annotation1.equals(annotation2);

        // Assert
        // The objects should not be considered equal because their display height,
        // draw scale factor, and drawable objects are different.
        assertFalse("Annotations with different properties should not be equal", areEqual);
    }
}