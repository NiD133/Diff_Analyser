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

import org.apache.commons.lang3.function.FailableIntFunction;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    //region fill(T[] a, T val) - Object Arrays
    //-----------------------------------------------------------------------

    @Test
    public void fillObjectArray_withNullArray_shouldReturnNull() {
        assertNull(ArrayFill.fill((Object[]) null, "value"));
    }

    @Test
    public void fillObjectArray_withEmptyArray_shouldReturnSameEmptyArray() {
        Object[] array = new Object[0];
        assertSame(array, ArrayFill.fill(array, "value"));
    }

    @Test
    public void fillObjectArray_withNonEmptyArray_shouldFillWithValue() {
        Object[] array = new Object[3];
        ArrayFill.fill(array, "filled");
        assertArrayEquals(new Object[]{"filled", "filled", "filled"}, array);
    }

    @Test
    public void fillObjectArray_withNullValue_shouldFillWithNull() {
        Object[] array = new Object[]{"a", "b", "c"};
        ArrayFill.fill(array, null);
        assertArrayEquals(new Object[]{null, null, null}, array);
    }

    @Test(expected = ArrayStoreException.class)
    public void fillObjectArray_withIncompatibleType_shouldThrowArrayStoreException() {
        // Attempting to store an Integer in a String array should fail.
        String[] array = new String[2];
        ArrayFill.fill((Object[]) array, 123);
    }

    //endregion

    //region fill(T[] array, FailableIntFunction<? extends T, E> generator)
    //-----------------------------------------------------------------------

    @Test
    public void fillWithGenerator_withNullArray_shouldReturnNull() {
        assertNull(ArrayFill.fill((Object[]) null, i -> i));
    }

    @Test
    public void fillWithGenerator_withEmptyArray_shouldReturnSameEmptyArray() {
        Object[] array = new Object[0];
        assertSame(array, ArrayFill.fill(array, i -> i));
    }

    @Test
    public void fillWithGenerator_withNullGenerator_shouldDoNothing() {
        // The underlying Arrays.setAll throws a NullPointerException, but ArrayFill handles it.
        String[] array = {"a", "b", "c"};
        String[] original = Arrays.copyOf(array, array.length);
        ArrayFill.fill(array, (FailableIntFunction<String, Throwable>) null);
        assertArrayEquals(original, array);
    }

    @Test
    public void fillWithGenerator_withNonEmptyArray_shouldFillWithGeneratedValues() throws Throwable {
        Object[] array = new Object[3];
        ArrayFill.fill(array, i -> "value-" + i);
        assertArrayEquals(new Object[]{"value-0", "value-1", "value-2"}, array);
    }

    @Test
    public void fillWithGenerator_usingNop_shouldFillWithNulls() throws Throwable {
        Object[] array = new Object[]{"a", "b", "c"};
        ArrayFill.fill(array, FailableIntFunction.nop());
        assertArrayEquals(new Object[]{null, null, null}, array);
    }

    //endregion

    //region Primitive Array Tests
    //-----------------------------------------------------------------------

    @Test
    public void fillBooleanArray_withNonEmptyArray_shouldFillWithValue() {
        boolean[] array = new boolean[3];
        ArrayFill.fill(array, true);
        // JUnit 4 does not have a boolean[] overload for assertArrayEquals
        assertTrue(Arrays.equals(new boolean[]{true, true, true}, array));
    }

    @Test
    public void fillBooleanArray_withNullArray_shouldReturnNull() {
        assertNull(ArrayFill.fill((boolean[]) null, true));
    }

    @Test
    public void fillByteArray_withNonEmptyArray_shouldFillWithValue() {
        byte[] array = new byte[3];
        ArrayFill.fill(array, (byte) 123);
        assertArrayEquals(new byte[]{123, 123, 123}, array);
    }

    @Test
    public void fillByteArray_withNullArray_shouldReturnNull() {
        assertNull(ArrayFill.fill((byte[]) null, (byte) 123));
    }

    @Test
    public void fillCharArray_withNonEmptyArray_shouldFillWithValue() {
        char[] array = new char[3];
        ArrayFill.fill(array, 'z');
        assertArrayEquals(new char[]{'z', 'z', 'z'}, array);
    }

    @Test
    public void fillCharArray_withNullArray_shouldReturnNull() {
        assertNull(ArrayFill.fill((char[]) null, 'z'));
    }

    @Test
    public void fillShortArray_withNonEmptyArray_shouldFillWithValue() {
        short[] array = new short[3];
        ArrayFill.fill(array, (short) 42);
        assertArrayEquals(new short[]{42, 42, 42}, array);
    }

    @Test
    public void fillShortArray_withNullArray_shouldReturnNull() {
        assertNull(ArrayFill.fill((short[]) null, (short) 42));
    }

    @Test
    public void fillIntArray_withNonEmptyArray_shouldFillWithValue() {
        int[] array = new int[3];
        ArrayFill.fill(array, -1);
        assertArrayEquals(new int[]{-1, -1, -1}, array);
    }

    @Test
    public void fillIntArray_withNullArray_shouldReturnNull() {
        assertNull(ArrayFill.fill((int[]) null, -1));
    }

    @Test
    public void fillLongArray_withNonEmptyArray_shouldFillWithValue() {
        long[] array = new long[3];
        ArrayFill.fill(array, 12345L);
        assertArrayEquals(new long[]{12345L, 12345L, 12345L}, array);
    }

    @Test
    public void fillLongArray_withNullArray_shouldReturnNull() {
        assertNull(ArrayFill.fill((long[]) null, 12345L));
    }

    @Test
    public void fillFloatArray_withNonEmptyArray_shouldFillWithValue() {
        float[] array = new float[3];
        ArrayFill.fill(array, 3.14f);
        assertArrayEquals(new float[]{3.14f, 3.14f, 3.14f}, array, 0.0f);
    }

    @Test
    public void fillFloatArray_withNullArray_shouldReturnNull() {
        assertNull(ArrayFill.fill((float[]) null, 3.14f));
    }

    @Test
    public void fillDoubleArray_withNonEmptyArray_shouldFillWithValue() {
        double[] array = new double[3];
        ArrayFill.fill(array, 1.618);
        assertArrayEquals(new double[]{1.618, 1.618, 1.618}, array, 0.0);
    }

    @Test
    public void fillDoubleArray_withNullArray_shouldReturnNull() {
        assertNull(ArrayFill.fill((double[]) null, 1.618));
    }

    //endregion
}