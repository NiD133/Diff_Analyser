package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.Partial;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadableInterval;
import org.joda.time.ReadablePartial;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class Years_ESTest extends Years_ESTest_scaffolding {

    // Test getYears() method
    @Test(timeout = 4000)
    public void getYears_returnsCorrectValue() {
        assertEquals(3, Years.THREE.getYears());
        assertEquals(2, Years.TWO.getYears());
        assertEquals(1, Years.ONE.getYears());
        assertEquals(0, Years.ZERO.getYears());
        assertEquals(-690, Years.years(-690).getYears());
        assertEquals(Integer.MAX_VALUE, Years.MAX_VALUE.getYears());
        assertEquals(Integer.MIN_VALUE, Years.MIN_VALUE.getYears());
    }

    // Test isLessThan() method
    @Test(timeout = 4000)
    public void isLessThan_whenGreaterValue_returnsFalse() {
        assertFalse(Years.MAX_VALUE.isLessThan(Years.THREE));
    }

    @Test(timeout = 4000)
    public void isLessThan_whenNull_returnsFalse() {
        assertFalse(Years.TWO.isLessThan(null));
        assertFalse(Years.ZERO.isLessThan(null));
    }

    @Test(timeout = 4000)
    public void isLessThan_whenSameValue_returnsFalse() {
        assertFalse(Years.ZERO.isLessThan(Years.ZERO));
    }

    @Test(timeout = 4000)
    public void isLessThan_whenSmallerValue_returnsTrue() {
        assertTrue(Years.MIN_VALUE.isLessThan(null));
        Years years0 = Years.ZERO;
        Years years1 = Years.TWO.plus(2); // 4 years
        assertTrue(years0.isLessThan(years1));
    }

    // Test isGreaterThan() method
    @Test(timeout = 4000)
    public void isGreaterThan_whenSmallerValue_returnsFalse() {
        Years years0 = Years.years(-690);
        assertFalse(years0.isGreaterThan(Years.MAX_VALUE));
        assertFalse(years0.isGreaterThan(years0));
    }

    @Test(timeout = 4000)
    public void isGreaterThan_whenNull_returnsExpected() {
        assertFalse(Years.MIN_VALUE.isGreaterThan(null));
        assertFalse(Years.ZERO.isGreaterThan(null));
        assertTrue(Years.MAX_VALUE.isGreaterThan(null));
    }

    @Test(timeout = 4000)
    public void isGreaterThan_whenLargerValue_returnsTrue() {
        Years years0 = Years.THREE;
        Years years1 = Years.years(-1);
        assertTrue(years0.isGreaterThan(years1));
    }

    // Test plus() methods
    @Test(timeout = 4000)
    public void plus_withNull_returnsSameInstance() {
        assertSame(Years.THREE, Years.THREE.plus((Years) null));
        assertEquals(0, Years.ZERO.plus((Years) null).getYears());
    }

    @Test(timeout = 4000)
    public void plus_withYears_addsCorrectly() {
        Years result = Years.TWO.plus(Years.MIN_VALUE);
        assertEquals(-2147483646, result.getYears());
    }

    @Test(timeout = 4000)
    public void plus_withInt_addsCorrectly() {
        assertEquals(0, Years.ZERO.plus(0).getYears());
        Years years1 = Years.TWO.plus(2);
        assertEquals(4, years1.getYears());
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void plus_withOverflow_throwsException() {
        Years.MAX_VALUE.plus(80);
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void plus_withMaxValueAddition_throwsException() {
        Years.MAX_VALUE.plus(Years.MAX_VALUE);
    }

    // Test minus() methods
    @Test(timeout = 4000)
    public void minus_withNull_returnsSameInstance() {
        assertSame(Years.TWO, Years.TWO.minus((Years) null));
    }

    @Test(timeout = 4000)
    public void minus_withYears_subtractsCorrectly() {
        Years result = Years.THREE.minus(Years.THREE);
        assertEquals(0, result.getYears());
        
        Years years0 = Years.TWO;
        Years years1 = years0.minus(1612);
        assertEquals(-1612, years1.getYears());
    }

    @Test(timeout = 4000)
    public void minus_withInt_subtractsCorrectly() {
        Years result = Years.years(6488).minus(6488);
        assertEquals(0, result.getYears());
        assertEquals(3, Years.THREE.minus(0).getYears());
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void minus_withMinValueSubtraction_throwsException() {
        Years.MIN_VALUE.minus(Years.MIN_VALUE);
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void minus_withUnderflow_throwsException() {
        Years.MIN_VALUE.minus(1);
    }

    // Test multipliedBy() method
    @Test(timeout = 4000)
    public void multipliedBy_calculatesCorrectly() {
        assertEquals(0, Years.ZERO.multipliedBy(0).getYears());
        Years years0 = Years.years(-1249);
        Years years1 = years0.multipliedBy(-1249);
        assertEquals(1560001, years1.getYears());
        assertEquals(-3387, Years.THREE.multipliedBy(-1129).getYears());
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void multipliedBy_withOverflow_throwsException() {
        Years.MAX_VALUE.multipliedBy(3);
    }

    // Test dividedBy() method
    @Test(timeout = 4000)
    public void dividedBy_calculatesCorrectly() {
        assertSame(Years.ZERO, Years.ZERO.dividedBy(1));
        Years result = Years.MAX_VALUE.dividedBy(-592);
        assertEquals(-3627506, result.getYears());
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void dividedBy_byZero_throwsException() {
        Years.ONE.dividedBy(0);
    }

    // Test negated() method
    @Test(timeout = 4000)
    public void negated_returnsNegativeValue() {
        assertEquals(-2, Years.TWO.negated().getYears());
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void negated_atMinValue_throwsException() {
        Years.MIN_VALUE.negated();
    }

    // Test yearsBetween() factory method
    @Test(timeout = 4000)
    public void yearsBetween_withSameInstants_returnsZero() {
        Instant instant = Instant.EPOCH;
        Years years = Years.yearsBetween(instant, instant);
        assertEquals(0, years.getYears());
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void yearsBetween_withNullInstants_throwsException() {
        Years.yearsBetween((ReadableInstant) null, null);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void yearsBetween_withNullPartials_throwsException() {
        Years.yearsBetween((ReadablePartial) null, null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void yearsBetween_withInvalidPartial_throwsException() {
        DateTimeFieldType[] types = { DateTimeFieldType.era() };
        Partial partial = new Partial(null, types, null);
        Years.yearsBetween(partial, partial);
    }

    // Test yearsIn() factory method
    @Test(timeout = 4000)
    public void yearsIn_withNullInterval_returnsZero() {
        Years years = Years.yearsIn(null);
        assertEquals(0, years.getYears());
    }

    // Test parseYears() factory method
    @Test(timeout = 4000)
    public void parseYears_withNull_returnsZero() {
        Years years = Years.parseYears(null);
        assertEquals(0, years.getYears());
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void parseYears_withEmptyString_throwsException() {
        Years.parseYears("");
    }

    // Test toString() method
    @Test(timeout = 4000)
    public void toString_returnsISOFormat() {
        assertEquals("P2Y", Years.TWO.toString());
    }

    // Test edge cases and overflow scenarios
    @Test(timeout = 4000)
    public void creatingExtremeValues_succeeds() {
        Years max = Years.years(Integer.MAX_VALUE);
        Years min = Years.years(Integer.MIN_VALUE);
        assertEquals(Integer.MAX_VALUE, max.getYears());
        assertEquals(Integer.MIN_VALUE, min.getYears());
    }
}