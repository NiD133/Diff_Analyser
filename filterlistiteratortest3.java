package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for FilterListIterator, focusing on its behavior at the boundaries of the list.
 */
public class FilterListIteratorTest {

    @Test
    @DisplayName("Iterator should have correct state after iterating to the end")
    void iteratorStateIsCorrectAfterIteratingToEnd() {
        // Arrange: Create a source list of numbers from 0 to 19.
        final List<Integer> sourceList = IntStream.range(0, 20)
                .boxed()
                .collect(Collectors.toList());

        // Arrange: Define a predicate to find numbers divisible by 4.
        // The filtered elements will be: 0, 4, 8, 12, 16.
        final Predicate<Integer> isDivisibleByFour = n -> n % 4 == 0;
        final ListIterator<Integer> filteredIterator = new FilterListIterator<>(sourceList.listIterator(), isDivisibleByFour);

        // Act: Advance the iterator to the end of the filtered sequence.
        while (filteredIterator.hasNext()) {
            filteredIterator.next();
        }

        // Assert: Verify the iterator's state is correct at the end.
        assertFalse(filteredIterator.hasNext(), "Iterator should not have a next element at the end.");
        assertTrue(filteredIterator.hasPrevious(), "Iterator should have a previous element at the end.");

        // Assert: Calling previous() should return the last filtered element.
        assertEquals(16, filteredIterator.previous(), "previous() should return the last element that matched the predicate.");
    }
}