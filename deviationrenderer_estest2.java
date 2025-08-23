package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link DeviationRenderer} class.
 */
public class DeviationRendererTest {

    private static final float DELTA = 0.00001f;

    /**
     * Verifies that the alpha property is initialized with a correct default value
     * and can be updated successfully via its setter.
     */
    @Test
    public void shouldSetAndGetAlphaValueCorrectly() {
        // Arrange: Create a renderer instance. The constructor arguments for drawing
        // lines and shapes are not relevant to this specific test.
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        
        // Assert: Check that the default alpha value is 0.5f as expected.
        assertEquals("Default alpha value should be 0.5f", 0.5f, renderer.getAlpha(), DELTA);

        // Act: Set a new alpha value.
        renderer.setAlpha(1.0f);

        // Assert: Verify that the getAlpha() method returns the newly set value.
        assertEquals("Alpha value should be updated to 1.0f", 1.0f, renderer.getAlpha(), DELTA);
    }
}