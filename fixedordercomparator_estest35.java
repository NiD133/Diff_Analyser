package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link FixedOrderComparator}.
 */
public class FixedOrderComparatorTest {

    /**
     * Tests that a newly created FixedOrderComparator, using the default constructor,
     * is configured to throw an exception when an unknown object is encountered.
     */
    @Test
    public void shouldDefaultToExceptionForUnknownObjectBehavior() {
        // Arrange: Create a comparator with the default constructor.
        final FixedOrderComparator<Object> comparator = new FixedOrderComparator<>();

        // Act: Retrieve the configured behavior for unknown objects.
        final FixedOrderComparator.UnknownObjectBehavior behavior = comparator.getUnknownObjectBehavior();

        // Assert: Verify that the default behavior is EXCEPTION.
        assertEquals(FixedOrderComparator.UnknownObjectBehavior.EXCEPTION, behavior);
    }
}