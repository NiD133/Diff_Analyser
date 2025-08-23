package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * Contains a test for the CollectionSortedBag class.
 * This test was improved for clarity from an auto-generated (EvoSuite) original.
 */
public class CollectionSortedBag_ESTestTest10 {

    /**
     * A simple helper class whose instances are not comparable to each other.
     * Its purpose is to be used in tests that require a non-Comparable object.
     */
    private static class NonComparableObject {
    }

    /**
     * Tests that removeAll() throws a ClassCastException when the decorated TreeBag
     * cannot compare an element from the removal collection with elements already in the bag.
     *
     * The original auto-generated test was flawed, as the exception was thrown during
     * test setup (adding a non-comparable element to a PriorityQueue) rather than
     * during the call to the method under test. This version corrects the logic.
     */
    @Test(timeout = 4000, expected = ClassCastException.class)
    public void removeAllWithNonComparableElementShouldThrowClassCastException() {
        // Arrange
        // A TreeBag requires its elements to be Comparable. We add a String to ensure
        // the bag is not empty, so that a comparison will be attempted.
        final SortedBag<Object> underlyingBag = new TreeBag<>();
        underlyingBag.add("a comparable element");

        // The CollectionSortedBag decorates the underlying TreeBag.
        final SortedBag<Object> collectionSortedBag = new CollectionSortedBag<>(underlyingBag);

        // Create a list containing a non-comparable object to be removed.
        final List<Object> elementsToRemove = Collections.singletonList(new NonComparableObject());

        // Act & Assert
        // The removeAll call will delegate to the TreeBag. The TreeBag will try to
        // compare the NonComparableObject with the String element already present,
        // which results in a ClassCastException because NonComparableObject does not
        // implement Comparable.
        collectionSortedBag.removeAll(elementsToRemove);
    }
}