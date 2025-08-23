package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Collection;
import org.apache.commons.collections4.SortedBag;

// The original test class name is kept as requested.
// In a real-world scenario, it would be renamed to something more descriptive,
// e.g., "CollectionSortedBagTest".
public class CollectionSortedBag_ESTestTest44 {

    /**
     * Tests that containsAll() returns true when the argument is an empty collection.
     * According to the java.util.Collection contract, any collection is considered
     * to contain all the elements of an empty collection.
     */
    @Test
    public void containsAll_shouldReturnTrue_whenCheckingForAnEmptyCollection() {
        // Arrange
        // Create an empty bag decorated by CollectionSortedBag.
        final SortedBag<String> backingBag = new TreeBag<>();
        final SortedBag<String> decoratedBag = new CollectionSortedBag<>(backingBag);

        // The collection to check for containment, which is empty.
        final Collection<String> emptyCollection = Collections.emptyList();

        // Act
        final boolean result = decoratedBag.containsAll(emptyCollection);

        // Assert
        assertTrue("A collection should always contain all elements of an empty collection.", result);
    }
}