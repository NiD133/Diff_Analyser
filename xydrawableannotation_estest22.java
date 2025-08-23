package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.legend.LegendTitle;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class, focusing on the equals() method.
 */
public class XYDrawableAnnotationTest {

    /**
     * Tests that two XYDrawableAnnotation objects are not considered equal if they
     * have different 'drawable' objects, even if all other properties are identical.
     */
    @Test
    public void equals_shouldReturnFalse_whenDrawablesAreDifferent() {
        // Arrange: Create two annotations with identical coordinates and dimensions,
        // but with different Drawable objects.
        final double x = 10.0;
        final double y = 20.0;
        final double width = 100.0;
        final double height = 50.0;
        final double scaleFactor = 1.0;

        Drawable drawable1 = new BlockContainer();
        Drawable drawable2 = new LegendTitle(new XYBarRenderer());

        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(x, y, width, height, scaleFactor, drawable1);
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(x, y, width, height, scaleFactor, drawable2);

        // Act & Assert: The equals method should return false because the drawables differ.
        assertFalse("Annotations with different drawables should not be equal", annotation1.equals(annotation2));
    }
}