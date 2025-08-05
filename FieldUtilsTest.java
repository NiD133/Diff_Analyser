/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.field;

import java.math.RoundingMode;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 *
 * @author Brian S O'Neill
 */
public class TestFieldUtils extends TestCase {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestFieldUtils.class);
    }

    public TestFieldUtils(String name) {
        super(name);
    }

    // Helper method for exception testing
    private void assertThrowsArithmeticException(Runnable operation) {
        try {
            operation.run();
            fail("Expected ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    //-----------------------------------------------------------------------
    public void testSafeAddInt_zeros() {
        assertEquals(0, FieldUtils.safeAdd(0, 0));
    }

    public void testSafeAddInt_positiveAndPositive() {
        assertEquals(5, FieldUtils.safeAdd(2, 3));
    }

    public void testSafeAddInt_positiveAndNegative() {
        assertEquals(-1, FieldUtils.safeAdd(2, -3));
    }

    public void testSafeAddInt_negativeAndPositive() {
        assertEquals(1, FieldUtils.safeAdd(-2, 3));
    }

    public void testSafeAddInt_negativeAndNegative() {
        assertEquals(-5, FieldUtils.safeAdd(-2, -3));
    }

    public void testSafeAddInt_maxMinusOne() {
        assertEquals(Integer.MAX_VALUE - 1, FieldUtils.safeAdd(Integer.MAX_VALUE, -1));
    }

    public void testSafeAddInt_minPlusOne() {
        assertEquals(Integer.MIN_VALUE + 1, FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
    }

    public void testSafeAddInt_minAndMax() {
        assertEquals(-1, FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertEquals(-1, FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    public void testSafeAddInt_overflowMax() {
        assertThrowsArithmeticException(() -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertThrowsArithmeticException(() -> FieldUtils.safeAdd(Integer.MAX_VALUE, 100));
        assertThrowsArithmeticException(() -> FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    public void testSafeAddInt_underflowMin() {
        assertThrowsArithmeticException(() -> FieldUtils.safeAdd(Integer.MIN_VALUE, -1));
        assertThrowsArithmeticException(() -> FieldUtils.safeAdd(Integer.MIN_VALUE, -100));
        assertThrowsArithmeticException(() -> FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    //-----------------------------------------------------------------------
    public void testSafeAddLong_zeros() {
        assertEquals(0L, FieldUtils.safeAdd(0L, 0L));
    }

    public void testSafeAddLong_positiveAndPositive() {
        assertEquals(5L, FieldUtils.safeAdd(2L, 3L));
    }

    public void testSafeAddLong_positiveAndNegative() {
        assertEquals(-1L, FieldUtils.safeAdd(2L, -3L));
    }

    public void testSafeAddLong_negativeAndPositive() {
        assertEquals(1L, FieldUtils.safeAdd(-2L, 3L));
    }

    public void testSafeAddLong_negativeAndNegative() {
        assertEquals(-5L, FieldUtils.safeAdd(-2L, -3L));
    }

    public void testSafeAddLong_maxMinusOne() {
        assertEquals(Long.MAX_VALUE - 1, FieldUtils.safeAdd(Long.MAX_VALUE, -1L));
    }

    public void testSafeAddLong_minPlusOne() {
        assertEquals(Long.MIN_VALUE + 1, FieldUtils.safeAdd(Long.MIN_VALUE, 1L));
    }

    public void testSafeAddLong_minAndMax() {
        assertEquals(-1, FieldUtils.safeAdd(Long.MIN_VALUE, Long.MAX_VALUE));
        assertEquals(-1, FieldUtils.safeAdd(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    public void testSafeAddLong_overflowMax() {
        assertThrowsArithmeticException(() -> FieldUtils.safeAdd(Long.MAX_VALUE, 1L));
        assertThrowsArithmeticException(() -> FieldUtils.safeAdd(Long.MAX_VALUE, 100L));
        assertThrowsArithmeticException(() -> FieldUtils.safeAdd(Long.MAX_VALUE, Long.MAX_VALUE));
    }

    public void testSafeAddLong_underflowMin() {
        assertThrowsArithmeticException(() -> FieldUtils.safeAdd(Long.MIN_VALUE, -1L));
        assertThrowsArithmeticException(() -> FieldUtils.safeAdd(Long.MIN_VALUE, -100L));
        assertThrowsArithmeticException(() -> FieldUtils.safeAdd(Long.MIN_VALUE, Long.MIN_VALUE));
    }

    //-----------------------------------------------------------------------
    public void testSafeSubtractLong_zeros() {
        assertEquals(0L, FieldUtils.safeSubtract(0L, 0L));
    }

    public void testSafeSubtractLong_positiveMinusPositive() {
        assertEquals(-1L, FieldUtils.safeSubtract(2L, 3L));
    }

    public void testSafeSubtractLong_positiveMinusNegative() {
        assertEquals(5L, FieldUtils.safeSubtract(2L, -3L));
    }

    public void testSafeSubtractLong_negativeMinusPositive() {
        assertEquals(-5L, FieldUtils.safeSubtract(-2L, 3L));
    }

    public void testSafeSubtractLong_negativeMinusNegative() {
        assertEquals(1L, FieldUtils.safeSubtract(-2L, -3L));
    }

    public void testSafeSubtractLong_maxMinusOne() {
        assertEquals(Long.MAX_VALUE - 1, FieldUtils.safeSubtract(Long.MAX_VALUE, 1L));
    }

    public void testSafeSubtractLong_minMinusNegativeOne() {
        assertEquals(Long.MIN_VALUE + 1, FieldUtils.safeSubtract(Long.MIN_VALUE, -1L));
    }

    public void testSafeSubtractLong_minMinusMin() {
        assertEquals(0, FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MIN_VALUE));
    }

    public void testSafeSubtractLong_maxMinusMax() {
        assertEquals(0, FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MAX_VALUE));
    }

    public void testSafeSubtractLong_underflowMin() {
        assertThrowsArithmeticException(() -> FieldUtils.safeSubtract(Long.MIN_VALUE, 1L));
        assertThrowsArithmeticException(() -> FieldUtils.safeSubtract(Long.MIN_VALUE, 100L));
        assertThrowsArithmeticException(() -> FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MAX_VALUE));
    }

    public void testSafeSubtractLong_overflowMax() {
        assertThrowsArithmeticException(() -> FieldUtils.safeSubtract(Long.MAX_VALUE, -1L));
        assertThrowsArithmeticException(() -> FieldUtils.safeSubtract(Long.MAX_VALUE, -100L));
        assertThrowsArithmeticException(() -> FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    //-----------------------------------------------------------------------
    public void testSafeMultiplyLongLong_zeros() {
        assertEquals(0L, FieldUtils.safeMultiply(0L, 0L));
    }

    public void testSafeMultiplyLongLong_ones() {
        assertEquals(1L, FieldUtils.safeMultiply(1L, 1L));
    }

    public void testSafeMultiplyLongLong_positiveAndPositive() {
        assertEquals(6L, FieldUtils.safeMultiply(2L, 3L));
    }

    public void testSafeMultiplyLongLong_positiveAndNegative() {
        assertEquals(-6L, FieldUtils.safeMultiply(2L, -3L));
    }

    public void testSafeMultiplyLongLong_negativeAndPositive() {
        assertEquals(-6L, FieldUtils.safeMultiply(-2L, 3L));
    }

    public void testSafeMultiplyLongLong_negativeAndNegative() {
        assertEquals(6L, FieldUtils.safeMultiply(-2L, -3L));
    }

    public void testSafeMultiplyLongLong_maxByOne() {
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1L));
    }

    public void testSafeMultiplyLongLong_minByOne() {
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1L));
    }

    public void testSafeMultiplyLongLong_maxByNegativeOne() {
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1L));
    }

    public void testSafeMultiplyLongLong_minByNegativeOne() {
        assertThrowsArithmeticException(() -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1L));
    }

    public void testSafeMultiplyLongLong_minByNegativeOneReversed() {
        assertThrowsArithmeticException(() -> FieldUtils.safeMultiply(-1L, Long.MIN_VALUE));
    }

    public void testSafeMultiplyLongLong_overflowScenarios() {
        assertThrowsArithmeticException(() -> FieldUtils.safeMultiply(Long.MIN_VALUE, 100L));
        assertThrowsArithmeticException(() -> FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MAX_VALUE));
        assertThrowsArithmeticException(() -> FieldUtils.safeMultiply(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    //-----------------------------------------------------------------------
    public void testSafeMultiplyLongInt_zeros() {
        assertEquals(0L, FieldUtils.safeMultiply(0L, 0));
    }

    public void testSafeMultiplyLongInt_ones() {
        assertEquals(1L, FieldUtils.safeMultiply(1L, 1));
    }

    public void testSafeMultiplyLongInt_positiveAndPositive() {
        assertEquals(6L, FieldUtils.safeMultiply(2L, 3));
    }

    public void testSafeMultiplyLongInt_positiveAndNegative() {
        assertEquals(-6L, FieldUtils.safeMultiply(2L, -3));
    }

    public void testSafeMultiplyLongInt_negativeAndPositive() {
        assertEquals(-6L, FieldUtils.safeMultiply(-2L, 3));
    }

    public void testSafeMultiplyLongInt_negativeAndNegative() {
        assertEquals(6L, FieldUtils.safeMultiply(-2L, -3));
    }

    public void testSafeMultiplyLongInt_minIntByNegativeOne() {
        assertEquals(-1L * Integer.MIN_VALUE, FieldUtils.safeMultiply(-1L, Integer.MIN_VALUE));
    }

    public void testSafeMultiplyLongInt_maxByOne() {
        assertEquals(Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
    }

    public void testSafeMultiplyLongInt_minByOne() {
        assertEquals(Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1));
    }

    public void testSafeMultiplyLongInt_maxByNegativeOne() {
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1));
    }

    public void testSafeMultiplyLongInt_minByNegativeOne() {
        assertThrowsArithmeticException(() -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1));
    }

    public void testSafeMultiplyLongInt_overflowScenarios() {
        assertThrowsArithmeticException(() -> FieldUtils.safeMultiply(Long.MIN_VALUE, 100));
        assertThrowsArithmeticException(() -> FieldUtils.safeMultiply(Long.MIN_VALUE, Integer.MAX_VALUE));
        assertThrowsArithmeticException(() -> FieldUtils.safeMultiply(Long.MAX_VALUE, Integer.MIN_VALUE));
    }

    //-----------------------------------------------------------------------
    public void testSafeDivideLongLong_basicDivision() {
        assertEquals(1L, FieldUtils.safeDivide(1L, 1L));
        assertEquals(0L, FieldUtils.safeDivide(1L, 3L));
        assertEquals(3L, FieldUtils.safeDivide(3L, 1L));
    }

    public void testSafeDivideLongLong_positiveDividend() {
        assertEquals(1L, FieldUtils.safeDivide(5L, 3L));
        assertEquals(-1L, FieldUtils.safeDivide(5L, -3L));
        assertEquals(2L, FieldUtils.safeDivide(6L, 3L));
        assertEquals(-2L, FieldUtils.safeDivide(6L, -3L));
        assertEquals(2L, FieldUtils.safeDivide(7L, 3L));
        assertEquals(-2L, FieldUtils.safeDivide(7L, -3L));
    }

    public void testSafeDivideLongLong_negativeDividend() {
        assertEquals(-1L, FieldUtils.safeDivide(-5L, 3L));
        assertEquals(1L, FieldUtils.safeDivide(-5L, -3L));
        assertEquals(-2L, FieldUtils.safeDivide(-6L, 3L));
        assertEquals(2L, FieldUtils.safeDivide(-6L, -3L));
        assertEquals(-2L, FieldUtils.safeDivide(-7L, 3L));
        assertEquals(2L, FieldUtils.safeDivide(-7L, -3L));
    }

    public void testSafeDivideLongLong_edgeCases() {
        assertEquals(Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L));
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L));
    }

    public void testSafeDivideLongLong_minDividedByNegativeOne() {
        assertThrowsArithmeticException(() -> FieldUtils.safeDivide(Long.MIN_VALUE, -1L));
    }

    public void testSafeDivideLongLong_divideByZero() {
        assertThrowsArithmeticException(() -> FieldUtils.safeDivide(1L, 0L));
    }

    //-----------------------------------------------------------------------
    public void testSafeDivideRoundingModeLong_basicDivision() {
        assertEquals(3L, FieldUtils.safeDivide(15L, 5L, RoundingMode.UNNECESSARY));
    }

    public void testSafeDivideRoundingModeLong_roundingModes() {
        assertEquals(59L, FieldUtils.safeDivide(179L, 3L, RoundingMode.FLOOR));
        assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.CEILING));
        assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_UP));
        assertEquals(-60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_UP));
        assertEquals(60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_DOWN));
        assertEquals(-60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_DOWN));
    }

    public void testSafeDivideRoundingModeLong_edgeCases() {
        assertEquals(Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L, RoundingMode.UNNECESSARY));
        assertEquals(Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L, RoundingMode.UNNECESSARY));
        assertEquals(-Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L, RoundingMode.UNNECESSARY));
    }

    public void testSafeDivideRoundingModeLong_minDividedByNegativeOne() {
        assertThrowsArithmeticException(() -> FieldUtils.safeDivide(Long.MIN_VALUE, -1L, RoundingMode.UNNECESSARY));
    }

    public void testSafeDivideRoundingModeLong_divideByZero() {
        assertThrowsArithmeticException(() -> FieldUtils.safeDivide(1L, 0L, RoundingMode.UNNECESSARY));
    }

}