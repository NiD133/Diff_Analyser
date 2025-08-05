package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

import org.junit.Test;

/**
 * Tests for {@link ZippingIterator}.
 */
public class ZippingIteratorTest {

    // --- Constructor Tests ---

    @Test(expected = NullPointerException.class)
    public void constructorWithTwoArgsShouldThrowExceptionForNullFirstIterator() {
        new ZippingIterator<>(null, Collections.emptyIterator());
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithTwoArgsShouldThrowExceptionForNullSecondIterator() {
        new ZippingIterator<>(Collections.emptyIterator(), null);
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithVarargsShouldThrowExceptionForNullIteratorInArray() {
        Iterator<String> iterator = Collections.emptyIterator();
        new ZippingIterator<>(iterator, null, iterator);
    }

    // --- hasNext() and next() Tests ---

    @Test
    public void shouldInterleaveElementsFromMultipleIteratorsOfSameLength() {
        // Arrange
        List<String> list1 = Arrays.asList("A1", "A2");
        List<String> list2 = Arrays.asList("B1", "B2");
        ZippingIterator<String> zippingIterator = new ZippingIterator<>(list1.iterator(), list2.iterator());

        // Act & Assert
        assertTrue(zippingIterator.hasNext());
        assertEquals("A1", zippingIterator.next());

        assertTrue(zippingIterator.hasNext());
        assertEquals("B1", zippingIterator.next());

        assertTrue(zippingIterator.hasNext());
        assertEquals("A2", zippingIterator.next());

        assertTrue(zippingIterator.hasNext());
        assertEquals("B2", zippingIterator.next());

        assertFalse(zippingIterator.hasNext());
    }

    @Test
    public void shouldContinueIteratingWhenOneIteratorIsLonger() {
        // Arrange
        List<String> list1 = Collections.singletonList("A1");
        List<String> list2 = Arrays.asList("B1", "B2");
        ZippingIterator<String> zippingIterator = new ZippingIterator<>(list1.iterator(), list2.iterator());

        // Act & Assert
        assertEquals("A1", zippingIterator.next());
        assertEquals("B1", zippingIterator.next());
        assertEquals("B2", zippingIterator.next());
        assertFalse(zippingIterator.hasNext());
    }

    @Test
    public void hasNextShouldReturnFalseWhenAllIteratorsAreEmpty() {
        // Arrange
        ZippingIterator<Object> zippingIterator = new ZippingIterator<>(
                Collections.emptyIterator(),
                Collections.emptyIterator()
        );

        // Act & Assert
        assertFalse(zippingIterator.hasNext());
    }
    
    @Test
    public void hasNextShouldBeIdempotent() {
        // Arrange
        List<String> list = Collections.singletonList("A");
        ZippingIterator<String> zippingIterator = new ZippingIterator<>(list.iterator());

        // Act & Assert
        assertTrue(zippingIterator.hasNext());
        assertTrue(zippingIterator.hasNext());
        
        zippingIterator.next();
        
        assertFalse(zippingIterator.hasNext());
        assertFalse(zippingIterator.hasNext());
    }

    @Test
    public void nextShouldReturnNullWhenUnderlyingIteratorReturnsNull() {
        // Arrange
        List<String> listWithNull = Arrays.asList((String) null);
        ZippingIterator<String> zippingIterator = new ZippingIterator<>(listWithNull.iterator());

        // Act
        String result = zippingIterator.next();

        // Assert
        assertNull(result);
    }

    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowNoSuchElementExceptionWhenAllIteratorsAreExhausted() {
        // Arrange
        List<String> list = Collections.singletonList("A");
        ZippingIterator<String> zippingIterator = new ZippingIterator<>(list.iterator());

        // Act
        zippingIterator.next(); // Exhaust the iterator

        // Assert: This call should throw
        zippingIterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowNoSuchElementExceptionForEmptyIterator() {
        // Arrange
        ZippingIterator<Object> zippingIterator = new ZippingIterator<>(Collections.emptyIterator());

        // Act & Assert
        zippingIterator.next();
    }

    // --- remove() Tests ---

    @Test
    public void removeShouldRemoveElementFromCorrectUnderlyingIterator() {
        // Arrange
        List<String> list1 = new LinkedList<>(Arrays.asList("A1", "A2"));
        List<String> list2 = new LinkedList<>(Arrays.asList("B1", "B2"));
        ZippingIterator<String> zippingIterator = new ZippingIterator<>(list1.iterator(), list2.iterator());

        zippingIterator.next(); // Consumes "A1" from list1
        zippingIterator.next(); // Consumes "B1" from list2

        // Act
        zippingIterator.remove(); // Removes the last returned element, "B1", from list2

        // Assert
        assertEquals("list1 should be unchanged", Arrays.asList("A1", "A2"), list1);
        assertEquals("\"B1\" should be removed from list2", Collections.singletonList("B2"), list2);
    }

    @Test(expected = IllegalStateException.class)
    public void removeShouldThrowIllegalStateExceptionWhenCalledBeforeNext() {
        // Arrange
        ZippingIterator<String> zippingIterator = new ZippingIterator<>(
                Collections.singletonList("A").iterator()
        );

        // Act & Assert
        zippingIterator.remove();
    }
    
    @Test(expected = IllegalStateException.class)
    public void removeShouldThrowIllegalStateExceptionWhenCalledTwice() {
        // Arrange
        List<String> list = new LinkedList<>(Collections.singletonList("A"));
        ZippingIterator<String> zippingIterator = new ZippingIterator<>(list.iterator());
        zippingIterator.next();

        // Act
        zippingIterator.remove(); // First call is valid

        // Assert: Second call should throw
        zippingIterator.remove();
    }

    // --- Concurrent Modification Tests ---

    @Test(expected = ConcurrentModificationException.class)
    public void nextShouldThrowConcurrentModificationExceptionIfUnderlyingCollectionIsModified() {
        // Arrange
        List<String> list = new LinkedList<>(Collections.singletonList("A"));
        Iterator<String> listIterator = list.iterator();
        ZippingIterator<String> zippingIterator = new ZippingIterator<>(listIterator);

        // Act: Modify the underlying collection after creating the iterator
        list.add("B");

        // Assert: This should throw ConcurrentModificationException
        zippingIterator.next();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void removeShouldThrowConcurrentModificationExceptionIfUnderlyingCollectionIsModified() {
        // Arrange
        List<String> list = new LinkedList<>(Collections.singletonList("A"));
        Iterator<String> listIterator = list.iterator();
        ZippingIterator<String> zippingIterator = new ZippingIterator<>(listIterator);

        zippingIterator.next(); // Get the first element

        // Act: Modify the underlying collection directly
        list.add("B");

        // Assert: This should throw ConcurrentModificationException
        zippingIterator.remove();
    }
}