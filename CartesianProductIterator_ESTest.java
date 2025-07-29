package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Stack;
import org.apache.commons.collections4.iterators.CartesianProductIterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CartesianProductIterator_ESTest extends CartesianProductIterator_ESTest_scaffolding {

    // Test to verify that the iterator correctly identifies the presence of a next element
    @Test(timeout = 4000)
    public void testHasNextWhenElementsExist() throws Throwable {
        Iterable<Locale.Category>[] iterables = (Iterable<Locale.Category>[]) Array.newInstance(Iterable.class, 2);
        PriorityQueue<Locale.Category> priorityQueue = new PriorityQueue<>();
        Locale.Category categoryFormat = Locale.Category.FORMAT;

        priorityQueue.add(categoryFormat);
        iterables[0] = priorityQueue;
        iterables[1] = priorityQueue;

        CartesianProductIterator<Object> iterator = new CartesianProductIterator<>(iterables);
        assertTrue(iterator.hasNext());
    }

    // Test to verify that a ConcurrentModificationException is thrown when the underlying collection is modified
    @Test(timeout = 4000)
    public void testConcurrentModificationException() throws Throwable {
        Stack<Locale.Category> stack = new Stack<>();
        Locale.Category categoryDisplay = Locale.Category.DISPLAY;
        stack.add(categoryDisplay);

        Iterable<Locale.Category>[] iterables = (Iterable<Locale.Category>[]) Array.newInstance(Iterable.class, 1);
        iterables[0] = stack;

        CartesianProductIterator<Locale.Category> iterator = new CartesianProductIterator<>(iterables);
        stack.add(categoryDisplay); // Modify the stack after creating the iterator

        try {
            iterator.next();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.Vector$Itr", e);
        }
    }

    // Test to verify that a NullPointerException is thrown when an iterable is null
    @Test(timeout = 4000)
    public void testNullPointerExceptionForNullIterable() throws Throwable {
        Iterable<Locale.Category>[] iterables = (Iterable<Locale.Category>[]) Array.newInstance(Iterable.class, 1);

        try {
            new CartesianProductIterator<>(iterables);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    // Test to verify that hasNext returns false when no elements are present
    @Test(timeout = 4000)
    public void testHasNextWhenNoElements() throws Throwable {
        Iterable<Locale.Category>[] iterables = (Iterable<Locale.Category>[]) Array.newInstance(Iterable.class, 2);
        PriorityQueue<Locale.Category> priorityQueue = new PriorityQueue<>();

        iterables[0] = priorityQueue;

        CartesianProductIterator<Object> iterator = new CartesianProductIterator<>(iterables);
        assertFalse(iterator.hasNext());
    }

    // Test to verify that the iterator correctly returns elements from the Cartesian product
    @Test(timeout = 4000)
    public void testNextReturnsElements() throws Throwable {
        EnumSet<Locale.Category> enumSet = EnumSet.allOf(Locale.Category.class);
        Iterable<Locale.Category>[] iterables = (Iterable<Locale.Category>[]) Array.newInstance(Iterable.class, 2);

        iterables[0] = enumSet;
        iterables[1] = enumSet;

        CartesianProductIterator<Locale.Category> iterator = new CartesianProductIterator<>(iterables);

        iterator.next();
        iterator.next();
        List<Locale.Category> result = iterator.next();

        assertFalse(result.isEmpty());
    }

    // Test to verify that a NoSuchElementException is thrown when next is called on an empty iterator
    @Test(timeout = 4000)
    public void testNoSuchElementException() throws Throwable {
        Iterable<SQLNonTransientConnectionException>[] iterables = (Iterable<SQLNonTransientConnectionException>[]) Array.newInstance(Iterable.class, 0);
        CartesianProductIterator<SQLNonTransientConnectionException> iterator = new CartesianProductIterator<>(iterables);

        try {
            iterator.next();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.collections4.iterators.CartesianProductIterator", e);
        }
    }

    // Test to verify that an UnsupportedOperationException is thrown when remove is called
    @Test(timeout = 4000)
    public void testUnsupportedOperationExceptionOnRemove() throws Throwable {
        Iterable<Locale.Category>[] iterables = (Iterable<Locale.Category>[]) Array.newInstance(Iterable.class, 0);
        CartesianProductIterator<Locale.Category> iterator = new CartesianProductIterator<>(iterables);

        try {
            iterator.remove();
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.collections4.iterators.CartesianProductIterator", e);
        }
    }
}