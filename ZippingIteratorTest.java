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
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test suite for {@link ZippingIterator}.
 * 
 * ZippingIterator provides interleaved iteration over multiple iterators.
 * For example, given iterators [1,3,5] and [2,4,6], it produces [1,2,3,4,5,6].
 */
@SuppressWarnings("boxing")
class ZippingIteratorTest extends AbstractIteratorTest<Integer> {

    private static final int TEST_RANGE_SIZE = 20;
    
    private List<Integer> evenNumbers;
    private List<Integer> oddNumbers;
    private List<Integer> fibonacciNumbers;

    @Override
    @SuppressWarnings("unchecked")
    public ZippingIterator<Integer> makeEmptyIterator() {
        return new ZippingIterator<>(IteratorUtils.<Integer>emptyIterator());
    }

    @Override
    public ZippingIterator<Integer> makeObject() {
        return new ZippingIterator<>(
            evenNumbers.iterator(), 
            oddNumbers.iterator(), 
            fibonacciNumbers.iterator()
        );
    }

    @BeforeEach
    public void setUp() throws Exception {
        setupTestData();
    }

    private void setupTestData() {
        evenNumbers = createEvenNumbers(TEST_RANGE_SIZE);
        oddNumbers = createOddNumbers(TEST_RANGE_SIZE);
        fibonacciNumbers = createFibonacciSequence();
    }

    private List<Integer> createEvenNumbers(int range) {
        List<Integer> evens = new ArrayList<>();
        for (int i = 0; i < range; i += 2) {
            evens.add(i);
        }
        return evens;
    }

    private List<Integer> createOddNumbers(int range) {
        List<Integer> odds = new ArrayList<>();
        for (int i = 1; i < range; i += 2) {
            odds.add(i);
        }
        return odds;
    }

    private List<Integer> createFibonacciSequence() {
        return Arrays.asList(1, 1, 2, 3, 5, 8, 13, 21);
    }

    @Test
    void testIterateWithSingleIterator_ShouldReturnAllElements() {
        // Given: A ZippingIterator with only even numbers
        @SuppressWarnings("unchecked")
        ZippingIterator<Integer> iterator = new ZippingIterator<>(evenNumbers.iterator());
        
        // When & Then: Should iterate through all even numbers in order
        for (Integer expectedEven : evenNumbers) {
            assertTrue(iterator.hasNext(), "Iterator should have next element");
            assertEquals(expectedEven, iterator.next(), "Should return expected even number");
        }
        assertFalse(iterator.hasNext(), "Iterator should be exhausted");
    }

    @Test
    void testIterateWithDuplicateIterators_ShouldInterleaveIdenticalSequences() {
        // Given: A ZippingIterator with the same iterator twice
        ZippingIterator<Integer> iterator = new ZippingIterator<>(
            evenNumbers.iterator(), 
            evenNumbers.iterator()
        );
        
        // When & Then: Should return each even number twice in sequence
        for (Integer expectedEven : evenNumbers) {
            assertTrue(iterator.hasNext());
            assertEquals(expectedEven, iterator.next(), "First occurrence of even number");
            
            assertTrue(iterator.hasNext());
            assertEquals(expectedEven, iterator.next(), "Second occurrence of even number");
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIterateWithEvenAndOddNumbers_ShouldProduceSequentialOrder() {
        // Given: A ZippingIterator with even and odd numbers
        ZippingIterator<Integer> iterator = new ZippingIterator<>(
            evenNumbers.iterator(), 
            oddNumbers.iterator()
        );
        
        // When & Then: Should produce natural sequence 0,1,2,3,4,5...19
        for (int expectedValue = 0; expectedValue < TEST_RANGE_SIZE; expectedValue++) {
            assertTrue(iterator.hasNext(), "Iterator should have element at position " + expectedValue);
            assertEquals(Integer.valueOf(expectedValue), iterator.next(), 
                "Should return sequential number: " + expectedValue);
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIterateWithThreeIterators_ShouldInterleaveFibonacciEvenOdd() {
        // Given: A ZippingIterator with fibonacci, even, and odd numbers
        ZippingIterator<Integer> iterator = new ZippingIterator<>(
            fibonacciNumbers.iterator(), 
            evenNumbers.iterator(), 
            oddNumbers.iterator()
        );
        
        // When & Then: Should cycle through iterators: fib -> even -> odd -> fib -> even -> odd...
        List<Integer> expectedSequence = buildExpectedInterleavedSequence();
        
        for (int i = 0; i < expectedSequence.size(); i++) {
            assertTrue(iterator.hasNext(), "Iterator should have element at position " + i);
            assertEquals(expectedSequence.get(i), iterator.next(), 
                "Element at position " + i + " should match expected sequence");
        }
        assertFalse(iterator.hasNext());
    }

    private List<Integer> buildExpectedInterleavedSequence() {
        List<Integer> expected = new ArrayList<>();
        int fibIndex = 0, evenIndex = 0, oddIndex = 0;
        
        // Continue until all iterators are exhausted
        while (fibIndex < fibonacciNumbers.size() || 
               evenIndex < evenNumbers.size() || 
               oddIndex < oddNumbers.size()) {
            
            // Add from fibonacci if available
            if (fibIndex < fibonacciNumbers.size()) {
                expected.add(fibonacciNumbers.get(fibIndex++));
            }
            // Add from evens if available
            if (evenIndex < evenNumbers.size()) {
                expected.add(evenNumbers.get(evenIndex++));
            }
            // Add from odds if available
            if (oddIndex < oddNumbers.size()) {
                expected.add(oddNumbers.get(oddIndex++));
            }
        }
        return expected;
    }

    @Test
    void testIterateWithOddThenEven_ShouldAlternateStartingWithOdd() {
        // Given: A ZippingIterator with odd numbers first, then even numbers
        ZippingIterator<Integer> iterator = new ZippingIterator<>(
            oddNumbers.iterator(), 
            evenNumbers.iterator()
        );
        
        // When & Then: Should alternate between odd and even, starting with odd
        for (int position = 0; position < TEST_RANGE_SIZE; position++) {
            assertTrue(iterator.hasNext());
            Integer actualValue = iterator.next();
            
            if (isEvenPosition(position)) {
                // Even positions should contain odd numbers
                Integer expectedOddValue = oddNumbers.get(position / 2);
                assertEquals(expectedOddValue, actualValue, 
                    "Position " + position + " should contain odd number");
            } else {
                // Odd positions should contain even numbers
                Integer expectedEvenValue = evenNumbers.get(position / 2);
                assertEquals(expectedEvenValue, actualValue, 
                    "Position " + position + " should contain even number");
            }
        }
        assertFalse(iterator.hasNext());
    }

    private boolean isEvenPosition(int position) {
        return position % 2 == 0;
    }

    @Test
    void testRemoveFromMultipleIterators_ShouldRemoveFromCorrectSource() {
        // Given: A ZippingIterator with even and odd numbers
        ZippingIterator<Integer> iterator = new ZippingIterator<>(
            evenNumbers.iterator(), 
            oddNumbers.iterator()
        );
        
        int originalTotalSize = evenNumbers.size() + oddNumbers.size();
        int expectedRemovals = 0;
        
        // When: Remove elements divisible by 4 or 3
        while (iterator.hasNext()) {
            Integer value = iterator.next();
            if (shouldRemoveValue(value)) {
                expectedRemovals++;
                iterator.remove();
            }
        }
        
        // Then: Total size should be reduced by the number of removed elements
        int actualTotalSize = evenNumbers.size() + oddNumbers.size();
        int expectedTotalSize = originalTotalSize - expectedRemovals;
        assertEquals(expectedTotalSize, actualTotalSize, 
            "Total size should be reduced by removed elements");
    }

    @Test
    void testRemoveFromSingleIterator_ShouldRemoveFromSource() {
        // Given: A ZippingIterator with only even numbers
        @SuppressWarnings("unchecked")
        ZippingIterator<Integer> iterator = new ZippingIterator<>(evenNumbers.iterator());
        
        int originalSize = evenNumbers.size();
        int expectedRemovals = 0;
        
        // When: Remove elements divisible by 4
        while (iterator.hasNext()) {
            Integer value = iterator.next();
            if (value % 4 == 0) {
                expectedRemovals++;
                iterator.remove();
            }
        }
        
        // Then: Size should be reduced by the number of removed elements
        int expectedSize = originalSize - expectedRemovals;
        assertEquals(expectedSize, evenNumbers.size(), 
            "Even numbers list should be reduced by removed elements");
    }

    private boolean shouldRemoveValue(Integer value) {
        return value % 4 == 0 || value % 3 == 0;
    }
}