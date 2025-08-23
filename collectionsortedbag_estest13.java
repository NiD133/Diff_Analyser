package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

/**
 * Tests for {@link CollectionSortedBag}.
 * This test focuses on the behavior of the containsAll method with incompatible collection types.
 */
public class CollectionSortedBag_ESTestTest13 {

    /**
     * Verifies that calling containsAll() with a collection of an incompatible
     * element type throws a ClassCastException. The underlying sorted collection
     * (like TreeBag) will fail when it tries to compare elements of different types.
     */
    @Test(expected = ClassCastException.class)
    public void containsAll_shouldThrowClassCastException_whenCheckingForIncompatibleType() {
        // Arrange: Create a bag designed to hold Integers.
        // We use a TreeBag as the decorated implementation, which requires comparable elements.
        final SortedBag<Integer> decoratedBag = new TreeBag<>();
        final SortedBag<Integer> bagOfIntegers = new CollectionSortedBag<>(decoratedBag);

        // Arrange: Create a collection containing an element of an incompatible type (a String).
        final Collection<String> incompatibleStringCollection = Collections.singleton("incompatible type");

        // Act: Attempt to check if the integer bag contains an element from the string collection.
        // This is expected to throw a ClassCastException because the underlying TreeBag
        // cannot compare an Integer with a String.
        bagOfIntegers.containsAll(incompatibleStringCollection);

        // Assert: A ClassCastException is expected, as declared by the @Test annotation.
    }
}