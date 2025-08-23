package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.block.BlockContainer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that the equals() method returns false when two annotations
     * differ only by their drawScaleFactor.
     */
    @Test
    public void equals_shouldReturnFalse_whenDrawScaleFactorsDiffer() {
        // Arrange
        Drawable commonDrawable = new BlockContainer();
        double x = -2674.76;
        double y = -2674.76;
        double width = 2.0;
        double height = -2674.76;

        // Create an annotation using the constructor that defaults drawScaleFactor to 1.0.
        XYDrawableAnnotation annotationWithDefaultScale = new XYDrawableAnnotation(
                x, y, width, height, commonDrawable);

        // Create a second annotation with an explicit, different drawScaleFactor.
        double customScaleFactor = -888.093;
        XYDrawableAnnotation annotationWithCustomScale = new XYDrawableAnnotation(
                x, y, width, height, customScaleFactor, commonDrawable);

        // Act & Assert
        // The core assertion: the two objects should not be considered equal.
        boolean areEqual = annotationWithDefaultScale.equals(annotationWithCustomScale);
        assertFalse("Annotations with different drawScaleFactors should not be equal.", areEqual);

        // For completeness, check the symmetric property of the equals contract.
        assertFalse("The equals() method should be symmetric.",
                annotationWithCustomScale.equals(annotationWithDefaultScale));
        
        // A more modern and direct way to assert inequality.
        assertNotEquals(annotationWithDefaultScale, annotationWithCustomScale);
    }
}