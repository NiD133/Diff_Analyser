package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * Tests for {@link CartesianProductIterator} focusing on failure scenarios.
 */
public class CartesianProductIteratorTest {

    /**
     * Tests that the iterator fails fast with a ConcurrentModificationException
     * if an underlying collection is modified after the iterator is created.
     * This is the expected behavior of iterators that follow the Java Collections Framework contract.
     */
    @Test(expected = ConcurrentModificationException.class)
    public void testNextThrowsExceptionWhenUnderlyingCollectionIsModified() {
        // Arrange: Create an iterator from a single list.
        final List<String> sourceList = new ArrayList<>();
        sourceList.add("A");

        // The CartesianProductIterator takes an array of Iterables.
        @SuppressWarnings("unchecked") // Safe as we are creating and populating the array ourselves.
        final Iterable<String>[] iterables = new Iterable[]{sourceList};
        final CartesianProductIterator<String> iterator = new CartesianProductIterator<>(iterables);

        // Act: Modify the source collection after the iterator has been created.
        // This invalidates the internal iterator state.
        sourceList.add("B");

        // Assert: Calling next() is now expected to throw a ConcurrentModificationException.
        // The @Test(expected = ...) annotation handles the assertion.
        iterator.next();
    }
}