package org.jfree.chart.renderer.xy;

import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * Tests for the cloning behavior of the {@link ClusteredXYBarRenderer} class.
 */
class ClusteredXYBarRendererTest {

    /**
     * Verifies that cloning a {@link ClusteredXYBarRenderer} instance results
     * in a new, independent object that is logically equal to the original.
     */
    @Test
    @DisplayName("A cloned renderer should be an independent and equal copy")
    void testCloning() {
        // Arrange: Create a renderer with non-default properties to ensure
        // the clone and equals methods are tested thoroughly.
        // The ClusteredXYBarRenderer has a specific 'centerBarAtStartValue' property.
        ClusteredXYBarRenderer original = new ClusteredXYBarRenderer(0.15, true);

        // Act: Clone the original renderer using the project's utility.
        ClusteredXYBarRenderer clone = CloneUtils.clone(original);

        // Assert: Verify the properties of the clone.
        
        // 1. The clone must be a different object in memory.
        assertNotSame(original, clone, "The clone should be a new object instance.");

        // 2. The clone must be logically equal to the original. This implies
        // that all its properties were copied correctly and relies on a
        // well-implemented equals() method.
        assertEquals(original, clone, "The clone should be logically equal to the original.");
    }
}