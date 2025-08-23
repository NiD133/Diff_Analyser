package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the interaction between the next() and previous() methods of FilterListIterator.
 * This test ensures that a call to next() correctly updates the state so that a
 * subsequent call to previous() returns the same element.
 */
class FilterListIteratorNextPreviousInteractionTest {

    private List<Integer> fullList;
    private List<Integer> listFilteredByThrees;

    private Predicate<Integer> truePredicate;
    private Predicate<Integer> divisibleByThreePredicate;

    @BeforeEach
    void setUp() {
        fullList = new ArrayList<>();
        listFilteredByThrees = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            fullList.add(i);
            if (i % 3 == 0) {
                listFilteredByThrees.add(i);
            }
        }

        truePredicate = x -> true;
        divisibleByThreePredicate = x -> x % 3 == 0;
    }

    @Test
    @DisplayName("next() should set state for subsequent previous() call when filtering")
    void nextCallShouldSetStateForSubsequentPreviousCallWhenFiltering() {
        // Arrange
        final ListIterator<Integer> expectedIterator = listFilteredByThrees.listIterator();
        final ListIterator<Integer> filterIterator = new FilterListIterator<>(fullList.listIterator(), divisibleByThreePredicate);

        // Act & Assert
        assertNextAndPreviousBehaveConsistently(expectedIterator, filterIterator);
    }

    @Test
    @DisplayName("next() should set state for subsequent previous() call with a pass-through filter")
    void nextCallShouldSetStateForSubsequentPreviousCallWithPassThroughFilter() {
        // Arrange
        final ListIterator<Integer> expectedIterator = fullList.listIterator();
        final ListIterator<Integer> filterIterator = new FilterListIterator<>(fullList.listIterator(), truePredicate);

        // Act & Assert
        assertNextAndPreviousBehaveConsistently(expectedIterator, filterIterator);
    }

    /**
     * Asserts that after two next() calls, a previous() call returns the last element retrieved by next().
     * This helper verifies a specific interaction scenario for a given "testing" iterator against a
     * known "expected" iterator.
     *
     * @param expected The iterator with the expected correct behavior (e.g., a standard ListIterator).
     * @param testing  The iterator being tested (e.g., a FilterListIterator).
     */
    private void assertNextAndPreviousBehaveConsistently(final ListIterator<Integer> expected, final ListIterator<Integer> testing) {
        // 1. Move cursor forward one step to ensure hasPrevious() will be true.
        assertEquals(expected.next(), testing.next(), "First element should match");
        assertTrue(testing.hasPrevious(), "Should have a previous element after one next() call");

        // 2. Call next() to get the element we will test against.
        final Integer elementFromNext = expected.next();
        final Integer actualElementFromNext = testing.next();
        assertEquals(elementFromNext, actualElementFromNext, "Second element from next() should match");

        // 3. Call previous() immediately. It should return the same element.
        final Integer elementFromPrevious = expected.previous();
        final Integer actualElementFromPrevious = testing.previous();

        // Assert: The element returned by previous() is the same one just returned by next().
        // This confirms the core contract for both the expected and the testing iterators.
        assertEquals(elementFromNext, elementFromPrevious, "For standard iterator, previous() should return the element from next()");
        assertEquals(actualElementFromNext, actualElementFromPrevious, "For filtered iterator, previous() should return the element from next()");

        // Assert: The filtered iterator's returned value from previous() matches the standard one.
        assertEquals(elementFromPrevious, actualElementFromPrevious, "The previous() element from the filtered iterator should match the expected one");
    }
}