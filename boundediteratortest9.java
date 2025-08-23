package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedIterator}.
 * This class name has been simplified from BoundedIteratorTestTest9 for clarity.
 */
public class BoundedIteratorTest<E> extends AbstractIteratorTest<E> {

    private final String[] testArray = { "a", "b", "c", "d", "e", "f", "g" };

    private List<E> fullList;

    @Override
    public Iterator<E> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<E>emptyList().iterator(), 0, 10);
    }

    @Override
    public Iterator<E> makeObject() {
        // BoundedIterator starting at offset 1 with a max of 6 elements.
        return new BoundedIterator<>(new ArrayList<>(fullList).iterator(), 1, fullList.size() - 1);
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        fullList = Arrays.asList((E[]) testArray);
    }

    @Test
    @DisplayName("remove() after iterating to the end should remove the last element")
    void testRemoveAfterConsumingIterator() {
        // Arrange
        // Create a BoundedIterator that starts at offset 1 ("b") and returns a maximum of 5 elements.
        final List<E> underlyingList = new ArrayList<>(fullList);
        final BoundedIterator<E> boundedIterator = new BoundedIterator<>(underlyingList.iterator(), 1, 5);

        // Act: Consume all elements from the iterator.
        while (boundedIterator.hasNext()) {
            boundedIterator.next();
        }

        // Sanity check: verify the iterator is now exhausted.
        assertFalse(boundedIterator.hasNext(), "Iterator should be exhausted after consuming all elements.");
        assertThrows(NoSuchElementException.class, boundedIterator::next,
                "next() should throw when iterator is exhausted.");

        // Act: Call remove() after the last call to next().
        // This should remove the last element returned, which was "f".
        boundedIterator.remove();

        // Assert
        // Verify that the element "f" was removed from the underlying list.
        @SuppressWarnings("unchecked")
        final List<E> expectedListAfterRemove = (List<E>) Arrays.asList("a", "b", "c", "d", "e", "g");
        assertEquals(expectedListAfterRemove, underlyingList,
                "The underlying list should be missing the removed element 'f'.");

        // Verify the iterator remains exhausted after the removal.
        assertFalse(boundedIterator.hasNext(), "Iterator should remain exhausted after remove().");
    }
}