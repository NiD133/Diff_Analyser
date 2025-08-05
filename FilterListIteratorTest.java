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
import java.util.Random;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.collections4.list.GrowthList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the FilterListIterator class.
 * 
 * FilterListIterator decorates another ListIterator using a predicate to filter elements,
 * only allowing through those elements that match the specified predicate.
 */
@SuppressWarnings("boxing")
class FilterListIteratorTest {

    // Test data: numbers 0-19
    private List<Integer> sourceNumbers;
    
    // Expected filtered results
    private List<Integer> expectedOddNumbers;
    private List<Integer> expectedEvenNumbers;
    private List<Integer> expectedMultiplesOfThree;
    private List<Integer> expectedMultiplesOfFour;
    private List<Integer> expectedMultiplesOfSix;
    
    // Test predicates
    private Predicate<Integer> alwaysTruePredicate;
    private Predicate<Integer> alwaysFalsePredicate;
    private Predicate<Integer> isEvenPredicate;
    private Predicate<Integer> isOddPredicate;
    private Predicate<Integer> isMultipleOfThreePredicate;
    private Predicate<Integer> isMultipleOfFourPredicate;
    
    private final Random random = new Random();

    @BeforeEach
    public void setUp() {
        initializeTestData();
        initializePredicates();
    }

    private void initializeTestData() {
        sourceNumbers = new ArrayList<>();
        expectedOddNumbers = new ArrayList<>();
        expectedEvenNumbers = new ArrayList<>();
        expectedMultiplesOfThree = new ArrayList<>();
        expectedMultiplesOfFour = new ArrayList<>();
        expectedMultiplesOfSix = new ArrayList<>();
        
        // Create test data: numbers 0-19 and their filtered subsets
        for (int i = 0; i < 20; i++) {
            sourceNumbers.add(i);
            
            if (i % 2 == 0) {
                expectedEvenNumbers.add(i);
            } else {
                expectedOddNumbers.add(i);
            }
            
            if (i % 3 == 0) {
                expectedMultiplesOfThree.add(i);
            }
            
            if (i % 4 == 0) {
                expectedMultiplesOfFour.add(i);
            }
            
            if (i % 6 == 0) {
                expectedMultiplesOfSix.add(i);
            }
        }
    }

    private void initializePredicates() {
        alwaysTruePredicate = x -> true;
        alwaysFalsePredicate = x -> false; // Fixed: was incorrectly returning true
        isEvenPredicate = x -> x % 2 == 0;
        isOddPredicate = x -> x % 2 != 0;
        isMultipleOfThreePredicate = x -> x % 3 == 0;
        isMultipleOfFourPredicate = x -> x % 4 == 0;
    }

    @Test
    void testFilteringWithAlwaysTruePredicate_ShouldReturnAllElements() {
        FilterListIterator<Integer> filteredIterator = 
            new FilterListIterator<>(sourceNumbers.listIterator(), alwaysTruePredicate);
        
        assertIteratorBehaviorMatches(sourceNumbers, filteredIterator);
    }

    @Test
    void testFilteringWithAlwaysFalsePredicate_ShouldReturnNoElements() {
        FilterListIterator<Integer> filteredIterator = 
            new FilterListIterator<>(sourceNumbers.listIterator(), alwaysFalsePredicate);
        
        assertIteratorBehaviorMatches(new ArrayList<>(), filteredIterator);
    }

    @Test
    void testFilteringEvenNumbers() {
        FilterListIterator<Integer> filteredIterator = 
            new FilterListIterator<>(sourceNumbers.listIterator(), isEvenPredicate);
        
        assertIteratorBehaviorMatches(expectedEvenNumbers, filteredIterator);
    }

    @Test
    void testFilteringOddNumbers() {
        FilterListIterator<Integer> filteredIterator = 
            new FilterListIterator<>(sourceNumbers.listIterator(), isOddPredicate);
        
        assertIteratorBehaviorMatches(expectedOddNumbers, filteredIterator);
    }

    @Test
    void testFilteringMultiplesOfThree() {
        FilterListIterator<Integer> filteredIterator = 
            new FilterListIterator<>(sourceNumbers.listIterator(), isMultipleOfThreePredicate);
        
        assertIteratorBehaviorMatches(expectedMultiplesOfThree, filteredIterator);
    }

    @Test
    void testFilteringMultiplesOfFour() {
        FilterListIterator<Integer> filteredIterator = 
            new FilterListIterator<>(sourceNumbers.listIterator(), isMultipleOfFourPredicate);
        
        assertIteratorBehaviorMatches(expectedMultiplesOfFour, filteredIterator);
    }

    @Test
    void testNestedFiltering_ThreesAndEvens_ShouldReturnMultiplesOfSix() {
        // First filter multiples of 3, then filter evens from that result
        FilterListIterator<Integer> firstFilter = 
            new FilterListIterator<>(sourceNumbers.listIterator(), isMultipleOfThreePredicate);
        FilterListIterator<Integer> nestedFilter = 
            new FilterListIterator<>(firstFilter, isEvenPredicate);
        
        assertIteratorBehaviorMatches(expectedMultiplesOfSix, nestedFilter);
    }

    @Test
    void testNestedFiltering_EvensAndThrees_ShouldReturnMultiplesOfSix() {
        // First filter evens, then filter multiples of 3 from that result
        FilterListIterator<Integer> firstFilter = 
            new FilterListIterator<>(sourceNumbers.listIterator(), isEvenPredicate);
        FilterListIterator<Integer> nestedFilter = 
            new FilterListIterator<>(firstFilter, isMultipleOfThreePredicate);
        
        assertIteratorBehaviorMatches(expectedMultiplesOfSix, nestedFilter);
    }

    @Test
    void testTripleNestedFiltering_ShouldReturnMultiplesOfSix() {
        // Test three levels of nesting with a true predicate as the final filter
        FilterListIterator<Integer> firstFilter = 
            new FilterListIterator<>(sourceNumbers.listIterator(), isMultipleOfThreePredicate);
        FilterListIterator<Integer> secondFilter = 
            new FilterListIterator<>(firstFilter, isEvenPredicate);
        FilterListIterator<Integer> tripleNestedFilter = 
            new FilterListIterator<>(secondFilter, alwaysTruePredicate);
        
        assertIteratorBehaviorMatches(expectedMultiplesOfSix, tripleNestedFilter);
    }

    @Test
    void testManualIterationOfMultiplesOfThree_VerifiesBasicForwardAndBackwardNavigation() {
        FilterListIterator<Integer> filteredIterator = 
            new FilterListIterator<>(sourceNumbers.listIterator(), isMultipleOfThreePredicate);

        // Forward iteration through all multiples of 3: [0, 3, 6, 9, 12, 15, 18]
        assertEquals(Integer.valueOf(0), filteredIterator.next());
        assertEquals(Integer.valueOf(3), filteredIterator.next());
        assertEquals(Integer.valueOf(6), filteredIterator.next());
        assertEquals(Integer.valueOf(9), filteredIterator.next());
        assertEquals(Integer.valueOf(12), filteredIterator.next());
        assertEquals(Integer.valueOf(15), filteredIterator.next());
        assertEquals(Integer.valueOf(18), filteredIterator.next());
        assertFalse(filteredIterator.hasNext());

        // Backward iteration through all elements
        assertEquals(Integer.valueOf(18), filteredIterator.previous());
        assertEquals(Integer.valueOf(15), filteredIterator.previous());
        assertEquals(Integer.valueOf(12), filteredIterator.previous());
        assertEquals(Integer.valueOf(9), filteredIterator.previous());
        assertEquals(Integer.valueOf(6), filteredIterator.previous());
        assertEquals(Integer.valueOf(3), filteredIterator.previous());
        assertEquals(Integer.valueOf(0), filteredIterator.previous());
        assertFalse(filteredIterator.hasPrevious());

        // Test mixed forward/backward navigation
        assertEquals(Integer.valueOf(0), filteredIterator.next());
        assertEquals(Integer.valueOf(0), filteredIterator.previous());
        assertEquals(Integer.valueOf(0), filteredIterator.next());

        assertEquals(Integer.valueOf(3), filteredIterator.next());
        assertEquals(Integer.valueOf(6), filteredIterator.next());
        assertEquals(Integer.valueOf(6), filteredIterator.previous());
        assertEquals(Integer.valueOf(3), filteredIterator.previous());
    }

    @Test
    void testIteratorStateAfterReachingEnd_HasPreviousButNotNext() {
        FilterListIterator<Integer> filteredIterator = 
            new FilterListIterator<>(sourceNumbers.listIterator(), isMultipleOfFourPredicate);
        ListIterator<Integer> expectedIterator = expectedMultiplesOfFour.listIterator();
        
        // Move both iterators to the end
        while (expectedIterator.hasNext()) {
            expectedIterator.next();
            filteredIterator.next();
        }
        
        // At the end: should have previous but not next
        assertTrue(filteredIterator.hasPrevious());
        assertFalse(filteredIterator.hasNext());
        assertEquals(expectedIterator.previous(), filteredIterator.previous());
    }

    @Test
    void testNextCallsChangesPreviousValue() {
        FilterListIterator<Integer> filteredIterator = 
            new FilterListIterator<>(sourceNumbers.listIterator(), isMultipleOfThreePredicate);
        
        verifyNextCallsChangePreviousValue(expectedMultiplesOfThree.listIterator(), filteredIterator);
    }

    @Test
    void testPreviousCallsChangeNextValue() {
        FilterListIterator<Integer> filteredIterator = 
            new FilterListIterator<>(sourceNumbers.listIterator(), isMultipleOfThreePredicate);
        ListIterator<Integer> expectedIterator = expectedMultiplesOfThree.listIterator();
        
        // Move to end first
        moveIteratorToEnd(expectedIterator, filteredIterator);
        
        verifyPreviousCallsChangeNextValue(expectedIterator, filteredIterator);
    }

    /**
     * Test for COLLECTIONS-360: FilterListIterator with empty predicate collection
     * should handle hasNext() and hasPrevious() correctly.
     */
    @Test
    void testEmptyPredicateCollection_ShouldHandleHasNextAndHasPrevious() {
        Collection<Predicate<Object>> emptyPredicates = new GrowthList<>();
        Predicate<Object> anyPredicate = PredicateUtils.anyPredicate(emptyPredicates);
        
        FilterListIterator<Object> iterator1 = new FilterListIterator<>(anyPredicate);
        assertFalse(iterator1.hasNext());
        
        FilterListIterator<Object> iterator2 = new FilterListIterator<>(anyPredicate);
        assertFalse(iterator2.hasPrevious());
    }

    @Test
    void testWalkListsUtilityMethod_VerifiesTestInfrastructure() {
        // Verify that our test utility method works correctly
        assertIteratorBehaviorMatches(sourceNumbers, sourceNumbers.listIterator());
    }

    // Helper methods

    /**
     * Comprehensive test that verifies a FilterListIterator behaves identically to
     * a standard ListIterator over the expected filtered results.
     */
    private <E> void assertIteratorBehaviorMatches(List<E> expectedList, ListIterator<E> actualIterator) {
        ListIterator<E> expectedIterator = expectedList.listIterator();

        // Test 1: Complete forward traversal
        walkForward(expectedIterator, actualIterator);

        // Test 2: Complete backward traversal
        walkBackward(expectedIterator, actualIterator);

        // Test 3: Forward-backward-forward pattern
        testForwardBackwardForwardPattern(expectedIterator, actualIterator);

        // Test 4: Complex navigation patterns
        testComplexNavigationPatterns(expectedList, expectedIterator, actualIterator);

        // Test 5: Random walk simulation
        performRandomWalkTest(expectedIterator, actualIterator);
    }

    private void walkForward(ListIterator<?> expected, ListIterator<?> actual) {
        while (expected.hasNext()) {
            assertIteratorIndicesMatch(expected, actual);
            assertTrue(actual.hasNext());
            assertEquals(expected.next(), actual.next());
        }
    }

    private void walkBackward(ListIterator<?> expected, ListIterator<?> actual) {
        while (expected.hasPrevious()) {
            assertIteratorIndicesMatch(expected, actual);
            assertTrue(actual.hasPrevious());
            assertEquals(expected.previous(), actual.previous());
        }
    }

    private void testForwardBackwardForwardPattern(ListIterator<?> expected, ListIterator<?> actual) {
        while (expected.hasNext()) {
            assertIteratorIndicesMatch(expected, actual);
            assertTrue(actual.hasNext());
            assertEquals(expected.next(), actual.next());
            
            assertTrue(actual.hasPrevious());
            assertEquals(expected.previous(), actual.previous());
            
            assertTrue(actual.hasNext());
            assertEquals(expected.next(), actual.next());
        }
        
        walkBackward(expected, actual);
    }

    private void testComplexNavigationPatterns(List<?> expectedList, 
                                             ListIterator<?> expected, 
                                             ListIterator<?> actual) {
        for (int i = 0; i < expectedList.size(); i++) {
            // Walk forward i steps
            for (int j = 0; j < i; j++) {
                assertIteratorIndicesMatch(expected, actual);
                assertTrue(expected.hasNext(), "Test logic error: expected should have next");
                assertTrue(actual.hasNext());
                assertEquals(expected.next(), actual.next());
            }
            
            // Walk back i/2 steps
            int backSteps = i / 2;
            for (int j = 0; j < backSteps; j++) {
                assertIteratorIndicesMatch(expected, actual);
                assertTrue(expected.hasPrevious(), "Test logic error: expected should have previous");
                assertTrue(actual.hasPrevious());
                assertEquals(expected.previous(), actual.previous());
            }
            
            // Walk forward i/2 steps
            for (int j = 0; j < backSteps; j++) {
                assertIteratorIndicesMatch(expected, actual);
                assertTrue(expected.hasNext(), "Test logic error: expected should have next");
                assertTrue(actual.hasNext());
                assertEquals(expected.next(), actual.next());
            }
            
            // Walk back i steps to return to start
            for (int j = 0; j < i; j++) {
                assertIteratorIndicesMatch(expected, actual);
                assertTrue(expected.hasPrevious(), "Test logic error: expected should have previous");
                assertTrue(actual.hasPrevious());
                assertEquals(expected.previous(), actual.previous());
            }
        }
    }

    private void performRandomWalkTest(ListIterator<?> expected, ListIterator<?> actual) {
        StringBuilder walkDescription = new StringBuilder(500);
        
        for (int i = 0; i < 500; i++) {
            if (random.nextBoolean()) {
                // Step forward
                walkDescription.append("+");
                if (expected.hasNext()) {
                    assertEquals(expected.next(), actual.next(), 
                        "Random walk failed at step: " + walkDescription.toString());
                }
            } else {
                // Step backward
                walkDescription.append("-");
                if (expected.hasPrevious()) {
                    assertEquals(expected.previous(), actual.previous(), 
                        "Random walk failed at step: " + walkDescription.toString());
                }
            }
            
            assertIteratorIndicesMatch(expected, actual, walkDescription.toString());
        }
    }

    private void assertIteratorIndicesMatch(ListIterator<?> expected, ListIterator<?> actual) {
        assertIteratorIndicesMatch(expected, actual, "");
    }

    private void assertIteratorIndicesMatch(ListIterator<?> expected, ListIterator<?> actual, String context) {
        assertEquals(expected.nextIndex(), actual.nextIndex(), 
            "Next index mismatch" + (context.isEmpty() ? "" : " at: " + context));
        assertEquals(expected.previousIndex(), actual.previousIndex(), 
            "Previous index mismatch" + (context.isEmpty() ? "" : " at: " + context));
    }

    private void moveIteratorToEnd(ListIterator<?> expected, ListIterator<?> actual) {
        while (expected.hasNext()) {
            expected.next();
            actual.next();
        }
    }

    /**
     * Verifies that calls to next() properly update the value returned by previous(),
     * even after previous() has been set by a call to hasPrevious().
     */
    private void verifyNextCallsChangePreviousValue(ListIterator<?> expected, ListIterator<?> actual) {
        assertEquals(expected.next(), actual.next());
        assertEquals(expected.hasPrevious(), actual.hasPrevious());
        
        Object expectedValue = expected.next();
        Object actualValue = actual.next();
        assertEquals(expectedValue, actualValue);
        
        Object expectedPrevious = expected.previous();
        Object actualPrevious = actual.previous();
        
        assertEquals(expectedValue, expectedPrevious, "Expected iterator: next() should equal subsequent previous()");
        assertEquals(actualValue, actualPrevious, "Actual iterator: next() should equal subsequent previous()");
        assertEquals(expectedPrevious, actualPrevious, "Both iterators should return same previous() value");
    }

    /**
     * Verifies that calls to previous() properly update the value returned by next(),
     * even after next() has been set by a call to hasNext().
     */
    private void verifyPreviousCallsChangeNextValue(ListIterator<?> expected, ListIterator<?> actual) {
        assertEquals(expected.previous(), actual.previous());
        assertEquals(expected.hasNext(), actual.hasNext());
        
        Object expectedValue = expected.previous();
        Object actualValue = actual.previous();
        assertEquals(expectedValue, actualValue);
        
        Object expectedNext = expected.next();
        Object actualNext = actual.next();
        
        assertEquals(expectedValue, expectedNext, "Expected iterator: previous() should equal subsequent next()");
        assertEquals(actualValue, actualNext, "Actual iterator: previous() should equal subsequent next()");
        assertEquals(expectedNext, actualNext, "Both iterators should return same next() value");
    }
}