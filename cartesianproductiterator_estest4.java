package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Contains improved, human-readable tests for {@link CartesianProductIterator}.
 */
public class CartesianProductIteratorTest {

    /**
     * Tests that the iterator is empty if any of the provided iterables is empty.
     * <p>
     * According to the definition of a Cartesian product, if any of the input sets
     * is empty, the resulting product is also an empty set. This test verifies
     * that the iterator correctly implements this behavior.
     */
    @Test
    public void hasNextShouldReturnFalseWhenAnyInputIterableIsEmpty() {
        // Arrange: Create two iterables, one of which is empty.
        // This setup is much simpler and clearer than the original test's use of
        // raw arrays, PriorityQueue, and obscure types.
        final List<String> nonEmptyIterable = List.of("A", "B");
        final List<String> emptyIterable = Collections.emptyList();

        // Act: Create a CartesianProductIterator with one empty input.
        // The constructor is expected to detect the empty iterable and initialize
        // the iterator to an empty state.
        final CartesianProductIterator<String> iterator =
                new CartesianProductIterator<>(nonEmptyIterable, emptyIterable);

        // Assert: The resulting iterator should have no elements.
        assertFalse("Iterator should be empty when one of the input iterables is empty.", iterator.hasNext());
    }
}