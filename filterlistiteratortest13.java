package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the state management and traversal logic of {@link FilterListIterator}
 * under various complex movement patterns. This test suite ensures that the iterator
 * behaves identically to a standard ListIterator on a pre-filtered list,
 * stress-testing its internal state through forwards, backwards, and random walks.
 */
public class FilterListIteratorComplexTraversalTest {

    private static final int LIST_SIZE = 20;
    private static final int RANDOM_WALK_STEPS = 500;

    private List<Integer> sourceList;
    private List<Integer> expectedFilteredList;
    private Predicate<Integer> isDivisibleByThree;

    private final Random random = new Random();

    @BeforeEach
    public void setUp() {
        sourceList = new ArrayList<>();
        expectedFilteredList = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            sourceList.add(i);
            if (i % 3 == 0) {
                expectedFilteredList.add(i);
            }
        }
        isDivisibleByThree = x -> x % 3 == 0;
    }

    @Test
    void testIteratorBehavesCorrectlyDuringComplexTraversals() {
        // Arrange
        final ListIterator<Integer> expectedIterator = expectedFilteredList.listIterator();
        final FilterListIterator<Integer> filteredIterator =
                new FilterListIterator<>(sourceList.listIterator(), isDivisibleByThree);

        // Act & Assert: Perform a series of traversals and verify behavior at each step.
        // The test is structured as a sequence of scenarios to validate the iterator's state machine.

        // Scenario 1: Simple full forward and backward traversal.
        assertWalksForwardAsExpected(expectedIterator, filteredIterator);
        assertWalksBackwardAsExpected(expectedIterator, filteredIterator);

        // Scenario 2: Alternating next() and previous() calls to test yo-yo movement.
        assertAlternatingNextPreviousTraversal(expectedIterator, filteredIterator);

        // Scenario 3: A complex, nested pattern of partial forward and backward movements.
        assertNestedPartialTraversals(expectedIterator, filteredIterator);

        // Scenario 4: A long, randomized sequence of movements to catch edge cases.
        assertRandomWalkTraversal(expectedIterator, filteredIterator);
    }

    /**
     * Verifies that the testing iterator traverses forward correctly, matching the expected iterator.
     */
    private void assertWalksForwardAsExpected(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasNext()) {
            assertTrue(testing.hasNext(), "Filtered iterator should have a next element");
            assertEquals(expected.nextIndex(), testing.nextIndex());
            assertEquals(expected.previousIndex(), testing.previousIndex());
            assertEquals(expected.next(), testing.next());
        }
        assertFalse(testing.hasNext(), "Filtered iterator should be at the end");
    }

    /**
     * Verifies that the testing iterator traverses backward correctly, matching the expected iterator.
     */
    private void assertWalksBackwardAsExpected(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasPrevious()) {
            assertTrue(testing.hasPrevious(), "Filtered iterator should have a previous element");
            assertEquals(expected.nextIndex(), testing.nextIndex());
            assertEquals(expected.previousIndex(), testing.previousIndex());
            assertEquals(expected.previous(), testing.previous());
        }
        assertFalse(testing.hasPrevious(), "Filtered iterator should be at the beginning");
    }

    /**
     * Verifies a "yo-yo" traversal pattern: next(), previous(), next().
     * This ensures that moving back and then forward returns the iterator to a consistent state.
     */
    private void assertAlternatingNextPreviousTraversal(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasNext()) {
            final Object expectedElement = expected.next();
            assertEquals(expectedElement, testing.next(), "First next() call should match");

            assertEquals(expectedElement, expected.previous(), "Expected iterator should move back");
            assertEquals(expectedElement, testing.previous(), "Filtered iterator should move back to the same element");

            assertEquals(expectedElement, expected.next(), "Expected iterator should move forward again");
            assertEquals(expectedElement, testing.next(), "Filtered iterator should move forward to the same element again");
        }
    }

    /**
     * Performs a series of increasingly larger partial traversals to stress-test state management.
     * The pattern is: forward i, back i/2, forward i/2, back i.
     */
    private void assertNestedPartialTraversals(final ListIterator<?> expected, final ListIterator<?> testing) {
        // Ensure iterators start from the beginning for this test sequence.
        while (expected.hasPrevious()) {
            expected.previous();
        }
        while (testing.hasPrevious()) {
            testing.previous();
        }

        final int size = expectedFilteredList.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < i; j++) {
                assertNextOperationMatches(expected, testing);
            }
            for (int j = 0; j < i / 2; j++) {
                assertPreviousOperationMatches(expected, testing);
            }
            for (int j = 0; j < i / 2; j++) {
                assertNextOperationMatches(expected, testing);
            }
            for (int j = 0; j < i; j++) {
                assertPreviousOperationMatches(expected, testing);
            }
        }
    }

    /**
     * Performs a long, randomized sequence of forward and backward steps to catch subtle state transition bugs.
     */
    private void assertRandomWalkTraversal(final ListIterator<?> expected, final ListIterator<?> testing) {
        final StringBuilder walkDescription = new StringBuilder(RANDOM_WALK_STEPS);

        for (int i = 0; i < RANDOM_WALK_STEPS; i++) {
            if (random.nextBoolean()) {
                walkDescription.append(" next()");
                if (expected.hasNext()) {
                    assertTrue(testing.hasNext(), "testing.hasNext() should be true. Walk: " + walkDescription);
                    assertEquals(expected.next(), testing.next(), "next() should return same element. Walk: " + walkDescription);
                } else {
                    assertFalse(testing.hasNext(), "testing.hasNext() should be false. Walk: " + walkDescription);
                }
            } else {
                walkDescription.append(" previous()");
                if (expected.hasPrevious()) {
                    assertTrue(testing.hasPrevious(), "testing.hasPrevious() should be true. Walk: " + walkDescription);
                    assertEquals(expected.previous(), testing.previous(), "previous() should return same element. Walk: " + walkDescription);
                } else {
                    assertFalse(testing.hasPrevious(), "testing.hasPrevious() should be false. Walk: " + walkDescription);
                }
            }
            assertEquals(expected.nextIndex(), testing.nextIndex(), "nextIndex() should match. Walk: " + walkDescription);
            assertEquals(expected.previousIndex(), testing.previousIndex(), "previousIndex() should match. Walk: " + walkDescription);
        }
    }

    private void assertNextOperationMatches(final ListIterator<?> expected, final ListIterator<?> testing) {
        assertTrue(expected.hasNext(), "Test logic error: expected iterator should have a next element.");
        assertWalksForwardAsExpected(expected, testing, 1);
    }

    private void assertPreviousOperationMatches(final ListIterator<?> expected, final ListIterator<?> testing) {
        assertTrue(expected.hasPrevious(), "Test logic error: expected iterator should have a previous element.");
        assertWalksBackwardAsExpected(expected, testing, 1);
    }
}