package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the FilterListIterator against a series of complex traversal patterns.
 * <p>
 * This test suite verifies that the FilterListIterator correctly maintains its internal
 * state during various sequences of forward, backward, and mixed navigation,
 * ensuring it behaves identically to a standard ListIterator on a pre-filtered list.
 * </p>
 */
@DisplayName("FilterListIterator Walk Test")
class FilterListIteratorWalkTest {

    private List<Integer> list;
    private List<Integer> expectedElements; // Multiples of 6
    private Predicate<Integer> evenPredicate;
    private Predicate<Integer> multipleOfThreePredicate;
    private final Random random = new Random();

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();
        expectedElements = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
            if (i % 6 == 0) {
                expectedElements.add(i);
            }
        }

        evenPredicate = x -> x % 2 == 0;
        multipleOfThreePredicate = x -> x % 3 == 0;
    }

    @Test
    @DisplayName("Nested filters with even and multiples-of-three predicates should yield multiples of six")
    void testNestedFilters_withEvenAndMultiplesOfThree_yieldsMultiplesOfSix() {
        // Nest two filters: one for multiples of three, and an outer one for even numbers.
        // The result should be an iterator over numbers that are multiples of both 2 and 3 (i.e., multiples of 6).
        final ListIterator<Integer> innerIterator = new FilterListIterator<>(list.listIterator(), multipleOfThreePredicate);
        final ListIterator<Integer> filteredIterator = new FilterListIterator<>(innerIterator, evenPredicate);

        verifyIteratorBehavesAsExpected(expectedElements, filteredIterator);
    }

    /**
     * Verifies that the iterator under test behaves identically to a standard ListIterator
     * over the expected elements, using a series of rigorous traversal patterns.
     *
     * @param expected The list of elements the iterator is expected to return.
     * @param testingIterator The FilterListIterator instance to test.
     * @param <E> The type of elements in the iterator.
     */
    private <E> void verifyIteratorBehavesAsExpected(final List<E> expected, final ListIterator<E> testingIterator) {
        final ListIterator<E> expectedIterator = expected.listIterator();

        // Each verification routine tests a specific traversal behavior.
        // The iterator's state is managed between routines to ensure valid preconditions.
        verifyFullTraversal(expectedIterator, testingIterator);
        verifyInterleavedTraversal(expectedIterator, testingIterator);

        // Reset iterators to the beginning for the next set of tests.
        walkBackward(expectedIterator, testingIterator);

        verifyComplexStepTraversal(expectedIterator, testingIterator, expected.size());
        verifyRandomWalk(expectedIterator, testingIterator);
    }

    /**
     * Verifies a full forward traversal followed by a full backward traversal.
     * Leaves both iterators at the beginning.
     */
    private <E> void verifyFullTraversal(final ListIterator<E> expected, final ListIterator<E> testing) {
        walkForward(expected, testing);
        walkBackward(expected, testing);
    }

    /**
     * Verifies traversal with interleaved next() and previous() calls.
     * Assumes iterators start at the beginning and leaves them at the end.
     */
    private <E> void verifyInterleavedTraversal(final ListIterator<E> expected, final ListIterator<E> testing) {
        while (expected.hasNext()) {
            assertEquals(expected.next(), testing.next(), "Interleaved: next() failed");
            assertEquals(expected.previous(), testing.previous(), "Interleaved: previous() after next() failed");
            assertEquals(expected.next(), testing.next(), "Interleaved: next() after previous() failed");
        }
    }

    /**
     * Verifies a complex stepping pattern: forward i, back i/2, forward i/2, back i.
     * This pattern is repeated for i from 0 to the list size.
     * Assumes iterators start at the beginning and leaves them there.
     */
    private <E> void verifyComplexStepTraversal(final ListIterator<E> expected, final ListIterator<E> testing, final int size) {
        for (int i = 0; i < size; i++) {
            // Walk forward i steps
            for (int j = 0; j < i; j++) {
                assertEquals(expected.next(), testing.next(), "Complex walk: forward failed");
            }
            // Walk back i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertEquals(expected.previous(), testing.previous(), "Complex walk: backward failed");
            }
            // Walk forward i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertEquals(expected.next(), testing.next(), "Complex walk: forward again failed");
            }
            // Walk back i steps to return to the start of this sub-test
            for (int j = 0; j < i; j++) {
                assertEquals(expected.previous(), testing.previous(), "Complex walk: backward to start failed");
            }
        }
    }

    /**
     * Verifies iterator behavior over a long, randomized sequence of next() and previous() calls.
     * Assumes iterators start at the beginning.
     */
    private <E> void verifyRandomWalk(final ListIterator<E> expected, final ListIterator<E> testing) {
        final StringBuilder walkDescription = new StringBuilder(500);
        for (int i = 0; i < 500; i++) {
            if (random.nextBoolean()) {
                walkDescription.append("+"); // Represents next()
                if (expected.hasNext()) {
                    assertEquals(expected.next(), testing.next(), "Random walk failed on next(): " + walkDescription);
                }
            } else {
                walkDescription.append("-"); // Represents previous()
                if (expected.hasPrevious()) {
                    assertEquals(expected.previous(), testing.previous(), "Random walk failed on previous(): " + walkDescription);
                }
            }
            assertEquals(expected.nextIndex(), testing.nextIndex(), "Random walk failed on nextIndex(): " + walkDescription);
            assertEquals(expected.previousIndex(), testing.previousIndex(), "Random walk failed on previousIndex(): " + walkDescription);
        }
    }

    /**
     * Traverses both iterators forward to the end, asserting equality at each step.
     */
    private void walkForward(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasNext()) {
            assertTrue(testing.hasNext(), "Testing iterator should have next element");
            assertEquals(expected.nextIndex(), testing.nextIndex());
            assertEquals(expected.previousIndex(), testing.previousIndex());
            assertEquals(expected.next(), testing.next());
        }
    }

    /**
     * Traverses both iterators backward to the beginning, asserting equality at each step.
     */
    private void walkBackward(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasPrevious()) {
            assertTrue(testing.hasPrevious(), "Testing iterator should have previous element");
            assertEquals(expected.nextIndex(), testing.nextIndex());
            assertEquals(expected.previousIndex(), testing.previousIndex());
            assertEquals(expected.previous(), testing.previous());
        }
    }
}