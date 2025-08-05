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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 *
 * The tests are structured to cover instance methods, deprecated static methods,
 * and the factory methods for creating RandomUtils instances.
 */
public class RandomUtilsTest {

    // A shared instance for tests where the type of randomness (secure vs. insecure) doesn't matter.
    private final RandomUtils randomUtils = RandomUtils.insecure();

    // ==========================================================================
    // Instance Methods (random...)
    // ==========================================================================

    @Test
    public void randomBytes_withPositiveCount_returnsArrayOfCorrectSize() {
        // Arrange
        final int count = 10;

        // Act
        final byte[] result = randomUtils.randomBytes(count);

        // Assert
        assertEquals(count, result.length);
    }

    @Test
    public void randomBytes_withZeroCount_returnsEmptyArray() {
        // Act
        final byte[] result = randomUtils.randomBytes(0);

        // Assert
        assertArrayEquals(new byte[0], result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void randomBytes_withNegativeCount_throwsIllegalArgumentException() {
        // Act
        randomUtils.randomBytes(-1); // Should throw
    }

    @Test
    public void randomInt_withValidRange_returnsValueInRange() {
        // Arrange
        final int startInclusive = 10;
        final int endExclusive = 20;

        // Act & Assert: Call multiple times to increase chance of catching boundary errors.
        for (int i = 0; i < 100; i++) {
            final int result = randomUtils.randomInt(startInclusive, endExclusive);
            assertTrue("Result " + result + " must be >= " + startInclusive, result >= startInclusive);
            assertTrue("Result " + result + " must be < " + endExclusive, result < endExclusive);
        }
    }

    @Test
    public void randomInt_withZeroRange_returnsStartValue() {
        assertEquals(0, randomUtils.randomInt(0, 0));
        assertEquals(404, randomUtils.randomInt(404, 404));
    }

    @Test(expected = IllegalArgumentException.class)
    public void randomInt_withEndLessThanStart_throwsIllegalArgumentException() {
        randomUtils.randomInt(10, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void randomInt_withNegativeStart_throwsIllegalArgumentException() {
        randomUtils.randomInt(-1, 5);
    }

    @Test
    public void randomLong_withValidRange_returnsValueInRange() {
        // Arrange
        final long startInclusive = 10L;
        final long endExclusive = 20L;

        // Act & Assert
        for (int i = 0; i < 100; i++) {
            final long result = randomUtils.randomLong(startInclusive, endExclusive);
            assertTrue("Result " + result + " must be >= " + startInclusive, result >= startInclusive);
            assertTrue("Result " + result + " must be < " + endExclusive, result < endExclusive);
        }
    }

    @Test
    public void randomLong_withZeroRange_returnsStartValue() {
        assertEquals(0L, randomUtils.randomLong(0L, 0L));
        assertEquals(404L, randomUtils.randomLong(404L, 404L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void randomLong_withEndLessThanStart_throwsIllegalArgumentException() {
        randomUtils.randomLong(10L, 5L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void randomLong_withNegativeStart_throwsIllegalArgumentException() {
        randomUtils.randomLong(-1L, 5L);
    }

    @Test
    public void randomDouble_withValidRange_returnsValueInRange() {
        // Arrange
        final double startInclusive = 10.0;
        final double endExclusive = 20.0;

        // Act & Assert
        for (int i = 0; i < 100; i++) {
            final double result = randomUtils.randomDouble(startInclusive, endExclusive);
            assertTrue("Result " + result + " must be >= " + startInclusive, result >= startInclusive);
            assertTrue("Result " + result + " must be < " + endExclusive, result < endExclusive);
        }
    }

    @Test
    public void randomDouble_withZeroRange_returnsStartValue() {
        assertEquals(0.0, randomUtils.randomDouble(0.0, 0.0), 0.001);
        assertEquals(40.4, randomUtils.randomDouble(40.4, 40.4), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void randomDouble_withEndLessThanStart_throwsIllegalArgumentException() {
        randomUtils.randomDouble(10.0, 5.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void randomDouble_withNegativeStart_throwsIllegalArgumentException() {
        randomUtils.randomDouble(-1.0, 5.0);
    }

    @Test
    public void randomFloat_withValidRange_returnsValueInRange() {
        // Arrange
        final float startInclusive = 10.0f;
        final float endExclusive = 20.0f;

        // Act & Assert
        for (int i = 0; i < 100; i++) {
            final float result = randomUtils.randomFloat(startInclusive, endExclusive);
            assertTrue("Result " + result + " must be >= " + startInclusive, result >= startInclusive);
            assertTrue("Result " + result + " must be < " + endExclusive, result < endExclusive);
        }
    }

    @Test
    public void randomFloat_withZeroRange_returnsStartValue() {
        assertEquals(0.0f, randomUtils.randomFloat(0.0f, 0.0f), 0.001f);
        assertEquals(4.04f, randomUtils.randomFloat(4.04f, 4.04f), 0.001f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void randomFloat_withEndLessThanStart_throwsIllegalArgumentException() {
        randomUtils.randomFloat(10.0f, 5.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void randomFloat_withNegativeStart_throwsIllegalArgumentException() {
        randomUtils.randomFloat(-1.0f, 5.0f);
    }

    /**
     * Tests that parameterless random methods execute without error.
     * The exact return value is unpredictable and not asserted.
     */
    @Test
    public void parameterlessRandomMethods_doNotThrowException() {
        randomUtils.randomBoolean();
        randomUtils.randomDouble();
        randomUtils.randomFloat();
        randomUtils.randomInt();
        randomUtils.randomLong();
    }

    // ==========================================================================
    // Deprecated Static Methods (next...)
    // ==========================================================================

    @Test
    @SuppressWarnings("deprecation")
    public void nextBytes_static_withPositiveCount_returnsArrayOfCorrectSize() {
        assertEquals(10, RandomUtils.nextBytes(10).length);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void nextBytes_static_withZeroCount_returnsEmptyArray() {
        assertArrayEquals(new byte[0], RandomUtils.nextBytes(0));
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("deprecation")
    public void nextBytes_static_withNegativeCount_throwsIllegalArgumentException() {
        RandomUtils.nextBytes(-1);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void nextInt_static_withValidRange_returnsValueInRange() {
        final int result = RandomUtils.nextInt(0, 10);
        assertTrue("Result " + result + " must be >= 0", result >= 0);
        assertTrue("Result " + result + " must be < 10", result < 10);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("deprecation")
    public void nextInt_static_withEndLessThanStart_throwsIllegalArgumentException() {
        RandomUtils.nextInt(10, 0);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void nextLong_static_withValidRange_returnsValueInRange() {
        final long result = RandomUtils.nextLong(0, 10);
        assertTrue("Result " + result + " must be >= 0", result >= 0);
        assertTrue("Result " + result + " must be < 10", result < 10);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("deprecation")
    public void nextLong_static_withEndLessThanStart_throwsIllegalArgumentException() {
        RandomUtils.nextLong(10L, 0L);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void nextDouble_static_withValidRange_returnsValueInRange() {
        final double result = RandomUtils.nextDouble(0.0, 10.0);
        assertTrue("Result " + result + " must be >= 0.0", result >= 0.0);
        assertTrue("Result " + result + " must be < 10.0", result < 10.0);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("deprecation")
    public void nextDouble_static_withEndLessThanStart_throwsIllegalArgumentException() {
        RandomUtils.nextDouble(10.0, 0.0);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void nextFloat_static_withValidRange_returnsValueInRange() {
        final float result = RandomUtils.nextFloat(0.0f, 10.0f);
        assertTrue("Result " + result + " must be >= 0.0f", result >= 0.0f);
        assertTrue("Result " + result + " must be < 10.0f", result < 10.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("deprecation")
    public void nextFloat_static_withEndLessThanStart_throwsIllegalArgumentException() {
        RandomUtils.nextFloat(10.0f, 0.0f);
    }

    /**
     * Tests that deprecated, parameterless static methods execute without error.
     */
    @Test
    @SuppressWarnings("deprecation")
    public void deprecatedParameterlessStaticMethods_doNotThrowException() {
        RandomUtils.nextBoolean();
        RandomUtils.nextDouble();
        RandomUtils.nextFloat();
        RandomUtils.nextInt();
        RandomUtils.nextLong();
    }

    // ==========================================================================
    // Factory Methods, Constructor, and Other Methods
    // ==========================================================================

    @Test
    public void insecure_returnsInstanceWithBackingRandom() {
        assertNotNull(RandomUtils.insecure().random());
    }

    @Test
    public void secure_returnsInstanceWithBackingRandom() {
        assertNotNull(RandomUtils.secure().random());
    }

    @Test
    public void secureStrong_returnsInstanceWithBackingRandom() {
        assertNotNull(RandomUtils.secureStrong().random());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void constructor_createsInstanceWithBackingRandom() {
        // The default constructor is deprecated but should still function.
        final RandomUtils fromConstructor = new RandomUtils();
        assertNotNull(fromConstructor);
        assertNotNull(fromConstructor.random());
    }

    @Test
    public void toString_doesNotThrowException() {
        assertNotNull(randomUtils.toString());
    }
}