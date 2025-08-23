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
 * Contains tests for the {@link CartesianProductIterator}.
 *
 * This class demonstrates improvements for a specific test case's understandability.
 */
public class CartesianProductIteratorTest extends AbstractIteratorTest<List<Character>> {

    // --- Test Fixtures ---

    private List<Character> letters;
    private List<Character> numbers;
    private List<Character> symbols;
    private List<Character> emptyList;

    @BeforeEach
    public void setUp() {
        letters = Arrays.asList('A', 'B', 'C');
        numbers = Arrays.asList('1', '2', '3');
        symbols = Arrays.asList('!', '?');
        emptyList = Collections.emptyList();
    }

    // --- AbstractIteratorTest Implementation ---

    @Override
    public CartesianProductIterator<Character> makeEmptyIterator() {
        return new CartesianProductIterator<>();
    }

    @Override
    public CartesianProductIterator<Character> makeObject() {
        return new CartesianProductIterator<>(letters, numbers, symbols);
    }

    @Override
    public boolean supportsRemove() {
        return false;
    }

    // --- Test Cases ---

    /**
     * Verifies that the Cartesian product is empty if any of the input lists is empty.
     * The resulting iterator should immediately indicate that it has no elements.
     */
    @Test
    void shouldBeEmptyWhenAnyInputListIsEmpty() {
        // Given: Input lists for the iterator, where one of them is empty.
        final CartesianProductIterator<Character> iterator =
                new CartesianProductIterator<>(letters, numbers, emptyList);

        // Then: The iterator should report that it has no next element.
        assertFalse(iterator.hasNext(),
                "Iterator should be empty because one of the input lists is empty.");

        // And: Attempting to get the next element should throw an exception,
        // confirming the iterator is truly empty.
        assertThrows(NoSuchElementException.class, iterator::next,
                "Calling next() on an empty iterator should throw an exception.");
    }
}