package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link ObjectGraphIterator}.
 * This refactored test focuses on the behavior of the remove() method
 * when dealing with nested iterators.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that calling remove() on an ObjectGraphIterator correctly removes the element
     * from the underlying collection of a nested iterator.
     */
    @Test
    public void removeAfterNextOnNestedIteratorShouldRemoveFromUnderlyingCollection() {
        // Arrange
        // 1. Create the underlying collection that will be modified.
        final LinkedList<Integer> underlyingList = new LinkedList<>();
        underlyingList.add(-42);

        // 2. Create a base iterator. We need to manually set its state to use
        //    the iterator from our list. This is done via the protected
        //    findNextByIterator method, which prepares the iterator to yield the
        //    next element from the provided iterator.
        final ObjectGraphIterator<Integer> baseIterator = new ObjectGraphIterator<>(999, null); // Root is irrelevant.
        baseIterator.findNextByIterator(underlyingList.iterator());

        // 3. Create the main ObjectGraphIterator to be tested, which wraps the base iterator.
        //    This creates a nested structure: graphIterator -> baseIterator -> list.iterator().
        final ObjectGraphIterator<Integer> graphIterator = new ObjectGraphIterator<>(baseIterator);

        // Pre-condition check: ensure the iterator is ready and the list is not empty.
        assertTrue("Iterator should have an element before calling next()", graphIterator.hasNext());
        assertEquals("Underlying list should have one element initially", 1, underlyingList.size());

        // Act
        // 1. Retrieve the element from the iterator.
        final Integer retrievedElement = graphIterator.next();

        // 2. Remove the element that was just retrieved.
        graphIterator.remove();

        // Assert
        // 1. Verify that the correct element was retrieved.
        assertEquals("The retrieved element should be the one from the list", Integer.valueOf(-42), retrievedElement);

        // 2. Verify that the remove() call was propagated to the underlying list.
        assertTrue("The underlying list should be empty after remove()", underlyingList.isEmpty());

        // 3. Verify that the iterator is now exhausted.
        assertFalse("Iterator should have no more elements after remove()", graphIterator.hasNext());
    }
}