package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Years_ESTest extends Years_ESTest_scaffolding {

    // Comparison Tests
    @Test(timeout = 4000)
    public void testMaxValueIsNotLessThanThree() {
        Years maxYears = Years.MAX_VALUE;
        Years threeYears = Years.THREE;
        assertFalse(maxYears.isLessThan(threeYears));
    }

    @Test(timeout = 4000)
    public void testTwoYearsIsNotLessThanNull() {
        Years twoYears = Years.TWO;
        assertFalse(twoYears.isLessThan(null));
    }

    @Test(timeout = 4000)
    public void testNegativeYearsIsNotGreaterThanMaxValue() {
        Years negativeYears = Years.years(-690);
        Years maxYears = Years.MAX_VALUE;
        assertFalse(negativeYears.isGreaterThan(maxYears));
    }

    @Test(timeout = 4000)
    public void testMinValueIsNotGreaterThanNull() {
        Years minYears = Years.MIN_VALUE;
        assertFalse(minYears.isGreaterThan(null));
    }

    @Test(timeout = 4000)
    public void testMinValueIsLessThanNull() {
        Years minYears = Years.MIN_VALUE;
        assertTrue(minYears.isLessThan(null));
    }

    @Test(timeout = 4000)
    public void testZeroYearsIsNotLessThanNull() {
        Years zeroYears = Years.ZERO;
        assertFalse(zeroYears.isLessThan(null));
    }

    @Test(timeout = 4000)
    public void testZeroYearsIsNotLessThanItself() {
        Years zeroYears = Years.ZERO;
        assertFalse(zeroYears.isLessThan(zeroYears));
    }

    @Test(timeout = 4000)
    public void testThreeYearsIsGreaterThanNegativeOneYear() {
        Years threeYears = Years.THREE;
        Years negativeOneYear = Years.years(-1);
        assertTrue(threeYears.isGreaterThan(negativeOneYear));
    }

    @Test(timeout = 4000)
    public void testMaxValueIsGreaterThanNull() {
        Years maxYears = Years.MAX_VALUE;
        assertTrue(maxYears.isGreaterThan(null));
    }

    @Test(timeout = 4000)
    public void testZeroYearsIsNotGreaterThanNull() {
        Years zeroYears = Years.ZERO;
        assertFalse(zeroYears.isGreaterThan(null));
    }

    @Test(timeout = 4000)
    public void testNegativeYearsIsNotGreaterThanItself() {
        Years negativeYears = Years.years(-690);
        assertFalse(negativeYears.isGreaterThan(negativeYears));
    }

    // Arithmetic Tests
    @Test(timeout = 4000)
    public void testPlusNullReturnsSameYears() {
        Years threeYears = Years.THREE;
        assertSame(threeYears, threeYears.plus(null));
    }

    @Test(timeout = 4000)
    public void testPlusMinValue() {
        Years minYears = Years.MIN_VALUE;
        Years twoYears = Years.TWO;
        Years result = twoYears.plus(minYears);
        assertEquals(-2147483646, result.getYears());
    }

    @Test(timeout = 4000)
    public void testPlusZero() {
        Years zeroYears = Years.ZERO;
        assertEquals(0, zeroYears.plus(0).getYears());
    }

    @Test(timeout = 4000)
    public void testMinusYears() {
        Years threeYears = Years.THREE;
        assertEquals(0, threeYears.minus(threeYears).getYears());
    }

    @Test(timeout = 4000)
    public void testMinusNullReturnsSameYears() {
        Years twoYears = Years.TWO;
        assertSame(twoYears, twoYears.minus(null));
    }

    @Test(timeout = 4000)
    public void testNegated() {
        Years twoYears = Years.TWO;
        assertEquals(-2, twoYears.negated().getYears());
    }

    @Test(timeout = 4000)
    public void testMultipliedByZero() {
        Years zeroYears = Years.ZERO;
        assertEquals(0, zeroYears.multipliedBy(0).getYears());
    }

    @Test(timeout = 4000)
    public void testMultipliedByNegative() {
        Years threeYears = Years.THREE;
        assertEquals(-3387, threeYears.multipliedBy(-1129).getYears());
    }

    @Test(timeout = 4000)
    public void testDividedByOne() {
        Years maxYears = Years.MAX_VALUE;
        assertEquals(Integer.MAX_VALUE, maxYears.dividedBy(1).getYears());
    }

    @Test(timeout = 4000)
    public void testDivideByZeroThrowsException() {
        Years oneYear = Years.ONE;
        try {
            oneYear.dividedBy(0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("/ by zero", e.getMessage());
        }
    }

    // Exception Tests
    @Test(timeout = 4000)
    public void testPlusMaxValueThrowsException() {
        Years maxYears = Years.MAX_VALUE;
        try {
            maxYears.plus(maxYears);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: 2147483647 + 2147483647", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testMinusMaxValueThrowsException() {
        Years maxYears = Years.MAX_VALUE;
        try {
            maxYears.minus(-2133);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("The calculation caused an overflow: 2147483647 + 2133", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testMultipliedByMaxValueThrowsException() {
        Years maxYears = Years.MAX_VALUE;
        try {
            maxYears.multipliedBy(3);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("Multiplication overflows an int: 2147483647 * 3", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testNegateMinValueThrowsException() {
        Years zeroYears = Years.ZERO;
        try {
            zeroYears.MIN_VALUE.negated();
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            assertEquals("Integer.MIN_VALUE cannot be negated", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testYearsBetweenNullThrowsException() {
        try {
            Years.yearsBetween(null, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("ReadableInstant objects must not be null", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testParseEmptyStringThrowsException() {
        try {
            Years.parseYears("");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format: \"\"", e.getMessage());
        }
    }

    // Utility Tests
    @Test(timeout = 4000)
    public void testParseNullStringReturnsZeroYears() {
        Years years = Years.parseYears(null);
        assertEquals(0, years.getYears());
    }

    @Test(timeout = 4000)
    public void testYearsInNullIntervalReturnsZero() {
        Years years = Years.yearsIn(null);
        assertEquals(0, years.getYears());
    }

    @Test(timeout = 4000)
    public void testGetYears() {
        Years years = Years.years(3);
        assertEquals(3, years.getYears());
    }

    @Test(timeout = 4000)
    public void testToString() {
        Years twoYears = Years.TWO;
        assertEquals("P2Y", twoYears.toString());
    }

    @Test(timeout = 4000)
    public void testGetFieldType() {
        Years zeroYears = Years.ZERO;
        assertEquals("years", zeroYears.getFieldType().getName());
    }
}