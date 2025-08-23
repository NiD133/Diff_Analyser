package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests complex iteration scenarios for {@link FilterListIterator}.
 *
 * <p>This test class focuses on ensuring the iterator maintains correct state
 * during various complex traversal patterns, including nested filtering and
 * mixed forward/backward movements.</p>
 */
@DisplayName("FilterListIterator Complex Walk Test")
class FilterListIteratorComplexWalkTest {

    private static final int LIST_SIZE = 20;
    private static final int RANDOM_WALK_STEPS = 500;

    private final Random random = new Random();

    // The complete list of integers from 0 to 19.
    private List<Integer> fullList;

    // Expected lists after applying specific filters.
    private List<Integer> evenNumbers;
    private List<Integer> multiplesOfThree;
    private List<Integer> multiplesOfSix;

    // Predicates for filtering.
    private Predicate<Integer> isEvenPredicate;
    private Predicate<Integer> isMultipleOfThreePredicate;

    @BeforeEach
    void setUp() {
        fullList = new ArrayList<>();
        evenNumbers = new ArrayList<>();
        multiplesOfThree = new ArrayList<>();
        multiplesOfSix = new ArrayList<>();

        for (int i = 0; i < LIST_SIZE; i++) {
            fullList.add(i);
            if (i % 2 == 0) {
                evenNumbers.add(i);
            }
            if (i % 3 == 0) {
                multiplesOfThree.add(i);
            }
            if (i % 6 == 0) {
                multiplesOfSix.add(i);
            }
        }

        isEvenPredicate = number -> number % 2 == 0;
        isMultipleOfThreePredicate = number -> number % 3 == 0;
    }

    @Test
    @DisplayName("Nested filters should behave correctly during complex walks")
    void testNestedFiltersWithComplexWalks() {
        // 1. Set up the iterator chain
        // We create a nested FilterListIterator to find numbers that are multiples of 3 AND are even.
        // This is equivalent to finding multiples of 6.
        final ListIterator<Integer> baseIterator = fullList.listIterator();
        final FilterListIterator<Integer> multiplesOfThreeIterator =
                new FilterListIterator<>(baseIterator, isMultipleOfThreePredicate);
        final FilterListIterator<Integer> multiplesOfSixIterator =
                new FilterListIterator<>(multiplesOfThreeIterator, isEvenPredicate);

        // 2. Further wrap with a pass-through filter to ensure it doesn't alter behavior.
        final FilterListIterator<Integer> finalIterator =
                new FilterListIterator<>(multiplesOfSixIterator, PredicateUtils.truePredicate());

        // 3. Perform a series of complex walks to stress-test the iterator's state
        performComprehensiveWalks(multiplesOfSix, finalIterator);
    }

    /**
     * Orchestrates a series of walk patterns to comprehensively test the iterator's state.
     *
     * @param expectedList The list containing the expected elements in the correct order.
     * @param iteratorToTest The FilterListIterator instance under test.
     */
    private <E> void performComprehensiveWalks(final List<E> expectedList, final ListIterator<E> iteratorToTest) {
        final ListIterator<E> expectedIterator = expectedList.listIterator();

        assertFullForwardWalk(expectedIterator, iteratorToTest);
        assertFullBackwardWalk(expectedIterator, iteratorToTest);
        assertZigZagWalk(expectedIterator, iteratorToTest);
        assertComplexPartialWalks(expectedList.size(), expectedIterator, iteratorToTest);
        assertRandomWalk(expectedIterator, iteratorToTest);
    }

    /**
     * Asserts that a full traversal from start to end matches the expected iterator.
     */
    private void assertFullForwardWalk(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasNext()) {
            assertTrue(testing.hasNext(), "Testing iterator should have a next element");
            assertEquals(expected.nextIndex(), testing.nextIndex());
            assertEquals(expected.previousIndex(), testing.previousIndex());
            assertEquals(expected.next(), testing.next());
        }
    }

    /**
     * Asserts that a full traversal from end to start matches the expected iterator.
     * Assumes the iterator is currently at the end.
     */
    private void assertFullBackwardWalk(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasPrevious()) {
            assertTrue(testing.hasPrevious(), "Testing iterator should have a previous element");
            assertEquals(expected.nextIndex(), testing.nextIndex());
            assertEquals(expected.previousIndex(), testing.previousIndex());
            assertEquals(expected.previous(), testing.previous());
        }
    }

    /**
     * Asserts correct behavior during a repeating "next, previous, next" walk.
     * Assumes the iterator is currently at the start.
     */
    private void assertZigZagWalk(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasNext()) {
            assertEquals(expected.next(), testing.next());
            assertEquals(expected.previous(), testing.previous());
            assertEquals(expected.next(), testing.next());
        }
    }

    /**
     * Performs a series of increasingly larger partial walks (forward and backward)
     * to stress the iterator's internal state management.
     * Assumes the iterator is currently at the start.
     */
    private void assertComplexPartialWalks(final int listSize, final ListIterator<?> expected, final ListIterator<?> testing) {
        for (int i = 0; i < listSize; i++) {
            // Walk forward i steps
            for (int j = 0; j < i; j++) {
                assertEquals(expected.next(), testing.next());
            }
            // Walk back i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertEquals(expected.previous(), testing.previous());
            }
            // Walk forward i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertEquals(expected.next(), testing.next());
            }
            // Walk back to the start
            for (int j = 0; j < i; j++) {
                assertEquals(expected.previous(), testing.previous());
            }
        }
    }

    /**
     * Performs a random walk of forward and backward steps, asserting equality at each step.
     */
    private void assertRandomWalk(final ListIterator<?> expected, final ListIterator<?> testing) {
        final StringBuilder walkDescription = new StringBuilder(RANDOM_WALK_STEPS);
        for (int i = 0; i < RANDOM_WALK_STEPS; i++) {
            if (random.nextBoolean()) {
                walkDescription.append(" next() ");
                if (expected.hasNext()) {
                    assertTrue(testing.hasNext(), "Expected hasNext but testing did not. Walk: " + walkDescription);
                    assertEquals(expected.next(), testing.next(), "Mismatch after walk: " + walkDescription);
                }
            } else {
                walkDescription.append(" previous() ");
                if (expected.hasPrevious()) {
                    assertTrue(testing.hasPrevious(), "Expected hasPrevious but testing did not. Walk: " + walkDescription);
                    assertEquals(expected.previous(), testing.previous(), "Mismatch after walk: " + walkDescription);
                }
            }
            assertEquals(expected.nextIndex(), testing.nextIndex(), "Index mismatch after walk: " + walkDescription);
        }
    }
}