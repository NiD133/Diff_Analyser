package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Contains tests for the {@link CollectionSortedBag} class.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that removeAll returns false if the underlying bag's comparator
     * is faulty and cannot find its own elements.
     *
     * The CollectionSortedBag#removeAll implementation relies on getCount() to determine
     * how many items to remove. If the underlying TreeBag has a comparator that
     * violates its contract (i.e., compare(x, x) != 0), its internal search
     * will fail. Consequently, getCount() will return 0, no elements will be
     * removed, and removeAll() should correctly report that the collection was
     * not modified by returning false.
     */
    @Test
    public void removeAllShouldReturnFalseWhenUnderlyingBagCannotFindItsOwnElements() {
        // Arrange
        // 1. Create a comparator that violates the contract by never returning 0.
        // This will cause search operations in the TreeBag to fail.
        @SuppressWarnings("unchecked")
        final Comparator<Object> faultyComparator = mock(Comparator.class);
        doReturn(-1).when(faultyComparator).compare(any(), any());

        // 2. Create the underlying bag with the faulty comparator and add an element.
        final SortedBag<Object> underlyingBag = new TreeBag<>(faultyComparator);
        final String element = "test_element";
        underlyingBag.add(element);

        // 3. Decorate the bag. The decorated bag will inherit the faulty search behavior.
        final SortedBag<Object> decoratedBag = CollectionSortedBag.collectionSortedBag(underlyingBag);

        // Act
        // Attempt to remove all elements from the decorated bag that are also in the
        // underlying bag. Because of the faulty comparator, the elements won't be found.
        final boolean wasModified = decoratedBag.removeAll(underlyingBag);

        // Assert
        // The operation should report that the collection was not modified.
        assertFalse("Expected removeAll to return false as elements could not be found", wasModified);
    }
}