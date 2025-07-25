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

/**
 * Tests {@link ArrayFill}.
 */
class ArrayFillTest extends AbstractLangTest {

    @Test
    void testFillWithNullArray() {
        // Test fill methods with null array
        testFillWithNullArray(boolean.class, ArrayFill::fill, true);
        testFillWithNullArray(byte.class, ArrayFill::fill, (byte) 1);
        testFillWithNullArray(char.class, ArrayFill::fill, 'A');
        testFillWithNullArray(double.class, ArrayFill::fill, 1.0);
        testFillWithNullArray(float.class, ArrayFill::fill, 1.0f);
        testFillWithNullArray(int.class, ArrayFill::fill, 1);
        testFillWithNullArray(long.class, ArrayFill::fill, 1L);
        testFillWithNullArray(short.class, ArrayFill::fill, (short) 1);
    }

    @Test
    void testFillWithValidArray() {
        // Test fill methods with valid array
        testFillWithValidArray(boolean.class, ArrayFill::fill, true);
        testFillWithValidArray(byte.class, ArrayFill::fill, (byte) 1);
        testFillWithValidArray(char.class, ArrayFill::fill, 'A');
        testFillWithValidArray(double.class, ArrayFill::fill, 1.0);
        testFillWithValidArray(float.class, ArrayFill::fill, 1.0f);
        testFillWithValidArray(int.class, ArrayFill::fill, 1);
        testFillWithValidArray(long.class, ArrayFill::fill, 1L);
        testFillWithValidArray(short.class, ArrayFill::fill, (short) 1);
        testFillWithValidArray(String.class, ArrayFill::fill, "A");
    }

    @Test
    void testFillWithFunction() throws Exception {
        // Test fill with function
        testFillWithFunction(Integer[]::new, Integer::valueOf);
    }

    private <T> void testFillWithNullArray(Class<T> clazz, TriFunction<T[], T, T[]> fillFunction, T value) {
        // Test fill with null array
        T[] array = null;
        T[] actual = fillFunction.apply(array, value);
        assertNull(actual);
    }

    private <T> void testFillWithValidArray(Class<T> clazz, TriFunction<T[], T, T[]> fillFunction, T value) {
        // Test fill with valid array
        T[] array = clazz == boolean.class ? new boolean[3] : clazz == byte.class ? new byte[3] : clazz == char.class ? new char[3] : clazz == double.class ? new double[3] : clazz == float.class ? new float[3] : clazz == int.class ? new int[3] : clazz == long.class ? new long[3] : clazz == short.class ? new short[3] : (T[]) new String[3];
        T[] actual = fillFunction.apply(array, value);
        assertSame(array, actual);
        for (T v : actual) {
            assertEquals(value, v);
        }
    }

    private <T, E extends Throwable> void testFillWithFunction(IntFunction<T[]> arrayFunction, FailableIntFunction<T, E> function) throws E {
        // Test fill with function
        T[] array = arrayFunction.apply(10);
        T[] actual = ArrayFill.fill(array, function);
        assertSame(array, actual);
        for (int i = 0; i < array.length; i++) {
            assertEquals(i, (int) array[i]);
        }
    }

    @FunctionalInterface
    private interface TriFunction<T, U, R> {
        R apply(T t, U u);
    }
}