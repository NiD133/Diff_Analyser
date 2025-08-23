package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.block.BlockContainer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class, focusing on the equals() method.
 */
public class XYDrawableAnnotationTest {

    /**
     * Tests that the equals() method returns false when comparing two annotations
     * that have different properties.
     */
    @Test
    public void equals_shouldReturnFalse_whenAnnotationsHaveDifferentProperties() {
        // Arrange
        // A common drawable object for both annotations.
        Drawable drawable = new BlockContainer();

        // Create a base annotation.
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
                2.0, 1.0, 1.0, 1.0, 1.0, drawable);

        // Create a second annotation with different coordinates, dimensions, and scale factor.
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(
                1.0, 2.0, 1.0, 2.0, 2.0, drawable);

        // Act
        boolean areEqual = annotation1.equals(annotation2);

        // Assert
        assertFalse("Annotations with different properties should not be considered equal.", areEqual);
    }
}