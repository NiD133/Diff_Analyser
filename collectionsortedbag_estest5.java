package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Locale;

/**
 * Tests for edge cases in {@link CollectionSortedBag}, particularly those
 * involving concurrent modification.
 */
public class CollectionSortedBagTest {

    /**
     * Verifies that calling retainAll() on a decorated bag with its own underlying
     * bag as the argument throws a ConcurrentModificationException.
     *
     * This tests a specific scenario where an operation on a decorator uses the
     * decorated collection as a parameter. This can lead to unsafe concurrent
     * iteration and modification if the decorator's implementation is not careful
     * to avoid modifying the collection while iterating over it.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void retainAllWithUnderlyingBagAsArgumentShouldThrowConcurrentModificationException() {
        // Arrange
        final SortedBag<Locale.FilteringMode> underlyingBag = new TreeBag<>();
        final SortedBag<Locale.FilteringMode> decoratedBag = new CollectionSortedBag<>(underlyingBag);

        // Add an element to the bag. The bag must not be empty for the
        // retainAll iterator to run and potentially trigger the concurrent modification.
        final Locale.FilteringMode sampleElement = Locale.FilteringMode.MAP_EXTENDED_RANGES;
        decoratedBag.add(sampleElement);

        // Act & Assert
        // The following call is expected to throw a ConcurrentModificationException.
        // The retainAll method iterates over the decorator, which in turn iterates
        // over the underlying bag. Passing the underlying bag as an argument can
        // cause it to be modified while the iteration is in progress.
        decoratedBag.retainAll(underlyingBag);
    }
}