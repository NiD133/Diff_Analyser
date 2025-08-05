package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the LoopingListIterator class.
 */
class LoopingListIteratorTest {

    private List<String> list;
    private LoopingListIterator<String> loop;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>(Arrays.asList("b", "e", "f"));
        loop = new LoopingListIterator<>(list);
    }

    /**
     * Tests the add method functionality.
     */
    @Test
    void testAdd() {
        loop.add("a"); // List becomes: a, b, e, f
        assertEquals("b", loop.next());
        loop.reset();
        assertEquals("a", loop.next());
        assertEquals("b", loop.next());

        loop.add("c"); // List becomes: a, b, c, e, f
        assertEquals("e", loop.next());
        assertEquals("e", loop.previous());
        assertEquals("c", loop.previous());
        assertEquals("c", loop.next());

        loop.add("d"); // List becomes: a, b, c, d, e, f
        loop.reset();
        assertEquals("a", loop.next());
        assertEquals("b", loop.next());
        assertEquals("c", loop.next());
        assertEquals("d", loop.next());
        assertEquals("e", loop.next());
        assertEquals("f", loop.next());
        assertEquals("a", loop.next());

        // Test adding elements when iterating backwards
        list = new ArrayList<>(Arrays.asList("b", "e", "f"));
        loop = new LoopingListIterator<>(list);

        loop.add("a"); // List becomes: a, b, e, f
        assertEquals("a", loop.previous());
        loop.reset();
        assertEquals("f", loop.previous());
        assertEquals("e", loop.previous());

        loop.add("d"); // List becomes: a, b, d, e, f
        assertEquals("d", loop.previous());

        loop.add("c"); // List becomes: a, b, c, d, e, f
        assertEquals("c", loop.previous());

        loop.reset();
        assertEquals("a", loop.next());
        assertEquals("b", loop.next());
        assertEquals("c", loop.next());
        assertEquals("d", loop.next());
        assertEquals("e", loop.next());
        assertEquals("f", loop.next());
        assertEquals("a", loop.next());
    }

    /**
     * Tests constructor exception for null input.
     */
    @Test
    void testConstructorEx() {
        assertThrows(NullPointerException.class, () -> new LoopingListIterator<>(null));
    }

    /**
     * Tests jogging back and forth between two elements without crossing boundaries.
     */
    @Test
    void testJoggingNotOverBoundary() {
        list = Arrays.asList("a", "b");
        loop = new LoopingListIterator<>(list);

        loop.reset();
        assertEquals("a", loop.next());
        assertEquals("a", loop.previous());
        assertEquals("a", loop.next());

        assertEquals("b", loop.next());
        assertEquals("b", loop.previous());
        assertEquals("b", loop.next());
    }

    /**
     * Tests jogging back and forth between two elements crossing boundaries.
     */
    @Test
    void testJoggingOverBoundary() {
        list = Arrays.asList("a", "b");
        loop = new LoopingListIterator<>(list);

        assertEquals("b", loop.previous());
        assertEquals("b", loop.next());
        assertEquals("b", loop.previous());

        assertEquals("a", loop.previous());
        assertEquals("a", loop.next());
        assertEquals("a", loop.previous());
    }

    /**
     * Tests behavior of an empty looping list iterator.
     */
    @Test
    void testLooping0() {
        list = new ArrayList<>();
        loop = new LoopingListIterator<>(list);

        assertFalse(loop.hasNext());
        assertFalse(loop.hasPrevious());
        assertThrows(NoSuchElementException.class, () -> loop.next());
        assertThrows(NoSuchElementException.class, () -> loop.previous());
    }

    /**
     * Tests behavior of a looping list iterator with a single element.
     */
    @Test
    void testLooping1() {
        list = Arrays.asList("a");
        loop = new LoopingListIterator<>(list);

        assertTrue(loop.hasNext());
        assertEquals("a", loop.next());

        assertTrue(loop.hasNext());
        assertEquals("a", loop.next());

        assertTrue(loop.hasPrevious());
        assertEquals("a", loop.previous());
    }

    /**
     * Tests behavior of a looping list iterator with two elements.
     */
    @Test
    void testLooping2() {
        list = Arrays.asList("a", "b");
        loop = new LoopingListIterator<>(list);

        assertTrue(loop.hasNext());
        assertEquals("a", loop.next());

        assertTrue(loop.hasNext());
        assertEquals("b", loop.next());

        assertTrue(loop.hasNext());
        assertEquals("a", loop.next());

        loop.reset();

        assertTrue(loop.hasPrevious());
        assertEquals("b", loop.previous());

        assertTrue(loop.hasPrevious());
        assertEquals("a", loop.previous());
    }

    /**
     * Tests nextIndex and previousIndex methods.
     */
    @Test
    void testNextAndPreviousIndex() {
        list = Arrays.asList("a", "b", "c");
        loop = new LoopingListIterator<>(list);

        assertEquals(0, loop.nextIndex());
        assertEquals(2, loop.previousIndex());

        assertEquals("a", loop.next());
        assertEquals(1, loop.nextIndex());
        assertEquals(0, loop.previousIndex());

        assertEquals("a", loop.previous());
        assertEquals(0, loop.nextIndex());
        assertEquals(2, loop.previousIndex());

        assertEquals("c", loop.previous());
        assertEquals(2, loop.nextIndex());
        assertEquals(1, loop.previousIndex());

        assertEquals("b", loop.previous());
        assertEquals(1, loop.nextIndex());
        assertEquals(0, loop.previousIndex());

        assertEquals("a", loop.previous());
        assertEquals(0, loop.nextIndex());
        assertEquals(2, loop.previousIndex());
    }

    /**
     * Tests removing elements while iterating backwards.
     */
    @Test
    void testRemovingElementsAndIteratingBackwards() {
        list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        loop = new LoopingListIterator<>(list);

        assertTrue(loop.hasPrevious());
        assertEquals("c", loop.previous());
        loop.remove();
        assertEquals(2, list.size());

        assertTrue(loop.hasPrevious());
        assertEquals("b", loop.previous());
        loop.remove();
        assertEquals(1, list.size());

        assertTrue(loop.hasPrevious());
        assertEquals("a", loop.previous());
        loop.remove();
        assertEquals(0, list.size());

        assertFalse(loop.hasPrevious());
        assertThrows(NoSuchElementException.class, () -> loop.previous());
    }

    /**
     * Tests removing elements while iterating forwards.
     */
    @Test
    void testRemovingElementsAndIteratingForward() {
        list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        loop = new LoopingListIterator<>(list);

        assertTrue(loop.hasNext());
        assertEquals("a", loop.next());
        loop.remove();
        assertEquals(2, list.size());

        assertTrue(loop.hasNext());
        assertEquals("b", loop.next());
        loop.remove();
        assertEquals(1, list.size());

        assertTrue(loop.hasNext());
        assertEquals("c", loop.next());
        loop.remove();
        assertEquals(0, list.size());

        assertFalse(loop.hasNext());
        assertThrows(NoSuchElementException.class, () -> loop.next());
    }

    /**
     * Tests the reset method functionality.
     */
    @Test
    void testReset() {
        list = Arrays.asList("a", "b", "c");
        loop = new LoopingListIterator<>(list);

        assertEquals("a", loop.next());
        assertEquals("b", loop.next());
        loop.reset();
        assertEquals("a", loop.next());
        loop.reset();
        assertEquals("a", loop.next());
        assertEquals("b", loop.next());
        assertEquals("c", loop.next());
        loop.reset();

        assertEquals("c", loop.previous());
        assertEquals("b", loop.previous());
        loop.reset();
        assertEquals("c", loop.previous());
        loop.reset();
        assertEquals("c", loop.previous());
        assertEquals("b", loop.previous());
        assertEquals("a", loop.previous());
    }

    /**
     * Tests the set method to change elements.
     */
    @Test
    void testSet() {
        list = Arrays.asList("q", "r", "z");
        loop = new LoopingListIterator<>(list);

        assertEquals("z", loop.previous());
        loop.set("c");

        loop.reset();
        assertEquals("q", loop.next());
        loop.set("a");

        assertEquals("r", loop.next());
        loop.set("b");

        loop.reset();
        assertEquals("a", loop.next());
        assertEquals("b", loop.next());
        assertEquals("c", loop.next());
    }
}