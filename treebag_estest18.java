package org.apache.commons.collections4.bag;

import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link TreeBag} class, focusing on its behavior as a SortedBag.
 */
public class TreeBagTest {

    /**
     * Verifies that the first() method returns the smallest element in the bag
     * according to the elements' natural ordering.
     */
    @Test
    public void first_shouldReturnSmallestElement_whenBagIsNotEmpty() {
        // Arrange: Create a TreeBag and add elements in an order different
        // from their natural sorting order. For Locale.Category enums, the
        // natural order is their declaration order: DISPLAY, then FORMAT.
        final TreeBag<Locale.Category> bag = new TreeBag<>();
        bag.add(Locale.Category.FORMAT);
        bag.add(Locale.Category.DISPLAY);

        final Locale.Category expectedSmallestElement = Locale.Category.DISPLAY;

        // Act: Retrieve the first element from the sorted bag.
        final Locale.Category actualFirstElement = bag.first();

        // Assert: The returned element should be the smallest one (DISPLAY),
        // demonstrating that the bag is correctly sorted.
        assertEquals("The first() method should return the smallest element based on natural ordering.",
                expectedSmallestElement, actualFirstElement);
    }
}