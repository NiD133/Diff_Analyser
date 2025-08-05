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
 * Test suite for FieldUtils safe arithmetic operations.
 * Tests overflow detection and boundary conditions for integer and long operations.
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

    public void testSafeAddInt() {
        // Test basic addition cases
        testBasicIntegerAddition();
        
        // Test boundary value operations that should succeed
        testIntegerAdditionBoundarySuccess();
        
        // Test overflow conditions that should throw exceptions
        testIntegerAdditionOverflow();
    }

    private void testBasicIntegerAddition() {
        assertEquals("Zero plus zero should equal zero", 
                     0, FieldUtils.safeAdd(0, 0));
        
        assertEquals("Positive plus positive", 
                     5, FieldUtils.safeAdd(2, 3));
        assertEquals("Positive plus negative", 
                     -1, FieldUtils.safeAdd(2, -3));
        assertEquals("Negative plus positive", 
                     1, FieldUtils.safeAdd(-2, 3));
        assertEquals("Negative plus negative", 
                     -5, FieldUtils.safeAdd(-2, -3));
    }

    private void testIntegerAdditionBoundarySuccess() {
        assertEquals("MAX_VALUE minus 1 should not overflow", 
                     Integer.MAX_VALUE - 1, FieldUtils.safeAdd(Integer.MAX_VALUE, -1));
        assertEquals("MIN_VALUE plus 1 should not underflow", 
                     Integer.MIN_VALUE + 1, FieldUtils.safeAdd(Integer.MIN_VALUE, 1));
        
        assertEquals("MIN_VALUE plus MAX_VALUE should equal -1", 
                     -1, FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MAX_VALUE));
        assertEquals("MAX_VALUE plus MIN_VALUE should equal -1", 
                     -1, FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    private void testIntegerAdditionOverflow() {
        assertOverflowException("MAX_VALUE + 1 should overflow", 
                               () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 1));
        assertOverflowException("MAX_VALUE + 100 should overflow", 
                               () -> FieldUtils.safeAdd(Integer.MAX_VALUE, 100));
        assertOverflowException("MAX_VALUE + MAX_VALUE should overflow", 
                               () -> FieldUtils.safeAdd(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        assertOverflowException("MIN_VALUE - 1 should underflow", 
                               () -> FieldUtils.safeAdd(Integer.MIN_VALUE, -1));
        assertOverflowException("MIN_VALUE - 100 should underflow", 
                               () -> FieldUtils.safeAdd(Integer.MIN_VALUE, -100));
        assertOverflowException("MIN_VALUE + MIN_VALUE should underflow", 
                               () -> FieldUtils.safeAdd(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    public void testSafeAddLong() {
        // Test basic addition cases
        testBasicLongAddition();
        
        // Test boundary value operations that should succeed
        testLongAdditionBoundarySuccess();
        
        // Test overflow conditions that should throw exceptions
        testLongAdditionOverflow();
    }

    private void testBasicLongAddition() {
        assertEquals("Zero plus zero should equal zero", 
                     0L, FieldUtils.safeAdd(0L, 0L));
        
        assertEquals("Positive plus positive", 
                     5L, FieldUtils.safeAdd(2L, 3L));
        assertEquals("Positive plus negative", 
                     -1L, FieldUtils.safeAdd(2L, -3L));
        assertEquals("Negative plus positive", 
                     1L, FieldUtils.safeAdd(-2L, 3L));
        assertEquals("Negative plus negative", 
                     -5L, FieldUtils.safeAdd(-2L, -3L));
    }

    private void testLongAdditionBoundarySuccess() {
        assertEquals("MAX_VALUE minus 1 should not overflow", 
                     Long.MAX_VALUE - 1, FieldUtils.safeAdd(Long.MAX_VALUE, -1L));
        assertEquals("MIN_VALUE plus 1 should not underflow", 
                     Long.MIN_VALUE + 1, FieldUtils.safeAdd(Long.MIN_VALUE, 1L));
        
        assertEquals("MIN_VALUE plus MAX_VALUE should equal -1", 
                     -1, FieldUtils.safeAdd(Long.MIN_VALUE, Long.MAX_VALUE));
        assertEquals("MAX_VALUE plus MIN_VALUE should equal -1", 
                     -1, FieldUtils.safeAdd(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    private void testLongAdditionOverflow() {
        assertOverflowException("MAX_VALUE + 1 should overflow", 
                               () -> FieldUtils.safeAdd(Long.MAX_VALUE, 1L));
        assertOverflowException("MAX_VALUE + 100 should overflow", 
                               () -> FieldUtils.safeAdd(Long.MAX_VALUE, 100L));
        assertOverflowException("MAX_VALUE + MAX_VALUE should overflow", 
                               () -> FieldUtils.safeAdd(Long.MAX_VALUE, Long.MAX_VALUE));
        
        assertOverflowException("MIN_VALUE - 1 should underflow", 
                               () -> FieldUtils.safeAdd(Long.MIN_VALUE, -1L));
        assertOverflowException("MIN_VALUE - 100 should underflow", 
                               () -> FieldUtils.safeAdd(Long.MIN_VALUE, -100L));
        assertOverflowException("MIN_VALUE + MIN_VALUE should underflow", 
                               () -> FieldUtils.safeAdd(Long.MIN_VALUE, Long.MIN_VALUE));
    }

    public void testSafeSubtractLong() {
        // Test basic subtraction cases
        testBasicLongSubtraction();
        
        // Test boundary value operations that should succeed
        testLongSubtractionBoundarySuccess();
        
        // Test overflow conditions that should throw exceptions
        testLongSubtractionOverflow();
    }

    private void testBasicLongSubtraction() {
        assertEquals("Zero minus zero should equal zero", 
                     0L, FieldUtils.safeSubtract(0L, 0L));
        
        assertEquals("Small positive minus larger positive", 
                     -1L, FieldUtils.safeSubtract(2L, 3L));
        assertEquals("Positive minus negative (addition)", 
                     5L, FieldUtils.safeSubtract(2L, -3L));
        assertEquals("Negative minus positive", 
                     -5L, FieldUtils.safeSubtract(-2L, 3L));
        assertEquals("Negative minus negative", 
                     1L, FieldUtils.safeSubtract(-2L, -3L));
    }

    private void testLongSubtractionBoundarySuccess() {
        assertEquals("MAX_VALUE minus 1 should not overflow", 
                     Long.MAX_VALUE - 1, FieldUtils.safeSubtract(Long.MAX_VALUE, 1L));
        assertEquals("MIN_VALUE minus (-1) should not underflow", 
                     Long.MIN_VALUE + 1, FieldUtils.safeSubtract(Long.MIN_VALUE, -1L));
        
        assertEquals("MIN_VALUE minus MIN_VALUE should equal 0", 
                     0, FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MIN_VALUE));
        assertEquals("MAX_VALUE minus MAX_VALUE should equal 0", 
                     0, FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MAX_VALUE));
    }

    private void testLongSubtractionOverflow() {
        assertOverflowException("MIN_VALUE - 1 should underflow", 
                               () -> FieldUtils.safeSubtract(Long.MIN_VALUE, 1L));
        assertOverflowException("MIN_VALUE - 100 should underflow", 
                               () -> FieldUtils.safeSubtract(Long.MIN_VALUE, 100L));
        assertOverflowException("MIN_VALUE - MAX_VALUE should underflow", 
                               () -> FieldUtils.safeSubtract(Long.MIN_VALUE, Long.MAX_VALUE));
        
        assertOverflowException("MAX_VALUE - (-1) should overflow", 
                               () -> FieldUtils.safeSubtract(Long.MAX_VALUE, -1L));
        assertOverflowException("MAX_VALUE - (-100) should overflow", 
                               () -> FieldUtils.safeSubtract(Long.MAX_VALUE, -100L));
        assertOverflowException("MAX_VALUE - MIN_VALUE should overflow", 
                               () -> FieldUtils.safeSubtract(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    public void testSafeMultiplyLongLong() {
        // Test basic multiplication cases
        testBasicLongMultiplication();
        
        // Test boundary value operations that should succeed
        testLongMultiplicationBoundarySuccess();
        
        // Test overflow conditions that should throw exceptions
        testLongMultiplicationOverflow();
    }

    private void testBasicLongMultiplication() {
        assertEquals("Zero times zero should equal zero", 
                     0L, FieldUtils.safeMultiply(0L, 0L));
        
        assertEquals("Identity multiplication", 
                     1L, FieldUtils.safeMultiply(1L, 1L));
        assertEquals("One times three", 
                     3L, FieldUtils.safeMultiply(1L, 3L));
        assertEquals("Three times one", 
                     3L, FieldUtils.safeMultiply(3L, 1L));
        
        assertEquals("Positive times positive", 
                     6L, FieldUtils.safeMultiply(2L, 3L));
        assertEquals("Positive times negative", 
                     -6L, FieldUtils.safeMultiply(2L, -3L));
        assertEquals("Negative times positive", 
                     -6L, FieldUtils.safeMultiply(-2L, 3L));
        assertEquals("Negative times negative", 
                     6L, FieldUtils.safeMultiply(-2L, -3L));
    }

    private void testLongMultiplicationBoundarySuccess() {
        assertEquals("MAX_VALUE times 1 should equal MAX_VALUE", 
                     Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1L));
        assertEquals("MIN_VALUE times 1 should equal MIN_VALUE", 
                     Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1L));
        assertEquals("MAX_VALUE times -1 should equal -MAX_VALUE", 
                     -Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1L));
    }

    private void testLongMultiplicationOverflow() {
        assertOverflowException("MIN_VALUE * (-1) should overflow", 
                               () -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1L));
        assertOverflowException("(-1) * MIN_VALUE should overflow", 
                               () -> FieldUtils.safeMultiply(-1L, Long.MIN_VALUE));
        assertOverflowException("MIN_VALUE * 100 should overflow", 
                               () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 100L));
        assertOverflowException("MIN_VALUE * MAX_VALUE should overflow", 
                               () -> FieldUtils.safeMultiply(Long.MIN_VALUE, Long.MAX_VALUE));
        assertOverflowException("MAX_VALUE * MIN_VALUE should overflow", 
                               () -> FieldUtils.safeMultiply(Long.MAX_VALUE, Long.MIN_VALUE));
    }

    public void testSafeMultiplyLongInt() {
        // Test basic multiplication cases
        testBasicLongIntMultiplication();
        
        // Test boundary value operations that should succeed
        testLongIntMultiplicationBoundarySuccess();
        
        // Test overflow conditions that should throw exceptions
        testLongIntMultiplicationOverflow();
    }

    private void testBasicLongIntMultiplication() {
        assertEquals("Zero times zero should equal zero", 
                     0L, FieldUtils.safeMultiply(0L, 0));
        
        assertEquals("Identity multiplication", 
                     1L, FieldUtils.safeMultiply(1L, 1));
        assertEquals("One times three", 
                     3L, FieldUtils.safeMultiply(1L, 3));
        assertEquals("Three times one", 
                     3L, FieldUtils.safeMultiply(3L, 1));
        
        assertEquals("Positive times positive", 
                     6L, FieldUtils.safeMultiply(2L, 3));
        assertEquals("Positive times negative", 
                     -6L, FieldUtils.safeMultiply(2L, -3));
        assertEquals("Negative times positive", 
                     -6L, FieldUtils.safeMultiply(-2L, 3));
        assertEquals("Negative times negative", 
                     6L, FieldUtils.safeMultiply(-2L, -3));
    }

    private void testLongIntMultiplicationBoundarySuccess() {
        assertEquals("(-1) * Integer.MIN_VALUE should work", 
                     -1L * Integer.MIN_VALUE, FieldUtils.safeMultiply(-1L, Integer.MIN_VALUE));
        
        assertEquals("MAX_VALUE times 1 should equal MAX_VALUE", 
                     Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, 1));
        assertEquals("MIN_VALUE times 1 should equal MIN_VALUE", 
                     Long.MIN_VALUE, FieldUtils.safeMultiply(Long.MIN_VALUE, 1));
        assertEquals("MAX_VALUE times -1 should equal -MAX_VALUE", 
                     -Long.MAX_VALUE, FieldUtils.safeMultiply(Long.MAX_VALUE, -1));
    }

    private void testLongIntMultiplicationOverflow() {
        assertOverflowException("MIN_VALUE * (-1) should overflow", 
                               () -> FieldUtils.safeMultiply(Long.MIN_VALUE, -1));
        assertOverflowException("MIN_VALUE * 100 should overflow", 
                               () -> FieldUtils.safeMultiply(Long.MIN_VALUE, 100));
        assertOverflowException("MIN_VALUE * Integer.MAX_VALUE should overflow", 
                               () -> FieldUtils.safeMultiply(Long.MIN_VALUE, Integer.MAX_VALUE));
        assertOverflowException("MAX_VALUE * Integer.MIN_VALUE should overflow", 
                               () -> FieldUtils.safeMultiply(Long.MAX_VALUE, Integer.MIN_VALUE));
    }

    public void testSafeDivideLongLong() {
        // Test basic division cases
        testBasicLongDivision();
        
        // Test boundary value operations that should succeed
        testLongDivisionBoundarySuccess();
        
        // Test overflow and division by zero conditions
        testLongDivisionExceptions();
    }

    private void testBasicLongDivision() {
        assertEquals("Identity division", 
                     1L, FieldUtils.safeDivide(1L, 1L));
        assertEquals("Same number division", 
                     1L, FieldUtils.safeDivide(3L, 3L));
        assertEquals("Division with remainder (truncated)", 
                     0L, FieldUtils.safeDivide(1L, 3L));
        assertEquals("Simple division", 
                     3L, FieldUtils.safeDivide(3L, 1L));
        
        // Test truncation behavior with various sign combinations
        assertEquals("5 / 3 = 1 (truncated)", 
                     1L, FieldUtils.safeDivide(5L, 3L));
        assertEquals("5 / -3 = -1 (truncated)", 
                     -1L, FieldUtils.safeDivide(5L, -3L));
        assertEquals("-5 / 3 = -1 (truncated)", 
                     -1L, FieldUtils.safeDivide(-5L, 3L));
        assertEquals("-5 / -3 = 1 (truncated)", 
                     1L, FieldUtils.safeDivide(-5L, -3L));
        
        // Test exact division
        assertEquals("6 / 3 = 2", 
                     2L, FieldUtils.safeDivide(6L, 3L));
        assertEquals("6 / -3 = -2", 
                     -2L, FieldUtils.safeDivide(6L, -3L));
        assertEquals("-6 / 3 = -2", 
                     -2L, FieldUtils.safeDivide(-6L, 3L));
        assertEquals("-6 / -3 = 2", 
                     2L, FieldUtils.safeDivide(-6L, -3L));
        
        // Test division with different remainders
        assertEquals("7 / 3 = 2 (truncated)", 
                     2L, FieldUtils.safeDivide(7L, 3L));
        assertEquals("7 / -3 = -2 (truncated)", 
                     -2L, FieldUtils.safeDivide(7L, -3L));
        assertEquals("-7 / 3 = -2 (truncated)", 
                     -2L, FieldUtils.safeDivide(-7L, 3L));
        assertEquals("-7 / -3 = 2 (truncated)", 
                     2L, FieldUtils.safeDivide(-7L, -3L));
    }

    private void testLongDivisionBoundarySuccess() {
        assertEquals("MAX_VALUE / 1 should equal MAX_VALUE", 
                     Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L));
        assertEquals("MIN_VALUE / 1 should equal MIN_VALUE", 
                     Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L));
        assertEquals("MAX_VALUE / -1 should equal -MAX_VALUE", 
                     -Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L));
    }

    private void testLongDivisionExceptions() {
        assertOverflowException("MIN_VALUE / (-1) should overflow", 
                               () -> FieldUtils.safeDivide(Long.MIN_VALUE, -1L));
        assertOverflowException("Division by zero should throw exception", 
                               () -> FieldUtils.safeDivide(1L, 0L));
    }

    public void testSafeDivideRoundingModeLong() {
        // Test exact division
        assertEquals("Exact division with UNNECESSARY rounding", 
                     3L, FieldUtils.safeDivide(15L, 5L, RoundingMode.UNNECESSARY));
        
        // Test different rounding modes with 179 / 3 = 59.666...
        assertEquals("179 / 3 with FLOOR rounding should be 59", 
                     59L, FieldUtils.safeDivide(179L, 3L, RoundingMode.FLOOR));
        assertEquals("179 / 3 with CEILING rounding should be 60", 
                     60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.CEILING));
        assertEquals("179 / 3 with HALF_UP rounding should be 60", 
                     60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_UP));
        assertEquals("-179 / 3 with HALF_UP rounding should be -60", 
                     -60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_UP));
        assertEquals("179 / 3 with HALF_DOWN rounding should be 60", 
                     60L, FieldUtils.safeDivide(179L, 3L, RoundingMode.HALF_DOWN));
        assertEquals("-179 / 3 with HALF_DOWN rounding should be -60", 
                     -60L, FieldUtils.safeDivide(-179L, 3L, RoundingMode.HALF_DOWN));

        // Test boundary values with rounding
        assertEquals("MAX_VALUE / 1 with UNNECESSARY rounding", 
                     Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, 1L, RoundingMode.UNNECESSARY));
        assertEquals("MIN_VALUE / 1 with UNNECESSARY rounding", 
                     Long.MIN_VALUE, FieldUtils.safeDivide(Long.MIN_VALUE, 1L, RoundingMode.UNNECESSARY));
        assertEquals("MAX_VALUE / -1 with UNNECESSARY rounding", 
                     -Long.MAX_VALUE, FieldUtils.safeDivide(Long.MAX_VALUE, -1L, RoundingMode.UNNECESSARY));

        // Test overflow and division by zero with rounding modes
        assertOverflowException("MIN_VALUE / (-1) should overflow even with rounding", 
                               () -> FieldUtils.safeDivide(Long.MIN_VALUE, -1L, RoundingMode.UNNECESSARY));
        assertOverflowException("Division by zero should throw exception even with rounding", 
                               () -> FieldUtils.safeDivide(1L, 0L, RoundingMode.UNNECESSARY));
    }

    /**
     * Helper method to assert that an ArithmeticException is thrown for overflow conditions.
     * Uses a functional interface to make the test code more readable.
     */
    private void assertOverflowException(String message, Runnable operation) {
        try {
            operation.run();
            fail(message + " - Expected ArithmeticException was not thrown");
        } catch (ArithmeticException e) {
            // Expected exception - test passes
        }
    }

    /**
     * Functional interface for operations that might throw ArithmeticException.
     * This makes the test code more readable by allowing lambda expressions.
     */
    @FunctionalInterface
    private interface Runnable {
        void run();
    }
}