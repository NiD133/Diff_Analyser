package org.apache.commons.collections4.iterators;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.collections4.iterators.LoopingListIterator;

/**
 * Test suite for LoopingListIterator functionality.
 * Tests basic operations, edge cases, and error conditions.
 */
public class LoopingListIteratorTest {

    private LinkedList<Integer> singleElementList;
    private LinkedList<Integer> multiElementList;
    private LinkedList<Object> emptyList;

    @Before
    public void setUp() {
        singleElementList = new LinkedList<>();
        singleElementList.add(42);
        
        multiElementList = new LinkedList<>();
        multiElementList.add(1);
        multiElementList.add(2);
        
        emptyList = new LinkedList<>();
    }

    // ========== Constructor Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_WithNullList_ThrowsNullPointerException() {
        new LoopingListIterator<>(null);
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testConstructor_WithModifiedSubList_ThrowsConcurrentModificationException() {
        LinkedList<Integer> list = new LinkedList<>();
        List<Integer> subList = list.subList(0, 0);
        list.add(1); // Modify original list after creating sublist
        new LoopingListIterator<>(subList);
    }

    // ========== Basic Navigation Tests ==========
    
    @Test
    public void testNext_WithSingleElement_ReturnsElement() {
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(singleElementList);
        
        Integer result = iterator.next();
        
        assertEquals(Integer.valueOf(42), result);
    }

    @Test
    public void testNext_WithNullElement_ReturnsNull() {
        LinkedList<Integer> listWithNull = new LinkedList<>();
        listWithNull.add(null);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(listWithNull);
        
        Integer result = iterator.next();
        
        assertNull(result);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNext_WithEmptyList_ThrowsNoSuchElementException() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);
        iterator.next();
    }

    @Test
    public void testPrevious_WithSingleElement_ReturnsElement() {
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(singleElementList);
        
        Integer result = iterator.previous();
        
        assertEquals(Integer.valueOf(42), result);
    }

    @Test
    public void testPrevious_WithNullElement_ReturnsNull() {
        LinkedList<Object> listWithNull = new LinkedList<>();
        listWithNull.add(null);
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(listWithNull);
        
        Object result = iterator.previous();
        
        assertNull(result);
    }

    @Test(expected = NoSuchElementException.class)
    public void testPrevious_WithEmptyList_ThrowsNoSuchElementException() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);
        iterator.previous();
    }

    // ========== HasNext/HasPrevious Tests ==========
    
    @Test
    public void testHasNext_WithEmptyList_ReturnsFalse() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);
        
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNext_WithNonEmptyList_ReturnsTrue() {
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(singleElementList);
        
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testHasNext_AfterAddingElement_ReturnsTrue() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);
        assertFalse(iterator.hasNext());
        
        iterator.add("test");
        
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testHasPrevious_WithEmptyList_ReturnsFalse() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);
        
        assertFalse(iterator.hasPrevious());
    }

    @Test
    public void testHasPrevious_WithNonEmptyList_ReturnsTrue() {
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(singleElementList);
        
        assertTrue(iterator.hasPrevious());
    }

    // ========== Index Tests ==========
    
    @Test
    public void testNextIndex_WithMultiElementList_ReturnsCorrectIndex() {
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(multiElementList);
        
        int index = iterator.nextIndex();
        
        assertEquals(0, index);
    }

    @Test
    public void testNextIndex_AfterAddingElement_ReturnsCorrectIndex() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);
        iterator.add("test");
        
        int index = iterator.nextIndex();
        
        assertEquals(0, index);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextIndex_WithEmptyList_ThrowsNoSuchElementException() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);
        iterator.nextIndex();
    }

    @Test
    public void testPreviousIndex_WithMultiElementList_ReturnsCorrectIndex() {
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(multiElementList);
        
        int index = iterator.previousIndex();
        
        assertEquals(1, index); // Points to last element (index 1)
    }

    @Test
    public void testPreviousIndex_AfterAddingElement_ReturnsCorrectIndex() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);
        iterator.add("test");
        
        int index = iterator.previousIndex();
        
        assertEquals(0, index);
    }

    @Test(expected = NoSuchElementException.class)
    public void testPreviousIndex_WithEmptyList_ThrowsNoSuchElementException() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);
        iterator.previousIndex();
    }

    // ========== Modification Tests ==========
    
    @Test
    public void testAdd_ToEmptyList_IncreasesSize() {
        LoopingListIterator<String> iterator = new LoopingListIterator<>(new LinkedList<>());
        
        iterator.add("test");
        
        assertEquals(1, iterator.size());
    }

    @Test
    public void testSet_AfterPrevious_UpdatesElement() {
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(singleElementList);
        iterator.previous();
        
        iterator.set(100);
        
        assertTrue(iterator.hasPrevious());
        assertEquals(Integer.valueOf(100), singleElementList.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testSet_WithoutPreviousNavigation_ThrowsIllegalStateException() {
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(singleElementList);
        iterator.set(100);
    }

    @Test
    public void testRemove_AfterNext_RemovesElement() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(new LinkedList<>());
        iterator.add("test");
        iterator.next();
        
        iterator.remove();
        
        assertFalse(iterator.hasPrevious());
        assertEquals(0, iterator.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testRemove_WithoutNavigation_ThrowsIllegalStateException() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);
        iterator.remove();
    }

    // ========== Utility Tests ==========
    
    @Test
    public void testSize_WithEmptyList_ReturnsZero() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);
        
        assertEquals(0, iterator.size());
    }

    @Test
    public void testSize_WithSingleElement_ReturnsOne() {
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(singleElementList);
        
        assertEquals(1, iterator.size());
    }

    @Test
    public void testReset_WithNonEmptyList_ResetsIterator() {
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);
        iterator.add("test");
        
        iterator.reset();
        
        assertTrue(iterator.hasNext());
    }

    // ========== Concurrent Modification Tests ==========
    
    @Test(expected = ConcurrentModificationException.class)
    public void testNext_AfterConcurrentModification_ThrowsConcurrentModificationException() {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        list.add("concurrent modification"); // Modify list after creating iterator
        
        iterator.next();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testPrevious_AfterConcurrentModification_ThrowsConcurrentModificationException() {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        list.add("concurrent modification");
        
        iterator.previous();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testSet_AfterConcurrentModification_ThrowsConcurrentModificationException() {
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(singleElementList);
        Integer element = iterator.previous();
        singleElementList.removeIf(x -> true); // Concurrent modification
        
        iterator.set(element);
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testAdd_AfterConcurrentModification_ThrowsConcurrentModificationException() {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        list.add("concurrent modification");
        
        iterator.add("test");
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testRemove_AfterConcurrentModification_ThrowsConcurrentModificationException() {
        LinkedList<Object> list = new LinkedList<>();
        LoopingListIterator<Object> iterator = new LoopingListIterator<>(list);
        list.add("concurrent modification");
        
        iterator.remove();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testReset_AfterConcurrentModification_ThrowsConcurrentModificationException() {
        LinkedList<Integer> list = new LinkedList<>();
        List<Integer> subList = list.subList(0, 0);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(subList);
        list.add(1); // Modify original list
        
        iterator.reset();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testHasPrevious_AfterConcurrentModification_ThrowsConcurrentModificationException() {
        LinkedList<Integer> list = new LinkedList<>();
        List<Integer> subList = list.subList(0, 0);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(subList);
        list.add(1);
        
        iterator.hasPrevious();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testNextIndex_AfterConcurrentModification_ThrowsConcurrentModificationException() {
        LinkedList<Integer> list = new LinkedList<>();
        List<Integer> subList = list.subList(0, 0);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(subList);
        list.add(1);
        
        iterator.nextIndex();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testPreviousIndex_AfterConcurrentModification_ThrowsConcurrentModificationException() {
        LinkedList<Integer> list = new LinkedList<>();
        List<Integer> subList = list.subList(0, 0);
        LoopingListIterator<Integer> iterator = new LoopingListIterator<>(subList);
        list.add(1);
        
        iterator.previousIndex();
    }
}