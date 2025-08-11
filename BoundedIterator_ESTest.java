package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link BoundedIterator}.
 *
 * These tests focus on clarity and cover the key behaviors:
 * - parameter validation (null, negative offset/max)
 * - honoring offset/max bounds
 * - interaction with empty sources
 * - remove() contract before/after next()
 * - fail-fast behavior of the underlying iterator
 */
public class BoundedIteratorTest {

    // --- Parameter validation ------------------------------------------------

    @Test
    public void constructor_withNullIterator_throwsNPE() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new BoundedIterator<Integer>(null, 0L, 0L)
        );
        assertEquals("iterator", ex.getMessage());
    }

    @Test
    public void constructor_withNegativeOffset_throwsIAE() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new BoundedIterator<Integer>(new ArrayList<Integer>().iterator(), -1L, 0L)
        );
        assertEquals("Offset parameter must not be negative.", ex.getMessage());
    }

    @Test
    public void constructor_withNegativeMax_throwsIAE() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new BoundedIterator<Integer>(new ArrayList<Integer>().iterator(), 0L, -1L)
        );
        assertEquals("Max parameter must not be negative.", ex.getMessage());
    }

    // --- Basic bounds behavior ----------------------------------------------

    @Test
    public void hasNext_respectsMaxZero() {
        List<Integer> data = Arrays.asList(1, 2, 3);
        BoundedIterator<Integer> it = new BoundedIterator<>(data.iterator(), 0L, 0L);

        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void hasNext_trueWhenWithinBounds() {
        List<Integer> data = Arrays.asList(42);
        BoundedIterator<Integer> it = new BoundedIterator<>(data.iterator(), 0L, 10L);

        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(42), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void offset_skipsElements_andStopsAtMax() {
        List<Integer> data = Arrays.asList(10, 20, 30, 40, 50);
        BoundedIterator<Integer> it = new BoundedIterator<>(data.iterator(), 1L, 2L);

        // Should return [20, 30]
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(20), it.next());
        assertTrue(it.hasNext());
        assertEquals(Integer.valueOf(30), it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void next_onEmptySourceThrowsNoSuchElementException() {
        List<Integer> empty = new ArrayList<>();
        BoundedIterator<Integer> it = new BoundedIterator<>(empty.iterator(), 0L, 5L);

        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    // --- remove() contract ---------------------------------------------------

    @Test
    public void remove_beforeNext_throwsIllegalStateException() {
        List<Integer> data = Arrays.asList(1, 2);
        BoundedIterator<Integer> it = new BoundedIterator<>(data.iterator(), 0L, 2L);

        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    public void remove_afterNext_deletesLastReturnedElement() {
        List<Integer> data = new ArrayList<>(Arrays.asList(1, 2, 3));
        BoundedIterator<Integer> it = new BoundedIterator<>(data.iterator(), 0L, 3L);

        assertEquals(Integer.valueOf(1), it.next());
        it.remove();

        assertEquals(Arrays.asList(2, 3), data);
    }

    @Test
    public void remove_stillIllegalIfOnlyOffsetWasApplied() {
        // init() advances the underlying iterator to honor offset
        // but remove() must still be illegal until next() is called on the wrapper
        List<Integer> data = new ArrayList<>(Arrays.asList(1, 2, 3));
        BoundedIterator<Integer> it = new BoundedIterator<>(data.iterator(), 2L, 1L);

        assertThrows(IllegalStateException.class, it::remove);
    }

    // --- Fail-fast behavior --------------------------------------------------

    @Test
    public void concurrentModification_detectedWhenSourceModifiedBeforeConstructionAndOffsetRequiresAdvance() {
        List<Integer> data = new LinkedList<>(Arrays.asList(1, 2, 3));
        Iterator<Integer> src = data.iterator();
        // Structural modification after creating the iterator
        data.add(99);

        // Constructor will advance the underlying iterator because offset > 0
        assertThrows(ConcurrentModificationException.class,
                () -> new BoundedIterator<>(src, 1L, 2L));
    }

    @Test
    public void concurrentModification_detectedWhenSourceModifiedAfterWrapperCreation() {
        List<Integer> data = new LinkedList<>(Arrays.asList(1, 2, 3));
        BoundedIterator<Integer> it = new BoundedIterator<>(data.iterator(), 0L, 3L);

        // Structural modification after creating the wrapper but before iteration
        data.add(99);

        assertThrows(ConcurrentModificationException.class, it::next);
    }
}