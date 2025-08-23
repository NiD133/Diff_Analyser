package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link DeviationRenderer} class, focusing on its constructor logic.
 */
public class DeviationRendererTest {

    /**
     * Verifies that the constructor correctly initializes the renderer with its
     * specific default properties.
     */
    @Test
    public void constructor_ShouldInitializeRendererWithDefaultProperties() {
        // Arrange: Create a new DeviationRenderer instance.
        // The constructor is the action being tested.
        DeviationRenderer renderer = new DeviationRenderer(false, false);

        // Assert: Check that the renderer's properties are set as expected.

        // 1. The alpha transparency for the deviation band should default to 0.5f.
        assertEquals("Default alpha should be 0.5F.", 0.5F, renderer.getAlpha(), 0.0f);

        // 2. The 'drawSeriesLineAsPath' property should be true by default.
        // For this specific renderer, this property is always true and cannot be changed.
        assertTrue("drawSeriesLineAsPath should be true by default.", renderer.getDrawSeriesLineAsPath());
    }
}