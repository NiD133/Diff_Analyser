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
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the FilterListIterator class.
 */
@SuppressWarnings("boxing")
class FilterListIteratorTest {

    private List<Integer> list;
    private List<Integer> odds;
    private List<Integer> evens;
    private List<Integer> threes;
    private List<Integer> fours;
    private List<Integer> sixes;
    private Predicate<Integer> truePredicate;
    private Predicate<Integer> falsePredicate;
    private Predicate<Integer> evenPredicate;
    private Predicate<Integer> oddPredicate;
    private Predicate<Integer> threePredicate;
    private Predicate<Integer> fourPredicate;
    private final Random random = new Random();

    @BeforeEach
    public void setUp() {
        initializeLists();
        initializePredicates();
    }

    @AfterEach
    public void tearDown() {
        clearListsAndPredicates();
    }

    @Test
    void testCollections360() {
        Collection<Predicate<Object>> emptyPredicates = new GrowthList<>();
        Predicate<Object> combinedPredicate = PredicateUtils.anyPredicate(emptyPredicates);
        FilterListIterator<Object> iterator = new FilterListIterator<>(combinedPredicate);
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
    }

    @Test
    void testEvens() {
        testFilterListIterator(evens, evenPredicate);
    }

    @Test
    void testFailingHasNextBug() {
        FilterListIterator<Integer> filtered = new FilterListIterator<>(list.listIterator(), fourPredicate);
        ListIterator<Integer> expected = fours.listIterator();
        iterateToEnd(expected, filtered);
        assertTrue(filtered.hasPrevious());
        assertFalse(filtered.hasNext());
        assertEquals(expected.previous(), filtered.previous());
    }

    @Test
    void testFalsePredicate() {
        testFilterListIterator(new ArrayList<>(), falsePredicate);
    }

    @Test
    void testFours() {
        testFilterListIterator(fours, fourPredicate);
    }

    @Test
    void testManual() {
        FilterListIterator<Integer> filtered = new FilterListIterator<>(list.listIterator(), threePredicate);
        testManualIteration(filtered);
    }

    @Test
    void testNestedSixes() {
        FilterListIterator<Integer> filtered = new FilterListIterator<>(
                new FilterListIterator<>(list.listIterator(), threePredicate),
                evenPredicate
        );
        testFilterListIterator(sixes, filtered);
    }

    @Test
    void testNestedSixes2() {
        FilterListIterator<Integer> filtered = new FilterListIterator<>(
                new FilterListIterator<>(list.listIterator(), evenPredicate),
                threePredicate
        );
        testFilterListIterator(sixes, filtered);
    }

    @Test
    void testNestedSixes3() {
        FilterListIterator<Integer> filtered = new FilterListIterator<>(
                new FilterListIterator<>(list.listIterator(), threePredicate),
                evenPredicate
        );
        testFilterListIterator(sixes, new FilterListIterator<>(filtered, truePredicate));
    }

    @Test
    void testNextChangesPrevious() {
        testNextPreviousBehavior(threes, threePredicate);
        testNextPreviousBehavior(list, truePredicate);
    }

    @Test
    void testOdds() {
        testFilterListIterator(odds, oddPredicate);
    }

    @Test
    void testPreviousChangesNext() {
        testPreviousNextBehavior(threes, threePredicate);
        testPreviousNextBehavior(list, truePredicate);
    }

    @Test
    void testThrees() {
        testFilterListIterator(threes, threePredicate);
    }

    @Test
    void testTruePredicate() {
        testFilterListIterator(list, truePredicate);
    }

    @Test
    void testWalkLists() {
        walkLists(list, list.listIterator());
    }

    // Helper methods

    private void initializeLists() {
        list = new ArrayList<>();
        odds = new ArrayList<>();
        evens = new ArrayList<>();
        threes = new ArrayList<>();
        fours = new ArrayList<>();
        sixes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
            if (i % 2 == 0) evens.add(i);
            if (i % 2 != 0) odds.add(i);
            if (i % 3 == 0) threes.add(i);
            if (i % 4 == 0) fours.add(i);
            if (i % 6 == 0) sixes.add(i);
        }
    }

    private void initializePredicates() {
        truePredicate = x -> true;
        falsePredicate = x -> false;
        evenPredicate = x -> x % 2 == 0;
        oddPredicate = x -> x % 2 != 0;
        threePredicate = x -> x % 3 == 0;
        fourPredicate = x -> x % 4 == 0;
    }

    private void clearListsAndPredicates() {
        list = null;
        odds = null;
        evens = null;
        threes = null;
        fours = null;
        sixes = null;
        truePredicate = null;
        falsePredicate = null;
        evenPredicate = null;
        oddPredicate = null;
        threePredicate = null;
        fourPredicate = null;
    }

    private void testFilterListIterator(List<Integer> expectedList, Predicate<Integer> predicate) {
        FilterListIterator<Integer> filtered = new FilterListIterator<>(list.listIterator(), predicate);
        walkLists(expectedList, filtered);
    }

    private void testManualIteration(FilterListIterator<Integer> filtered) {
        Integer[] expectedSequence = {0, 3, 6, 9, 12, 15, 18};
        for (Integer expected : expectedSequence) {
            assertEquals(expected, filtered.next());
        }
        for (int i = expectedSequence.length - 1; i >= 0; i--) {
            assertEquals(expectedSequence[i], filtered.previous());
        }
        assertFalse(filtered.hasPrevious());
    }

    private void testNextPreviousBehavior(List<Integer> expectedList, Predicate<Integer> predicate) {
        FilterListIterator<Integer> filtered = new FilterListIterator<>(list.listIterator(), predicate);
        nextNextPrevious(expectedList.listIterator(), filtered);
    }

    private void testPreviousNextBehavior(List<Integer> expectedList, Predicate<Integer> predicate) {
        FilterListIterator<Integer> filtered = new FilterListIterator<>(list.listIterator(), predicate);
        ListIterator<Integer> expected = expectedList.listIterator();
        walkForward(expected, filtered);
        previousPreviousNext(expected, filtered);
    }

    private void nextNextPrevious(final ListIterator<?> expected, final ListIterator<?> testing) {
        assertEquals(expected.next(), testing.next());
        assertEquals(expected.hasPrevious(), testing.hasPrevious());
        final Object expectedNext = expected.next();
        final Object testingNext = testing.next();
        assertEquals(expectedNext, testingNext);
        final Object expectedPrevious = expected.previous();
        final Object testingPrevious = testing.previous();
        assertEquals(expectedNext, expectedPrevious);
        assertEquals(testingNext, testingPrevious);
    }

    private void previousPreviousNext(final ListIterator<?> expected, final ListIterator<?> testing) {
        assertEquals(expected.previous(), testing.previous());
        assertEquals(expected.hasNext(), testing.hasNext());
        final Object expectedPrevious = expected.previous();
        final Object testingPrevious = testing.previous();
        assertEquals(expectedPrevious, testingPrevious);
        final Object expectedNext = expected.next();
        final Object testingNext = testing.next();
        assertEquals(expectedPrevious, expectedNext);
        assertEquals(testingPrevious, testingNext);
    }

    private void walkBackward(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasPrevious()) {
            assertEquals(expected.nextIndex(), testing.nextIndex());
            assertEquals(expected.previousIndex(), testing.previousIndex());
            assertTrue(testing.hasPrevious());
            assertEquals(expected.previous(), testing.previous());
        }
    }

    private void walkForward(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasNext()) {
            assertEquals(expected.nextIndex(), testing.nextIndex());
            assertEquals(expected.previousIndex(), testing.previousIndex());
            assertTrue(testing.hasNext());
            assertEquals(expected.next(), testing.next());
        }
    }

    private <E> void walkLists(final List<E> list, final ListIterator<E> testing) {
        final ListIterator<E> expected = list.listIterator();
        walkForward(expected, testing);
        walkBackward(expected, testing);
        randomWalk(expected, testing);
    }

    private <E> void randomWalk(final ListIterator<E> expected, final ListIterator<E> testing) {
        final StringBuilder walkDescription = new StringBuilder(500);
        for (int i = 0; i < 500; i++) {
            if (random.nextBoolean()) {
                walkDescription.append("+");
                if (expected.hasNext()) {
                    assertEquals(expected.next(), testing.next(), walkDescription.toString());
                }
            } else {
                walkDescription.append("-");
                if (expected.hasPrevious()) {
                    assertEquals(expected.previous(), testing.previous(), walkDescription.toString());
                }
            }
            assertEquals(expected.nextIndex(), testing.nextIndex(), walkDescription.toString());
            assertEquals(expected.previousIndex(), testing.previousIndex(), walkDescription.toString());
        }
    }
}