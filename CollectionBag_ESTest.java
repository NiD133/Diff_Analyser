package org.apache.commons.collections4.bag;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

import java.util.*;

import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CollectionBag_ESTest extends CollectionBag_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testRemoveExistingElement() {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        Locale.Category category = Locale.Category.FORMAT;
        treeBag.add(category);
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);

        boolean result = collectionBag.remove(category);

        assertTrue("The element should be removed successfully", result);
    }

    @Test(timeout = 4000)
    public void testRemoveNonExistingElement() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        HashBag<Object> hashBag = new HashBag<>(linkedList);
        CollectionBag<Object> collectionBag = new CollectionBag<>(hashBag);
        Locale.FilteringMode filteringMode = Locale.FilteringMode.REJECT_EXTENDED_RANGES;

        boolean result = collectionBag.remove(filteringMode);

        assertFalse("The element should not be removed as it does not exist", result);
    }

    @Test(timeout = 4000)
    public void testContainsAllWithPredicatedSortedBag() {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator).compare(any(), any());
        TreeBag<Locale.FilteringMode> treeBag = new TreeBag<>(comparator);
        Predicate<Object> predicate = ExceptionPredicate.exceptionPredicate();
        PredicatedSortedBag<Locale.FilteringMode> predicatedSortedBag = PredicatedSortedBag.predicatedSortedBag(treeBag, predicate);
        Locale.FilteringMode filteringMode = Locale.FilteringMode.IGNORE_EXTENDED_RANGES;
        treeBag.add(filteringMode, 5);
        Stack<LinkedList<Object>> stack = new Stack<>();
        TreeBag<LinkedList<Object>> treeBag1 = new TreeBag<>(stack);
        CollectionBag<LinkedList<Object>> collectionBag = new CollectionBag<>(treeBag1);

        boolean result = collectionBag.containsAll(predicatedSortedBag);

        assertFalse("The collection should not contain all elements of the predicated sorted bag", result);
    }

    @Test(timeout = 4000)
    public void testRetainAllWithEmptyTreeBag() {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);
        Locale.Category category = Locale.Category.FORMAT;
        collectionBag.add(category, 3073);

        // Expecting an exception as the operation is not supported
        try {
            collectionBag.retainAll(treeBag);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.collections4.bag.UnmodifiableSortedBag", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAllWithUnmodifiableBag() {
        EnumSet<Locale.FilteringMode> enumSet = EnumSet.allOf(Locale.FilteringMode.class);
        HashBag<Locale.FilteringMode> hashBag = new HashBag<>(enumSet);
        Bag<Object> unmodifiableBag = UnmodifiableBag.unmodifiableBag(hashBag);
        CollectionBag<Object> collectionBag = new CollectionBag<>(unmodifiableBag);

        // Expecting an exception as the operation is not supported
        try {
            collectionBag.removeAll(hashBag);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.collections4.bag.UnmodifiableBag", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddAllWithConcurrentModification() {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        Locale.Category category = Locale.Category.FORMAT;
        treeBag.add(category);
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);
        collectionBag.addAll(treeBag);

        // Expecting an exception due to concurrent modification
        try {
            collectionBag.addAll(treeBag);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("org.apache.commons.collections4.bag.AbstractMapBag$BagIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddWithExceptionTransformer() {
        HashBag<Object> hashBag = new HashBag<>();
        Transformer<Object, Object> transformer = ExceptionTransformer.exceptionTransformer();
        Bag<Object> transformedBag = TransformedBag.transformingBag(hashBag, transformer);
        CollectionBag<Object> collectionBag = new CollectionBag<>(transformedBag);

        // Expecting an exception due to the exception transformer
        try {
            collectionBag.add(new Object(), 5);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.ExceptionTransformer", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddWithNullElement() {
        TreeBag<Object> treeBag = new TreeBag<>();
        CollectionBag<Object> collectionBag = new CollectionBag<>(treeBag);

        // Expecting an exception due to null element
        try {
            collectionBag.add(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.TreeMap", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAllWithNullCollection() {
        TreeBag<Object> treeBag = new TreeBag<>();
        CollectionBag<Object> collectionBag = new CollectionBag<>(treeBag);

        // Expecting an exception due to null collection
        try {
            collectionBag.removeAll(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.collections4.bag.AbstractMapBag", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddAllWithEmptyCollection() {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);

        boolean result = collectionBag.addAll(treeBag);

        assertFalse("Adding an empty collection should not change the collection", result);
    }

    @Test(timeout = 4000)
    public void testRemoveAllWithEmptyCollection() {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);

        boolean result = collectionBag.removeAll(treeBag);

        assertFalse("Removing an empty collection should not change the collection", result);
    }

    @Test(timeout = 4000)
    public void testContainsAllWithEmptyCollection() {
        TreeBag<Locale.Category> treeBag = new TreeBag<>();
        CollectionBag<Locale.Category> collectionBag = new CollectionBag<>(treeBag);

        boolean result = collectionBag.containsAll(treeBag);

        assertTrue("An empty collection should be considered as contained", result);
    }
}