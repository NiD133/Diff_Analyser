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
 *
 * This suite intentionally keeps the original behavior and acceptance criteria,
 * while improving readability and reducing duplication.
 */
class RandomUtilsTest extends AbstractLangTest {

    // Tolerance for comparing doubles and floats.
    private static final double DELTA = 1e-5;

    // Common sample ranges used across tests.
    private static final int INT_START = 33;
    private static final int INT_END = 42;
    private static final long LONG_START = 33L;
    private static final long LONG_END = 42L;
    private static final float FLOAT_START = 33f;
    private static final float FLOAT_END = 42f;
    private static final double DOUBLE_START = 33d;
    private static final double DOUBLE_END = 42d;

    // Providers for instance API tests (secure, strong secure, insecure).
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    // ---------------------------------------------------------------------
    // Helper assertions to make intent explicit and messages clear.
    // ---------------------------------------------------------------------

    private static void assertInRangeClosedOpen(String what, double value, double startInclusive, double endExclusive) {
        assertTrue(value >= startInclusive, () -> what + " expected >= " + startInclusive + " but was " + value);
        assertTrue(value < endExclusive, () -> what + " expected < " + endExclusive + " but was " + value);
    }

    private static void assertInRangeClosedOpen(String what, float value, float startInclusive, float endExclusive) {
        assertTrue(value >= startInclusive, () -> what + " expected >= " + startInclusive + " but was " + value);
        assertTrue(value < endExclusive, () -> what + " expected < " + endExclusive + " but was " + value);
    }

    private static void assertInRangeClosedOpen(String what, int value, int startInclusive, int endExclusive) {
        assertTrue(value >= startInclusive, () -> what + " expected >= " + startInclusive + " but was " + value);
        assertTrue(value < endExclusive, () -> what + " expected < " + endExclusive + " but was " + value);
    }

    private static void assertInRangeClosedOpen(String what, long value, long startInclusive, long endExclusive) {
        assertTrue(value >= startInclusive, () -> what + " expected >= " + startInclusive + " but was " + value);
        assertTrue(value < endExclusive, () -> what + " expected < " + endExclusive + " but was " + value);
    }

    private static void assertInRangeClosedClosed(String what, double value, double startInclusive, double endInclusive) {
        assertTrue(value >= startInclusive, () -> what + " expected >= " + startInclusive + " but was " + value);
        assertTrue(value <= endInclusive, () -> what + " expected <= " + endInclusive + " but was " + value);
    }

    private static void assertInRangeClosedClosed(String what, float value, float startInclusive, float endInclusive) {
        assertTrue(value >= startInclusive, () -> what + " expected >= " + startInclusive + " but was " + value);
        assertTrue(value <= endInclusive, () -> what + " expected <= " + endInclusive + " but was " + value);
    }

    // ---------------------------------------------------------------------
    // Basic object/constructor tests
    // ---------------------------------------------------------------------

    @Test
    void testConstructorExists() {
        // Sanity: public constructor exists (deprecated) and creates object.
        assertNotNull(new RandomUtils());
    }

    // ---------------------------------------------------------------------
    // Static API tests
    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("Static API")
    class StaticApi {

        @Test
        void booleanValue() {
            final boolean result = RandomUtils.nextBoolean();
            // Sanity check: method returns without error and yields a boolean.
            assertTrue(result || !result, "Expected a boolean value");
        }

        @Test
        void bytesOfLength() {
            final byte[] bytes = RandomUtils.nextBytes(20);
            assertEquals(20, bytes.length, "Unexpected byte array length");
        }

        @Test
        void bytesZeroLength() {
            assertArrayEquals(new byte[0], RandomUtils.nextBytes(0));
        }

        @Test
        void bytesNegativeLengthThrows() {
            assertIllegalArgumentException(() -> RandomUtils.nextBytes(-1));
        }

        @Test
        void doubleInRange() {
            final double v = RandomUtils.nextDouble(DOUBLE_START, DOUBLE_END);
            assertInRangeClosedOpen("double", v, DOUBLE_START, DOUBLE_END);
        }

        @Test
        void doubleMinimalRangeReturnsBound() {
            assertEquals(42.1, RandomUtils.nextDouble(42.1, 42.1), DELTA);
        }

        @Test
        void doubleLowerGreaterUpperThrows() {
            assertIllegalArgumentException(() -> RandomUtils.nextDouble(2, 1));
        }

        @Test
        void doubleNegativeStartThrows() {
            assertIllegalArgumentException(() -> RandomUtils.nextDouble(-1, 1));
        }

        @Test
        void doubleRandomResultInDefaultRange() {
            final double v = RandomUtils.nextDouble();
            assertTrue(v >= 0d, "Expected >= 0");
            assertTrue(v < Double.MAX_VALUE, "Expected < Double.MAX_VALUE");
        }

        @Test
        void doubleExtremeRange() {
            final double v = RandomUtils.nextDouble(0, Double.MAX_VALUE);
            // Note: The original tests allow <= MAX_VALUE and leave a TODO.
            assertInRangeClosedClosed("double", v, 0, Double.MAX_VALUE); // TODO: should the end be exclusive?
        }

        @Test
        void floatInRange() {
            final float v = RandomUtils.nextFloat(FLOAT_START, FLOAT_END);
            assertInRangeClosedOpen("float", v, FLOAT_START, FLOAT_END);
        }

        @Test
        void floatMinimalRangeReturnsBound() {
            assertEquals(42.1f, RandomUtils.nextFloat(42.1f, 42.1f), DELTA);
        }

        @Test
        void floatLowerGreaterUpperThrows() {
            assertIllegalArgumentException(() -> RandomUtils.nextFloat(2, 1));
        }

        @Test
        void floatNegativeStartThrows() {
            assertIllegalArgumentException(() -> RandomUtils.nextFloat(-1, 1));
        }

        @Test
        void floatRandomResultInDefaultRange() {
            final float v = RandomUtils.nextFloat();
            assertTrue(v >= 0f, "Expected >= 0");
            assertTrue(v < Float.MAX_VALUE, "Expected < Float.MAX_VALUE");
        }

        @Test
        void floatExtremeRange() {
            final float v = RandomUtils.nextFloat(0, Float.MAX_VALUE);
            // Note: The original tests allow <= MAX_VALUE and leave a TODO.
            assertInRangeClosedClosed("float", v, 0f, Float.MAX_VALUE); // TODO: should the end be exclusive?
        }

        @Test
        void intInRange() {
            final int v = RandomUtils.nextInt(INT_START, INT_END);
            assertInRangeClosedOpen("int", v, INT_START, INT_END);
        }

        @Test
        void intMinimalRangeReturnsBound() {
            assertEquals(42, RandomUtils.nextInt(42, 42));
        }

        @Test
        void intLowerGreaterUpperThrows() {
            assertIllegalArgumentException(() -> RandomUtils.nextInt(2, 1));
        }

        @Test
        void intNegativeStartThrows() {
            assertIllegalArgumentException(() -> RandomUtils.nextInt(-1, 1));
        }

        @Test
        void intRandomResultInDefaultRange() {
            final int v = RandomUtils.nextInt();
            // Note: original test expects > 0 (not >= 0), keep as-is.
            assertTrue(v > 0, "Expected > 0");
            assertTrue(v < Integer.MAX_VALUE, "Expected < Integer.MAX_VALUE");
        }

        @Test
        void intExtremeRange() {
            final int v = RandomUtils.nextInt(0, Integer.MAX_VALUE);
            assertTrue(v >= 0, "Expected >= 0");
            assertTrue(v < Integer.MAX_VALUE, "Expected < Integer.MAX_VALUE");
        }

        @Test
        void longInRange() {
            final long v = RandomUtils.nextLong(LONG_START, LONG_END);
            assertInRangeClosedOpen("long", v, LONG_START, LONG_END);
        }

        @Test
        void longMinimalRangeReturnsBound() {
            assertEquals(42L, RandomUtils.nextLong(42L, 42L));
        }

        @Test
        void longLowerGreaterUpperThrows() {
            assertIllegalArgumentException(() -> RandomUtils.nextLong(2, 1));
        }

        @Test
        void longNegativeStartThrows() {
            assertIllegalArgumentException(() -> RandomUtils.nextLong(-1, 1));
        }

        @Test
        void longRandomResultInDefaultRange() {
            final long v = RandomUtils.nextLong();
            assertTrue(v >= 0L, "Expected >= 0");
            assertTrue(v < Long.MAX_VALUE, "Expected < Long.MAX_VALUE");
        }

        @Test
        void longExtremeRange() {
            final long v = RandomUtils.nextLong(0, Long.MAX_VALUE);
            assertTrue(v >= 0L, "Expected >= 0");
            assertTrue(v < Long.MAX_VALUE, "Expected < Long.MAX_VALUE");
        }

        /**
         * See LANG-1592. Ensure the implementation for long range does not return endExclusive.
         */
        @Test
        void longLargeValueRangeDoesNotIncludeEndExclusive() {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            final int n = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < n; i++) {
                assertNotEquals(endExclusive, RandomUtils.nextLong(startInclusive, endExclusive),
                        "Should never return endExclusive");
            }
        }
    }

    // ---------------------------------------------------------------------
    // Instance API tests (secure, secureStrong, insecure)
    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("Instance API (secure, secureStrong, insecure)")
    class InstanceApi {

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void booleanValue(final RandomUtils ru) {
            final boolean result = ru.randomBoolean();
            assertTrue(result || !result, "Expected a boolean value");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void bytesOfLength(final RandomUtils ru) {
            final byte[] bytes = ru.randomBytes(20);
            assertEquals(20, bytes.length, "Unexpected byte array length");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void bytesZeroLength(final RandomUtils ru) {
            assertArrayEquals(new byte[0], ru.randomBytes(0));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void bytesNegativeLengthThrows(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomBytes(-1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void doubleInRange(final RandomUtils ru) {
            final double v = ru.randomDouble(DOUBLE_START, DOUBLE_END);
            assertInRangeClosedOpen("double", v, DOUBLE_START, DOUBLE_END);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void doubleMinimalRangeReturnsBound(final RandomUtils ru) {
            assertEquals(42.1, ru.randomDouble(42.1, 42.1), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void doubleLowerGreaterUpperThrows(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void doubleNegativeStartThrows(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void doubleRandomResultInDefaultRange(final RandomUtils ru) {
            final double v = ru.randomDouble();
            assertTrue(v >= 0d, "Expected >= 0");
            assertTrue(v < Double.MAX_VALUE, "Expected < Double.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void doubleExtremeRange(final RandomUtils ru) {
            final double v = ru.randomDouble(0, Double.MAX_VALUE);
            // Note: The original tests allow <= MAX_VALUE and leave a TODO.
            assertInRangeClosedClosed("double", v, 0, Double.MAX_VALUE); // TODO: should the end be exclusive?
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void floatInRange(final RandomUtils ru) {
            final float v = ru.randomFloat(FLOAT_START, FLOAT_END);
            assertInRangeClosedOpen("float", v, FLOAT_START, FLOAT_END);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void floatMinimalRangeReturnsBound(final RandomUtils ru) {
            assertEquals(42.1f, ru.randomFloat(42.1f, 42.1f), DELTA);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void floatLowerGreaterUpperThrows(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void floatNegativeStartThrows(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void floatRandomResultInDefaultRange(final RandomUtils ru) {
            final float v = ru.randomFloat();
            assertTrue(v >= 0f, "Expected >= 0");
            assertTrue(v < Float.MAX_VALUE, "Expected < Float.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void floatExtremeRange(final RandomUtils ru) {
            final float v = ru.randomFloat(0, Float.MAX_VALUE);
            // Note: The original tests allow <= MAX_VALUE and leave a TODO.
            assertInRangeClosedClosed("float", v, 0f, Float.MAX_VALUE); // TODO: should the end be exclusive?
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void intInRange(final RandomUtils ru) {
            final int v = ru.randomInt(INT_START, INT_END);
            assertInRangeClosedOpen("int", v, INT_START, INT_END);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void intMinimalRangeReturnsBound(final RandomUtils ru) {
            assertEquals(42, ru.randomInt(42, 42));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void intLowerGreaterUpperThrows(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void intNegativeStartThrows(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void intRandomResultInDefaultRange(final RandomUtils ru) {
            final int v = ru.randomInt();
            // Note: original test expects > 0 (not >= 0), keep as-is.
            assertTrue(v > 0, "Expected > 0");
            assertTrue(v < Integer.MAX_VALUE, "Expected < Integer.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void intExtremeRange(final RandomUtils ru) {
            final int v = ru.randomInt(0, Integer.MAX_VALUE);
            assertTrue(v >= 0, "Expected >= 0");
            assertTrue(v < Integer.MAX_VALUE, "Expected < Integer.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void longInRange(final RandomUtils ru) {
            final long v = ru.randomLong(LONG_START, LONG_END);
            assertInRangeClosedOpen("long", v, LONG_START, LONG_END);
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void longMinimalRangeReturnsBound(final RandomUtils ru) {
            assertEquals(42L, ru.randomLong(42L, 42L));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void longLowerGreaterUpperThrows(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(2, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void longNegativeStartThrows(final RandomUtils ru) {
            assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void longRandomResultInDefaultRange(final RandomUtils ru) {
            final long v = ru.randomLong();
            assertTrue(v >= 0L, "Expected >= 0");
            assertTrue(v < Long.MAX_VALUE, "Expected < Long.MAX_VALUE");
        }

        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void longExtremeRange(final RandomUtils ru) {
            final long v = ru.randomLong(0, Long.MAX_VALUE);
            assertTrue(v >= 0L, "Expected >= 0");
            assertTrue(v < Long.MAX_VALUE, "Expected < Long.MAX_VALUE");
        }

        /**
         * See LANG-1592. Ensure the implementation for long range does not return endExclusive.
         */
        @ParameterizedTest
        @MethodSource("org.apache.commons.lang3.RandomUtilsTest#randomProvider")
        void longLargeValueRangeDoesNotIncludeEndExclusive(final RandomUtils ru) {
            final long startInclusive = 12900000000001L;
            final long endExclusive = 12900000000016L;
            final int n = (int) (endExclusive - startInclusive) * 1000;
            for (int i = 0; i < n; i++) {
                assertNotEquals(endExclusive, ru.randomLong(startInclusive, endExclusive),
                        "Should never return endExclusive");
            }
        }
    }
}