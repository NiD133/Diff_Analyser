package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.UniquePredicate;

/**
 * Test suite for FilterListIterator functionality.
 * Tests the behavior of filtering list iterators with various predicates.
 */
public class FilterListIteratorTest {

    @Test
    public void testConstructorWithNoPredicate() {
        FilterListIterator<String> filterIterator = new FilterListIterator<>();
        
        assertNull(filterIterator.getListIterator());
        assertNull(filterIterator.getPredicate());
        assertEquals(0, filterIterator.nextIndex());
        assertEquals(-1, filterIterator.previousIndex());
        assertFalse(filterIterator.hasNext());
        assertFalse(filterIterator.hasPrevious());
    }

    @Test
    public void testConstructorWithPredicate() {
        Predicate<String> notNullPredicate = NotNullPredicate.notNullPredicate();
        FilterListIterator<String> filterIterator = new FilterListIterator<>(notNullPredicate);
        
        assertNull(filterIterator.getListIterator());
        assertEquals(notNullPredicate, filterIterator.getPredicate());
    }

    @Test
    public void testConstructorWithIteratorAndPredicate() {
        LinkedList<String> list = new LinkedList<>();
        list.add("hello");
        list.add(null);
        list.add("world");
        
        ListIterator<String> baseIterator = list.listIterator();
        Predicate<String> notNullPredicate = NotNullPredicate.notNullPredicate();
        FilterListIterator<String> filterIterator = new FilterListIterator<>(baseIterator, notNullPredicate);
        
        assertEquals(baseIterator, filterIterator.getListIterator());
        assertEquals(notNullPredicate, filterIterator.getPredicate());
    }

    @Test
    public void testFilteringWithNullPredicate() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(null);
        list.add(42);
        list.add(null);
        
        ListIterator<Integer> baseIterator = list.listIterator();
        Predicate<Integer> nullPredicate = NullPredicate.nullPredicate();
        FilterListIterator<Integer> filterIterator = new FilterListIterator<>(baseIterator, nullPredicate);
        
        assertTrue(filterIterator.hasNext());
        assertNull(filterIterator.next());
        assertTrue(filterIterator.hasNext());
        assertNull(filterIterator.next());
        assertFalse(filterIterator.hasNext());
    }

    @Test
    public void testFilteringWithNotNullPredicate() {
        LinkedList<String> list = new LinkedList<>();
        list.add("first");
        list.add(null);
        list.add("second");
        
        ListIterator<String> baseIterator = list.listIterator();
        Predicate<String> notNullPredicate = NotNullPredicate.notNullPredicate();
        FilterListIterator<String> filterIterator = new FilterListIterator<>(baseIterator, notNullPredicate);
        
        assertTrue(filterIterator.hasNext());
        assertEquals("first", filterIterator.next());
        assertTrue(filterIterator.hasNext());
        assertEquals("second", filterIterator.next());
        assertFalse(filterIterator.hasNext());
    }

    @Test
    public void testBackwardIteration() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(null);
        list.add(1);
        list.add(null);
        
        ListIterator<Integer> baseIterator = list.listIterator(list.size());
        Predicate<Integer> nullPredicate = NullPredicate.nullPredicate();
        FilterListIterator<Integer> filterIterator = new FilterListIterator<>(baseIterator, nullPredicate);
        
        assertTrue(filterIterator.hasPrevious());
        assertNull(filterIterator.previous());
        assertTrue(filterIterator.hasPrevious());
        assertNull(filterIterator.previous());
        assertFalse(filterIterator.hasPrevious());
    }

    @Test
    public void testIndexTracking() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(null);
        
        ListIterator<Integer> baseIterator = list.listIterator();
        Predicate<Integer> nullPredicate = NullPredicate.nullPredicate();
        FilterListIterator<Integer> filterIterator = new FilterListIterator<>(baseIterator, nullPredicate);
        
        assertEquals(0, filterIterator.nextIndex());
        assertEquals(-1, filterIterator.previousIndex());
        
        filterIterator.next();
        assertEquals(1, filterIterator.nextIndex());
        assertEquals(0, filterIterator.previousIndex());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextWhenNoElements() {
        FilterListIterator<String> filterIterator = new FilterListIterator<>();
        filterIterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testPreviousWhenNoElements() {
        FilterListIterator<String> filterIterator = new FilterListIterator<>();
        filterIterator.previous();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextWhenNoMatchingElements() {
        LinkedList<String> list = new LinkedList<>();
        list.add("test");
        
        ListIterator<String> baseIterator = list.listIterator();
        Predicate<String> nullPredicate = NullPredicate.nullPredicate();
        FilterListIterator<String> filterIterator = new FilterListIterator<>(baseIterator, nullPredicate);
        
        filterIterator.next(); // Should throw since "test" doesn't match null predicate
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddNotSupported() {
        FilterListIterator<String> filterIterator = new FilterListIterator<>();
        filterIterator.add("test");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveNotSupported() {
        FilterListIterator<String> filterIterator = new FilterListIterator<>();
        filterIterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNotSupported() {
        FilterListIterator<String> filterIterator = new FilterListIterator<>();
        filterIterator.set("test");
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModificationDetection() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(42);
        
        ListIterator<Integer> baseIterator = list.listIterator();
        Predicate<Integer> notNullPredicate = NotNullPredicate.notNullPredicate();
        FilterListIterator<Integer> filterIterator = new FilterListIterator<>(baseIterator, notNullPredicate);
        
        // Modify the list after creating the iterator
        list.add(100);
        
        // This should throw ConcurrentModificationException
        filterIterator.hasNext();
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionPredicateThrowsException() {
        LinkedList<String> list = new LinkedList<>();
        list.add("test");
        
        ListIterator<String> baseIterator = list.listIterator();
        Predicate<String> exceptionPredicate = ExceptionPredicate.exceptionPredicate();
        FilterListIterator<String> filterIterator = new FilterListIterator<>(baseIterator, exceptionPredicate);
        
        // This should throw RuntimeException from ExceptionPredicate
        filterIterator.hasNext();
    }

    @Test
    public void testUniquePredicateFiltering() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(1); // Duplicate
        list.add(3);
        
        ListIterator<Integer> baseIterator = list.listIterator();
        Predicate<Integer> uniquePredicate = new UniquePredicate<>();
        FilterListIterator<Integer> filterIterator = new FilterListIterator<>(baseIterator, uniquePredicate);
        
        assertTrue(filterIterator.hasNext());
        assertEquals(Integer.valueOf(1), filterIterator.next());
        assertTrue(filterIterator.hasNext());
        assertEquals(Integer.valueOf(2), filterIterator.next());
        // The second occurrence of 1 should be filtered out
        assertTrue(filterIterator.hasNext());
        assertEquals(Integer.valueOf(3), filterIterator.next());
        assertFalse(filterIterator.hasNext());
    }

    @Test
    public void testEqualPredicateFiltering() {
        LinkedList<String> list = new LinkedList<>();
        list.add("apple");
        list.add("banana");
        list.add("apple");
        list.add("cherry");
        
        ListIterator<String> baseIterator = list.listIterator();
        Predicate<String> equalsPredicate = new EqualPredicate<>("apple");
        FilterListIterator<String> filterIterator = new FilterListIterator<>(baseIterator, equalsPredicate);
        
        assertTrue(filterIterator.hasNext());
        assertEquals("apple", filterIterator.next());
        assertTrue(filterIterator.hasNext());
        assertEquals("apple", filterIterator.next());
        assertFalse(filterIterator.hasNext());
    }

    @Test
    public void testSetListIterator() {
        LinkedList<String> list1 = new LinkedList<>();
        list1.add("first");
        
        LinkedList<String> list2 = new LinkedList<>();
        list2.add("second");
        
        FilterListIterator<String> filterIterator = new FilterListIterator<>();
        Predicate<String> notNullPredicate = NotNullPredicate.notNullPredicate();
        filterIterator.setPredicate(notNullPredicate);
        
        // Set first iterator
        filterIterator.setListIterator(list1.listIterator());
        assertTrue(filterIterator.hasNext());
        assertEquals("first", filterIterator.next());
        
        // Change to second iterator
        filterIterator.setListIterator(list2.listIterator());
        assertTrue(filterIterator.hasNext());
        assertEquals("second", filterIterator.next());
    }

    @Test
    public void testSetPredicate() {
        LinkedList<String> list = new LinkedList<>();
        list.add("test");
        list.add(null);
        
        ListIterator<String> baseIterator = list.listIterator();
        FilterListIterator<String> filterIterator = new FilterListIterator<>(baseIterator);
        
        // Initially no predicate
        assertNull(filterIterator.getPredicate());
        
        // Set not-null predicate
        Predicate<String> notNullPredicate = NotNullPredicate.notNullPredicate();
        filterIterator.setPredicate(notNullPredicate);
        assertEquals(notNullPredicate, filterIterator.getPredicate());
        
        // Should only return non-null elements
        assertTrue(filterIterator.hasNext());
        assertEquals("test", filterIterator.next());
        assertFalse(filterIterator.hasNext());
    }

    @Test(expected = NullPointerException.class)
    public void testNullPredicateThrowsException() {
        LinkedList<String> list = new LinkedList<>();
        list.add("test");
        
        ListIterator<String> baseIterator = list.listIterator();
        FilterListIterator<String> filterIterator = new FilterListIterator<>(baseIterator);
        
        // With null predicate, should throw NPE when trying to iterate
        filterIterator.hasNext();
    }
}