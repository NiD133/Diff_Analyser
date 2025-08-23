package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Transformer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ObjectGraphIteratorTest {

    // A simple transformer that "flattens" nested Iterables/Iterators.
    // If the input is:
    // - Iterable: returns its iterator
    // - Iterator: returns it directly
    // - Anything else: returns the input element (a "leaf" in the graph)
    private static final Transformer<Object, Object> FLATTENING_TRANSFORMER = input -> {
        if (input instanceof Iterable) {
            return ((Iterable<?>) input).iterator();
        }
        if (input instanceof Iterator) {
            return input;
        }
        return input;
    };

    @Test
    public void nextReturnsRootWhenTransformerIsNull() {
        // Given a simple root object and no transformer
        ObjectGraphIterator<Integer> it = new ObjectGraphIterator<>(42, null);

        // When/Then
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(42), it.next());
        assertFalse(it.hasNext());

        // Next after end should fail
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void emptyRootIteratorHasNoElements() {
        // Given an empty iterator as root
        ObjectGraphIterator<Object> it = new ObjectGraphIterator<>(Collections.emptyList().iterator());

        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void removeBeforeNextThrowsIllegalState() {
        // Given an empty iterator as root
        ObjectGraphIterator<Object> it = new ObjectGraphIterator<>(Collections.emptyList().iterator());

        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    public void removeAfterNextRemovesFromUnderlyingCollection() {
        // Given a list as the object graph root with a flattening transformer
        ArrayList<Object> list = new ArrayList<>(Arrays.asList(10, 20, 30));
        ObjectGraphIterator<Object> it = new ObjectGraphIterator<>(list, FLATTENING_TRANSFORMER);

        // When we advance once and remove
        assertTrue(it.hasNext());
        assertEquals(10, it.next());
        it.remove(); // should remove 10 from the underlying list

        // Then the list reflects the removal
        assertEquals(Arrays.asList(20, 30), list);
    }

    @Test
    public void flattensNestedIterables() {
        // Given nested content: [ [1, 2], 3, [], 4 ]
        ArrayList<Object> nested = new ArrayList<>();
        nested.add(Arrays.asList(1, 2));
        nested.add(3);
        nested.add(Collections.emptyList());
        nested.add(4);

        ObjectGraphIterator<Object> it = new ObjectGraphIterator<>(nested, FLATTENING_TRANSFORMER);

        // Collect flattened results
        ArrayList<Integer> results = new ArrayList<>();
        while (it.hasNext()) {
            Object next = it.next();
            // With the transformer above, leaves are the non-iterable elements (the integers here)
            assertTrue(next instanceof Integer);
            results.add((Integer) next);
        }

        assertEquals(Arrays.asList(1, 2, 3, 4), results);
    }

    @Test
    public void findNextByIteratorNullThrowsNullPointerException() {
        // Accessing protected method is possible since we are in the same package
        ObjectGraphIterator<Object> it = new ObjectGraphIterator<>((Iterator<?>) null);

        assertThrows(NullPointerException.class, () -> it.findNextByIterator(null));
    }

    @Test
    public void nextOnNullRootIteratorThrowsNoSuchElement() {
        ObjectGraphIterator<Object> it = new ObjectGraphIterator<>((Iterator<?>) null);

        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void exceptionFromTransformerBubblesUpOnNext() {
        // Transformer that always throws
        Transformer<Object, Object> throwingTransformer = input -> { throw new RuntimeException("boom"); };

        ObjectGraphIterator<Object> it = new ObjectGraphIterator<>(new Object(), throwingTransformer);

        assertThrows(RuntimeException.class, it::next);
    }
}