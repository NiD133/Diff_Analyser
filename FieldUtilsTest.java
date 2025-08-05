/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link FieldUtils}.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 */
@DisplayName("FieldUtils")
class TestFieldUtils {

    @Nested
    @DisplayName("safeAdd(int, int)")
    class SafeAddInt {

        @Test
        @DisplayName("should add positive and negative integers correctly")
        void shouldAddNormally() {
            assertEquals(0, FieldUtils.safeAdd(0, 0));
            assertEquals(5, FieldUtils.safeAdd(2, 3));
            assertEquals(-1, FieldUtils.safeAdd(2, -3));
            assertEquals(1, FieldUtils.safeAdd(-2, 3));
            assertEquals(-5, FieldUtils.safeAdd(-2, -3));
        }

        @Test
        @DisplayName("should handle additions with integer boundaries without overflow")
        void shouldHandleBoundaries() {
            assertEquals(Integer.MAX_VALUE - 1, FieldUtils.safeAdd(Integer.MAX_VALUE, -1));
            assertEquals(Integer.MIN_VALUE + 1, FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
            assertEquals(-1, FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MAX_VALUE));
            assertEquals(-1, FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MIN_VALUE));
        }

        @Test
        @DisplayName("should throw ArithmeticException on positive overflow")
        void shouldThrowOnPositiveOverflow() {
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 100));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MAX_VALUE));
        }

        @Test
        @DisplayName("should throw ArithmeticException on negative overflow")
        void shouldThrowOnNegativeOverflow() {
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, -1));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, -100));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MIN_VALUE));
        }
    }

    @Nested
    @DisplayName("safeAdd(long, long)")
    class SafeAddLong {

        @Test
        @DisplayName("should add positive and negative longs correctly")
        void shouldAddNormally() {
            assertEquals(0L, FieldUtils.safeAdd(0L, 0L));
            assertEquals(5L, FieldUtils.safeAdd(2L, 3L));
            assertEquals(-1L, FieldUtils.safeAdd(2L, -3L));
            assertEquals(1L, FieldUtils.safeAdd(-2L, 3L));
            assertEquals(-5L, FieldUtils.safeAdd(-2L, -3L));
        }

        @Test
        @DisplayName("should handle additions with long boundaries without overflow")
        void shouldHandleBoundaries() {
            assertEquals(Long.MAX_VALUE - 1, FieldUtils.safeAdd(Long.MAX_VALUE, -1L));
            assertEquals(Long.MIN_VALUE + 1, FieldUtils.safeAdd(Long.MIN_VALUE, 1L));
            assertEquals(-1L, FieldUtils.safeAdd(Long.MIN_VALUE, Long.MAX_VALUE));
            assertEquals(-1L, FieldUtils.safeAdd(Long.MAX_VALUE, Long.MIN_VALUE));
        }

        @Test
        @DisplayName("should throw ArithmeticException on positive overflow")
        void shouldThrowOnPositiveOverflow() {
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MAX_VALUE, 1L));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MAX_VALUE, Long.MAX_VALUE));
        }

        @Test
        @DisplayName("should throw ArithmeticException on negative overflow")
        void shouldThrowOnNegativeOverflow() {
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MIN_VALUE, -1L));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(Long.MIN_VALUE, Long.MIN_VALUE));
        }
    }

    @Nested
    @DisplayName("safeSubtract(long, long)")
    class SafeSubtractLong {

        @Test
        @DisplayName("should subtract positive and negative longs correctly")
        void shouldSubtractNormally() {
            assertEquals(0L, FieldUtils.safeSubtract(0L, 0L));
            assertEquals(-1L, FieldUtils.safeSubtract(2L, 3L));
            assertEquals(5L, FieldUtils.safeSubtract(2L, -3L));
            assertEquals(-5L, FieldUtils.safeSubtract(-2L, 3L));
            assertEquals(1L, FieldUtils.safeSubtract(-2L, -3L));
        }

        @Test
        @DisplayName("should handle subtractions with long boundaries without overflow")
        void shouldHandleBoundaries() {
            assertEquals(Long.MAX_VALUE - 1, FieldUtils.safeSubtract(Long.MAX_VALUE, 1L));
            assertEquals(Long.MIN_VALUE + 1, FieldUtils.safeSubtract(Long.MIN_VALUE, -1L));
            assertEquals(0L, FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MIN_VALUE));
            assertEquals(0L, FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MAX_VALUE));
        }

        @Test
        @DisplayName("should throw ArithmeticException on overflow")
        void shouldThrowOnOverflow() {
            // Negative overflow
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, 1L));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MAX_VALUE));

            // Positive overflow
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MAX_VALUE, -1L));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MIN_VALUE));
        }
    }

    @Nested
    @DisplayName("safeMultiply(long, long)")
    class SafeMultiplyLongLong {

        @Test
        @DisplayName("should multiply positive and negative longs correctly")
        void shouldMultiplyNormally() {
            assertEquals(0L, FieldUtils.safeMultiply(0L, 0L));
            assertEquals(6L, FieldUtils.safeMultiply(2L, 3L));
            assertEquals(-6L, FieldUtils.safeMultiply(2L, -3L));
            assertEquals(-6L, FieldUtils.safeMultiply(-2L, 3L));
            assertEquals(6L, FieldUtils.safeMultiply(-2L, -3L));
        }

        @Test
        @DisplayName("should handle multiplications with 1 and -1 correctly")
        void shouldHandleMultiplicationByOne() {
            assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1L));
            assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1L));
            assertEquals(-Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1L));
        }

        @Test
        @DisplayName("should throw ArithmeticException on overflow")
        void shouldThrowOnOverflow() {
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1L));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(-1L, Long.MIN_VALUE));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 2L));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, Long.MAX_VALUE));
        }
    }

    @Nested
    @DisplayName("safeMultiply(long, int)")
    class SafeMultiplyLongInt {

        @Test
        @DisplayName("should multiply long by int correctly")
        void shouldMultiplyNormally() {
            assertEquals(0L, FieldUtils.safeMultiply(0L, 0));
            assertEquals(6L, FieldUtils.safeMultiply(2L, 3));
            assertEquals(-6L, FieldUtils.safeMultiply(2L, -3));
        }

        @Test
        @DisplayName("should handle multiplications with boundaries without overflow")
        void shouldHandleBoundaries() {
            assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
            assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1));
            assertEquals(-Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1));
            // Special case: -1L * Integer.MIN_VALUE fits in a long
            assertEquals(-(long) Integer.MIN_VALUE, FieldUtils.safeMultiply(-1L, Integer.MIN_VALUE));
        }

        @Test
        @DisplayName("should throw ArithmeticException on overflow")
        void shouldThrowOnOverflow() {
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, 2));
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(Long.MAX_VALUE, Integer.MIN_VALUE));
        }
    }

    @Nested
    @DisplayName("safeDivide(long, long)")
    class SafeDivideLongLong {

        @Test
        @DisplayName("should divide longs with truncation towards zero")
        void shouldDivideAndTruncate() {
            assertEquals(2L, FieldUtils.safeDivide(6L, 3L));
            assertEquals(1L, FieldUtils.safeDivide(5L, 3L), "5/3 should truncate to 1");
            assertEquals(-1L, FieldUtils.safeDivide(5L, -3L), "5/-3 should truncate to -1");
            assertEquals(-1L, FieldUtils.safeDivide(-5L, 3L), "-5/3 should truncate to -1");
            assertEquals(1L, FieldUtils.safeDivide(-5L, -3L), "-5/-3 should truncate to 1");
        }

        @Test
        @DisplayName("should handle divisions with long boundaries without overflow")
        void shouldHandleBoundaries() {
            assertEquals(Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L));
            assertEquals(Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L));
            assertEquals(-Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L));
        }

        @Test
        @DisplayName("should throw ArithmeticException when dividing Long.MIN_VALUE by -1")
        void shouldThrowOnMinLongDivisionByMinusOne() {
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeDivide(Long.MIN_VALUE, -1L));
        }

        @Test
        @DisplayName("should throw ArithmeticException when dividing by zero")
        void shouldThrowOnDivisionByZero() {
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeDivide(1L, 0L));
        }
    }

    @Nested
    @DisplayName("safeDivide(long, long, RoundingMode)")
    class SafeDivideWithRounding {

        @Test
        @DisplayName("should divide longs correctly for various rounding modes")
        void shouldDivideWithRounding() {
            assertEquals(3L, FieldUtils.safeDivide(15L, 5L, RoundingMode.UNNECESSARY));
            assertEquals(59L, FieldUtils.safeDivide(179L, 3L, RoundingMode.FLOOR));
            assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.CEILING));
            assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_UP));
            assertEquals(-60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_UP));
            assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_DOWN));
            assertEquals(-60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_DOWN));
        }

        @Test
        @DisplayName("should handle divisions with long boundaries without overflow")
        void shouldHandleBoundaries() {
            assertEquals(Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L, RoundingMode.UNNECESSARY));
            assertEquals(Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L, RoundingMode.UNNECESSARY));
            assertEquals(-Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L, RoundingMode.UNNECESSARY));
        }

        @Test
        @DisplayName("should throw ArithmeticException when dividing Long.MIN_VALUE by -1")
        void shouldThrowOnMinLongDivisionByMinusOne() {
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeDivide(Long.MIN_VALUE, -1L, RoundingMode.UNNECESSARY));
        }

        @Test
        @DisplayName("should throw ArithmeticException when dividing by zero")
        void shouldThrowOnDivisionByZero() {
            assertThrows(ArithmeticException.class, () -> FieldUtils.safeDivide(1L, 0L, RoundingMode.UNNECESSARY));
        }
    }
}