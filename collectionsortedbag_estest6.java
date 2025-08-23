package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

/**
 * Unit tests for {@link CollectionSortedBag}.
 */
public class CollectionSortedBagTest {

    /**
     * Tests that calling retainAll() with a null collection argument
     * throws a NullPointerException, as required by the Collection contract.
     */
    @Test(expected = NullPointerException.class)
    public void retainAllWithNullCollectionShouldThrowNullPointerException() {
        // Arrange: Create a CollectionSortedBag decorating a simple TreeBag.
        // The specific implementation of the decorated bag is not important for this test,
        // as we are verifying the behavior of the decorator's contract.
        final SortedBag<String> decorated = new TreeBag<>();
        final SortedBag<String> collectionSortedBag = new CollectionSortedBag<>(decorated);

        // Act: Call retainAll with a null argument.
        // Assert: The @Test(expected) annotation asserts that a NullPointerException is thrown.
        collectionSortedBag.retainAll(null);
    }
}