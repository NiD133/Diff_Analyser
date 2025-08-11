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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for SegmentUtils focused on:
 * - Counting JVM method arguments from method descriptors.
 * - Counting matches in arrays of flags using an IMatcher.
 *
 * Notes on JVM descriptors used in these tests:
 * - Primitives: Z, B, C, S, I, F each occupy one argument slot.
 * - Reference types: L...; occupy one argument slot.
 * - Arrays: [X still occupy one argument slot regardless of dimension.
 * - long (J) and double (D) are treated specially:
 *   - countArgs: width for long/double = 1 (pure argument count).
 *   - countInvokeInterfaceArgs: width for long/double = 2 (JVM slot width).
 */
@DisplayName("SegmentUtils tests")
class SegmentUtilsTest {

    /**
     * Simple matcher that matches numbers divisible by a given divisor.
     */
    private static final class DivisibleByMatcher implements IMatcher {

        private final int divisor;

        DivisibleByMatcher(final int divisor) {
            this.divisor = divisor;
        }

        @Override
        public boolean matches(final long value) {
            return value % divisor == 0;
        }
    }

    private static final IMatcher EVEN = new DivisibleByMatcher(2);
    private static final IMatcher MULTIPLE_OF_FIVE = new DivisibleByMatcher(5);

    private static final long[] ONE_TO_TEN = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    private static final long[] FIVE_TO_SEVEN = { 5, 6, 7 };

    // ----------------------------------------------------------------------
    // countArgs (width for long/double = 1)
    // ----------------------------------------------------------------------

    /**
     * Data for countArgs:
     * - Each long/double counts as 1.
     * - Arrays and reference types count as 1.
     */
    static Stream<Arguments> descriptorsForCountArgs() {
        return Stream.of(
            Arguments.of("()V", 0),                  // no args
            Arguments.of("(Z)V", 1),                 // boolean
            Arguments.of("(D)V", 1),                 // double (width=1 here)
            Arguments.of("([D)V", 1),                // array of double -> still 1
            Arguments.of("([[D)V", 1),               // 2D array of double -> still 1
            Arguments.of("(DD)V", 2),                // two doubles
            Arguments.of("(DDD)V", 3),               // three doubles
            Arguments.of("(Lblah/blah;D)V", 2),      // ref + double
            Arguments.of("(Lblah/blah;DLbLah;)V", 3) // ref + double + ref
        );
    }

    @ParameterizedTest(name = "countArgs: descriptor \"{0}\" -> {1} argument(s)")
    @MethodSource("descriptorsForCountArgs")
    @DisplayName("countArgs counts arguments with long/double width = 1")
    void countArgs_countsArguments(final String descriptor, final int expectedArgsCount) {
        assertEquals(expectedArgsCount, SegmentUtils.countArgs(descriptor));
    }

    // ----------------------------------------------------------------------
    // countInvokeInterfaceArgs (width for long/double = 2)
    // ----------------------------------------------------------------------

    /**
     * Data for countInvokeInterfaceArgs:
     * - Each long/double counts as 2 (JVM slot width).
     * - Arrays and reference types count as 1.
     */
    static Stream<Arguments> descriptorsForInvokeInterfaceArgs() {
        return Stream.of(
            Arguments.of("(Z)V", 1),                       // boolean
            Arguments.of("(D)V", 2),                       // double -> 2 slots
            Arguments.of("(J)V", 2),                       // long -> 2 slots
            Arguments.of("([D)V", 1),                      // array still 1 slot
            Arguments.of("([[D)V", 1),                     // still 1 slot
            Arguments.of("(DD)V", 4),                      // two doubles -> 4 slots
            Arguments.of("(Lblah/blah;D)V", 3),            // ref (1) + double (2)
            Arguments.of("(Lblah/blah;DLbLah;)V", 4),      // ref (1) + double (2) + ref (1)
            Arguments.of("([Lblah/blah;DLbLah;)V", 4)      // array of ref (1) + double (2) + ref (1)
        );
    }

    @ParameterizedTest(name = "countInvokeInterfaceArgs: descriptor \"{0}\" -> {1} slot(s)")
    @MethodSource("descriptorsForInvokeInterfaceArgs")
    @DisplayName("countInvokeInterfaceArgs counts argument slots with long/double width = 2")
    void countInvokeInterfaceArgs_countsArgumentSlots(final String descriptor, final int expectedSlotCount) {
        assertEquals(expectedSlotCount, SegmentUtils.countInvokeInterfaceArgs(descriptor));
    }

    // ----------------------------------------------------------------------
    // countMatches
    // ----------------------------------------------------------------------

    @Test
    @DisplayName("countMatches on single array")
    void countMatches_singleArray() {
        // Even numbers between 1 and 10: 2, 4, 6, 8, 10 -> 5
        assertEquals(5, SegmentUtils.countMatches(ONE_TO_TEN, EVEN), "even numbers in 1..10");

        // Multiples of five between 1 and 10: 5, 10 -> 2
        assertEquals(2, SegmentUtils.countMatches(ONE_TO_TEN, MULTIPLE_OF_FIVE), "multiples of five in 1..10");
    }

    @Test
    @DisplayName("countMatches on two-dimensional arrays")
    void countMatches_twoDimensionalArrays() {
        final long[][] twoRows = { ONE_TO_TEN, FIVE_TO_SEVEN };
        final long[][] oneRow = { ONE_TO_TEN };

        // Evens across both rows: 2,4,6,8,10 and 6 -> 6
        assertEquals(6, SegmentUtils.countMatches(twoRows, EVEN), "even numbers across two rows");

        // Evens across a single row: same as single-array case -> 5
        assertEquals(5, SegmentUtils.countMatches(oneRow, EVEN), "even numbers across one row");

        // Multiples of five across both rows: 5,10 and 5 -> 3
        assertEquals(3, SegmentUtils.countMatches(twoRows, MULTIPLE_OF_FIVE), "multiples of five across two rows");

        // Multiples of five across a single row: same as single-array case -> 2
        assertEquals(2, SegmentUtils.countMatches(oneRow, MULTIPLE_OF_FIVE), "multiples of five across one row");
    }
}