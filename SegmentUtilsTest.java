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

class SegmentUtilsTest {

    // Matcher class to check if a number is divisible by a given divisor
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

    // Matchers for even numbers and multiples of five
    public static final IMatcher EVEN_MATCHER = new DivisibilityMatcher(2);
    public static final IMatcher FIVE_MATCHER = new DivisibilityMatcher(5);

    // Provides test data for counting arguments in method descriptors
    static Stream<Arguments> provideCountArgsTestData() {
        return Stream.of(
            Arguments.of("()V", 0),
            Arguments.of("(D)V", 1),
            Arguments.of("([D)V", 1),
            Arguments.of("([[D)V", 1),
            Arguments.of("(DD)V", 2),
            Arguments.of("(DDD)V", 3),
            Arguments.of("(Lblah/blah;D)V", 2),
            Arguments.of("(Lblah/blah;DLbLah;)V", 3)
        );
    }

    // Provides test data for counting arguments in invoke interface method descriptors
    static Stream<Arguments> provideCountInvokeInterfaceArgsTestData() {
        return Stream.of(
            Arguments.of("(Z)V", 1),
            Arguments.of("(D)V", 2),
            Arguments.of("(J)V", 2),
            Arguments.of("([D)V", 1),
            Arguments.of("([[D)V", 1),
            Arguments.of("(DD)V", 4),
            Arguments.of("(Lblah/blah;D)V", 3),
            Arguments.of("(Lblah/blah;DLbLah;)V", 4),
            Arguments.of("([Lblah/blah;DLbLah;)V", 4)
        );
    }

    // Test for counting arguments in method descriptors
    @ParameterizedTest
    @MethodSource("provideCountArgsTestData")
    void testCountArgs(final String descriptor, final int expectedArgsCount) {
        assertEquals(expectedArgsCount, SegmentUtils.countArgs(descriptor));
    }

    // Test for counting arguments in invoke interface method descriptors
    @ParameterizedTest
    @MethodSource("provideCountInvokeInterfaceArgsTestData")
    void testCountInvokeInterfaceArgs(final String descriptor, final int expectedCountInvokeInterfaceArgs) {
        assertEquals(expectedCountInvokeInterfaceArgs, SegmentUtils.countInvokeInterfaceArgs(descriptor));
    }

    // Test for counting matches using matchers
    @Test
    void testMatches() {
        final long[] numbersOneToTen = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        
        // Test even numbers matcher
        assertEquals(6, SegmentUtils.countMatches(new long[][] { numbersOneToTen, new long[] { 5, 6, 7 } }, EVEN_MATCHER));
        assertEquals(5, SegmentUtils.countMatches(new long[][] { numbersOneToTen }, EVEN_MATCHER));
        assertEquals(5, SegmentUtils.countMatches(numbersOneToTen, EVEN_MATCHER));
        
        // Test multiples of five matcher
        assertEquals(3, SegmentUtils.countMatches(new long[][] { numbersOneToTen, new long[] { 5, 6, 7 } }, FIVE_MATCHER));
        assertEquals(2, SegmentUtils.countMatches(new long[][] { numbersOneToTen }, FIVE_MATCHER));
        assertEquals(2, SegmentUtils.countMatches(numbersOneToTen, FIVE_MATCHER));
    }
}