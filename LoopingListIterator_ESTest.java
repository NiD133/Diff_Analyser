package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;

/**
 * Provides comprehensive tests for the LoopingListIterator class.
 * This refactored test suite focuses on clarity and maintainability.
 */
public class LoopingListIteratorRefactoredTest {

    //
    // Constructor Tests
    //

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullListShouldThrowNullPointerException() {
        // Act: Attempt to create an iterator with a null list.
        new LoopingListIterator<>(null);
    }

    @Test
    public void testConstructorWithModifiedSubListShouldThrowException() {
        // Arrange: Create a sublist and then modify its parent list.
        List<String> parentList = new LinkedList<>();
        List<String> subList = parentList.subList(0, 0);
        parentList.add("Concurrent Modification");

        // Act & Assert: Creating the iterator on the sublist should fail.
        try {
            new LoopingListIterator<>(subList);
            fail("Expected ConcurrentModificationException");
        } catch (final ConcurrentModificationException e) {
            // This is the expected behavior.
        }
    }

    //
    // Tests on an Empty List
    //

    @Test
    public void testHasNextOnEmptyListShouldReturnFalse() {
        // Arrange
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(new LinkedList<>());
        // Act & Assert
        assertFalse("hasNext() should be false for an empty list.", iterator.hasNext());
    }

    @Test
    public void testHasPreviousOnEmptyListShouldReturnFalse() {
        // Arrange
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(new LinkedList<>());
        // Act & Assert
        assertFalse("hasPrevious() should be false for an empty list.", iterator.hasPrevious());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextOnEmptyListShouldThrowNoSuchElementException() {
        // Arrange
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(new LinkedList<>());
        // Act
        iterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testPreviousOnEmptyListShouldThrowNoSuchElementException() {
        // Arrange
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(new LinkedList<>());
        // Act
        iterator.previous();
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testNextIndexOnEmptyListShouldThrowNoSuchElementException() {
        // Arrange
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(new LinkedList<>());
        // Act
        iterator.nextIndex();
    }

    @Test(expected = NoSuchElementException.class)
    public void testPreviousIndexOnEmptyListShouldThrowNoSuchElementException() {
        // Arrange
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(new LinkedList<>());
        // Act
        iterator.previousIndex();
    }

    @Test
    public void testSizeOnEmptyListShouldReturnZero() {
        // Arrange
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(new LinkedList<>());
        // Act & Assert
        assertEquals("Size of an empty list iterator should be 0.", 0, iterator.size());
    }

    @Test
    public void testResetOnEmptyListDoesNotThrowException() {
        // Arrange
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(new LinkedList<>());
        // Act
        iterator.reset();
        // Assert
        assertFalse("Iterator should still have no next element after reset.", iterator.hasNext());
    }

    //
    // Tests on a Non-Empty List
    //

    @Test
    public void testHasNextAndHasPreviousOnNonEmptyListShouldReturnTrue() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);
        
        // Act & Assert
        assertTrue("hasNext() should be true for a non-empty list.", iterator.hasNext());
        assertTrue("hasPrevious() should be true for a non-empty list.", iterator.hasPrevious());
    }

    @Test
    public void testNextLoopsAroundCorrectly() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        list.add("B");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Act & Assert
        assertEquals("First call to next() should return 'A'.", "A", iterator.next());
        assertEquals("Second call to next() should return 'B'.", "B", iterator.next());
        assertEquals("Third call to next() should loop and return 'A'.", "A", iterator.next());
    }

    @Test
    public void testPreviousLoopsAroundCorrectly() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        list.add("B");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Act & Assert
        assertEquals("First call to previous() should loop and return 'B'.", "B", iterator.previous());
        assertEquals("Second call to previous() should return 'A'.", "A", iterator.previous());
        assertEquals("Third call to previous() should loop and return 'B'.", "B", iterator.previous());
    }

    @Test
    public void testNextAndPreviousInteractions() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Act & Assert
        assertEquals("A", iterator.next());
        assertEquals("B", iterator.next());
        assertEquals("B", iterator.previous());
        assertEquals("A", iterator.previous());
        assertEquals("C", iterator.previous()); // Loops around to the end
        assertEquals("C", iterator.next());
        assertEquals("A", iterator.next());     // Loops around to the start
    }
    
    @Test
    public void testNextIndexLoopsCorrectly() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        list.add("B");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Act & Assert
        assertEquals("Initial nextIndex should be 0.", 0, iterator.nextIndex());
        iterator.next(); // Cursor is now after "A"
        assertEquals("nextIndex after one next() call should be 1.", 1, iterator.nextIndex());
        iterator.next(); // Cursor is now after "B", loops to start
        assertEquals("nextIndex should loop back to 0.", 0, iterator.nextIndex());
    }

    @Test
    public void testPreviousIndexLoopsCorrectly() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        list.add("B");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Act & Assert
        assertEquals("Initial previousIndex should be last index (1).", 1, iterator.previousIndex());
        iterator.next(); // Cursor is now after "A"
        assertEquals("previousIndex after one next() call should be 0.", 0, iterator.previousIndex());
        iterator.previous(); // Cursor is now before "A"
        assertEquals("previousIndex should loop back to 1.", 1, iterator.previousIndex());
    }

    @Test
    public void testNextAndPreviousWithNullElements() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add(null);
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);
        
        // Act & Assert
        assertNull("next() should correctly return a null element.", iterator.next());
        assertNull("previous() should correctly return a null element.", iterator.previous());
    }

    //
    // Modification Tests (add, set, remove)
    //

    @Test
    public void testAddMakesElementAvailableToIterator() {
        // Arrange
        final List<String> list = new LinkedList<>();
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Act
        iterator.add("A");

        // Assert
        assertEquals("Size should be 1 after add.", 1, iterator.size());
        // After add, previous() should return the new element
        assertEquals("previous() should return the newly added element.", "A", iterator.previous());
        // And next() should then also return it
        assertEquals("next() should now also return the new element.", "A", iterator.next());
    }

    @Test
    public void testRemoveAfterNextOnSingleElementListShouldMakeListEmpty() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Act
        iterator.next();
        iterator.remove();

        // Assert
        assertEquals("Size should be 0 after remove.", 0, iterator.size());
        assertFalse("hasNext() should be false after removing the only element.", iterator.hasNext());
        assertFalse("hasPrevious() should be false after removing the only element.", iterator.hasPrevious());
    }

    @Test
    public void testSetAfterPreviousShouldUpdateElement() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Act
        iterator.previous(); // Returns "A"
        iterator.set("B");   // Replaces "A" with "B"

        // Assert
        iterator.reset(); // Reset to check from the start
        assertEquals("The element should have been updated to 'B'.", "B", iterator.next());
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveWithoutNextOrPreviousShouldThrowIllegalStateException() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);
        // Act
        iterator.remove(); // Should throw
    }

    @Test(expected = IllegalStateException.class)
    public void testSetWithoutNextOrPreviousShouldThrowIllegalStateException() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);
        // Act
        iterator.set("B"); // Should throw
    }

    //
    // Concurrent Modification Tests
    //

    @Test
    public void testNextAfterConcurrentModificationShouldThrowException() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);
        list.add("B"); // Modify list directly

        // Act & Assert
        try {
            iterator.next();
            fail("Expected ConcurrentModificationException");
        } catch (final ConcurrentModificationException e) {
            // Expected
        }
    }
    
    @Test
    public void testPreviousAfterConcurrentModificationShouldThrowException() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);
        list.add("B"); // Modify list directly

        // Act & Assert
        try {
            iterator.previous();
            fail("Expected ConcurrentModificationException");
        } catch (final ConcurrentModificationException e) {
            // Expected
        }
    }

    @Test
    public void testRemoveAfterConcurrentModificationShouldThrowException() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);
        iterator.next();
        list.add("B"); // Modify list directly

        // Act & Assert
        try {
            iterator.remove();
            fail("Expected ConcurrentModificationException");
        } catch (final ConcurrentModificationException e) {
            // Expected
        }
    }
    
    @Test
    public void testResetAfterConcurrentModificationShouldThrowException() {
        // Arrange
        final List<String> list = new LinkedList<>();
        list.add("A");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);
        list.add("B"); // Modify list directly

        // Act & Assert
        try {
            iterator.reset();
            fail("Expected ConcurrentModificationException");
        } catch (final ConcurrentModificationException e) {
            // Expected
        }
    }
}