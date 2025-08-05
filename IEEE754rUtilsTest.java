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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link org.apache.commons.lang3.math.IEEE754rUtils}.
 */
class IEEE754rUtilsTest extends AbstractLangTest {

    @Test
    void testConstructorForCoverage() {
        new IEEE754rUtils();
    }

    @Nested
    @DisplayName("min(float...)")
    class MinFloatTests {

        @Test
        void shouldThrowNullPointerExceptionForNullInput() {
            assertNullPointerException(() -> IEEE754rUtils.min((float[]) null), "Input array cannot be null.");
        }

        @Test
        void shouldThrowIllegalArgumentExceptionForEmptyInput() {
            assertIllegalArgumentException(IEEE754rUtils::min, "Input array cannot be empty.");
        }

        @Test
        void shouldReturnMinValueWhenVarargsContainNaN() {
            assertEquals(1.2f, IEEE754rUtils.min(1.2f, 2.5f, Float.NaN), 0.0f);
        }

        @Test
        void shouldReturnMinValueWhenArrayContainsNaN() {
            final float[] array = {1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN};
            assertEquals(1.2f, IEEE754rUtils.min(array), 0.0f);
        }

        @Test
        void shouldReturnMinValueWhenArrayStartsWithNaN() {
            final float[] array = {Float.NaN, 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN};
            assertEquals(1.2f, IEEE754rUtils.min(array), 0.0f);
        }

        @Test
        void shouldReturnNaNWhenAllInputsAreNaN() {
            assertTrue(Float.isNaN(IEEE754rUtils.min(Float.NaN, Float.NaN, Float.NaN)));
        }
    }

    @Nested
    @DisplayName("max(float...)")
    class MaxFloatTests {

        @Test
        void shouldThrowNullPointerExceptionForNullInput() {
            assertNullPointerException(() -> IEEE754rUtils.max((float[]) null), "Input array cannot be null.");
        }

        @Test
        void shouldThrowIllegalArgumentExceptionForEmptyInput() {
            assertIllegalArgumentException(IEEE754rUtils::max, "Input array cannot be empty.");
        }

        @Test
        void shouldReturnMaxValueWhenVarargsContainNaN() {
            assertEquals(2.5f, IEEE754rUtils.max(1.2f, 2.5f, Float.NaN), 0.0f);
        }

        @Test
        void shouldReturnMaxValueWhenArrayContainsNaN() {
            final float[] array = {1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN};
            assertEquals(42.0f, IEEE754rUtils.max(array), 0.0f);
        }

        @Test
        void shouldReturnMaxValueWhenArrayStartsWithNaN() {
            final float[] array = {Float.NaN, 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN};
            assertEquals(42.0f, IEEE754rUtils.max(array), 0.0f);
        }

        @Test
        void shouldReturnNaNWhenAllInputsAreNaN() {
            assertTrue(Float.isNaN(IEEE754rUtils.max(Float.NaN, Float.NaN, Float.NaN)));
        }
    }

    @Nested
    @DisplayName("min(double...)")
    class MinDoubleTests {

        @Test
        void shouldThrowNullPointerExceptionForNullInput() {
            assertNullPointerException(() -> IEEE754rUtils.min((double[]) null), "Input array cannot be null.");
        }

        @Test
        void shouldThrowIllegalArgumentExceptionForEmptyInput() {
            assertIllegalArgumentException(IEEE754rUtils::min, "Input array cannot be empty.");
        }

        @Test
        void shouldReturnMinValueWhenVarargsContainNaN() {
            assertEquals(1.2, IEEE754rUtils.min(1.2, 2.5, Double.NaN), 0.0d);
        }

        @Test
        void shouldReturnMinValueWhenArrayContainsNaN() {
            final double[] array = {1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN};
            assertEquals(1.2, IEEE754rUtils.min(array), 0.0d);
        }

        @Test
        void shouldReturnMinValueWhenArrayStartsWithNaN() {
            final double[] array = {Double.NaN, 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN};
            assertEquals(1.2, IEEE754rUtils.min(array), 0.0d);
        }

        @Test
        void shouldReturnNaNWhenAllInputsAreNaN() {
            assertTrue(Double.isNaN(IEEE754rUtils.min(Double.NaN, Double.NaN, Double.NaN)));
        }
    }

    @Nested
    @DisplayName("max(double...)")
    class MaxDoubleTests {

        @Test
        void shouldThrowNullPointerExceptionForNullInput() {
            assertNullPointerException(() -> IEEE754rUtils.max((double[]) null), "Input array cannot be null.");
        }

        @Test
        void shouldThrowIllegalArgumentExceptionForEmptyInput() {
            assertIllegalArgumentException(IEEE754rUtils::max, "Input array cannot be empty.");
        }

        @Test
        void shouldReturnMaxValueWhenVarargsContainNaN() {
            assertEquals(2.5, IEEE754rUtils.max(1.2, 2.5, Double.NaN), 0.0d);
        }

        @Test
        void shouldReturnMaxValueWhenArrayContainsNaN() {
            final double[] array = {1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN};
            assertEquals(42.0, IEEE754rUtils.max(array), 0.0d);
        }

        @Test
        void shouldReturnMaxValueWhenArrayStartsWithNaN() {
            final double[] array = {Double.NaN, 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN};
            assertEquals(42.0, IEEE754rUtils.max(array), 0.0d);
        }

        @Test
        void shouldReturnNaNWhenAllInputsAreNaN() {
            assertTrue(Double.isNaN(IEEE754rUtils.max(Double.NaN, Double.NaN, Double.NaN)));
        }
    }
}