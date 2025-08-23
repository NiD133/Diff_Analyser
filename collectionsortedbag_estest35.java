package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Contains an improved test case for the CollectionSortedBag class.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that calling retainAll() with the collection itself as an argument
     * does not modify the collection and correctly returns false.
     *
     * The contract of Collection.retainAll(c) states it should return true
     * if the collection was modified. When a collection retains all of its own
     * elements, no modification occurs, and the method should return false.
     */
    @Test
    public void retainAllWithSelfShouldNotModifyBagAndReturnFalse() {
        // Arrange
        // A mock comparator allows any object to be added to the underlying TreeBag
        // without requiring it to be Comparable.
        @SuppressWarnings("unchecked") // Safe mock creation with generics
        final Comparator<Object> mockComparator = mock(Comparator.class);
        // Stub the compare method to always return a consistent non-zero value.
        doReturn(-1).when(mockComparator).compare(any(), any());

        // The CollectionSortedBag decorates an underlying SortedBag (a TreeBag in this case).
        final SortedBag<Object> underlyingBag = new TreeBag<>(mockComparator);
        final CollectionSortedBag<Object> bag = new CollectionSortedBag<>(underlyingBag);

        final String testElement = "an element";
        bag.add(testElement);

        // Pre-condition check to ensure the bag is set up correctly.
        assertEquals("Bag should contain one element before the test", 1, bag.size());

        // Act
        // Perform the retainAll operation with the bag itself as the argument.
        final boolean wasModified = bag.retainAll(bag);

        // Assert
        // The operation should not modify the bag, so it must return false.
        assertFalse("retainAll(self) should return false as no elements were removed", wasModified);

        // The bag's state should remain unchanged.
        assertEquals("Bag size should remain 1 after retainAll(self)", 1, bag.size());
        assertEquals("The element's count should still be 1", 1, bag.getCount(testElement));
    }
}