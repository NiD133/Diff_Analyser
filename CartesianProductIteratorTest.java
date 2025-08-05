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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link CartesianProductIterator}.
 */
class CartesianProductIteratorTest extends AbstractIteratorTest<List<Character>> {

    private List<Character> letters;
    private List<Character> numbers;
    private List<Character> symbols;
    private List<Character> emptyList;

    @Override
    public CartesianProductIterator<Character> makeEmptyIterator() {
        return new CartesianProductIterator<>();
    }

    @Override
    public CartesianProductIterator<Character> makeObject() {
        return new CartesianProductIterator<>(letters, numbers, symbols);
    }

    @BeforeEach
    public void setUp() {
        letters = Arrays.asList('A', 'B', 'C');
        numbers = Arrays.asList('1', '2', '3');
        symbols = Arrays.asList('!', '?');
        emptyList = Collections.emptyList();
    }

    @Override
    public boolean supportsRemove() {
        return false;
    }

    @Test
    void testHasNext_WhenOneCollectionIsEmpty_ShouldReturnFalse() {
        CartesianProductIterator<Character> it = new CartesianProductIterator<>(letters, Collections.emptyList());
        verifyNoResults(it);
    }

    @Test
    void testNext_ShouldReturnAllCombinations() {
        List<Character[]> results = collectIteratorResults(makeObject());
        verifyCartesianProduct(letters, numbers, symbols, results);
    }

    @Test
    void testNext_WhenAllCollectionsAreEmpty_ShouldReturnNoCombinations() {
        CartesianProductIterator<Character> it = new CartesianProductIterator<>(emptyList, emptyList, emptyList);
        verifyNoResults(it);
    }

    @Test
    void testNext_WhenFirstCollectionIsEmpty_ShouldReturnNoCombinations() {
        CartesianProductIterator<Character> it = new CartesianProductIterator<>(emptyList, numbers, symbols);
        verifyNoResults(it);
    }

    @Test
    void testNext_WhenLastCollectionIsEmpty_ShouldReturnNoCombinations() {
        CartesianProductIterator<Character> it = new CartesianProductIterator<>(letters, numbers, emptyList);
        verifyNoResults(it);
    }

    @Test
    void testNext_WhenMiddleCollectionIsEmpty_ShouldReturnNoCombinations() {
        CartesianProductIterator<Character> it = new CartesianProductIterator<>(letters, emptyList, symbols);
        verifyNoResults(it);
    }

    @Test
    void testNext_WhenSameListUsedMultipleTimes_ShouldReturnAllCombinations() {
        CartesianProductIterator<Character> it = new CartesianProductIterator<>(letters, letters, letters);
        List<Character[]> results = collectIteratorResults(it);
        verifyCartesianProduct(letters, letters, letters, results);
    }

    @Override
    @Test
    void testForEachRemaining() {
        List<Character[]> results = new ArrayList<>();
        makeObject().forEachRemaining(t -> results.add(t.toArray(new Character[0])));
        verifyCartesianProduct(letters, numbers, symbols, results);
    }

    @Test
    void testRemove_ShouldThrowUnsupportedOperationException() {
        CartesianProductIterator<Character> it = makeObject();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    // Helper methods ==========================================================
    
    private List<Character[]> collectIteratorResults(CartesianProductIterator<Character> iterator) {
        List<Character[]> results = new ArrayList<>();
        while (iterator.hasNext()) {
            results.add(iterator.next().toArray(new Character[0]));
        }
        verifyNoResults(iterator); // Verify post-exhaustion state
        return results;
    }
    
    private void verifyNoResults(CartesianProductIterator<Character> iterator) {
        assertFalse(iterator.hasNext(), "Iterator should report no more elements");
        assertThrows(NoSuchElementException.class, iterator::next, 
            "Calling next() should throw when exhausted");
    }
    
    private void verifyCartesianProduct(
        List<Character> list1, 
        List<Character> list2, 
        List<Character> list3, 
        List<Character[]> actualResults
    ) {
        int expectedSize = list1.size() * list2.size() * list3.size();
        assertEquals(expectedSize, actualResults.size(), 
            "Incorrect number of cartesian product results");
        
        Iterator<Character[]> resultIterator = actualResults.iterator();
        for (Character item1 : list1) {
            for (Character item2 : list2) {
                for (Character item3 : list3) {
                    Character[] expectedTuple = {item1, item2, item3};
                    assertArrayEquals(expectedTuple, resultIterator.next(), 
                        "Incorrect cartesian product tuple");
                }
            }
        }
    }
}