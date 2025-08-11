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

import java.util.Arrays;

import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ArrayFill}.
 *
 * Goals:
 * - Clear naming: What is filled and what is expected.
 * - No loops for assertions: Compare against pre-filled expected arrays.
 * - Explicit null handling checks.
 * - One place for common constants and helpers.
 */
class ArrayFillTest extends AbstractLangTest {

    private static final int SIZE = 3;
    private static final float EPSILON_F = 0.0f;
    private static final double EPSILON_D = 0.0d;

    // Helper creators for expected arrays (primitive overloads)

    private static boolean[] expected(final int size, final boolean val) {
        final boolean[] a = new boolean[size];
        Arrays.fill(a, val);
        return a;
    }

    private static byte[] expected(final int size, final byte val) {
        final byte[] a = new byte[size];
        Arrays.fill(a, val);
        return a;
    }

    private static char[] expected(final int size, final char val) {
        final char[] a = new char[size];
        Arrays.fill(a, val);
        return a;
    }

    private static double[] expected(final int size, final double val) {
        final double[] a = new double[size];
        Arrays.fill(a, val);
        return a;
    }

    private static float[] expected(final int size, final float val) {
        final float[] a = new float[size];
        Arrays.fill(a, val);
        return a;
    }

    private static int[] expected(final int size, final int val) {
        final int[] a = new int[size];
        Arrays.fill(a, val);
        return a;
    }

    private static long[] expected(final int size, final long val) {
        final long[] a = new long[size];
        Arrays.fill(a, val);
        return a;
    }

    private static short[] expected(final int size, final short val) {
        final short[] a = new short[size];
        Arrays.fill(a, val);
        return a;
    }

    // Generic helper for object arrays: preserve component type via copyOf.
    private static <T> T[] expected(final T[] template, final T val) {
        final T[] a = Arrays.copyOf(template, template.length);
        Arrays.fill(a, val);
        return a;
    }

    // ---------- Primitive arrays ----------

    @Test
    void fillBooleanArray_populatesAllElements_andReturnsSameInstance() {
        final boolean[] array = new boolean[SIZE];
        final boolean[] returned = ArrayFill.fill(array, true);
        assertSame(array, returned);
        assertArrayEquals(expected(SIZE, true), array);
    }

    @Test
    void fillBooleanArray_withNullArray_returnsNull() {
        assertNull(ArrayFill.fill((boolean[]) null, true));
    }

    @Test
    void fillByteArray_populatesAllElements_andReturnsSameInstance() {
        final byte[] array = new byte[SIZE];
        final byte[] returned = ArrayFill.fill(array, (byte) 1);
        assertSame(array, returned);
        assertArrayEquals(expected(SIZE, (byte) 1), array);
    }

    @Test
    void fillByteArray_withNullArray_returnsNull() {
        assertNull(ArrayFill.fill((byte[]) null, (byte) 1));
    }

    @Test
    void fillCharArray_populatesAllElements_andReturnsSameInstance() {
        final char[] array = new char[SIZE];
        final char[] returned = ArrayFill.fill(array, 'A');
        assertSame(array, returned);
        assertArrayEquals(expected(SIZE, 'A'), array);
    }

    @Test
    void fillCharArray_withNullArray_returnsNull() {
        assertNull(ArrayFill.fill((char[]) null, 'A'));
    }

    @Test
    void fillDoubleArray_populatesAllElements_andReturnsSameInstance() {
        final double[] array = new double[SIZE];
        final double[] returned = ArrayFill.fill(array, 1d);
        assertSame(array, returned);
        assertArrayEquals(expected(SIZE, 1d), array, EPSILON_D);
    }

    @Test
    void fillDoubleArray_withNullArray_returnsNull() {
        assertNull(ArrayFill.fill((double[]) null, 1d));
    }

    @Test
    void fillFloatArray_populatesAllElements_andReturnsSameInstance() {
        final float[] array = new float[SIZE];
        final float[] returned = ArrayFill.fill(array, 1f);
        assertSame(array, returned);
        assertArrayEquals(expected(SIZE, 1f), array, EPSILON_F);
    }

    @Test
    void fillFloatArray_withNullArray_returnsNull() {
        assertNull(ArrayFill.fill((float[]) null, 1f));
    }

    @Test
    void fillIntArray_populatesAllElements_andReturnsSameInstance() {
        final int[] array = new int[SIZE];
        final int[] returned = ArrayFill.fill(array, 1);
        assertSame(array, returned);
        assertArrayEquals(expected(SIZE, 1), array);
    }

    @Test
    void fillIntArray_withNullArray_returnsNull() {
        assertNull(ArrayFill.fill((int[]) null, 1));
    }

    @Test
    void fillLongArray_populatesAllElements_andReturnsSameInstance() {
        final long[] array = new long[SIZE];
        final long[] returned = ArrayFill.fill(array, 1L);
        assertSame(array, returned);
        assertArrayEquals(expected(SIZE, 1L), array);
    }

    @Test
    void fillLongArray_withNullArray_returnsNull() {
        assertNull(ArrayFill.fill((long[]) null, 1L));
    }

    @Test
    void fillShortArray_populatesAllElements_andReturnsSameInstance() {
        final short[] array = new short[SIZE];
        final short[] returned = ArrayFill.fill(array, (short) 1);
        assertSame(array, returned);
        assertArrayEquals(expected(SIZE, (short) 1), array);
    }

    @Test
    void fillShortArray_withNullArray_returnsNull() {
        assertNull(ArrayFill.fill((short[]) null, (short) 1));
    }

    // ---------- Object arrays (constant value) ----------

    @Test
    void fillObjectArray_populatesAllElements_andReturnsSameInstance() {
        final String[] array = new String[SIZE];
        final String[] returned = ArrayFill.fill(array, "A");
        assertSame(array, returned);
        assertArrayEquals(expected(array, "A"), array);
    }

    @Test
    void fillObjectArray_withNullArray_returnsNull() {
        assertNull(ArrayFill.fill((Object[]) null, 1));
    }

    // ---------- Object arrays (generator) ----------

    @Test
    void fillWithGenerator_withNullArray_returnsNull() {
        final FailableIntFunction<String, Exception> generator = null;
        assertNull(ArrayFill.fill((String[]) null, generator));
    }

    @Test
    void fillWithGenerator_withEmptyArraysAndNullGenerator_returnsSameInstance() throws Exception {
        final FailableIntFunction<Boolean, Exception> nullBooleanGen = null;
        final Boolean[] emptyBooleanArray = ArrayUtils.EMPTY_BOOLEAN_OBJECT_ARRAY;
        final Boolean[] returnedBoolean = ArrayFill.fill(emptyBooleanArray, nullBooleanGen);
        assertSame(emptyBooleanArray, returnedBoolean);
        assertEquals(0, returnedBoolean.length);

        final FailableIntFunction<Object, Exception> nullObjectGen = null;
        final Object[] emptyObjectArray = ArrayUtils.EMPTY_OBJECT_ARRAY;
        final Object[] returnedObject = ArrayFill.fill(emptyObjectArray, nullObjectGen);
        assertSame(emptyObjectArray, returnedObject);
        assertEquals(0, returnedObject.length);
    }

    @Test
    void fillWithGenerator_populatesArrayUsingIndex_andReturnsSameInstance() throws Exception {
        final Integer[] array = new Integer[10];
        final Integer[] returned = ArrayFill.fill(array, Integer::valueOf);
        assertSame(array, returned);

        final Integer[] expected = new Integer[array.length];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = i;
        }
        assertArrayEquals(expected, array);
    }
}