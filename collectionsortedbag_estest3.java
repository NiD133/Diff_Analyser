package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CollectionSortedBag#containsAll(Collection)} method.
 */
public class CollectionSortedBagContainsAllTest {

    @Test
    public void testContainsAll_onEmptyBagWithNonEmptyCollection_shouldReturnFalse() {
        // Arrange: Create an empty CollectionSortedBag and a non-empty collection.
        final SortedBag<String> emptyDecoratedBag = new TreeBag<>();
        final SortedBag<String> emptyCollectionBag = new CollectionSortedBag<>(emptyDecoratedBag);

        final Collection<String> nonEmptyCollection = Arrays.asList("A", "B");

        // Pre-condition checks
        assertTrue("The bag under test should be empty before the call", emptyCollectionBag.isEmpty());
        assertFalse("The collection to check against should not be empty", nonEmptyCollection.isEmpty());

        // Act: Check if the empty bag contains all elements of the non-empty collection.
        final boolean result = emptyCollectionBag.containsAll(nonEmptyCollection);

        // Assert: The result must be false.
        assertFalse("An empty bag cannot contain all elements of a non-empty collection", result);
    }

    @Test
    public void testContainsAll_onNonEmptyBagWithContainedElements_shouldReturnTrue() {
        // Arrange: Create a bag and a collection with elements that are all present in the bag.
        final SortedBag<String> decoratedBag = new TreeBag<>(Arrays.asList("A", "B", "C"));
        final SortedBag<String> collectionBag = new CollectionSortedBag<>(decoratedBag);

        final Collection<String> subsetCollection = Arrays.asList("A", "C");

        // Act: Check if the bag contains all elements of the subset collection.
        final boolean result = collectionBag.containsAll(subsetCollection);

        // Assert: The result must be true.
        assertTrue("The bag should contain all elements of the subset collection", result);
    }

    @Test
    public void testContainsAll_onNonEmptyBagWithSomeMissingElements_shouldReturnFalse() {
        // Arrange: Create a bag and a collection where some elements are not in the bag.
        final SortedBag<String> decoratedBag = new TreeBag<>(Arrays.asList("A", "B", "C"));
        final SortedBag<String> collectionBag = new CollectionSortedBag<>(decoratedBag);

        final Collection<String> supersetCollection = Arrays.asList("A", "D");

        // Act: Check if the bag contains all elements of the superset collection.
        final boolean result = collectionBag.containsAll(supersetCollection);

        // Assert: The result must be false.
        assertFalse("The bag should not contain all elements when some are missing", result);
    }
}