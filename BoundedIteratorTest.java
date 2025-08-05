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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link BoundedIterator}.
 */
public class BoundedIteratorTest<E> extends AbstractIteratorTest<E> {

    private static final String[] TEST_ARRAY = {"a", "b", "c", "d", "e", "f", "g"};
    private List<E> testList;

    @Override
    public Iterator<E> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.emptyIterator(), 0, 10);
    }

    @Override
    public Iterator<E> makeObject() {
        return new BoundedIterator<>(testList.iterator(), 1, testList.size() - 1);
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        testList = Arrays.asList((E[]) TEST_ARRAY);
    }

    // ======================================================
    // Constructor Validation Tests
    // ======================================================

    @Test
    void constructor_NegativeOffset_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new BoundedIterator<>(testList.iterator(), -1, 5));
        assertEquals("Offset parameter must not be negative.", ex.getMessage());
    }

    @Test
    void constructor_NegativeMax_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new BoundedIterator<>(testList.iterator(), 0, -1));
        assertEquals("Max parameter must not be negative.", ex.getMessage());
    }

    // ======================================================
    // Iteration Behavior Tests
    // ======================================================

    @Test
    void iteration_OffsetAndMaxWithinBounds_ReturnsExpectedElements() {
        Iterator<E> iterator = new BoundedIterator<>(testList.iterator(), 2, 4);

        assertEquals("c", iterator.next());
        assertEquals("d", iterator.next());
        assertEquals("e", iterator.next());
        assertEquals("f", iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void iteration_MaxExceedsSize_ReturnsElementsFromOffsetToEnd() {
        Iterator<E> iterator = new BoundedIterator<>(testList.iterator(), 1, 10);

        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertEquals("d", iterator.next());
        assertEquals("e", iterator.next());
        assertEquals("f", iterator.next());
        assertEquals("g", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void iteration_OffsetExceedsSize_ReturnsNoElements() {
        Iterator<E> iterator = new BoundedIterator<>(testList.iterator(), 10, 5);
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void iteration_MaxIsZero_ReturnsNoElements() {
        Iterator<E> iterator = new BoundedIterator<>(testList.iterator(), 3, 0);
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void iteration_OffsetZeroAndMaxSize_ReturnsAllElements() {
        Iterator<E> iterator = new BoundedIterator<>(testList.iterator(), 0, testList.size());

        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertEquals("d", iterator.next());
        assertEquals("e", iterator.next());
        assertEquals("f", iterator.next());
        assertEquals("g", iterator.next());
        assertFalse(iterator.hasNext());
    }

    // ======================================================
    // Removal Behavior Tests
    // ======================================================

    @Test
    void remove_FirstElement_RemovesFromUnderlyingCollection() {
        List<E> copy = new ArrayList<>(testList);
        Iterator<E> iterator = new BoundedIterator<>(copy.iterator(), 1, 5);

        assertEquals("b", iterator.next());
        iterator.remove();
        assertFalse(copy.contains("b"));
        assertEquals(6, copy.size());
    }

    @Test
    void remove_MiddleElement_RemovesFromUnderlyingCollection() {
        List<E> copy = new ArrayList<>(testList);
        Iterator<E> iterator = new BoundedIterator<>(copy.iterator(), 1, 5);

        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertEquals("d", iterator.next());
        iterator.remove();
        assertFalse(copy.contains("d"));
        assertEquals(6, copy.size());
    }

    @Test
    void remove_LastElement_RemovesFromUnderlyingCollection() {
        List<E> copy = new ArrayList<>(testList);
        Iterator<E> iterator = new BoundedIterator<>(copy.iterator(), 1, 5);

        // Advance to last element
        iterator.next(); // b
        iterator.next(); // c
        iterator.next(); // d
        iterator.next(); // e
        iterator.next(); // f
        iterator.remove();
        assertFalse(copy.contains("f"));
        assertEquals(6, copy.size());
    }

    @Test
    void remove_WithoutCallingNext_ThrowsIllegalStateException() {
        List<E> copy = new ArrayList<>(testList);
        Iterator<E> iterator = new BoundedIterator<>(copy.iterator(), 1, 5);

        IllegalStateException ex = assertThrows(IllegalStateException.class, iterator::remove);
        assertEquals("remove() cannot be called before calling next()", ex.getMessage());
    }

    @Test
    void remove_CalledTwiceWithoutNext_ThrowsIllegalStateException() {
        List<E> copy = new ArrayList<>(testList);
        Iterator<E> iterator = new BoundedIterator<>(copy.iterator(), 1, 5);

        iterator.next(); // b
        iterator.remove();
        assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    void remove_UnsupportedByDecoratedIterator_ThrowsUnsupportedOperationException() {
        Iterator<E> mockIterator = new Iterator<E>() {
            private final Iterator<E> delegate = testList.iterator();
            @Override public boolean hasNext() { return delegate.hasNext(); }
            @Override public E next() { return delegate.next(); }
            @Override public void remove() { throw new UnsupportedOperationException(); }
        };

        Iterator<E> iterator = new BoundedIterator<>(mockIterator, 1, 5);
        iterator.next(); // b
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }
}