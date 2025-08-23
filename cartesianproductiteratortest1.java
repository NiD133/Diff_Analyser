package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Tests for {@link CartesianProductIterator}.
 * <p>
 * This class extends {@link AbstractIteratorTest} to leverage a standard suite
 * of iterator tests, while also adding specific tests for the CartesianProductIterator.
 * </p>
 */
class CartesianProductIteratorTest extends AbstractIteratorTest<List<Character>> {

    private List<Character> letters;
    private List<Character> numbers;
    private List<Character> symbols;

    @BeforeEach
    void setup() {
        letters = Arrays.asList('A', 'B', 'C');
        numbers = Arrays.asList('1', '2', '3');
        symbols = Arrays.asList('!', '?');
    }

    /**
     * Creates a standard, non-empty iterator for the abstract superclass tests.
     * The product will be of letters, numbers, and symbols.
     */
    @Override
    public CartesianProductIterator<Character> makeObject() {
        return new CartesianProductIterator<>(letters, numbers, symbols);
    }

    /**
     * Creates an empty iterator for the abstract superclass tests.
     * A CartesianProductIterator is empty if constructed with no arguments.
     */
    @Override
    public CartesianProductIterator<Character> makeEmptyIterator() {
        return new CartesianProductIterator<>();
    }

    /**
     * The iterator does not support the remove operation.
     */
    @Override
    public boolean supportsRemove() {
        return false;
    }

    @Test
    @DisplayName("Iterator should be empty if any input iterable is empty")
    void shouldBeEmptyWhenAnyInputIsEmpty() {
        // Arrange: Create an iterator where one of the input lists is empty.
        final CartesianProductIterator<Character> iterator =
                new CartesianProductIterator<>(letters, Collections.emptyList());

        // Assert: The resulting iterator should have no elements.
        assertFalse(iterator.hasNext(), "Iterator should be empty if one input is empty.");
        assertThrows(NoSuchElementException.class, iterator::next,
                "Calling next() on an empty iterator should throw an exception.");
    }
}