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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link CartesianProductIterator}.
 */
class CartesianProductIteratorTest extends AbstractIteratorTest<List<Character>> {

    private List<Character> letters;
    private List<Character> numbers;
    private List<Character> symbols;
    private List<Character> emptyList;

    @BeforeEach
    public void setUp() {
        letters = Arrays.asList('A', 'B', 'C');
        numbers = Arrays.asList('1', '2', '3');
        symbols = Arrays.asList('!', '?');
        emptyList = Collections.emptyList();
    }

    //--- AbstractIteratorTest implementations --------------------------------

    @Override
    public CartesianProductIterator<Character> makeEmptyIterator() {
        // An iterator with no iterables is one way to create an empty one.
        return new CartesianProductIterator<>();
    }

    @Override
    public CartesianProductIterator<Character> makeObject() {
        // A standard iterator with three non-empty lists.
        return new CartesianProductIterator<>(letters, numbers, symbols);
    }

    @Override
    public boolean supportsRemove() {
        return false;
    }

    //--- Test Cases ----------------------------------------------------------

    @Test
    void constructor_shouldThrowNullPointerException_forNullIterableArray() {
        assertThrows(NullPointerException.class, () -> new CartesianProductIterator<>(null));
    }

    @Test
    void constructor_shouldThrowNullPointerException_forNullIterableInArray() {
        assertThrows(NullPointerException.class, () -> new CartesianProductIterator<>(letters, null, symbols));
    }

    @ParameterizedTest
    @MethodSource("emptyProductScenarios")
    @DisplayName("Iterator should be empty if any input iterable is empty")
    void iterator_shouldBeEmpty_whenAnyInputIsEmpty(final List<Iterable<Character>> iterables) {
        // Arrange
        @SuppressWarnings("unchecked")
        final Iterable<Character>[] iterablesArray = iterables.toArray(new Iterable[0]);
        final CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(iterablesArray);

        // Act & Assert
        assertFalse(iterator.hasNext(), "Iterator should be empty");
        assertThrows(NoSuchElementException.class, iterator::next, "next() should throw if iterator is empty");
    }

    static Stream<Arguments> emptyProductScenarios() {
        // These lists must be defined here as this is a static method.
        final List<Character> letters = Arrays.asList('A', 'B');
        final List<Character> numbers = Arrays.asList('1', '2');
        final List<Character> empty = Collections.emptyList();

        return Stream.of(
            // Scenario: First iterable is empty
            Arguments.of(Arrays.asList(empty, letters, numbers)),
            // Scenario: Middle iterable is empty
            Arguments.of(Arrays.asList(letters, empty, numbers)),
            // Scenario: Last iterable is empty
            Arguments.of(Arrays.asList(letters, numbers, empty)),
            // Scenario: All iterables are empty
            Arguments.of(Arrays.asList(empty, empty, empty)),
            // Scenario: A single empty iterable
            Arguments.of(Collections.singletonList(empty))
        );
    }

    @Test
    void next_shouldReturnAllCombinationsInCorrectOrder() {
        // Arrange
        final CartesianProductIterator<Character> iterator = makeObject();
        final List<List<Character>> expected = new ArrayList<>();
        for (final Character letter : letters) {
            for (final Character number : numbers) {
                for (final Character symbol : symbols) {
                    expected.add(Arrays.asList(letter, number, symbol));
                }
            }
        }

        // Act
        final List<List<Character>> actual = new ArrayList<>();
        iterator.forEachRemaining(actual::add);

        // Assert
        assertEquals(18, actual.size());
        assertEquals(expected, actual);
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void next_shouldHandleRepeatedInputLists() {
        // Arrange
        final CartesianProductIterator<Character> iterator = new CartesianProductIterator<>(letters, letters);
        final List<List<Character>> expected = new ArrayList<>();
        for (final Character l1 : letters) {
            for (final Character l2 : letters) {
                expected.add(Arrays.asList(l1, l2));
            }
        }

        // Act
        final List<List<Character>> actual = new ArrayList<>();
        iterator.forEachRemaining(actual::add);

        // Assert
        assertEquals(9, actual.size());
        assertEquals(expected, actual);
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Override
    @Test
    void testForEachRemaining() {
        // Arrange
        final CartesianProductIterator<Character> iterator = makeObject();
        final List<List<Character>> expected = new ArrayList<>();
        for (final Character letter : letters) {
            for (final Character number : numbers) {
                for (final Character symbol : symbols) {
                    expected.add(Arrays.asList(letter, number, symbol));
                }
            }
        }

        // Act
        final List<List<Character>> actual = new ArrayList<>();
        iterator.forEachRemaining(tuple -> actual.add(new ArrayList<>(tuple)));

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void remove_shouldThrowUnsupportedOperationException() {
        // Arrange
        final CartesianProductIterator<Character> iterator = makeObject();
        iterator.next(); // remove() can only be called after next()

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }
}