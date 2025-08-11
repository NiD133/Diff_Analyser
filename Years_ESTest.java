package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Instant;
import org.joda.time.Partial;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadableInterval;
import org.joda.time.ReadablePartial;

/**
 * Test suite for the Years class, covering all major functionality including
 * creation, arithmetic operations, comparisons, and edge cases.
 */
public class YearsTest {

    // ========== Factory Method Tests ==========
    
    @Test
    public void testYearsFactory_CreatesCorrectValues() {
        assertEquals(0, Years.years(0).getYears());
        assertEquals(1, Years.years(1).getYears());
        assertEquals(2, Years.years(2).getYears());
        assertEquals(3, Years.years(3).getYears());
        assertEquals(-690, Years.years(-690).getYears());
        assertEquals(Integer.MAX_VALUE, Years.years(Integer.MAX_VALUE).getYears());
        assertEquals(Integer.MIN_VALUE, Years.years(Integer.MIN_VALUE).getYears());
    }

    @Test
    public void testYearsBetween_SameInstant_ReturnsZero() {
        Instant epoch = Instant.EPOCH;
        Years result = Years.yearsBetween(epoch, epoch);
        assertEquals(0, result.getYears());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testYearsBetween_NullInstants_ThrowsException() {
        Years.yearsBetween((ReadableInstant) null, (ReadableInstant) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testYearsBetween_NullPartials_ThrowsException() {
        Years.yearsBetween((ReadablePartial) null, (ReadablePartial) null);
    }

    @Test
    public void testYearsIn_NullInterval_ReturnsZero() {
        Years result = Years.yearsIn((ReadableInterval) null);
        assertEquals(0, result.getYears());
    }

    @Test
    public void testParseYears_NullString_ReturnsValidYears() {
        Years result = Years.parseYears(null);
        assertEquals(1, result.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseYears_EmptyString_ThrowsException() {
        Years.parseYears("");
    }

    // ========== Arithmetic Operations Tests ==========
    
    @Test
    public void testPlus_WithInteger() {
        Years twoYears = Years.TWO;
        Years result = twoYears.plus(2);
        assertEquals(4, result.getYears());
        
        Years zeroYears = Years.years(0);
        Years sameResult = zeroYears.plus(0);
        assertEquals(0, sameResult.getYears());
    }

    @Test
    public void testPlus_WithYearsObject() {
        Years minValue = Years.MIN_VALUE;
        Years twoYears = Years.TWO;
        Years result = twoYears.plus(minValue);
        assertEquals(-2147483646, result.getYears());
    }

    @Test
    public void testPlus_WithNull_ReturnsSameInstance() {
        Years threeYears = Years.THREE;
        Years result = threeYears.plus((Years) null);
        assertSame(threeYears, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testPlus_Overflow_ThrowsException() {
        Years.MAX_VALUE.plus(Years.MAX_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void testPlus_IntegerOverflow_ThrowsException() {
        Years.MAX_VALUE.plus(80);
    }

    @Test
    public void testMinus_WithInteger() {
        Years result1 = Years.years(6488).minus(6488);
        assertEquals(0, result1.getYears());
        
        Years result2 = Years.THREE.minus(0);
        assertEquals(3, result2.getYears());
        
        Years result3 = Years.ZERO.minus(1612);
        assertEquals(-1612, result3.getYears());
    }

    @Test
    public void testMinus_WithYearsObject() {
        Years twoYears = Years.TWO;
        Years minusOneThousand = twoYears.plus(-1131);
        Years result = minusOneThousand.minus(twoYears);
        assertEquals(-1131, result.getYears());
        
        Years result2 = Years.THREE.minus(Years.THREE);
        assertEquals(0, result2.getYears());
    }

    @Test
    public void testMinus_WithNull_ReturnsSameInstance() {
        Years twoYears = Years.TWO;
        Years result = twoYears.minus((Years) null);
        assertSame(twoYears, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testMinus_Overflow_ThrowsException() {
        Years.MIN_VALUE.minus(Years.MIN_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void testMinus_NegativeOverflow_ThrowsException() {
        Years.MAX_VALUE.minus(-2133);
    }

    @Test
    public void testMultipliedBy() {
        Years zeroResult = Years.years(0).multipliedBy(0);
        assertEquals(0, zeroResult.getYears());
        
        Years positiveResult = Years.years(-1249).multipliedBy(-1249);
        assertEquals(1560001, positiveResult.getYears());
        
        Years negativeResult = Years.THREE.multipliedBy(-1129);
        assertEquals(-3387, negativeResult.getYears());
    }

    @Test(expected = ArithmeticException.class)
    public void testMultipliedBy_Overflow_ThrowsException() {
        Years.MAX_VALUE.multipliedBy(3);
    }

    @Test
    public void testDividedBy() {
        Years zeroResult = Years.ZERO.dividedBy(1);
        assertSame(Years.ZERO, zeroResult);
        
        Years maxDivided = Years.MAX_VALUE.dividedBy(-592);
        assertEquals(-3627506, maxDivided.getYears());
        
        Years maxByOne = Years.MAX_VALUE.dividedBy(1);
        assertEquals(Integer.MAX_VALUE, maxByOne.getYears());
    }

    @Test(expected = ArithmeticException.class)
    public void testDividedBy_ZeroDivisor_ThrowsException() {
        Years.ONE.dividedBy(0);
    }

    @Test
    public void testNegated() {
        Years twoYears = Years.TWO;
        Years negated = twoYears.negated();
        assertEquals(-2, negated.getYears());
    }

    @Test(expected = ArithmeticException.class)
    public void testNegated_MinValue_ThrowsException() {
        Years.MIN_VALUE.negated();
    }

    // ========== Comparison Tests ==========
    
    @Test
    public void testIsGreaterThan() {
        assertTrue("THREE should be greater than negative one", 
                   Years.THREE.isGreaterThan(Years.years(-1)));
        assertTrue("MAX_VALUE should be greater than null", 
                   Years.MAX_VALUE.isGreaterThan(null));
        
        assertFalse("Zero should not be greater than null", 
                    Years.years(0).isGreaterThan(null));
        assertFalse("Value should not be greater than itself", 
                    Years.years(-690).isGreaterThan(Years.years(-690)));
        assertFalse("Negative value should not be greater than MAX_VALUE", 
                    Years.years(-690).isGreaterThan(Years.MAX_VALUE));
        assertFalse("MIN_VALUE should not be greater than null", 
                    Years.MIN_VALUE.isGreaterThan(null));
    }

    @Test
    public void testIsLessThan() {
        assertTrue("MIN_VALUE should be less than null", 
                   Years.MIN_VALUE.isLessThan(null));
        assertTrue("Zero should be less than four", 
                   Years.years(0).isLessThan(Years.TWO.plus(2)));
        
        assertFalse("MAX_VALUE should not be less than THREE", 
                    Years.MAX_VALUE.isLessThan(Years.THREE));
        assertFalse("TWO should not be less than null", 
                    Years.TWO.isLessThan(null));
        assertFalse("Zero should not be less than null", 
                    Years.years(0).isLessThan(null));
        assertFalse("Value should not be less than itself", 
                    Years.years(0).isLessThan(Years.years(0)));
    }

    // ========== Getter and Utility Method Tests ==========
    
    @Test
    public void testGetYears() {
        assertEquals(0, Years.years(0).getYears());
        assertEquals(-690, Years.years(-690).getYears());
        assertEquals(4, Years.TWO.plus(2).getYears());
    }

    @Test
    public void testGetFieldType() {
        assertEquals("years", Years.ZERO.getFieldType().getName());
    }

    @Test
    public void testGetPeriodType() {
        // Just verify it doesn't throw an exception
        Years.years(0).getPeriodType();
    }

    @Test
    public void testToString() {
        assertEquals("P2Y", Years.TWO.toString());
    }

    // ========== Edge Cases and Error Conditions ==========
    
    @Test(expected = NullPointerException.class)
    public void testYearsBetween_InvalidPartial_ThrowsException() {
        DateTimeFieldType[] fieldTypes = {DateTimeFieldType.era()};
        Partial invalidPartial = new Partial(null, fieldTypes, null);
        Years.yearsBetween(invalidPartial, invalidPartial);
    }

    @Test(expected = NullPointerException.class)
    public void testYearsBetween_PartialWithNullFieldType_ThrowsException() {
        DateTimeFieldType[] fieldTypes = new DateTimeFieldType[1]; // Contains null
        Partial invalidPartial = new Partial(null, fieldTypes, null);
        Years.yearsBetween(invalidPartial, invalidPartial);
    }
}