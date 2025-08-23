package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link CollectionBag}.
 */
public class CollectionBagTest {

    /**
     * Tests that an exception from the decorated bag (e.g., a TreeBag) is correctly
     * propagated when adding an element. A TreeBag requires its elements to be
     * Comparable, and this test verifies that attempting to add a non-Comparable
     * element (a LinkedList) results in a ClassCastException.
     */
    @Test
    public void addShouldPropagateClassCastExceptionFromDecoratedBag() {
        // Arrange: Create a CollectionBag decorating a TreeBag.
        // TreeBag requires elements to be Comparable, but LinkedList is not.
        Bag<LinkedList<Integer>> underlyingTreeBag = new TreeBag<>();
        Bag<LinkedList<Integer>> collectionBag = new CollectionBag<>(underlyingTreeBag);
        LinkedList<Integer> nonComparableElement = new LinkedList<>();
        final int elementsToAdd = 5;

        // Act & Assert: The add operation is delegated to the underlying TreeBag,
        // which throws a ClassCastException because it cannot compare LinkedList instances.
        assertThrows(ClassCastException.class, () -> {
            collectionBag.add(nonComparableElement, elementsToAdd);
        });
    }
}