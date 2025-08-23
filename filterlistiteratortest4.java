package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Performs a comprehensive set of tests on the navigation capabilities of the
 * {@link FilterListIterator}, comparing its behavior to a standard ListIterator.
 * This includes forward, backward, alternating, and random walks.
 */
public class FilterListIteratorComprehensiveNavigationTest {

    private ArrayList<Integer> sourceList;
    private ArrayList<Integer> evenNumbers;

    private Predicate<Integer> falsePredicate;
    private Predicate<Integer> evenNumberPredicate;

    private final Random random = new Random();

    @BeforeEach
    public void setUp() {
        sourceList = new ArrayList<>();
        evenNumbers = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            sourceList.add(i);
            if (i % 2 == 0) {
                evenNumbers.add(i);
            }
        }

        falsePredicate = element -> false; // Bug fix: was incorrectly 'x -> true'
        evenNumberPredicate = element -> element % 2 == 0;
    }

    /**
     * Tests the iterator's navigation when the predicate filters out all elements,
     * resulting in an effectively empty iterator.
     */
    @Test
    void testNavigation_whenPredicateFiltersAll() {
        final ListIterator<Integer> filteredIterator =
                new FilterListIterator<>(sourceList.listIterator(), falsePredicate);

        assertComprehensiveNavigation(Collections.emptyList(), filteredIterator);
    }

    /**
     * Tests the iterator's navigation with a predicate that allows a subset of
     * elements (even numbers) to pass through.
     */
    @Test
    void testNavigation_whenPredicateFiltersSome() {
        final ListIterator<Integer> filteredIterator =
                new FilterListIterator<>(sourceList.listIterator(), evenNumberPredicate);

        assertComprehensiveNavigation(evenNumbers, filteredIterator);
    }

    /**
     * Asserts that the filtered iterator's navigation behavior matches the expected
     * iterator's behavior across a series of complex walk patterns.
     *
     * @param <E> the element type
     * @param expectedResult the list of elements expected to be in the filtered iterator
     * @param filteredIterator the {@link FilterListIterator} to test
     */
    private <E> void assertComprehensiveNavigation(final List<E> expectedResult, final ListIterator<E> filteredIterator) {
        final ListIterator<E> expectedIterator = expectedResult.listIterator();

        assertFullForwardAndBackwardWalks(expectedIterator, filteredIterator);
        assertAlternatingNextPreviousWalk(expectedIterator, filteredIterator);
        assertBouncingWalk(expectedIterator, filteredIterator);
        assertRandomWalk(expectedIterator, filteredIterator);
    }

    /**
     * Verifies walking fully forward, then fully backward.
     */
    private <E> void assertFullForwardAndBackwardWalks(final ListIterator<E> expected, final ListIterator<E> actual) {
        assertWalksForwardCorrectly(expected, actual);
        assertWalksBackwardCorrectly(expected, actual);
    }

    /**
     * Verifies a pattern of next(), previous(), next() calls.
     */
    private <E> void assertAlternatingNextPreviousWalk(final ListIterator<E> expected, final ListIterator<E> actual) {
        // After a full walk, the iterators are at the end. Reset to the start.
        while (expected.hasPrevious()) {
            expected.previous();
            actual.previous();
        }

        while (expected.hasNext()) {
            assertEquals(expected.next(), actual.next());
            if (expected.hasPrevious()) {
                assertEquals(expected.previous(), actual.previous());
                assertEquals(expected.next(), actual.next());
            }
        }
        // Ensure we end up at the end of the list again
        assertWalksForwardCorrectly(expected, actual);
    }

    /**
     * Verifies a complex walk pattern that moves forward and backward in increasing increments.
     */
    private <E> void assertBouncingWalk(final ListIterator<E> expected, final ListIterator<E> actual) {
        // Reset iterators to the start
        while (expected.hasPrevious()) {
            expected.previous();
            actual.previous();
        }

        for (int i = 0; i < expected.nextIndex(); i++) {
            // Walk forward i steps
            for (int j = 0; j < i; j++) {
                if (!expected.hasNext()) {
                    break;
                }
                assertEquals(expected.next(), actual.next());
            }
            // Walk back i/2 steps
            for (int j = 0; j < i / 2; j++) {
                if (!expected.hasPrevious()) {
                    break;
                }
                assertEquals(expected.previous(), actual.previous());
            }
        }
    }

    /**
     * Verifies that the iterator state remains correct after a long series of
     * random forward and backward steps.
     */
    private <E> void assertRandomWalk(final ListIterator<E> expected, final ListIterator<E> actual) {
        final StringBuilder walkDescription = new StringBuilder();
        final int walkSteps = 500;

        for (int i = 0; i < walkSteps; i++) {
            assertEquals(expected.nextIndex(), actual.nextIndex(), "Next index mismatch before step " + i);
            assertEquals(expected.previousIndex(), actual.previousIndex(), "Previous index mismatch before step " + i);

            if (random.nextBoolean()) {
                walkDescription.append("F"); // Forward
                if (expected.hasNext()) {
                    assertEquals(expected.next(), actual.next(), "Mismatch on next() during random walk: " + walkDescription);
                }
            } else {
                walkDescription.append("B"); // Backward
                if (expected.hasPrevious()) {
                    assertEquals(expected.previous(), actual.previous(), "Mismatch on previous() during random walk: " + walkDescription);
                }
            }
        }
    }

    /**
     * Asserts that the 'actual' iterator behaves identically to the 'expected'
     * iterator when traversing forward through all remaining elements.
     */
    private void assertWalksForwardCorrectly(final ListIterator<?> expected, final ListIterator<?> actual) {
        while (expected.hasNext()) {
            assertTrue(actual.hasNext(), "Actual iterator should have a next element.");
            assertEquals(expected.nextIndex(), actual.nextIndex(), "Next indices should match before next().");
            assertEquals(expected.previousIndex(), actual.previousIndex(), "Previous indices should match before next().");
            assertEquals(expected.next(), actual.next(), "Elements from next() should match.");
        }
        assertFalse(actual.hasNext(), "Actual iterator should be at the end.");
    }

    /**
     * Asserts that the 'actual' iterator behaves identically to the 'expected'
     * iterator when traversing backward through all preceding elements.
     */
    private void assertWalksBackwardCorrectly(final ListIterator<?> expected, final ListIterator<?> actual) {
        while (expected.hasPrevious()) {
            assertTrue(actual.hasPrevious(), "Actual iterator should have a previous element.");
            assertEquals(expected.nextIndex(), actual.nextIndex(), "Next indices should match before previous().");
            assertEquals(expected.previousIndex(), actual.previousIndex(), "Previous indices should match before previous().");
            assertEquals(expected.previous(), actual.previous(), "Elements from previous() should match.");
        }
        assertFalse(actual.hasPrevious(), "Actual iterator should be at the beginning.");
    }
}