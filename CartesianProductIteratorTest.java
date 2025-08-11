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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CartesianProductIterator}.
 * 
 * The tests focus on:
 * - Exhaustivity (all tuples are produced, in the expected order).
 * - Empty inputs (any empty input yields an empty product).
 * - forEachRemaining behavior.
 * - Unsupported remove().
 *
 * Helpers are used to:
 * - Collect tuples from an iterator.
 * - Build expected Cartesian products from input lists.
 * - Assert exhaustion after iteration.
 */
class CartesianProductIteratorTest extends AbstractIteratorTest<List<Character>> {

    private List<Character> letters;
    private List<Character> numbers;
    private List<Character> symbols;
    private List<Character> emptyList;

    @BeforeEach
    void setUp() {
        letters = Arrays.asList('A', 'B', 'C');
        numbers = Arrays.asList('1', '2', '3');
        symbols = Arrays.asList('!', '?');
        emptyList = Collections.emptyList();
    }

    @Override
    public CartesianProductIterator<Character> makeEmptyIterator() {
        return new CartesianProductIterator<>();
    }

    @Override
    public CartesianProductIterator<Character> makeObject() {
        return new CartesianProductIterator<>(letters, numbers, symbols);
    }

    @Override
    public boolean supportsRemove() {
        return false;
    }

    @Test
    void emptyInputYieldsEmptyProduct() {
        final CartesianProductIterator<Character> it =
                new CartesianProductIterator<>(letters, Collections.emptyList());

        assertFalse(it.hasNext(), "Product must be empty when any input is empty");
        assertThrows(NoSuchElementException.class, it::next, "next() must throw when empty");
    }

    @Test
    void producesAllTuplesInExpectedOrder() {
        final CartesianProductIterator<Character> it = makeObject();

        final List<List<Character>> actual = collectTuples(it);
        assertExhausted(it);

        final List<List<Character>> expected = expectedTuples(letters, numbers, symbols);
        assertTupleSequenceEquals(expected, actual);
    }

    @Test
    void allEmptyInputsYieldNoTuples() {
        final CartesianProductIterator<Character> it =
                new CartesianProductIterator<>(emptyList, emptyList, emptyList);

        final List<List<Character>> actual = collectTuples(it);
        assertExhausted(it);

        assertEquals(0, actual.size(), "No tuples expected when all inputs are empty");
    }

    @Test
    void emptyFirstInputYieldsNoTuples() {
        final CartesianProductIterator<Character> it =
                new CartesianProductIterator<>(emptyList, numbers, symbols);

        final List<List<Character>> actual = collectTuples(it);
        assertExhausted(it);

        assertEquals(0, actual.size(), "No tuples expected when first input is empty");
    }

    @Test
    void emptyLastInputYieldsNoTuples() {
        final CartesianProductIterator<Character> it =
                new CartesianProductIterator<>(letters, numbers, emptyList);

        final List<List<Character>> actual = collectTuples(it);
        assertExhausted(it);

        assertEquals(0, actual.size(), "No tuples expected when last input is empty");
    }

    @Test
    void anyEmptyInputYieldsNoTuples() {
        final CartesianProductIterator<Character> it =
                new CartesianProductIterator<>(letters, emptyList, symbols);

        final List<List<Character>> actual = collectTuples(it);
        assertExhausted(it);

        assertEquals(0, actual.size(), "No tuples expected when any input is empty");
    }

    @Test
    void sameListRepeatedStillProducesFullProduct() {
        final CartesianProductIterator<Character> it =
                new CartesianProductIterator<>(letters, letters, letters);

        final List<List<Character>> actual = collectTuples(it);
        assertExhausted(it);

        final List<List<Character>> expected = expectedTuples(letters, letters, letters);
        assertTupleSequenceEquals(expected, actual);
    }

    @Override
    @Test
    void testForEachRemaining() {
        final CartesianProductIterator<Character> it = makeObject();

        final List<List<Character>> actual = new ArrayList<>();
        it.forEachRemaining(tuple -> actual.add(new ArrayList<>(tuple)));

        final List<List<Character>> expected = expectedTuples(letters, numbers, symbols);
        assertTupleSequenceEquals(expected, actual);
    }

    @Test
    void removeIsUnsupported() {
        final CartesianProductIterator<Character> it = makeObject();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    // ----------------------- Helpers -----------------------

    /**
     * Collects all tuples from the iterator into a list, copying each tuple
     * to avoid accidental aliasing.
     */
    private static List<List<Character>> collectTuples(final Iterator<List<Character>> it) {
        final List<List<Character>> results = new ArrayList<>();
        while (it.hasNext()) {
            results.add(new ArrayList<>(it.next()));
        }
        return results;
    }

    /**
     * Asserts the iterator is exhausted by verifying that a subsequent next() call throws.
     */
    private static void assertExhausted(final Iterator<?> it) {
        assertThrows(NoSuchElementException.class, it::next, "Iterator should be exhausted");
    }

    /**
     * Builds the expected Cartesian product using the same order as nested for-loops:
     * for (a in list0) for (b in list1) ... produce [a, b, ...].
     */
    @SafeVarargs
    private static List<List<Character>> expectedTuples(final List<Character>... inputs) {
        return expectedTuples(Arrays.asList(inputs));
    }

    private static List<List<Character>> expectedTuples(final List<List<Character>> inputs) {
        final List<List<Character>> out = new ArrayList<>();
        for (final List<Character> in : inputs) {
            if (in.isEmpty()) {
                return out; // empty product if any input is empty
            }
        }
        backtrack(inputs, 0, new ArrayList<>(), out);
        return out;
    }

    private static void backtrack(final List<List<Character>> inputs,
                                  final int depth,
                                  final List<Character> current,
                                  final List<List<Character>> out) {
        if (depth == inputs.size()) {
            out.add(new ArrayList<>(current));
            return;
        }
        for (final Character c : inputs.get(depth)) {
            current.add(c);
            backtrack(inputs, depth + 1, current, out);
            current.remove(current.size() - 1);
        }
    }

    /**
     * Compares two lists of tuples element-by-element to also validate order.
     */
    private static void assertTupleSequenceEquals(final List<List<Character>> expected,
                                                  final List<List<Character>> actual) {
        assertEquals(expected.size(), actual.size(), "Tuple count differs");
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i), "Tuple at index " + i + " differs");
        }
    }
}