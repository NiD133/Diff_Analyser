package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.*;

/**
 * Test suite for ObjectGraphIterator functionality.
 * Tests the iterator's ability to traverse object graphs using transformers.
 */
public class ObjectGraphIterator_ImprovedTest {

    // ========== Constructor Tests ==========
    
    @Test
    public void testConstructorWithRootObjectAndNullTransformer() {
        Integer rootValue = 42;
        ObjectGraphIterator<Integer> iterator = new ObjectGraphIterator<>(rootValue, null);
        
        assertTrue("Should have next element", iterator.hasNext());
        assertEquals("Should return root value", rootValue, iterator.next());
        assertFalse("Should not have more elements", iterator.hasNext());
    }
    
    @Test
    public void testConstructorWithIterator() {
        List<String> items = Arrays.asList("first", "second", "third");
        Iterator<String> sourceIterator = items.iterator();
        ObjectGraphIterator<String> graphIterator = new ObjectGraphIterator<>(sourceIterator);
        
        List<String> result = new ArrayList<>();
        while (graphIterator.hasNext()) {
            result.add(graphIterator.next());
        }
        
        assertEquals("Should contain all items", items, result);
    }
    
    @Test
    public void testConstructorWithEmptyIterator() {
        Iterator<String> emptyIterator = Collections.<String>emptyList().iterator();
        ObjectGraphIterator<String> graphIterator = new ObjectGraphIterator<>(emptyIterator);
        
        assertFalse("Empty iterator should have no elements", graphIterator.hasNext());
    }

    // ========== Basic Iteration Tests ==========
    
    @Test
    public void testSingleValueIteration() {
        String singleValue = "test";
        ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(singleValue, null);
        
        assertTrue("Should have one element", iterator.hasNext());
        assertEquals("Should return the single value", singleValue, iterator.next());
        assertFalse("Should be exhausted after one element", iterator.hasNext());
    }
    
    @Test
    public void testIterationWithConstantTransformer() {
        String rootValue = "root";
        String constantValue = "constant";
        Transformer<String, String> constantTransformer = ConstantTransformer.constantFactory(constantValue);
        
        ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(rootValue, constantTransformer);
        
        assertTrue("Should have elements", iterator.hasNext());
        // The behavior depends on how the transformer is applied
        iterator.next(); // Just verify it doesn't throw
    }

    // ========== Exception Handling Tests ==========
    
    @Test(expected = NoSuchElementException.class)
    public void testNextOnEmptyIterator() {
        ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>((Iterator<String>) null);
        iterator.next(); // Should throw NoSuchElementException
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemoveWithoutNext() {
        List<String> items = Arrays.asList("item");
        ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(items.iterator());
        iterator.remove(); // Should throw IllegalStateException
    }
    
    @Test(expected = IllegalStateException.class)
    public void testMultipleRemoveCalls() {
        List<String> items = new ArrayList<>(Arrays.asList("item1", "item2"));
        ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(items.iterator());
        
        iterator.next();
        iterator.remove(); // First remove should work
        iterator.remove(); // Second remove should throw IllegalStateException
    }

    // ========== Transformer Exception Tests ==========
    
    @Test(expected = RuntimeException.class)
    public void testExceptionTransformer() {
        String rootValue = "test";
        Transformer<String, String> exceptionTransformer = ExceptionTransformer.exceptionTransformer();
        ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(rootValue, exceptionTransformer);
        
        iterator.next(); // Should trigger transformer and throw RuntimeException
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullTransformerInIfTransformer() {
        String rootValue = "test";
        Transformer<String, String> ifTransformer = new IfTransformer<>(
            FalsePredicate.falsePredicate(), 
            null, // This null transformer should cause NPE
            null
        );
        ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(rootValue, ifTransformer);
        
        iterator.hasNext(); // Should trigger NPE
    }

    // ========== Concurrent Modification Tests ==========
    
    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModificationDuringIteration() {
        LinkedList<String> list = new LinkedList<>(Arrays.asList("item1"));
        ListIterator<String> listIterator = list.listIterator();
        ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(listIterator);
        
        // Modify the list while iterator is active
        list.add("item2");
        
        iterator.next(); // Should throw ConcurrentModificationException
    }

    // ========== Complex Transformer Tests ==========
    
    @Test
    public void testMapTransformerWithValidMapping() {
        String key = "testKey";
        String expectedValue = "testValue";
        Map<String, String> map = new HashMap<>();
        map.put(key, expectedValue);
        
        Transformer<String, String> mapTransformer = MapTransformer.mapTransformer(map);
        ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(key, mapTransformer);
        
        assertTrue("Should have next element", iterator.hasNext());
        // Note: The exact behavior depends on ObjectGraphIterator's internal logic
    }
    
    @Test
    public void testFactoryTransformer() {
        String rootValue = "root";
        String factoryValue = "created";
        Factory<String> factory = ConstantFactory.constantFactory(factoryValue);
        Transformer<String, String> factoryTransformer = new FactoryTransformer<>(factory);
        
        ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(rootValue, factoryTransformer);
        
        assertTrue("Should have elements", iterator.hasNext());
        // Verify it doesn't throw exceptions during iteration
        iterator.next();
    }

    // ========== Edge Cases ==========
    
    @Test
    public void testNullRootValue() {
        ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(null, null);
        // Should handle null root gracefully
        assertFalse("Null root should result in empty iteration", iterator.hasNext());
    }
    
    @Test
    public void testNestedIterators() {
        // Create a simple nested structure
        List<String> innerList = Arrays.asList("nested1", "nested2");
        List<Iterator<String>> outerList = Arrays.asList(innerList.iterator());
        
        ObjectGraphIterator<Iterator<String>> iterator = new ObjectGraphIterator<>(outerList.iterator());
        
        // This tests the iterator's ability to handle nested iterator structures
        assertTrue("Should handle nested iterators", iterator.hasNext());
    }

    // ========== State Management Tests ==========
    
    @Test
    public void testIteratorStateAfterExhaustion() {
        String singleValue = "test";
        ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(singleValue, null);
        
        // Exhaust the iterator
        iterator.next();
        
        assertFalse("Should be exhausted", iterator.hasNext());
        assertFalse("Should remain exhausted", iterator.hasNext());
    }
    
    @Test
    public void testMultipleHasNextCalls() {
        String value = "test";
        ObjectGraphIterator<String> iterator = new ObjectGraphIterator<>(value, null);
        
        // Multiple hasNext() calls should be idempotent
        assertTrue("First hasNext() should return true", iterator.hasNext());
        assertTrue("Second hasNext() should return true", iterator.hasNext());
        assertTrue("Third hasNext() should return true", iterator.hasNext());
        
        // Should still be able to get the value
        assertEquals("Should return the value", value, iterator.next());
    }

    // ========== Helper Methods for Complex Scenarios ==========
    
    /**
     * Creates a simple transformer that returns null for any input.
     * Useful for testing null handling.
     */
    private Transformer<Object, Object> createNullTransformer() {
        return ConstantTransformer.nullTransformer();
    }
    
    /**
     * Creates a list with known values for testing.
     */
    private List<String> createTestList() {
        return new ArrayList<>(Arrays.asList("first", "second", "third"));
    }
}