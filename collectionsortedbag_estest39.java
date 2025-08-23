package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

/**
 * Test suite for {@link CollectionSortedBag}.
 */
public class CollectionSortedBagTest {

    @Test
    public void removeAllWithNullCollectionShouldReturnFalseAndNotModifyBag() {
        // Arrange
        // Create an empty bag and its decorator
        final TreeBag<Object> backingBag = new TreeBag<>();
        final CollectionSortedBag<Object> decoratedBag = new CollectionSortedBag<>(backingBag);

        // Act
        // Attempt to remove a null collection of elements.
        // The contract for Collection.removeAll() suggests a NullPointerException,
        // but the implementation in AbstractMapBag (a superclass of TreeBag)
        // explicitly handles null by returning false.
        final boolean wasModified = decoratedBag.removeAll((Collection<?>) null);

        // Assert
        // The operation should report that the bag was not modified.
        assertFalse("removeAll(null) should return false", wasModified);
        // Verify that the bag remains unchanged (and empty in this case).
        assertTrue("The bag should not be modified by removing a null collection", decoratedBag.isEmpty());
    }
}