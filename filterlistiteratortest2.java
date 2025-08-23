package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the {@link FilterListIterator}.
 * This class verifies that the iterator correctly filters elements based on a predicate
 * while supporting all operations of a {@link ListIterator}, including complex
 * forward, backward, and mixed traversal patterns.
 */
@DisplayName("FilterListIterator")
public class FilterListIteratorTest {

    private static final int LIST_SIZE = 20;
    private final Random random = new Random();

    private List<Integer> fullList;
    private List<Integer> evens;
    private List<Integer> odds;
    private List<Integer> threes;
    private List<Integer> sixes;

    private Predicate<Integer> truePred;
    private Predicate<Integer> falsePred;
    private Predicate<Integer> evenPred;
    private Predicate<Integer> oddPred;
    private Predicate<Integer> threePred;

    @BeforeEach
    public void setUp() {
        fullList = new ArrayList<>();
        evens = new ArrayList<>();
        odds = new ArrayList<>();
        threes = new ArrayList<>();
        sixes = new ArrayList<>();

        for (int i = 0; i < LIST_SIZE; i++) {
            fullList.add(i);
            if (i % 2 == 0) {
                evens.add(i);
            }
            if (i % 2 != 0) {
                odds.add(i);
            }
            if (i % 3 == 0) {
                threes.add(i);
            }
            if (i % 6 == 0) {
                sixes.add(i);
            }
        }

        truePred = PredicateUtils.truePredicate();
        falsePred = PredicateUtils.falsePredicate();
        evenPred = x -> x % 2 == 0;
        oddPred = x -> x % 2 != 0;
        threePred = x -> x % 3 == 0;
    }

    /**
     * Provides test cases for various predicates.
     * Each case includes a description, the predicate to apply, and the expected resulting list.
     */
    private Stream<Arguments> createPredicateTestCases() {
        return Stream.of(
            Arguments.of("even numbers", evenPred, evens),
            Arguments.of("odd numbers", oddPred, odds),
            Arguments.of("multiples of 3", threePred, threes),
            Arguments.of("multiples of 6 (composite)", PredicateUtils.andPredicate(evenPred, threePred), sixes),
            Arguments.of("all numbers (true predicate)", truePred, fullList),
            Arguments.of("no numbers (false predicate)", falsePred, Collections.emptyList())
        );
    }

    @DisplayName("should correctly filter elements")
    @ParameterizedTest(name = "when filtering for {0}")
    @MethodSource("createPredicateTestCases")
    public void testFilteredIteration(final String description, final Predicate<Integer> predicate, final List<Integer> expected) {
        // Each assertion block tests a different traversal pattern on a fresh iterator
        // to ensure the tests are independent and focused.
        assertSequentialWalk(expected, new FilterListIterator<>(fullList.listIterator(), predicate));
        assertInterleavedWalk(expected, new FilterListIterator<>(fullList.listIterator(), predicate));
        assertRandomWalk(expected, new FilterListIterator<>(fullList.listIterator(), predicate));
    }

    @Test
    @DisplayName("should handle an empty underlying list")
    public void testEmptyList() {
        final List<Integer> emptyList = Collections.emptyList();
        final FilterListIterator<Integer> iterator = new FilterListIterator<>(emptyList.listIterator(), truePred);

        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertEquals(0, iterator.nextIndex());
        assertEquals(-1, iterator.previousIndex());
    }

    /**
     * Asserts that a simple forward walk followed by a backward walk works correctly.
     */
    private <E> void assertSequentialWalk(final List<E> expectedList, final ListIterator<E> actual) {
        final ListIterator<E> expected = expectedList.listIterator();
        walkForward(expected, actual);
        walkBackward(expected, actual);
    }

    /**
     * Asserts that complex, interleaved calls to next() and previous() produce the correct behavior.
     */
    private <E> void assertInterleavedWalk(final List<E> expectedList, final ListIterator<E> actual) {
        final ListIterator<E> expected = expectedList.listIterator();

        // Test a "yo-yo" pattern: next, previous, next
        while (expected.hasNext()) {
            assertEquals(expected.next(), actual.next());
            assertEquals(expected.previous(), actual.previous());
            assertEquals(expected.next(), actual.next());
        }
        walkBackward(expected, actual); // Return to start

        // Test a more complex walk pattern
        for (int i = 0; i < expectedList.size(); i++) {
            // Walk forward i steps
            for (int j = 0; j < i; j++) {
                assertEquals(expected.next(), actual.next());
            }
            // Walk back i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertEquals(expected.previous(), actual.previous());
            }
            // Walk forward i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertEquals(expected.next(), actual.next());
            }
            // Walk back to start
            for (int j = 0; j < i; j++) {
                assertEquals(expected.previous(), actual.previous());
            }
        }
    }

    /**
     * Asserts that the iterator behaves correctly during a long, randomized sequence of
     * next() and previous() calls.
     */
    private <E> void assertRandomWalk(final List<E> expectedList, final ListIterator<E> actual) {
        final ListIterator<E> expected = expectedList.listIterator();
        final StringBuilder walkDescription = new StringBuilder(500);

        for (int i = 0; i < 500; i++) {
            if (random.nextBoolean()) {
                walkDescription.append(" next() ");
                if (expected.hasNext()) {
                    assertTrue(actual.hasNext(), "actual.hasNext() should be true. Walk: " + walkDescription);
                    assertEquals(expected.next(), actual.next(), "next() mismatch. Walk: " + walkDescription);
                }
            } else {
                walkDescription.append(" previous() ");
                if (expected.hasPrevious()) {
                    assertTrue(actual.hasPrevious(), "actual.hasPrevious() should be true. Walk: " + walkDescription);
                    assertEquals(expected.previous(), actual.previous(), "previous() mismatch. Walk: " + walkDescription);
                }
            }
            assertEquals(expected.nextIndex(), actual.nextIndex(), "nextIndex mismatch. Walk: " + walkDescription);
            assertEquals(expected.previousIndex(), actual.previousIndex(), "previousIndex mismatch. Walk: " + walkDescription);
        }
    }

    private void walkForward(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasNext()) {
            assertTrue(testing.hasNext(), "testing iterator should have a next element");
            assertEquals(expected.nextIndex(), testing.nextIndex());
            assertEquals(expected.previousIndex(), testing.previousIndex());
            assertEquals(expected.next(), testing.next());
        }
        assertFalse(testing.hasNext(), "testing iterator should be at the end");
    }

    private void walkBackward(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasPrevious()) {
            assertTrue(testing.hasPrevious(), "testing iterator should have a previous element");
            assertEquals(expected.nextIndex(), testing.nextIndex());
            assertEquals(expected.previousIndex(), testing.previousIndex());
            assertEquals(expected.previous(), testing.previous());
        }
        assertFalse(testing.hasPrevious(), "testing iterator should be at the beginning");
    }
}