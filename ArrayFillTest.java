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
 * Unless required by applicable law or agreed to in writing, software,
 * software distributed under the License is distributed on an "AS IS" BASIS,
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ArrayFill}.
 */
class ArrayFillTest extends AbstractLangTest {

    @Nested
    class FillPrimitiveArrays {

        @Test
        void testFillBooleanArray() {
            boolean[] array = new boolean[3];
            boolean valueToFill = true;

            boolean[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return the same array instance");
            for (boolean element : filledArray) {
                assertEquals(valueToFill, element, "All elements should be equal to the fill value");
            }
        }

        @Test
        void testFillBooleanArrayNull() {
            boolean[] array = null;
            boolean valueToFill = true;

            boolean[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return null when input array is null");
        }

        @Test
        void testFillByteArray() {
            byte[] array = new byte[3];
            byte valueToFill = (byte) 1;

            byte[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return the same array instance");
            for (byte element : filledArray) {
                assertEquals(valueToFill, element, "All elements should be equal to the fill value");
            }
        }

        @Test
        void testFillByteArrayNull() {
            byte[] array = null;
            byte valueToFill = (byte) 1;

            byte[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return null when input array is null");
        }

        @Test
        void testFillCharArray() {
            char[] array = new char[3];
            char valueToFill = 'A';

            char[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return the same array instance");
            for (char element : filledArray) {
                assertEquals(valueToFill, element, "All elements should be equal to the fill value");
            }
        }

        @Test
        void testFillCharArrayNull() {
            char[] array = null;
            char valueToFill = 'A';

            char[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return null when input array is null");
        }

        @Test
        void testFillDoubleArray() {
            double[] array = new double[3];
            double valueToFill = 1.0;

            double[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return the same array instance");
            for (double element : filledArray) {
                assertEquals(valueToFill, element, "All elements should be equal to the fill value");
            }
        }

        @Test
        void testFillDoubleArrayNull() {
            double[] array = null;
            double valueToFill = 1.0;

            double[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return null when input array is null");
        }

        @Test
        void testFillFloatArray() {
            float[] array = new float[3];
            float valueToFill = 1.0f;

            float[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return the same array instance");
            for (float element : filledArray) {
                assertEquals(valueToFill, element, "All elements should be equal to the fill value");
            }
        }

        @Test
        void testFillFloatArrayNull() {
            float[] array = null;
            float valueToFill = 1.0f;

            float[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return null when input array is null");
        }

        @Test
        void testFillIntArray() {
            int[] array = new int[3];
            int valueToFill = 1;

            int[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return the same array instance");
            for (int element : filledArray) {
                assertEquals(valueToFill, element, "All elements should be equal to the fill value");
            }
        }

        @Test
        void testFillIntArrayNull() {
            int[] array = null;
            int valueToFill = 1;

            int[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return null when input array is null");
        }

        @Test
        void testFillLongArray() {
            long[] array = new long[3];
            long valueToFill = 1L;

            long[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return the same array instance");
            for (long element : filledArray) {
                assertEquals(valueToFill, element, "All elements should be equal to the fill value");
            }
        }

        @Test
        void testFillLongArrayNull() {
            long[] array = null;
            long valueToFill = 1L;

            long[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return null when input array is null");
        }

        @Test
        void testFillShortArray() {
            short[] array = new short[3];
            short valueToFill = (short) 1;

            short[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return the same array instance");
            for (short element : filledArray) {
                assertEquals(valueToFill, element, "All elements should be equal to the fill value");
            }
        }

        @Test
        void testFillShortArrayNull() {
            short[] array = null;
            short valueToFill = (short) 1;

            short[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return null when input array is null");
        }
    }

    @Nested
    class FillObjectArrays {
        @Test
        void testFillObjectArray() {
            String[] array = new String[3];
            String valueToFill = "A";

            String[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return the same array instance");
            for (String element : filledArray) {
                assertEquals(valueToFill, element, "All elements should be equal to the fill value");
            }
        }

        @Test
        void testFillObjectArrayNull() {
            Object[] array = null;
            Object valueToFill = 1;

            Object[] filledArray = ArrayFill.fill(array, valueToFill);

            assertSame(array, filledArray, "Should return null when input array is null");
        }

        @Test
        void testFillWithFunction() throws Exception {
            FailableIntFunction<?, Exception> nullIntFunction = null;
            assertNull(ArrayFill.fill(null, nullIntFunction), "Should return null if array is null and function is null.");
            assertArrayEquals(null, ArrayFill.fill(null, nullIntFunction), "Should return null if array is null and function is null.");
            assertArrayEquals(ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY, ArrayFill.fill(ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY, nullIntFunction), "Should return empty array if array is empty and function is null.");
            assertArrayEquals(ArrayUtils.EMPTY_OBJECT_ARRAY, ArrayFill.fill(ArrayUtils.EMPTY_OBJECT_ARRAY, nullIntFunction), "Should return empty array if array is empty and function is null.");

            Integer[] array = new Integer[10];
            Integer[] filledArray = ArrayFill.fill(array, Integer::valueOf);

            assertSame(array, filledArray, "Should return the same array instance");
            for (int i = 0; i < array.length; i++) {
                assertEquals(i, array[i].intValue(), "Element at index " + i + " should be equal to " + i);
            }
        }
    }
}