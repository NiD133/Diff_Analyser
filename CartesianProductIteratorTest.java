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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link CartesianProductIterator}.
 *
 * The Cartesian product creates all possible combinations where one element
 * is taken from each input list. For example:
 * - Lists: [A,B], [1,2] 
 * - Product: [A,1], [A,2], [B,1], [B,2]
 */
class CartesianProductIteratorTest extends AbstractIteratorTest<List<Character>> {

    // Test data representing different types of collections
    private List<Character> threeLetters;    // [A, B, C]
    private List<Character> threeNumbers;    // [1, 2, 3] 
    private List<Character> twoSymbols;      // [!, ?]
    private List<Character> emptyList;

    @Override
    public CartesianProductIterator<Character> makeEmptyIterator() {
        return new CartesianProductIterator<>();
    }

    @Override
    public CartesianProductIterator<Character> makeObject() {
        // Creates iterator for 3×3×2 = 18 combinations
        return new CartesianProductIterator<>(threeLetters, threeNumbers, twoSymbols);
    }

    @BeforeEach
    public void setUp() {
        threeLetters = Arrays.asList('A', 'B', 'C');
        threeNumbers = Arrays.asList('1', '2', '3');
        twoSymbols = Arrays.asList('!', '?');
        emptyList = Collections.emptyList();
    }

    @Override
    public boolean supportsRemove() {
        return false;
    }

    @Test
    void testEmptyIteratorHasNoElements() {
        CartesianProductIterator<Character> emptyIterator = makeEmptyIterator();

        assertFalse(emptyIterator.hasNext());
        assertThrows(NoSuchElementException.class, emptyIterator::next);
    }

    @Test
    void testIteratorWithEmptyCollectionHasNoElements() {
        CartesianProductIterator<Character> iterator =
                new CartesianProductIterator<>(threeLetters, emptyList);

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testGeneratesAllCombinationsInCorrectOrder() {
        CartesianProductIterator<Character> iterator = makeObject();

        // Expected: 3 letters × 3 numbers × 2 symbols = 18 combinations
        List<List<Character>> actualCombinations = collectAllCombinations(iterator);
        List<List<Character>> expectedCombinations = buildExpectedCombinations();

        assertEquals(expectedCombinations, actualCombinations);

        // Verify iterator is exhausted
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testAllEmptyListsProduceNoCombinations() {
        CartesianProductIterator<Character> iterator =
                new CartesianProductIterator<>(emptyList, emptyList, emptyList);

        List<List<Character>> combinations = collectAllCombinations(iterator);

        assertTrue(combinations.isEmpty());
    }

    @Test
    void testAnyEmptyListResultsInNoCombinations() {
        // Test with empty list in different positions
        testEmptyListAtPosition(0, emptyList, threeNumbers, twoSymbols);
        testEmptyListAtPosition(1, threeLetters, emptyList, twoSymbols);
        testEmptyListAtPosition(2, threeLetters, threeNumbers, emptyList);
    }

    @Test
    void testSameListUsedMultipleTimesCreatesCombinations() {
        CartesianProductIterator<Character> iterator =
                new CartesianProductIterator<>(threeLetters, threeLetters, threeLetters);

        // Expected: 3×3×3 = 27 combinations
        List<List<Character>> combinations = collectAllCombinations(iterator);

        assertEquals(27, combinations.size());
        verifyAllLetterCombinations(combinations);
    }

    @Test
    void testForEachRemainingProcessesAllCombinations() {
        CartesianProductIterator<Character> iterator = makeObject();
        List<List<Character>> combinationsFromForEach = collectUsingForEachRemaining(iterator);
        List<List<Character>> expectedCombinations = buildExpectedCombinations();

        assertEquals(expectedCombinations, combinationsFromForEach);
    }

    @Test
    void testRemoveOperationThrowsException() {
        CartesianProductIterator<Character> iterator = makeObject();

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    // Helper methods for cleaner test code

    private void testEmptyListAtPosition(int position, List<Character>... lists) {
        CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(lists);
        List<List<Character>> combinations = collectAllCombinations(iterator);

        assertTrue(combinations.isEmpty(),
                "Empty list at position " + position + " should result in no combinations");
    }

    private List<List<Character>> collectAllCombinations(CartesianProductIterator<Character> iterator) {
        List<List<Character>> results = new ArrayList<>();
        while (iterator.hasNext()) {
            results.add(iterator.next());
        }
        return results;
    }

    private List<List<Character>> collectUsingForEachRemaining(CartesianProductIterator<Character> iterator) {
        List<List<Character>> results = new ArrayList<>();
        iterator.forEachRemaining(results::add);
        return results;
    }

    private List<List<Character>> buildExpectedCombinations() {
        List<List<Character>> expected = new ArrayList<>();

        // Build combinations in the expected order: letters × numbers × symbols
        for (Character letter : threeLetters) {
            for (Character number : threeNumbers) {
                for (Character symbol : twoSymbols) {
                    expected.add(Arrays.asList(letter, number, symbol));
                }
            }
        }

        return expected;
    }

    private void verifyAllLetterCombinations(List<List<Character>> combinations) {
        List<List<Character>> expected = new ArrayList<>();

        for (Character first : threeLetters) {
            for (Character second : threeLetters) {
                for (Character third : threeLetters) {
                    expected.add(Arrays.asList(first, second, third));
                }
            }
        }

        assertEquals(expected, combinations);
    }
}