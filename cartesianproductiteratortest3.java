package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for CartesianProductIterator.
 * This class focuses on specific edge cases not covered by the abstract parent tests.
 */
// Renamed class for clarity and to follow standard naming conventions.
public class CartesianProductIteratorTest extends AbstractIteratorTest<List<Character>> {

    private List<Character> letters;
    private List<Character> numbers;
    private List<Character> symbols;
    private List<Character> emptyList;

    @Override
    public CartesianProductIterator<Character> makeEmptyIterator() {
        // An iterator with no input iterables is empty.
        return new CartesianProductIterator<>();
    }

    @Override
    public CartesianProductIterator<Character> makeObject() {
        // A standard, non-empty iterator for the abstract tests.
        return new CartesianProductIterator<>(letters, numbers, symbols);
    }

    @BeforeEach
    public void setUp() {
        letters = Arrays.asList('A', 'B', 'C');
        numbers = Arrays.asList('1', '2', '3');
        symbols = Arrays.asList('!', '?');
        emptyList = Collections.emptyList();
    }

    @Override
    public boolean supportsRemove() {
        return false;
    }

    /**
     * Verifies that the iterator is empty if all input iterables are empty.
     * According to the class documentation, if any input is empty, the
     * entire cartesian product is empty.
     */
    @Test
    void testIteratorIsEmptyWhenAllInputsAreEmpty() {
        // Arrange: Create an iterator where all inputs are empty lists.
        final CartesianProductIterator<Character> iterator =
                new CartesianProductIterator<>(emptyList, emptyList, emptyList);

        // Assert: The iterator should immediately report that it has no elements.
        assertFalse(iterator.hasNext(),
                "Iterator should be empty when all input lists are empty.");

        // Assert: Calling next() on an empty iterator must throw an exception,
        // confirming the iterator contract.
        assertThrows(NoSuchElementException.class, iterator::next,
                "Calling next() on an empty iterator should throw NoSuchElementException.");
    }
}