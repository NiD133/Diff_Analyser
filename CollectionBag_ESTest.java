package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.*;
import org.apache.commons.collections4.functors.*;

/**
 * Test suite for CollectionBag class.
 * Tests the decorator pattern implementation that makes Bag comply with Collection contract.
 */
public class CollectionBagTest {

    // ========== Basic Operations Tests ==========
    
    @Test
    public void testRemoveExistingElement() {
        // Given: A CollectionBag with an integer element
        HashBag<Integer> underlyingBag = new HashBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(underlyingBag);
        Integer element = 389;
        underlyingBag.add(element);
        
        // When: Removing the element
        boolean wasRemoved = collectionBag.remove(element);
        
        // Then: Element should be successfully removed
        assertTrue("Element should be removed successfully", wasRemoved);
    }

    @Test
    public void testAddSingleElement() {
        // Given: An empty CollectionBag
        HashBag<Integer> underlyingBag = new HashBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(underlyingBag);
        Integer element = 0;
        
        // When: Adding an element
        boolean wasAdded = collectionBag.add(element);
        
        // Then: Element should be added successfully
        assertTrue("Element should be added successfully", wasAdded);
        assertTrue("Bag should contain the added element", collectionBag.contains(element));
    }

    @Test
    public void testContainsAllWithEmptyCollections() {
        // Given: Two empty collections
        TreeBag<Integer> emptyTreeBag = new TreeBag<>();
        HashBag<Object> emptyHashBag = new HashBag<>(emptyTreeBag);
        CollectionBag<Object> collectionBag = new CollectionBag<>(emptyHashBag);
        
        // When: Checking if one contains all elements of the other
        boolean containsAll = collectionBag.containsAll(emptyTreeBag);
        
        // Then: Should return true (empty set contains all elements of empty set)
        assertTrue("Empty collection should contain all elements of another empty collection", containsAll);
    }

    // ========== Collection Operations Tests ==========
    
    @Test
    public void testRetainAllWithSameElements() {
        // Given: A CollectionBag and a collection with the same element
        TreeBag<Integer> treeBag = new TreeBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(treeBag);
        Integer element = 1;
        
        HashBag<Integer> otherBag = new HashBag<>();
        otherBag.add(element);
        collectionBag.addAll(otherBag);
        
        // When: Retaining all elements that are also in the original tree bag
        boolean wasModified = collectionBag.retainAll(treeBag);
        
        // Then: Collection should contain the element and not be modified
        assertTrue("Collection should still contain the element", collectionBag.contains(element));
        assertFalse("Collection should not be modified when retaining existing elements", wasModified);
    }

    @Test
    public void testRetainAllRemovesNonMatchingElements() {
        // Given: A CollectionBag with an element not in the retain collection
        HashBag<Integer> underlyingBag = new HashBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(underlyingBag);
        Integer element = 2221;
        underlyingBag.add(element);
        
        LinkedList<Integer> emptyList = new LinkedList<>();
        
        // When: Retaining only elements in the empty list
        boolean wasModified = collectionBag.retainAll(emptyList);
        
        // Then: Element should be removed
        assertFalse("Element should be removed from underlying bag", underlyingBag.contains(element));
        assertTrue("Collection should be modified", wasModified);
    }

    @Test
    public void testRemoveAllWithMatchingElements() {
        // Given: A CollectionBag and another collection with overlapping elements
        TreeBag<Integer> treeBag = new TreeBag<>();
        Integer element = 2616;
        treeBag.add(element);
        CollectionBag<Integer> collectionBag = new CollectionBag<>(treeBag);
        collectionBag.addAll(treeBag); // Now has 2 copies
        
        HashBag<Object> otherBag = new HashBag<>(treeBag);
        CollectionBag<Object> otherCollectionBag = new CollectionBag<>(otherBag);
        
        // When: Removing all elements that exist in the other collection
        boolean wasModified = otherCollectionBag.removeAll(treeBag);
        
        // Then: Elements should be removed
        assertEquals("Original bag should still have 2 elements", 2, treeBag.size());
        assertTrue("Collection should be modified", wasModified);
    }

    @Test
    public void testRemoveAllWithNoMatchingElements() {
        // Given: Two collections with no overlapping elements
        TreeBag<Integer> integerBag = new TreeBag<>();
        CollectionBag<Integer> integerCollectionBag = new CollectionBag<>(integerBag);
        
        HashBag<ComparatorPredicate.Criterion> criterionBag = new HashBag<>();
        CollectionBag<ComparatorPredicate.Criterion> criterionCollectionBag = 
            new CollectionBag<>(criterionBag);
        
        // When: Removing all elements (no overlap)
        boolean wasModified = criterionCollectionBag.removeAll(integerCollectionBag);
        
        // Then: No modification should occur
        assertFalse("Collection should not be modified when no elements match", wasModified);
    }

    @Test
    public void testAddAllWithEmptyCollection() {
        // Given: A CollectionBag and an empty collection to add
        TreeBag<Integer> treeBag = new TreeBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(treeBag);
        HashBag<Integer> emptyBag = new HashBag<>();
        
        // When: Adding all elements from empty collection
        boolean wasModified = collectionBag.addAll(emptyBag);
        
        // Then: No modification should occur
        assertFalse("Collection should not be modified when adding empty collection", wasModified);
    }

    @Test
    public void testAddAllWithElements() {
        // Given: A CollectionBag and another collection with elements
        TreeBag<Integer> treeBag = new TreeBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(treeBag);
        Integer element = 2358;
        treeBag.add(element);
        collectionBag.addAll(treeBag); // Now has 2 copies
        
        HashBag<Object> otherBag = new HashBag<>(treeBag);
        CollectionBag<Object> otherCollectionBag = new CollectionBag<>(otherBag);
        
        // When: Adding all elements from the first collection
        boolean wasModified = otherCollectionBag.addAll(treeBag);
        
        // Then: Elements should be added
        assertEquals("Other bag should now have 4 elements", 4, otherBag.size());
        assertTrue("Collection should be modified", wasModified);
    }

    // ========== Factory Method Tests ==========
    
    @Test
    public void testCollectionBagFactoryMethod() {
        // Given: A regular bag
        HashBag<ComparatorPredicate.Criterion> regularBag = new HashBag<>();
        
        // When: Creating a CollectionBag using factory method
        Bag<ComparatorPredicate.Criterion> collectionCompliantBag = 
            CollectionBag.collectionBag(regularBag);
        
        // Then: Should create a valid collection-compliant bag
        assertEquals("Factory method should create empty bag", 0, collectionCompliantBag.size());
    }

    // ========== Error Condition Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullBag() {
        // When: Creating CollectionBag with null bag
        // Then: Should throw NullPointerException
        new CollectionBag<>((Bag<Object>) null);
    }

    @Test(expected = NullPointerException.class)
    public void testFactoryMethodWithNullBag() {
        // When: Using factory method with null bag
        // Then: Should throw NullPointerException
        CollectionBag.collectionBag((Bag<Object>) null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddAllWithNullCollection() {
        // Given: A CollectionBag
        TreeBag<Integer> treeBag = new TreeBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(treeBag);
        
        // When: Adding null collection
        // Then: Should throw NullPointerException
        collectionBag.addAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void testRetainAllWithNullCollection() {
        // Given: A CollectionBag
        TreeBag<Integer> treeBag = new TreeBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(treeBag);
        
        // When: Retaining null collection
        // Then: Should throw NullPointerException
        collectionBag.retainAll(null);
    }

    @Test
    public void testRemoveAllWithNullCollection() {
        // Given: A CollectionBag
        HashBag<Integer> hashBag = new HashBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(hashBag);
        
        // When: Removing null collection
        boolean wasModified = collectionBag.removeAll(null);
        
        // Then: Should return false (no modification)
        assertFalse("Removing null collection should not modify the bag", wasModified);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveOnUnmodifiableBag() {
        // Given: An unmodifiable bag wrapped in CollectionBag
        TreeBag<Integer> treeBag = new TreeBag<>();
        Bag<Object> unmodifiableBag = UnmodifiableBag.unmodifiableBag(treeBag);
        CollectionBag<Object> collectionBag = new CollectionBag<>(unmodifiableBag);
        
        // When: Attempting to remove an element
        // Then: Should throw UnsupportedOperationException
        collectionBag.remove(new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddOnUnmodifiableBag() {
        // Given: An unmodifiable bag wrapped in CollectionBag
        TreeBag<Integer> treeBag = new TreeBag<>();
        SortedBag<Integer> unmodifiableBag = UnmodifiableSortedBag.unmodifiableSortedBag(treeBag);
        CollectionBag<Integer> collectionBag = new CollectionBag<>(unmodifiableBag);
        
        // When: Attempting to add an element
        // Then: Should throw UnsupportedOperationException
        collectionBag.add(-300);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullToTreeBagBasedCollection() {
        // Given: A CollectionBag based on TreeBag (doesn't allow nulls)
        TreeBag<Integer> treeBag = new TreeBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(treeBag);
        
        // When: Adding null element
        // Then: Should throw NullPointerException
        collectionBag.add(null);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullFromTreeBagBasedCollection() {
        // Given: A CollectionBag based on TreeBag (doesn't allow nulls)
        TreeBag<Integer> treeBag = new TreeBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(treeBag);
        
        // When: Removing null element
        // Then: Should throw NullPointerException
        collectionBag.remove(null);
    }

    // ========== Edge Cases and Complex Scenarios ==========
    
    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModificationDuringRetainAll() {
        // Given: A CollectionBag with elements
        TreeBag<Integer> treeBag = new TreeBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(treeBag);
        collectionBag.add(10, 10); // Add 10 copies
        
        // When: Retaining all from the same collection (causes concurrent modification)
        // Then: Should throw ConcurrentModificationException
        collectionBag.retainAll(treeBag);
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModificationDuringRemoveAll() {
        // Given: A CollectionBag with elements
        TreeBag<Integer> treeBag = new TreeBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(treeBag);
        collectionBag.add(10, 10); // Add 10 copies
        
        // When: Removing all from the same collection (causes concurrent modification)
        // Then: Should throw ConcurrentModificationException
        collectionBag.removeAll(treeBag);
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModificationDuringAddAll() {
        // Given: A CollectionBag with elements
        TreeBag<Integer> treeBag = new TreeBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(treeBag);
        collectionBag.add(273, 273); // Add many copies
        
        // When: Adding all from the same collection (causes concurrent modification)
        // Then: Should throw ConcurrentModificationException
        collectionBag.addAll(treeBag);
    }

    @Test
    public void testRemoveNonExistentElement() {
        // Given: A CollectionBag without a specific element
        HashBag<Integer> hashBag = new HashBag<>();
        CollectionBag<Integer> collectionBag = new CollectionBag<>(hashBag);
        SynchronizedBag<Integer> nonExistentElement = new SynchronizedBag<>(hashBag, new LinkedList<>());
        
        // When: Removing non-existent element
        boolean wasRemoved = collectionBag.remove(nonExistentElement);
        
        // Then: Should return false
        assertFalse("Removing non-existent element should return false", wasRemoved);
    }
}