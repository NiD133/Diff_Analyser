package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.collections4.list.GrowthList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for FilterListIterator.
 *
 * The test suite builds several reference lists (full list, evens, odds, multiples)
 * and then verifies that a FilterListIterator over the full list behaves exactly like
 * iterating the corresponding reference list directly. Utility methods compare both
 * value and iterator index contract while walking forward/backward, including a
 * deterministic random walk for broader coverage.
 */
class FilterListIteratorTest {

    private static final int SIZE = 20;
    private static final int RANDOM_WALK_STEPS = 500;
    private static final long RANDOM_SEED = 0xC0FFEE; // deterministic for reproducibility

    private List<Integer> source;     // 0..19
    private List<Integer> odds;       // odd numbers from source
    private List<Integer> evens;      // even numbers from source
    private List<Integer> threes;     // multiples of 3 from source
    private List<Integer> fours;      // multiples of 4 from source
    private List<Integer> sixes;      // multiples of 6 from source

    private Predicate<Integer> alwaysTrue;
    private Predicate<Integer> alwaysFalse;
    private Predicate<Integer> isEven;
    private Predicate<Integer> isOdd;
    private Predicate<Integer> isMultipleOf3;
    private Predicate<Integer> isMultipleOf4;

    private final Random random = new Random(RANDOM_SEED);

    @BeforeEach
    void setUp() {
        source = new ArrayList<>();
        odds = new ArrayList<>();
        evens = new ArrayList<>();
        threes = new ArrayList<>();
        fours = new ArrayList<>();
        sixes = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            source.add(i);
            if (i % 2 == 0) {
                evens.add(i);
            } else {
                odds.add(i);
            }
            if (i % 3 == 0) {
                threes.add(i);
            }
            if (i % 4 == 0) {
                fours.add(i);
            }
            if (i % 6 == 0) {
                sixes.add(i);
            }
        }

        alwaysTrue = x -> true;
        alwaysFalse = x -> false; // fixed: this is the logical "false" predicate
        isEven = x -> x % 2 == 0;
        isOdd = x -> x % 2 != 0;
        isMultipleOf3 = x -> x % 3 == 0;
        isMultipleOf4 = x -> x % 4 == 0;
    }

    @AfterEach
    void tearDown() {
        source = null;
        odds = null;
        evens = null;
        threes = null;
        fours = null;
        sixes = null;
        alwaysTrue = null;
        alwaysFalse = null;
        isEven = null;
        isOdd = null;
        isMultipleOf3 = null;
        isMultipleOf4 = null;
    }

    @Test
    @DisplayName("COLLECTIONS-360: hasNext/hasPrevious on iterator built from anyPredicate(empty)")
    void testCollections360_hasNextAndHasPreviousOnEmptyAnyPredicate() {
        // See: https://issues.apache.org/jira/browse/COLLECTIONS-360
        final Collection<Predicate<Object>> emptyPredicates = new GrowthList<>();
        final Predicate<Object> anyOfNone = PredicateUtils.anyPredicate(emptyPredicates);

        final FilterListIterator<Object> itForHasNext = new FilterListIterator<>(anyOfNone);
        assertFalse(itForHasNext.hasNext(), "No next element should be available");

        final FilterListIterator<Object> itForHasPrevious = new FilterListIterator<>(anyOfNone);
        assertFalse(itForHasPrevious.hasPrevious(), "No previous element should be available");
    }

    @Test
    void testEvens() {
        final FilterListIterator<Integer> filtered = new FilterListIterator<>(source.listIterator(), isEven);
        assertIteratesLike(evens, filtered);
    }

    @Test
    @DisplayName("Regression: hasNext bug when positioned at the end")
    void testFailingHasNextBug() {
        final FilterListIterator<Integer> filtered = new FilterListIterator<>(source.listIterator(), isMultipleOf4);
        final ListIterator<Integer> expected = fours.listIterator();

        // Walk to the end in lock-step
        while (expected.hasNext()) {
            expected.next();
            filtered.next();
        }

        assertTrue(filtered.hasPrevious(), "Should have previous at end");
        assertFalse(filtered.hasNext(), "Should not have next at end");
        assertEquals(expected.previous(), filtered.previous(), "Previous element at end should match");
    }

    @Test
    void testFalsePredicate() {
        final FilterListIterator<Integer> filtered = new FilterListIterator<>(source.listIterator(), alwaysFalse);
        assertIteratesLike(List.of(), filtered);
    }

    @Test
    void testFours() {
        final FilterListIterator<Integer> filtered = new FilterListIterator<>(source.listIterator(), isMultipleOf4);
        assertIteratesLike(fours, filtered);
    }

    @Test
    @DisplayName("Manual sanity check with multiples of 3")
    void testManual() {
        final FilterListIterator<Integer> filtered = new FilterListIterator<>(source.listIterator(), isMultipleOf3);

        // Forward through all multiples of 3
        assertEquals(Integer.valueOf(0), filtered.next());
        assertEquals(Integer.valueOf(3), filtered.next());
        assertEquals(Integer.valueOf(6), filtered.next());
        assertEquals(Integer.valueOf(9), filtered.next());
        assertEquals(Integer.valueOf(12), filtered.next());
        assertEquals(Integer.valueOf(15), filtered.next());
        assertEquals(Integer.valueOf(18), filtered.next());

        // Backward through all multiples of 3
        assertEquals(Integer.valueOf(18), filtered.previous());
        assertEquals(Integer.valueOf(15), filtered.previous());
        assertEquals(Integer.valueOf(12), filtered.previous());
        assertEquals(Integer.valueOf(9), filtered.previous());
        assertEquals(Integer.valueOf(6), filtered.previous());
        assertEquals(Integer.valueOf(3), filtered.previous());
        assertEquals(Integer.valueOf(0), filtered.previous());

        assertFalse(filtered.hasPrevious());

        // Forward again to the end
        assertEquals(Integer.valueOf(0), filtered.next());
        assertEquals(Integer.valueOf(3), filtered.next());
        assertEquals(Integer.valueOf(6), filtered.next());
        assertEquals(Integer.valueOf(9), filtered.next());
        assertEquals(Integer.valueOf(12), filtered.next());
        assertEquals(Integer.valueOf(15), filtered.next());
        assertEquals(Integer.valueOf(18), filtered.next());

        assertFalse(filtered.hasNext());

        // Backward again to the start
        assertEquals(Integer.valueOf(18), filtered.previous());
        assertEquals(Integer.valueOf(15), filtered.previous());
        assertEquals(Integer.valueOf(12), filtered.previous());
        assertEquals(Integer.valueOf(9), filtered.previous());
        assertEquals(Integer.valueOf(6), filtered.previous());
        assertEquals(Integer.valueOf(3), filtered.previous());
        assertEquals(Integer.valueOf(0), filtered.previous());

        // Local oscillations
        assertEquals(Integer.valueOf(0), filtered.next());
        assertEquals(Integer.valueOf(0), filtered.previous());
        assertEquals(Integer.valueOf(0), filtered.next());

        assertEquals(Integer.valueOf(3), filtered.next());
        assertEquals(Integer.valueOf(6), filtered.next());
        assertEquals(Integer.valueOf(6), filtered.previous());
        assertEquals(Integer.valueOf(3), filtered.previous());
        assertEquals(Integer.valueOf(3), filtered.next());
        assertEquals(Integer.valueOf(6), filtered.next());

        assertEquals(Integer.valueOf(9), filtered.next());
        assertEquals(Integer.valueOf(12), filtered.next());
        assertEquals(Integer.valueOf(15), filtered.next());
        assertEquals(Integer.valueOf(15), filtered.previous());
        assertEquals(Integer.valueOf(12), filtered.previous());
        assertEquals(Integer.valueOf(9), filtered.previous());
    }

    @Test
    void testNestedSixes() {
        final FilterListIterator<Integer> filtered =
            new FilterListIterator<>(
                new FilterListIterator<>(source.listIterator(), isMultipleOf3),
                isEven
            );
        assertIteratesLike(sixes, filtered);
    }

    @Test
    void testNestedSixes2() {
        final FilterListIterator<Integer> filtered =
            new FilterListIterator<>(
                new FilterListIterator<>(source.listIterator(), isEven),
                isMultipleOf3
            );
        assertIteratesLike(sixes, filtered);
    }

    @Test
    void testNestedSixes3() {
        final FilterListIterator<Integer> filtered =
            new FilterListIterator<>(
                new FilterListIterator<>(source.listIterator(), isMultipleOf3),
                isEven
            );
        assertIteratesLike(sixes, new FilterListIterator<>(filtered, alwaysTrue));
    }

    @Test
    @DisplayName("Calling next() should affect subsequent previous() result")
    void testNextChangesPrevious() {
        // With filtering
        assertNextNextThenPrevious(threes.listIterator(),
            new FilterListIterator<>(source.listIterator(), isMultipleOf3));

        // Without filtering (identity)
        assertNextNextThenPrevious(source.listIterator(),
            new FilterListIterator<>(source.listIterator(), alwaysTrue));
    }

    @Test
    void testOdds() {
        final FilterListIterator<Integer> filtered = new FilterListIterator<>(source.listIterator(), isOdd);
        assertIteratesLike(odds, filtered);
    }

    @Test
    @DisplayName("Calling previous() should affect subsequent next() result")
    void testPreviousChangesNext() {
        {
            final FilterListIterator<Integer> filtered = new FilterListIterator<>(source.listIterator(), isMultipleOf3);
            final ListIterator<Integer> expected = threes.listIterator();
            walkForward(expected, filtered);
            assertPreviousPreviousThenNext(expected, filtered);
        }
        {
            final FilterListIterator<Integer> filtered = new FilterListIterator<>(source.listIterator(), alwaysTrue);
            final ListIterator<Integer> expected = source.listIterator();
            walkForward(expected, filtered);
            assertPreviousPreviousThenNext(expected, filtered);
        }
    }

    @Test
    void testThrees() {
        final FilterListIterator<Integer> filtered = new FilterListIterator<>(source.listIterator(), isMultipleOf3);
        assertIteratesLike(threes, filtered);
    }

    @Test
    void testTruePredicate() {
        final FilterListIterator<Integer> filtered = new FilterListIterator<>(source.listIterator(), alwaysTrue);
        assertIteratesLike(source, filtered);
    }

    @Test
    @DisplayName("Control: reference iterator walks like itself")
    void testWalkLists() {
        // Sanity check that our verification method is correct by comparing a list iterator to itself.
        assertIteratesLike(source, source.listIterator());
    }

    // --------------------------------------------------------------------------------------------
    // Utilities
    // --------------------------------------------------------------------------------------------

    /**
     * Verifies that the 'testing' iterator yields exactly the same elements and ListIterator
     * indices as iterating 'reference' directly, across a comprehensive set of movements:
     * - full forward walk
     * - full backward walk
     * - forward/back/forward oscillations at each step
     * - partial forward/back/forward/back sweeps for multiple lengths
     * - deterministic random walk of RANDOM_WALK_STEPS steps
     */
    private <E> void assertIteratesLike(final List<E> reference, final ListIterator<E> testing) {
        final ListIterator<E> expected = reference.listIterator();

        // Walk all the way forward
        walkForward(expected, testing);

        // Walk all the way back
        walkBackward(expected, testing);

        // Forward, back, forward oscillation
        while (expected.hasNext()) {
            assertIndicesEqual(expected, testing);
            assertTrue(testing.hasNext());
            assertEquals(expected.next(), testing.next());

            assertTrue(testing.hasPrevious());
            assertEquals(expected.previous(), testing.previous());

            assertTrue(testing.hasNext());
            assertEquals(expected.next(), testing.next());
        }

        // Walk all the way back
        walkBackward(expected, testing);

        // Progressive forward/back patterns
        for (int i = 0; i < reference.size(); i++) {
            // Walk forward i
            for (int j = 0; j < i; j++) {
                assertIndicesEqual(expected, testing);
                assertTrue(expected.hasNext(), "Test logic error: expected must have next");
                assertTrue(testing.hasNext());
                assertEquals(expected.next(), testing.next());
            }
            // Walk back i/2
            for (int j = 0; j < i / 2; j++) {
                assertIndicesEqual(expected, testing);
                assertTrue(expected.hasPrevious(), "Test logic error: expected must have previous");
                assertTrue(testing.hasPrevious());
                assertEquals(expected.previous(), testing.previous());
            }
            // Walk forward i/2
            for (int j = 0; j < i / 2; j++) {
                assertIndicesEqual(expected, testing);
                assertTrue(expected.hasNext(), "Test logic error: expected must have next");
                assertTrue(testing.hasNext());
                assertEquals(expected.next(), testing.next());
            }
            // Walk back i
            for (int j = 0; j < i; j++) {
                assertIndicesEqual(expected, testing);
                assertTrue(expected.hasPrevious(), "Test logic error: expected must have previous");
                assertTrue(testing.hasPrevious());
                assertEquals(expected.previous(), testing.previous());
            }
        }

        // Deterministic random walk for broader state-space coverage
        final StringBuilder walkDescription = new StringBuilder(RANDOM_WALK_STEPS);
        for (int i = 0; i < RANDOM_WALK_STEPS; i++) {
            if (random.nextBoolean()) {
                walkDescription.append('+'); // step forward
                if (expected.hasNext()) {
                    assertEquals(expected.next(), testing.next(), walkDescription.toString());
                }
            } else {
                walkDescription.append('-'); // step backward
                if (expected.hasPrevious()) {
                    assertEquals(expected.previous(), testing.previous(), walkDescription.toString());
                }
            }
            final String msg = walkDescription.toString();
            assertEquals(expected.nextIndex(), testing.nextIndex(), msg);
            assertEquals(expected.previousIndex(), testing.previousIndex(), msg);
        }
    }

    /**
     * Walks forward completely, asserting values and indices match.
     */
    private void walkForward(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasNext()) {
            assertIndicesEqual(expected, testing);
            assertTrue(testing.hasNext());
            assertEquals(expected.next(), testing.next());
        }
    }

    /**
     * Walks backward completely, asserting values and indices match.
     */
    private void walkBackward(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasPrevious()) {
            assertIndicesEqual(expected, testing);
            assertTrue(testing.hasPrevious());
            assertEquals(expected.previous(), testing.previous());
        }
    }

    /**
     * Helper asserting that two iterators report the same indices.
     */
    private void assertIndicesEqual(final ListIterator<?> expected, final ListIterator<?> testing) {
        assertEquals(expected.nextIndex(), testing.nextIndex(), "nextIndex mismatch");
        assertEquals(expected.previousIndex(), testing.previousIndex(), "previousIndex mismatch");
    }

    /**
     * Verifies: calling next() twice shifts the element returned by previous(),
     * even if hasPrevious() has been checked in between.
     */
    private void assertNextNextThenPrevious(final ListIterator<?> expected, final ListIterator<?> testing) {
        assertEquals(expected.next(), testing.next());
        assertEquals(expected.hasPrevious(), testing.hasPrevious());

        final Object expectedSecond = expected.next();
        final Object testingSecond = testing.next();
        assertEquals(expectedSecond, testingSecond);

        final Object expectedPrev = expected.previous();
        final Object testingPrev = testing.previous();

        assertEquals(expectedSecond, expectedPrev);
        assertEquals(testingSecond, testingPrev);
    }

    /**
     * Verifies: calling previous() twice shifts the element returned by next(),
     * even if hasNext() has been checked in between.
     */
    private void assertPreviousPreviousThenNext(final ListIterator<?> expected, final ListIterator<?> testing) {
        assertEquals(expected.previous(), testing.previous());
        assertEquals(expected.hasNext(), testing.hasNext());

        final Object expectedSecond = expected.previous();
        final Object testingSecond = testing.previous();
        assertEquals(expectedSecond, testingSecond);

        final Object expectedNext = expected.next();
        final Object testingNext = testing.next();

        assertEquals(expectedSecond, expectedNext);
        assertEquals(testingSecond, testingNext);
    }
}