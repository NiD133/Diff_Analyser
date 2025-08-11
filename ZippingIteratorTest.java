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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test suite for {@link ZippingIterator}.
 */
@SuppressWarnings("boxing")
class ZippingIteratorTest extends AbstractIteratorTest<Integer> {

    private List<Integer> evens;
    private List<Integer> odds;
    private List<Integer> fib;

    @Override
    @SuppressWarnings("unchecked")
    public ZippingIterator<Integer> makeEmptyIterator() {
        return new ZippingIterator<>(IteratorUtils.<Integer>emptyIterator());
    }

    @Override
    public ZippingIterator<Integer> makeObject() {
        return new ZippingIterator<>(evens.iterator(), odds.iterator(), fib.iterator());
    }

    @BeforeEach
    public void setUp() {
        // Generate even numbers: [0, 2, 4, ..., 18]
        evens = IntStream.range(0, 20)
                .filter(i -> i % 2 == 0)
                .boxed()
                .collect(Collectors.toList());
        
        // Generate odd numbers: [1, 3, 5, ..., 19]
        odds = IntStream.range(0, 20)
                .filter(i -> i % 2 != 0)
                .boxed()
                .collect(Collectors.toList());
        
        // Predefined Fibonacci sequence
        fib = Arrays.asList(1, 1, 2, 3, 5, 8, 13, 21);
    }

    // Helper to assert sequence of values from iterator
    private void assertSequence(ZippingIterator<Integer> iterator, List<Integer> expectedSequence) {
        for (Integer expected : expectedSequence) {
            assertTrue(iterator.hasNext(), "Iterator should have next value: " + expected);
            assertEquals(expected, iterator.next());
        }
        assertFalse(iterator.hasNext(), "Iterator should be exhausted after sequence");
    }

    @Test
    void testSingleIterator() {
        @SuppressWarnings("unchecked")
        ZippingIterator<Integer> iterator = new ZippingIterator<>(evens.iterator());
        assertSequence(iterator, evens);
    }

    @Test
    void testTwoIdenticalIterators() {
        // Expected: each element from 'evens' repeated twice consecutively
        List<Integer> expected = evens.stream()
                .flatMap(i -> List.of(i, i).stream())
                .collect(Collectors.toList());
        
        ZippingIterator<Integer> iterator = new ZippingIterator<>(evens.iterator(), evens.iterator());
        assertSequence(iterator, expected);
    }

    @Test
    void testTwoIteratorsInterleaved() {
        // Expected: interleaved sequence [0,1,2,3,...,19]
        List<Integer> expected = IntStream.range(0, 20)
                .boxed()
                .collect(Collectors.toList());
        
        ZippingIterator<Integer> iterator = new ZippingIterator<>(evens.iterator(), odds.iterator());
        assertSequence(iterator, expected);
    }

    @Test
    void testTwoIteratorsAlternating() {
        // Expected: alternating sequence [1,0,3,2,...,19,18]
        List<Integer> expected = new ArrayList<>();
        for (int i = 0; i < odds.size(); i++) {
            expected.add(odds.get(i));
            expected.add(evens.get(i));
        }
        
        ZippingIterator<Integer> iterator = new ZippingIterator<>(odds.iterator(), evens.iterator());
        assertSequence(iterator, expected);
    }

    @Test
    void testThreeIteratorsInterleaved() {
        // Expected sequence: round-robin from fib, evens, then odds until exhaustion
        List<Integer> expected = Arrays.asList(
            1, 0, 1,     // fib[0], evens[0], odds[0]
            1, 2, 3,     // fib[1], evens[1], odds[1]
            2, 4, 5,     // fib[2], evens[2], odds[2]
            3, 6, 7,     // fib[3], evens[3], odds[3]
            5, 8, 9,     // fib[4], evens[4], odds[4]
            8, 10, 11,   // fib[5], evens[5], odds[5]
            13, 12, 13,  // fib[6], evens[6], odds[6]
            21, 14, 15,  // fib[7], evens[7], odds[7]
            16, 17,       // evens[8], odds[8]
            18, 19        // evens[9], odds[9]
        );
        
        ZippingIterator<Integer> iterator = new ZippingIterator<>(
            fib.iterator(), evens.iterator(), odds.iterator()
        );
        assertSequence(iterator, expected);
    }

    @Test
    void testRemoveWithSingleIterator() {
        // Condition: remove even numbers divisible by 4
        Predicate<Integer> shouldRemove = val -> val % 4 == 0;
        
        // Track expected state after removals
        List<Integer> expectedEvens = new ArrayList<>(evens);
        
        @SuppressWarnings("unchecked")
        ZippingIterator<Integer> iterator = new ZippingIterator<>(evens.iterator());
        while (iterator.hasNext()) {
            Integer val = iterator.next();
            if (shouldRemove.test(val)) {
                iterator.remove();
                expectedEvens.remove(val);
            }
        }
        
        assertEquals(expectedEvens, evens, "Evens list after removal");
    }

    @Test
    void testRemoveWithTwoIterators() {
        // Condition: remove numbers divisible by 3 or 4
        Predicate<Integer> shouldRemove = val -> val % 3 == 0 || val % 4 == 0;
        
        // Track expected state after removals
        List<Integer> expectedEvens = new ArrayList<>(evens);
        List<Integer> expectedOdds = new ArrayList<>(odds);
        
        ZippingIterator<Integer> iterator = new ZippingIterator<>(evens.iterator(), odds.iterator());
        while (iterator.hasNext()) {
            Integer val = iterator.next();
            if (shouldRemove.test(val)) {
                iterator.remove();
                // Remove from correct source list
                if (val % 2 == 0) {
                    expectedEvens.remove(val);
                } else {
                    expectedOdds.remove(val);
                }
            }
        }
        
        assertEquals(expectedEvens, evens, "Evens list after removal");
        assertEquals(expectedOdds, odds, "Odds list after removal");
    }
}