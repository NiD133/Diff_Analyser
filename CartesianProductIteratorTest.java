package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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

/**
 * Unit tests for {@link CartesianProductIterator}.
 */
class CartesianProductIteratorTest extends AbstractIteratorTest<List<Character>> {

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

    @Test
    void testEmptyCollection() {
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(letters, emptyList);
        assertFalse(iterator.hasNext(), "Iterator should not have next element when one list is empty");
        assertThrows(NoSuchElementException.class, iterator::next, "Calling next() should throw NoSuchElementException");
    }

    @Test
    void testAllTuplesGenerated() {
        List<Character[]> expectedTuples = generateExpectedTuples(letters, numbers, symbols);
        CartesianProductIterator<Character> iterator = makeObject();
        List<Character[]> actualTuples = collectTuples(iterator);

        assertEquals(expectedTuples.size(), actualTuples.size(), "Number of tuples should match expected count");
        assertArrayEquals(expectedTuples.toArray(), actualTuples.toArray(), "Tuples should match expected tuples");
    }

    @Test
    void testNoTuplesWithAllEmptyLists() {
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(emptyList, emptyList, emptyList);
        assertFalse(iterator.hasNext(), "Iterator should not have next element when all lists are empty");
    }

    @Test
    void testNoTuplesWithEmptyFirstList() {
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(emptyList, numbers, symbols);
        assertFalse(iterator.hasNext(), "Iterator should not have next element when the first list is empty");
    }

    @Test
    void testNoTuplesWithEmptyLastList() {
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(letters, numbers, emptyList);
        assertFalse(iterator.hasNext(), "Iterator should not have next element when the last list is empty");
    }

    @Test
    void testNoTuplesWithAnyEmptyList() {
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(letters, emptyList, symbols);
        assertFalse(iterator.hasNext(), "Iterator should not have next element when any list is empty");
    }

    @Test
    void testAllTuplesWithSameList() {
        List<Character[]> expectedTuples = generateExpectedTuples(letters, letters, letters);
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(letters, letters, letters);
        List<Character[]> actualTuples = collectTuples(iterator);

        assertEquals(expectedTuples.size(), actualTuples.size(), "Number of tuples should match expected count");
        assertArrayEquals(expectedTuples.toArray(), actualTuples.toArray(), "Tuples should match expected tuples");
    }

    @Override
    @Test
    void testForEachRemaining() {
        List<Character[]> expectedTuples = generateExpectedTuples(letters, numbers, symbols);
        CartesianProductIterator<Character> iterator = makeObject();
        List<Character[]> actualTuples = new ArrayList<>();

        iterator.forEachRemaining(tuple -> actualTuples.add(tuple.toArray(new Character[0])));

        assertEquals(expectedTuples.size(), actualTuples.size(), "Number of tuples should match expected count");
        assertArrayEquals(expectedTuples.toArray(), actualTuples.toArray(), "Tuples should match expected tuples");
    }

    @Test
    void testRemoveThrows() {
        CartesianProductIterator<Character> iterator = makeObject();
        assertThrows(UnsupportedOperationException.class, iterator::remove, "Calling remove() should throw UnsupportedOperationException");
    }

    /**
     * Helper method to generate expected tuples for comparison.
     */
    private List<Character[]> generateExpectedTuples(List<Character>... lists) {
        List<Character[]> expectedTuples = new ArrayList<>();
        for (Character a : lists[0]) {
            for (Character b : lists[1]) {
                for (Character c : lists[2]) {
                    expectedTuples.add(new Character[]{a, b, c});
                }
            }
        }
        return expectedTuples;
    }

    /**
     * Helper method to collect tuples from the iterator.
     */
    private List<Character[]> collectTuples(CartesianProductIterator<Character> iterator) {
        List<Character[]> tuples = new ArrayList<>();
        while (iterator.hasNext()) {
            tuples.add(iterator.next().toArray(new Character[0]));
        }
        return tuples;
    }
}