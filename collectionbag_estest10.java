package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * Tests for {@link CollectionBag} focusing on its decorator behavior.
 */
public class CollectionBagTest {

    /**
     * Tests that calling remove(null) on a CollectionBag that decorates a TreeBag
     * correctly propagates the expected NullPointerException.
     * <p>
     * The test verifies that the decorator passes the method call to the underlying
     * bag, and that exceptions from the underlying bag are not swallowed. A TreeBag
     * is used as the decorated instance because it is known to not support null elements.
     */
    @Test(expected = NullPointerException.class)
    public void removeNullFromCollectionBagDecoratingTreeBagThrowsNullPointerException() {
        // Arrange: Create a CollectionBag that decorates a TreeBag.
        // TreeBag does not support null elements.
        final Bag<Integer> decoratedBag = new TreeBag<>();
        final Bag<Integer> collectionBag = CollectionBag.collectionBag(decoratedBag);

        // Act & Assert: Attempting to remove a null element should throw a
        // NullPointerException, which is the expected behavior of the underlying TreeBag.
        collectionBag.remove(null);
    }
}