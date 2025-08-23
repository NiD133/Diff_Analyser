package org.apache.commons.collections4.bag;

import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Collections;
import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * Tests for {@link CollectionBag} focusing on its interaction with decorated bags.
 */
public class CollectionBagTest {

    /**
     * Tests that a modification attempt on a CollectionBag throws an exception
     * if it decorates an unmodifiable underlying bag.
     */
    @Test
    public void removeAllOnCollectionBagDecoratingUnmodifiableBagShouldThrowException() {
        // Arrange: Create a CollectionBag that decorates an unmodifiable bag.
        final Bag<String> unmodifiableBag = UnmodifiableBag.unmodifiableBag(new HashBag<>());
        final Bag<String> collectionBag = new CollectionBag<>(unmodifiableBag);

        // Create a collection of items to attempt to remove.
        final Collection<String> itemsToRemove = Collections.singleton("item to remove");

        // Act & Assert: Verify that calling a modification method throws an exception.
        try {
            collectionBag.removeAll(itemsToRemove);
            fail("Expected an UnsupportedOperationException because the underlying bag is unmodifiable.");
        } catch (final UnsupportedOperationException e) {
            // This is the expected behavior. The test passes.
        }
    }
}