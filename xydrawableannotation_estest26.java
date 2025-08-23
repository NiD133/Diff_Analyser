package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.block.BlockContainer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class, focusing on object equality.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that the equals() method returns false when comparing two annotations
     * that have different properties.
     */
    @Test
    public void equals_shouldReturnFalse_forAnnotationsWithDifferentProperties() {
        // Arrange
        Drawable drawable = new BlockContainer();

        // Create a primary annotation with a specific scale factor.
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
                10.0, // x
                20.0, // y
                100.0, // displayWidth
                50.0, // displayHeight
                2.0,  // drawScaleFactor
                drawable
        );

        // Create a second annotation with a different y-coordinate, height, and scale factor.
        // This uses a constructor that sets the scale factor to a default of 1.0.
        XYDrawableAnnotation differentAnnotation = new XYDrawableAnnotation(
                10.0,  // x (same)
                -20.0, // y (different)
                100.0, // displayWidth (same)
                -50.0, // displayHeight (different)
                drawable
        );

        // Act
        boolean areEqual = annotation1.equals(differentAnnotation);

        // Assert
        assertFalse("Annotations with different properties should not be considered equal", areEqual);
    }
}