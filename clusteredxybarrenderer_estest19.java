package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * This test class focuses on the behavior of the equals() method in ClusteredXYBarRenderer.
 */
public class ClusteredXYBarRenderer_ESTestTest19 extends ClusteredXYBarRenderer_ESTest_scaffolding {

    /**
     * Verifies the reflexive property of the equals() method.
     * An instance of ClusteredXYBarRenderer should always be equal to itself.
     */
    @Test
    public void equals_onSameInstance_shouldReturnTrue() {
        // Arrange
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();

        // Act & Assert: An object must be equal to itself.
        assertTrue("An object should be equal to itself", renderer.equals(renderer));
    }
}