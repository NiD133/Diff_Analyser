package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the equals() method in the {@link DeviationRenderer} class.
 */
public class DeviationRenderer_ESTestTest1 extends DeviationRenderer_ESTest_scaffolding {

    /**
     * Tests that two DeviationRenderer instances are not considered equal if their
     * 'alpha' property is different.
     */
    @Test
    public void equals_shouldReturnFalse_whenAlphaIsDifferent() throws CloneNotSupportedException {
        // Arrange
        DeviationRenderer renderer1 = new DeviationRenderer();
        DeviationRenderer renderer2 = (DeviationRenderer) renderer1.clone();

        // Pre-condition check: ensure the cloned renderer is equal to the original
        assertEquals(renderer1, renderer2);

        // Act: Modify the alpha property of the second renderer
        renderer2.setAlpha(0.8f);

        // Assert: The two renderers should no longer be equal
        assertNotEquals(renderer1, renderer2);
    }
}