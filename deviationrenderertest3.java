package org.jfree.chart.renderer.xy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests for the cloning functionality of the {@link DeviationRenderer} class.
 */
class DeviationRendererTest {

    /**
     * Verifies that the clone() method creates a new instance that is an
     * independent but equal copy of the original. This test checks the
     * fundamental contract of the clone() method.
     */
    @Test
    void cloningShouldProduceIndependentAndEqualInstance() throws CloneNotSupportedException {
        // Arrange: Create an instance of the renderer.
        DeviationRenderer original = new DeviationRenderer();

        // Act: Clone the original instance.
        DeviationRenderer clone = (DeviationRenderer) original.clone();

        // Assert: Verify the properties of a valid clone.
        // 1. The clone must be a different object in memory.
        assertNotSame(original, clone);

        // 2. The clone must be of the exact same class as the original.
        assertSame(original.getClass(), clone.getClass());

        // 3. The clone must be "equal" to the original, as defined by the equals() method.
        assertEquals(original, clone);
    }
}