package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains tests for the DeviationRenderer class.
 * This particular test focuses on the behavior of the equals() method.
 */
public class DeviationRenderer_ESTestTest8 extends DeviationRenderer_ESTest_scaffolding {

    /**
     * Tests that the equals() method returns false when comparing two
     * DeviationRenderer instances with different alpha values.
     */
    @Test
    public void equals_shouldReturnFalse_whenAlphaIsDifferent() {
        // Arrange: Create a renderer and a clone of it.
        DeviationRenderer renderer1 = new DeviationRenderer();
        DeviationRenderer renderer2 = (DeviationRenderer) renderer1.clone();

        // Pre-condition: Verify that the fresh clone is equal to the original.
        assertTrue("A fresh clone should be equal to the original object.",
                renderer1.equals(renderer2));

        // Act: Modify the alpha property of the second renderer to make it different.
        // The default alpha is 0.5f.
        renderer2.setAlpha(0.8f);

        // Assert: The two renderers should no longer be considered equal.
        assertFalse("Renderers with different alpha values should not be equal.",
                renderer1.equals(renderer2));
    }
}