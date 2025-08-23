package org.jfree.chart.labels;

import org.jfree.chart.internal.CloneUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link SymbolicXYItemLabelGenerator} class.
 */
class SymbolicXYItemLabelGeneratorTest {

    /**
     * Verifies that cloning a SymbolicXYItemLabelGenerator instance results in an
     * independent object that is logically equal to the original. This test
     * confirms adherence to the general contract of Object.clone().
     */
    @Test
    @DisplayName("Cloning creates an independent and equal copy")
    void clone_shouldBeIndependentAndEqual() throws CloneNotSupportedException {
        // Arrange: Create an instance of the generator.
        SymbolicXYItemLabelGenerator original = new SymbolicXYItemLabelGenerator();

        // Act: Clone the original instance using the library's utility.
        SymbolicXYItemLabelGenerator clone = CloneUtils.clone(original);

        // Assert: The clone must be a new instance but logically equal to the original.
        assertAll("A valid clone should be a new, but equal, object of the same class",
                () -> assertNotSame(original, clone, "A clone must be a different object in memory."),
                () -> assertEquals(original, clone, "A clone must be logically equal to the original."),
                () -> assertSame(original.getClass(), clone.getClass(), "A clone must be of the exact same class as the original.")
        );
    }
}