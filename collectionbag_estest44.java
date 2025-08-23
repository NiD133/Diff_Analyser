package org.apache.commons.collections4.bag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

/**
 * Tests for {@link CollectionBag}.
 */
public class CollectionBagTest {

    /**
     * Tests that decorating an empty bag with CollectionBag.collectionBag()
     * results in a new empty bag.
     */
    @Test
    public void testCollectionBagFactoryWithEmptyBag() {
        // Arrange: Create an empty source bag.
        final Bag<String> sourceBag = new HashBag<>();

        // Act: Decorate the empty bag using the factory method.
        final Bag<String> decoratedBag = CollectionBag.collectionBag(sourceBag);

        // Assert: The decorated bag should also be empty.
        assertTrue("The decorated bag should be empty", decoratedBag.isEmpty());
        assertEquals("The decorated bag should have a size of 0", 0, decoratedBag.size());
    }
}