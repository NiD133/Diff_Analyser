package org.apache.commons.collections4.bag;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertNull;

/**
 * Contains tests for the {@link TreeBag} class, focusing on its behavior as a SortedBag.
 */
public class TreeBagTest {

    /**
     * Tests that the first() method correctly returns null when a null element
     * is the only item added to a TreeBag.
     *
     * This scenario uses a custom comparator that treats all elements as equal,
     * which allows the underlying TreeMap to accept a null key.
     */
    @Test
    public void testFirstReturnsNullWhenNullIsTheOnlyElement() {
        // Arrange: Create a comparator that considers all elements to be equal.
        // This is a specific condition needed for the underlying TreeMap to handle
        // the null element in this way.
        final Comparator<Object> allElementsEqualComparator = (o1, o2) -> 0;
        final TreeBag<Object> bag = new TreeBag<>(allElementsEqualComparator);

        // Act: Add a single null element to the bag.
        bag.add(null);
        final Object firstElement = bag.first();

        // Assert: The first element in the bag should be null.
        assertNull("The first element should be null", firstElement);
    }
}