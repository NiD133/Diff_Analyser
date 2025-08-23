package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link DeviationRenderer} class.
 */
public class DeviationRendererTest {

    private static final float DELTA = 0.00001f;

    /**
     * Verifies that the constructor correctly initializes a DeviationRenderer
     * with its required default properties.
     */
    @Test
    public void constructorShouldSetDefaultProperties() {
        // Arrange: Create a new renderer instance. The constructor parameters 
        // (for drawing lines and shapes) should not affect the properties being tested here.
        DeviationRenderer renderer = new DeviationRenderer(false, false);

        // Act & Assert
        
        // 1. Verify the default alpha value. This value controls the transparency
        // of the shaded deviation area.
        float expectedAlpha = 0.5f;
        assertEquals("Default alpha should be 0.5f", expectedAlpha, renderer.getAlpha(), DELTA);

        // 2. Verify that drawing the series line as a path is enabled by default.
        // This is a specific requirement for this renderer to function correctly.
        assertTrue("drawSeriesLineAsPath should be true by default", renderer.getDrawSeriesLineAsPath());
    }
}