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

import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link ArraySorter}.
 */
class ArraySorterTest extends AbstractLangTest {

    // -----------------------
    // Helpers to remove duplication and clarify intent
    // -----------------------

    private void assertSortsAndReturnsSame(final byte[] original) {
        final byte[] expected = original.clone();
        Arrays.sort(expected);

        final byte[] returned = ArraySorter.sort(original);

        assertSame(original, returned, "sort(byte[]) should return the same instance");
        assertArrayEquals(expected, returned, "sort(byte[]) should sort ascending in place");
    }

    private void assertSortsAndReturnsSame(final char[] original) {
        final char[] expected = original.clone();
        Arrays.sort(expected);

        final char[] returned = ArraySorter.sort(original);

        assertSame(original, returned, "sort(char[]) should return the same instance");
        assertArrayEquals(expected, returned, "sort(char[]) should sort ascending in place");
    }

    private void assertSortsAndReturnsSame(final double[] original) {
        final double[] expected = original.clone();
        Arrays.sort(expected);

        final double[] returned = ArraySorter.sort(original);

        assertSame(original, returned, "sort(double[]) should return the same instance");
        assertArrayEquals(expected, returned, "sort(double[]) should sort ascending in place");
    }

    private void assertSortsAndReturnsSame(final float[] original) {
        final float[] expected = original.clone();
        Arrays.sort(expected);

        final float[] returned = ArraySorter.sort(original);

        assertSame(original, returned, "sort(float[]) should return the same instance");
        assertArrayEquals(expected, returned, "sort(float[]) should sort ascending in place");
    }

    private void assertSortsAndReturnsSame(final int[] original) {
        final int[] expected = original.clone();
        Arrays.sort(expected);

        final int[] returned = ArraySorter.sort(original);

        assertSame(original, returned, "sort(int[]) should return the same instance");
        assertArrayEquals(expected, returned, "sort(int[]) should sort ascending in place");
    }

    private void assertSortsAndReturnsSame(final long[] original) {
        final long[] expected = original.clone();
        Arrays.sort(expected);

        final long[] returned = ArraySorter.sort(original);

        assertSame(original, returned, "sort(long[]) should return the same instance");
        assertArrayEquals(expected, returned, "sort(long[]) should sort ascending in place");
    }

    private void assertSortsAndReturnsSame(final short[] original) {
        final short[] expected = original.clone();
        Arrays.sort(expected);

        final short[] returned = ArraySorter.sort(original);

        assertSame(original, returned, "sort(short[]) should return the same instance");
        assertArrayEquals(expected, returned, "sort(short[]) should sort ascending in place");
    }

    private <T> void assertSortsAndReturnsSame(final T[] original) {
        final T[] expected = original.clone();
        Arrays.sort(expected);

        final T[] returned = ArraySorter.sort(original);

        assertSame(original, returned, "sort(T[]) should return the same instance");
        assertArrayEquals(expected, returned, "sort(T[]) should sort ascending in place");
    }

    private <T> void assertSortsAndReturnsSameWithComparator(final T[] original,
            final Comparator<? super T> comparator) {
        final T[] expected = original.clone();
        Arrays.sort(expected, comparator);

        final T[] returned = ArraySorter.sort(original, comparator);

        assertSame(original, returned, "sort(T[], Comparator) should return the same instance");
        assertArrayEquals(expected, returned, "sort(T[], Comparator) should sort using the comparator in place");
    }

    // -----------------------
    // Tests
    // -----------------------

    @Test
    @DisplayName("sort(byte[]) sorts in place and returns the same instance")
    void sortsByteArrayInPlace() {
        assertSortsAndReturnsSame(new byte[] {2, 1});
    }

    @Test
    @DisplayName("sort(char[]) sorts in place and returns the same instance")
    void sortsCharArrayInPlace() {
        assertSortsAndReturnsSame(new char[] {2, 1});
    }

    @Test
    @DisplayName("sort(double[]) sorts in place and returns the same instance")
    void sortsDoubleArrayInPlace() {
        assertSortsAndReturnsSame(new double[] {2, 1});
    }

    @Test
    @DisplayName("sort(float[]) sorts in place and returns the same instance")
    void sortsFloatArrayInPlace() {
        assertSortsAndReturnsSame(new float[] {2, 1});
    }

    @Test
    @DisplayName("sort(int[]) sorts in place and returns the same instance")
    void sortsIntArrayInPlace() {
        assertSortsAndReturnsSame(new int[] {2, 1});
    }

    @Test
    @DisplayName("sort(long[]) sorts in place and returns the same instance")
    void sortsLongArrayInPlace() {
        assertSortsAndReturnsSame(new long[] {2, 1});
    }

    @Test
    @DisplayName("sort(short[]) sorts in place and returns the same instance")
    void sortsShortArrayInPlace() {
        assertSortsAndReturnsSame(new short[] {2, 1});
    }

    @Test
    @DisplayName("sort(T[]) sorts objects using natural order in place and returns the same instance")
    void sortsObjectArrayInPlace() {
        assertSortsAndReturnsSame(ArrayUtils.toArray("foo", "bar"));
    }

    @Test
    @DisplayName("sort(T[], Comparator) sorts using provided comparator in place and returns the same instance")
    void sortsWithExplicitComparatorInPlace() {
        assertSortsAndReturnsSameWithComparator(ArrayUtils.toArray("foo", "bar"), String::compareTo);
    }

    @Test
    @DisplayName("All overloads return null when given null")
    void returnsNullWhenInputIsNull() {
        assertNull(ArraySorter.sort((byte[]) null));
        assertNull(ArraySorter.sort((char[]) null));
        assertNull(ArraySorter.sort((double[]) null));
        assertNull(ArraySorter.sort((float[]) null));
        assertNull(ArraySorter.sort((int[]) null));
        assertNull(ArraySorter.sort((long[]) null));
        assertNull(ArraySorter.sort((short[]) null));
        assertNull(ArraySorter.sort((String[]) null));
        assertNull(ArraySorter.sort((String[]) null, String::compareTo));
    }

    @Test
    @DisplayName("Empty arrays are returned unchanged (no-op)")
    void emptyArraysAreNoOp() {
        final byte[] b = new byte[0];
        assertSame(b, ArraySorter.sort(b));
        assertArrayEquals(new byte[0], b);

        final char[] c = new char[0];
        assertSame(c, ArraySorter.sort(c));
        assertArrayEquals(new char[0], c);

        final double[] d = new double[0];
        assertSame(d, ArraySorter.sort(d));
        assertArrayEquals(new double[0], d);

        final float[] f = new float[0];
        assertSame(f, ArraySorter.sort(f));
        assertArrayEquals(new float[0], f);

        final int[] i = new int[0];
        assertSame(i, ArraySorter.sort(i));
        assertArrayEquals(new int[0], i);

        final long[] l = new long[0];
        assertSame(l, ArraySorter.sort(l));
        assertArrayEquals(new long[0], l);

        final short[] s = new short[0];
        assertSame(s, ArraySorter.sort(s));
        assertArrayEquals(new short[0], s);

        final String[] o = new String[0];
        assertSame(o, ArraySorter.sort(o));
        assertArrayEquals(new String[0], o);
        assertSame(o, ArraySorter.sort(o, String::compareTo));
    }
}