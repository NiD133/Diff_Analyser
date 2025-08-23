package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CartesianProductIterator}.
 * This class name has been simplified from CartesianProductIteratorTestTest8 for clarity.
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
        // The iterator under test is a product of three lists:
        // letters: {'A', 'B', 'C'} (size 3)
        // numbers: {'1', '2', '3'} (size 3)
        // symbols: {'!', '?'}     (size 2)
        // Total expected combinations: 3 * 3 * 2 = 18
        return new CartesianProductIterator<>(letters, numbers, symbols);
    }

    @BeforeEach
    public void setUp() {
        letters = Arrays.asList('A', 'B', 'C');
        numbers = Arrays.asList('1', '2', '3');
        symbols = Arrays.asList('!', '?');
    }

    @Override
    public boolean supportsRemove() {
        return false;
    }

    /**
     * Tests that forEachRemaining() consumes all elements from the iterator
     * and provides them in the correct cartesian product order.
     */
    @Override
    @Test
    @DisplayName("forEachRemaining() should consume all elements in the correct order")
    void testForEachRemaining() {
        // Arrange: Define the full set of expected results explicitly.
        // This makes the test's purpose clear without needing to analyze complex logic.
        final List<List<Character>> expectedTuples = generateExpectedTuples();
        final CartesianProductIterator<Character> iterator = makeObject();
        final List<List<Character>> actualTuples = new ArrayList<>();

        // Act: Consume the iterator and collect the results.
        iterator.forEachRemaining(actualTuples::add);

        // Assert: Verify that the collected results match the expected results.
        // assertEquals on two lists checks both size and element-by-element equality.
        assertEquals(expectedTuples, actualTuples);
    }

    /**
     * Helper method to generate the expected list of tuples for the test.
     * This isolates the test's setup from its execution and assertion.
     */
    private List<List<Character>> generateExpectedTuples() {
        final List<List<Character>> expected = new ArrayList<>();
        for (final Character letter : letters) {
            for (final Character number : numbers) {
                for (final Character symbol : symbols) {
                    expected.add(Arrays.asList(letter, number, symbol));
                }
            }
        }
        return expected;
    }
}