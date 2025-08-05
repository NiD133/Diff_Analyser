/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link RandomUtils}.
 */
class RandomUtilsTest extends AbstractLangTest {

    /**
     * A delta for comparing double and float values.
     */
    private static final double DELTA = 1e-5;

    /**
     * Provides instances of different RandomUtils implementations.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    @Test
    @DisplayName("The deprecated constructor should not throw an exception for backward compatibility")
    void constructor_shouldNotThrow() {
        assertNotNull(new RandomUtils());
    }

    @Nested
    @DisplayName("Methods: nextBoolean() / randomBoolean()")
    class NextBooleanTests {
        @Test
        @DisplayName("Static nextBoolean() should execute without failing")
        void staticNextBoolean_shouldExecuteWithoutFailing() {
            final boolean result = RandomUtils.nextBoolean();
            // A smoke test to ensure the method returns a valid boolean.
            assertTrue(result || !result);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomBoolean() should execute without failing")
        void instanceRandomBoolean_shouldExecuteWithoutFailing(final RandomUtils ru) {
            final boolean result = ru.randomBoolean();
            // A smoke test to ensure the method returns a valid boolean.
            assertTrue(result || !result);
        }
    }

    @Nested
    @DisplayName("Methods: nextBytes() / randomBytes()")
    class NextBytesTests {
        @Test
        @DisplayName("Static nextBytes() should return an array of the requested size")
        void staticNextBytes_shouldReturnArrayOfCorrectSize() {
            assertEquals(20, RandomUtils.nextBytes(20).length);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomBytes() should return an array of the requested size")
        void instanceRandomBytes_shouldReturnArrayOfCorrectSize(final RandomUtils ru) {
            assertEquals(20, ru.randomBytes(20).length);
        }

        @Test
        @DisplayName("Static nextBytes() should return an empty array for a count of zero")
        void staticNextBytes_shouldReturnEmptyArrayForZeroCount() {
            assertArrayEquals(new byte[0], RandomUtils.nextBytes(0));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomBytes() should return an empty array for a count of zero")
        void instanceRandomBytes_shouldReturnEmptyArrayForZeroCount(final RandomUtils ru) {
            assertArrayEquals(new byte[0], ru.randomBytes(0));
        }

        @Test
        @DisplayName("Static nextBytes() should throw IllegalArgumentException for a negative count")
        void staticNextBytes_shouldThrowExceptionForNegativeCount() {
            assertIllegalArgumentException(() -> RandomUtils.nextBytes(-1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomBytes() should throw IllegalArgumentException for a negative count")
        void instanceRandomBytes_shouldThrowExceptionForNegativeCount(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomBytes(-1));
        }
    }

    @Nested
    @DisplayName("Methods: nextDouble() / randomDouble()")
    class NextDoubleTests {
        @Test
        @DisplayName("Static nextDouble() should generate value in default range [0, Double.MAX_VALUE)")
        void staticNextDouble_shouldGenerateValueInDefaultRange() {
            final double result = RandomUtils.nextDouble();
            assertTrue(result >= 0d && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomDouble() should generate value in default range [0, Double.MAX_VALUE)")
        void instanceRandomDouble_shouldGenerateValueInDefaultRange(final RandomUtils ru) {
            final double result = ru.randomDouble();
            assertTrue(result >= 0d && result < Double.MAX_VALUE);
        }

        @Test
        @DisplayName("Static nextDouble() should generate value in specified range")
        void staticNextDouble_shouldGenerateValueInSpecifiedRange() {
            final double result = RandomUtils.nextDouble(33d, 42d);
            assertTrue(result >= 33d && result < 42d);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomDouble() should generate value in specified range")
        void instanceRandomDouble_shouldGenerateValueInSpecifiedRange(final RandomUtils ru) {
            final double result = ru.randomDouble(33d, 42d);
            assertTrue(result >= 33d && result < 42d);
        }

        @Test
        @DisplayName("Static nextDouble() should handle the full positive range")
        void staticNextDouble_shouldHandleFullPositiveRange() {
            final double result = RandomUtils.nextDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0 && result < Double.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomDouble() should handle the full positive range")
        void instanceRandomDouble_shouldHandleFullPositiveRange(final RandomUtils ru) {
            final double result = ru.randomDouble(0, Double.MAX_VALUE);
            assertTrue(result >= 0 && result < Double.MAX_VALUE);
        }

        @Test
        @DisplayName("Static nextDouble() should return start value when range is zero")
        void staticNextDouble_shouldReturnStartWhenRangeIsZero() {
            assertEquals(42.1, RandomUtils.nextDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomDouble() should return start value when range is zero")
        void instanceRandomDouble_shouldReturnStartWhenRangeIsZero(final RandomUtils ru) {
            assertEquals(42.1, ru.randomDouble(42.1, 42.1), DELTA);
        }

        @Test
        @DisplayName("Static nextDouble() should throw IllegalArgumentException when start > end")
        void staticNextDouble_shouldThrowExceptionWhenStartIsGreaterThanEnd() {
            assertIllegalArgumentException(() -> RandomUtils.nextDouble(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomDouble() should throw IllegalArgumentException when start > end")
        void instanceRandomDouble_shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
        }

        @Test
        @DisplayName("Static nextDouble() should throw IllegalArgumentException for a negative start")
        void staticNextDouble_shouldThrowExceptionForNegativeStart() {
            assertIllegalArgumentException(() -> RandomUtils.nextDouble(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomDouble() should throw IllegalArgumentException for a negative start")
        void instanceRandomDouble_shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
        }
    }

    @Nested
    @DisplayName("Methods: nextFloat() / randomFloat()")
    class NextFloatTests {
        @Test
        @DisplayName("Static nextFloat() should generate value in default range [0, Float.MAX_VALUE)")
        void staticNextFloat_shouldGenerateValueInDefaultRange() {
            final float result = RandomUtils.nextFloat();
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomFloat() should generate value in default range [0, Float.MAX_VALUE)")
        void instanceRandomFloat_shouldGenerateValueInDefaultRange(final RandomUtils ru) {
            final float result = ru.randomFloat();
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @Test
        @DisplayName("Static nextFloat() should generate value in specified range")
        void staticNextFloat_shouldGenerateValueInSpecifiedRange() {
            final float result = RandomUtils.nextFloat(33f, 42f);
            assertTrue(result >= 33f && result < 42f);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomFloat() should generate value in specified range")
        void instanceRandomFloat_shouldGenerateValueInSpecifiedRange(final RandomUtils ru) {
            final float result = ru.randomFloat(33f, 42f);
            assertTrue(result >= 33f && result < 42f);
        }

        @Test
        @DisplayName("Static nextFloat() should handle the full positive range")
        void staticNextFloat_shouldHandleFullPositiveRange() {
            final float result = RandomUtils.nextFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomFloat() should handle the full positive range")
        void instanceRandomFloat_shouldHandleFullPositiveRange(final RandomUtils ru) {
            final float result = ru.randomFloat(0, Float.MAX_VALUE);
            assertTrue(result >= 0f && result < Float.MAX_VALUE);
        }

        @Test
        @DisplayName("Static nextFloat() should return start value when range is zero")
        void staticNextFloat_shouldReturnStartWhenRangeIsZero() {
            assertEquals(42.1f, RandomUtils.nextFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomFloat() should return start value when range is zero")
        void instanceRandomFloat_shouldReturnStartWhenRangeIsZero(final RandomUtils ru) {
            assertEquals(42.1f, ru.randomFloat(42.1f, 42.1f), DELTA);
        }

        @Test
        @DisplayName("Static nextFloat() should throw IllegalArgumentException when start > end")
        void staticNextFloat_shouldThrowExceptionWhenStartIsGreaterThanEnd() {
            assertIllegalArgumentException(() -> RandomUtils.nextFloat(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomFloat() should throw IllegalArgumentException when start > end")
        void instanceRandomFloat_shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
        }

        @Test
        @DisplayName("Static nextFloat() should throw IllegalArgumentException for a negative start")
        void staticNextFloat_shouldThrowExceptionForNegativeStart() {
            assertIllegalArgumentException(() -> RandomUtils.nextFloat(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomFloat() should throw IllegalArgumentException for a negative start")
        void instanceRandomFloat_shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
        }
    }

    @Nested
    @DisplayName("Methods: nextInt() / randomInt()")
    class NextIntTests {
        @Test
        @DisplayName("Static nextInt() should generate value in default range [0, Integer.MAX_VALUE)")
        void staticNextInt_shouldGenerateValueInDefaultRange() {
            final int result = RandomUtils.nextInt();
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomInt() should generate value in default range [0, Integer.MAX_VALUE)")
        void instanceRandomInt_shouldGenerateValueInDefaultRange(final RandomUtils ru) {
            final int result = ru.randomInt();
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @Test
        @DisplayName("Static nextInt() should generate value in specified range")
        void staticNextInt_shouldGenerateValueInSpecifiedRange() {
            final int result = RandomUtils.nextInt(33, 42);
            assertTrue(result >= 33 && result < 42);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomInt() should generate value in specified range")
        void instanceRandomInt_shouldGenerateValueInSpecifiedRange(final RandomUtils ru) {
            final int result = ru.randomInt(33, 42);
            assertTrue(result >= 33 && result < 42);
        }

        @Test
        @DisplayName("Static nextInt() should handle the full positive range")
        void staticNextInt_shouldHandleFullPositiveRange() {
            final int result = RandomUtils.nextInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomInt() should handle the full positive range")
        void instanceRandomInt_shouldHandleFullPositiveRange(final RandomUtils ru) {
            final int result = ru.randomInt(0, Integer.MAX_VALUE);
            assertTrue(result >= 0 && result < Integer.MAX_VALUE);
        }

        @Test
        @DisplayName("Static nextInt() should return start value when range is zero")
        void staticNextInt_shouldReturnStartWhenRangeIsZero() {
            assertEquals(42, RandomUtils.nextInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomInt() should return start value when range is zero")
        void instanceRandomInt_shouldReturnStartWhenRangeIsZero(final RandomUtils ru) {
            assertEquals(42, ru.randomInt(42, 42));
        }

        @Test
        @DisplayName("Static nextInt() should throw IllegalArgumentException when start > end")
        void staticNextInt_shouldThrowExceptionWhenStartIsGreaterThanEnd() {
            assertIllegalArgumentException(() -> RandomUtils.nextInt(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomInt() should throw IllegalArgumentException when start > end")
        void instanceRandomInt_shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(2, 1));
        }

        @Test
        @DisplayName("Static nextInt() should throw IllegalArgumentException for a negative start")
        void staticNextInt_shouldThrowExceptionForNegativeStart() {
            assertIllegalArgumentException(() -> RandomUtils.nextInt(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomInt() should throw IllegalArgumentException for a negative start")
        void instanceRandomInt_shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
        }
    }

    @Nested
    @DisplayName("Methods: nextLong() / randomLong()")
    class NextLongTests {
        @Test
        @DisplayName("Static nextLong() should generate value in default range [0, Long.MAX_VALUE)")
        void staticNextLong_shouldGenerateValueInDefaultRange() {
            final long result = RandomUtils.nextLong();
            assertTrue(result >= 0L && result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomLong() should generate value in default range [0, Long.MAX_VALUE)")
        void instanceRandomLong_shouldGenerateValueInDefaultRange(final RandomUtils ru) {
            final long result = ru.randomLong();
            assertTrue(result >= 0L && result < Long.MAX_VALUE);
        }

        @Test
        @DisplayName("Static nextLong() should generate value in specified range")
        void staticNextLong_shouldGenerateValueInSpecifiedRange() {
            final long result = RandomUtils.nextLong(33L, 42L);
            assertTrue(result >= 33L && result < 42L);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomLong() should generate value in specified range")
        void instanceRandomLong_shouldGenerateValueInSpecifiedRange(final RandomUtils ru) {
            final long result = ru.randomLong(33L, 42L);
            assertTrue(result >= 33L && result < 42L);
        }

        @Test
        @DisplayName("Static nextLong() should handle the full positive range")
        void staticNextLong_shouldHandleFullPositiveRange() {
            final long result = RandomUtils.nextLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0 && result < Long.MAX_VALUE);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomLong() should handle the full positive range")
        void instanceRandomLong_shouldHandleFullPositiveRange(final RandomUtils ru) {
            final long result = ru.randomLong(0, Long.MAX_VALUE);
            assertTrue(result >= 0 && result < Long.MAX_VALUE);
        }

        @Test
        @DisplayName("Static nextLong() should return start value when range is zero")
        void staticNextLong_shouldReturnStartWhenRangeIsZero() {
            assertEquals(42L, RandomUtils.nextLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomLong() should return start value when range is zero")
        void instanceRandomLong_shouldReturnStartWhenRangeIsZero(final RandomUtils ru) {
            assertEquals(42L, ru.randomLong(42L, 42L));
        }

        @Test
        @DisplayName("Static nextLong() should throw IllegalArgumentException when start > end")
        void staticNextLong_shouldThrowExceptionWhenStartIsGreaterThanEnd() {
            assertIllegalArgumentException(() -> RandomUtils.nextLong(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomLong() should throw IllegalArgumentException when start > end")
        void instanceRandomLong_shouldThrowExceptionWhenStartIsGreaterThanEnd(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(2, 1));
        }

        @Test
        @DisplayName("Static nextLong() should throw IllegalArgumentException for a negative start")
        void staticNextLong_shouldThrowExceptionForNegativeStart() {
            assertIllegalArgumentException(() -> RandomUtils.nextLong(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomLong() should throw IllegalArgumentException for a negative start")
        void instanceRandomLong_shouldThrowExceptionForNegativeStart(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
        }

        /**
         * Tests that {@code nextLong(start, end)} does not return {@code end}.
         * A previous implementation using {@code (long) nextDouble(start, end)}
         * could return {@code end} for large longs. See LANG-1592.
         */
        @Test
        @DisplayName("Static nextLong() should not generate the exclusive upper bound for large numbers (LANG-1592)")
        void staticNextLong_shouldNotReturnEndExclusiveForLargeValues() {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // Loop many times to increase the chance of hitting the bug if it exists.
            final int n = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < n; i++) {
                assertNotEquals(endExclusive, RandomUtils.nextLong(startInclusive, endExclusive));
            }
        }

        /**
         * Tests that {@code randomLong(start, end)} does not return {@code end}.
         * A previous implementation using {@code (long) randomDouble(start, end)}
         * could return {@code end} for large longs. See LANG-1592.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        @DisplayName("Instance randomLong() should not generate the exclusive upper bound for large numbers (LANG-1592)")
        void instanceRandomLong_shouldNotReturnEndExclusiveForLargeValues(final RandomUtils ru) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            // Loop many times to increase the chance of hitting the bug if it exists.
            final int n = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < n; i++) {
                assertNotEquals(endExclusive, ru.randomLong(startInclusive, endExclusive));
            }
        }
    }
}