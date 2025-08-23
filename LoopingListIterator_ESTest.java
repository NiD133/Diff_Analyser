package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for LoopingListIterator.
 *
 * These tests avoid EvoSuite scaffolding and aim to document the iterator’s
 * contract clearly:
 * - Looping behavior for next/previous
 * - Index semantics and wrap-around
 * - Mutating operations (add, set, remove, reset) and their preconditions
 * - Handling of empty lists and null elements
 * - Basic fail-fast behavior on concurrent modification
 */
public class LoopingListIteratorTest {

    // ---------------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------------

    @Test
    public void constructor_nullList_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new LoopingListIterator<>(null));
    }

    // ---------------------------------------------------------------------
    // Empty list behavior
    // ---------------------------------------------------------------------

    @Test
    public void emptyList_hasNoNextOrPreviousAndThrowsOnAccess() {
        LoopingListIterator<Integer> it = new LoopingListIterator<>(new LinkedList<>());

        assertFalse(it.hasNext());
        assertFalse(it.hasPrevious());

        assertThrows(NoSuchElementException.class, it::next);
        assertThrows(NoSuchElementException.class, it::previous);
        assertThrows(NoSuchElementException.class, it::nextIndex);
        assertThrows(NoSuchElementException.class, it::previousIndex);
    }

    // ---------------------------------------------------------------------
    // Size
    // ---------------------------------------------------------------------

    @Test
    public void size_reflectsCurrentListSize() {
        LoopingListIterator<Integer> it = new LoopingListIterator<>(new LinkedList<>(Arrays.asList(42)));
        assertEquals(1, it.size());
    }

    // ---------------------------------------------------------------------
    // Basic looping behavior
    // ---------------------------------------------------------------------

    @Test
    public void loopsForwardAndBackward() {
        List<String> data = new LinkedList<>(Arrays.asList("A", "B", "C"));
        LoopingListIterator<String> it = new LoopingListIterator<>(data);

        // forward looping
        assertEquals("A", it.next());
        assertEquals("B", it.next());
        assertEquals("C", it.next());
        // loops to beginning
        assertEquals("A", it.next());

        // backward looping
        assertEquals("C", it.previous());
        assertEquals("B", it.previous());
    }

    // ---------------------------------------------------------------------
    // Index semantics and wrap-around
    // ---------------------------------------------------------------------

    @Test
    public void nextIndex_wrapsToZeroAtPhysicalEnd() {
        LoopingListIterator<Integer> it = new LoopingListIterator<>(new LinkedList<>(Arrays.asList(1, 2)));

        // Move iterator to physical end (after 2 next() calls on size=2)
        it.next(); // -> 1
        it.next(); // -> 2

        // At physical end, nextIndex() should be 0
        assertEquals(0, it.nextIndex());
    }

    @Test
    public void previousIndex_wrapsToSizeMinusOneAtPhysicalBeginning() {
        LoopingListIterator<Integer> it = new LoopingListIterator<>(new LinkedList<>(Arrays.asList(10, 20, 30)));

        // At physical beginning (fresh iterator), previousIndex() is size - 1
        assertEquals(2, it.previousIndex());
    }

    // ---------------------------------------------------------------------
    // Add
    // ---------------------------------------------------------------------

    @Test
    public void add_onEmptyEnablesIterationAndReturnsInsertedElement() {
        LoopingListIterator<String> it = new LoopingListIterator<>(new LinkedList<>());

        assertFalse(it.hasNext());
        assertFalse(it.hasPrevious());

        it.add("X");

        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals("X", it.next());
        assertEquals(1, it.size());
    }

    // ---------------------------------------------------------------------
    // Remove
    // ---------------------------------------------------------------------

    @Test
    public void remove_afterNextRemovesLastReturnedAndSecondRemoveWithoutMoveFails() {
        LinkedList<Integer> list = new LinkedList<>(Arrays.asList(1, 2));
        LoopingListIterator<Integer> it = new LoopingListIterator<>(list);

        assertEquals(Integer.valueOf(1), it.next());
        it.remove(); // removes 1
        assertEquals(Arrays.asList(2), list);
        assertEquals(1, it.size());

        // Cannot remove again without an intervening next/previous call
        assertThrows(IllegalStateException.class, it::remove);

        // After moving again, remove is allowed
        assertEquals(Integer.valueOf(2), it.next());
        it.remove(); // removes 2
        assertTrue(list.isEmpty());
        assertFalse(it.hasNext());
        assertFalse(it.hasPrevious());
    }

    @Test
    public void remove_withoutPriorNextOrPrevious_throwsIllegalState() {
        LoopingListIterator<Integer> it = new LoopingListIterator<>(new LinkedList<>(Arrays.asList(1)));
        assertThrows(IllegalStateException.class, it::remove);
    }

    // ---------------------------------------------------------------------
    // Set
    // ---------------------------------------------------------------------

    @Test
    public void set_replacesLastReturnedElement() {
        LinkedList<Integer> list = new LinkedList<>(Arrays.asList(10, 20));
        LoopingListIterator<Integer> it = new LoopingListIterator<>(list);

        assertEquals(Integer.valueOf(10), it.next());
        it.set(99);

        assertEquals(Arrays.asList(99, 20), list);
    }

    @Test
    public void set_withoutPriorNextOrPrevious_throwsIllegalState() {
        LoopingListIterator<Integer> it = new LoopingListIterator<>(new LinkedList<>(Arrays.asList(1)));
        assertThrows(IllegalStateException.class, () -> it.set(2));
    }

    // ---------------------------------------------------------------------
    // Reset
    // ---------------------------------------------------------------------

    @Test
    public void reset_movesBackToStart() {
        LoopingListIterator<Integer> it = new LoopingListIterator<>(new LinkedList<>(Arrays.asList(1, 2, 3)));

        // Move somewhere in the list
        assertEquals(Integer.valueOf(1), it.next());
        assertEquals(Integer.valueOf(2), it.next());

        // Reset and verify we start from the first element again
        it.reset();
        assertEquals(Integer.valueOf(1), it.next());
    }

    @Test
    public void reset_disablesRemoveUntilNextOrPreviousIsCalledAgain() {
        LoopingListIterator<Integer> it = new LoopingListIterator<>(new LinkedList<>(Arrays.asList(1, 2)));

        assertEquals(Integer.valueOf(1), it.next());
        it.reset();

        // After reset, remove is not allowed until next/previous is called again
        assertThrows(IllegalStateException.class, it::remove);

        // After moving, remove is allowed again
        it.next();
        it.remove();
        assertEquals(1, it.size());
    }

    // ---------------------------------------------------------------------
    // Null elements
    // ---------------------------------------------------------------------

    @Test
    public void supportsNullElements() {
        LoopingListIterator<Integer> it = new LoopingListIterator<>(new LinkedList<>(Arrays.asList((Integer) null)));
        assertTrue(it.hasNext());
        assertNull(it.next());
        assertTrue(it.hasPrevious());
    }

    // ---------------------------------------------------------------------
    // Concurrent modification (fail-fast behavior from underlying list)
    // ---------------------------------------------------------------------

    @Test
    public void concurrentModification_onUnderlyingList_throwsCME() {
        LinkedList<Integer> list = new LinkedList<>(Arrays.asList(1, 2));
        LoopingListIterator<Integer> it = new LoopingListIterator<>(list);

        // External structural modification
        list.add(3);

        // Any iterator operation that checks modCount should now fail fast
        assertThrows(ConcurrentModificationException.class, it::next);
    }
}