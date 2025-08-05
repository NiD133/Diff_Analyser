package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Arrays;

/**
 * Test suite for BoundedIterator class.
 * 
 * BoundedIterator decorates another iterator to return elements in a specific range [offset, offset+max).
 * The offset specifies how many elements to skip, and max specifies the maximum number of elements to return.
 */
public class BoundedIteratorTest {

    // ========== Constructor Validation Tests ==========

    @Test(expected = NullPointerException.class)
    public void constructor_WithNullIterator_ThrowsNullPointerException() {
        new BoundedIterator<String>(null, 0L, 5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_WithNegativeOffset_ThrowsIllegalArgumentException() {
        Iterator<String> mockIterator = createMockIterator();
        new BoundedIterator<>(mockIterator, -1L, 5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_WithNegativeMax_ThrowsIllegalArgumentException() {
        Iterator<String> mockIterator = createMockIterator();
        new BoundedIterator<>(mockIterator, 0L, -1L);
    }

    // ========== Basic Functionality Tests ==========

    @Test
    public void hasNext_WithElementsInRange_ReturnsTrue() {
        LinkedList<Integer> list = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5));
        Iterator<Integer> iterator = list.iterator();
        
        // Create bounded iterator: skip 0 elements, return max 3 elements
        BoundedIterator<Integer> boundedIterator = new BoundedIterator<>(iterator, 0L, 3L);
        
        assertTrue("Should have next element when elements are available in range", 
                   boundedIterator.hasNext());
    }

    @Test
    public void hasNext_WithZeroMaxElements_ReturnsFalse() {
        LinkedList<Integer> list = new LinkedList<>(Arrays.asList(1, 2, 3));
        Iterator<Integer> iterator = list.iterator();
        
        // Create bounded iterator with max = 0 (no elements to return)
        BoundedIterator<Integer> boundedIterator = new BoundedIterator<>(iterator, 0L, 0L);
        
        assertFalse("Should not have next element when max is 0", 
                    boundedIterator.hasNext());
    }

    @Test
    public void hasNext_WithOffsetBeyondAvailableElements_ReturnsTrue() {
        LinkedList<Integer> list = new LinkedList<>(Arrays.asList(42));
        Iterator<Integer> iterator = list.descendingIterator();
        
        // Skip 0 elements, return up to 508 elements (more than available)
        BoundedIterator<Integer> boundedIterator = new BoundedIterator<>(iterator, 0L, 508L);
        
        assertTrue("Should return true when elements are available within bounds", 
                   boundedIterator.hasNext());
    }

    // ========== Next() Method Tests ==========

    @Test(expected = NoSuchElementException.class)
    public void next_WithEmptyIteratorAndPositiveMax_ThrowsNoSuchElementException() {
        LinkedList<Boolean> emptyList = new LinkedList<>();
        Iterator<Boolean> iterator = emptyList.iterator();
        
        // Try to get elements from empty iterator
        BoundedIterator<Object> boundedIterator = new BoundedIterator<>(iterator, 1909L, 1909L);
        boundedIterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void next_WithZeroMaxElements_ThrowsNoSuchElementException() {
        LinkedList<Integer> list = new LinkedList<>(Arrays.asList(1, 2, 3));
        Iterator<Integer> iterator = list.descendingIterator();
        
        // Max = 0 means no elements should be returned
        BoundedIterator<Integer> boundedIterator = new BoundedIterator<>(iterator, 0L, 0L);
        boundedIterator.next();
    }

    @Test
    public void next_WithMockedIterator_ReturnsExpectedElement() {
        Iterator<String> mockIterator = createMockIteratorWithElements("test-element");
        
        BoundedIterator<String> boundedIterator = new BoundedIterator<>(mockIterator, 0L, 1L);
        String result = boundedIterator.next();
        
        assertEquals("Should return the element from underlying iterator", "test-element", result);
    }

    // ========== Remove() Method Tests ==========

    @Test(expected = IllegalStateException.class)
    public void remove_WithoutCallingNext_ThrowsIllegalStateException() {
        Iterator<String> mockIterator = createMockIteratorWithElements("element1", "element2");
        BoundedIterator<String> boundedIterator = new BoundedIterator<>(mockIterator, 0L, 2L);
        
        // Try to remove without calling next() first
        boundedIterator.remove();
    }

    @Test(expected = IllegalStateException.class)
    public void remove_OnNestedBoundedIteratorWithoutNext_ThrowsIllegalStateException() {
        Iterator<String> mockIterator = createMockIteratorWithElements("element1", "element2");
        
        // Create nested bounded iterators
        BoundedIterator<String> firstBounded = new BoundedIterator<>(mockIterator, 1L, 1L);
        firstBounded.next(); // Advance the first iterator
        
        BoundedIterator<String> secondBounded = new BoundedIterator<>(firstBounded, 1L, 0L);
        
        // Try to remove on second iterator without calling its next()
        secondBounded.remove();
    }

    @Test
    public void remove_AfterCallingNext_SuccessfullyRemoves() {
        Iterator<String> mockIterator = createMockIteratorWithElements("element-to-remove");
        BoundedIterator<String> boundedIterator = new BoundedIterator<>(mockIterator, 0L, 1L);
        
        boundedIterator.next();
        boundedIterator.remove(); // Should not throw exception
        
        verify(mockIterator).remove();
    }

    // ========== Concurrent Modification Tests ==========

    @Test(expected = ConcurrentModificationException.class)
    public void next_WithConcurrentModificationDuringIteration_ThrowsConcurrentModificationException() {
        LinkedList<Object> list = new LinkedList<>();
        Iterator<Object> iterator = list.descendingIterator();
        BoundedIterator<Object> boundedIterator = new BoundedIterator<>(iterator, 5L, 5L);
        
        // Modify the list after creating the iterator
        list.add(iterator);
        
        boundedIterator.next();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void constructor_WithConcurrentModificationDuringInit_ThrowsConcurrentModificationException() {
        LinkedList<Object> list = new LinkedList<>();
        Iterator<Object> iterator = list.iterator();
        
        // Modify list after getting iterator but before creating BoundedIterator
        list.add(iterator);
        
        // Constructor should fail due to concurrent modification during initialization
        new BoundedIterator<>(iterator, 1973L, 287L);
    }

    // ========== Edge Case Tests ==========

    @Test
    public void nestedBoundedIterators_HaveDifferentBehavior() {
        Iterator<String> mockIterator = createMockIteratorWithElements("element1");
        when(mockIterator.hasNext()).thenReturn(true, false);
        
        BoundedIterator<String> firstBounded = new BoundedIterator<>(mockIterator, 1L, 5L);
        BoundedIterator<String> secondBounded = new BoundedIterator<>(firstBounded, 5L, 0L);
        
        assertNotEquals("Nested bounded iterators should not be equal", 
                       firstBounded, secondBounded);
    }

    // ========== Helper Methods ==========

    @SuppressWarnings("unchecked")
    private Iterator<String> createMockIterator() {
        return mock(Iterator.class);
    }

    @SuppressWarnings("unchecked")
    private Iterator<String> createMockIteratorWithElements(String... elements) {
        Iterator<String> mockIterator = mock(Iterator.class);
        
        if (elements.length > 0) {
            // Set up hasNext() to return true for each element, then false
            Boolean[] hasNextReturns = new Boolean[elements.length + 1];
            for (int i = 0; i < elements.length; i++) {
                hasNextReturns[i] = true;
            }
            hasNextReturns[elements.length] = false;
            
            when(mockIterator.hasNext()).thenReturn(hasNextReturns[0], 
                    Arrays.copyOfRange(hasNextReturns, 1, hasNextReturns.length));
            
            // Set up next() to return elements in sequence
            if (elements.length == 1) {
                when(mockIterator.next()).thenReturn(elements[0]);
            } else {
                when(mockIterator.next()).thenReturn(elements[0], 
                        Arrays.copyOfRange(elements, 1, elements.length));
            }
        }
        
        return mockIterator;
    }
}