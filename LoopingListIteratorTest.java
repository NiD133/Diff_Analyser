package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

/**
 * Tests for LoopingListIterator focusing on clarity, intent-revealing names,
 * and small, behavior-focused scenarios. Helper methods reduce repetition and
 * make the traversal intent explicit.
 */
class LoopingListIteratorTest {

    // -------------------- Helpers --------------------

    private static <T> List<T> mutableListOf(final T... items) {
        return new ArrayList<>(Arrays.asList(items));
    }

    private static <T> void assertCycleForward(final LoopingListIterator<T> it, final List<T> expected) {
        it.reset();
        for (final T e : expected) {
            assertEquals(e, it.next(), "next() should traverse in order while looping");
        }
    }

    private static <T> void assertCycleBackward(final LoopingListIterator<T> it, final List<T> expected) {
        it.reset();
        for (int i = expected.size() - 1; i >= 0; i--) {
            assertEquals(expected.get(i), it.previous(), "previous() should traverse in reverse order while looping");
        }
    }

    // -------------------- Constructor --------------------

    @Test
    void testConstructorNullListThrows() {
        assertThrows(NullPointerException.class, () -> new LoopingListIterator<>(null));
    }

    // -------------------- Empty list --------------------

    @Test
    void testEmptyList_hasNoElementsAndThrowsOnTraversal() {
        final List<Object> list = new ArrayList<>();
        final LoopingListIterator<Object> loop = new LoopingListIterator<>(list);

        assertFalse(loop.hasNext());
        assertFalse(loop.hasPrevious());
        assertThrows(NoSuchElementException.class, loop::next);
        assertThrows(NoSuchElementException.class, loop::previous);
    }

    // -------------------- Single element list --------------------

    @Test
    void testSingleElement_loopsForeverBothDirections() {
        final List<String> list = Arrays.asList("a");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a>

        for (int i = 0; i < 3; i++) {
            assertTrue(loop.hasNext());
            assertEquals("a", loop.next());
        }
        for (int i = 0; i < 3; i++) {
            assertTrue(loop.hasPrevious());
            assertEquals("a", loop.previous());
        }
    }

    // -------------------- Two element list --------------------

    @Test
    void testTwoElements_nextLoopsAndPreviousLoopsWithReset() {
        final List<String> list = Arrays.asList("a", "b");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a> b

        assertTrue(loop.hasNext());
        assertEquals("a", loop.next()); // a <b>
        assertTrue(loop.hasNext());
        assertEquals("b", loop.next()); // <a> b
        assertTrue(loop.hasNext());
        assertEquals("a", loop.next()); // a <b>

        loop.reset();                   // <a> b

        assertTrue(loop.hasPrevious());
        assertEquals("b", loop.previous()); // a <b>
        assertTrue(loop.hasPrevious());
        assertEquals("a", loop.previous()); // <a> b
        assertTrue(loop.hasPrevious());
        assertEquals("b", loop.previous()); // a <b>
    }

    // -------------------- Jogging between boundaries --------------------

    @Test
    void testJoggingNotOverBoundary() {
        final List<String> list = Arrays.asList("a", "b");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a> b

        loop.reset();
        assertEquals("a", loop.next());     // a <b>
        assertEquals("a", loop.previous()); // <a> b
        assertEquals("a", loop.next());     // a <b>

        assertEquals("b", loop.next());     // <a> b
        assertEquals("b", loop.previous()); // a <b>
        assertEquals("b", loop.next());     // <a> b
    }

    @Test
    void testJoggingOverBoundary() {
        final List<String> list = Arrays.asList("a", "b");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a> b

        // Move across the end/beginning boundary in both directions.
        assertEquals("b", loop.previous()); // a <b>
        assertEquals("b", loop.next());     // <a> b
        assertEquals("b", loop.previous()); // a <b>

        assertEquals("a", loop.previous()); // <a> b
        assertEquals("a", loop.next());     // a <b>
        assertEquals("a", loop.previous()); // <a> b
    }

    // -------------------- Indices --------------------

    @Test
    void testNextAndPreviousIndex() {
        final List<String> list = Arrays.asList("a", "b", "c");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a> b c

        assertEquals(0, loop.nextIndex());
        assertEquals(2, loop.previousIndex());

        assertEquals("a", loop.next());        // a <b> c
        assertEquals(1, loop.nextIndex());
        assertEquals(0, loop.previousIndex());

        assertEquals("a", loop.previous());    // <a> b c
        assertEquals(0, loop.nextIndex());
        assertEquals(2, loop.previousIndex());

        assertEquals("c", loop.previous());    // a b <c>
        assertEquals(2, loop.nextIndex());
        assertEquals(1, loop.previousIndex());

        assertEquals("b", loop.previous());    // a <b> c
        assertEquals(1, loop.nextIndex());
        assertEquals(0, loop.previousIndex());

        assertEquals("a", loop.previous());    // <a> b c
        assertEquals(0, loop.nextIndex());
        assertEquals(2, loop.previousIndex());
    }

    // -------------------- Remove --------------------

    @Test
    void testRemoveWhileIteratingForward() {
        final List<String> list = mutableListOf("a", "b", "c");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a> b c

        assertTrue(loop.hasNext());
        assertEquals("a", loop.next()); // a <b> c
        loop.remove();                  // <b> c
        assertEquals(2, list.size());

        assertTrue(loop.hasNext());
        assertEquals("b", loop.next()); // b <c>
        loop.remove();                  // <c>
        assertEquals(1, list.size());

        assertTrue(loop.hasNext());
        assertEquals("c", loop.next()); // <c>
        loop.remove();                  // ---
        assertEquals(0, list.size());

        assertFalse(loop.hasNext());
        assertThrows(NoSuchElementException.class, loop::next);
    }

    @Test
    void testRemoveWhileIteratingBackward() {
        final List<String> list = mutableListOf("a", "b", "c");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <a> b c

        assertTrue(loop.hasPrevious());
        assertEquals("c", loop.previous()); // a b <c>
        loop.remove();                      // <a> b
        assertEquals(2, list.size());

        assertTrue(loop.hasPrevious());
        assertEquals("b", loop.previous()); // a <b>
        loop.remove();                      // <a>
        assertEquals(1, list.size());

        assertTrue(loop.hasPrevious());
        assertEquals("a", loop.previous()); // <a>
        loop.remove();                      // ---
        assertEquals(0, list.size());

        assertFalse(loop.hasPrevious());
        assertThrows(NoSuchElementException.class, loop::previous);
    }

    // -------------------- Reset --------------------

    @Test
    void testResetPositionsAtStartForBothDirections() {
        final List<String> data = Arrays.asList("a", "b", "c");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(data); // <a> b c

        assertEquals("a", loop.next()); // a <b> c
        assertEquals("b", loop.next()); // a b <c>

        loop.reset(); // <a> b c
        assertCycleForward(loop, data); // a, b, c

        loop.reset(); // <a> b c
        assertEquals("c", loop.previous()); // a b <c>
        assertEquals("b", loop.previous()); // a <b> c

        loop.reset(); // <a> b c
        assertCycleBackward(loop, data); // c, b, a
    }

    // -------------------- Set --------------------

    @Test
    void testSetReplacesLastReturnedElement() {
        final List<String> list = Arrays.asList("q", "r", "z");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <q> r z

        // Replace last returned via previous (z -> c)
        assertEquals("z", loop.previous()); // q r <z>
        loop.set("c");                      // q r <c>

        // Replace last returned via next (q -> a)
        loop.reset();                       // <q> r c
        assertEquals("q", loop.next());     // q <r> c
        loop.set("a");                      // a <r> c

        // Replace last returned via next (r -> b)
        assertEquals("r", loop.next());     // a r <c>
        loop.set("b");                      // a b <c>

        // Verify final order
        loop.reset();
        assertEquals("a", loop.next());     // a <b> c
        assertEquals("b", loop.next());     // a b <c>
        assertEquals("c", loop.next());     // <a> b c
    }

    // -------------------- Add --------------------

    @Test
    void testAdd_previousReturnsNewlyInsertedElement_andOrderIsCorrect() {
        final List<String> list = mutableListOf("b", "e", "f");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <b> e f

        loop.add("a");                                // a inserted before next() (b)
        assertEquals("a", loop.previous(), "previous() should return the element just added");

        loop.reset();
        assertCycleForward(loop, Arrays.asList("a", "b", "e", "f"));
        assertEquals(Arrays.asList("a", "b", "e", "f"), list);
    }

    @Test
    void testAdd_multipleInsertionsBuildExpectedOrder() {
        final List<String> list = mutableListOf("b", "e", "f");
        final LoopingListIterator<String> loop = new LoopingListIterator<>(list); // <b> e f

        // Insert at head
        loop.add("a");                            // [a, b, e, f]
        assertEquals("a", loop.previous());       // verify add semantics

        // Move after b and insert c before e
        loop.reset();                             // <a> b e f
        assertEquals("a", loop.next());           // a <b> e f
        assertEquals("b", loop.next());           // a b <e> f
        loop.add("c");                            // a b c <e> f

        // Move to e again and insert d before e
        assertEquals("e", loop.next());           // a b c e <f>
        assertEquals("e", loop.previous());       // a b c <e> f
        loop.add("d");                            // a b c d <e> f

        // Verify final order
        assertEquals(Arrays.asList("a", "b", "c", "d", "e", "f"), list);

        // And that the iterator loops in that order
        assertCycleForward(loop, list);
        assertCycleBackward(loop, list);
    }
}