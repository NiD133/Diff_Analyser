package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CartesianProductIterator}.
 * This class name has been improved from the original CartesianProductIteratorTestTest7.
 */
public class CartesianProductIteratorTest extends AbstractIteratorTest<List<Character>> {

    private List<Character> letters;
    private List<Character> numbers;
    private List<Character> symbols;

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
        // The emptyList field was unused in the original test and has been removed.
    }

    @Override
    public boolean supportsRemove() {
        return false;
    }

    /**
     * Verifies that the iterator correctly produces the full Cartesian product
     * when the same list is provided as input multiple times.
     */
    @Test
    void iteratorWithRepeatedListShouldProduceFullCartesianProduct() {
        // Arrange
        final CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(letters, letters, letters);
        final int expectedSize = (int) Math.pow(letters.size(), 3); // 3^3 = 27
        int actualSize = 0;

        // Act & Assert
        // Generate the expected tuples on-the-fly and compare with the iterator's output.
        // This avoids storing all results in memory.
        for (final Character c1 : letters) {
            for (final Character c2 : letters) {
                for (final Character c3 : letters) {
                    final List<Character> expectedTuple = Arrays.asList(c1, c2, c3);

                    assertTrue(iterator.hasNext(), "Iterator should have more elements at this point.");
                    assertEquals(expectedTuple, iterator.next());
                    actualSize++;
                }
            }
        }

        // Final assertions to ensure the iterator is fully exhausted
        assertEquals(expectedSize, actualSize, "The number of iterated elements should match the expected size.");
        assertFalse(iterator.hasNext(), "Iterator should be exhausted after consuming all elements.");
        assertThrows(NoSuchElementException.class, iterator::next,
                "Calling next() on an exhausted iterator should throw an exception.");
    }
}