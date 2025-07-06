package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Vector;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.CollectionBag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.PredicatedBag;
import org.apache.commons.collections4.bag.PredicatedSortedBag;
import org.apache.commons.collections4.bag.SynchronizedBag;
import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.commons.collections4.bag.TransformedBag;
import org.apache.commons.collections4.bag.TransformedSortedBag;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.bag.UnmodifiableBag;
import org.apache.commons.collections4.bag.UnmodifiableSortedBag;
import org.apache.commons.collections4.functors.AndPredicate;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.IdentityPredicate;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.NullIsFalsePredicate;
import org.apache.commons.collections4.functors.TransformerClosure;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class CollectionBagTest extends CollectionBag_ESTest_scaffolding {

    // Test cases for add operation
    @Test(timeout = 4000)
    public void testAdd() throws Throwable {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);
        Locale.Category localeCategory = Locale.Category.FORMAT;
        boolean result = collectionBag.add(localeCategory);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testAddAll() throws Throwable {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);
        treeBag.add(Locale.Category.FORMAT);
        treeBag.add(Locale.Category.DISPLAY);
        CollectionBag<Locale.Category> collectionBag1 = new CollectionBag<>(treeBag);
        boolean result = collectionBag.addAll(collectionBag1);
        assertTrue(result);
    }

    // Test cases for remove operation
    @Test(timeout = 4000)
    public void testRemove() throws Throwable {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        Locale.Category localeCategory = Locale.Category.FORMAT;
        treeBag.add(localeCategory);
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);
        boolean result = collectionBag.remove(localeCategory);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testRemoveAll() throws Throwable {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        treeBag.add(Locale.Category.FORMAT);
        treeBag.add(Locale.Category.DISPLAY);
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);
        EnumSet<Locale.Category> enumSet = EnumSet.allOf(Locale.Category.class);
        boolean result = collectionBag.removeAll(enumSet);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testRetainAll() throws Throwable {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        treeBag.add(Locale.Category.FORMAT);
        treeBag.add(Locale.Category.DISPLAY);
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);
        EnumSet<Locale.Category> enumSet = EnumSet.of(Locale.Category.FORMAT);
        boolean result = collectionBag.retainAll(enumSet);
        assertTrue(result);
    }

    // Test cases for different edge cases and Exception scenarios
    @Test(timeout = 4000)
    public void testExceptionFromRuntimeException() throws Throwable {
        try {
            TreeBag<Transformer<InstanceofPredicate, InstanceofPredicate>> treeBag = new TreeBag<>();
            CollectionBag<Transformer<InstanceofPredicate, InstanceofPredicate>> collectionBag = new CollectionBag<>(treeBag);
            collectionBag = new CollectionBag<>((Bag<Transformer<InstanceofPredicate, InstanceofPredicate>>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Verify the exception is correctly thrown
        }
    }

    @Test(timeout = 4000)
    public void testExceptionFromConcurrentModificationException() throws Throwable {
        try {
            TreeBag<Locale.Category> treeBag = new TreeBag<>();
            treeBag.add(Locale.Category.FORMAT);
            CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);
            collectionBag.addAll(treeBag);
            collectionBag.removeAll(treeBag);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            // Verify the exception is correctly thrown
        }
    }

    @Test(timeout = 4000)
    public void testExceptionFromUnsupportedOperationException() throws Throwable {
        try {
            TreeBag<Locale.Category> treeBag = new TreeBag<>();
            UnmodifiableBag<Locale.Category> unmodifiableBag = UnmodifiableBag.unmodifiableBag(treeBag);
            CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(unmodifiableBag);
            collectionBag.add(Locale.Category.FORMAT);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Verify the exception is correctly thrown
        }
    }

    @Test(timeout = 4000)
    public void testNullElement() throws Throwable {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);
        try {
            collectionBag.add(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Verify the exception is correctly thrown
        }
    }

    // Additional tests
    @Test(timeout = 4000)
    public void testContainsAll() throws Throwable {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        treeBag.add(Locale.Category.FORMAT);
        treeBag.add(Locale.Category.DISPLAY);
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);
        CollectionBag<Locale.Category> collectionBag1 = new CollectionBag<>(treeBag);
        boolean result = collectionBag.containsAll(collectionBag1);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testEmptyCollection() throws Throwable {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);
        try {
            collectionBag.addAll(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Verify the exception is correctly thrown
        }
    }
}