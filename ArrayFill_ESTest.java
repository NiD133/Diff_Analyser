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
import java.util.Arrays;
import org.apache.commons.lang3.ArrayFill;
import org.apache.commons.lang3.function.FailableIntFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class ArrayFillTest {

  @Test(timeout = 4000)
  public void testFillEmptyBooleanArray()  throws Throwable  {
      // Given
      boolean[] booleanArray = new boolean[0];
      boolean value = false;

      // When
      boolean[] filledArray = ArrayFill.fill(booleanArray, value);

      // Then
      assertEquals(0, filledArray.length);
  }

  @Test(timeout = 4000)
  public void testFillShortArray()  throws Throwable  {
      // Given
      short[] shortArray = new short[0];
      short value = 1;

      // When
      short[] filledArray = ArrayFill.fill(shortArray, value);

      // Then
      assertArrayEquals(new short[] {}, filledArray);
  }

  @Test(timeout = 4000)
  public void testFillObjectArray()  throws Throwable  {
      // Given
      Object[] objectArray = new Object[6];
      Object object = new Object();

      // When
      Object[] filledArray = ArrayFill.fill(objectArray, object);

      // Then
      assertSame(objectArray, filledArray);
  }

  @Test(timeout = 4000)
  public void testFillLongArray()  throws Throwable  {
      // Given
      long[] longArray = new long[7];
      long value = 0L;

      // When
      long[] filledArray = ArrayFill.fill(longArray, value);

      // Then
      assertSame(longArray, filledArray);
  }

  @Test(timeout = 4000)
  public void testFillIntArray()  throws Throwable  {
      // Given
      int[] intArray = new int[5];
      int value = 0;

      // When
      int[] filledArray = ArrayFill.fill(intArray, value);

      // Then
      assertArrayEquals(new int[] {0, 0, 0, 0, 0}, filledArray);
  }

  @Test(timeout = 4000)
  public void testFillFloatArray()  throws Throwable  {
      // Given
      float[] floatArray = new float[0];
      float value = 1311.7F;

      // When
      float[] filledArray = ArrayFill.fill(floatArray, value);

      // Then
      assertArrayEquals(new float[] {}, filledArray, 0.01F);
  }

  @Test(timeout = 4000)
  public void testFillDoubleArray()  throws Throwable  {
      // Given
      double[] doubleArray = new double[7];
      double value = 176.99;

      // When
      double[] filledArray = ArrayFill.fill(doubleArray, value);

      // Then
      assertArrayEquals(new double[] {176.99, 176.99, 176.99, 176.99, 176.99, 176.99, 176.99}, filledArray, 0.01);
  }

  @Test(timeout = 4000)
  public void testFillCharArray()  throws Throwable  {
      // Given
      char[] charArray = new char[0];
      char value = '>';

      // When
      char[] filledArray = ArrayFill.fill(charArray, value);

      // Then
      assertEquals(0, filledArray.length);
  }

  @Test(timeout = 4000)
  public void testFillByteArray()  throws Throwable  {
      // Given
      byte[] byteArray = new byte[0];
      byte value = (byte) (-83);

      // When
      byte[] filledArray = ArrayFill.fill(byteArray, value);

      // Then
      assertSame(byteArray, filledArray);
  }

  @Test(timeout = 4000)
  public void testFillThrowableArray()  throws Throwable  {
      // Given
      Throwable[] throwableArray = new Throwable[0];
      MockThrowable mockThrowable = new MockThrowable();

      // When
      Throwable[] filledArray = ArrayFill.fill(throwableArray, (Throwable) mockThrowable);

      // Then
      assertSame(throwableArray, filledArray);
  }

  @Test(timeout = 4000)
  public void testFillNullObjectArray()  throws Throwable  {
      // Given
      Object object = new Object();

      // When
      Object[] filledArray = ArrayFill.fill((Object[]) null, object);

      // Then
      assertNull(filledArray);
  }

  @Test(timeout = 4000)
  public void testFillObjectArrayWithFailableFunction()  throws Throwable  {
      // Given
      Object[] objectArray = new Object[2];
      FailableIntFunction<Object, Throwable> failableIntFunction = FailableIntFunction.nop();

      // When
      Object[] filledArray = ArrayFill.fill(objectArray, (FailableIntFunction<?, Throwable>) failableIntFunction);

      // Then
      assertSame(objectArray, filledArray);
  }

  @Test(timeout = 4000)
  public void testFillObjectArrayWithNullFailableFunction()  throws Throwable  {
      // Given
      Object[] objectArray = new Object[7];

      // When
      Object[] filledArray = ArrayFill.fill(objectArray, (FailableIntFunction<?, Throwable>) null);

      // Then
      assertSame(objectArray, filledArray);
  }

  @Test(timeout = 4000)
  public void testFillEmptyObjectArrayWithFailableFunction()  throws Throwable  {
      // Given
      FailableIntFunction<Object, Throwable> failableIntFunction = FailableIntFunction.nop();
      Object[] objectArray = new Object[0];

      // When
      Object[] filledArray = ArrayFill.fill(objectArray, (FailableIntFunction<?, Throwable>) failableIntFunction);

      // Then
      assertSame(objectArray, filledArray);
  }

  @Test(timeout = 4000)
  public void testFillNullObjectArrayWithFailableFunction()  throws Throwable  {
      // Given
      FailableIntFunction<Object, Throwable> failableIntFunction = FailableIntFunction.nop();

      // When
      Object[] filledArray = ArrayFill.fill((Object[]) null, (FailableIntFunction<?, Throwable>) failableIntFunction);

      // Then
      assertNull(filledArray);
  }

  @Test(timeout = 4000)
  public void testFillShortArrayWithOneElement()  throws Throwable  {
      // Given
      short[] shortArray = new short[1];
      short value = 0;

      // When
      short[] filledArray = ArrayFill.fill(shortArray, value);

      // Then
      assertArrayEquals(new short[] {(short)0}, filledArray);
  }

  @Test(timeout = 4000)
  public void testFillNullShortArray()  throws Throwable  {
      // Given
      short value = (short) (-1244);

      // When
      short[] filledArray = ArrayFill.fill((short[]) null, value);

      // Then
      assertNull(filledArray);
  }

  @Test(timeout = 4000)
  public void testFillEmptyLongArray()  throws Throwable  {
      // Given
      long[] longArray = new long[0];
      long value = 0L;

      // When
      long[] filledArray = ArrayFill.fill(longArray, value);

      // Then
      assertEquals(0, filledArray.length);
  }

  @Test(timeout = 4000)
  public void testFillNullLongArray()  throws Throwable  {
      // Given
      long value = 1822L;

      // When
      long[] filledArray = ArrayFill.fill((long[]) null, value);

      // Then
      assertNull(filledArray);
  }

  @Test(timeout = 4000)
  public void testFillEmptyIntArray()  throws Throwable  {
      // Given
      int[] intArray = new int[0];
      int value = (-1);

      // When
      int[] filledArray = ArrayFill.fill(intArray, value);

      // Then
      assertSame(intArray, filledArray);
  }

  @Test(timeout = 4000)
  public void testFillNullIntArray()  throws Throwable  {
      // Given
      int value = (-7720);

      // When
      int[] filledArray = ArrayFill.fill((int[]) null, value);

      // Then
      assertNull(filledArray);
  }

  @Test(timeout = 4000)
  public void testFillFloatArrayWithTwoElements()  throws Throwable  {
      // Given
      float[] floatArray = new float[2];
      float value = (-1.0F);

      // When
      float[] filledArray = ArrayFill.fill(floatArray, value);

      // Then
      assertArrayEquals(new float[] {(-1.0F), (-1.0F)}, filledArray, 0.01F);
  }

  @Test(timeout = 4000)
  public void testFillNullFloatArray()  throws Throwable  {
      // Given
      float value = (float) 1;

      // When
      float[] filledArray = ArrayFill.fill((float[]) null, value);

      // Then
      assertNull(filledArray);
  }

  @Test(timeout = 4000)
  public void testFillEmptyDoubleArray()  throws Throwable  {
      // Given
      double[] doubleArray = new double[0];
      double value = (double) (byte)0;

      // When
      double[] filledArray = ArrayFill.fill(doubleArray, value);

      // Then
      assertSame(filledArray, doubleArray);
  }

  @Test(timeout = 4000)
  public void testFillNullDoubleArray()  throws Throwable  {
      // Given
      double value = (double) (short) (-1244);

      // When
      double[] filledArray = ArrayFill.fill((double[]) null, value);

      // Then
      assertNull(filledArray);
  }

  @Test(timeout = 4000)
  public void testFillCharArrayWithTwoElements()  throws Throwable  {
      // Given
      char[] charArray = new char[2];
      char value = 'J';

      // When
      char[] filledArray = ArrayFill.fill(charArray, value);

      // Then
      assertArrayEquals(new char[] {'J', 'J'}, filledArray);
  }

  @Test(timeout = 4000)
  public void testFillNullCharArray()  throws Throwable  {
      // Given
      char value = 'f';

      // When
      char[] filledArray = ArrayFill.fill((char[]) null, value);

      // Then
      assertNull(filledArray);
  }

  @Test(timeout = 4000)
  public void testFillByteArrayWithOneElement()  throws Throwable  {
      // Given
      byte[] byteArray = new byte[1];
      byte value = (byte)0;

      // When
      byte[] filledArray = ArrayFill.fill(byteArray, value);

      // Then
      assertArrayEquals(new byte[] {(byte)0}, filledArray);
  }

  @Test(timeout = 4000)
  public void testFillNullByteArray()  throws Throwable  {
      // Given
      byte value = (byte) (-40);

      // When
      byte[] filledArray = ArrayFill.fill((byte[]) null, value);

      // Then
      assertNull(filledArray);
  }

  @Test(timeout = 4000)
  public void testFillBooleanArrayWithEightElements()  throws Throwable  {
      // Given
      boolean[] booleanArray = new boolean[8];
      boolean value = false;

      // When
      boolean[] filledArray = ArrayFill.fill(booleanArray, value);

      // Then
      assertTrue(Arrays.equals(new boolean[] {false, false, false, false, false, false, false, false}, filledArray));
  }

  @Test(timeout = 4000)
  public void testFillNullBooleanArray()  throws Throwable  {
      // Given
      boolean value = false;

      // When
      boolean[] filledArray = ArrayFill.fill((boolean[]) null, value);

      // Then
      assertNull(filledArray);
  }
}