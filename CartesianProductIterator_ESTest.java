package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class CartesianProductIteratorTest {

    @Test
    public void producesCartesianProductInExpectedOrder() {
        // Given two finite iterables
        Iterable<String> letters = Arrays.asList("A", "B");
        Iterable<String> numbers = Arrays.asList("1", "2");

        // When iterating the Cartesian product
        CartesianProductIterator<String> it = new CartesianProductIterator<>(letters, numbers);
        List<List<String>> allTuples = new ArrayList<>();
        while (it.hasNext()) {
            allTuples.add(it.next());
        }

        // Then the second iterable advances fastest (nested-loop order)
        List<List<String>> expected = Arrays.asList(
                Arrays.asList("A", "1"),
                Arrays.asList("A", "2"),
                Arrays.asList("B", "1"),
                Arrays.asList("B", "2")
        );
        assertEquals(expected, allTuples);
    }

    @Test
    public void hasNextIsFalseWhenAnyInputIterableIsEmpty() {
        Iterable<String> nonEmpty = Arrays.asList("X");
        Iterable<String> empty = Collections.emptyList();

        CartesianProductIterator<String> it = new CartesianProductIterator<>(nonEmpty, empty);

        assertFalse(it.hasNext());
    }

    @Test
    public void nextThrowsWhenNoIterablesProvided() {
        // No input iterables => empty Cartesian product
        CartesianProductIterator<String> it = new CartesianProductIterator<>();

        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void constructorThrowsForNullVarargsArray() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new CartesianProductIterator<>((Iterable<String>[]) null)
        );
        assertEquals("iterables", ex.getMessage());
    }

    @Test
    public void constructorThrowsForNullIterableElement() {
        Iterable<String> good = Arrays.asList("a");
        Iterable<String> bad = null;

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new CartesianProductIterator<>(good, bad)
        );
        assertEquals("iterable", ex.getMessage());
    }

    @Test
    public void surfacesConcurrentModificationFromUnderlyingIterable() {
        // Given a fail-fast iterable (ArrayList)
        List<Integer> data = new ArrayList<>(Arrays.asList(1));

        // And an iterator created from it
        CartesianProductIterator<Integer> it = new CartesianProductIterator<>(data);

        // When the underlying iterable is structurally modified after creation
        data.add(2);

        // Then calling next() surfaces the ConcurrentModificationException from the underlying iterator
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    public void removeIsUnsupported() {
        CartesianProductIterator<Integer> it = new CartesianProductIterator<>();

        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    public void nextThrowsAfterExhaustion() {
        CartesianProductIterator<Integer> it = new CartesianProductIterator<>(Arrays.asList(42));

        assertTrue(it.hasNext());
        assertEquals(Arrays.asList(42), it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }
}