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
 * Unit tests for {@link RandomUtils}.
 */
class RandomUtilsTest extends AbstractLangTest {

    // Tolerance for comparing floating-point numbers
    private static final double DELTA = 1e-5;

    /**
     * Provides different RandomUtils instances for parameterized tests.
     */
    static Stream<RandomUtils> randomProvider() {
        return Stream.of(RandomUtils.secure(), RandomUtils.secureStrong(), RandomUtils.insecure());
    }

    // Test methods for boolean generation

    @Test
    void testNextBoolean() {
        boolean result = RandomUtils.nextBoolean();
        assertTrue(result || !result, "Result should be a boolean value");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomBoolean(RandomUtils randomUtils) {
        boolean result = randomUtils.randomBoolean();
        assertTrue(result || !result, "Result should be a boolean value");
    }

    // Test methods for constructor

    @Test
    void testConstructor() {
        assertNotNull(new RandomUtils(), "Constructor should create a non-null instance");
    }

    // Test methods for double generation

    @Test
    void testNextDoubleWithinRange() {
        double result = RandomUtils.nextDouble(33d, 42d);
        assertTrue(result >= 33d && result < 42d, "Result should be within the specified range");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDoubleWithinRange(RandomUtils randomUtils) {
        double result = randomUtils.randomDouble(33d, 42d);
        assertTrue(result >= 33d && result < 42d, "Result should be within the specified range");
    }

    @Test
    void testNextDoubleWithInvalidRange() {
        assertIllegalArgumentException(() -> RandomUtils.nextDouble(2, 1), "Should throw IllegalArgumentException for invalid range");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDoubleWithInvalidRange(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomDouble(2, 1), "Should throw IllegalArgumentException for invalid range");
    }

    @Test
    void testNextDoubleMinimalRange() {
        assertEquals(42.1, RandomUtils.nextDouble(42.1, 42.1), DELTA, "Result should be equal to the minimal range value");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDoubleMinimalRange(RandomUtils randomUtils) {
        assertEquals(42.1, randomUtils.randomDouble(42.1, 42.1), DELTA, "Result should be equal to the minimal range value");
    }

    @Test
    void testNextDoubleNegativeRange() {
        assertIllegalArgumentException(() -> RandomUtils.nextDouble(-1, 1), "Should throw IllegalArgumentException for negative range");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDoubleNegativeRange(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomDouble(-1, 1), "Should throw IllegalArgumentException for negative range");
    }

    @Test
    void testNextDoubleRandomResult() {
        double result = RandomUtils.nextDouble();
        assertTrue(result >= 0d && result < Double.MAX_VALUE, "Result should be a valid double value");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomDoubleRandomResult(RandomUtils randomUtils) {
        double result = randomUtils.randomDouble();
        assertTrue(result >= 0d && result < Double.MAX_VALUE, "Result should be a valid double value");
    }

    // Test methods for float generation

    @Test
    void testNextFloatWithinRange() {
        float result = RandomUtils.nextFloat(33f, 42f);
        assertTrue(result >= 33f && result < 42f, "Result should be within the specified range");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloatWithinRange(RandomUtils randomUtils) {
        float result = randomUtils.randomFloat(33f, 42f);
        assertTrue(result >= 33f && result < 42f, "Result should be within the specified range");
    }

    @Test
    void testNextFloatWithInvalidRange() {
        assertIllegalArgumentException(() -> RandomUtils.nextFloat(2, 1), "Should throw IllegalArgumentException for invalid range");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloatWithInvalidRange(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomFloat(2, 1), "Should throw IllegalArgumentException for invalid range");
    }

    @Test
    void testNextFloatMinimalRange() {
        assertEquals(42.1f, RandomUtils.nextFloat(42.1f, 42.1f), DELTA, "Result should be equal to the minimal range value");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloatMinimalRange(RandomUtils randomUtils) {
        assertEquals(42.1f, randomUtils.randomFloat(42.1f, 42.1f), DELTA, "Result should be equal to the minimal range value");
    }

    @Test
    void testNextFloatNegativeRange() {
        assertIllegalArgumentException(() -> RandomUtils.nextFloat(-1, 1), "Should throw IllegalArgumentException for negative range");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloatNegativeRange(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomFloat(-1, 1), "Should throw IllegalArgumentException for negative range");
    }

    @Test
    void testNextFloatRandomResult() {
        float result = RandomUtils.nextFloat();
        assertTrue(result >= 0f && result < Float.MAX_VALUE, "Result should be a valid float value");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomFloatRandomResult(RandomUtils randomUtils) {
        float result = randomUtils.randomFloat();
        assertTrue(result >= 0f && result < Float.MAX_VALUE, "Result should be a valid float value");
    }

    // Test methods for integer generation

    @Test
    void testNextIntWithinRange() {
        int result = RandomUtils.nextInt(33, 42);
        assertTrue(result >= 33 && result < 42, "Result should be within the specified range");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomIntWithinRange(RandomUtils randomUtils) {
        int result = randomUtils.randomInt(33, 42);
        assertTrue(result >= 33 && result < 42, "Result should be within the specified range");
    }

    @Test
    void testNextIntWithInvalidRange() {
        assertIllegalArgumentException(() -> RandomUtils.nextInt(2, 1), "Should throw IllegalArgumentException for invalid range");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomIntWithInvalidRange(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomInt(2, 1), "Should throw IllegalArgumentException for invalid range");
    }

    @Test
    void testNextIntMinimalRange() {
        assertEquals(42, RandomUtils.nextInt(42, 42), "Result should be equal to the minimal range value");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomIntMinimalRange(RandomUtils randomUtils) {
        assertEquals(42, randomUtils.randomInt(42, 42), "Result should be equal to the minimal range value");
    }

    @Test
    void testNextIntNegativeRange() {
        assertIllegalArgumentException(() -> RandomUtils.nextInt(-1, 1), "Should throw IllegalArgumentException for negative range");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomIntNegativeRange(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomInt(-1, 1), "Should throw IllegalArgumentException for negative range");
    }

    @Test
    void testNextIntRandomResult() {
        int randomResult = RandomUtils.nextInt();
        assertTrue(randomResult > 0 && randomResult < Integer.MAX_VALUE, "Result should be a valid int value");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomIntRandomResult(RandomUtils randomUtils) {
        int randomResult = randomUtils.randomInt();
        assertTrue(randomResult > 0 && randomResult < Integer.MAX_VALUE, "Result should be a valid int value");
    }

    // Test methods for long generation

    @Test
    void testNextLongWithinRange() {
        long result = RandomUtils.nextLong(33L, 42L);
        assertTrue(result >= 33L && result < 42L, "Result should be within the specified range");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLongWithinRange(RandomUtils randomUtils) {
        long result = randomUtils.randomLong(33L, 42L);
        assertTrue(result >= 33L && result < 42L, "Result should be within the specified range");
    }

    @Test
    void testNextLongWithInvalidRange() {
        assertIllegalArgumentException(() -> RandomUtils.nextLong(2, 1), "Should throw IllegalArgumentException for invalid range");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLongWithInvalidRange(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomLong(2, 1), "Should throw IllegalArgumentException for invalid range");
    }

    @Test
    void testNextLongMinimalRange() {
        assertEquals(42L, RandomUtils.nextLong(42L, 42L), "Result should be equal to the minimal range value");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLongMinimalRange(RandomUtils randomUtils) {
        assertEquals(42L, randomUtils.randomLong(42L, 42L), "Result should be equal to the minimal range value");
    }

    @Test
    void testNextLongNegativeRange() {
        assertIllegalArgumentException(() -> RandomUtils.nextLong(-1, 1), "Should throw IllegalArgumentException for negative range");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLongNegativeRange(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomLong(-1, 1), "Should throw IllegalArgumentException for negative range");
    }

    @Test
    void testNextLongRandomResult() {
        long result = RandomUtils.nextLong();
        assertTrue(result >= 0L && result < Long.MAX_VALUE, "Result should be a valid long value");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomLongRandomResult(RandomUtils randomUtils) {
        long result = randomUtils.randomLong();
        assertTrue(result >= 0L && result < Long.MAX_VALUE, "Result should be a valid long value");
    }

    // Test methods for byte array generation

    @Test
    void testNextBytes() {
        byte[] result = RandomUtils.nextBytes(20);
        assertEquals(20, result.length, "Result should be a byte array of the specified length");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomBytes(RandomUtils randomUtils) {
        byte[] result = randomUtils.randomBytes(20);
        assertEquals(20, result.length, "Result should be a byte array of the specified length");
    }

    @Test
    void testNextBytesNegative() {
        assertIllegalArgumentException(() -> RandomUtils.nextBytes(-1), "Should throw IllegalArgumentException for negative array size");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testRandomBytesNegative(RandomUtils randomUtils) {
        assertIllegalArgumentException(() -> randomUtils.randomBytes(-1), "Should throw IllegalArgumentException for negative array size");
    }

    @Test
    void testZeroLengthNextBytes() {
        assertArrayEquals(new byte[0], RandomUtils.nextBytes(0), "Result should be an empty byte array");
    }

    @ParameterizedTest
    @MethodSource("randomProvider")
    void testZeroLengthRandomBytes(RandomUtils randomUtils) {
        assertArrayEquals(new byte[0], randomUtils.randomBytes(0), "Result should be an empty byte array");
    }
}