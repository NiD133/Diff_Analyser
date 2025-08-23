package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Contains tests for the {@link CollectionSortedBag} decorator.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that {@code remove(Object)} succeeds if the provided object is considered
     * equal to an existing element by the bag's comparator, even if they are
     * different object instances.
     */
    @Test
    public void testRemoveSucceedsWhenComparatorFindsAMatch() {
        // Arrange
        // 1. Create a comparator that considers any two objects to be equal.
        @SuppressWarnings("unchecked")
        final Comparator<Object> allEqualComparator = mock(Comparator.class);
        doReturn(0).when(allEqualComparator).compare(any(), any());

        // 2. Create a backing TreeBag with this comparator and add an element.
        final SortedBag<Object> backingBag = new TreeBag<>(allEqualComparator);
        backingBag.add("first element");

        // 3. Decorate the bag with CollectionSortedBag, the class under test.
        final SortedBag<Object> collectionSortedBag = new CollectionSortedBag<>(backingBag);

        // 4. Define a different object that the comparator will treat as equal.
        final Object objectToRemove = "second element";

        // Act
        // Attempt to remove the second object. Because the comparator returns 0 (equal),
        // the bag should find and remove the first element.
        final boolean wasRemoved = collectionSortedBag.remove(objectToRemove);

        // Assert
        assertTrue("The remove operation should return true as the comparator " +
                   "considers the objects to be equal.", wasRemoved);
    }
}