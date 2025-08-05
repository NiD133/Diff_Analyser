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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit test suite for {@link ZippingIterator}.
 */
class ZippingIteratorTest extends AbstractIteratorTest<Integer> {

    private List<Integer> evens;
    private List<Integer> odds;
    private List<Integer> fibonacci;

    @Override
    public ZippingIterator<Integer> makeEmptyIterator() {
        return new ZippingIterator<>(IteratorUtils.emptyIterator());
    }

    @Override
    public ZippingIterator<Integer> makeObject() {
        return new ZippingIterator<>(evens.iterator(), odds.iterator(), fibonacci.iterator());
    }

    @BeforeEach
    public void setUp() {
        evens = new ArrayList<>();
        odds = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                evens.add(i);
            } else {
                odds.add(i);
            }
        }

        fibonacci = new ArrayList<>(Arrays.asList(1, 1, 2, 3, 5, 8, 13, 21));
    }

    /**
     * Tests that a ZippingIterator with a single underlying iterator behaves the same as the original.
     */
    @Test
    @DisplayName("Zipping a single iterator should be equivalent to the original iterator")
    void testZippingSingleIterator() {
        final ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(evens.iterator());
        final List<Integer> result = IteratorUtils.toList(zippingIterator);
        assertEquals(evens, result);
    }

    /**
     * Tests zipping two iterators of the same length.
     */
    @Test
    @DisplayName("Zipping two iterators of the same length should interleave their elements")
    void testZippingTwoEqualLengthIterators() {
        final ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(evens.iterator(), odds.iterator());

        final List<Integer> expected = IntStream.range(0, 20).boxed().collect(Collectors.toList());
        final List<Integer> actual = IteratorUtils.toList(zippingIterator);

        assertEquals(expected, actual);
    }

    /**
     * Tests that the order of iterators provided to the constructor is respected.
     */
    @Test
    @DisplayName("Zipping should respect the order of the provided iterators")
    void testZippingOrderIsRespected() {
        final ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(odds.iterator(), evens.iterator());

        final List<Integer> expected = new ArrayList<>();
        for (int i = 0; i < odds.size(); i++) {
            expected.add(odds.get(i));
            expected.add(evens.get(i));
        }

        final List<Integer> actual = IteratorUtils.toList(zippingIterator);
        assertEquals(expected, actual);
    }

    /**
     * Tests zipping two identical iterators.
     */
    @Test
    @DisplayName("Zipping two identical iterators should interleave their elements")
    void testZippingTwoIdenticalIterators() {
        final ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(evens.iterator(), evens.iterator());

        final List<Integer> expected = new ArrayList<>();
        for (final Integer even : evens) {
            expected.add(even);
            expected.add(even);
        }

        final List<Integer> actual = IteratorUtils.toList(zippingIterator);
        assertEquals(expected, actual);
    }

    /**
     * Tests zipping three iterators of different lengths. The zipping should continue
     * with the remaining iterators once the shortest one is exhausted.
     */
    @Test
    @DisplayName("Zipping iterators of different lengths should continue until all are exhausted")
    void testZippingThreeDifferentLengthIterators() {
        final ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(fibonacci.iterator(), evens.iterator(), odds.iterator());

        // Manually construct the expected interleaved list
        final List<Integer> expected = new ArrayList<>();
        final int fibSize = fibonacci.size(); // 8
        final int evensSize = evens.size();   // 10

        // First, interleave all three lists until the shortest (fibonacci) is exhausted
        for (int i = 0; i < fibSize; i++) {
            expected.add(fibonacci.get(i));
            expected.add(evens.get(i));
            expected.add(odds.get(i));
        }

        // Then, continue interleaving the remaining two lists
        for (int i = fibSize; i < evensSize; i++) {
            expected.add(evens.get(i));
            expected.add(odds.get(i));
        }

        final List<Integer> actual = IteratorUtils.toList(zippingIterator);
        assertEquals(expected, actual);
        assertFalse(zippingIterator.hasNext());
    }

    /**
     * Tests the remove() operation on a ZippingIterator with a single underlying iterator.
     */
    @Test
    @DisplayName("remove() on a single-iterator zip should modify the source collection")
    void testRemoveOnSingleIterator() {
        // A mutable copy for the test
        final List<Integer> mutableEvens = new ArrayList<>(evens);
        final ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(mutableEvens.iterator());

        // Remove all multiples of 4
        zippingIterator.forEachRemaining(val -> {
            if (val % 4 == 0) {
                zippingIterator.remove();
            }
        });

        final List<Integer> expected = Arrays.asList(2, 6, 10, 14, 18);
        assertEquals(expected, mutableEvens);
    }

    /**
     * Tests the remove() operation on a ZippingIterator, ensuring it removes elements
     * from the correct underlying source collection.
     */
    @Test
    @DisplayName("remove() on a multi-iterator zip should modify the correct source collection")
    void testRemoveOnZippedIterators() {
        // Mutable copies for the test
        final List<Integer> mutableEvens = new ArrayList<>(evens);
        final List<Integer> mutableOdds = new ArrayList<>(odds);
        final ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(mutableEvens.iterator(), mutableOdds.iterator());

        // Remove elements that are multiples of 3 or 4
        while (zippingIterator.hasNext()) {
            final Integer val = zippingIterator.next();
            if (val % 4 == 0 || val % 3 == 0) {
                zippingIterator.remove();
            }
        }

        final List<Integer> expectedEvens = Arrays.asList(2, 10, 14);
        final List<Integer> expectedOdds = Arrays.asList(1, 5, 7, 11, 13, 17, 19);

        assertEquals(expectedEvens, mutableEvens, "Elements should be removed from the 'evens' list");
        assertEquals(expectedOdds, mutableOdds, "Elements should be removed from the 'odds' list");
    }
}