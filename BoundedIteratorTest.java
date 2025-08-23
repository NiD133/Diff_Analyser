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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedIterator}.
 *
 * This version focuses on readability by:
 * - Using String as the concrete element type to remove noise from generics.
 * - Introducing small helper methods to reduce repetition and clarify intent.
 * - Giving tests descriptive names and adding brief intent-revealing comments.
 */
public class BoundedIteratorTest extends AbstractIteratorTest<String> {

    private static final List<String> LETTERS =
            Arrays.asList("a", "b", "c", "d", "e", "f", "g");

    private List<String> mutableLetters;

    @BeforeEach
    void setUp() {
        // Fresh copy for tests that mutate the underlying collection via remove()
        mutableLetters = new ArrayList<>(LETTERS);
    }

    // AbstractIteratorTest hooks

    @Override
    public Iterator<String> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<String>emptyList().iterator(), 0, 10);
    }

    @Override
    public Iterator<String> makeObject() {
        return new BoundedIterator<>(new ArrayList<>(LETTERS).iterator(), 1, LETTERS.size() - 1);
    }

    // Helper methods

    private static Iterator<String> bounded(List<String> src, long offset, long max) {
        return new BoundedIterator<>(src.iterator(), offset, max);
    }

    private static void assertNextEquals(Iterator<?> it, Object expected) {
        assertTrue(it.hasNext());
        assertEquals(expected, it.next());
    }

    private static void assertNoMoreElements(Iterator<?> it) {
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    // Tests

    @Test
    @DisplayName("Returns only elements within [offset, offset+max)")
    void bounded_returnsElementsWithinRange() {
        Iterator<String> it = bounded(LETTERS, 2, 4);

        assertNextEquals(it, "c");
        assertNextEquals(it, "d");
        assertNextEquals(it, "e");
        assertNextEquals(it, "f");
        assertNoMoreElements(it);
    }

    @Test
    @DisplayName("max = 0 behaves like an empty iterator")
    void emptyRange_maxZero_behavesEmpty() {
        Iterator<String> it = bounded(LETTERS, 3, 0);

        assertNoMoreElements(it);
    }

    @Test
    @DisplayName("max > size consumes until underlying iterator ends")
    void maxGreaterThanSize_consumesToEnd() {
        Iterator<String> it = bounded(LETTERS, 1, 10);

        assertNextEquals(it, "b");
        assertNextEquals(it, "c");
        assertNextEquals(it, "d");
        assertNextEquals(it, "e");
        assertNextEquals(it, "f");
        assertNextEquals(it, "g");
        assertNoMoreElements(it);
    }

    @Test
    @DisplayName("Negative max throws IllegalArgumentException with message")
    void negativeMax_throwsIAEWithMessage() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> bounded(LETTERS, 3, -1));
        assertEquals("Max parameter must not be negative.", ex.getMessage());
    }

    @Test
    @DisplayName("Negative offset throws IllegalArgumentException with message")
    void negativeOffset_throwsIAEWithMessage() {
        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> bounded(LETTERS, -1, 4));
        assertEquals("Offset parameter must not be negative.", ex.getMessage());
    }

    @Test
    @DisplayName("Offset > size behaves like an empty iterator")
    void offsetGreaterThanSize_behavesEmpty() {
        Iterator<String> it = bounded(LETTERS, 10, 4);

        assertNoMoreElements(it);
    }

    @Test
    @DisplayName("Calling remove() twice without next() throws IllegalStateException")
    void remove_calledTwice_throwsIllegalState() {
        Iterator<String> it = bounded(mutableLetters, 1, 5);

        assertNextEquals(it, "b");
        it.remove();

        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    @DisplayName("remove() after first element removes from backing collection")
    void remove_firstElement_removesFromBackedCollection() {
        Iterator<String> it = bounded(mutableLetters, 1, 5);

        assertNextEquals(it, "b");
        it.remove();
        assertFalse(mutableLetters.contains("b"));

        assertNextEquals(it, "c");
        assertNextEquals(it, "d");
        assertNextEquals(it, "e");
        assertNextEquals(it, "f");
        assertNoMoreElements(it);
    }

    @Test
    @DisplayName("remove() after reaching the end removes the last returned element")
    void remove_lastElement_removesFromBackedCollection() {
        Iterator<String> it = bounded(mutableLetters, 1, 5);

        assertNextEquals(it, "b");
        assertNextEquals(it, "c");
        assertNextEquals(it, "d");
        assertNextEquals(it, "e");
        assertNextEquals(it, "f");

        assertFalse(it.hasNext());
        NoSuchElementException nse = assertThrows(NoSuchElementException.class, it::next);
        assertNull(nse.getMessage());

        it.remove(); // removes "f"
        assertFalse(mutableLetters.contains("f"));

        assertFalse(it.hasNext());
        NoSuchElementException nse2 = assertThrows(NoSuchElementException.class, it::next);
        assertNull(nse2.getMessage());
    }

    @Test
    @DisplayName("remove() in the middle removes from backing collection")
    void remove_middleElement_removesFromBackedCollection() {
        Iterator<String> it = bounded(mutableLetters, 1, 5);

        assertNextEquals(it, "b");
        assertNextEquals(it, "c");
        assertNextEquals(it, "d");

        it.remove(); // removes "d"
        assertFalse(mutableLetters.contains("d"));

        assertNextEquals(it, "e");
        assertNextEquals(it, "f");
        assertNoMoreElements(it);
    }

    @Test
    @DisplayName("If underlying iterator does not support remove, the exception is propagated")
    void remove_notSupported_propagatesUnsupportedOperationException() {
        Iterator<String> unremovable = new AbstractIteratorDecorator<String>(LETTERS.iterator()) {
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

        Iterator<String> it = new BoundedIterator<>(unremovable, 1, 5);
        assertNextEquals(it, "b");

        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class, it::remove);
        assertNull(ex.getMessage());
    }

    @Test
    @DisplayName("Calling remove() before next() throws IllegalStateException with message")
    void remove_withoutCallingNext_throwsIllegalStateWithMessage() {
        Iterator<String> it = bounded(mutableLetters, 1, 5);

        IllegalStateException ex = assertThrows(IllegalStateException.class, it::remove);
        assertEquals("remove() cannot be called before calling next()", ex.getMessage());
    }

    @Test
    @DisplayName("Offset = 0 and max = size yields same elements as decorated iterator")
    void sameAsDecorated_offsetZeroMaxSize_returnsAllElements() {
        Iterator<String> it = bounded(LETTERS, 0, LETTERS.size());

        assertNextEquals(it, "a");
        assertNextEquals(it, "b");
        assertNextEquals(it, "c");
        assertNextEquals(it, "d");
        assertNextEquals(it, "e");
        assertNextEquals(it, "f");
        assertNextEquals(it, "g");
        assertNoMoreElements(it);
    }
}