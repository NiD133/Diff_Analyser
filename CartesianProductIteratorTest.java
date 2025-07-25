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
import static org.junit.jupiter.api.Assertions.assertTrue;

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
 *
 * <p>This class tests the CartesianProductIterator to ensure it correctly generates
 * all possible combinations (tuples) from a given set of input iterables.</p>
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
    void testEmptyCollection() {
        // Given an iterator with an empty collection
        final CartesianProductIterator<Character> it = new CartesianProductIterator<>(letters, Collections.emptyList());

        // Then the iterator should not have any more elements
        assertFalse(it.hasNext());

        // And attempting to get the next element should throw a NoSuchElementException
        assertThrows(NoSuchElementException.class, it::next);
    }

    /**
     * Tests that the iterator returns all possible tuples.
     */
    @Test
    void testExhaustivity() {
        // Given a CartesianProductIterator
        final CartesianProductIterator<Character> it = makeObject();
        final List<Character[]> resultsList = new ArrayList<>();

        // When iterating through all the tuples
        while (it.hasNext()) {
            final List<Character> tuple = it.next();
            resultsList.add(tuple.toArray(new Character[0]));
        }

        // Then the iterator should be exhausted
        assertThrows(NoSuchElementException.class, it::next);

        // And the number of results should be the product of the sizes of the input lists (3 * 3 * 2 = 18)
        assertEquals(18, resultsList.size());

        // And the results should match the expected Cartesian product
        final Iterator<Character[]> itResults = resultsList.iterator();
        for (final Character a : letters) {
            for (final Character b : numbers) {
                for (final Character c : symbols) {
                    assertArrayEquals(new Character[]{a, b, c}, itResults.next());
                }
            }
        }
        assertFalse(itResults.hasNext(), "All elements should have been checked");

    }

    /**
     * Tests the case where all input lists are empty.  The iterator should be empty as well.
     */
    @Test
    void testExhaustivityWithAllEmptyLists() {
        // Given a CartesianProductIterator with all empty lists
        final CartesianProductIterator<Character> it = new CartesianProductIterator<>(emptyList, emptyList, emptyList);
        final List<Character[]> resultsList = new ArrayList<>();

        // When iterating through all the tuples
        while (it.hasNext()) {
            final List<Character> tuple = it.next();
            resultsList.add(tuple.toArray(new Character[0]));
        }

        // Then the iterator should be exhausted
        assertThrows(NoSuchElementException.class, it::next);

        // And the results list should be empty
        assertEquals(0, resultsList.size());
    }

    /**
     * Tests the case where the first list is empty.  The iterator should be empty.
     */
    @Test
    void testExhaustivityWithEmptyFirstList() {
        // Given a CartesianProductIterator with an empty first list
        final CartesianProductIterator<Character> it = new CartesianProductIterator<>(emptyList, numbers, symbols);
        final List<Character[]> resultsList = new ArrayList<>();

        // When iterating through all the tuples
        while (it.hasNext()) {
            final List<Character> tuple = it.next();
            resultsList.add(tuple.toArray(new Character[0]));
        }

        // Then the iterator should be exhausted
        assertThrows(NoSuchElementException.class, it::next);

        // And the results list should be empty
        assertEquals(0, resultsList.size());
    }

    /**
     * Tests the case where the last list is empty.  The iterator should be empty.
     */
    @Test
    void testExhaustivityWithEmptyLastList() {
        // Given a CartesianProductIterator with an empty last list
        final CartesianProductIterator<Character> it = new CartesianProductIterator<>(letters, numbers, emptyList);
        final List<Character[]> resultsList = new ArrayList<>();

        // When iterating through all the tuples
        while (it.hasNext()) {
            final List<Character> tuple = it.next();
            resultsList.add(tuple.toArray(new Character[0]));
        }

        // Then the iterator should be exhausted
        assertThrows(NoSuchElementException.class, it::next);

        // And the results list should be empty
        assertEquals(0, resultsList.size());
    }

    /**
     * Tests the case where one of the lists (other than first or last) is empty.  The iterator should be empty.
     */
    @Test
    void testExhaustivityWithEmptyList() {
        // Given a CartesianProductIterator with an empty middle list
        final CartesianProductIterator<Character> it = new CartesianProductIterator<>(letters, emptyList, symbols);
        final List<Character[]> resultsList = new ArrayList<>();

        // When iterating through all the tuples
        while (it.hasNext()) {
            final List<Character> tuple = it.next();
            resultsList.add(tuple.toArray(new Character[0]));
        }

        // Then the iterator should be exhausted
        assertThrows(NoSuchElementException.class, it::next);

        // And the results list should be empty
        assertEquals(0, resultsList.size());
    }

    /**
     * Tests the case where the same list is passed multiple times.
     */
    @Test
    void testExhaustivityWithSameList() {
        // Given a CartesianProductIterator with the same list repeated
        final CartesianProductIterator<Character> it = new CartesianProductIterator<>(letters, letters, letters);
        final List<Character[]> resultsList = new ArrayList<>();

        // When iterating through all the tuples
        while (it.hasNext()) {
            final List<Character> tuple = it.next();
            resultsList.add(tuple.toArray(new Character[0]));
        }

        // Then the iterator should be exhausted
        assertThrows(NoSuchElementException.class, it::next);

        // And the number of results should be the product of the sizes of the input lists (3 * 3 * 3 = 27)
        assertEquals(27, resultsList.size());

        // And the results should match the expected Cartesian product
        final Iterator<Character[]> itResults = resultsList.iterator();
        for (final Character a : letters) {
            for (final Character b : letters) {
                for (final Character c : letters) {
                    assertArrayEquals(new Character[]{a, b, c}, itResults.next());
                }
            }
        }
        assertFalse(itResults.hasNext(), "All elements should have been checked");
    }

    /**
     * Tests the {@code forEachRemaining} method to ensure all tuples are provided to the consumer.
     */
    @Test
    void testForEachRemaining() {
        // Given a CartesianProductIterator
        final CartesianProductIterator<Character> it = makeObject();
        final List<Character[]> resultsList = new ArrayList<>();

        // When using forEachRemaining to consume all tuples
        it.forEachRemaining(tuple -> resultsList.add(tuple.toArray(new Character[0])));

        // Then the number of results should be the product of the sizes of the input lists (3 * 3 * 2 = 18)
        assertEquals(18, resultsList.size());

        // And the results should match the expected Cartesian product
        final Iterator<Character[]> itResults = resultsList.iterator();
        for (final Character a : letters) {
            for (final Character b : numbers) {
                for (final Character c : symbols) {
                    assertArrayEquals(new Character[]{a, b, c}, itResults.next());
                }
            }
        }

        assertFalse(itResults.hasNext(), "All elements should have been checked");
    }

    @Test
    void testRemoveThrows() {
        // Given a CartesianProductIterator
        final CartesianProductIterator<Character> it = makeObject();

        // Then calling remove should throw an UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, it::remove);
    }
}