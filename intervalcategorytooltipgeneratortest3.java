package org.jfree.chart.labels;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link IntervalCategoryToolTipGenerator} class, focusing on the
 * equals() and hashCode() contract.
 */
class IntervalCategoryToolTipGeneratorTest {

    @Test
    @DisplayName("Two equal objects must have the same hash code")
    void hashCode_shouldBeConsistentForEqualObjects() {
        // Arrange: Create two identical IntervalCategoryToolTipGenerator instances.
        // The default constructor creates objects that should be considered equal.
        var generator1 = new IntervalCategoryToolTipGenerator();
        var generator2 = new IntervalCategoryToolTipGenerator();

        // Assert: Verify that the objects are equal and that their hash codes match,
        // which is required by the Java Object.hashCode() contract.
        assertTrue(generator1.equals(generator2), "Two default generators should be equal.");
        assertEquals(generator1.hashCode(), generator2.hashCode(), "Hash codes of equal objects must be equal.");
    }
}