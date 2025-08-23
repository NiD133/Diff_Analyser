package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.List;

/**
 * Unit tests for {@link CartesianProductIterator}.
 */
public class CartesianProductIteratorTest {

    /**
     * Tests that hasNext() returns true when the iterator is created
     * with non-empty iterables.
     */
    @Test
    public void hasNext_shouldReturnTrue_whenAllIterablesAreNonEmpty() {
        // Arrange
        // Create two non-empty lists to form the Cartesian product.
        final List<String> list1 = List.of("A");
        final List<Integer> list2 = List.of(1);

        // Act
        // Create the CartesianProductIterator with the two lists.
        final CartesianProductIterator<Object> iterator = new CartesianProductIterator<>(list1, list2);

        // Assert
        // The iterator should have a next element because neither input list is empty.
        assertTrue("Iterator should have a next element", iterator.hasNext());
    }
}