package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link DeviationRenderer} class.
 */
public class DeviationRendererTest {

    /**
     * Verifies that the constructor correctly initializes the renderer with its
     * default properties. The default alpha should be 0.5f, and drawing the
     * series line as a path should be enabled.
     */
    @Test
    public void constructor_shouldSetDefaultProperties() {
        // Arrange & Act: Create a new renderer instance with lines and shapes visible.
        DeviationRenderer renderer = new DeviationRenderer(true, true);

        // Assert: Check that the default property values are set as expected.
        assertEquals("Default alpha value should be 0.5f", 0.5f, renderer.getAlpha(), 0.0f);
        assertTrue("Drawing series line as a path should be enabled by default", renderer.getDrawSeriesLineAsPath());
    }

    /**
     * Tests the reflexivity property of the equals() method. An object must
     * always be equal to itself.
     */
    @Test
    public void equals_shouldReturnTrueForSameInstance() {
        // Arrange: Create a new renderer instance.
        DeviationRenderer renderer = new DeviationRenderer(true, true);

        // Act & Assert: An instance should always be equal to itself.
        // This confirms the reflexive property of the equals() method.
        assertEquals("A renderer instance should be equal to itself", renderer, renderer);
    }
}