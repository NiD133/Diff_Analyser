package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link CollectionSortedBag}.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that calling addAll() with the bag itself as an argument returns false,
     * as the collection is not modified. This adheres to the java.util.Collection contract.
     */
    @Test
    public void addAll_withSelfAsArgument_shouldReturnFalse() {
        // Arrange
        // A mock comparator is required by the TreeBag constructor, but it will not be
        // invoked because the bag is empty and no elements are compared.
        @SuppressWarnings("unchecked")
        final Comparator<Object> mockComparator = mock(Comparator.class);
        
        final SortedBag<Object> decoratedBag = new TreeBag<>(mockComparator);
        final SortedBag<Object> bag = new CollectionSortedBag<>(decoratedBag);

        // Act
        // Attempt to add all elements from the bag into itself.
        final boolean wasModified = bag.addAll(bag);

        // Assert
        // The collection should not change, so the method must return false.
        assertFalse("addAll(self) should return false, indicating the collection was not modified.", wasModified);
    }
}