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
package org.apache.commons.lang3.math;

import static org.apache.commons.lang3.LangAssertions.assertIllegalArgumentException;
import static org.apache.commons.lang3.LangAssertions.assertNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link org.apache.commons.lang3.math.IEEE754rUtils}.
 * 
 * IEEE754r standard specifies that NaN values should be ignored when finding min/max,
 * and NaN is only returned if all values are NaN.
 */
class IEEE754rUtilsTest extends AbstractLangTest {

    private static final double DELTA = 0.01;

    @Test
    void testConstructorExists() {
        new IEEE754rUtils();
    }

    @Test
    void testNullAndEmptyArraysThrowExceptions() {
        // Test null float arrays
        assertNullPointerException(() -> IEEE754rUtils.min((float[]) null), 
            "min() should throw NullPointerException for null float array");
        assertNullPointerException(() -> IEEE754rUtils.max((float[]) null), 
            "max() should throw NullPointerException for null float array");
        
        // Test empty float arrays
        assertIllegalArgumentException(() -> IEEE754rUtils.min(new float[0]), 
            "min() should throw IllegalArgumentException for empty float array");
        assertIllegalArgumentException(() -> IEEE754rUtils.max(new float[0]), 
            "max() should throw IllegalArgumentException for empty float array");

        // Test null double arrays
        assertNullPointerException(() -> IEEE754rUtils.min((double[]) null), 
            "min() should throw NullPointerException for null double array");
        assertNullPointerException(() -> IEEE754rUtils.max((double[]) null), 
            "max() should throw NullPointerException for null double array");
        
        // Test empty double arrays
        assertIllegalArgumentException(() -> IEEE754rUtils.min(new double[0]), 
            "min() should throw IllegalArgumentException for empty double array");
        assertIllegalArgumentException(() -> IEEE754rUtils.max(new double[0]), 
            "max() should throw IllegalArgumentException for empty double array");
    }

    @Test
    void testNaNHandlingWithMultipleValues() {
        // Test double values with NaN - NaN should be ignored when other values present
        assertEquals(1.2, IEEE754rUtils.min(1.2, 2.5, Double.NaN), DELTA,
            "min() should ignore NaN and return the smallest non-NaN value");
        assertEquals(2.5, IEEE754rUtils.max(1.2, 2.5, Double.NaN), DELTA,
            "max() should ignore NaN and return the largest non-NaN value");
        
        // Test float values with NaN - NaN should be ignored when other values present
        assertEquals(1.2f, IEEE754rUtils.min(1.2f, 2.5f, Float.NaN), DELTA,
            "min() should ignore NaN and return the smallest non-NaN value");
        assertEquals(2.5f, IEEE754rUtils.max(1.2f, 2.5f, Float.NaN), DELTA,
            "max() should ignore NaN and return the largest non-NaN value");
    }

    @Test
    void testAllNaNValuesReturnNaN() {
        // When all values are NaN, the result should be NaN
        assertTrue(Double.isNaN(IEEE754rUtils.max(Double.NaN, Double.NaN, Double.NaN)),
            "max() should return NaN when all double values are NaN");
        assertTrue(Float.isNaN(IEEE754rUtils.max(Float.NaN, Float.NaN, Float.NaN)),
            "max() should return NaN when all float values are NaN");
    }

    @Test
    void testArraysWithNaNValues() {
        // Test double arrays with NaN values mixed in
        final double[] doubleArrayWithNaN = { 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN };
        assertEquals(42.0, IEEE754rUtils.max(doubleArrayWithNaN), DELTA,
            "max() should find largest value ignoring NaN elements");
        assertEquals(1.2, IEEE754rUtils.min(doubleArrayWithNaN), DELTA,
            "min() should find smallest value ignoring NaN elements");

        // Test double arrays with NaN at beginning and end
        final double[] doubleArrayNaNAtEnds = { Double.NaN, 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN };
        assertEquals(42.0, IEEE754rUtils.max(doubleArrayNaNAtEnds), DELTA,
            "max() should handle NaN values at any position in array");
        assertEquals(1.2, IEEE754rUtils.min(doubleArrayNaNAtEnds), DELTA,
            "min() should handle NaN values at any position in array");

        // Test float arrays with NaN values mixed in
        final float[] floatArrayWithNaN = { 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN };
        assertEquals(1.2f, IEEE754rUtils.min(floatArrayWithNaN), DELTA,
            "min() should find smallest float value ignoring NaN elements");
        assertEquals(42.0f, IEEE754rUtils.max(floatArrayWithNaN), DELTA,
            "max() should find largest float value ignoring NaN elements");

        // Test float arrays with NaN at beginning and end
        final float[] floatArrayNaNAtEnds = { Float.NaN, 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN };
        assertEquals(1.2f, IEEE754rUtils.min(floatArrayNaNAtEnds), DELTA,
            "min() should handle NaN values at any position in float array");
        assertEquals(42.0f, IEEE754rUtils.max(floatArrayNaNAtEnds), DELTA,
            "max() should handle NaN values at any position in float array");
    }
}