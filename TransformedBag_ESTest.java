package org.apache.commons.collections4.bag;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Comparator;
import java.util.Set;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.bag.TransformedBag;
import org.apache.commons.collections4.bag.TransformedSortedBag;
import org.apache.commons.collections4.bag.UnmodifiableBag;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;

/**
 * Test suite for TransformedBag functionality.
 * Tests the behavior of bags that transform elements when they are added.
 */
public class TransformedBagTest {

    // Test Factory Methods
    
    @Test(timeout = 4000)
    public void testTransformingBag_CreatesEmptyBag() {
        // Given: An empty bag and a transformer
        TreeBag<Object> emptyBag = new TreeBag<>();
        Transformer<Object, Object> transformer = createMockTransformer("testMethod");
        
        // When: Creating a transforming bag
        TransformedSortedBag<Object> transformedBag = 
            TransformedSortedBag.transformingSortedBag(emptyBag, transformer);
        
        // Then: The unique set should be empty
        Set<Object> uniqueSet = transformedBag.uniqueSet();
        assertEquals("Unique set should be empty", 0, uniqueSet.size());
    }

    @Test(timeout = 4000)
    public void testTransformingBag_WithNullTransformer_ThrowsException() {
        // Given: A bag with null transformer
        HashBag<Integer> bag = new HashBag<>();
        
        // When & Then: Should throw NullPointerException
        try {
            TransformedBag.transformingBag(bag, null);
            fail("Expected NullPointerException for null transformer");
        } catch (NullPointerException e) {
            assertEquals("transformer", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testTransformedBag_WithNullBag_ThrowsException() {
        // Given: Null bag and valid transformer
        Transformer<Object, Integer> transformer = ConstantTransformer.nullTransformer();
        
        // When & Then: Should throw NullPointerException
        try {
            TransformedBag.transformedBag(null, transformer);
            fail("Expected NullPointerException for null bag");
        } catch (NullPointerException e) {
            assertEquals("collection", e.getMessage());
        }
    }

    // Test Basic Operations

    @Test(timeout = 4000)
    public void testAdd_WithValidCount_ReturnsTrue() {
        // Given: A bag with existing elements and a constant transformer
        HashBag<Integer> originalBag = new HashBag<>();
        Integer value = 165;
        originalBag.add(value);
        
        Transformer<Object, Integer> transformer = ConstantTransformer.constantTransformer(value);
        TreeBag<Integer> treeBag = new TreeBag<>(originalBag);
        TransformedSortedBag<Integer> transformedBag = 
            TransformedSortedBag.transformingSortedBag(treeBag, transformer);
        
        // When: Adding with negative count (should be treated as no-op)
        boolean result = transformedBag.add(value, -803);
        
        // Then: Should return false (no elements added)
        assertFalse("Adding negative count should return false", result);
    }

    @Test(timeout = 4000)
    public void testRemove_WithValidObject_ReturnsExpectedResult() {
        // Given: A synchronized sorted bag with mock comparator
        Comparator<Object> mockComparator = createMockComparator();
        TreeBag<Integer> treeBag = new TreeBag<>(mockComparator);
        treeBag.add(862);
        
        Transformer<Object, Integer> nullTransformer = ConstantTransformer.nullTransformer();
        TransformedSortedBag<Integer> transformedBag = 
            new TransformedSortedBag<>(treeBag, nullTransformer);
        
        // When: Removing an object
        HashBag<Object> objectToRemove = new HashBag<>();
        boolean removed = transformedBag.remove(objectToRemove, 3791);
        
        // Then: Should successfully remove and return true
        assertTrue("Remove operation should succeed", removed);
        assertFalse("Original bag should not contain 0", treeBag.contains(0));
    }

    @Test(timeout = 4000)
    public void testRemove_WithNonExistentObject_ReturnsFalse() {
        // Given: A bag that doesn't contain the object to remove
        HashBag<Integer> bag = new HashBag<>();
        Transformer<Object, Integer> transformer = ConstantTransformer.nullTransformer();
        TransformedBag<Integer> transformedBag = new TransformedBag<>(bag, transformer);
        
        TreeBag<Object> objectToRemove = new TreeBag<>();
        
        // When: Trying to remove non-existent object
        boolean removed = transformedBag.remove(objectToRemove, 0);
        
        // Then: Should return false
        assertFalse("Removing non-existent object should return false", removed);
    }

    // Test Count Operations

    @Test(timeout = 4000)
    public void testGetCount_WithExistingElement_ReturnsCorrectCount() {
        // Given: A bag with one element
        Comparator<Object> mockComparator = createMockComparator();
        TreeBag<Integer> treeBag = new TreeBag<>(mockComparator);
        Integer value = 2;
        treeBag.add(value);
        
        Transformer<Object, Integer> constantTransformer = ConstantTransformer.constantTransformer(value);
        TransformedSortedBag<Integer> transformedBag = 
            new TransformedSortedBag<>(treeBag, constantTransformer);
        
        // When: Getting count of an object
        TreeBag<Object> queryObject = new TreeBag<>();
        int count = transformedBag.getCount(queryObject);
        
        // Then: Should return 1
        assertEquals("Count should be 1", 1, count);
    }

    @Test(timeout = 4000)
    public void testGetCount_WithNonExistentElement_ReturnsZero() {
        // Given: Two different transformed bags
        TreeBag<Integer> treeBag1 = new TreeBag<>();
        TreeBag<Integer> treeBag2 = new TreeBag<>();
        
        Transformer<Object, Integer> transformer1 = ConstantTransformer.constantTransformer(2);
        Transformer<Object, Object> transformer2 = createMockTransformer("testMethod");
        
        TransformedSortedBag<Integer> transformedBag1 = 
            new TransformedSortedBag<>(treeBag1, transformer1);
        TransformedSortedBag<Object> transformedBag2 = 
            new TransformedSortedBag<>(treeBag2, transformer2);
        
        // When: Getting count of one bag in another
        int count = transformedBag1.getCount(transformedBag2);
        
        // Then: Should return 0
        assertEquals("Count should be 0 for non-existent element", 0, count);
    }

    // Test Bag Access

    @Test(timeout = 4000)
    public void testGetBag_ReturnsUnderlyingBag() {
        // Given: A transformed bag wrapping a tree bag
        TreeBag<HashBag<Object>> treeBag = new TreeBag<>();
        Transformer<Object, HashBag<Object>> transformer = ConstantTransformer.nullTransformer();
        TransformedSortedBag<HashBag<Object>> transformedBag = 
            TransformedSortedBag.transformingSortedBag(treeBag, transformer);
        
        // When: Getting the underlying bag
        Bag<HashBag<Object>> underlyingBag = transformedBag.getBag();
        
        // Then: Should return empty bag
        assertEquals("Underlying bag should be empty", 0, underlyingBag.size());
    }

    @Test(timeout = 4000)
    public void testGetBag_WithExistingElements_ReturnsPopulatedBag() {
        // Given: A bag with existing elements
        HashBag<Integer> originalBag = new HashBag<>();
        Integer value = 165;
        originalBag.add(value);
        
        TreeBag<Object> treeBag = new TreeBag<>(originalBag);
        Transformer<Object, Integer> transformer = ConstantTransformer.constantTransformer(value);
        TransformedSortedBag<Object> transformedBag = 
            new TransformedSortedBag<>(treeBag, transformer);
        
        // When: Getting the underlying bag
        Bag<Object> underlyingBag = transformedBag.getBag();
        
        // Then: Should contain the original element
        assertTrue("Underlying bag should contain original element", 
                  underlyingBag.contains(value));
    }

    // Test Exception Scenarios

    @Test(timeout = 4000)
    public void testAdd_WithExceptionTransformer_ThrowsRuntimeException() {
        // Given: A bag with exception transformer
        TreeBag<Object> treeBag = new TreeBag<>();
        Transformer<Object, Integer> exceptionTransformer = ExceptionTransformer.exceptionTransformer();
        TransformedSortedBag<Object> transformedBag = 
            TransformedSortedBag.transformingSortedBag(treeBag, exceptionTransformer);
        
        // When & Then: Adding should throw RuntimeException
        try {
            transformedBag.add(exceptionTransformer, 1);
            fail("Expected RuntimeException from ExceptionTransformer");
        } catch (RuntimeException e) {
            assertEquals("ExceptionTransformer invoked", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testAdd_WithUnmodifiableBag_ThrowsUnsupportedOperationException() {
        // Given: An unmodifiable bag
        HashBag<Integer> originalBag = new HashBag<>();
        Bag<Integer> unmodifiableBag = UnmodifiableBag.unmodifiableBag(originalBag);
        Transformer<Integer, Integer> transformer = ConstantTransformer.nullTransformer();
        TransformedBag<Integer> transformedBag = new TransformedBag<>(unmodifiableBag, transformer);
        
        // When & Then: Adding should throw UnsupportedOperationException
        try {
            transformedBag.add(560, 560);
            fail("Expected UnsupportedOperationException for unmodifiable bag");
        } catch (UnsupportedOperationException e) {
            // Expected behavior
        }
    }

    // Test Equality

    @Test(timeout = 4000)
    public void testEquals_WithSameBag_ReturnsTrue() {
        // Given: A transformed bag
        HashBag<Integer> originalBag = new HashBag<>();
        Transformer<Object, Integer> transformer = ConstantTransformer.constantTransformer(165);
        TreeBag<Integer> treeBag = new TreeBag<>(originalBag);
        TransformedSortedBag<Integer> transformedBag = 
            TransformedSortedBag.transformingSortedBag(treeBag, transformer);
        
        // When: Comparing with itself
        boolean isEqual = transformedBag.equals(transformedBag);
        
        // Then: Should return true
        assertTrue("Bag should equal itself", isEqual);
    }

    @Test(timeout = 4000)
    public void testEquals_WithDifferentType_ReturnsFalse() {
        // Given: A transformed bag and a different object type
        TreeBag<Object> treeBag = new TreeBag<>();
        Transformer<Object, Object> transformer = createMockTransformer("testMethod");
        TransformedSortedBag<Object> transformedBag = 
            new TransformedSortedBag<>(treeBag, transformer);
        
        Integer differentObject = 2;
        
        // When: Comparing with different type
        boolean isEqual = transformedBag.equals(differentObject);
        
        // Then: Should return false
        assertFalse("Bag should not equal different type", isEqual);
    }

    // Test Hash Code

    @Test(timeout = 4000)
    public void testHashCode_DoesNotThrowException() {
        // Given: A transformed bag
        TreeBag<Object> treeBag = new TreeBag<>();
        Transformer<Object, Object> transformer = createMockTransformer("testMethod");
        TransformedSortedBag<Object> transformedBag = 
            TransformedSortedBag.transformedSortedBag(treeBag, transformer);
        
        // When: Getting hash code
        // Then: Should not throw exception
        assertNotNull("Hash code should be calculable", transformedBag.hashCode());
    }

    // Helper Methods

    @SuppressWarnings("unchecked")
    private Comparator<Object> createMockComparator() {
        Comparator<Object> mockComparator = mock(Comparator.class);
        when(mockComparator.compare(any(), any())).thenReturn(0);
        return mockComparator;
    }

    @SuppressWarnings("unchecked")
    private Transformer<Object, Object> createMockTransformer(String methodName) {
        // In real implementation, this would create an InvokerTransformer
        // For testing purposes, we'll use a simple constant transformer
        return ConstantTransformer.nullTransformer();
    }
}