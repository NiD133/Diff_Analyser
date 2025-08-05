/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ArrayFill}.
 */
class ArrayFillTest extends AbstractLangTest {

    @Nested
    @DisplayName("Tests for fill(array, value)")
    class FillWithValue {

        @Test
        void forBooleanArray_whenFilled_thenAllElementsAreUpdated() {
            // Arrange
            final boolean[] array = new boolean[3];
            final boolean[] expected = {true, true, true};

            // Act
            final boolean[] result = ArrayFill.fill(array, true);

            // Assert
            assertSame(array, result, "Should return the same array instance");
            assertArrayEquals(expected, result, "Array elements should be filled with the value");
        }

        @Test
        void forBooleanArray_whenArrayIsNull_thenReturnsNull() {
            assertNull(ArrayFill.fill((boolean[]) null, true), "Should return null for a null input array");
        }

        @Test
        void forByteArray_whenFilled_thenAllElementsAreUpdated() {
            // Arrange
            final byte[] array = new byte[3];
            final byte fillValue = (byte) 42;
            final byte[] expected = {fillValue, fillValue, fillValue};

            // Act
            final byte[] result = ArrayFill.fill(array, fillValue);

            // Assert
            assertSame(array, result, "Should return the same array instance");
            assertArrayEquals(expected, result, "Array elements should be filled with the value");
        }

        @Test
        void forByteArray_whenArrayIsNull_thenReturnsNull() {
            assertNull(ArrayFill.fill((byte[]) null, (byte) 42), "Should return null for a null input array");
        }

        @Test
        void forCharArray_whenFilled_thenAllElementsAreUpdated() {
            // Arrange
            final char[] array = new char[3];
            final char fillValue = 'X';
            final char[] expected = {fillValue, fillValue, fillValue};

            // Act
            final char[] result = ArrayFill.fill(array, fillValue);

            // Assert
            assertSame(array, result, "Should return the same array instance");
            assertArrayEquals(expected, result, "Array elements should be filled with the value");
        }

        @Test
        void forCharArray_whenArrayIsNull_thenReturnsNull() {
            assertNull(ArrayFill.fill((char[]) null, 'X'), "Should return null for a null input array");
        }

        @Test
        void forDoubleArray_whenFilled_thenAllElementsAreUpdated() {
            // Arrange
            final double[] array = new double[3];
            final double fillValue = 42.5;
            final double[] expected = {fillValue, fillValue, fillValue};

            // Act
            final double[] result = ArrayFill.fill(array, fillValue);

            // Assert
            assertSame(array, result, "Should return the same array instance");
            assertArrayEquals(expected, result, "Array elements should be filled with the value");
        }

        @Test
        void forDoubleArray_whenArrayIsNull_thenReturnsNull() {
            assertNull(ArrayFill.fill((double[]) null, 42.5), "Should return null for a null input array");
        }

        @Test
        void forFloatArray_whenFilled_thenAllElementsAreUpdated() {
            // Arrange
            final float[] array = new float[3];
            final float fillValue = 42.5f;
            final float[] expected = {fillValue, fillValue, fillValue};

            // Act
            final float[] result = ArrayFill.fill(array, fillValue);

            // Assert
            assertSame(array, result, "Should return the same array instance");
            assertArrayEquals(expected, result, "Array elements should be filled with the value");
        }

        @Test
        void forFloatArray_whenArrayIsNull_thenReturnsNull() {
            assertNull(ArrayFill.fill((float[]) null, 42.5f), "Should return null for a null input array");
        }

        @Test
        void forIntArray_whenFilled_thenAllElementsAreUpdated() {
            // Arrange
            final int[] array = new int[3];
            final int fillValue = 42;
            final int[] expected = {fillValue, fillValue, fillValue};

            // Act
            final int[] result = ArrayFill.fill(array, fillValue);

            // Assert
            assertSame(array, result, "Should return the same array instance");
            assertArrayEquals(expected, result, "Array elements should be filled with the value");
        }

        @Test
        void forIntArray_whenArrayIsNull_thenReturnsNull() {
            assertNull(ArrayFill.fill((int[]) null, 42), "Should return null for a null input array");
        }

        @Test
        void forLongArray_whenFilled_thenAllElementsAreUpdated() {
            // Arrange
            final long[] array = new long[3];
            final long fillValue = 42L;
            final long[] expected = {fillValue, fillValue, fillValue};

            // Act
            final long[] result = ArrayFill.fill(array, fillValue);

            // Assert
            assertSame(array, result, "Should return the same array instance");
            assertArrayEquals(expected, result, "Array elements should be filled with the value");
        }

        @Test
        void forLongArray_whenArrayIsNull_thenReturnsNull() {
            assertNull(ArrayFill.fill((long[]) null, 42L), "Should return null for a null input array");
        }

        @Test
        void forShortArray_whenFilled_thenAllElementsAreUpdated() {
            // Arrange
            final short[] array = new short[3];
            final short fillValue = (short) 42;
            final short[] expected = {fillValue, fillValue, fillValue};

            // Act
            final short[] result = ArrayFill.fill(array, fillValue);

            // Assert
            assertSame(array, result, "Should return the same array instance");
            assertArrayEquals(expected, result, "Array elements should be filled with the value");
        }

        @Test
        void forShortArray_whenArrayIsNull_thenReturnsNull() {
            assertNull(ArrayFill.fill((short[]) null, (short) 42), "Should return null for a null input array");
        }

        @Test
        void forObjectArray_whenFilled_thenAllElementsAreUpdated() {
            // Arrange
            final String[] array = new String[3];
            final String fillValue = "Apache";
            final String[] expected = {fillValue, fillValue, fillValue};

            // Act
            final String[] result = ArrayFill.fill(array, fillValue);

            // Assert
            assertSame(array, result, "Should return the same array instance");
            assertArrayEquals(expected, result, "Array elements should be filled with the value");
        }

        @Test
        void forObjectArray_whenArrayIsNull_thenReturnsNull() {
            assertNull(ArrayFill.fill((Object[]) null, "value"), "Should return null for a null input array");
        }
    }

    @Nested
    @DisplayName("Tests for fill(array, generator)")
    class FillWithGenerator {

        @Test
        void withValidGenerator_shouldFillArrayAndReturnSameInstance() throws Exception {
            // Arrange
            final Integer[] array = new Integer[5];
            final FailableIntFunction<Integer, Exception> generator = Integer::valueOf;
            final Integer[] expected = {0, 1, 2, 3, 4};

            // Act
            final Integer[] result = ArrayFill.fill(array, generator);

            // Assert
            assertSame(array, result, "Should return the same array instance");
            assertArrayEquals(expected, result, "Array should be filled by the generator");
        }

        @Test
        void withNullArray_shouldReturnNull() throws Exception {
            // Arrange
            final FailableIntFunction<Integer, Exception> generator = Integer::valueOf;

            // Act
            final Integer[] result = ArrayFill.fill(null, generator);

            // Assert
            assertNull(result, "Should return null for a null input array");
        }

        @Test
        void withNullGenerator_shouldReturnSameArrayUnchanged() throws Exception {
            // Arrange
            final Integer[] array = {10, 20, 30};
            final Integer[] expected = {10, 20, 30};
            final FailableIntFunction<Integer, Exception> nullGenerator = null;

            // Act
            final Integer[] result = ArrayFill.fill(array, nullGenerator);

            // Assert
            assertSame(array, result, "Should return the same array instance");
            assertArrayEquals(expected, result, "Array should not be modified by a null generator");
        }

        @Test
        void withEmptyArray_shouldReturnSameEmptyArray() throws Exception {
            // Arrange
            final String[] emptyArray = {};
            final FailableIntFunction<String, Exception> generator = i -> "X";

            // Act
            final String[] result = ArrayFill.fill(emptyArray, generator);

            // Assert
            assertSame(emptyArray, result, "Should return the same empty array instance");
        }
    }
}