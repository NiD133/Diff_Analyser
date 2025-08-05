package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;

/**
 * Tests for {@link BoundedIterator}.
 */
public class BoundedIteratorTest {

    /**
     * Helper method to collect all remaining elements from an iterator into a list.
     */
    private <E> List<E> drainToList(final Iterator<E> iterator) {
        final List<E> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);
        return list;
    }

    // ========================================================================
    // Constructor Tests
    // ========================================================================

    @Test
    public void constructorShouldThrowOnNullIterator() {
        try {
            new BoundedIterator<>(null, 0, 10);
            fail("Constructor should have thrown a NullPointerException for a null iterator.");
        } catch (final NullPointerException e) {
            assertEquals("iterator", e.getMessage());
        }
    }

    @Test
    public void constructorShouldThrowOnNegativeOffset() {
        try {
            new BoundedIterator<>(Collections.emptyIterator(), -1, 10);
            fail("Constructor should have thrown an IllegalArgumentException for a negative offset.");
        } catch (final IllegalArgumentException e) {
            assertEquals("Offset parameter must not be negative.", e.getMessage());
        }
    }

    @Test
    public void constructorShouldThrowOnNegativeMax() {
        try {
            new BoundedIterator<>(Collections.emptyIterator(), 0, -1);
            fail("Constructor should have thrown an IllegalArgumentException for a negative max.");
        } catch (final IllegalArgumentException e) {
            assertEquals("Max parameter must not be negative.", e.getMessage());
        }
    }

    // ========================================================================
    // Happy Path Tests
    // ========================================================================

    @Test
    public void shouldIterateOverFullRange() {
        // Arrange
        final List<String> elements = Arrays.asList("a", "b", "c", "d", "e");
        final Iterator<String> boundedIterator = new BoundedIterator<>(elements.iterator(), 0, 5);

        // Act
        final List<String> result = drainToList(boundedIterator);

        // Assert
        assertEquals(elements, result);
    }

    @Test
    public void shouldIterateWithOffset() {
        // Arrange
        final List<String> elements = Arrays.asList("a", "b", "c", "d", "e");
        final Iterator<String> boundedIterator = new BoundedIterator<>(elements.iterator(), 2, 3);

        // Act
        final List<String> result = drainToList(boundedIterator);

        // Assert
        final List<String> expected = Arrays.asList("c", "d", "e");
        assertEquals(expected, result);
    }

    @Test
    public void shouldIterateWithMaxLimit() {
        // Arrange
        final List<String> elements = Arrays.asList("a", "b", "c", "d", "e");
        final Iterator<String> boundedIterator = new BoundedIterator<>(elements.iterator(), 0, 3);

        // Act
        final List<String> result = drainToList(boundedIterator);

        // Assert
        final List<String> expected = Arrays.asList("a", "b", "c");
        assertEquals(expected, result);
    }

    @Test
    public void shouldIterateWithOffsetAndMax() {
        // Arrange
        final List<String> elements = Arrays.asList("a", "b", "c", "d", "e");
        final Iterator<String> boundedIterator = new BoundedIterator<>(elements.iterator(), 1, 2);

        // Act
        final List<String> result = drainToList(boundedIterator);

        // Assert
        final List<String> expected = Arrays.asList("b", "c");
        assertEquals(expected, result);
    }

    // ========================================================================
    // Edge Case and Exception Tests
    // ========================================================================

    @Test
    public void hasNextShouldBeFalseWhenMaxIsZero() {
        // Arrange
        final List<String> elements = Arrays.asList("a", "b", "c");
        final Iterator<String> boundedIterator = new BoundedIterator<>(elements.iterator(), 0, 0);

        // Act & Assert
        assertFalse("Iterator should be empty when max is 0", boundedIterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowWhenMaxIsZero() {
        // Arrange
        final List<String> elements = Arrays.asList("a", "b", "c");
        final Iterator<String> boundedIterator = new BoundedIterator<>(elements.iterator(), 0, 0);

        // Act
        boundedIterator.next(); // Should throw
    }

    @Test
    public void hasNextShouldBeFalseWhenOffsetExceedsSize() {
        // Arrange
        final List<String> elements = Arrays.asList("a", "b");
        final Iterator<String> boundedIterator = new BoundedIterator<>(elements.iterator(), 5, 5);

        // Act & Assert
        assertFalse("hasNext() should be false if offset is beyond the end of the iterator", boundedIterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void nextShouldThrowWhenOffsetExceedsSize() {
        // Arrange
        final List<String> elements = Arrays.asList("a", "b");
        final Iterator<String> boundedIterator = new BoundedIterator<>(elements.iterator(), 5, 5);

        // Act
        boundedIterator.next(); // Should throw
    }

    @Test
    public void hasNextShouldBecomeFalseAfterLastElement() {
        // Arrange
        final List<String> elements = Arrays.asList("a", "b");
        final Iterator<String> boundedIterator = new BoundedIterator<>(elements.iterator(), 0, 2);

        // Act
        assertTrue(boundedIterator.hasNext());
        boundedIterator.next(); // "a"
        assertTrue(boundedIterator.hasNext());
        boundedIterator.next(); // "b"

        // Assert
        assertFalse("hasNext should be false after iterating all bounded elements", boundedIterator.hasNext());
    }

    @Test
    public void removeShouldSucceedAfterNext() {
        // Arrange
        final List<String> list = new LinkedList<>(Arrays.asList("a", "b", "c", "d"));
        // Bounded to "b", "c"
        final Iterator<String> boundedIterator = new BoundedIterator<>(list.iterator(), 1, 2);

        // Act
        final String first = boundedIterator.next(); // Consumes "b"
        boundedIterator.remove(); // Removes "b"

        // Assert
        assertEquals("b", first);
        final List<String> expectedList = Arrays.asList("a", "c", "d");
        assertEquals("Element 'b' should have been removed from the list", expectedList, list);
    }

    @Test
    public void removeShouldThrowBeforeNext() {
        // Arrange
        final List<String> list = new LinkedList<>(Arrays.asList("a", "b", "c"));
        final Iterator<String> boundedIterator = new BoundedIterator<>(list.iterator(), 1, 2);

        // Act & Assert
        try {
            boundedIterator.remove();
            fail("remove() should throw IllegalStateException if next() has not been called");
        } catch (final IllegalStateException e) {
            assertEquals("remove() cannot be called before calling next()", e.getMessage());
        }
    }

    @Test(expected = ConcurrentModificationException.class)
    public void nextShouldThrowConcurrentModificationException() {
        // Arrange
        final List<String> list = new LinkedList<>(Arrays.asList("a", "b", "c"));
        final Iterator<String> boundedIterator = new BoundedIterator<>(list.iterator(), 0, 3);

        // Act
        boundedIterator.next(); // "a"
        list.add("d"); // Modify the underlying collection
        boundedIterator.next(); // Should throw
    }

    @Test(expected = ConcurrentModificationException.class)
    public void constructorShouldThrowConcurrentModificationExceptionDuringInitialization() {
        // Arrange
        final List<String> list = new LinkedList<>(Arrays.asList("a", "b", "c"));
        final Iterator<String> listIterator = list.iterator();

        // Act
        list.add("d"); // Modify collection after creating iterator but before BoundedIterator construction
        new BoundedIterator<>(listIterator, 2, 1); // Should throw during init() while skipping elements
    }
}