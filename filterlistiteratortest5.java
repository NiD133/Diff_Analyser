package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests complex traversal scenarios for the {@link FilterListIterator}.
 *
 * <p>This test verifies that the FilterListIterator behaves identically to a standard
 * ListIterator on a pre-filtered list during various complex walk patterns, including
 * forward, backward, alternating, and random traversals.
 */
class FilterListIteratorComplexWalkTest {

    private List<Integer> sourceList;
    private final Random random = new Random();

    @BeforeEach
    void setUp() {
        sourceList = IntStream.range(0, 20).boxed().collect(Collectors.toList());
    }

    /**
     * Provides test cases with different predicates and their corresponding
     * expected lists of filtered results.
     */
    static Stream<Arguments> providePredicatesAndExpectedLists() {
        final List<Integer> list = IntStream.range(0, 20).boxed().collect(Collectors.toList());

        final Predicate<Integer> evenPred = x -> x % 2 == 0;
        final Predicate<Integer> oddPred = x -> x % 2 != 0;
        final Predicate<Integer> threePred = x -> x % 3 == 0;
        final Predicate<Integer> fourPred = x -> x % 4 == 0;

        return Stream.of(
            Arguments.of("Evens", evenPred, list.stream().filter(evenPred).collect(Collectors.toList())),
            Arguments.of("Odds", oddPred, list.stream().filter(oddPred).collect(Collectors.toList())),
            Arguments.of("Divisible by 3", threePred, list.stream().filter(threePred).collect(Collectors.toList())),
            Arguments.of("Divisible by 4", fourPred, list.stream().filter(fourPred).collect(Collectors.toList()))
        );
    }

    @ParameterizedTest(name = "Run {index}: {0}")
    @MethodSource("providePredicatesAndExpectedLists")
    void testIteratorBehavesCorrectlyDuringComplexWalks(
            final String description, final Predicate<Integer> predicate, final List<Integer> expectedList) {

        final ListIterator<Integer> filterListIterator = new FilterListIterator<>(sourceList.listIterator(), predicate);
        performComplexWalksAndAssert(expectedList, filterListIterator);
    }

    /**
     * Orchestrates a series of complex traversals on the iterator, asserting its
     * state and behavior against a standard iterator at each step.
     *
     * @param expectedList The list of elements the filtered iterator is expected to return.
     * @param testingIterator The {@link FilterListIterator} instance under test.
     */
    private <E> void performComplexWalksAndAssert(final List<E> expectedList, final ListIterator<E> testingIterator) {
        final ListIterator<E> expectedIterator = expectedList.listIterator();

        // 1. Full walk forward
        assertWalkForward(expectedIterator, testingIterator);

        // 2. Full walk backward
        assertWalkBackward(expectedIterator, testingIterator);

        // 3. Alternating walk: next, previous, next
        assertAlternatingNextPrevious(expectedIterator, testingIterator);

        // 4. Reset to start by walking all the way back
        assertWalkBackward(expectedIterator, testingIterator);

        // 5. A series of partial forward and backward walks
        assertComplexPartialWalks(expectedIterator, testingIterator, expectedList.size());

        // 6. A long random walk
        assertRandomWalk(expectedIterator, testingIterator);
    }

    /**
     * Asserts that the testing iterator traverses forward correctly.
     */
    private void assertWalkForward(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasNext()) {
            assertTrue(testing.hasNext(), "testing iterator should have a next element");
            assertEquals(expected.nextIndex(), testing.nextIndex());
            assertEquals(expected.previousIndex(), testing.previousIndex());
            assertEquals(expected.next(), testing.next());
        }
    }

    /**
     * Asserts that the testing iterator traverses backward correctly.
     */
    private void assertWalkBackward(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasPrevious()) {
            assertTrue(testing.hasPrevious(), "testing iterator should have a previous element");
            assertEquals(expected.nextIndex(), testing.nextIndex());
            assertEquals(expected.previousIndex(), testing.previousIndex());
            assertEquals(expected.previous(), testing.previous());
        }
    }

    /**
     * Asserts correct behavior when alternating between next() and previous() calls.
     */
    private void assertAlternatingNextPrevious(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasNext()) {
            assertTrue(testing.hasNext());
            assertEquals(expected.next(), testing.next());

            assertTrue(testing.hasPrevious());
            assertEquals(expected.previous(), testing.previous());

            assertTrue(testing.hasNext());
            assertEquals(expected.next(), testing.next());
        }
    }

    /**
     * Performs a series of increasingly longer partial walks to stress test the iterator's state.
     * For each step `i`, it walks forward `i` times, back `i/2`, forward `i/2`, and finally back `i` steps.
     */
    private void assertComplexPartialWalks(final ListIterator<?> expected, final ListIterator<?> testing, final int size) {
        for (int i = 0; i <= size; i++) {
            // Walk forward i steps
            for (int j = 0; j < i; j++) {
                assertTrue(expected.hasNext());
                assertEquals(expected.next(), testing.next());
            }
            // Walk back i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertTrue(expected.hasPrevious());
                assertEquals(expected.previous(), testing.previous());
            }
            // Walk forward i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertTrue(expected.hasNext());
                assertEquals(expected.next(), testing.next());
            }
            // Walk back i steps to return to the start
            for (int j = 0; j < i; j++) {
                assertTrue(expected.hasPrevious());
                assertEquals(expected.previous(), testing.previous());
            }
        }
    }

    /**
     * Asserts correct behavior during a long, randomized sequence of next() and previous() calls.
     */
    private void assertRandomWalk(final ListIterator<?> expected, final ListIterator<?> testing) {
        final StringBuilder walkDescription = new StringBuilder(500);
        for (int i = 0; i < 500; i++) {
            if (random.nextBoolean()) {
                walkDescription.append(" next() ");
                if (expected.hasNext()) {
                    assertTrue(testing.hasNext(), "Mismatch in hasNext() during random walk");
                    assertEquals(expected.next(), testing.next(), "Mismatch on next() after sequence: " + walkDescription);
                }
            } else {
                walkDescription.append(" previous() ");
                if (expected.hasPrevious()) {
                    assertTrue(testing.hasPrevious(), "Mismatch in hasPrevious() during random walk");
                    assertEquals(expected.previous(), testing.previous(), "Mismatch on previous() after sequence: " + walkDescription);
                }
            }
            assertEquals(expected.nextIndex(), testing.nextIndex(), "Mismatch in nextIndex() after sequence: " + walkDescription);
            assertEquals(expected.previousIndex(), testing.previousIndex(), "Mismatch in previousIndex() after sequence: " + walkDescription);
        }
    }
}