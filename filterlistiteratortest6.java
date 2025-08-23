package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests manual traversal scenarios for the {@link FilterListIterator}.
 * This class focuses on verifying the iterator's behavior with sequences of
 * next() and previous() calls.
 */
@DisplayName("FilterListIterator Manual Traversal")
public class FilterListIteratorManualTraversalTest {

    private List<Integer> numbers;
    private List<Integer> numbersDivisibleByThree;
    private Predicate<Integer> isDivisibleByThree;

    @BeforeEach
    public void setUp() {
        numbers = IntStream.range(0, 20).boxed().collect(Collectors.toList());
        isDivisibleByThree = n -> n % 3 == 0;
        numbersDivisibleByThree = numbers.stream()
                                         .filter(isDivisibleByThree::test)
                                         .collect(Collectors.toList());
    }

    /**
     * Tests a full traversal forward to the end of the iterator, followed by a
     * full traversal backward to the beginning.
     */
    @Test
    public void testFullForwardAndBackwardTraversal() {
        // Arrange
        final ListIterator<Integer> filteredIterator = new FilterListIterator<>(numbers.listIterator(), isDivisibleByThree);
        final ListIterator<Integer> expectedIterator = numbersDivisibleByThree.listIterator();

        // Act & Assert: Forward traversal
        // Compare the filtered iterator against an iterator over the expected elements
        while (expectedIterator.hasNext()) {
            assertTrue(filteredIterator.hasNext(), "Filtered iterator should have more elements going forward");
            assertEquals(expectedIterator.next(), filteredIterator.next(), "Forward traversal returned an unexpected element");
        }
        assertFalse(filteredIterator.hasNext(), "Filtered iterator should be at the end after full forward traversal");

        // Act & Assert: Backward traversal
        while (expectedIterator.hasPrevious()) {
            assertTrue(filteredIterator.hasPrevious(), "Filtered iterator should have previous elements going backward");
            assertEquals(expectedIterator.previous(), filteredIterator.previous(), "Backward traversal returned an unexpected element");
        }
        assertFalse(filteredIterator.hasPrevious(), "Filtered iterator should be at the beginning after full backward traversal");
    }

    /**
     * Tests the behavior of calling next() and then immediately previous()
     * when the iterator is at the beginning.
     */
    @Test
    public void testAlternatingNextAndPreviousAtStart() {
        // Arrange
        final ListIterator<Integer> filteredIterator = new FilterListIterator<>(numbers.listIterator(), isDivisibleByThree);

        // Act & Assert
        // Move forward to the first element (0)
        assertEquals(0, filteredIterator.next());

        // Move back. Should return the same element (0) and reset cursor position.
        assertEquals(0, filteredIterator.previous());

        // The iterator should now be at the very beginning.
        assertFalse(filteredIterator.hasPrevious());

        // Moving forward again should return the first element (0) again.
        assertEquals(0, filteredIterator.next());
    }

    /**
     * Tests a more complex sequence of next() and previous() calls in the
     * middle of the iteration.
     */
    @Test
    public void testAlternatingNextAndPreviousInMiddle() {
        // Arrange
        final ListIterator<Integer> filteredIterator = new FilterListIterator<>(numbers.listIterator(), isDivisibleByThree);

        // Act & Assert: Move the iterator to a position in the middle
        assertEquals(0, filteredIterator.next());
        assertEquals(3, filteredIterator.next());
        assertEquals(6, filteredIterator.next()); // Iterator cursor is now after 6

        // Test moving back and forth around elements 3 and 6
        assertEquals(6, filteredIterator.previous()); // Returns 6, cursor is between 3 and 6
        assertEquals(3, filteredIterator.previous()); // Returns 3, cursor is between 0 and 3
        assertEquals(3, filteredIterator.next());     // Returns 3, cursor is between 3 and 6
        assertEquals(6, filteredIterator.next());     // Returns 6, cursor is after 6

        // Continue forward traversal from the current position
        assertEquals(9, filteredIterator.next());
        assertEquals(12, filteredIterator.next());
        assertEquals(15, filteredIterator.next()); // Iterator cursor is now after 15

        // Traverse backward from the new position
        assertEquals(15, filteredIterator.previous()); // Returns 15, cursor is between 12 and 15
        assertEquals(12, filteredIterator.previous()); // Returns 12, cursor is between 9 and 12
        assertEquals(9, filteredIterator.previous());  // Returns 9, cursor is between 6 and 9
    }
}