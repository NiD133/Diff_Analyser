package org.apache.commons.collections4.bag;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.PriorityQueue;

import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.bag.UnmodifiableSortedBag;
import org.apache.commons.collections4.bag.PredicatedSortedBag;
import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.commons.collections4.bag.TransformedSortedBag;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;

/**
 * Test suite for CollectionSortedBag functionality.
 * Tests the decorator pattern implementation that makes SortedBag comply with Collection contract.
 */
public class CollectionSortedBag_ImprovedTest {

    private TreeBag<String> baseBag;
    private CollectionSortedBag<String> collectionBag;
    private Comparator<Object> mockComparator;

    @Before
    public void setUp() {
        baseBag = new TreeBag<>();
        collectionBag = new CollectionSortedBag<>(baseBag);
        mockComparator = mock(Comparator.class);
    }

    // Factory method tests
    @Test
    public void testFactoryMethod_CreatesCollectionSortedBag() {
        TreeBag<String> treeBag = new TreeBag<>();
        SortedBag<String> result = CollectionSortedBag.collectionSortedBag(treeBag);
        
        assertNotNull(result);
        assertTrue(result instanceof CollectionSortedBag);
        assertEquals(0, result.size());
    }

    @Test(expected = NullPointerException.class)
    public void testFactoryMethod_ThrowsExceptionForNullBag() {
        CollectionSortedBag.collectionSortedBag(null);
    }

    // Constructor tests
    @Test(expected = NullPointerException.class)
    public void testConstructor_ThrowsExceptionForNullBag() {
        new CollectionSortedBag<>(null);
    }

    // Add operation tests
    @Test
    public void testAdd_SingleElement_ReturnsTrue() {
        boolean result = collectionBag.add("test");
        
        assertTrue(result);
        assertTrue(collectionBag.contains("test"));
        assertEquals(1, collectionBag.size());
    }

    @Test
    public void testAdd_WithCount_AddsMultipleInstances() {
        String element = "test";
        int count = 3;
        
        boolean result = collectionBag.add(element, count);
        
        assertTrue(result);
        assertEquals(count, collectionBag.getCount(element));
        assertEquals(count, collectionBag.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAdd_NullElement_ThrowsException() {
        collectionBag.add(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAdd_UnmodifiableBag_ThrowsException() {
        SortedBag<String> unmodifiableBag = UnmodifiableSortedBag.unmodifiableSortedBag(baseBag);
        CollectionSortedBag<String> wrapper = new CollectionSortedBag<>(unmodifiableBag);
        
        wrapper.add("test");
    }

    // AddAll operation tests
    @Test
    public void testAddAll_EmptyCollection_ReturnsFalse() {
        PriorityQueue<String> emptyQueue = new PriorityQueue<>();
        
        boolean result = collectionBag.addAll(emptyQueue);
        
        assertFalse(result);
        assertEquals(0, collectionBag.size());
    }

    @Test
    public void testAddAll_NonEmptyCollection_ReturnsTrue() {
        PriorityQueue<String> queue = new PriorityQueue<>();
        queue.add("element1");
        queue.add("element2");
        
        boolean result = collectionBag.addAll(queue);
        
        assertTrue(result);
        assertTrue(collectionBag.contains("element1"));
        assertTrue(collectionBag.contains("element2"));
        assertEquals(2, collectionBag.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddAll_NullCollection_ThrowsException() {
        collectionBag.addAll(null);
    }

    // Remove operation tests
    @Test
    public void testRemove_ExistingElement_ReturnsTrue() {
        when(mockComparator.compare(any(), any())).thenReturn(0);
        TreeBag<Object> treeBag = new TreeBag<>(mockComparator);
        CollectionSortedBag<Object> bag = new CollectionSortedBag<>(treeBag);
        
        Object element = new Object();
        bag.add(element);
        
        boolean result = bag.remove(1); // Due to comparator returning 0, any object matches
        
        assertTrue(result);
    }

    @Test
    public void testRemove_NonExistingElement_ReturnsFalse() {
        HashBag<String> hashBag = new HashBag<>();
        SynchronizedSortedBag<String> syncBag = new SynchronizedSortedBag<>(hashBag, "lock");
        CollectionSortedBag<String> bag = new CollectionSortedBag<>(syncBag);
        
        boolean result = bag.remove("nonexistent");
        
        assertFalse(result);
    }

    @Test(expected = NullPointerException.class)
    public void testRemove_NullElement_ThrowsException() {
        collectionBag.remove(null);
    }

    // RemoveAll operation tests
    @Test
    public void testRemoveAll_EmptyBag_ReturnsFalse() {
        TreeBag<String> otherBag = new TreeBag<>();
        
        boolean result = collectionBag.removeAll(otherBag);
        
        assertFalse(result);
    }

    @Test
    public void testRemoveAll_WithMatchingElements_ReturnsTrue() {
        when(mockComparator.compare(any(), any())).thenReturn(-1, -1, -1, 0, 0);
        TreeBag<Object> treeBag = new TreeBag<>(mockComparator);
        CollectionSortedBag<Object> bag = new CollectionSortedBag<>(treeBag);
        
        Object element = new Object();
        treeBag.add(element);
        treeBag.add(null);
        
        HashBag<Object> toRemove = new HashBag<>(bag);
        boolean result = bag.removeAll(toRemove);
        
        assertTrue(result);
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testRemoveAll_ConcurrentModification_ThrowsException() {
        when(mockComparator.compare(any(), any())).thenReturn(0, 0, 0, 0, 0);
        TreeBag<Object> treeBag = new TreeBag<>(mockComparator);
        CollectionSortedBag<Object> bag = new CollectionSortedBag<>(treeBag);
        
        Object element = new Object();
        treeBag.add(element);
        bag.addAll(treeBag); // This creates concurrent modification scenario
        
        bag.removeAll(treeBag);
    }

    // RetainAll operation tests
    @Test(expected = NullPointerException.class)
    public void testRetainAll_NullCollection_ThrowsException() {
        collectionBag.retainAll(null);
    }

    @Test
    public void testRetainAll_SameCollection_ReturnsFalse() {
        when(mockComparator.compare(any(), any())).thenReturn(-1959);
        TreeBag<Object> treeBag = new TreeBag<>(mockComparator);
        CollectionSortedBag<Object> bag = new CollectionSortedBag<>(treeBag);
        
        Object element = new Object();
        treeBag.add(element);
        
        boolean result = bag.retainAll(bag);
        
        assertFalse(result);
    }

    // ContainsAll operation tests
    @Test
    public void testContainsAll_EmptyCollection_ReturnsTrue() {
        TreeBag<String> emptyBag = new TreeBag<>();
        
        boolean result = collectionBag.containsAll(emptyBag);
        
        assertTrue(result);
    }

    @Test
    public void testContainsAll_NonMatchingElements_ReturnsFalse() {
        when(mockComparator.compare(any(), any())).thenReturn(0);
        TreeBag<Object> treeBag1 = new TreeBag<>(mockComparator);
        CollectionSortedBag<Object> bag1 = new CollectionSortedBag<>(treeBag1);
        
        Object element = new Object();
        bag1.add(element, 1021);
        
        TreeBag<String> treeBag2 = new TreeBag<>();
        CollectionSortedBag<String> bag2 = new CollectionSortedBag<>(treeBag2);
        
        boolean result = bag2.containsAll(treeBag1);
        
        assertFalse(result);
    }

    // Exception handling tests for decorated bags
    @Test(expected = UnsupportedOperationException.class)
    public void testUnmodifiableBag_AddOperation_ThrowsException() {
        TreeBag<String> treeBag = new TreeBag<>();
        SortedBag<String> unmodifiableBag = UnmodifiableSortedBag.unmodifiableSortedBag(treeBag);
        CollectionSortedBag<String> bag = new CollectionSortedBag<>(unmodifiableBag);
        
        bag.add("test", 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPredicatedBag_InvalidElement_ThrowsException() {
        TreeBag<String> treeBag = new TreeBag<>();
        Predicate<String> predicate = EqualPredicate.equalPredicate("allowed");
        PredicatedSortedBag<String> predicatedBag = PredicatedSortedBag.predicatedSortedBag(treeBag, predicate);
        CollectionSortedBag<String> bag = new CollectionSortedBag<>(predicatedBag);
        
        bag.add("notallowed");
    }

    @Test(expected = RuntimeException.class)
    public void testTransformedBag_ExceptionTransformer_ThrowsException() {
        TreeBag<String> treeBag = new TreeBag<>();
        CollectionSortedBag<String> baseBag = new CollectionSortedBag<>(treeBag);
        Transformer<String, String> exceptionTransformer = ExceptionTransformer.exceptionTransformer();
        TransformedSortedBag<String> transformedBag = new TransformedSortedBag<>(baseBag, exceptionTransformer);
        CollectionSortedBag<String> bag = new CollectionSortedBag<>(transformedBag);
        
        bag.add("test");
    }

    @Test(expected = NullPointerException.class)
    public void testTransformedBag_NullTransformer_ThrowsException() {
        TreeBag<String> treeBag = new TreeBag<>();
        Transformer<String, String> nullTransformer = ConstantTransformer.nullTransformer();
        TransformedSortedBag<String> transformedBag = TransformedSortedBag.transformingSortedBag(treeBag, nullTransformer);
        CollectionSortedBag<String> bag = new CollectionSortedBag<>(transformedBag);
        
        bag.add("test", 32);
    }

    // Type safety tests
    @Test(expected = ClassCastException.class)
    public void testTypeSafety_IncompatibleTypes_ThrowsException() {
        TreeBag<String> stringBag = new TreeBag<>();
        TreeBag<Integer> intBag = new TreeBag<>();
        intBag.add(123);
        
        CollectionSortedBag<String> bag = new CollectionSortedBag<>(stringBag);
        
        // This should throw ClassCastException due to type incompatibility
        bag.addAll((Collection) intBag);
    }
}