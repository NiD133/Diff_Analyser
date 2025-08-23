package org.apache.commons.collections4.bag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * Tests for {@link CollectionBag}.
 */
public class CollectionBagTest {

    /**
     * Tests that the factory method collectionBag() throws a NullPointerException
     * when the decorated bag is null.
     */
    @Test
    public void collectionBagFactoryShouldThrowExceptionForNullInput() {
        try {
            CollectionBag.collectionBag((Bag<Object>) null);
            fail("Expected a NullPointerException to be thrown.");
        } catch (final NullPointerException e) {
            // The AbstractCollectionDecorator's constructor, which is called by CollectionBag,
            // uses Objects.requireNonNull(collection, "collection"), so this is the expected message.
            assertEquals("collection", e.getMessage());
        }
    }
}