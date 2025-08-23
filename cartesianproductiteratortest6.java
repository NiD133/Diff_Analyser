package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CartesianProductIteratorTestTest6 extends AbstractIteratorTest<List<Character>> {

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
     * Tests that the iterator is empty if any of the input iterables is empty.
     * This aligns with set theory, where the Cartesian product of any set with an
     * empty set results in an empty set.
     */
    @Test
    void whenAnyInputIterableIsEmpty_thenIteratorHasNoElements() {
        // Arrange: Create an iterator where one of the input lists is empty.
        final CartesianProductIterator<Character> iterator =
            new CartesianProductIterator<>(letters, emptyList, symbols);

        // Assert: The iterator should report that it has no more elements.
        assertFalse(iterator.hasNext(),
            "Iterator should be empty if one of the input iterables is empty.");

        // Assert: Calling next() on an empty iterator must throw an exception.
        assertThrows(NoSuchElementException.class, iterator::next,
            "Calling next() on an empty iterator should throw NoSuchElementException.");
    }
}