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
 * 
 * BoundedIterator decorates another iterator to return elements in a specific range
 * defined by an offset (starting position) and max (maximum number of elements).
 *
 * @param <E> the type of elements tested by this iterator.
 */
public class BoundedIteratorTest<E> extends AbstractIteratorTest<E> {

    // Test data: array ["a", "b", "c", "d", "e", "f", "g"] with indices 0-6
    private static final String[] TEST_ARRAY = {
        "a", "b", "c", "d", "e", "f", "g"
    };
    
    // Constants for better readability
    private static final int OFFSET_ZERO = 0;
    private static final int OFFSET_ONE = 1;
    private static final int OFFSET_TWO = 2;
    private static final int OFFSET_THREE = 3;
    private static final int OFFSET_TEN = 10;
    private static final int MAX_ZERO = 0;
    private static final int MAX_FOUR = 4;
    private static final int MAX_FIVE = 5;
    private static final int MAX_TEN = 10;

    private List<E> testList;

    @Override
    public Iterator<E> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<E>emptyList().iterator(), OFFSET_ZERO, MAX_TEN);
    }

    @Override
    public Iterator<E> makeObject() {
        return new BoundedIterator<>(new ArrayList<>(testList).iterator(), OFFSET_ONE, testList.size() - 1);
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() throws Exception {
        testList = Arrays.asList((E[]) TEST_ARRAY);
    }

    @Test
    void testBounded_WithOffsetAndLimit_ReturnsCorrectSubset() {
        // Given: iterator with offset=2, max=4 on ["a","b","c","d","e","f","g"]
        // Expected: returns ["c","d","e","f"] (4 elements starting from index 2)
        final Iterator<E> iterator = createBoundedIterator(OFFSET_TWO, MAX_FOUR);

        // When & Then: verify correct elements are returned in order
        assertNextElement(iterator, "c");
        assertNextElement(iterator, "d");
        assertNextElement(iterator, "e");
        assertNextElement(iterator, "f");
        
        assertIteratorExhausted(iterator);
    }

    @Test
    void testEmptyBounded_WithMaxZero_ReturnsNoElements() {
        // Given: iterator with max=0 (empty iterator regardless of offset)
        final Iterator<E> iterator = createBoundedIterator(OFFSET_THREE, MAX_ZERO);
        
        // When & Then: should behave as empty iterator
        assertIteratorExhausted(iterator);
    }

    @Test
    void testMaxGreaterThanSize_ReturnsAllRemainingElements() {
        // Given: iterator with offset=1, max=10 on 7-element array
        // Expected: returns all elements from index 1 to end ["b","c","d","e","f","g"]
        final Iterator<E> iterator = createBoundedIterator(OFFSET_ONE, MAX_TEN);

        // When & Then: verify all remaining elements are returned
        assertNextElement(iterator, "b");
        assertNextElement(iterator, "c");
        assertNextElement(iterator, "d");
        assertNextElement(iterator, "e");
        assertNextElement(iterator, "f");
        assertNextElement(iterator, "g");
        
        assertIteratorExhausted(iterator);
    }

    @Test
    void testNegativeMax_ThrowsIllegalArgumentException() {
        // When & Then: negative max should throw IllegalArgumentException
        final IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> createBoundedIterator(OFFSET_THREE, -1)
        );
        assertEquals("Max parameter must not be negative.", exception.getMessage());
    }

    @Test
    void testNegativeOffset_ThrowsIllegalArgumentException() {
        // When & Then: negative offset should throw IllegalArgumentException
        final IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> createBoundedIterator(-1, MAX_FOUR)
        );
        assertEquals("Offset parameter must not be negative.", exception.getMessage());
    }

    @Test
    void testOffsetGreaterThanSize_ReturnsNoElements() {
        // Given: iterator with offset beyond array size
        final Iterator<E> iterator = createBoundedIterator(OFFSET_TEN, MAX_FOUR);
        
        // When & Then: should behave as empty iterator
        assertIteratorExhausted(iterator);
    }

    @Test
    void testSameAsDecorated_WithFullRange_ReturnsAllElements() {
        // Given: iterator with offset=0 and max=size (should return all elements)
        final Iterator<E> iterator = createBoundedIterator(OFFSET_ZERO, testList.size());

        // When & Then: verify all elements are returned in order
        assertNextElement(iterator, "a");
        assertNextElement(iterator, "b");
        assertNextElement(iterator, "c");
        assertNextElement(iterator, "d");
        assertNextElement(iterator, "e");
        assertNextElement(iterator, "f");
        assertNextElement(iterator, "g");
        
        assertIteratorExhausted(iterator);
    }

    // Remove operation tests
    
    @Test
    void testRemoveFirst_RemovesElementFromUnderlyingCollection() {
        // Given: mutable copy of test list and iterator positioned at first element
        final List<E> mutableList = new ArrayList<>(testList);
        final Iterator<E> iterator = new BoundedIterator<>(mutableList.iterator(), OFFSET_ONE, MAX_FIVE);
        
        // When: advance to first element and remove it
        assertEquals("b", iterator.next());
        iterator.remove();
        
        // Then: element should be removed from underlying collection
        assertFalse(mutableList.contains("b"));
        
        // And: iterator should continue normally with remaining elements
        assertNextElement(iterator, "c");
        assertNextElement(iterator, "d");
        assertNextElement(iterator, "e");
        assertNextElement(iterator, "f");
        
        assertIteratorExhausted(iterator);
    }

    @Test
    void testRemoveMiddle_RemovesElementFromUnderlyingCollection() {
        // Given: mutable copy and iterator positioned at middle element
        final List<E> mutableList = new ArrayList<>(testList);
        final Iterator<E> iterator = new BoundedIterator<>(mutableList.iterator(), OFFSET_ONE, MAX_FIVE);
        
        // When: advance to middle element and remove it
        advanceIterator(iterator, 3); // Move to "d"
        iterator.remove();
        
        // Then: element should be removed from underlying collection
        assertFalse(mutableList.contains("d"));
        
        // And: iterator should continue with remaining elements
        assertNextElement(iterator, "e");
        assertNextElement(iterator, "f");
        
        assertIteratorExhausted(iterator);
    }

    @Test
    void testRemoveLast_RemovesElementFromUnderlyingCollection() {
        // Given: mutable copy and iterator positioned at last element
        final List<E> mutableList = new ArrayList<>(testList);
        final Iterator<E> iterator = new BoundedIterator<>(mutableList.iterator(), OFFSET_ONE, MAX_FIVE);
        
        // When: advance to last element
        advanceIterator(iterator, 5); // Move through all elements to "f"
        assertIteratorExhausted(iterator);
        
        // And: remove the last element
        iterator.remove();
        
        // Then: element should be removed from underlying collection
        assertFalse(mutableList.contains("f"));
        assertIteratorExhausted(iterator);
    }

    @Test
    void testRemoveWithoutCallingNext_ThrowsIllegalStateException() {
        // Given: fresh iterator
        final List<E> mutableList = new ArrayList<>(testList);
        final Iterator<E> iterator = new BoundedIterator<>(mutableList.iterator(), OFFSET_ONE, MAX_FIVE);
        
        // When & Then: calling remove() before next() should throw IllegalStateException
        final IllegalStateException exception = assertThrows(
            IllegalStateException.class, 
            iterator::remove
        );
        assertEquals("remove() cannot be called before calling next()", exception.getMessage());
    }

    @Test
    void testRemoveCalledTwice_ThrowsIllegalStateException() {
        // Given: iterator with one element consumed
        final List<E> mutableList = new ArrayList<>(testList);
        final Iterator<E> iterator = new BoundedIterator<>(mutableList.iterator(), OFFSET_ONE, MAX_FIVE);
        
        iterator.next();
        iterator.remove(); // First remove should succeed
        
        // When & Then: second remove() without next() should throw IllegalStateException
        assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    void testRemoveUnsupported_PropagatesUnsupportedOperationException() {
        // Given: iterator that doesn't support remove operation
        final Iterator<E> unsupportedRemoveIterator = createUnsupportedRemoveIterator();
        final Iterator<E> boundedIterator = new BoundedIterator<>(unsupportedRemoveIterator, OFFSET_ONE, MAX_FIVE);
        
        // When: try to remove after calling next()
        assertEquals("b", boundedIterator.next());
        
        // Then: should propagate UnsupportedOperationException
        final UnsupportedOperationException exception = assertThrows(
            UnsupportedOperationException.class, 
            boundedIterator::remove
        );
        assertNull(exception.getMessage());
    }

    // Helper methods for better readability and reduced duplication
    
    private Iterator<E> createBoundedIterator(int offset, int max) {
        return new BoundedIterator<>(testList.iterator(), offset, max);
    }
    
    private void assertNextElement(Iterator<E> iterator, String expectedElement) {
        assertTrue(iterator.hasNext(), "Iterator should have next element");
        assertEquals(expectedElement, iterator.next(), "Next element should match expected value");
    }
    
    private void assertIteratorExhausted(Iterator<E> iterator) {
        assertFalse(iterator.hasNext(), "Iterator should be exhausted");
        assertThrows(NoSuchElementException.class, iterator::next, "Should throw NoSuchElementException when exhausted");
    }
    
    private void advanceIterator(Iterator<E> iterator, int steps) {
        for (int i = 0; i < steps; i++) {
            if (iterator.hasNext()) {
                iterator.next();
            }
        }
    }
    
    private Iterator<E> createUnsupportedRemoveIterator() {
        return new AbstractIteratorDecorator<E>(testList.iterator()) {
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}