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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

/**
 * Tests {@link ArrayFill}.
 */
class ArrayFillTest extends AbstractLangTest {

    @Nested
    class PrimitiveArrayTests {
        
        @Test
        void fillBooleanArray_shouldFillAllElementsWithGivenValue() {
            // Given
            final boolean[] array = new boolean[3];
            final boolean fillValue = true;
            
            // When
            final boolean[] result = ArrayFill.fill(array, fillValue);
            
            // Then
            assertSame(array, result, "Should return the same array instance");
            assertAllElementsEqual(result, fillValue);
        }

        @Test
        void fillBooleanArray_withNullArray_shouldReturnNull() {
            // Given
            final boolean[] nullArray = null;
            
            // When
            final boolean[] result = ArrayFill.fill(nullArray, true);
            
            // Then
            assertNull(result);
        }

        @Test
        void fillByteArray_shouldFillAllElementsWithGivenValue() {
            // Given
            final byte[] array = new byte[3];
            final byte fillValue = (byte) 1;
            
            // When
            final byte[] result = ArrayFill.fill(array, fillValue);
            
            // Then
            assertSame(array, result, "Should return the same array instance");
            assertAllElementsEqual(result, fillValue);
        }

        @Test
        void fillByteArray_withNullArray_shouldReturnNull() {
            // Given
            final byte[] nullArray = null;
            
            // When
            final byte[] result = ArrayFill.fill(nullArray, (byte) 1);
            
            // Then
            assertNull(result);
        }

        @Test
        void fillCharArray_shouldFillAllElementsWithGivenValue() {
            // Given
            final char[] array = new char[3];
            final char fillValue = 'A';
            
            // When
            final char[] result = ArrayFill.fill(array, fillValue);
            
            // Then
            assertSame(array, result, "Should return the same array instance");
            assertAllElementsEqual(result, fillValue);
        }

        @Test
        void fillCharArray_withNullArray_shouldReturnNull() {
            // Given
            final char[] nullArray = null;
            
            // When
            final char[] result = ArrayFill.fill(nullArray, 'A');
            
            // Then
            assertNull(result);
        }

        @Test
        void fillDoubleArray_shouldFillAllElementsWithGivenValue() {
            // Given
            final double[] array = new double[3];
            final double fillValue = 1.0;
            
            // When
            final double[] result = ArrayFill.fill(array, fillValue);
            
            // Then
            assertSame(array, result, "Should return the same array instance");
            assertAllElementsEqual(result, fillValue);
        }

        @Test
        void fillDoubleArray_withNullArray_shouldReturnNull() {
            // Given
            final double[] nullArray = null;
            
            // When
            final double[] result = ArrayFill.fill(nullArray, 1.0);
            
            // Then
            assertNull(result);
        }

        @Test
        void fillFloatArray_shouldFillAllElementsWithGivenValue() {
            // Given
            final float[] array = new float[3];
            final float fillValue = 1.0f;
            
            // When
            final float[] result = ArrayFill.fill(array, fillValue);
            
            // Then
            assertSame(array, result, "Should return the same array instance");
            assertAllElementsEqual(result, fillValue);
        }

        @Test
        void fillFloatArray_withNullArray_shouldReturnNull() {
            // Given
            final float[] nullArray = null;
            
            // When
            final float[] result = ArrayFill.fill(nullArray, 1.0f);
            
            // Then
            assertNull(result);
        }

        @Test
        void fillIntArray_shouldFillAllElementsWithGivenValue() {
            // Given
            final int[] array = new int[3];
            final int fillValue = 1;
            
            // When
            final int[] result = ArrayFill.fill(array, fillValue);
            
            // Then
            assertSame(array, result, "Should return the same array instance");
            assertAllElementsEqual(result, fillValue);
        }

        @Test
        void fillIntArray_withNullArray_shouldReturnNull() {
            // Given
            final int[] nullArray = null;
            
            // When
            final int[] result = ArrayFill.fill(nullArray, 1);
            
            // Then
            assertNull(result);
        }

        @Test
        void fillLongArray_shouldFillAllElementsWithGivenValue() {
            // Given
            final long[] array = new long[3];
            final long fillValue = 1L;
            
            // When
            final long[] result = ArrayFill.fill(array, fillValue);
            
            // Then
            assertSame(array, result, "Should return the same array instance");
            assertAllElementsEqual(result, fillValue);
        }

        @Test
        void fillLongArray_withNullArray_shouldReturnNull() {
            // Given
            final long[] nullArray = null;
            
            // When
            final long[] result = ArrayFill.fill(nullArray, 1L);
            
            // Then
            assertNull(result);
        }

        @Test
        void fillShortArray_shouldFillAllElementsWithGivenValue() {
            // Given
            final short[] array = new short[3];
            final short fillValue = (short) 1;
            
            // When
            final short[] result = ArrayFill.fill(array, fillValue);
            
            // Then
            assertSame(array, result, "Should return the same array instance");
            assertAllElementsEqual(result, fillValue);
        }

        @Test
        void fillShortArray_withNullArray_shouldReturnNull() {
            // Given
            final short[] nullArray = null;
            
            // When
            final short[] result = ArrayFill.fill(nullArray, (short) 1);
            
            // Then
            assertNull(result);
        }
    }

    @Nested
    class ObjectArrayTests {
        
        @Test
        void fillObjectArray_shouldFillAllElementsWithGivenValue() {
            // Given
            final String[] array = new String[3];
            final String fillValue = "A";
            
            // When
            final String[] result = ArrayFill.fill(array, fillValue);
            
            // Then
            assertSame(array, result, "Should return the same array instance");
            assertAllElementsEqual(result, fillValue);
        }

        @Test
        void fillObjectArray_withNullArray_shouldReturnNull() {
            // Given
            final Object[] nullArray = null;
            
            // When
            final Object[] result = ArrayFill.fill(nullArray, "value");
            
            // Then
            assertNull(result);
        }
    }

    @Nested
    class FunctionBasedFillTests {
        
        @Test
        void fillWithFunction_shouldUseIndexAsValue() throws Exception {
            // Given
            final Integer[] array = new Integer[10];
            
            // When
            final Integer[] result = ArrayFill.fill(array, Integer::valueOf);
            
            // Then
            assertSame(array, result, "Should return the same array instance");
            for (int i = 0; i < array.length; i++) {
                assertEquals(i, array[i].intValue(), 
                    "Element at index " + i + " should have value " + i);
            }
        }

        @Test
        void fillWithFunction_withNullArray_shouldReturnNull() throws Exception {
            // Given
            final FailableIntFunction<?, Exception> function = Integer::valueOf;
            
            // When & Then
            assertNull(ArrayFill.fill(null, function));
        }

        @Test
        void fillWithFunction_withNullFunction_shouldReturnArray() throws Exception {
            // Given
            final FailableIntFunction<?, Exception> nullFunction = null;
            
            // When & Then
            assertNull(ArrayFill.fill(null, nullFunction));
            assertArrayEquals(ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY, 
                ArrayFill.fill(ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY, nullFunction));
            assertArrayEquals(ArrayUtils.EMPTY_OBJECT_ARRAY, 
                ArrayFill.fill(ArrayUtils.EMPTY_OBJECT_ARRAY, nullFunction));
        }
    }

    // Helper methods for better readability
    
    private void assertAllElementsEqual(final boolean[] array, final boolean expected) {
        for (final boolean value : array) {
            assertEquals(expected, value);
        }
    }
    
    private void assertAllElementsEqual(final byte[] array, final byte expected) {
        for (final byte value : array) {
            assertEquals(expected, value);
        }
    }
    
    private void assertAllElementsEqual(final char[] array, final char expected) {
        for (final char value : array) {
            assertEquals(expected, value);
        }
    }
    
    private void assertAllElementsEqual(final double[] array, final double expected) {
        for (final double value : array) {
            assertEquals(expected, value);
        }
    }
    
    private void assertAllElementsEqual(final float[] array, final float expected) {
        for (final float value : array) {
            assertEquals(expected, value);
        }
    }
    
    private void assertAllElementsEqual(final int[] array, final int expected) {
        for (final int value : array) {
            assertEquals(expected, value);
        }
    }
    
    private void assertAllElementsEqual(final long[] array, final long expected) {
        for (final long value : array) {
            assertEquals(expected, value);
        }
    }
    
    private void assertAllElementsEqual(final short[] array, final short expected) {
        for (final short value : array) {
            assertEquals(expected, value);
        }
    }
    
    private void assertAllElementsEqual(final String[] array, final String expected) {
        for (final String value : array) {
            assertEquals(expected, value);
        }
    }
}