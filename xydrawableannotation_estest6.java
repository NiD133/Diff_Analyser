package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.block.BlockContainer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class.
 */
public class XYDrawableAnnotationTest {

    /**
     * Verifies that two XYDrawableAnnotation instances with different properties
     * are not considered equal.
     */
    @Test
    public void equals_whenAnnotationsHaveDifferentProperties_shouldReturnFalse() {
        // Arrange
        Drawable drawable = new BlockContainer();

        // Create an annotation with specific, non-zero properties.
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(
                -10.0, -20.0, 100.0, 50.0, 2.0, drawable);

        // Create a second, different annotation using the constructor that
        // sets a default scale factor of 1.0.
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(
                0.0, 0.0, 0.0, 0.0, drawable);

        // Act
        boolean areEqual = annotation1.equals(annotation2);

        // Assert
        // The primary goal is to test that the two different annotations are not equal.
        assertFalse("Annotations with different properties should not be equal", areEqual);

        // The original test also verified the state of the second annotation,
        // which is a good check for its constructor. We'll keep that here for completeness.
        assertEquals("Default scale factor should be 1.0", 1.0, annotation2.getDrawScaleFactor(), 0.001);
        assertEquals("X coordinate should be 0.0", 0.0, annotation2.getX(), 0.001);
        assertEquals("Y coordinate should be 0.0", 0.0, annotation2.getY(), 0.001);
        assertEquals("Display width should be 0.0", 0.0, annotation2.getDisplayWidth(), 0.001);
        assertEquals("Display height should be 0.0", 0.0, annotation2.getDisplayHeight(), 0.001);
    }
}