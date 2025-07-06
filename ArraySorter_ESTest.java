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

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Comparator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

/**
 * Test suite for the ArraySorter class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ArraySorterTest {

    /**
     * Tests sorting an empty short array.
     */
    @Test(timeout = 4000)
    public void testSortEmptyShortArray() throws Throwable {
        short[] shortArray = new short[0];
        short[] sortedArray = ArraySorter.sort(shortArray);
        assertSame(shortArray, sortedArray);
    }

    /**
     * Tests sorting an array of objects with a comparator.
     */
    @Test(timeout = 4000)
    public void testSortObjectsWithComparator() throws Throwable {
        Object[] objectArray = new Object[0];
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        Object[] sortedArray = ArraySorter.sort(objectArray, comparator);
        assertSame(objectArray, sortedArray);
    }

    /**
     * Tests sorting an array of objects with duplicate values.
     */
    @Test(timeout = 4000)
    public void testSortObjectsWithDuplicates() throws Throwable {
        Object[] objectArray = new Object[7];
        objectArray[0] = (Object) 4951.0F;
        objectArray[1] = (Object) 0.0F;
        objectArray[2] = (Object) 0.0F;
        objectArray[3] = (Object) 0.0F;
        objectArray[4] = (Object) 4951.0F;
        objectArray[5] = (Object) 4951.0F;
        objectArray[6] = (Object) (-1750.1F);
        Object[] sortedArray = ArraySorter.sort(objectArray);
        assertSame(objectArray, sortedArray);
    }

    /**
     * Tests sorting an empty object array.
     */
    @Test(timeout = 4000)
    public void testSortEmptyObjectArray() throws Throwable {
        Object[] objectArray = new Object[0];
        Object[] sortedArray = ArraySorter.sort(objectArray);
        assertSame(objectArray, sortedArray);
    }

    /**
     * Tests sorting an array of long values.
     */
    @Test(timeout = 4000)
    public void testSortLongArray() throws Throwable {
        long[] longArray = new long[0];
        long[] sortedArray = ArraySorter.sort(longArray);
        assertEquals(0, sortedArray.length);
    }

    /**
     * Tests sorting an array of integers.
     */
    @Test(timeout = 4000)
    public void testSortIntArray() throws Throwable {
        int[] intArray = new int[1];
        int[] sortedArray = ArraySorter.sort(intArray);
        assertSame(intArray, sortedArray);
    }

    /**
     * Tests sorting an array of floating point numbers.
     */
    @Test(timeout = 4000)
    public void testSortFloatArray() throws Throwable {
        float[] floatArray = new float[8];
        float[] sortedArray = ArraySorter.sort(floatArray);
        assertSame(floatArray, sortedArray);
    }

    /**
     * Tests sorting an array of doubles.
     */
    @Test(timeout = 4000)
    public void testSortDoubleArray() throws Throwable {
        double[] doubleArray = new double[0];
        double[] sortedArray = ArraySorter.sort(doubleArray);
        assertArrayEquals(new double[] {}, sortedArray, 0.01);
    }

    /**
     * Tests sorting an array of characters.
     */
    @Test(timeout = 4000)
    public void testSortCharArray() throws Throwable {
        char[] charArray = new char[0];
        char[] sortedArray = ArraySorter.sort(charArray);
        assertArrayEquals(new char[] {}, sortedArray);
    }

    /**
     * Tests sorting an array of bytes.
     */
    @Test(timeout = 4000)
    public void testSortByteArray() throws Throwable {
        byte[] byteArray = new byte[0];
        byte[] sortedArray = ArraySorter.sort(byteArray);
        assertSame(byteArray, sortedArray);
    }

    /**
     * Tests sorting an array of objects with a null comparator.
     */
    @Test(timeout = 4000, expected = ClassCastException.class)
    public void testSortObjectsWithNullComparator() throws Throwable {
        Object[] objectArray = new Object[4];
        objectArray[1] = new Object();
        ArraySorter.sort(objectArray, null);
    }

    /**
     * Tests sorting an array of objects with different types.
     */
    @Test(timeout = 4000, expected = ClassCastException.class)
    public void testSortObjectsWithDifferentTypes() throws Throwable {
        Object[] objectArray = new Object[6];
        objectArray[0] = (Object) 0L;
        objectArray[1] = (Object) (short) 0;
        ArraySorter.sort(objectArray);
    }

    /**
     * Tests sorting an array of objects with a custom comparator.
     */
    @Test(timeout = 4000)
    public void testSortObjectsWithCustomComparator() throws Throwable {
        Object[] objectArray = new Object[2];
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator).compare(any(), any());
        Object[] sortedArray = ArraySorter.sort(objectArray, comparator);
        assertEquals(2, sortedArray.length);
    }

    /**
     * Tests sorting a null array with a comparator.
     */
    @Test(timeout = 4000)
    public void testSortNullArrayWithComparator() throws Throwable {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        Object[] sortedArray = ArraySorter.sort(null, comparator);
        assertNull(sortedArray);
    }

    /**
     * Tests sorting an array of objects with null values.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testSortObjectsWithNullValues() throws Throwable {
        Object[] objectArray = new Object[6];
        ArraySorter.sort(objectArray);
    }

    /**
     * Tests sorting a null array.
     */
    @Test(timeout = 4000)
    public void testSortNullArray() throws Throwable {
        Object[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray);
    }

    /**
     * Tests sorting an empty short array.
     */
    @Test(timeout = 4000)
    public void testSortEmptyShortArrayAgain() throws Throwable {
        short[] shortArray = new short[0];
        short[] sortedArray = ArraySorter.sort(shortArray);
        assertSame(shortArray, sortedArray);
    }

    /**
     * Tests sorting a null short array.
     */
    @Test(timeout = 4000)
    public void testSortNullShortArray() throws Throwable {
        short[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray);
    }

    /**
     * Tests sorting a long array.
     */
    @Test(timeout = 4000)
    public void testSortLongArrayAgain() throws Throwable {
        long[] longArray = new long[3];
        long[] sortedArray = ArraySorter.sort(longArray);
        assertSame(longArray, sortedArray);
    }

    /**
     * Tests sorting a null long array.
     */
    @Test(timeout = 4000)
    public void testSortNullLongArray() throws Throwable {
        long[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray);
    }

    /**
     * Tests sorting an empty integer array.
     */
    @Test(timeout = 4000)
    public void testSortEmptyIntArray() throws Throwable {
        int[] intArray = new int[0];
        int[] sortedArray = ArraySorter.sort(intArray);
        assertSame(intArray, sortedArray);
    }

    /**
     * Tests sorting a null integer array.
     */
    @Test(timeout = 4000)
    public void testSortNullIntArray() throws Throwable {
        int[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray);
    }

    /**
     * Tests sorting an empty float array.
     */
    @Test(timeout = 4000)
    public void testSortEmptyFloatArray() throws Throwable {
        float[] floatArray = new float[0];
        float[] sortedArray = ArraySorter.sort(floatArray);
        assertArrayEquals(new float[] {}, sortedArray, 0.01F);
    }

    /**
     * Tests sorting a null float array.
     */
    @Test(timeout = 4000)
    public void testSortNullFloatArray() throws Throwable {
        float[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray);
    }

    /**
     * Tests sorting a double array.
     */
    @Test(timeout = 4000)
    public void testSortDoubleArrayAgain() throws Throwable {
        double[] doubleArray = new double[7];
        double[] sortedArray = ArraySorter.sort(doubleArray);
        assertSame(doubleArray, sortedArray);
    }

    /**
     * Tests sorting a null double array.
     */
    @Test(timeout = 4000)
    public void testSortNullDoubleArray() throws Throwable {
        double[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray);
    }

    /**
     * Tests sorting a character array.
     */
    @Test(timeout = 4000)
    public void testSortCharArrayAgain() throws Throwable {
        char[] charArray = new char[1];
        char[] sortedArray = ArraySorter.sort(charArray);
        assertSame(charArray, sortedArray);
    }

    /**
     * Tests sorting a null character array.
     */
    @Test(timeout = 4000)
    public void testSortNullCharArray() throws Throwable {
        char[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray);
    }

    /**
     * Tests sorting a byte array.
     */
    @Test(timeout = 4000)
    public void testSortByteArrayAgain() throws Throwable {
        byte[] byteArray = new byte[1];
        byte[] sortedArray = ArraySorter.sort(byteArray);
        assertArrayEquals(new byte[] {(byte) 0}, sortedArray);
    }

    /**
     * Tests sorting a null byte array.
     */
    @Test(timeout = 4000)
    public void testSortNullByteArray() throws Throwable {
        byte[] sortedArray = ArraySorter.sort(null);
        assertNull(sortedArray);
    }

    /**
     * Tests constructing an instance of ArraySorter.
     */
    @Test(timeout = 4000)
    public void testConstructInstance() throws Throwable {
        new ArraySorter();
    }
}