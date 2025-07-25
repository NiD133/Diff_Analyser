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
            boolean value = true;

            boolean[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return the same array instance.");
            for (boolean element : filledArray) {
                assertEquals(value, element, "All elements should be equal to the fill value.");
            }
        }

        @Test
        void testFillBooleanArrayWithNullArray() {
            boolean[] array = null;
            boolean value = true;

            boolean[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return null when input array is null.");
        }

        @Test
        void testFillByteArray() {
            byte[] array = new byte[3];
            byte value = (byte) 1;

            byte[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return the same array instance.");
            for (byte element : filledArray) {
                assertEquals(value, element, "All elements should be equal to the fill value.");
            }
        }

        @Test
        void testFillByteArrayWithNullArray() {
            byte[] array = null;
            byte value = (byte) 1;

            byte[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return null when input array is null.");
        }

        @Test
        void testFillCharArray() {
            char[] array = new char[3];
            char value = 'A';

            char[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return the same array instance.");
            for (char element : filledArray) {
                assertEquals(value, element, "All elements should be equal to the fill value.");
            }
        }

        @Test
        void testFillCharArrayWithNullArray() {
            char[] array = null;
            char value = 'A';

            char[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return null when input array is null.");
        }

        @Test
        void testFillDoubleArray() {
            double[] array = new double[3];
            double value = 1.0;

            double[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return the same array instance.");
            for (double element : filledArray) {
                assertEquals(value, element, "All elements should be equal to the fill value.");
            }
        }

        @Test
        void testFillDoubleArrayWithNullArray() {
            double[] array = null;
            double value = 1.0;

            double[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return null when input array is null.");
        }

        @Test
        void testFillFloatArray() {
            float[] array = new float[3];
            float value = 1.0f;

            float[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return the same array instance.");
            for (float element : filledArray) {
                assertEquals(value, element, "All elements should be equal to the fill value.");
            }
        }

        @Test
        void testFillFloatArrayWithNullArray() {
            float[] array = null;
            float value = 1.0f;

            float[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return null when input array is null.");
        }

        @Test
        void testFillIntArray() {
            int[] array = new int[3];
            int value = 1;

            int[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return the same array instance.");
            for (int element : filledArray) {
                assertEquals(value, element, "All elements should be equal to the fill value.");
            }
        }

        @Test
        void testFillIntArrayWithNullArray() {
            int[] array = null;
            int value = 1;

            int[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return null when input array is null.");
        }

        @Test
        void testFillLongArray() {
            long[] array = new long[3];
            long value = 1L;

            long[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return the same array instance.");
            for (long element : filledArray) {
                assertEquals(value, element, "All elements should be equal to the fill value.");
            }
        }

        @Test
        void testFillLongArrayWithNullArray() {
            long[] array = null;
            long value = 1L;

            long[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return null when input array is null.");
        }

        @Test
        void testFillShortArray() {
            short[] array = new short[3];
            short value = (short) 1;

            short[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return the same array instance.");
            for (short element : filledArray) {
                assertEquals(value, element, "All elements should be equal to the fill value.");
            }
        }

        @Test
        void testFillShortArrayWithNullArray() {
            short[] array = null;
            short value = (short) 1;

            short[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return null when input array is null.");
        }
    }

    @Nested
    class FillObjectArrays {
        @Test
        void testFillObjectArray() {
            String[] array = new String[3];
            String value = "A";

            String[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return the same array instance.");
            for (String element : filledArray) {
                assertEquals(value, element, "All elements should be equal to the fill value.");
            }
        }

        @Test
        void testFillObjectArrayWithNullArray() {
            Object[] array = null;
            Object value = "A";

            Object[] filledArray = ArrayFill.fill(array, value);

            assertSame(array, filledArray, "Should return null when input array is null.");
        }

        @Test
        void testFillWithFunction() throws Exception {
            FailableIntFunction<?, Exception> nullIntFunction = null;
            assertNull(ArrayFill.fill(null, nullIntFunction), "fill(null, nullIntFunction) must return null");
            assertArrayEquals(null, ArrayFill.fill(null, nullIntFunction), "fill(null, nullIntFunction) must return null");
            assertArrayEquals(ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY, ArrayFill.fill(ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY, nullIntFunction), "fill(empty, nullIntFunction) must return empty");
            assertArrayEquals(ArrayUtils.EMPTY_OBJECT_ARRAY, ArrayFill.fill(ArrayUtils.EMPTY_OBJECT_ARRAY, nullIntFunction), "fill(empty, nullIntFunction) must return empty");

            Integer[] array = new Integer[10];
            Integer[] filledArray = ArrayFill.fill(array, Integer::valueOf);

            assertSame(array, filledArray, "Should return the same array instance.");
            for (int i = 0; i < array.length; i++) {
                assertEquals(i, array[i].intValue(), "Element at index " + i + " should be equal to " + i);
            }
        }
    }
}