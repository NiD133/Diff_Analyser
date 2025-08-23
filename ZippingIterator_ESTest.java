package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ZippingIterator_ESTest extends ZippingIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testZippingIteratorEquality() throws Throwable {
        // Test to verify that two ZippingIterators with the same iterators are not equal
        LinkedList<Integer> list = new LinkedList<>();
        Iterator<Integer> descendingIterator = list.descendingIterator();
        ZippingIterator<Integer> zippingIterator1 = new ZippingIterator<>(descendingIterator, descendingIterator);
        Iterator<Integer>[] iteratorArray = (Iterator<Integer>[]) Array.newInstance(Iterator.class, 1);
        iteratorArray[0] = zippingIterator1;
        ZippingIterator<Integer> zippingIterator2 = new ZippingIterator<>(iteratorArray);
        
        assertFalse(zippingIterator2.equals(zippingIterator1));
    }

    @Test(timeout = 4000)
    public void testNextWithNullElement() throws Throwable {
        // Test to verify that next() returns null when the list contains a null element
        LinkedList<Integer> list = new LinkedList<>();
        list.add(null);
        Iterator<Integer> descendingIterator = list.descendingIterator();
        ZippingIterator<Object> zippingIterator = new ZippingIterator<>(descendingIterator, descendingIterator);
        
        Object result = zippingIterator.next();
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testNextWithSingleElement() throws Throwable {
        // Test to verify that next() returns the correct element when the list contains one element
        LinkedList<Object> list = new LinkedList<>();
        Object element = new Object();
        list.push(element);
        Iterator<Object> descendingIterator = list.descendingIterator();
        ZippingIterator<Object> zippingIterator = new ZippingIterator<>(descendingIterator, descendingIterator);
        
        Object result = zippingIterator.next();
        assertSame(result, element);
    }

    @Test(timeout = 4000)
    public void testRemoveAfterModification() throws Throwable {
        // Test to verify that remove() throws ConcurrentModificationException after list modification
        LinkedList<Integer> list = new LinkedList<>();
        Integer element = -550;
        list.add(element);
        Iterator<Integer> iterator = list.iterator();
        ZippingIterator<Object> zippingIterator = new ZippingIterator<>(iterator, iterator, iterator);
        
        Object result = zippingIterator.next();
        list.removeLastOccurrence(result);
        
        try {
            zippingIterator.remove();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextAfterModification() throws Throwable {
        // Test to verify that next() throws ConcurrentModificationException after list modification
        LinkedList<InstanceofPredicate> list = new LinkedList<>();
        ListIterator<InstanceofPredicate> listIterator = list.listIterator();
        list.add(null);
        ZippingIterator<InstanceofPredicate> zippingIterator = new ZippingIterator<>(listIterator, listIterator, listIterator);
        
        try {
            zippingIterator.next();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullIteratorArray() throws Throwable {
        // Test to verify that constructor throws NullPointerException when passed a null iterator array
        Iterator<Integer>[] iteratorArray = (Iterator<Integer>[]) Array.newInstance(Iterator.class, 1);
        
        try {
            new ZippingIterator<>(iteratorArray);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullIterators() throws Throwable {
        // Test to verify that constructor throws NullPointerException when passed null iterators
        try {
            new ZippingIterator<>(null, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithTwoNullIterators() throws Throwable {
        // Test to verify that constructor throws NullPointerException when passed two null iterators
        try {
            new ZippingIterator<>(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testHasNextWithNonEmptyList() throws Throwable {
        // Test to verify that hasNext() returns true when there are elements in the list
        LinkedList<InstanceofPredicate> list = new LinkedList<>();
        ListIterator<InstanceofPredicate> listIterator = list.listIterator();
        InstanceofPredicate predicate = new InstanceofPredicate(Integer.class);
        list.add(predicate);
        ZippingIterator<InstanceofPredicate> zippingIterator = new ZippingIterator<>(listIterator, listIterator);
        
        assertTrue(zippingIterator.hasNext());
    }

    @Test(timeout = 4000)
    public void testHasNextWithEmptyList() throws Throwable {
        // Test to verify that hasNext() returns false when the list is empty
        LinkedList<Integer> list = new LinkedList<>();
        Iterator<Integer> descendingIterator = list.descendingIterator();
        ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(descendingIterator, descendingIterator, descendingIterator);
        
        assertFalse(zippingIterator.hasNext());
    }

    @Test(timeout = 4000)
    public void testRemoveWithoutNext() throws Throwable {
        // Test to verify that remove() throws IllegalStateException when called without next()
        LinkedList<Integer> list = new LinkedList<>();
        Iterator<Integer> iterator = list.iterator();
        ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(iterator, iterator, iterator);
        ZippingIterator<Object> zippingIteratorWithObject = new ZippingIterator<>(zippingIterator, iterator, iterator);
        
        try {
            zippingIteratorWithObject.remove();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.apache.commons.collections4.iterators.ZippingIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAfterNext() throws Throwable {
        // Test to verify that remove() works correctly after next() is called
        LinkedList<Integer> list = new LinkedList<>();
        Integer element = -550;
        list.add(element);
        Iterator<Integer> iterator = list.iterator();
        ZippingIterator<Object> zippingIterator = new ZippingIterator<>(iterator, iterator, iterator);
        
        zippingIterator.next();
        zippingIterator.remove();
    }

    @Test(timeout = 4000)
    public void testNextWithEmptyList() throws Throwable {
        // Test to verify that next() throws NoSuchElementException when the list is empty
        LinkedList<Integer> list = new LinkedList<>();
        Iterator<Integer> iterator = list.iterator();
        ZippingIterator<Object> zippingIterator = new ZippingIterator<>(iterator, iterator, iterator);
        
        try {
            zippingIterator.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.collections4.iterators.ZippingIterator", e);
        }
    }
}