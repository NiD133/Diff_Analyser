package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertEquals;

import org.apache.commons.collections4.comparators.FixedOrderComparator.UnknownObjectBehavior;
import org.junit.Test;

/**
 * This class contains tests for the {@link FixedOrderComparator#equals(Object)} method.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class FixedOrderComparator_ESTestTest20 { // Original class name is kept for context

    /**
     * Tests that two newly created, empty FixedOrderComparators are equal.
     * <p>
     * This test verifies that the {@code equals()} method correctly compares two
     * comparators based on their internal state (order map, unknown object behavior,
     * lock status) rather than their generic type, which is erased at runtime.
     */
    @Test
    public void twoEmptyComparatorsShouldBeEqual() {
        // Arrange: Create two empty comparators with different generic types.
        // Their internal state is identical by default.
        final FixedOrderComparator<Object> comparatorA = new FixedOrderComparator<>();
        final FixedOrderComparator<UnknownObjectBehavior> comparatorB = new FixedOrderComparator<>();

        // Act & Assert:
        // 1. The two comparators should be equal to each other.
        assertEquals("Two new, empty comparators should be equal", comparatorA, comparatorB);

        // 2. The equals contract requires symmetry.
        assertEquals("Equality should be symmetric", comparatorB, comparatorA);

        // 3. The equals/hashCode contract requires equal objects to have equal hash codes.
        assertEquals("Hash codes of equal objects must be equal", comparatorA.hashCode(), comparatorB.hashCode());
    }
}