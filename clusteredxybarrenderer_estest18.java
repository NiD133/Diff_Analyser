package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Contains tests for the {@link ClusteredXYBarRenderer#equals(Object)} method.
 * This class focuses on verifying the equality contract of the renderer.
 */
public class ClusteredXYBarRenderer_ESTestTest18 extends ClusteredXYBarRenderer_ESTest_scaffolding {

    /**
     * Verifies that the equals() method returns false when comparing two
     * ClusteredXYBarRenderer instances that were constructed with different properties.
     */
    @Test
    public void equals_shouldReturnFalse_forRenderersWithDifferentProperties() {
        // Arrange: Create a renderer with default properties.
        // The default constructor sets margin to 0.0 and centerBarAtStartValue to false.
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();

        // Arrange: Create a second renderer with non-default properties.
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer(10.0, true);

        // Act & Assert: The two instances should not be considered equal.
        // Using assertNotEquals is more semantic for this comparison.
        assertNotEquals(renderer1, renderer2);

        // For completeness, we can also check the direct boolean result.
        boolean areEqual = renderer1.equals(renderer2);
        assertFalse("Renderers with different margin and centerBarAtStartValue properties should not be equal.", areEqual);
    }
}