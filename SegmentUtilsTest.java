/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress.harmony.unpack200;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for SegmentUtils utility methods used in unpack200 operations.
 * This class tests method descriptor parsing and matching operations.
 */
class SegmentUtilsTest {

    /**
     * Test implementation of IMatcher that matches numbers divisible by a given divisor.
     * Used to test the countMatches functionality with different criteria.
     */
    private static final class DivisibilityMatcher implements IMatcher {
        private final int divisor;

        DivisibilityMatcher(final int divisor) {
            this.divisor = divisor;
        }

        @Override
        public boolean matches(final long value) {
            return value % divisor == 0;
        }
    }

    // Matcher instances for common test scenarios
    private static final IMatcher EVEN_NUMBER_MATCHER = new DivisibilityMatcher(2);
    private static final IMatcher MULTIPLE_OF_FIVE_MATCHER = new DivisibilityMatcher(5);

    /**
     * Provides test data for countArgs method.
     * Tests various Java method descriptors and their expected argument counts.
     * 
     * Format: (parameter_types)return_type
     * - V = void
     * - D = double
     * - [D = double array
     * - [[D = double array of arrays
     * - Lblah/blah; = object reference
     */
    static Stream<Arguments> methodDescriptorTestData() {
        return Stream.of(
            // No parameters
            Arguments.of("()V", 0),
            
            // Single parameter cases
            Arguments.of("(D)V", 1),           // double parameter
            Arguments.of("([D)V", 1),          // double array parameter
            Arguments.of("([[D)V", 1),         // double array of arrays parameter
            
            // Multiple parameter cases
            Arguments.of("(DD)V", 2),          // two double parameters
            Arguments.of("(DDD)V", 3),         // three double parameters
            
            // Mixed parameter types
            Arguments.of("(Lblah/blah;D)V", 2),        // object reference + double
            Arguments.of("(Lblah/blah;DLbLah;)V", 3)   // object reference + double + object reference
        );
    }

    /**
     * Provides test data for countInvokeInterfaceArgs method.
     * For invokeinterface, long and double types count as 2 arguments (they occupy 2 stack slots).
     * 
     * Format: (parameter_types)return_type
     * - Z = boolean (1 slot)
     * - D = double (2 slots)
     * - J = long (2 slots)
     * - [type = array reference (1 slot regardless of element type)
     * - Lclass; = object reference (1 slot)
     */
    static Stream<Arguments> invokeInterfaceArgsTestData() {
        return Stream.of(
            // Single parameter cases
            Arguments.of("(Z)V", 1),           // boolean: 1 slot
            Arguments.of("(D)V", 2),           // double: 2 slots
            Arguments.of("(J)V", 2),           // long: 2 slots
            Arguments.of("([D)V", 1),          // double array reference: 1 slot
            Arguments.of("([[D)V", 1),         // double array of arrays reference: 1 slot
            
            // Multiple parameter cases
            Arguments.of("(DD)V", 4),          // two doubles: 2 + 2 = 4 slots
            Arguments.of("(Lblah/blah;D)V", 3),        // object reference + double: 1 + 2 = 3 slots
            Arguments.of("(Lblah/blah;DLbLah;)V", 4),  // object + double + object: 1 + 2 + 1 = 4 slots
            Arguments.of("([Lblah/blah;DLbLah;)V", 4)  // object array + double + object: 1 + 2 + 1 = 4 slots
        );
    }

    /**
     * Tests the countArgs method which counts the number of arguments in a method descriptor.
     * Each parameter type counts as 1 argument regardless of its JVM stack slot usage.
     */
    @ParameterizedTest
    @MethodSource("methodDescriptorTestData")
    void shouldCountArgumentsInMethodDescriptor(final String methodDescriptor, final int expectedArgumentCount) {
        int actualArgumentCount = SegmentUtils.countArgs(methodDescriptor);
        
        assertEquals(expectedArgumentCount, actualArgumentCount,
            String.format("Method descriptor '%s' should have %d arguments", 
                methodDescriptor, expectedArgumentCount));
    }

    /**
     * Tests the countInvokeInterfaceArgs method which counts arguments considering JVM stack slot usage.
     * Long and double types occupy 2 stack slots, while other types occupy 1 slot.
     * This is important for the invokeinterface bytecode instruction.
     */
    @ParameterizedTest
    @MethodSource("invokeInterfaceArgsTestData")
    void shouldCountInvokeInterfaceArgumentSlots(final String methodDescriptor, final int expectedSlotCount) {
        int actualSlotCount = SegmentUtils.countInvokeInterfaceArgs(methodDescriptor);
        
        assertEquals(expectedSlotCount, actualSlotCount,
            String.format("Method descriptor '%s' should require %d stack slots for invokeinterface", 
                methodDescriptor, expectedSlotCount));
    }

    /**
     * Tests the countMatches method with both 1D and 2D arrays.
     * Verifies that the method correctly counts elements matching given criteria.
     */
    @Test
    void shouldCountMatchingElementsInArrays() {
        // Test data: numbers 1 through 10
        final long[] numbersOneToTen = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        final long[] numbersFromFiveToSeven = { 5, 6, 7 };
        final long[][] twoDimensionalArray = { numbersOneToTen, numbersFromFiveToSeven };
        final long[][] singleRowArray = { numbersOneToTen };

        // Test counting even numbers
        // In combined array: 2, 4, 6, 8, 10 (from first array) + 6 (from second array) = 6 total
        assertEquals(6, SegmentUtils.countMatches(twoDimensionalArray, EVEN_NUMBER_MATCHER),
            "Should count 6 even numbers in the 2D array (including duplicate 6)");
        
        // In single array: 2, 4, 6, 8, 10 = 5 total
        assertEquals(5, SegmentUtils.countMatches(singleRowArray, EVEN_NUMBER_MATCHER),
            "Should count 5 even numbers in single row array");
        
        // Test 1D array directly
        assertEquals(5, SegmentUtils.countMatches(numbersOneToTen, EVEN_NUMBER_MATCHER),
            "Should count 5 even numbers in 1D array");

        // Test counting multiples of 5
        // In combined array: 5, 10 (from first array) + 5 (from second array) = 3 total
        assertEquals(3, SegmentUtils.countMatches(twoDimensionalArray, MULTIPLE_OF_FIVE_MATCHER),
            "Should count 3 multiples of 5 in the 2D array (including duplicate 5)");
        
        // In single array: 5, 10 = 2 total
        assertEquals(2, SegmentUtils.countMatches(singleRowArray, MULTIPLE_OF_FIVE_MATCHER),
            "Should count 2 multiples of 5 in single row array");
        
        // Test 1D array directly
        assertEquals(2, SegmentUtils.countMatches(numbersOneToTen, MULTIPLE_OF_FIVE_MATCHER),
            "Should count 2 multiples of 5 in 1D array");
    }
}