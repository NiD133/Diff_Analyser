package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CartesianProductIteratorTestTest2 extends AbstractIteratorTest<List<Character>> {

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
     * Tests that the iterator produces all possible combinations in the correct, predictable order.
     */
    @Test
    void shouldGenerateAllCombinationsInCorrectOrder() {
        // Arrange: Define the full set of expected combinations in the precise order.
        // The nested loops mirror the logic of the CartesianProductIterator, making the expected
        // output clear and easy to verify.
        final List<List<Character>> expectedCombinations = new ArrayList<>();
        for (final Character letter : letters) {
            for (final Character number : numbers) {
                for (final Character symbol : symbols) {
                    expectedCombinations.add(Arrays.asList(letter, number, symbol));
                }
            }
        }
        // A quick sanity check on the expected size.
        assertEquals(18, expectedCombinations.size());

        // Act: Create the iterator and collect all its elements into a list.
        final CartesianProductIterator<Character> cartesianIterator = makeObject();
        final List<List<Character>> actualCombinations = new ArrayList<>();
        cartesianIterator.forEachRemaining(actualCombinations::add);

        // Assert: The generated combinations must match the expected list in both content and order.
        assertEquals(expectedCombinations, actualCombinations);

        // Assert: The iterator must be fully exhausted and behave correctly at its end.
        assertFalse(cartesianIterator.hasNext(), "hasNext() should be false after full iteration");
        assertThrows(NoSuchElementException.class, cartesianIterator::next,
                "next() should throw NoSuchElementException after full iteration");
    }
}