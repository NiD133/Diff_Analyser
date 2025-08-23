package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Unit tests for the {@link DeviationRenderer} class.
 */
class DeviationRendererTest {

    /**
     * Verifies that the renderer implements the PublicCloneable interface,
     * which is a required contract for objects that support cloning.
     */
    @Test
    @DisplayName("DeviationRenderer should be publicly cloneable")
    void rendererShouldImplementPublicCloneable() {
        // Arrange
        DeviationRenderer renderer = new DeviationRenderer();

        // Assert
        assertInstanceOf(PublicCloneable.class, renderer,
                "The renderer must implement PublicCloneable to support cloning.");
    }
}