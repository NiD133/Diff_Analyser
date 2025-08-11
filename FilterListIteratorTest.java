/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.collections4.list.GrowthList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the FilterListIterator class.
 */
@SuppressWarnings("boxing")
class FilterListIteratorTest {

    private List<Integer> list;
    private List<Integer> odds;
    private List<Integer> evens;
    private List<Integer> multiplesOfThree;
    private List<Integer> multiplesOfFour;
    private List<Integer> multiplesOfSix;
    
    private Predicate<Integer> alwaysTruePredicate;
    private Predicate<Integer> alwaysFalsePredicate;
    private Predicate<Integer> evenNumberPredicate;
    private Predicate<Integer> oddNumberPredicate;
    private Predicate<Integer> multipleOfThreePredicate;
    private Predicate<Integer> multipleOfFourPredicate;

    @BeforeEach
    public void setUp() {
        list = new ArrayList<>();
        odds = new ArrayList<>();
        evens = new ArrayList<>();
        multiplesOfThree = new ArrayList<>();
        multiplesOfFour = new ArrayList<>();
        multiplesOfSix = new ArrayList<>();
        
        for (int i = 0; i < 20; i++) {
            list.add(i);
            
            if (i % 2 == 0) {
                evens.add(i);
            } else {
                odds.add(i);
            }
            
            if (i % 3 == 0) {
                multiplesOfThree.add(i);
            }
            
            if (i % 4 == 0) {
                multiplesOfFour.add(i);
            }
            
            if (i % 6 == 0) {
                multiplesOfSix.add(i);
            }
        }

        alwaysTruePredicate = x -> true;
        alwaysFalsePredicate = x -> false;
        evenNumberPredicate = x -> x % 2 == 0;
        oddNumberPredicate = x -> x % 2 != 0;
        multipleOfThreePredicate = x -> x % 3 == 0;
        multipleOfFourPredicate = x -> x % 4 == 0;
    }

    @AfterEach
    public void tearDown() {
        list = null;
        odds = null;
        evens = null;
        multiplesOfThree = null;
        multiplesOfFour = null;
        multiplesOfSix = null;
        alwaysTruePredicate = null;
        alwaysFalsePredicate = null;
        evenNumberPredicate = null;
        oddNumberPredicate = null;
        multipleOfThreePredicate = null;
        multipleOfFourPredicate = null;
    }

    // =======================================================================
    //  Test Cases for Edge Cases
    // =======================================================================

    /**
     * Tests that iterator behaves correctly when using an empty predicate.
     * (Original test case for COLLECTIONS-360)
     */
    @Test
    void testIteratorWithEmptyPredicate() {
        final Collection<Predicate<Object>> emptyPredicates = new GrowthList<>();
        final Predicate<Object> combinedPredicate = PredicateUtils.anyPredicate(emptyPredicates);
        
        final FilterListIterator<Object> forwardIterator = new FilterListIterator<>(combinedPredicate);
        assertFalse(forwardIterator.hasNext());
        
        final FilterListIterator<Object> backwardIterator = new FilterListIterator<>(combinedPredicate);
        assertFalse(backwardIterator.hasPrevious());
    }

    // =======================================================================
    //  Test Cases for Single Filters
    // =======================================================================

    @Test
    void testAlwaysTruePredicate() {
        final FilterListIterator<Integer> iterator = 
            new FilterListIterator<>(list.listIterator(), alwaysTruePredicate);
        walkLists(list, iterator);
    }

    @Test
    void testAlwaysFalsePredicate() {
        final FilterListIterator<Integer> iterator = 
            new FilterListIterator<>(list.listIterator(), alwaysFalsePredicate);
        walkLists(new ArrayList<>(), iterator);
    }

    @Test
    void testEvenNumberFilter() {
        final FilterListIterator<Integer> iterator = 
            new FilterListIterator<>(list.listIterator(), evenNumberPredicate);
        walkLists(evens, iterator);
    }

    @Test
    void testOddNumberFilter() {
        final FilterListIterator<Integer> iterator = 
            new FilterListIterator<>(list.listIterator(), oddNumberPredicate);
        walkLists(odds, iterator);
    }

    @Test
    void testMultipleOfThreeFilter() {
        final FilterListIterator<Integer> iterator = 
            new FilterListIterator<>(list.listIterator(), multipleOfThreePredicate);
        walkLists(multiplesOfThree, iterator);
    }

    @Test
    void testMultipleOfFourFilter() {
        final FilterListIterator<Integer> iterator = 
            new FilterListIterator<>(list.listIterator(), multipleOfFourPredicate);
        walkLists(multiplesOfFour, iterator);
    }

    // =======================================================================
    //  Test Cases for Nested Filters
    // =======================================================================

    @Test
    void testNestedFilters_ThreeThenEven() {
        final FilterListIterator<Integer> firstFilter = 
            new FilterListIterator<>(list.listIterator(), multipleOfThreePredicate);
        final FilterListIterator<Integer> iterator = 
            new FilterListIterator<>(firstFilter, evenNumberPredicate);
        walkLists(multiplesOfSix, iterator);
    }

    @Test
    void testNestedFilters_EvenThenThree() {
        final FilterListIterator<Integer> firstFilter = 
            new FilterListIterator<>(list.listIterator(), evenNumberPredicate);
        final FilterListIterator<Integer> iterator = 
            new FilterListIterator<>(firstFilter, multipleOfThreePredicate);
        walkLists(multiplesOfSix, iterator);
    }

    @Test
    void testNestedFilters_ThreeThenEvenThenTrue() {
        final FilterListIterator<Integer> firstFilter = 
            new FilterListIterator<>(list.listIterator(), multipleOfThreePredicate);
        final FilterListIterator<Integer> secondFilter = 
            new FilterListIterator<>(firstFilter, evenNumberPredicate);
        final FilterListIterator<Integer> iterator = 
            new FilterListIterator<>(secondFilter, alwaysTruePredicate);
        walkLists(multiplesOfSix, iterator);
    }

    // =======================================================================
    //  Test Cases for Iterator Behavior
    // =======================================================================

    @Test
    void testHasNextAfterLastElement() {
        final FilterListIterator<Integer> iterator = 
            new FilterListIterator<>(list.listIterator(), multipleOfFourPredicate);
        
        // Traverse to end
        final ListIterator<Integer> expected = multiplesOfFour.listIterator();
        while (expected.hasNext()) {
            expected.next();
            iterator.next();
        }
        
        // Verify terminal state
        assertTrue(iterator.hasPrevious());
        assertFalse(iterator.hasNext());
        
        // Verify previous returns last element
        assertEquals(expected.previous(), iterator.previous());
    }

    @Test
    void testNextThenPrevious() {
        // Test with multiple-of-three filter
        FilterListIterator<Integer> iterator = 
            new FilterListIterator<>(list.listIterator(), multipleOfThreePredicate);
        assertNextThenPrevious(multiplesOfThree.listIterator(), iterator);
        
        // Test with always-true filter
        iterator = new FilterListIterator<>(list.listIterator(), alwaysTruePredicate);
        assertNextThenPrevious(list.listIterator(), iterator);
    }

    @Test
    void testPreviousThenNext() {
        // Test with multiple-of-three filter
        FilterListIterator<Integer> iterator = 
            new FilterListIterator<>(list.listIterator(), multipleOfThreePredicate);
        ListIterator<Integer> expected = multiplesOfThree.listIterator();
        walkForward(expected, iterator);
        assertPreviousThenNext(expected, iterator);
        
        // Test with always-true filter
        iterator = new FilterListIterator<>(list.listIterator(), alwaysTruePredicate);
        expected = list.listIterator();
        walkForward(expected, iterator);
        assertPreviousThenNext(expected, iterator);
    }

    // =======================================================================
    //  Manual Walk-Through Test (Comprehensive behavior verification)
    // =======================================================================

    @Test
    void testComprehensiveIteratorBehavior() {
        final FilterListIterator<Integer> iterator = 
            new FilterListIterator<>(list.listIterator(), multipleOfThreePredicate);

        // Forward traversal
        assertEquals(0, iterator.next());
        assertEquals(3, iterator.next());
        assertEquals(6, iterator.next());
        assertEquals(9, iterator.next());
        assertEquals(12, iterator.next());
        assertEquals(15, iterator.next());
        assertEquals(18, iterator.next());

        // Backward traversal
        assertEquals(18, iterator.previous());
        assertEquals(15, iterator.previous());
        assertEquals(12, iterator.previous());
        assertEquals(9, iterator.previous());
        assertEquals(6, iterator.previous());
        assertEquals(3, iterator.previous());
        assertEquals(0, iterator.previous());

        // Verify beginning state
        assertFalse(iterator.hasPrevious());

        // Forward traversal again
        assertEquals(0, iterator.next());
        assertEquals(3, iterator.next());
        assertEquals(6, iterator.next());
        assertEquals(9, iterator.next());
        assertEquals(12, iterator.next());
        assertEquals(15, iterator.next());
        assertEquals(18, iterator.next());

        // Verify end state
        assertFalse(iterator.hasNext());

        // Backward traversal again
        assertEquals(18, iterator.previous());
        assertEquals(15, iterator.previous());
        assertEquals(12, iterator.previous());
        assertEquals(9, iterator.previous());
        assertEquals(6, iterator.previous());
        assertEquals(3, iterator.previous());
        assertEquals(0, iterator.previous());

        // Single step forward and back at start
        assertEquals(0, iterator.next());
        assertEquals(0, iterator.previous());
        assertEquals(0, iterator.next());

        // Step forward two, then back one
        assertEquals(3, iterator.next());
        assertEquals(6, iterator.next());
        assertEquals(6, iterator.previous());
        assertEquals(3, iterator.previous());
        assertEquals(3, iterator.next());
        assertEquals(6, iterator.next());

        // Continue forward, then backward steps
        assertEquals(9, iterator.next());
        assertEquals(12, iterator.next());
        assertEquals(15, iterator.next());
        assertEquals(15, iterator.previous());
        assertEquals(12, iterator.previous());
        assertEquals(9, iterator.previous());
    }

    // =======================================================================
    //  Helper Methods
    // =======================================================================

    /**
     * Verifies that calling next() twice then previous() returns the same element,
     * even after hasPrevious() has been called.
     */
    private void assertNextThenPrevious(final ListIterator<?> expected, final ListIterator<?> actual) {
        // First next
        assertEquals(expected.next(), actual.next());
        // Check hasPrevious state
        assertEquals(expected.hasPrevious(), actual.hasPrevious());
        // Second next
        final Object expectedNext = expected.next();
        final Object actualNext = actual.next();
        assertEquals(expectedNext, actualNext);
        // Previous should return the same element
        final Object expectedPrev = expected.previous();
        final Object actualPrev = actual.previous();
        assertEquals(expectedNext, expectedPrev);
        assertEquals(actualNext, actualPrev);
    }

    /**
     * Verifies that calling previous() twice then next() returns the same element,
     * even after hasNext() has been called.
     */
    private void assertPreviousThenNext(final ListIterator<?> expected, final ListIterator<?> actual) {
        // First previous
        assertEquals(expected.previous(), actual.previous());
        // Check hasNext state
        assertEquals(expected.hasNext(), actual.hasNext());
        // Second previous
        final Object expectedPrev = expected.previous();
        final Object actualPrev = actual.previous();
        assertEquals(expectedPrev, actualPrev);
        // Next should return the same element
        final Object expectedNext = expected.next();
        final Object actualNext = actual.next();
        assertEquals(expectedPrev, expectedNext);
        assertEquals(expectedPrev, actualNext);
    }

    /**
     * Walks forward through entire iterator verifying each step.
     */
    private void walkForward(final ListIterator<?> expected, final ListIterator<?> actual) {
        while (expected.hasNext()) {
            assertEquals(expected.nextIndex(), actual.nextIndex());
            assertEquals(expected.previousIndex(), actual.previousIndex());
            assertTrue(actual.hasNext());
            assertEquals(expected.next(), actual.next());
        }
    }

    /**
     * Walks backward through entire iterator verifying each step.
     */
    private void walkBackward(final ListIterator<?> expected, final ListIterator<?> actual) {
        while (expected.hasPrevious()) {
            assertEquals(expected.nextIndex(), actual.nextIndex());
            assertEquals(expected.previousIndex(), actual.previousIndex());
            assertTrue(actual.hasPrevious());
            assertEquals(expected.previous(), actual.previous());
        }
    }

    /**
     * Comprehensive iterator walk-through that verifies:
     * 1. Full forward traversal
     * 2. Full backward traversal
     * 3. Alternating forward/backward steps
     * 4. Indexed forward/backward sequences
     */
    private <E> void walkLists(final List<E> expectedList, final ListIterator<E> actualIterator) {
        final ListIterator<E> expectedIterator = expectedList.listIterator();

        // 1. Walk all the way forward
        walkForward(expectedIterator, actualIterator);

        // 2. Walk all the way back
        walkBackward(expectedIterator, actualIterator);

        // 3. Forward/backward alternating pattern
        while (expectedIterator.hasNext()) {
            // Verify indices
            assertEquals(expectedIterator.nextIndex(), actualIterator.nextIndex());
            assertEquals(expectedIterator.previousIndex(), actualIterator.previousIndex());
            
            // Step forward and verify
            assertTrue(actualIterator.hasNext());
            final E expectedNext = expectedIterator.next();
            final E actualNext = actualIterator.next();
            assertEquals(expectedNext, actualNext);
            
            // Step backward and verify
            assertTrue(actualIterator.hasPrevious());
            final E expectedPrev = expectedIterator.previous();
            final E actualPrev = actualIterator.previous();
            assertEquals(expectedPrev, actualPrev);
            
            // Step forward again and verify
            assertTrue(actualIterator.hasNext());
            final E expectedNextAgain = expectedIterator.next();
            final E actualNextAgain = actualIterator.next();
            assertEquals(expectedNextAgain, actualNextAgain);
        }

        // 4. Index-based sequences
        for (int i = 0; i < expectedList.size(); i++) {
            // Walk forward i steps
            for (int j = 0; j < i; j++) {
                assertEquals(expectedIterator.nextIndex(), actualIterator.nextIndex());
                assertEquals(expectedIterator.previousIndex(), actualIterator.previousIndex());
                assertTrue(expectedIterator.hasNext());
                assertTrue(actualIterator.hasNext());
                assertEquals(expectedIterator.next(), actualIterator.next());
            }

            // Walk backward i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertEquals(expectedIterator.nextIndex(), actualIterator.nextIndex());
                assertEquals(expectedIterator.previousIndex(), actualIterator.previousIndex());
                assertTrue(expectedIterator.hasPrevious());
                assertTrue(actualIterator.hasPrevious());
                assertEquals(expectedIterator.previous(), actualIterator.previous());
            }

            // Walk forward i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertEquals(expectedIterator.nextIndex(), actualIterator.nextIndex());
                assertEquals(expectedIterator.previousIndex(), actualIterator.previousIndex());
                assertTrue(expectedIterator.hasNext());
                assertTrue(actualIterator.hasNext());
                assertEquals(expectedIterator.next(), actualIterator.next());
            }

            // Walk backward i steps
            for (int j = 0; j < i; j++) {
                assertEquals(expectedIterator.nextIndex(), actualIterator.nextIndex());
                assertEquals(expectedIterator.previousIndex(), actualIterator.previousIndex());
                assertTrue(expectedIterator.hasPrevious());
                assertTrue(actualIterator.hasPrevious());
                assertEquals(expectedIterator.previous(), actualIterator.previous());
            }
        }
    }
}