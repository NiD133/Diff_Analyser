package org.jfree.chart.renderer.xy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link ClusteredXYBarRenderer} class, focusing on its general
 * object contract methods like equals() and hashCode().
 */
public class ClusteredXYBarRendererTest {

    /**
     * Verifies that two equal ClusteredXYBarRenderer instances produce the same
     * hash code, which is a requirement of the Object.hashCode() contract.
     */
    @Test
    public void hashCode_forEqualObjects_shouldBeEqual() {
        // Arrange: Create two renderers with default settings. According to the
        // equals() contract, they should be identical.
        ClusteredXYBarRenderer renderer1 = new ClusteredXYBarRenderer();
        ClusteredXYBarRenderer renderer2 = new ClusteredXYBarRenderer();

        // Assert: First, confirm the prerequisite that the objects are equal.
        assertEquals(renderer1, renderer2, "Two default renderers should be equal.");

        // Assert: The main check, that their hash codes are also equal.
        assertEquals(renderer1.hashCode(), renderer2.hashCode(), "Equal objects must have equal hash codes.");
    }
}