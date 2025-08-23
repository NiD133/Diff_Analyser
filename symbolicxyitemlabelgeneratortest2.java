package org.jfree.chart.labels;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link SymbolicXYItemLabelGenerator} class, focusing on its
 * general object contracts like equals() and hashCode().
 */
class SymbolicXYItemLabelGeneratorTest {

    @Test
    @DisplayName("hashCode() is consistent with the equals() method")
    void hashCode_shouldBeConsistentWithEquals() {
        // Arrange: Create two separate instances that should be equal by default.
        // The SymbolicXYItemLabelGenerator has no state, so any two new instances are equivalent.
        SymbolicXYItemLabelGenerator generator1 = new SymbolicXYItemLabelGenerator();
        SymbolicXYItemLabelGenerator generator2 = new SymbolicXYItemLabelGenerator();

        // Assert: Verify the fundamental contract between equals() and hashCode().
        // First, confirm that the two objects are indeed equal.
        assertEquals(generator1, generator2, "Two default instances should be equal.");

        // Then, confirm that their hash codes are also equal, as required by the contract.
        assertEquals(generator1.hashCode(), generator2.hashCode(), "Equal objects must have equal hash codes.");
    }
}