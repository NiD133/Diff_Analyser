package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * Tests for {@link CollectionBag} to ensure it correctly handles null arguments.
 */
public class CollectionBagTest {

    /**
     * Verifies that calling addAll() with a null collection
     * throws a NullPointerException, as specified by the
     * {@link java.util.Collection#addAll(java.util.Collection)} contract.
     */
    @Test(expected = NullPointerException.class)
    public void addAllWithNullCollectionShouldThrowNullPointerException() {
        // Arrange: Create an empty CollectionBag instance for the test.
        final Bag<Integer> collectionBag = new CollectionBag<>(new TreeBag<>());

        // Act: Call the method under test with a null argument.
        // The test framework will assert that a NullPointerException is thrown.
        collectionBag.addAll(null);
    }
}