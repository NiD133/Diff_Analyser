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
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link RandomUtils}
 */
class RandomUtilsTest extends AbstractLangTest {

    // Test constants for better maintainability
    private static final double FLOATING_POINT_DELTA = 1e-5;
    private static final int STANDARD_BYTE_ARRAY_SIZE = 20;
    private static final double TEST_RANGE_MIN = 33.0;
    private static final double TEST_RANGE_MAX = 42.0;
    private static final int TEST_RANGE_MIN_INT = 33;
    private static final int TEST_RANGE_MAX_INT = 42;
    private static final long TEST_RANGE_MIN_LONG = 33L;
    private static final long TEST_RANGE_MAX_LONG = 42L;
    private static final float TEST_RANGE_MIN_FLOAT = 33f;
    private static final float TEST_RANGE_MAX_FLOAT = 42f;
    private static final double MINIMAL_RANGE_VALUE = 42.1;
    private static final int MINIMAL_RANGE_VALUE_INT = 42;
    private static final long MINIMAL_RANGE_VALUE_LONG = 42L;
    private static final float MINIMAL_RANGE_VALUE_FLOAT = 42.1f;
    
    // Constants for the large value range test (LANG-1592 regression test)
    private static final long LARGE_RANGE_START = 12900000000001L;
    private static final long LARGE_RANGE_END = 12900000000016L;
    private static final int LARGE_RANGE_TEST_MULTIPLIER = 1000;

    /**
     * Provides different RandomUtils instances for parameterized tests.
     * Tests secure, secureStrong, and insecure variants.
     */
    static Stream<RandomUtils> randomUtilsProvider() {
        return Stream.of(
            RandomUtils.secure(), 
            RandomUtils.secureStrong(), 
            RandomUtils.insecure()
        );
    }

    // ========== Constructor Tests ==========
    
    @Test
    void shouldAllowConstructorInstantiation() {
        assertNotNull(new RandomUtils());
    }

    // ========== Boolean Generation Tests ==========

    @Test
    void shouldGenerateBooleanValue_StaticMethod() {
        boolean result = RandomUtils.nextBoolean();
        
        // Boolean must be either true or false (tautology test to ensure method executes)
        assertTrue(result || !result);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldGenerateBooleanValue_InstanceMethod(RandomUtils randomUtils) {
        boolean result = randomUtils.randomBoolean();
        
        assertTrue(result || !result);
    }

    // ========== Byte Array Generation Tests ==========

    @Test
    void shouldGenerateByteArrayOfSpecifiedSize_StaticMethod() {
        byte[] result = RandomUtils.nextBytes(STANDARD_BYTE_ARRAY_SIZE);
        
        assertEquals(STANDARD_BYTE_ARRAY_SIZE, result.length);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldGenerateByteArrayOfSpecifiedSize_InstanceMethod(RandomUtils randomUtils) {
        byte[] result = randomUtils.randomBytes(STANDARD_BYTE_ARRAY_SIZE);
        
        assertEquals(STANDARD_BYTE_ARRAY_SIZE, result.length);
    }

    @Test
    void shouldGenerateEmptyByteArrayWhenSizeIsZero_StaticMethod() {
        byte[] expected = new byte[0];
        byte[] actual = RandomUtils.nextBytes(0);
        
        assertArrayEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldGenerateEmptyByteArrayWhenSizeIsZero_InstanceMethod(RandomUtils randomUtils) {
        byte[] expected = new byte[0];
        byte[] actual = randomUtils.randomBytes(0);
        
        assertArrayEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionWhenByteArraySizeIsNegative_StaticMethod() {
        assertIllegalArgumentException(() -> RandomUtils.nextBytes(-1));
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldThrowExceptionWhenByteArraySizeIsNegative_InstanceMethod(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomBytes(-1));
    }

    // ========== Integer Generation Tests ==========

    @Test
    void shouldGenerateRandomIntegerWithinBounds_StaticMethod() {
        int result = RandomUtils.nextInt(TEST_RANGE_MIN_INT, TEST_RANGE_MAX_INT);
        
        assertIntegerWithinRange(result, TEST_RANGE_MIN_INT, TEST_RANGE_MAX_INT);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldGenerateRandomIntegerWithinBounds_InstanceMethod(RandomUtils randomUtils) {
        int result = randomUtils.randomInt(TEST_RANGE_MIN_INT, TEST_RANGE_MAX_INT);
        
        assertIntegerWithinRange(result, TEST_RANGE_MIN_INT, TEST_RANGE_MAX_INT);
    }

    @Test
    void shouldGenerateRandomIntegerWithoutBounds_StaticMethod() {
        int result = RandomUtils.nextInt();
        
        assertTrue(result > 0);
        assertTrue(result < Integer.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldGenerateRandomIntegerWithoutBounds_InstanceMethod(RandomUtils randomUtils) {
        int result = randomUtils.randomInt();
        
        assertTrue(result > 0);
        assertTrue(result < Integer.MAX_VALUE);
    }

    @Test
    void shouldHandleMinimalIntegerRange_StaticMethod() {
        int result = RandomUtils.nextInt(MINIMAL_RANGE_VALUE_INT, MINIMAL_RANGE_VALUE_INT);
        
        assertEquals(MINIMAL_RANGE_VALUE_INT, result);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldHandleMinimalIntegerRange_InstanceMethod(RandomUtils randomUtils) {
        int result = randomUtils.randomInt(MINIMAL_RANGE_VALUE_INT, MINIMAL_RANGE_VALUE_INT);
        
        assertEquals(MINIMAL_RANGE_VALUE_INT, result);
    }

    @Test
    void shouldHandleExtremeIntegerRange_StaticMethod() {
        int result = RandomUtils.nextInt(0, Integer.MAX_VALUE);
        
        assertTrue(result >= 0);
        assertTrue(result < Integer.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldHandleExtremeIntegerRange_InstanceMethod(RandomUtils randomUtils) {
        int result = randomUtils.randomInt(0, Integer.MAX_VALUE);
        
        assertTrue(result >= 0);
        assertTrue(result < Integer.MAX_VALUE);
    }

    @Test
    void shouldThrowExceptionWhenIntegerStartIsNegative_StaticMethod() {
        assertIllegalArgumentException(() -> RandomUtils.nextInt(-1, 1));
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldThrowExceptionWhenIntegerStartIsNegative_InstanceMethod(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomInt(-1, 1));
    }

    @Test
    void shouldThrowExceptionWhenIntegerStartGreaterThanEnd_StaticMethod() {
        assertIllegalArgumentException(() -> RandomUtils.nextInt(2, 1));
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldThrowExceptionWhenIntegerStartGreaterThanEnd_InstanceMethod(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomInt(2, 1));
    }

    // ========== Long Generation Tests ==========

    @Test
    void shouldGenerateRandomLongWithinBounds_StaticMethod() {
        long result = RandomUtils.nextLong(TEST_RANGE_MIN_LONG, TEST_RANGE_MAX_LONG);
        
        assertLongWithinRange(result, TEST_RANGE_MIN_LONG, TEST_RANGE_MAX_LONG);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldGenerateRandomLongWithinBounds_InstanceMethod(RandomUtils randomUtils) {
        long result = randomUtils.randomLong(TEST_RANGE_MIN_LONG, TEST_RANGE_MAX_LONG);
        
        assertLongWithinRange(result, TEST_RANGE_MIN_LONG, TEST_RANGE_MAX_LONG);
    }

    @Test
    void shouldGenerateRandomLongWithoutBounds_StaticMethod() {
        long result = RandomUtils.nextLong();
        
        assertTrue(result >= 0L);
        assertTrue(result < Long.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldGenerateRandomLongWithoutBounds_InstanceMethod(RandomUtils randomUtils) {
        long result = randomUtils.randomLong();
        
        assertTrue(result >= 0L);
        assertTrue(result < Long.MAX_VALUE);
    }

    @Test
    void shouldHandleMinimalLongRange_StaticMethod() {
        long result = RandomUtils.nextLong(MINIMAL_RANGE_VALUE_LONG, MINIMAL_RANGE_VALUE_LONG);
        
        assertEquals(MINIMAL_RANGE_VALUE_LONG, result);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldHandleMinimalLongRange_InstanceMethod(RandomUtils randomUtils) {
        long result = randomUtils.randomLong(MINIMAL_RANGE_VALUE_LONG, MINIMAL_RANGE_VALUE_LONG);
        
        assertEquals(MINIMAL_RANGE_VALUE_LONG, result);
    }

    @Test
    void shouldHandleExtremeLongRange_StaticMethod() {
        long result = RandomUtils.nextLong(0, Long.MAX_VALUE);
        
        assertTrue(result >= 0);
        assertTrue(result < Long.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldHandleExtremeLongRange_InstanceMethod(RandomUtils randomUtils) {
        long result = randomUtils.randomLong(0, Long.MAX_VALUE);
        
        assertTrue(result >= 0);
        assertTrue(result < Long.MAX_VALUE);
    }

    /**
     * Regression test for LANG-1592.
     * Previous implementation using nextDouble() could generate values equal to the upper limit.
     * This test ensures the upper bound is always exclusive for large value ranges.
     */
    @Test
    void shouldNeverReturnUpperBoundForLargeValueRange_StaticMethod() {
        int testIterations = calculateLargeRangeTestIterations();
        
        for (int i = 0; i < testIterations; i++) {
            long result = RandomUtils.nextLong(LARGE_RANGE_START, LARGE_RANGE_END);
            assertNotEquals(LARGE_RANGE_END, result, 
                "Upper bound should be exclusive, but got: " + result);
        }
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldNeverReturnUpperBoundForLargeValueRange_InstanceMethod(RandomUtils randomUtils) {
        int testIterations = calculateLargeRangeTestIterations();
        
        for (int i = 0; i < testIterations; i++) {
            long result = randomUtils.randomLong(LARGE_RANGE_START, LARGE_RANGE_END);
            assertNotEquals(LARGE_RANGE_END, result,
                "Upper bound should be exclusive, but got: " + result);
        }
    }

    @Test
    void shouldThrowExceptionWhenLongStartIsNegative_StaticMethod() {
        assertIllegalArgumentException(() -> RandomUtils.nextLong(-1, 1));
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldThrowExceptionWhenLongStartIsNegative_InstanceMethod(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomLong(-1, 1));
    }

    @Test
    void shouldThrowExceptionWhenLongStartGreaterThanEnd_StaticMethod() {
        assertIllegalArgumentException(() -> RandomUtils.nextLong(2, 1));
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldThrowExceptionWhenLongStartGreaterThanEnd_InstanceMethod(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomLong(2, 1));
    }

    // ========== Double Generation Tests ==========

    @Test
    void shouldGenerateRandomDoubleWithinBounds_StaticMethod() {
        double result = RandomUtils.nextDouble(TEST_RANGE_MIN, TEST_RANGE_MAX);
        
        assertDoubleWithinRange(result, TEST_RANGE_MIN, TEST_RANGE_MAX);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldGenerateRandomDoubleWithinBounds_InstanceMethod(RandomUtils randomUtils) {
        double result = randomUtils.randomDouble(TEST_RANGE_MIN, TEST_RANGE_MAX);
        
        assertDoubleWithinRange(result, TEST_RANGE_MIN, TEST_RANGE_MAX);
    }

    @Test
    void shouldGenerateRandomDoubleWithoutBounds_StaticMethod() {
        double result = RandomUtils.nextDouble();
        
        assertTrue(result >= 0d);
        assertTrue(result < Double.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldGenerateRandomDoubleWithoutBounds_InstanceMethod(RandomUtils randomUtils) {
        double result = randomUtils.randomDouble();
        
        assertTrue(result >= 0d);
        assertTrue(result < Double.MAX_VALUE);
    }

    @Test
    void shouldHandleMinimalDoubleRange_StaticMethod() {
        double result = RandomUtils.nextDouble(MINIMAL_RANGE_VALUE, MINIMAL_RANGE_VALUE);
        
        assertEquals(MINIMAL_RANGE_VALUE, result, FLOATING_POINT_DELTA);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldHandleMinimalDoubleRange_InstanceMethod(RandomUtils randomUtils) {
        double result = randomUtils.randomDouble(MINIMAL_RANGE_VALUE, MINIMAL_RANGE_VALUE);
        
        assertEquals(MINIMAL_RANGE_VALUE, result, FLOATING_POINT_DELTA);
    }

    @Test
    void shouldHandleExtremeDoubleRange_StaticMethod() {
        double result = RandomUtils.nextDouble(0, Double.MAX_VALUE);
        
        assertTrue(result >= 0);
        assertTrue(result <= Double.MAX_VALUE); // Note: TODO comment suggests this should be < max
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldHandleExtremeDoubleRange_InstanceMethod(RandomUtils randomUtils) {
        double result = randomUtils.randomDouble(0, Double.MAX_VALUE);
        
        assertTrue(result >= 0);
        assertTrue(result <= Double.MAX_VALUE); // Note: TODO comment suggests this should be < max
    }

    @Test
    void shouldThrowExceptionWhenDoubleStartIsNegative_StaticMethod() {
        assertIllegalArgumentException(() -> RandomUtils.nextDouble(-1, 1));
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldThrowExceptionWhenDoubleStartIsNegative_InstanceMethod(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomDouble(-1, 1));
    }

    @Test
    void shouldThrowExceptionWhenDoubleStartGreaterThanEnd_StaticMethod() {
        assertIllegalArgumentException(() -> RandomUtils.nextDouble(2, 1));
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldThrowExceptionWhenDoubleStartGreaterThanEnd_InstanceMethod(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomDouble(2, 1));
    }

    // ========== Float Generation Tests ==========

    @Test
    void shouldGenerateRandomFloatWithinBounds_StaticMethod() {
        float result = RandomUtils.nextFloat(TEST_RANGE_MIN_FLOAT, TEST_RANGE_MAX_FLOAT);
        
        assertFloatWithinRange(result, TEST_RANGE_MIN_FLOAT, TEST_RANGE_MAX_FLOAT);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldGenerateRandomFloatWithinBounds_InstanceMethod(RandomUtils randomUtils) {
        float result = randomUtils.randomFloat(TEST_RANGE_MIN_FLOAT, TEST_RANGE_MAX_FLOAT);
        
        assertFloatWithinRange(result, TEST_RANGE_MIN_FLOAT, TEST_RANGE_MAX_FLOAT);
    }

    @Test
    void shouldGenerateRandomFloatWithoutBounds_StaticMethod() {
        float result = RandomUtils.nextFloat();
        
        assertTrue(result >= 0f);
        assertTrue(result < Float.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldGenerateRandomFloatWithoutBounds_InstanceMethod(RandomUtils randomUtils) {
        float result = randomUtils.randomFloat();
        
        assertTrue(result >= 0f);
        assertTrue(result < Float.MAX_VALUE);
    }

    @Test
    void shouldHandleMinimalFloatRange_StaticMethod() {
        float result = RandomUtils.nextFloat(MINIMAL_RANGE_VALUE_FLOAT, MINIMAL_RANGE_VALUE_FLOAT);
        
        assertEquals(MINIMAL_RANGE_VALUE_FLOAT, result, FLOATING_POINT_DELTA);
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldHandleMinimalFloatRange_InstanceMethod(RandomUtils randomUtils) {
        float result = randomUtils.randomFloat(MINIMAL_RANGE_VALUE_FLOAT, MINIMAL_RANGE_VALUE_FLOAT);
        
        assertEquals(MINIMAL_RANGE_VALUE_FLOAT, result, FLOATING_POINT_DELTA);
    }

    @Test
    void shouldHandleExtremeFloatRange_StaticMethod() {
        float result = RandomUtils.nextFloat(0, Float.MAX_VALUE);
        
        assertTrue(result >= 0f);
        assertTrue(result <= Float.MAX_VALUE); // Note: TODO comment suggests this should be < max
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldHandleExtremeFloatRange_InstanceMethod(RandomUtils randomUtils) {
        float result = randomUtils.randomFloat(0, Float.MAX_VALUE);
        
        assertTrue(result >= 0f);
        assertTrue(result <= Float.MAX_VALUE); // Note: TODO comment suggests this should be < max
    }

    @Test
    void shouldThrowExceptionWhenFloatStartIsNegative_StaticMethod() {
        assertIllegalArgumentException(() -> RandomUtils.nextFloat(-1, 1));
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldThrowExceptionWhenFloatStartIsNegative_InstanceMethod(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomFloat(-1, 1));
    }

    @Test
    void shouldThrowExceptionWhenFloatStartGreaterThanEnd_StaticMethod() {
        assertIllegalArgumentException(() -> RandomUtils.nextFloat(2, 1));
    }

    @ParameterizedTest
    @MethodSource("randomUtilsProvider")
    void shouldThrowExceptionWhenFloatStartGreaterThanEnd_InstanceMethod(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomFloat(2, 1));
    }

    // ========== Helper Methods ==========

    private void assertIntegerWithinRange(int actual, int minInclusive, int maxExclusive) {
        assertTrue(actual >= minInclusive, 
            "Expected value >= " + minInclusive + " but was: " + actual);
        assertTrue(actual < maxExclusive, 
            "Expected value < " + maxExclusive + " but was: " + actual);
    }

    private void assertLongWithinRange(long actual, long minInclusive, long maxExclusive) {
        assertTrue(actual >= minInclusive, 
            "Expected value >= " + minInclusive + " but was: " + actual);
        assertTrue(actual < maxExclusive, 
            "Expected value < " + maxExclusive + " but was: " + actual);
    }

    private void assertDoubleWithinRange(double actual, double minInclusive, double maxExclusive) {
        assertTrue(actual >= minInclusive, 
            "Expected value >= " + minInclusive + " but was: " + actual);
        assertTrue(actual < maxExclusive, 
            "Expected value < " + maxExclusive + " but was: " + actual);
    }

    private void assertFloatWithinRange(float actual, float minInclusive, float maxExclusive) {
        assertTrue(actual >= minInclusive, 
            "Expected value >= " + minInclusive + " but was: " + actual);
        assertTrue(actual < maxExclusive, 
            "Expected value < " + maxExclusive + " but was: " + actual);
    }

    private int calculateLargeRangeTestIterations() {
        return (int) (LARGE_RANGE_END - LARGE_RANGE_START) * LARGE_RANGE_TEST_MULTIPLIER;
    }
}