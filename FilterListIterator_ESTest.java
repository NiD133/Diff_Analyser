package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Test;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for FilterListIterator.
 *
 * These tests aim to:
 * - Show how to use FilterListIterator with clear, small scenarios.
 * - Cover forward/backward filtering, index methods, resetting iterator/predicate.
 * - Verify UnsupportedOperationException for mutating operations.
 * - Verify NoSuchElementException and ConcurrentModificationException cases.
 */
public class FilterListIteratorTest {

    @Test
    public void forwardIteration_filtersOutNulls_andPreservesOrder() {
        LinkedList<Integer> data = new LinkedList<>(Arrays.asList(null, 1, null, 2, 3));
        Predicate<Integer> notNull = NotNullPredicate.notNullPredicate();

        FilterListIterator<Integer> it = new FilterListIterator<>(data.listIterator(), notNull);

        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(1), it.next());
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(2), it.next());
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(3), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void backwardIteration_filtersOutNulls_andPreservesReverseOrder() {
        LinkedList<Integer> data = new LinkedList<>(Arrays.asList(null, 1, null, 2, 3));
        Predicate<Integer> notNull = NotNullPredicate.notNullPredicate();

        // Start from the end to iterate backwards
        FilterListIterator<Integer> it = new FilterListIterator<>(data.listIterator(data.size()), notNull);

        assertTrue(it.hasPrevious());
        assertEquals(Integer.valueOf(3), it.previous());
        assertTrue(it.hasPrevious());
        assertEquals(Integer.valueOf(2), it.previous());
        assertTrue(it.hasPrevious());
        assertEquals(Integer.valueOf(1), it.previous());
        assertFalse(it.hasPrevious());
    }

    @Test
    public void indexMethods_reflectFilteredSequence() {
        LinkedList<Integer> data = new LinkedList<>(Arrays.asList(null, 10, 20));
        Predicate<Integer> notNull = NotNullPredicate.notNullPredicate();

        FilterListIterator<Integer> it = new FilterListIterator<>(data.listIterator(), notNull);

        // At start of filtered view
        assertEquals(0, it.nextIndex());
        assertEquals(-1, it.previousIndex());

        // After consuming first filtered element (10)
        assertEquals(Integer.valueOf(10), it.next());
        assertEquals(1, it.nextIndex());
        assertEquals(0, it.previousIndex());

        // After consuming second filtered element (20)
        assertEquals(Integer.valueOf(20), it.next());
        assertEquals(2, it.nextIndex());
        assertEquals(1, it.previousIndex());

        // No more matches
        assertFalse(it.hasNext());
    }

    @Test
    public void noSuchElement_whenNoMatchingElements() {
        LinkedList<Integer> data = new LinkedList<>(Arrays.asList(null, null));
        Predicate<Integer> notNull = NotNullPredicate.notNullPredicate();

        FilterListIterator<Integer> it = new FilterListIterator<>(data.listIterator(), notNull);

        assertFalse(it.hasNext());
        try {
            it.next();
            fail("Expected NoSuchElementException when no next element is available");
        } catch (NoSuchElementException expected) {
            // ok
        }

        assertFalse(it.hasPrevious());
        try {
            it.previous();
            fail("Expected NoSuchElementException when no previous element is available");
        } catch (NoSuchElementException expected) {
            // ok
        }
    }

    @Test
    public void resetWithNewPredicateAndIterator() {
        LinkedList<Integer> data = new LinkedList<>(Arrays.asList(1, 2, 3));

        // First: accept all
        FilterListIterator<Integer> it = new FilterListIterator<>(data.listIterator(), TruePredicate.truePredicate());
        assertEquals(Integer.valueOf(1), it.next());

        // Reset: only accept 3, and restart from beginning
        it.setPredicate(EqualPredicate.equalPredicate(3));
        it.setListIterator(data.listIterator());

        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(3), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void unsupportedOperations_add_remove_set_throw() {
        FilterListIterator<Integer> it = new FilterListIterator<>(TruePredicate.truePredicate());
        // No underlying iterator set, but operations are unsupported regardless.

        try {
            it.add(1);
            fail("Expected UnsupportedOperationException for add");
        } catch (UnsupportedOperationException expected) {
            // ok
        }

        try {
            it.remove();
            fail("Expected UnsupportedOperationException for remove");
        } catch (UnsupportedOperationException expected) {
            // ok
        }

        try {
            it.set(1);
            fail("Expected UnsupportedOperationException for set");
        } catch (UnsupportedOperationException expected) {
            // ok
        }
    }

    @Test
    public void getters_returnConfiguredObjects() {
        LinkedList<Integer> data = new LinkedList<>(Arrays.asList(1, 2));
        ListIterator<Integer> base = data.listIterator();
        Predicate<Integer> notNull = NotNullPredicate.notNullPredicate();

        FilterListIterator<Integer> it = new FilterListIterator<>(base, notNull);

        assertSame("getListIterator should return the underlying iterator", base, it.getListIterator());
        assertSame("getPredicate should return the configured predicate", notNull, it.getPredicate());
    }

    @Test
    public void hasNextFalse_and_hasPreviousFalse_whenEmpty() {
        LinkedList<Integer> empty = new LinkedList<>();
        FilterListIterator<Integer> it = new FilterListIterator<>(empty.listIterator(), TruePredicate.truePredicate());

        assertFalse(it.hasNext());
        assertFalse(it.hasPrevious());
        assertEquals(0, it.nextIndex());
        assertEquals(-1, it.previousIndex());
    }

    @Test
    public void concurrentModification_isDetectedFromUnderlyingListIterator() {
        LinkedList<Integer> data = new LinkedList<>();
        ListIterator<Integer> base = data.listIterator();

        FilterListIterator<Integer> it = new FilterListIterator<>(base, TruePredicate.truePredicate());

        // Structural modification after obtaining the list iterator should trip CME upon use
        data.add(42);
        try {
            it.hasNext();
            fail("Expected ConcurrentModificationException after modifying the underlying list");
        } catch (ConcurrentModificationException expected) {
            // ok
        }
    }
}