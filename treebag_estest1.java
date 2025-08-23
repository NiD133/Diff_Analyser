package org.apache.commons.collections4.bag;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Contains unit tests for the {@link TreeBag} class.
 */
public class TreeBagTest {

    /**
     * Tests that the last() method correctly returns null when null is the only element
     * in the bag. A custom comparator that treats all elements as equal is used to
     * ensure the underlying TreeMap can handle the null key.
     */
    @Test
    public void testLast_returnsNull_whenNullIsTheOnlyElementAdded() {
        // Arrange: Create a TreeBag with a comparator that treats all elements as equal.
        // This is necessary for the underlying TreeMap to handle a null key without a
        // NullPointerException when natural ordering is not used.
        Comparator<Object> alwaysEqualComparator = (o1, o2) -> 0;
        SortedBag<Object> bag = new TreeBag<>(alwaysEqualComparator);

        // Act: Add a null element to the bag.
        bag.add(null);
        Object lastElement = bag.last();

        // Assert: Verify that the last element is indeed null and the bag's state is correct.
        assertNull("The last element should be null as it's the only one present.", lastElement);
        assertEquals("The bag size should be 1.", 1, bag.size());
        assertEquals("The count of the null element should be 1.", 1, bag.getCount(null));
    }
}