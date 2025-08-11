package org.apache.commons.collections4.iterators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NOPClosure;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class BoundedIteratorTest {

    @Test(timeout = 4000)
    public void testRemoveWithoutNextThrowsIllegalStateException() {
        Iterator<Predicate<Object>> mockIterator = createMockIterator(true, true, false);
        BoundedIterator<Predicate<Object>> boundedIterator = new BoundedIterator<>(mockIterator, 1031L, 1031L);
        boundedIterator.next();

        BoundedIterator<Predicate<Object>> boundedIteratorWithZeroMax = new BoundedIterator<>(boundedIterator, 1031L, 0L);

        try {
            boundedIteratorWithZeroMax.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.collections4.iterators.BoundedIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testHasNextReturnsTrueForNonEmptyIterator() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        Iterator<Integer> descendingIterator = list.descendingIterator();
        BoundedIterator<Integer> boundedIterator = new BoundedIterator<>(descendingIterator, 0L, 508L);

        assertTrue(boundedIterator.hasNext());
    }

    @Test(timeout = 4000, expected = NoSuchElementException.class)
    public void testNextOnEmptyIteratorThrowsNoSuchElementException() {
        LinkedList<Boolean> emptyList = new LinkedList<>();
        Iterator<Boolean> iterator = emptyList.iterator();
        BoundedIterator<Object> boundedIterator = new BoundedIterator<>(iterator, 1909L, 1909L);

        boundedIterator.next();
    }

    @Test(timeout = 4000, expected = ConcurrentModificationException.class)
    public void testConcurrentModificationThrowsException() {
        LinkedList<Object> list = new LinkedList<>();
        Iterator<Object> descendingIterator = list.descendingIterator();
        BoundedIterator<Object> boundedIterator = new BoundedIterator<>(descendingIterator, 5L, 5L);
        list.add(descendingIterator);

        boundedIterator.next();
    }

    @Test(timeout = 4000, expected = ConcurrentModificationException.class)
    public void testConcurrentModificationDuringInitializationThrowsException() {
        LinkedList<Object> list = new LinkedList<>();
        Iterator<Object> iterator = list.iterator();
        list.add(iterator);

        new BoundedIterator<>(iterator, 1973L, 287L);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testNullIteratorThrowsNullPointerException() {
        new BoundedIterator<Boolean>(null, 0L, 0L);
    }

    @Test(timeout = 4000)
    public void testRemoveAfterNext() {
        Closure<Integer> closure = NOPClosure.nopClosure();
        Iterator<Closure<Integer>> mockIterator = createMockIterator(closure);
        BoundedIterator<Closure<Integer>> boundedIterator = new BoundedIterator<>(mockIterator, 0L, 909L);

        boundedIterator.next();
        boundedIterator.remove();
    }

    @Test(timeout = 4000)
    public void testBoundedIteratorEquality() {
        Iterator<Closure<Integer>> mockIterator = createMockIterator(true, false);
        BoundedIterator<Closure<Integer>> boundedIterator = new BoundedIterator<>(mockIterator, 1L, 5275L);
        BoundedIterator<Closure<Integer>> boundedIteratorWithZeroMax = new BoundedIterator<>(boundedIterator, 5275L, 0L);

        assertFalse(boundedIteratorWithZeroMax.equals(boundedIterator));
    }

    @Test(timeout = 4000)
    public void testHasNextReturnsFalseForEmptyIterator() {
        LinkedList<Object> emptyList = new LinkedList<>();
        Iterator<Object> iterator = emptyList.iterator();
        BoundedIterator<Object> boundedIterator = new BoundedIterator<>(iterator, 0L, 0L);

        assertFalse(boundedIterator.hasNext());
    }

    @Test(timeout = 4000, expected = NoSuchElementException.class)
    public void testNextOnEmptyBoundedIteratorThrowsNoSuchElementException() {
        LinkedList<Integer> emptyList = new LinkedList<>();
        Iterator<Integer> descendingIterator = emptyList.descendingIterator();
        BoundedIterator<Integer> boundedIterator = new BoundedIterator<>(descendingIterator, 0L, 0L);

        boundedIterator.next();
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testNegativeMaxThrowsIllegalArgumentException() {
        new BoundedIterator<Boolean>(null, 0L, -1022L);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testNegativeOffsetThrowsIllegalArgumentException() {
        new BoundedIterator<Boolean>(null, -1007L, -1007L);
    }

    // Helper method to create a mock iterator with specified hasNext and next behavior
    private <T> Iterator<T> createMockIterator(T... nextValues) {
        Iterator<T> mockIterator = (Iterator<T>) mock(Iterator.class, new ViolatedAssumptionAnswer());
        doReturn(true, false).when(mockIterator).hasNext();
        doReturn(nextValues).when(mockIterator).next();
        return mockIterator;
    }
}