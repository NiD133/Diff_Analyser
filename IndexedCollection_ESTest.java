package org.apache.commons.collections4.collection;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.apache.commons.collections4.functors.NotNullPredicate;

/**
 * Test suite for IndexedCollection functionality.
 * Tests both unique and non-unique indexed collections with various operations.
 */
public class IndexedCollection_ESTest {

    // ========== Basic Operations Tests ==========
    
    @Test
    public void testAddAllToNonUniqueIndexedCollection() {
        // Given: A non-unique indexed collection and a list with duplicate elements
        List<Integer> sourceList = new LinkedList<>();
        Integer value = -3039;
        sourceList.add(value);
        sourceList.add(value); // Add duplicate
        
        Transformer<Integer, String> keyTransformer = new ConstantTransformer<>("key");
        List<Integer> targetList = new LinkedList<>();
        IndexedCollection<String, Integer> indexedCollection = 
            IndexedCollection.nonUniqueIndexedCollection(targetList, keyTransformer);
        
        // When: Adding all elements from source list
        boolean result = indexedCollection.addAll(sourceList);
        
        // Then: All elements should be added successfully
        assertTrue("Should successfully add all elements", result);
        assertEquals("Target list should contain both elements", 2, targetList.size());
    }

    @Test
    public void testGetElementByKey() {
        // Given: An indexed collection with one element
        List<List<Integer>> collection = new LinkedList<>();
        List<Integer> element = new LinkedList<>();
        collection.add(element);
        
        Integer key = 1;
        Transformer<List<Integer>, Integer> keyTransformer = ConstantTransformer.constantTransformer(key);
        IndexedCollection<Integer, List<Integer>> indexedCollection = 
            IndexedCollection.uniqueIndexedCollection(collection, keyTransformer);
        
        // When: Retrieving element by key
        List<Integer> retrievedElement = indexedCollection.get(key);
        
        // Then: Should return the correct element
        assertSame("Should return the same element that was added", element, retrievedElement);
    }

    @Test
    public void testValuesReturnedByKey() {
        // Given: An indexed collection with one element
        List<List<Integer>> collection = new LinkedList<>();
        List<Integer> element = new LinkedList<>();
        collection.add(element);
        
        Integer key = 1;
        Transformer<List<Integer>, Integer> keyTransformer = ConstantTransformer.constantTransformer(key);
        IndexedCollection<Integer, List<Integer>> indexedCollection = 
            IndexedCollection.uniqueIndexedCollection(collection, keyTransformer);
        
        // When: Getting all values for the key
        Collection<List<Integer>> values = indexedCollection.values(key);
        
        // Then: Should return collection containing the element
        assertNotNull("Values collection should not be null", values);
        assertTrue("Values should contain the added element", values.contains(element));
    }

    // ========== Collection Operations Tests ==========

    @Test
    public void testRemoveAllFromEmptyCollection() {
        // Given: An empty indexed collection
        List<Integer> collection = new LinkedList<>();
        Transformer<Integer, Integer> keyTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Integer, Integer> indexedCollection = 
            IndexedCollection.uniqueIndexedCollection(collection, keyTransformer);
        
        // When: Attempting to remove all elements from empty collection
        boolean result = indexedCollection.removeAll(collection);
        
        // Then: Should return false (no elements removed)
        assertFalse("Should return false when removing from empty collection", result);
    }

    @Test
    public void testRemoveExistingElement() {
        // Given: An indexed collection with a null element
        List<Integer> collection = new LinkedList<>();
        collection.add(null);
        
        Transformer<Integer, Integer> keyTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Integer, Integer> indexedCollection = 
            IndexedCollection.uniqueIndexedCollection(collection, keyTransformer);
        
        // When: Removing the null element
        boolean result = indexedCollection.remove(null);
        
        // Then: Should successfully remove the element
        assertTrue("Should successfully remove existing element", result);
        assertTrue("Collection should be empty after removal", collection.isEmpty());
    }

    @Test
    public void testContainsExistingElement() {
        // Given: An indexed collection with an element
        List<Integer> collection = new LinkedList<>();
        Integer element = 3;
        collection.add(element);
        
        Transformer<Integer, String> keyTransformer = new ConstantTransformer<>("key");
        IndexedCollection<String, Integer> indexedCollection = 
            IndexedCollection.nonUniqueIndexedCollection(collection, keyTransformer);
        
        // When: Checking if element exists
        boolean contains = indexedCollection.contains(element);
        
        // Then: Should return true
        assertTrue("Should contain the added element", contains);
    }

    @Test
    public void testContainsNonExistentElement() {
        // Given: An empty indexed collection
        List<Object> collection = new LinkedList<>();
        Transformer<Object, Object> keyTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Object, Object> indexedCollection = 
            IndexedCollection.uniqueIndexedCollection(collection, keyTransformer);
        
        // When: Checking for non-existent element
        boolean contains = indexedCollection.contains(collection);
        
        // Then: Should return false
        assertFalse("Should not contain non-existent element", contains);
    }

    // ========== Error Handling Tests ==========

    @Test(expected = RuntimeException.class)
    public void testExceptionTransformerThrowsOnCreation() {
        // Given: A collection with an element and an exception-throwing transformer
        List<Integer> collection = new LinkedList<>();
        collection.add(null);
        
        Transformer<Integer, Integer> exceptionTransformer = ExceptionTransformer.exceptionTransformer();
        
        // When: Creating indexed collection with exception transformer
        // Then: Should throw RuntimeException
        IndexedCollection.uniqueIndexedCollection(collection, exceptionTransformer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUniqueIndexViolation() {
        // Given: A transformer that returns the same key for different objects
        Transformer<Object, Object> constantTransformer = ConstantTransformer.nullTransformer();
        List<Object> collection = new LinkedList<>();
        
        Object obj1 = new Object();
        Object obj2 = new Object();
        collection.add(obj1);
        collection.add(obj2); // Both will have same key (null)
        
        // When: Creating unique indexed collection with duplicate keys
        // Then: Should throw IllegalArgumentException
        IndexedCollection.uniqueIndexedCollection(collection, constantTransformer);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCollectionThrowsException() {
        // When: Creating indexed collection with null collection
        // Then: Should throw NullPointerException
        IndexedCollection.uniqueIndexedCollection(null, null);
    }

    // ========== Reindexing Tests ==========

    @Test
    public void testReindexWithValidTransformer() {
        // Given: An indexed collection with null transformer
        List<Object> collection = new LinkedList<>();
        Object element = new Object();
        collection.add(element);
        
        Transformer<Object, Integer> keyTransformer = new ConstantTransformer<>(null);
        IndexedCollection<Integer, Object> indexedCollection = 
            IndexedCollection.nonUniqueIndexedCollection(collection, keyTransformer);
        
        // When: Reindexing the collection
        indexedCollection.reindex();
        
        // Then: Should complete without error
        assertEquals("Collection should maintain its size", 1, collection.size());
    }

    // ========== Utility Methods Tests ==========

    @Test
    public void testClearCollection() {
        // Given: An indexed collection with elements
        List<Integer> collection = new LinkedList<>();
        collection.add(1);
        
        IndexedCollection<Integer, Integer> indexedCollection = 
            IndexedCollection.uniqueIndexedCollection(collection, null);
        
        // When: Clearing the collection
        indexedCollection.clear();
        
        // Then: Collection should be empty
        assertTrue("Collection should be empty after clear", collection.isEmpty());
    }

    @Test
    public void testGetNonExistentKey() {
        // Given: An empty indexed collection
        List<Object> collection = new LinkedList<>();
        Transformer<Object, Object> keyTransformer = ConstantTransformer.constantTransformer(collection);
        IndexedCollection<Object, Object> indexedCollection = 
            IndexedCollection.nonUniqueIndexedCollection(collection, keyTransformer);
        
        // When: Getting element with non-existent key
        Object result = indexedCollection.get(collection);
        
        // Then: Should return null
        assertNull("Should return null for non-existent key", result);
    }

    @Test
    public void testValuesWithNonExistentKey() {
        // Given: An empty indexed collection
        List<Object> collection = new LinkedList<>();
        Transformer<Object, Object> keyTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Object, Object> indexedCollection = 
            IndexedCollection.uniqueIndexedCollection(collection, keyTransformer);
        
        // When: Getting values for non-existent key
        Collection<Object> values = indexedCollection.values(new Object());
        
        // Then: Should return null
        assertNull("Should return null for non-existent key", values);
    }

    @Test
    public void testContainsAllWithValidCollection() {
        // Given: An indexed collection with a null element
        List<Integer> collection = new LinkedList<>();
        collection.add(null);
        
        Transformer<Integer, Integer> keyTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Integer, Integer> indexedCollection = 
            IndexedCollection.uniqueIndexedCollection(collection, keyTransformer);
        
        // When: Checking if collection contains all elements from itself
        boolean containsAll = indexedCollection.containsAll(collection);
        
        // Then: Should return true
        assertTrue("Collection should contain all its own elements", containsAll);
    }

    @Test
    public void testRetainAllWithEmptyCollection() {
        // Given: An indexed collection with elements
        List<Object> collection = new LinkedList<>();
        Object element = new Object();
        collection.add(element);
        
        Transformer<Object, Object> keyTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Object, Object> indexedCollection = 
            IndexedCollection.nonUniqueIndexedCollection(collection, keyTransformer);
        
        // When: Retaining only elements from empty collection
        List<Object> emptyList = new LinkedList<>();
        boolean result = indexedCollection.retainAll(emptyList);
        
        // Then: Should remove all elements and return true
        assertTrue("Should return true when elements are removed", result);
        assertTrue("Collection should be empty after retainAll with empty collection", 
                  collection.isEmpty());
    }

    @Test
    public void testRetainAllWithSameCollection() {
        // Given: An indexed collection
        List<Integer> collection = new LinkedList<>();
        Transformer<Integer, Integer> keyTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Integer, Integer> indexedCollection = 
            IndexedCollection.nonUniqueIndexedCollection(collection, keyTransformer);
        
        // When: Retaining all elements from the same collection
        boolean result = indexedCollection.retainAll(collection);
        
        // Then: Should return false (no changes made)
        assertFalse("Should return false when no elements are removed", result);
    }
}