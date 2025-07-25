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
 * Unit tests for the {@link BoundedIterator}.
 * <p>
 *   This test suite focuses on verifying the correct behavior of the
 *   {@link BoundedIterator} class, including boundary conditions,
 *   edge cases, and exception handling.
 * </p>
 * @param <E> The type of elements iterated over.  Using String for concrete tests.
 */
public class BoundedIteratorTest<E> extends AbstractIteratorTest<E> {

    private static final String[] STRING_ARRAY = {"a", "b", "c", "d", "e", "f", "g"};
    private List<E> testList;

    @Override
    public Iterator<E> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<E>emptyList().iterator(), 0, 10);
    }

    @Override
    public Iterator<E> makeObject() {
        return new BoundedIterator<>(new ArrayList<>(testList).iterator(), 1, testList.size() - 1);
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        testList = Arrays.asList((E[]) STRING_ARRAY);
    }

    @Test
    void testBoundedIteratorWithinRange() {
        // Arrange
        final int offset = 2;
        final int maxElements = 4;
        final Iterator<E> iterator = new BoundedIterator<>(testList.iterator(), offset, maxElements);

        // Act & Assert
        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("c", iterator.next(), "First element should be 'c'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("d", iterator.next(), "Second element should be 'd'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("e", iterator.next(), "Third element should be 'e'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("f", iterator.next(), "Fourth element should be 'f'.");

        assertFalse(iterator.hasNext(), "Iterator should not have next element.");
        assertThrows(NoSuchElementException.class, iterator::next, "Should throw NoSuchElementException.");
    }

    @Test
    void testBoundedIteratorWithZeroMax() {
        // Arrange
        final int offset = 3;
        final int maxElements = 0;
        final Iterator<E> iterator = new BoundedIterator<>(testList.iterator(), offset, maxElements);

        // Act & Assert
        assertFalse(iterator.hasNext(), "Iterator should be empty.");
        assertThrows(NoSuchElementException.class, iterator::next, "Should throw NoSuchElementException.");
    }

    @Test
    void testBoundedIteratorWithMaxGreaterThanSize() {
        // Arrange
        final int offset = 1;
        final int maxElements = 10;
        final Iterator<E> iterator = new BoundedIterator<>(testList.iterator(), offset, maxElements);

        // Act & Assert
        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("b", iterator.next(), "First element should be 'b'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("c", iterator.next(), "Second element should be 'c'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("d", iterator.next(), "Third element should be 'd'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("e", iterator.next(), "Fourth element should be 'e'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("f", iterator.next(), "Fifth element should be 'f'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("g", iterator.next(), "Sixth element should be 'g'.");

        assertFalse(iterator.hasNext(), "Iterator should not have next element.");
        assertThrows(NoSuchElementException.class, iterator::next, "Should throw NoSuchElementException.");
    }

    @Test
    void testNegativeMaxThrowsIllegalArgumentException() {
        // Arrange
        final int offset = 3;
        final int negativeMax = -1;

        // Act & Assert
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new BoundedIterator<>(testList.iterator(), offset, negativeMax), "Should throw IllegalArgumentException.");
        assertEquals("Max parameter must not be negative.", thrown.getMessage(), "Exception message should match.");
    }

    @Test
    void testNegativeOffsetThrowsIllegalArgumentException() {
        // Arrange
        final int negativeOffset = -1;
        final int maxElements = 4;

        // Act & Assert
        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> new BoundedIterator<>(testList.iterator(), negativeOffset, maxElements), "Should throw IllegalArgumentException.");
        assertEquals("Offset parameter must not be negative.", thrown.getMessage(), "Exception message should match.");
    }

    @Test
    void testOffsetGreaterThanSizeResultsInEmptyIterator() {
        // Arrange
        final int offset = 10;
        final int maxElements = 4;
        final Iterator<E> iterator = new BoundedIterator<>(testList.iterator(), offset, maxElements);

        // Act & Assert
        assertFalse(iterator.hasNext(), "Iterator should be empty.");
        assertThrows(NoSuchElementException.class, iterator::next, "Should throw NoSuchElementException.");
    }

    @Test
    void testRemoveCalledTwiceThrowsIllegalStateException() {
        // Arrange
        final List<E> testListCopy = new ArrayList<>(testList);
        final int offset = 1;
        final int maxElements = 5;
        final Iterator<E> iterator = new BoundedIterator<>(testListCopy.iterator(), offset, maxElements);

        // Act
        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("b", iterator.next(), "First element should be 'b'.");
        iterator.remove();

        // Assert
        assertThrows(IllegalStateException.class, iterator::remove, "Should throw IllegalStateException.");
    }

    @Test
    void testRemoveFirstElementRemovesFromUnderlyingList() {
        // Arrange
        final List<E> testListCopy = new ArrayList<>(testList);
        final int offset = 1;
        final int maxElements = 5;
        final Iterator<E> iterator = new BoundedIterator<>(testListCopy.iterator(), offset, maxElements);

        // Act
        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("b", iterator.next(), "First element should be 'b'.");
        iterator.remove();

        // Assert
        assertFalse(testListCopy.contains("b"), "Underlying list should not contain 'b'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("c", iterator.next(), "Second element should be 'c'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("d", iterator.next(), "Third element should be 'd'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("e", iterator.next(), "Fourth element should be 'e'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("f", iterator.next(), "Fifth element should be 'f'.");

        assertFalse(iterator.hasNext(), "Iterator should not have next element.");
        assertThrows(NoSuchElementException.class, iterator::next, "Should throw NoSuchElementException.");
    }

    @Test
    void testRemoveLastElementRemovesFromUnderlyingList() {
        // Arrange
        final List<E> testListCopy = new ArrayList<>(testList);
        final int offset = 1;
        final int maxElements = 5;
        final Iterator<E> iterator = new BoundedIterator<>(testListCopy.iterator(), offset, maxElements);

        // Act
        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("b", iterator.next(), "First element should be 'b'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("c", iterator.next(), "Second element should be 'c'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("d", iterator.next(), "Third element should be 'd'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("e", iterator.next(), "Fourth element should be 'e'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("f", iterator.next(), "Fifth element should be 'f'.");

        assertFalse(iterator.hasNext(), "Iterator should not have next element.");
        assertThrows(NoSuchElementException.class, iterator::next, "Should throw NoSuchElementException.");

        iterator.remove();

        // Assert
        assertFalse(testListCopy.contains("f"), "Underlying list should not contain 'f'.");
    }

    @Test
    void testRemoveMiddleElementRemovesFromUnderlyingList() {
        // Arrange
        final List<E> testListCopy = new ArrayList<>(testList);
        final int offset = 1;
        final int maxElements = 5;
        final Iterator<E> iterator = new BoundedIterator<>(testListCopy.iterator(), offset, maxElements);

        // Act
        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("b", iterator.next(), "First element should be 'b'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("c", iterator.next(), "Second element should be 'c'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("d", iterator.next(), "Third element should be 'd'.");
        iterator.remove();

        // Assert
        assertFalse(testListCopy.contains("d"), "Underlying list should not contain 'd'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("e", iterator.next(), "Fourth element should be 'e'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("f", iterator.next(), "Fifth element should be 'f'.");

        assertFalse(iterator.hasNext(), "Iterator should not have next element.");
        assertThrows(NoSuchElementException.class, iterator::next, "Should throw NoSuchElementException.");
    }

    @Test
    void testRemoveUnsupportedFromDecoratedIteratorThrowsUnsupportedOperationException() {
        // Arrange
        final Iterator<E> mockIterator = new AbstractIteratorDecorator<E>(testList.iterator()) {
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

        final int offset = 1;
        final int maxElements = 5;
        final Iterator<E> iterator = new BoundedIterator<>(mockIterator, offset, maxElements);

        // Act
        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("b", iterator.next(), "First element should be 'b'.");

        // Assert
        assertThrows(UnsupportedOperationException.class, iterator::remove, "Should throw UnsupportedOperationException.");
    }

    @Test
    void testRemoveWithoutCallingNextThrowsIllegalStateException() {
        // Arrange
        final List<E> testListCopy = new ArrayList<>(testList);
        final int offset = 1;
        final int maxElements = 5;
        final Iterator<E> iterator = new BoundedIterator<>(testListCopy.iterator(), offset, maxElements);

        // Act & Assert
        final IllegalStateException thrown = assertThrows(IllegalStateException.class, iterator::remove, "Should throw IllegalStateException.");
        assertEquals("remove() cannot be called before calling next()", thrown.getMessage(), "Exception message should match.");
    }

    @Test
    void testSameAsDecoratedIteratorWhenOffsetIsZeroAndMaxIsSize() {
        // Arrange
        final int offset = 0;
        final int maxElements = testList.size();
        final Iterator<E> iterator = new BoundedIterator<>(testList.iterator(), offset, maxElements);

        // Act & Assert
        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("a", iterator.next(), "First element should be 'a'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("b", iterator.next(), "Second element should be 'b'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("c", iterator.next(), "Third element should be 'c'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("d", iterator.next(), "Fourth element should be 'd'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("e", iterator.next(), "Fifth element should be 'e'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("f", iterator.next(), "Sixth element should be 'f'.");

        assertTrue(iterator.hasNext(), "Iterator should have next element.");
        assertEquals("g", iterator.next(), "Seventh element should be 'g'.");

        assertFalse(iterator.hasNext(), "Iterator should not have next element.");
        assertThrows(NoSuchElementException.class, iterator::next, "Should throw NoSuchElementException.");
    }
}