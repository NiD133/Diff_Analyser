package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * Contains tests for the {@link CollectionSortedBag} class, focusing on its
 * exception-throwing behavior.
 */
// The original class name 'CollectionSortedBag_ESTestTest30' is kept for context,
// but in a real-world scenario, it would be renamed to 'CollectionSortedBagTest'.
public class CollectionSortedBag_ESTestTest30 {

    /**
     * Tests that attempting to add a null element to a CollectionSortedBag
     * throws a NullPointerException.
     * <p>
     * This behavior is expected because the decorated bag (a TreeBag in this case)
     * does not permit null elements, and the decorator should preserve this contract.
     */
    @Test(expected = NullPointerException.class)
    public void add_whenNullElementIsProvided_throwsNullPointerException() {
        // Arrange: Create a CollectionSortedBag decorating a TreeBag, which disallows nulls.
        final SortedBag<Object> decoratedBag = new TreeBag<>();
        final SortedBag<Object> collectionSortedBag = new CollectionSortedBag<>(decoratedBag);

        // Act: Attempt to add a null element, which should trigger the exception.
        collectionSortedBag.add(null);

        // Assert: The @Test(expected) annotation verifies that a NullPointerException was thrown.
    }
}