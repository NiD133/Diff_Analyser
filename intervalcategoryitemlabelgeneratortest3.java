package org.jfree.chart.labels;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests for the cloning behavior of the {@link IntervalCategoryItemLabelGenerator} class.
 */
class IntervalCategoryItemLabelGeneratorTest {

    @Test
    @DisplayName("A cloned generator is independent but equal to the original")
    void clone_shouldProduceIndependentAndEqualInstance() throws CloneNotSupportedException {
        // Arrange: Create an instance of the generator.
        IntervalCategoryItemLabelGenerator original = new IntervalCategoryItemLabelGenerator();

        // Act: Clone the original instance.
        IntervalCategoryItemLabelGenerator cloned = (IntervalCategoryItemLabelGenerator) original.clone();

        // Assert: Verify the clone's properties against the original.
        // The clone contract requires that the new object is a different instance,
        // of the same class, and is logically equal to the original.
        assertAll("Cloned generator validation",
            () -> assertNotSame(original, cloned, "Clone should be a new object instance."),
            () -> assertSame(original.getClass(), cloned.getClass(), "Clone should be of the exact same class."),
            () -> assertEquals(original, cloned, "Clone should be equal to the original.")
        );
    }
}