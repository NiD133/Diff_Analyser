package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link DeviationRenderer} class, focusing on the alpha property.
 */
public class DeviationRendererTest {

    /**
     * Verifies that the setAlpha() method correctly updates the renderer's alpha value,
     * and that the initial default value is set as expected.
     */
    @Test
    public void testSetAlphaUpdatesAlphaValue() {
        // Arrange: Create a DeviationRenderer instance.
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        float expectedInitialAlpha = 0.5F;
        float expectedNewAlpha = 1.0F;

        // Assert: Check the initial default alpha value.
        assertEquals("The default alpha value should be 0.5F.", expectedInitialAlpha, renderer.getAlpha(), 0.0f);

        // Act: Set a new alpha value.
        renderer.setAlpha(expectedNewAlpha);

        // Assert: Verify that the alpha value has been updated.
        assertEquals("The alpha value should be updated after calling setAlpha.", expectedNewAlpha, renderer.getAlpha(), 0.0f);
    }
}