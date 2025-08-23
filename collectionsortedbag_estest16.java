package org.apache.commons.collections4.bag;

import static org.junit.Assert.fail;

import java.util.ConcurrentModificationException;
import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * Tests for {@link CollectionSortedBag} focusing on concurrent modification scenarios.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that a ConcurrentModificationException is thrown when attempting to add a bag's
     * contents to itself using addAll().
     * <p>
     * This is the expected behavior for collections based on the fail-fast iterator
     * principle. The addAll() method iterates over the source collection while adding
     * elements to the destination. When the source and destination are the same, the
     * underlying collection is modified during iteration, which the iterator detects
     * and signals by throwing a ConcurrentModificationException.
     */
    @Test
    public void addAllWithSelfAsSourceShouldThrowConcurrentModificationException() {
        // Arrange
        // Use TreeBag as a concrete implementation of SortedBag.
        final SortedBag<String> underlyingBag = new TreeBag<>();
        underlyingBag.add("one");
        underlyingBag.add("two");

        // Decorate the bag with CollectionSortedBag, the class under test.
        final SortedBag<String> collectionSortedBag = CollectionSortedBag.collectionSortedBag(underlyingBag);

        // Act & Assert
        try {
            // Attempt to add the bag to itself. This operation should fail fast.
            collectionSortedBag.addAll(collectionSortedBag);
            fail("Expected a ConcurrentModificationException to be thrown.");
        } catch (final ConcurrentModificationException e) {
            // This exception is expected. The test passes.
        }
    }
}