package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.Bag;
import org.junit.Test;

import java.util.ConcurrentModificationException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

/**
 * Contains tests for {@link CollectionBag}.
 */
public class CollectionBagTest {

    /**
     * Tests that a ConcurrentModificationException is thrown when attempting to remove
     * the elements of the decorated bag from the decorator itself.
     *
     * <p>The {@code removeAll} method iterates over the bag's contents. If the collection
     * passed as an argument is the same underlying collection, modifications during
     * iteration will cause a fail-fast exception.</p>
     */
    @Test
    public void removeAll_whenModifyingTheUnderlyingBagDuringIteration_shouldThrowConcurrentModificationException() {
        // Arrange: Create a bag and its decorator, then add an element.
        Bag<Integer> decoratedBag = new TreeBag<>();
        Bag<Integer> collectionBag = new CollectionBag<>(decoratedBag);
        collectionBag.add(42);

        // Act & Assert: Attempting to remove the decorated bag from itself should fail.
        // This is because the `removeAll` operation iterates over `collectionBag`
        // while simultaneously modifying it, which is not allowed.
        assertThrows(ConcurrentModificationException.class, () -> {
            collectionBag.removeAll(decoratedBag);
        });
    }

    /**
     * This is an alternative implementation using a classic try-catch block,
     * which is closer to the original test's structure.
     */
    @Test
    public void removeAll_whenModifyingTheUnderlyingBagDuringIteration_shouldThrowConcurrentModificationException_tryCatch() {
        // Arrange
        Bag<Integer> decoratedBag = new TreeBag<>();
        Bag<Integer> collectionBag = new CollectionBag<>(decoratedBag);
        collectionBag.add(42);

        // Act & Assert
        try {
            collectionBag.removeAll(decoratedBag);
            fail("Expected a ConcurrentModificationException to be thrown, but it was not.");
        } catch (final ConcurrentModificationException e) {
            // Expected exception was thrown, so the test passes.
        }
    }
}