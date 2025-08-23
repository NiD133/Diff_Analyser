package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains a refactored test for the {@link CollectionSortedBag}.
 * The original test was auto-generated and has been rewritten for improved clarity and maintainability.
 */
public class CollectionSortedBag_ESTestTest37 { // Retaining original class name for context

    /**
     * Tests that calling removeAll with a collection containing all of the bag's
     * elements successfully removes them and results in an empty bag.
     */
    @Test
    public void testRemoveAll_whenRemovingAllElements_shouldLeaveBagEmpty() {
        // Arrange
        // 1. Create the underlying sorted bag (TreeBag) and populate it.
        //    Using simple, comparable Strings avoids the need for a complex mock comparator.
        final SortedBag<String> underlyingBag = new TreeBag<>();
        underlyingBag.add("A");
        underlyingBag.add("B");

        // 2. Decorate the underlying bag with CollectionSortedBag, the class under test.
        final SortedBag<String> decoratedBag = new CollectionSortedBag<>(underlyingBag);

        // 3. Create a collection containing all the elements to be removed.
        final Collection<String> elementsToRemove = new HashBag<>(Arrays.asList("A", "B"));

        // Act
        // Call the method under test.
        final boolean wasModified = decoratedBag.removeAll(elementsToRemove);

        // Assert
        // 1. The operation should return true, indicating the collection was changed.
        assertTrue("removeAll should return true when elements are removed.", wasModified);

        // 2. The decorated bag should be empty after the operation.
        assertTrue("The bag should be empty after removing all its elements.", decoratedBag.isEmpty());
        assertEquals("The bag size should be 0 after removing all elements.", 0, decoratedBag.size());
    }
}