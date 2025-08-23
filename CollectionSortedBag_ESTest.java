package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.PriorityQueue;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.bag.CollectionSortedBag;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.functors.IfClosure;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CollectionSortedBag_ESTest extends CollectionSortedBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testRemoveExistingElement() throws Throwable {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator).compare(any(), any());
        TreeBag<Object> treeBag = new TreeBag<>(comparator);
        IfClosure<Object> ifClosure = new IfClosure<>(null, null);
        treeBag.add(ifClosure);
        CollectionSortedBag<Object> collectionSortedBag = new CollectionSortedBag<>(treeBag);
        Integer integer = 1;
        boolean result = collectionSortedBag.remove(integer);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testRemoveNonExistingElement() throws Throwable {
        TreeBag<Locale.FilteringMode> treeBag = new TreeBag<>();
        CollectionSortedBag<Locale.FilteringMode> collectionSortedBag = new CollectionSortedBag<>(treeBag);
        boolean result = collectionSortedBag.remove(Locale.FilteringMode.REJECT_EXTENDED_RANGES);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testAddAndContainsAll() throws Throwable {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator).compare(any(), any());
        TreeBag<Object> treeBag = new TreeBag<>(comparator);
        CollectionSortedBag<Object> collectionSortedBag = new CollectionSortedBag<>(treeBag);
        Object object = new Object();
        collectionSortedBag.add(object, 1021);
        assertTrue(collectionSortedBag.containsAll(treeBag));
    }

    @Test(timeout = 4000)
    public void testAddElement() throws Throwable {
        PriorityQueue<Locale.Category> priorityQueue = new PriorityQueue<>();
        TreeBag<Locale.Category> treeBag = new TreeBag<>(priorityQueue);
        CollectionSortedBag<Locale.Category> collectionSortedBag = new CollectionSortedBag<>(treeBag);
        Locale.Category category = Locale.Category.DISPLAY;
        boolean result = collectionSortedBag.add(category);
        assertTrue(collectionSortedBag.contains(category));
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testRetainAllThrowsException() throws Throwable {
        TreeBag<Locale.FilteringMode> treeBag = new TreeBag<>();
        CollectionSortedBag<Locale.FilteringMode> collectionSortedBag = new CollectionSortedBag<>(treeBag);
        collectionSortedBag.add(Locale.FilteringMode.MAP_EXTENDED_RANGES, 1407);
        try {
            collectionSortedBag.retainAll(treeBag);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAllThrowsException() throws Throwable {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        TreeBag<Object> treeBag = new TreeBag<>(comparator);
        CollectionSortedBag<Object> collectionSortedBag = new CollectionSortedBag<>(treeBag);
        try {
            collectionSortedBag.removeAll(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAllConcurrentModificationException() throws Throwable {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator).compare(any(), any());
        TreeBag<Object> treeBag = new TreeBag<>(comparator);
        Object object = new Object();
        treeBag.add(object);
        CollectionSortedBag<Object> collectionSortedBag = new CollectionSortedBag<>(treeBag);
        collectionSortedBag.addAll(treeBag);
        try {
            collectionSortedBag.removeAll(treeBag);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAllUnsupportedOperationException() throws Throwable {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(4).when(comparator).compare(any(), any());
        TreeBag<Object> treeBag = new TreeBag<>(comparator);
        Object object = new Object();
        treeBag.add(object);
        SortedBag<Object> sortedBag = UnmodifiableSortedBag.unmodifiableSortedBag(treeBag);
        CollectionSortedBag<Object> collectionSortedBag = new CollectionSortedBag<>(sortedBag);
        try {
            collectionSortedBag.removeAll(sortedBag);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAllNullPointerException() throws Throwable {
        TreeBag<Integer> treeBag = new TreeBag<>(null);
        CollectionSortedBag<Integer> collectionSortedBag = new CollectionSortedBag<>(treeBag);
        try {
            collectionSortedBag.remove(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Additional tests can be similarly refactored for clarity and readability
}