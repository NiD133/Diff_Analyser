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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link org.apache.commons.lang3.math.IEEE754rUtils}.
 */
class IEEE754rUtilsTest extends AbstractLangTest {

    @Test
    void testConstructorExists() {
        new IEEE754rUtils();
    }

    // Exception Tests
    // ===============

    @Test
    void testMinDoubleArrayThrowsExceptions() {
        // Test null input
        assertNullPointerException(() -> IEEE754rUtils.min((double[]) null), 
            "NullPointerException expected for null input");
        
        // Test empty array
        assertIllegalArgumentException(() -> IEEE754rUtils.min(new double[0]), 
            "IllegalArgumentException expected for empty input");
    }

    @Test
    void testMaxDoubleArrayThrowsExceptions() {
        // Test null input
        assertNullPointerException(() -> IEEE754rUtils.max((double[]) null), 
            "NullPointerException expected for null input");
        
        // Test empty array
        assertIllegalArgumentException(() -> IEEE754rUtils.max(new double[0]), 
            "IllegalArgumentException expected for empty input");
    }

    @Test
    void testMinFloatArrayThrowsExceptions() {
        // Test null input
        assertNullPointerException(() -> IEEE754rUtils.min((float[]) null), 
            "NullPointerException expected for null input");
        
        // Test empty array
        assertIllegalArgumentException(() -> IEEE754rUtils.min(new float[0]), 
            "IllegalArgumentException expected for empty input");
    }

    @Test
    void testMaxFloatArrayThrowsExceptions() {
        // Test null input
        assertNullPointerException(() -> IEEE754rUtils.max((float[]) null), 
            "NullPointerException expected for null input");
        
        // Test empty array
        assertIllegalArgumentException(() -> IEEE754rUtils.max(new float[0]), 
            "IllegalArgumentException expected for empty input");
    }

    // Double Tests
    // ============

    @Test
    void testMinDoubleWithNaNInArguments() {
        // NaN should be ignored when determining min value
        assertEquals(1.2, IEEE754rUtils.min(1.2, 2.5, Double.NaN), 0.01);
    }

    @Test
    void testMaxDoubleWithNaNInArguments() {
        // NaN should be ignored when determining max value
        assertEquals(2.5, IEEE754rUtils.max(1.2, 2.5, Double.NaN), 0.01);
    }

    @Test
    void testMaxDoubleWithAllNaN() {
        // Should return NaN when all values are NaN
        assertTrue(Double.isNaN(IEEE754rUtils.max(Double.NaN, Double.NaN, Double.NaN)));
    }

    @Test
    void testMinDoubleArrayWithNaN() {
        final double[] arrayA = { 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN };
        final double[] arrayB = { Double.NaN, 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN };

        // NaN values should be ignored when determining min value
        assertAll(
            () -> assertEquals(1.2, IEEE754rUtils.min(arrayA), 0.01, "Array A"),
            () -> assertEquals(1.2, IEEE754rUtils.min(arrayB), 0.01, "Array B")
        );
    }

    @Test
    void testMaxDoubleArrayWithNaN() {
        final double[] arrayA = { 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN };
        final double[] arrayB = { Double.NaN, 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN };

        // NaN values should be ignored when determining max value
        assertAll(
            () -> assertEquals(42.0, IEEE754rUtils.max(arrayA), 0.01, "Array A"),
            () -> assertEquals(42.0, IEEE754rUtils.max(arrayB), 0.01, "Array B")
        );
    }

    // Float Tests
    // ===========

    @Test
    void testMinFloatWithNaNInArguments() {
        // NaN should be ignored when determining min value
        assertEquals(1.2f, IEEE754rUtils.min(1.2f, 2.5f, Float.NaN), 0.01f);
    }

    @Test
    void testMaxFloatWithNaNInArguments() {
        // NaN should be ignored when determining max value
        assertEquals(2.5f, IEEE754rUtils.max(1.2f, 2.5f, Float.NaN), 0.01f);
    }

    @Test
    void testMaxFloatWithAllNaN() {
        // Should return NaN when all values are NaN
        assertTrue(Float.isNaN(IEEE754rUtils.max(Float.NaN, Float.NaN, Float.NaN)));
    }

    @Test
    void testMinFloatArrayWithNaN() {
        final float[] arrayA = { 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN };
        final float[] arrayB = { Float.NaN, 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN };

        // NaN values should be ignored when determining min value
        assertAll(
            () -> assertEquals(1.2f, IEEE754rUtils.min(arrayA), 0.01f, "Array A"),
            () -> assertEquals(1.2f, IEEE754rUtils.min(arrayB), 0.01f, "Array B")
        );
    }

    @Test
    void testMaxFloatArrayWithNaN() {
        final float[] arrayA = { 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN };
        final float[] arrayB = { Float.NaN, 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN };

        // NaN values should be ignored when determining max value
        assertAll(
            () -> assertEquals(42.0f, IEEE754rUtils.max(arrayA), 0.01f, "Array A"),
            () -> assertEquals(42.0f, IEEE754rUtils.max(arrayB), 0.01f, "Array B")
        );
    }
}