package org.apache.commons.collections4.bag;

import org.junit.Test;
import java.util.Comparator;
import static org.junit.Assert.assertNull;

/**
 * This test suite contains tests for the {@link TreeBag} class.
 * The original test class name 'TreeBag_ESTestTest6' is kept for context,
 * but would ideally be renamed to something more descriptive, like 'TreeBagTest'.
 */
public class TreeBag_ESTestTest6 { // Note: Scaffolding dependency removed for clarity.

    /**
     * Tests that the comparator() method returns null when the TreeBag is
     * constructed without a specific comparator, indicating it uses
     * the natural ordering of its elements.
     */
    @Test
    public void comparatorShouldReturnNullWhenUsingNaturalOrdering() {
        // Arrange: Create a TreeBag using the default constructor. This constructor
        // sets up the bag to use the natural ordering of its elements.
        final TreeBag<String> bag = new TreeBag<>();

        // Act: Retrieve the comparator from the bag.
        final Comparator<? super String> comparator = bag.comparator();

        // Assert: The comparator should be null, as per the contract for
        // collections that are sorted by the elements' natural ordering.
        assertNull("The comparator should be null when natural ordering is used.", comparator);
    }
}