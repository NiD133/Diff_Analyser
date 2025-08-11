package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Test suite for ZippingIterator which provides interleaved iteration 
 * over multiple iterators (e.g., [1,2] + [A,B] = [1,A,2,B])
 */
public class ZippingIterator_ESTest {

    @Test
    public void testNestedZippingIterators_ShouldNotBeEqual() {
        // Given: An empty list and its iterator
        List<Integer> emptyList = new LinkedList<>();
        Iterator<Integer> emptyIterator = emptyList.iterator();
        
        // When: Creating nested ZippingIterators
        ZippingIterator<Integer> innerZipper = new ZippingIterator<>(emptyIterator, emptyIterator);
        ZippingIterator<Integer> outerZipper = new ZippingIterator<>(innerZipper);
        
        // Then: They should not be equal (different objects)
        assertFalse("Nested ZippingIterators should not be equal", 
                   outerZipper.equals(innerZipper));
    }

    @Test
    public void testNext_WithNullElement_ShouldReturnNull() {
        // Given: A list containing null
        List<Integer> listWithNull = new LinkedList<>();
        listWithNull.add(null);
        Iterator<Integer> iterator = listWithNull.iterator();
        
        // When: Creating ZippingIterator with same iterator twice
        ZippingIterator<Object> zipper = new ZippingIterator<>(iterator, iterator);
        
        // Then: Should return the null element
        Object result = zipper.next();
        assertNull("Should return null element", result);
    }

    @Test
    public void testNext_WithNonNullElement_ShouldReturnElement() {
        // Given: A list with one object
        List<Object> listWithObject = new LinkedList<>();
        Object testObject = new Object();
        listWithObject.add(testObject);
        Iterator<Object> iterator = listWithObject.iterator();
        
        // When: Creating ZippingIterator
        ZippingIterator<Object> zipper = new ZippingIterator<>(iterator, iterator);
        
        // Then: Should return the same object
        Object result = zipper.next();
        assertSame("Should return the same object", testObject, result);
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testRemove_AfterConcurrentModification_ShouldThrowException() {
        // Given: A list with one element
        List<Integer> list = new LinkedList<>();
        Integer testValue = -550;
        list.add(testValue);
        Iterator<Integer> iterator = list.iterator();
        
        // When: Creating ZippingIterator and modifying list concurrently
        ZippingIterator<Object> zipper = new ZippingIterator<>(iterator, iterator, iterator);
        Object retrievedValue = zipper.next();
        list.remove(retrievedValue); // Concurrent modification
        
        // Then: Should throw ConcurrentModificationException
        zipper.remove();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testNext_AfterConcurrentModification_ShouldThrowException() {
        // Given: An empty list and its iterator
        List<Object> list = new LinkedList<>();
        Iterator<Object> iterator = list.listIterator();
        
        // When: Modifying list after creating iterator
        list.add(null); // Concurrent modification
        ZippingIterator<Object> zipper = new ZippingIterator<>(iterator, iterator, iterator);
        
        // Then: Should throw ConcurrentModificationException
        zipper.next();
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_WithNullIteratorInArray_ShouldThrowException() {
        // Given: An array with null iterator
        @SuppressWarnings("unchecked")
        Iterator<Integer>[] iteratorArray = new Iterator[1];
        iteratorArray[0] = null;
        
        // When: Creating ZippingIterator with null iterator
        // Then: Should throw NullPointerException
        new ZippingIterator<>(iteratorArray);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_WithThreeNullIterators_ShouldThrowException() {
        // When: Creating ZippingIterator with null iterators
        // Then: Should throw NullPointerException
        new ZippingIterator<>(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_WithTwoNullIterators_ShouldThrowException() {
        // When: Creating ZippingIterator with null iterators
        // Then: Should throw NullPointerException
        new ZippingIterator<>(null, null);
    }

    @Test
    public void testHasNext_WithElements_ShouldReturnTrueConsistently() {
        // Given: A list with one element
        List<String> list = Arrays.asList("test");
        Iterator<String> iterator = list.iterator();
        
        // When: Creating ZippingIterator
        ZippingIterator<String> zipper = new ZippingIterator<>(iterator, iterator);
        
        // Then: hasNext() should return true consistently
        assertTrue("First hasNext() call should return true", zipper.hasNext());
        assertTrue("Second hasNext() call should return true", zipper.hasNext());
    }

    @Test
    public void testHasNext_WithEmptyIterators_ShouldReturnFalse() {
        // Given: Empty lists
        List<Integer> emptyList = new LinkedList<>();
        Iterator<Integer> emptyIterator = emptyList.iterator();
        
        // When: Creating ZippingIterator with empty iterators
        ZippingIterator<Integer> zipper = new ZippingIterator<>(emptyIterator, emptyIterator, emptyIterator);
        
        // Then: Should return false
        assertFalse("hasNext() should return false for empty iterators", zipper.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testRemove_WithoutCallingNext_ShouldThrowException() {
        // Given: Empty iterators
        List<Integer> emptyList = new LinkedList<>();
        Iterator<Integer> emptyIterator = emptyList.iterator();
        
        // When: Creating ZippingIterator and calling remove without next
        ZippingIterator<Object> zipper = new ZippingIterator<>(emptyIterator, emptyIterator, emptyIterator);
        
        // Then: Should throw IllegalStateException
        zipper.remove();
    }

    @Test
    public void testRemove_AfterNext_ShouldSucceed() {
        // Given: A list with one element
        List<Integer> list = new LinkedList<>();
        Integer testValue = -550;
        list.add(testValue);
        Iterator<Integer> iterator = list.iterator();
        
        // When: Creating ZippingIterator, calling next, then remove
        ZippingIterator<Object> zipper = new ZippingIterator<>(iterator, iterator, iterator);
        zipper.next();
        
        // Then: Remove should succeed without exception
        zipper.remove(); // Should not throw exception
    }

    @Test(expected = NoSuchElementException.class)
    public void testNext_WithEmptyIterators_ShouldThrowException() {
        // Given: Empty iterators
        List<Integer> emptyList = new LinkedList<>();
        Iterator<Integer> emptyIterator = emptyList.iterator();
        
        // When: Creating ZippingIterator with empty iterators
        ZippingIterator<Object> zipper = new ZippingIterator<>(emptyIterator, emptyIterator, emptyIterator);
        
        // Then: Should throw NoSuchElementException
        zipper.next();
    }
}