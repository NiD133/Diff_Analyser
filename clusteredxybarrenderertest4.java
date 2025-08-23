package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Tests for the {@link ClusteredXYBarRenderer} class, focusing on its
 * compliance with basic API contracts.
 */
class ClusteredXYBarRendererTest {

    /**
     * Verifies that the renderer implements the {@link PublicCloneable} interface,
     * which is essential for the chart's cloning mechanism.
     */
    @Test
    void shouldImplementPublicCloneable() {
        // Create an instance of the renderer to be tested.
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        // Assert that the renderer is an instance of PublicCloneable.
        // This ensures that it correctly participates in the public cloning API.
        assertInstanceOf(PublicCloneable.class, renderer,
                "ClusteredXYBarRenderer must implement PublicCloneable to be cloneable.");
    }
}