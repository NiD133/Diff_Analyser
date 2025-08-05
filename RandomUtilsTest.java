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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link RandomUtils}
 */
class RandomUtilsTest extends AbstractLangTest {

    /**
     * Tolerance for floating-point comparisons
     */
    private static final double DELTA = 1e-5;

    // =========================================================
    // Test Data Providers
    // =========================================================

    static Stream<Arguments> randomProvider() {
        return Stream.of(
            Arguments.of("secure", RandomUtils.secure()),
            Arguments.of("secureStrong", RandomUtils.secureStrong()),
            Arguments.of("insecure", RandomUtils.insecure())
        );
    }

    // =========================================================
    // Tests for Static Methods (Deprecated)
    // =========================================================

    @Test
    void testStaticConstructor() {
        assertNotNull(new RandomUtils());
    }

    @Test
    void testStaticNextBoolean() {
        final boolean result = RandomUtils.nextBoolean();
        assertTrue(result || !result);
    }

    @Test
    void testStaticNextBytes_WithPositiveLength() {
        final byte[] result = RandomUtils.nextBytes(20);
        assertEquals(20, result.length);
    }

    @Test
    void testStaticNextBytes_WithZeroLength() {
        assertArrayEquals(new byte[0], RandomUtils.nextBytes(0));
    }

    @Test
    void testStaticNextBytes_WithNegativeLength_ThrowsException() {
        assertIllegalArgumentException(() -> RandomUtils.nextBytes(-1));
    }

    @Test
    void testStaticNextDouble_WithNormalRange() {
        final double result = RandomUtils.nextDouble(33d, 42d);
        assertTrue(result >= 33d);
        assertTrue(result < 42d);
    }

    @Test
    void testStaticNextDouble_WithMinimalRange() {
        assertEquals(42.1, RandomUtils.nextDouble(42.1, 42.1), DELTA);
    }

    @Test
    void testStaticNextDouble_NoArgs() {
        final double result = RandomUtils.nextDouble();
        assertTrue(result >= 0d);
        assertTrue(result < Double.MAX_VALUE);
    }

    @Test
    void testStaticNextDouble_ExtremeRange() {
        final double result = RandomUtils.nextDouble(0, Double.MAX_VALUE);
        assertTrue(result >= 0d);
        assertTrue(result < Double.MAX_VALUE);
    }

    @Test
    void testStaticNextDouble_WithNegativeStart_ThrowsException() {
        assertIllegalArgumentException(() -> RandomUtils.nextDouble(-1, 1));
    }

    @Test
    void testStaticNextDouble_WithLowerGreaterThanUpper_ThrowsException() {
        assertIllegalArgumentException(() -> RandomUtils.nextDouble(2, 1));
    }

    @Test
    void testStaticNextFloat_WithNormalRange() {
        final float result = RandomUtils.nextFloat(33f, 42f);
        assertTrue(result >= 33f);
        assertTrue(result < 42f);
    }

    @Test
    void testStaticNextFloat_WithMinimalRange() {
        assertEquals(42.1f, RandomUtils.nextFloat(42.1f, 42.1f), DELTA);
    }

    @Test
    void testStaticNextFloat_NoArgs() {
        final float result = RandomUtils.nextFloat();
        assertTrue(result >= 0f);
        assertTrue(result < Float.MAX_VALUE);
    }

    @Test
    void testStaticNextFloat_ExtremeRange() {
        final float result = RandomUtils.nextFloat(0, Float.MAX_VALUE);
        assertTrue(result >= 0f);
        assertTrue(result < Float.MAX_VALUE);
    }

    @Test
    void testStaticNextFloat_WithNegativeStart_ThrowsException() {
        assertIllegalArgumentException(() -> RandomUtils.nextFloat(-1, 1));
    }

    @Test
    void testStaticNextFloat_WithLowerGreaterThanUpper_ThrowsException() {
        assertIllegalArgumentException(() -> RandomUtils.nextFloat(2, 1));
    }

    @Test
    void testStaticNextInt_WithNormalRange() {
        final int result = RandomUtils.nextInt(33, 42);
        assertTrue(result >= 33);
        assertTrue(result < 42);
    }

    @Test
    void testStaticNextInt_WithMinimalRange() {
        assertEquals(42, RandomUtils.nextInt(42, 42));
    }

    @Test
    void testStaticNextInt_NoArgs() {
        final int result = RandomUtils.nextInt();
        assertTrue(result >= 0);
        assertTrue(result < Integer.MAX_VALUE);
    }

    @Test
    void testStaticNextInt_ExtremeRange() {
        final int result = RandomUtils.nextInt(0, Integer.MAX_VALUE);
        assertTrue(result >= 0);
        assertTrue(result < Integer.MAX_VALUE);
    }

    @Test
    void testStaticNextInt_WithNegativeStart_ThrowsException() {
        assertIllegalArgumentException(() -> RandomUtils.nextInt(-1, 1));
    }

    @Test
    void testStaticNextInt_WithLowerGreaterThanUpper_ThrowsException() {
        assertIllegalArgumentException(() -> RandomUtils.nextInt(2, 1));
    }

    @Test
    void testStaticNextLong_WithNormalRange() {
        final long result = RandomUtils.nextLong(33L, 42L);
        assertTrue(result >= 33L);
        assertTrue(result < 42L);
    }

    @Test
    void testStaticNextLong_WithMinimalRange() {
        assertEquals(42L, RandomUtils.nextLong(42L, 42L));
    }

    @Test
    void testStaticNextLong_NoArgs() {
        final long result = RandomUtils.nextLong();
        assertTrue(result >= 0L);
        assertTrue(result < Long.MAX_VALUE);
    }

    @Test
    void testStaticNextLong_ExtremeRange() {
        final long result = RandomUtils.nextLong(0, Long.MAX_VALUE);
        assertTrue(result >= 0);
        assertTrue(result < Long.MAX_VALUE);
    }

    @Test
    void testStaticNextLong_WithNegativeStart_ThrowsException() {
        assertIllegalArgumentException(() -> RandomUtils.nextLong(-1, 1));
    }

    @Test
    void testStaticNextLong_WithLowerGreaterThanUpper_ThrowsException() {
        assertIllegalArgumentException(() -> RandomUtils.nextLong(2, 1));
    }

    /**
     * Tests regression for LANG-1592 where large long ranges could
     * produce values equal to the exclusive upper bound
     */
    @Test
    void testStaticNextLong_LargeValueRangeRegression() {
        final long startInclusive = 12900000000001L;
        final long endExclusive = 12900000000016L;
        final int n = (int) (endExclusive - startInclusive) * 1000;
        for (int i = 0; i < n; i++) {
            assertNotEquals(endExclusive, RandomUtils.nextLong(startInclusive, endExclusive));
        }
    }

    // =========================================================
    // Parameterized Tests for Instance Methods
    // =========================================================

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomBoolean(String providerName, RandomUtils ru) {
        final boolean result = ru.randomBoolean();
        assertTrue(result || !result);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomBytes_WithPositiveLength(String providerName, RandomUtils ru) {
        final byte[] result = ru.randomBytes(20);
        assertEquals(20, result.length);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomBytes_WithZeroLength(String providerName, RandomUtils ru) {
        assertArrayEquals(new byte[0], ru.randomBytes(0));
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomBytes_WithNegativeLength_ThrowsException(String providerName, RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomBytes(-1));
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomDouble_WithNormalRange(String providerName, RandomUtils ru) {
        final double result = ru.randomDouble(33d, 42d);
        assertTrue(result >= 33d);
        assertTrue(result < 42d);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomDouble_WithMinimalRange(String providerName, RandomUtils ru) {
        assertEquals(42.1, ru.randomDouble(42.1, 42.1), DELTA);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomDouble_NoArgs(String providerName, RandomUtils ru) {
        final double result = ru.randomDouble();
        assertTrue(result >= 0d);
        assertTrue(result < Double.MAX_VALUE);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomDouble_ExtremeRange(String providerName, RandomUtils ru) {
        final double result = ru.randomDouble(0, Double.MAX_VALUE);
        assertTrue(result >= 0d);
        assertTrue(result < Double.MAX_VALUE);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomDouble_WithNegativeStart_ThrowsException(String providerName, RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomDouble(-1, 1));
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomDouble_WithLowerGreaterThanUpper_ThrowsException(String providerName, RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomDouble(2, 1));
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomFloat_WithNormalRange(String providerName, RandomUtils ru) {
        final float result = ru.randomFloat(33f, 42f);
        assertTrue(result >= 33f);
        assertTrue(result < 42f);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomFloat_WithMinimalRange(String providerName, RandomUtils ru) {
        assertEquals(42.1f, ru.randomFloat(42.1f, 42.1f), DELTA);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomFloat_NoArgs(String providerName, RandomUtils ru) {
        final float result = ru.randomFloat();
        assertTrue(result >= 0f);
        assertTrue(result < Float.MAX_VALUE);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomFloat_ExtremeRange(String providerName, RandomUtils ru) {
        final float result = ru.randomFloat(0, Float.MAX_VALUE);
        assertTrue(result >= 0f);
        assertTrue(result < Float.MAX_VALUE);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomFloat_WithNegativeStart_ThrowsException(String providerName, RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomFloat(-1, 1));
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomFloat_WithLowerGreaterThanUpper_ThrowsException(String providerName, RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomFloat(2, 1));
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomInt_WithNormalRange(String providerName, RandomUtils ru) {
        final int result = ru.randomInt(33, 42);
        assertTrue(result >= 33);
        assertTrue(result < 42);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomInt_WithMinimalRange(String providerName, RandomUtils ru) {
        assertEquals(42, ru.randomInt(42, 42));
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomInt_NoArgs(String providerName, RandomUtils ru) {
        final int result = ru.randomInt();
        assertTrue(result >= 0);
        assertTrue(result < Integer.MAX_VALUE);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomInt_ExtremeRange(String providerName, RandomUtils ru) {
        final int result = ru.randomInt(0, Integer.MAX_VALUE);
        assertTrue(result >= 0);
        assertTrue(result < Integer.MAX_VALUE);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomInt_WithNegativeStart_ThrowsException(String providerName, RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomInt(-1, 1));
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomInt_WithLowerGreaterThanUpper_ThrowsException(String providerName, RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomInt(2, 1));
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomLong_WithNormalRange(String providerName, RandomUtils ru) {
        final long result = ru.randomLong(33L, 42L);
        assertTrue(result >= 33L);
        assertTrue(result < 42L);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomLong_WithMinimalRange(String providerName, RandomUtils ru) {
        assertEquals(42L, ru.randomLong(42L, 42L));
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomLong_NoArgs(String providerName, RandomUtils ru) {
        final long result = ru.randomLong();
        assertTrue(result >= 0L);
        assertTrue(result < Long.MAX_VALUE);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomLong_ExtremeRange(String providerName, RandomUtils ru) {
        final long result = ru.randomLong(0, Long.MAX_VALUE);
        assertTrue(result >= 0);
        assertTrue(result < Long.MAX_VALUE);
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomLong_WithNegativeStart_ThrowsException(String providerName, RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomLong(-1, 1));
    }

    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomLong_WithLowerGreaterThanUpper_ThrowsException(String providerName, RandomUtils ru) {
        assertIllegalArgumentException(() -> ru.randomLong(2, 1));
    }

    /**
     * Tests regression for LANG-1592 where large long ranges could
     * produce values equal to the exclusive upper bound
     */
    @ParameterizedTest(name = "[{index}] provider: {0}")
    @MethodSource("randomProvider")
    void testInstanceRandomLong_LargeValueRangeRegression(String providerName, RandomUtils ru) {
        final long startInclusive = 12900000000001L;
        final long endExclusive = 12900000000016L;
        final int n = (int) (endExclusive - startInclusive) * 1000;
        for (int i = 0; i < n; i++) {
            assertNotEquals(endExclusive, ru.randomLong(startInclusive, endExclusive));
        }
    }
}