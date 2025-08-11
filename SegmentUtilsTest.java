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

    /**
     * Matcher implementation that checks if a value is divisible by a given number.
     */
    private static final class ModuloMatcher implements IMatcher {

        private final int divisor;

        ModuloMatcher(final int divisor) {
            this.divisor = divisor;
        }

        @Override
        public boolean matches(final long value) {
            return value % divisor == 0;
        }
    }

    private static final IMatcher EVEN_MATCHER = new ModuloMatcher(2);
    private static final IMatcher FIVE_MATCHER = new ModuloMatcher(5);

    static Stream<Arguments> countArgs() {
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

    static Stream<Arguments> countInvokeInterfaceArgs() {
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

    /**
     * Tests countArgs with various method descriptors.
     * 
     * @param descriptor method descriptor string
     * @param expectedArgsCount expected number of arguments
     */
    @ParameterizedTest
    @MethodSource("countArgs")
    void testCountArgs(final String descriptor, final int expectedArgsCount) {
        assertEquals(expectedArgsCount, SegmentUtils.countArgs(descriptor));
    }

    /**
     * Tests countInvokeInterfaceArgs with various method descriptors.
     * 
     * @param descriptor method descriptor string
     * @param expectedCount expected argument count
     */
    @ParameterizedTest
    @MethodSource("countInvokeInterfaceArgs")
    void testCountInvokeInterfaceArgs(final String descriptor, final int expectedCount) {
        assertEquals(expectedCount, SegmentUtils.countInvokeInterfaceArgs(descriptor));
    }

    /**
     * Tests countMatches with a single array using the even matcher.
     */
    @Test
    void testCountMatches_SingleArray_WithEvenMatcher() {
        final long[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assertEquals(5, SegmentUtils.countMatches(numbers, EVEN_MATCHER));
    }

    /**
     * Tests countMatches with a single array using the five matcher.
     */
    @Test
    void testCountMatches_SingleArray_WithFiveMatcher() {
        final long[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assertEquals(2, SegmentUtils.countMatches(numbers, FIVE_MATCHER));
    }

    /**
     * Tests countMatches with multiple arrays using the even matcher.
     */
    @Test
    void testCountMatches_MultipleArrays_WithEvenMatcher() {
        final long[] firstArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final long[] secondArray = {5, 6, 7};
        final long[][] arrays = {firstArray, secondArray};
        
        assertEquals(6, SegmentUtils.countMatches(arrays, EVEN_MATCHER));
    }

    /**
     * Tests countMatches with multiple arrays using the five matcher.
     */
    @Test
    void testCountMatches_MultipleArrays_WithFiveMatcher() {
        final long[] firstArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final long[] secondArray = {5, 6, 7};
        final long[][] arrays = {firstArray, secondArray};
        
        assertEquals(3, SegmentUtils.countMatches(arrays, FIVE_MATCHER));
    }
}