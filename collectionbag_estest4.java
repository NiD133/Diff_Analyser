package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link CollectionBag}.
 * This refactored test focuses on a specific scenario from the original generated test.
 */
public class CollectionBagTest {

    /**
     * Tests that calling retainAll() on a CollectionBag throws an UnsupportedOperationException
     * if the bag it decorates is unmodifiable.
     *
     * The retainAll() operation needs to remove elements, which is forbidden by the
     * underlying unmodifiable bag's iterator.
     */
    @Test
    public void retainAllShouldThrowExceptionWhenDecoratingAnUnmodifiableBag() {
        // Arrange: Create a CollectionBag that wraps an unmodifiable bag containing an element.
        final Bag<Integer> baseBag = new TreeBag<>();
        baseBag.add(100);

        final SortedBag<Integer> unmodifiableBag = UnmodifiableSortedBag.unmodifiableSortedBag(baseBag);
        final Bag<Integer> collectionBag = new CollectionBag<>(unmodifiableBag);

        // Act & Assert: Attempting to retain an empty collection should try to remove the element.
        try {
            collectionBag.retainAll(Collections.emptySet());
            fail("Expected an UnsupportedOperationException because the underlying bag is unmodifiable.");
        } catch (final UnsupportedOperationException e) {
            // The exception is expected. Verify its message for more precise testing.
            assertEquals("remove() is not supported", e.getMessage());
        }
    }
}