package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains tests for the DeviationRenderer.
 * This specific test focuses on the initial state of a newly created renderer.
 */
public class DeviationRenderer_ESTestTest19 {

    /**
     * Verifies that the DeviationRenderer constructor correctly initializes
     * the renderer with its default properties.
     */
    @Test
    public void newRendererShouldInitializeWithDefaultSettings() {
        // Arrange & Act: Create a new DeviationRenderer instance.
        // The constructor is the "action" under test.
        DeviationRenderer renderer = new DeviationRenderer(true, true);

        // Assert: Check that the renderer's properties are set to their expected default values.

        // The default alpha transparency for the deviation shading should be 0.5f.
        assertEquals("Default alpha value should be 0.5f", 0.5F, renderer.getAlpha(), 0.001F);

        // The DeviationRenderer is designed to draw series lines as a path to create the
        // shaded area, so this property should always be true.
        assertTrue("drawSeriesLineAsPath should be true by default", renderer.getDrawSeriesLineAsPath());
    }
}