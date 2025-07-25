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
    void testExhaustivity() {
        List<Character[]> expectedResults = generateExpectedResults(letters, numbers, symbols);
        List<Character[]> actualResults = collectResults(makeObject());

        assertEquals(expectedResults.size(), actualResults.size(), "Result size should match expected size");
        assertArrayEquals(expectedResults.toArray(), actualResults.toArray(), "Results should match expected tuples");
    }

    @Test
    void testExhaustivityWithAllEmptyLists() {
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(emptyList, emptyList, emptyList);
        List<Character[]> results = collectResults(iterator);

        assertEquals(0, results.size(), "Result size should be zero when all lists are empty");
    }

    @Test
    void testExhaustivityWithEmptyFirstList() {
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(emptyList, numbers, symbols);
        List<Character[]> results = collectResults(iterator);

        assertEquals(0, results.size(), "Result size should be zero when the first list is empty");
    }

    @Test
    void testExhaustivityWithEmptyLastList() {
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(letters, numbers, emptyList);
        List<Character[]> results = collectResults(iterator);

        assertEquals(0, results.size(), "Result size should be zero when the last list is empty");
    }

    @Test
    void testExhaustivityWithEmptyList() {
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(letters, emptyList, symbols);
        List<Character[]> results = collectResults(iterator);

        assertEquals(0, results.size(), "Result size should be zero when any list is empty");
    }

    @Test
    void testExhaustivityWithSameList() {
        List<Character[]> expectedResults = generateExpectedResults(letters, letters, letters);
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(letters, letters, letters);
        List<Character[]> actualResults = collectResults(iterator);

        assertEquals(expectedResults.size(), actualResults.size(), "Result size should match expected size");
        assertArrayEquals(expectedResults.toArray(), actualResults.toArray(), "Results should match expected tuples");
    }

    @Override
    @Test
    void testForEachRemaining() {
        List<Character[]> expectedResults = generateExpectedResults(letters, numbers, symbols);
        CartesianProductIterator<Character> iterator = makeObject();
        List<Character[]> actualResults = new ArrayList<>();
        iterator.forEachRemaining(tuple -> actualResults.add(tuple.toArray(new Character[0])));

        assertEquals(expectedResults.size(), actualResults.size(), "Result size should match expected size");
        assertArrayEquals(expectedResults.toArray(), actualResults.toArray(), "Results should match expected tuples");
    }

    @Test
    void testRemoveThrows() {
        CartesianProductIterator<Character> iterator = makeObject();
        assertThrows(UnsupportedOperationException.class, iterator::remove, "Calling remove() should throw UnsupportedOperationException");
    }

    private List<Character[]> generateExpectedResults(List<Character>... lists) {
        List<Character[]> results = new ArrayList<>();
        for (Character a : lists[0]) {
            for (Character b : lists[1]) {
                for (Character c : lists[2]) {
                    results.add(new Character[]{a, b, c});
                }
            }
        }
        return results;
    }

    private List<Character[]> collectResults(CartesianProductIterator<Character> iterator) {
        List<Character[]> results = new ArrayList<>();
        while (iterator.hasNext()) {
            List<Character> tuple = iterator.next();
            results.add(tuple.toArray(new Character[0]));
        }
        assertThrows(NoSuchElementException.class, iterator::next, "Calling next() should throw NoSuchElementException when no more elements");
        return results;
    }
}