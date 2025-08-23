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
 * Tests for {@link CartesianProductIterator}.
 * This class focuses on edge cases like empty input iterables.
 */
public class CartesianProductIteratorTest extends AbstractIteratorTest<List<Character>> {

    private List<Character> letters;
    private List<Character> numbers;
    private List<Character> symbols;
    private List<Character> emptyList;

    @Override
    public CartesianProductIterator<Character> makeEmptyIterator() {
        return new CartesianProductIterator<>();
    }

    @Override
    public CartesianProductIterator<Character> makeObject() {
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
     * The original test was specific to the first list being empty.
     * This improved test directly and clearly verifies the iterator's state.
     */
    @Test
    void shouldBeEmptyWhenFirstIterableIsEmpty() {
        // Arrange: Create an iterator where the first input iterable is empty.
        // The cartesian product should be empty if any of its components are empty.
        final CartesianProductIterator<Character> iterator =
            new CartesianProductIterator<>(emptyList, numbers, symbols);

        // Assert: The iterator should report that it has no elements.
        assertFalse(iterator.hasNext(), "Iterator should be empty if the first iterable is empty");

        // Assert: Calling next() on an empty iterator must throw an exception.
        assertThrows(NoSuchElementException.class, iterator::next,
            "Calling next() on an exhausted iterator should throw an exception");
    }
}