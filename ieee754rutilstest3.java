package org.apache.commons.lang3.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link IEEE754rUtils} focusing on its handling of NaN values in min/max operations.
 * This class addresses the behavior originally tested in a method named {@code testLang381},
 * which ensures that NaN values are ignored unless all values are NaN.
 */
public class IEEE754rUtilsMinMaxNaNTest extends AbstractLangTest {

    @Nested
    @DisplayName("For double values")
    class DoubleTests {

        @Test
        @DisplayName("min() should ignore NaNs and return the minimum of the other numbers")
        void testMinWithNaNs() {
            // Test with varargs
            assertEquals(1.2, IEEE754rUtils.min(1.2, 2.5, Double.NaN));

            // Test with an array containing NaNs
            final double[] arrayWithNaNs = {1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN};
            assertEquals(1.2, IEEE754rUtils.min(arrayWithNaNs));

            // Test with an array starting with NaN
            final double[] arrayWithNaNAtStart = {Double.NaN, 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN};
            assertEquals(1.2, IEEE754rUtils.min(arrayWithNaNAtStart));
        }

        @Test
        @DisplayName("max() should ignore NaNs and return the maximum of the other numbers")
        void testMaxWithNaNs() {
            // Test with varargs
            assertEquals(2.5, IEEE754rUtils.max(1.2, 2.5, Double.NaN));

            // Test with an array containing NaNs
            final double[] arrayWithNaNs = {1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN};
            assertEquals(42.0, IEEE754rUtils.max(arrayWithNaNs));

            // Test with an array starting with NaN
            final double[] arrayWithNaNAtStart = {Double.NaN, 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN};
            assertEquals(42.0, IEEE754rUtils.max(arrayWithNaNAtStart));
        }

        @Test
        @DisplayName("min() should return NaN if all values are NaN")
        void testMinWithAllNaNs() {
            assertTrue(Double.isNaN(IEEE754rUtils.min(Double.NaN, Double.NaN, Double.NaN)));
        }

        @Test
        @DisplayName("max() should return NaN if all values are NaN")
        void testMaxWithAllNaNs() {
            assertTrue(Double.isNaN(IEEE754rUtils.max(Double.NaN, Double.NaN, Double.NaN)));
        }
    }

    @Nested
    @DisplayName("For float values")
    class FloatTests {

        @Test
        @DisplayName("min() should ignore NaNs and return the minimum of the other numbers")
        void testMinWithNaNs() {
            // Test with varargs
            assertEquals(1.2f, IEEE754rUtils.min(1.2f, 2.5f, Float.NaN));

            // Test with an array containing NaNs
            final float[] arrayWithNaNs = {1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN};
            assertEquals(1.2f, IEEE754rUtils.min(arrayWithNaNs));

            // Test with an array starting with NaN
            final float[] arrayWithNaNAtStart = {Float.NaN, 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN};
            assertEquals(1.2f, IEEE754rUtils.min(arrayWithNaNAtStart));
        }

        @Test
        @DisplayName("max() should ignore NaNs and return the maximum of the other numbers")
        void testMaxWithNaNs() {
            // Test with varargs
            assertEquals(2.5f, IEEE754rUtils.max(1.2f, 2.5f, Float.NaN));

            // Test with an array containing NaNs
            final float[] arrayWithNaNs = {1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN};
            assertEquals(42.0f, IEEE754rUtils.max(arrayWithNaNs));

            // Test with an array starting with NaN
            final float[] arrayWithNaNAtStart = {Float.NaN, 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN};
            assertEquals(42.0f, IEEE754rUtils.max(arrayWithNaNAtStart));
        }

        @Test
        @DisplayName("min() should return NaN if all values are NaN")
        void testMinWithAllNaNs() {
            assertTrue(Float.isNaN(IEEE754rUtils.min(Float.NaN, Float.NaN, Float.NaN)));
        }

        @Test
        @DisplayName("max() should return NaN if all values are NaN")
        void testMaxWithAllNaNs() {
            assertTrue(Float.isNaN(IEEE754rUtils.max(Float.NaN, Float.NaN, Float.NaN)));
        }
    }
}