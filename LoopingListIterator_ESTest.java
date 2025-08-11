package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.apache.commons.collections4.iterators.LoopingListIterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the LoopingListIterator class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class LoopingListIteratorTest extends LoopingListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testSetNullElement() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(-844);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(list);
        
        iterator.previous();
        iterator.set(null);
        
        assertTrue(iterator.hasPrevious());
    }

    @Test(timeout = 4000)
    public void testSizeOfIterator() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(0);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(list);
        
        assertEquals(1, iterator.size());
    }

    @Test(timeout = 4000)
    public void testPreviousIndexAfterPush() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        Integer value = -3558;
        list.add(value);
        list.push(value);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(list);
        
        assertEquals(1, iterator.previousIndex());
    }

    @Test(timeout = 4000)
    public void testAddInstanceofPredicate() throws Throwable {
        LinkedList<InstanceofPredicate> list = new LinkedList<>();
        InstanceofPredicate predicate = new InstanceofPredicate(Integer.class);
        list.add(predicate);
        LoopingListIterator<InstanceofPredicate> iterator = new LoopingListIterator<>(list);
        
        iterator.add(predicate);
        
        assertEquals(1, iterator.nextIndex());
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationOnSet() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(-844);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(list);
        Integer value = iterator.previous();
        
        list.removeIf(new UniquePredicate<>());
        
        try {
            iterator.set(value);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationOnReset() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        List<Integer> sublist = list.subList(0, 0);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(sublist);
        
        list.add(0);
        
        try {
            iterator.reset();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.SubList", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationOnRemove() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        list.add(iterator);
        
        try {
            iterator.remove();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationOnPreviousIndex() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        List<Integer> sublist = list.subList(0, 0);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(sublist);
        
        list.add(0);
        
        try {
            iterator.previousIndex();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.SubList", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationOnPrevious() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        list.add(iterator);
        
        try {
            iterator.previous();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationOnNextIndex() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        List<Integer> sublist = list.subList(0, 0);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(sublist);
        
        list.add(0);
        
        try {
            iterator.nextIndex();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.SubList", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationOnNext() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        list.add(iterator);
        
        try {
            iterator.next();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationOnHasPrevious() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        List<Integer> sublist = list.subList(0, 0);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(sublist);
        
        list.add(0);
        
        try {
            iterator.hasPrevious();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.SubList", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationOnAdd() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        list.add(iterator);
        
        try {
            iterator.add(list);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationOnConstructor() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        List<Integer> sublist = list.subList(0, 0);
        
        list.add(0);
        
        try {
            new LoopingListIterator<>(sublist);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.SubList", e);
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerOnConstructor() throws Throwable {
        try {
            new LoopingListIterator<>(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testPreviousIndexAfterAdd() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        iterator.add(list);
        
        assertEquals(0, iterator.previousIndex());
    }

    @Test(timeout = 4000)
    public void testNoSuchElementOnPreviousIndex() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        try {
            iterator.previousIndex();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.collections4.iterators.LoopingListIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testPreviousAfterAdd() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        iterator.add(list);
        
        LinkedList<Object> previousList = (LinkedList<Object>) iterator.previous();
        
        assertEquals(1, previousList.size());
    }

    @Test(timeout = 4000)
    public void testNoSuchElementOnPrevious() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        try {
            iterator.previous();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.collections4.iterators.LoopingListIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testPreviousWithNullClosure() throws Throwable {
        LinkedList<Closure<Integer>> list = new LinkedList<>();
        list.add(null);
        LoopingListIterator<Closure<Integer>> iterator = new LoopingListIterator<>(list);
        
        assertNull(iterator.previous());
    }

    @Test(timeout = 4000)
    public void testNextIndexAfterAddInstanceofPredicate() throws Throwable {
        LinkedList<InstanceofPredicate> list = new LinkedList<>();
        LoopingListIterator<InstanceofPredicate> iterator = new LoopingListIterator<>(list);
        
        InstanceofPredicate predicate = new InstanceofPredicate(Object.class);
        iterator.add(predicate);
        
        assertEquals(0, iterator.nextIndex());
    }

    @Test(timeout = 4000)
    public void testNoSuchElementOnNextIndex() throws Throwable {
        LinkedList<LinkedList<Object>> list = new LinkedList<>();
        LoopingListIterator<LinkedList<Object>> iterator = new LoopingListIterator<>(list);
        
        try {
            iterator.nextIndex();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.collections4.iterators.LoopingListIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAfterAddAndNext() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        iterator.add(list);
        iterator.next();
        iterator.remove();
        
        assertFalse(iterator.hasPrevious());
    }

    @Test(timeout = 4000)
    public void testNoSuchElementOnNext() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(list);
        
        try {
            iterator.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.collections4.iterators.LoopingListIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextWithNullElement() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(null);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(list);
        
        assertNull(iterator.next());
    }

    @Test(timeout = 4000)
    public void testHasPreviousOnEmptyList() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(list);
        
        assertFalse(iterator.hasPrevious());
    }

    @Test(timeout = 4000)
    public void testHasPreviousWithNullElement() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(null);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(list);
        
        assertTrue(iterator.hasPrevious());
    }

    @Test(timeout = 4000)
    public void testHasNextOnEmptyList() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        assertFalse(iterator.hasNext());
    }

    @Test(timeout = 4000)
    public void testHasNextAfterAdd() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        assertFalse(iterator.hasNext());
        
        iterator.add(list);
        
        assertTrue(iterator.hasNext());
    }

    @Test(timeout = 4000)
    public void testResetOnEmptyList() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        iterator.reset();
        
        assertFalse(iterator.hasNext());
    }

    @Test(timeout = 4000)
    public void testIllegalStateOnSet() throws Throwable {
        LinkedList<Integer> list = new LinkedList<>();
        LinkedList<LinkedList<Integer>> listOfLists = new LinkedList<>();
        LoopingListIterator<LinkedList<Integer>> iterator = new LoopingListIterator<>(listOfLists);
        
        try {
            iterator.set(list);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }

    @Test(timeout = 4000)
    public void testSizeOnEmptyList() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        assertEquals(0, iterator.size());
    }

    @Test(timeout = 4000)
    public void testIllegalStateOnRemove() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        
        try {
            iterator.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }
}