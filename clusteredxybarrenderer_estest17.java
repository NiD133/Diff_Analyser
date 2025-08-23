package org.jfree.chart.renderer.xy;

import org.jfree.chart.util.DirectionalGradientPaintTransformer;
import org.junit.Test;

/**
 * Contains tests for the clone() method in the {@link ClusteredXYBarRenderer} class.
 */
public class ClusteredXYBarRenderer_ESTestTest17 {

    /**
     * Verifies that clone() throws a CloneNotSupportedException if the renderer
     * has a GradientPaintTransformer that does not implement Cloneable.
     *
     * The clone() method in the renderer's superclass attempts to deep-clone
     * its properties. This test ensures that it correctly fails when a property,
     * like DirectionalGradientPaintTransformer, is not cloneable.
     */
    @Test(expected = CloneNotSupportedException.class)
    public void cloneWithNonCloneableGradientTransformerThrowsException() throws CloneNotSupportedException {
        // Arrange: Create a renderer and set a GradientPaintTransformer that is not cloneable.
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        DirectionalGradientPaintTransformer nonCloneableTransformer = new DirectionalGradientPaintTransformer();
        renderer.setGradientPaintTransformer(nonCloneableTransformer);

        // Act: Attempt to clone the renderer.
        // This is expected to throw a CloneNotSupportedException because the
        // transformer property cannot be cloned.
        renderer.clone();
    }
}