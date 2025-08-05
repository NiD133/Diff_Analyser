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
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests the FilterListIterator class, focusing on clarity and maintainability.
 */
@DisplayName("FilterListIterator Tests")
class FilterListIteratorTest {

    // --- Predicates for filtering ---
    private static final Predicate<Integer> IS_EVEN_PREDICATE = n -> n % 2 == 0;
    private static final Predicate<Integer> IS_ODD_PREDICATE = n -> n % 2 != 0;
    private static final Predicate<Integer> IS_MULTIPLE_OF_3_PREDICATE = n -> n % 3 == 0;
    private static final Predicate<Integer> IS_MULTIPLE_OF_4_PREDICATE = n -> n % 4 == 0;
    private static final Predicate<Integer> ALWAYS_TRUE_PREDICATE = PredicateUtils.truePredicate();
    private static final Predicate<Integer> ALWAYS_FALSE_PREDICATE = PredicateUtils.falsePredicate();

    // --- Test data ---
    private List<Integer> fullList;
    private List<Integer> evens;
    private List<Integer> odds;
    private List<Integer> multiplesOf3;
    private List<Integer> multiplesOf4;
    private List<Integer> multiplesOf6;

    @BeforeEach
    void setUp() {
        fullList = IntStream.range(0, 20).boxed().collect(Collectors.toList());
        evens = fullList.stream().filter(IS_EVEN_PREDICATE).collect(Collectors.toList());
        odds = fullList.stream().filter(IS_ODD_PREDICATE).collect(Collectors.toList());
        multiplesOf3 = fullList.stream().filter(IS_MULTIPLE_OF_3_PREDICATE).collect(Collectors.toList());
        multiplesOf4 = fullList.stream().filter(IS_MULTIPLE_OF_4_PREDICATE).collect(Collectors.toList());
        multiplesOf6 = fullList.stream()
                .filter(IS_EVEN_PREDICATE)
                .filter(IS_MULTIPLE_OF_3_PREDICATE)
                .collect(Collectors.toList());
    }

    // ========================================================================
    // --- Helper Methods for Assertions ---
    // ========================================================================

    /**
     * Asserts that the filtered iterator behaves identically to an expected iterator
     * during a full forward and backward traversal.
     */
    private <E> void assertIteratorBehavesLike(final List<E> expected, final ListIterator<E> filteredIterator) {
        final ListIterator<E> expectedIterator = expected.listIterator();
        assertForwardTraversalEquals(expectedIterator, filteredIterator);
        assertBackwardTraversalEquals(expectedIterator, filteredIterator);
    }

    /**
     * Asserts that the iterators are equivalent when traversing forward.
     */
    private void assertForwardTraversalEquals(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasNext()) {
            assertTrue(testing.hasNext(), "Testing iterator should have a next element");
            assertEquals(expected.nextIndex(), testing.nextIndex(), "Next indices should match");
            assertEquals(expected.previousIndex(), testing.previousIndex(), "Previous indices should match");
            assertEquals(expected.next(), testing.next(), "Next elements should be equal");
        }
        assertFalse(testing.hasNext(), "Testing iterator should be at the end");
    }

    /**
     * Asserts that the iterators are equivalent when traversing backward.
     */
    private void assertBackwardTraversalEquals(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasPrevious()) {
            assertTrue(testing.hasPrevious(), "Testing iterator should have a previous element");
            assertEquals(expected.nextIndex(), testing.nextIndex(), "Next indices should match");
            assertEquals(expected.previousIndex(), testing.previousIndex(), "Previous indices should match");
            assertEquals(expected.previous(), testing.previous(), "Previous elements should be equal");
        }
        assertFalse(testing.hasPrevious(), "Testing iterator should be at the beginning");
    }

    // ========================================================================
    // --- Test Cases ---
    // ========================================================================

    @Nested
    @DisplayName("Basic Filtering")
    class BasicFilteringTests {

        @Test
        @DisplayName("should return only even numbers")
        void filter_WithEvenPredicate_ShouldReturnOnlyEvens() {
            final FilterListIterator<Integer> filtered = new FilterListIterator<>(fullList.listIterator(), IS_EVEN_PREDICATE);
            assertIteratorBehavesLike(evens, filtered);
        }

        @Test
        @DisplayName("should return only odd numbers")
        void filter_WithOddPredicate_ShouldReturnOnlyOdds() {
            final FilterListIterator<Integer> filtered = new FilterListIterator<>(fullList.listIterator(), IS_ODD_PREDICATE);
            assertIteratorBehavesLike(odds, filtered);
        }

        @Test
        @DisplayName("should return only multiples of 3")
        void filter_WithMultipleOf3Predicate_ShouldReturnOnlyMultiplesOf3() {
            final FilterListIterator<Integer> filtered = new FilterListIterator<>(fullList.listIterator(), IS_MULTIPLE_OF_3_PREDICATE);
            assertIteratorBehavesLike(multiplesOf3, filtered);
        }

        @Test
        @DisplayName("should return all elements when predicate is always true")
        void filter_WithTruePredicate_ShouldReturnAllElements() {
            final FilterListIterator<Integer> filtered = new FilterListIterator<>(fullList.listIterator(), ALWAYS_TRUE_PREDICATE);
            assertIteratorBehavesLike(fullList, filtered);
        }

        @Test
        @DisplayName("should return no elements when predicate is always false")
        void filter_WithFalsePredicate_ShouldReturnNoElements() {
            final FilterListIterator<Integer> filtered = new FilterListIterator<>(fullList.listIterator(), ALWAYS_FALSE_PREDICATE);
            assertIteratorBehavesLike(new ArrayList<>(), filtered);
        }
    }

    @Nested
    @DisplayName("Nested Filtering")
    class NestedFilteringTests {

        @Test
        @DisplayName("should correctly filter with nested (even, then multiple-of-3) predicates")
        void filter_WithNestedPredicates_ShouldReturnMultiplesOf6() {
            final FilterListIterator<Integer> evenFilter = new FilterListIterator<>(fullList.listIterator(), IS_EVEN_PREDICATE);
            final FilterListIterator<Integer> finalFilter = new FilterListIterator<>(evenFilter, IS_MULTIPLE_OF_3_PREDICATE);
            assertIteratorBehavesLike(multiplesOf6, finalFilter);
        }

        @Test
        @DisplayName("should correctly filter with nested (multiple-of-3, then even) predicates")
        void filter_WithNestedPredicatesReversed_ShouldReturnMultiplesOf6() {
            final FilterListIterator<Integer> multipleOf3Filter = new FilterListIterator<>(fullList.listIterator(), IS_MULTIPLE_OF_3_PREDICATE);
            final FilterListIterator<Integer> finalFilter = new FilterListIterator<>(multipleOf3Filter, IS_EVEN_PREDICATE);
            assertIteratorBehavesLike(multiplesOf6, finalFilter);
        }
    }

    @Nested
    @DisplayName("Traversal Behavior")
    class TraversalBehaviorTests {

        @Test
        @DisplayName("next() followed by previous() should return the same element")
        void nextThenPrevious_ShouldReturnSameElement() {
            final FilterListIterator<Integer> filtered = new FilterListIterator<>(fullList.listIterator(), IS_MULTIPLE_OF_3_PREDICATE);
            final ListIterator<Integer> expected = multiplesOf3.listIterator();

            // Move both iterators forward
            assertEquals(expected.next(), filtered.next()); // 0
            assertEquals(expected.next(), filtered.next()); // 3

            // Calling previous() should return the element just returned by next()
            assertEquals(3, filtered.previous());
        }

        @Test
        @DisplayName("previous() followed by next() should return the same element")
        void previousThenNext_ShouldReturnSameElement() {
            final FilterListIterator<Integer> filtered = new FilterListIterator<>(fullList.listIterator(), IS_MULTIPLE_OF_3_PREDICATE);
            final ListIterator<Integer> expected = multiplesOf3.listIterator();

            // Move both iterators to the end
            while (expected.hasNext()) {
                expected.next();
                filtered.next();
            }

            // Move both iterators backward
            assertEquals(expected.previous(), filtered.previous()); // 18
            assertEquals(expected.previous(), filtered.previous()); // 15

            // Calling next() should return the element just returned by previous()
            assertEquals(15, filtered.next());
        }

        @Test
        @DisplayName("hasNext() should be false at the end of the filtered list")
        void hasNext_AtEndOfFilteredList_ShouldBeFalse() {
            final FilterListIterator<Integer> filtered = new FilterListIterator<>(fullList.listIterator(), IS_MULTIPLE_OF_4_PREDICATE);

            // Walk to the end of the filtered list
            while (filtered.hasNext()) {
                filtered.next();
            }

            assertTrue(filtered.hasPrevious(), "Should have a previous element at the end");
            assertFalse(filtered.hasNext(), "Should not have a next element at the end");
            assertEquals(Integer.valueOf(16), filtered.previous(), "Previous should return the last filtered element");
        }

        @Test
        @DisplayName("should handle extensive random forward and backward traversals")
        void iterator_WithRandomizedTraversal_ShouldRemainConsistent() {
            final FilterListIterator<Integer> filtered = new FilterListIterator<>(fullList.listIterator(), IS_MULTIPLE_OF_3_PREDICATE);
            final ListIterator<Integer> expected = multiplesOf3.listIterator();
            final Random random = new Random(12345); // Use a fixed seed for reproducibility

            for (int i = 0; i < 500; i++) {
                if (random.nextBoolean()) {
                    if (expected.hasNext()) {
                        assertEquals(expected.next(), filtered.next(), "Mismatch during random forward step");
                    }
                } else {
                    if (expected.hasPrevious()) {
                        assertEquals(expected.previous(), filtered.previous(), "Mismatch during random backward step");
                    }
                }
                assertEquals(expected.nextIndex(), filtered.nextIndex());
                assertEquals(expected.previousIndex(), filtered.previousIndex());
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        /**
         * Test for JIRA issue COLLECTIONS-360.
         * An iterator with a predicate that is based on an empty collection should not have next or previous elements.
         */
        @Test
        @DisplayName("should not have next or previous with an empty predicate collection")
        void iterator_WithEmptyAnyPredicate_ShouldBeEmpty() {
            final Predicate<Object> anyOfNothing = PredicateUtils.anyPredicate(Collections.emptyList());

            final FilterListIterator<Object> iterator1 = new FilterListIterator<>(anyOfNothing);
            assertFalse(iterator1.hasNext());

            final FilterListIterator<Object> iterator2 = new FilterListIterator<>(anyOfNothing);
            assertFalse(iterator2.hasPrevious());
        }
    }
}