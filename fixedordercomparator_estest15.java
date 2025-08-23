package org.apache.commons.collections4.comparators;

import org.junit.Test;
import java.util.Collections;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link FixedOrderComparator} class, focusing on its hashCode contract.
 */
public class FixedOrderComparatorTest {

    /**
     * Tests that calling hashCode() on a FixedOrderComparator initialized with an empty
     * list is consistent. A key contract of the hashCode() method is that it must
     * consistently return the same integer, provided no information used in equals
     * comparisons on the object is modified.
     */
    @Test
    public void hashCodeShouldBeConsistentForEmptyComparator() {
        // Arrange: Create a comparator with no defined order (an empty list).
        final FixedOrderComparator<Object> emptyComparator = new FixedOrderComparator<>(Collections.emptyList());

        // Act: Calculate the hash code multiple times to check for consistency.
        final int firstHashCode = emptyComparator.hashCode();
        final int secondHashCode = emptyComparator.hashCode();

        // Assert: The hash code should be the same for each call.
        // This also implicitly verifies that the method completes without throwing an exception.
        assertEquals("hashCode() must return a consistent value for the same object.",
                     firstHashCode, secondHashCode);
    }
}