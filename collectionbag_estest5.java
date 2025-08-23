package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * Unit tests for {@link CollectionBag}.
 */
public class CollectionBagTest {

    /**
     * Tests that calling retainAll() with a null collection argument
     * throws a NullPointerException, as specified by the
     * {@link java.util.Collection#retainAll(java.util.Collection)} contract.
     */
    @Test(expected = NullPointerException.class)
    public void retainAllWithNullCollectionShouldThrowNullPointerException() {
        // Arrange: Create a CollectionBag instance. The decorated bag can be empty
        // as the null check should precede any element processing.
        final Bag<Object> decoratedBag = new TreeBag<>();
        final Bag<Object> collectionBag = new CollectionBag<>(decoratedBag);

        // Act: Attempt to retain a null collection.
        collectionBag.retainAll(null);

        // Assert: The test expects a NullPointerException, which is handled by
        // the `expected` attribute of the @Test annotation.
    }
}