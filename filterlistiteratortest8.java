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
 * Performs a comprehensive set of walk-through tests on a {@link FilterListIterator}.
 *
 * <p>This test class verifies that a {@code FilterListIterator}, especially a nested one,
 * behaves identically to a standard {@link ListIterator} on a pre-filtered list. It checks
 * various complex traversal patterns to ensure the iterator's internal state is managed correctly.
 */
class FilterListIteratorComprehensiveWalkTest {

    private static final int LIST_SIZE = 20;
    private static final int RANDOM_WALK_STEPS = 500;

    private List<Integer> fullList;
    private List<Integer> expectedSixes;

    private Predicate<Integer> isEven;
    private Predicate<Integer> isDivisibleByThree;

    private final Random random = new Random();

    @BeforeEach
    void setUp() {
        fullList = new ArrayList<>();
        expectedSixes = new ArrayList<>();

        for (int i = 0; i < LIST_SIZE; i++) {
            fullList.add(i);
            if (i % 6 == 0) {
                expectedSixes.add(i);
            }
        }

        isEven = n -> n % 2 == 0;
        isDivisibleByThree = n -> n % 3 == 0;
    }

    @Test
    void whenIteratingWithNestedFilters_thenBehavesLikeCorrectlyPreFilteredList() {
        // Arrange: Create a nested filter iterator.
        // It first filters for even numbers, then for numbers divisible by three.
        // The result should be numbers divisible by six.
        final ListIterator<Integer> listIterator = fullList.listIterator();
        final FilterListIterator<Integer> evenFilter = new FilterListIterator<>(listIterator, isEven);
        final FilterListIterator<Integer> nestedFilter = new FilterListIterator<>(evenFilter, isDivisibleByThree);

        // Act & Assert: Verify the iterator behaves identically to an iterator over the expected list.
        assertIteratorBehavesAsExpected(expectedSixes, nestedFilter);
    }

    /**
     * Asserts that the actual iterator behaves identically to the expected iterator
     * through a series of sequential, stateful traversal tests.
     *
     * @param expectedElements the list of elements the iterator is expected to produce.
     * @param actualIterator   the {@link FilterListIterator} under test.
     */
    private <E> void assertIteratorBehavesAsExpected(final List<E> expectedElements, final ListIterator<E> actualIterator) {
        final ListIterator<E> expectedIterator = expectedElements.listIterator();

        assertWalksForwardAndThenBackward(expectedIterator, actualIterator);
        assertWalksWithAlternatingNextAndPrevious(expectedIterator, actualIterator);
        assertWalksWithComplexPattern(expectedElements.size(), expectedIterator, actualIterator);
        assertWalksRandomly(expectedIterator, actualIterator);
    }

    /**
     * Asserts that a full forward traversal followed by a full backward traversal
     * works correctly. Leaves both iterators at the start.
     */
    private <E> void assertWalksForwardAndThenBackward(final ListIterator<E> expected, final ListIterator<E> actual) {
        walkForward(expected, actual);
        walkBackward(expected, actual);
    }

    /**
     * Asserts that alternating between next() and previous() calls maintains the
     * correct iterator state. Leaves both iterators at the start.
     */
    private <E> void assertWalksWithAlternatingNextAndPrevious(final ListIterator<E> expected, final ListIterator<E> actual) {
        while (expected.hasNext()) {
            assertNextEquals(expected, actual);
            assertPreviousEquals(expected, actual);
            assertNextEquals(expected, actual);
        }
        // Return to start for the next test.
        walkBackward(expected, actual);
    }

    /**
     * Asserts correct behavior during a complex, structured walk pattern.
     * Leaves both iterators at the start.
     */
    private <E> void assertWalksWithComplexPattern(final int size, final ListIterator<E> expected, final ListIterator<E> actual) {
        for (int i = 0; i < size; i++) {
            // Walk forward i steps
            for (int j = 0; j < i; j++) {
                assertNextEquals(expected, actual);
            }
            // Walk back i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertPreviousEquals(expected, actual);
            }
            // Walk forward i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertNextEquals(expected, actual);
            }
            // Walk back i steps to return to the start
            for (int j = 0; j < i; j++) {
                assertPreviousEquals(expected, actual);
            }
        }
    }

    /**
     * Asserts correct behavior during a long, randomized sequence of forward and
     * backward steps.
     */
    private <E> void assertWalksRandomly(final ListIterator<E> expected, final ListIterator<E> actual) {
        final StringBuilder walkDescription = new StringBuilder(RANDOM_WALK_STEPS * 10);

        for (int i = 0; i < RANDOM_WALK_STEPS; i++) {
            if (random.nextBoolean()) {
                walkDescription.append(" next()");
                if (expected.hasNext()) {
                    assertNextEquals(expected, actual, "Mismatch after walk: " + walkDescription);
                } else {
                    assertFalse(actual.hasNext(), "Actual iterator should not have next. Walk: " + walkDescription);
                }
            } else {
                walkDescription.append(" previous()");
                if (expected.hasPrevious()) {
                    assertPreviousEquals(expected, actual, "Mismatch after walk: " + walkDescription);
                } else {
                    assertFalse(actual.hasPrevious(), "Actual iterator should not have previous. Walk: " + walkDescription);
                }
            }
        }
    }

    // Core traversal and assertion helpers

    private <E> void walkForward(final ListIterator<E> expected, final ListIterator<E> actual) {
        while (expected.hasNext()) {
            assertNextEquals(expected, actual);
        }
    }

    private <E> void walkBackward(final ListIterator<E> expected, final ListIterator<E> actual) {
        while (expected.hasPrevious()) {
            assertPreviousEquals(expected, actual);
        }
    }

    private <E> void assertNextEquals(final ListIterator<E> expected, final ListIterator<E> actual) {
        assertNextEquals(expected, actual, "");
    }

    private <E> void assertNextEquals(final ListIterator<E> expected, final ListIterator<E> actual, final String message) {
        assertEquals(expected.nextIndex(), actual.nextIndex(), "Mismatched nextIndex. " + message);
        assertEquals(expected.previousIndex(), actual.previousIndex(), "Mismatched previousIndex. " + message);
        assertTrue(actual.hasNext(), "Actual iterator has no next. " + message);
        assertEquals(expected.next(), actual.next(), "Mismatched next() element. " + message);
    }

    private <E> void assertPreviousEquals(final ListIterator<E> expected, final ListIterator<E> actual) {
        assertPreviousEquals(expected, actual, "");
    }

    private <E> void assertPreviousEquals(final ListIterator<E> expected, final ListIterator<E> actual, final String message) {
        assertEquals(expected.nextIndex(), actual.nextIndex(), "Mismatched nextIndex. " + message);
        assertEquals(expected.previousIndex(), actual.previousIndex(), "Mismatched previousIndex. " + message);
        assertTrue(actual.hasPrevious(), "Actual iterator has no previous. " + message);
        assertEquals(expected.previous(), actual.previous(), "Mismatched previous() element. " + message);
    }
}