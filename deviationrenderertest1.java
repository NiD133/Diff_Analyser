package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Contains tests for the {@link DeviationRenderer#equals(Object)} method.
 */
public class DeviationRendererEqualsTest {

    /**
     * Verifies that the equals() method returns false when comparing two
     * DeviationRenderer instances that have different alpha values.
     *
     * This test ensures that the 'alpha' property is correctly handled
     * in the equality comparison.
     */
    @Test
    public void testEquals_ReturnsFalse_WhenAlphaIsDifferent() {
        // Arrange: Create two DeviationRenderer instances.
        // The default alpha value is 0.5f.
        DeviationRenderer renderer1 = new DeviationRenderer();
        DeviationRenderer renderer2 = new DeviationRenderer();

        // Pre-condition check: Ensure they are equal before any changes.
        assertEquals("Renderers should be equal upon default instantiation", renderer1, renderer2);

        // Act: Change the alpha property of the second renderer to a different value.
        renderer2.setAlpha(0.8f);

        // Assert: The two renderers should no longer be considered equal.
        assertNotEquals("Renderers should not be equal after changing the alpha value", renderer1, renderer2);
    }
}