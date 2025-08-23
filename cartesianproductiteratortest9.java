package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CartesianProductIterator}.
 * <p>
 * This class extends {@link AbstractIteratorTest} to leverage a suite of
 * common iterator contract tests. It provides concrete implementations for the
 * abstract test setup methods and adds tests for behaviors specific to
 * {@link CartesianProductIterator}.
 * </p>
 */
public class CartesianProductIteratorTest extends AbstractIteratorTest<List<Character>> {

    private List<Character> letters;
    private List<Character> numbers;
    private List<Character> symbols;

    @BeforeEach
    public void setUp() {
        // Initializes test data used to form the Cartesian product
        letters = Arrays.asList('A', 'B', 'C');
        numbers = Arrays.asList('1', '2', '3');
        symbols = Arrays.asList('!', '?');
    }

    /**
     * Creates an iterator over a Cartesian product of three non-empty lists.
     * Used by tests in the superclass to verify behavior with a typical, non-empty iterator.
     *
     * @return an iterator for [{A,B,C}, {1,2,3}, {!,?}]
     */
    @Override
    public CartesianProductIterator<Character> makeObject() {
        return new CartesianProductIterator<>(letters, numbers, symbols);
    }

    /**
     * Creates an iterator that should be empty because it is given no backing iterables.
     * Used by tests in the superclass to verify behavior with an empty iterator.
     */
    @Override
    public CartesianProductIterator<Character> makeEmptyIterator() {
        return new CartesianProductIterator<>();
    }

    /**
     * Informs the abstract test suite that {@link CartesianProductIterator#remove()} is not supported.
     *
     * @return false, always.
     */
    @Override
    public boolean supportsRemove() {
        return false;
    }

    /**
     * Verifies that calling remove() on the iterator is an unsupported operation.
     */
    @Test
    void removeShouldThrowUnsupportedOperationException() {
        // Given a new CartesianProductIterator
        final CartesianProductIterator<Character> iterator = makeObject();

        // When the remove() method is called
        // Then an UnsupportedOperationException should be thrown
        assertThrows(UnsupportedOperationException.class,
                     iterator::remove,
                     "The remove() operation should not be supported by CartesianProductIterator.");
    }
}