/*
 *  Copyright 2001-2013 Stephen Colebourne
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
package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

/**
 * Unit tests for {@link Years}.
 */
public class TestYears {
    // Test in 2002/03 as time zones are more well known
    // (before the late 90's they were all over the place)
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    //-----------------------------------------------------------------------
    // Test Constants
    //-----------------------------------------------------------------------
    @Test
    public void constants_shouldHoldCorrectValues() {
        assertEquals(0, Years.ZERO.getYears());
        assertEquals(1, Years.ONE.getYears());
        assertEquals(2, Years.TWO.getYears());
        assertEquals(3, Years.THREE.getYears());
        assertEquals(Integer.MAX_VALUE, Years.MAX_VALUE.getYears());
        assertEquals(Integer.MIN_VALUE, Years.MIN_VALUE.getYears());
    }

    //-----------------------------------------------------------------------
    // Test Factory Methods
    //-----------------------------------------------------------------------
    @Test
    public void years_factory_shouldReturnCachedInstancesForConstants() {
        assertSame(Years.ZERO, Years.years(0));
        assertSame(Years.ONE, Years.years(1));
        assertSame(Years.TWO, Years.years(2));
        assertSame(Years.THREE, Years.years(3));
        assertSame(Years.MAX_VALUE, Years.years(Integer.MAX_VALUE));
        assertSame(Years.MIN_VALUE, Years.years(Integer.MIN_VALUE));
    }

    @Test
    public void years_factory_shouldCreateNewInstancesForOtherValues() {
        assertEquals(4, Years.years(4).getYears());
        assertEquals(-1, Years.years(-1).getYears());
    }

    @Test
    public void yearsBetween_instants_shouldCalculateCorrectYears() {
        // Arrange
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime endAfter = new DateTime(2009, 6, 9, 12, 0, 0, 0, PARIS);
        
        // Act & Assert
        assertEquals(3, Years.yearsBetween(start, endAfter).getYears());
        assertEquals(-3, Years.yearsBetween(endAfter, start).getYears());
        assertEquals(0, Years.yearsBetween(start, start).getYears());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void yearsBetween_partials_shouldCalculateCorrectYears() {
        // Arrange
        LocalDate start = new LocalDate(2006, 6, 9);
        LocalDate endAfter = new LocalDate(2009, 6, 9);
        YearMonthDay endAfterLegacy = new YearMonthDay(2012, 6, 9);

        // Act & Assert
        assertEquals(3, Years.yearsBetween(start, endAfter).getYears());
        assertEquals(-3, Years.yearsBetween(endAfter, start).getYears());
        assertEquals(0, Years.yearsBetween(start, start).getYears());
        assertEquals(6, Years.yearsBetween(start, endAfterLegacy).getYears());
    }

    @Test
    public void yearsIn_interval_shouldCalculateCorrectYears() {
        // Arrange
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime end = new DateTime(2009, 6, 9, 12, 0, 0, 0, PARIS);
        
        // Act & Assert
        assertEquals(3, Years.yearsIn(new Interval(start, end)).getYears());
        assertEquals(0, Years.yearsIn(new Interval(start, start)).getYears());
    }

    @Test
    public void yearsIn_interval_shouldReturnZeroForNullInterval() {
        assertEquals(0, Years.yearsIn(null).getYears());
    }

    @Test
    public void parseYears_shouldParseValidFormats() {
        assertEquals(0, Years.parseYears("P0Y").getYears());
        assertEquals(1, Years.parseYears("P1Y").getYears());
        assertEquals(-3, Years.parseYears("P-3Y").getYears());
        assertEquals(2, Years.parseYears("P2Y0M").getYears());
        assertEquals(2, Years.parseYears("P2YT0H0M").getYears());
    }

    @Test
    public void parseYears_shouldReturnZeroForNull() {
        assertEquals(0, Years.parseYears(null).getYears());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseYears_shouldThrowExceptionForPeriodWithNonYearFields() {
        Years.parseYears("P1M1D");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseYears_shouldThrowExceptionForPeriodWithTimeComponent() {
        Years.parseYears("P1YT1H");
    }

    //-----------------------------------------------------------------------
    // Test Getters
    //-----------------------------------------------------------------------
    @Test
    public void getters_shouldReturnCorrectValues() {
        // Arrange
        Years test = Years.years(20);

        // Act & Assert
        assertEquals(20, test.getYears());
        assertEquals(DurationFieldType.years(), test.getFieldType());
        assertEquals(PeriodType.years(), test.getPeriodType());
    }

    //-----------------------------------------------------------------------
    // Test Comparison Methods
    //-----------------------------------------------------------------------
    @Test
    public void isGreaterThan_shouldReturnCorrectly() {
        assertTrue(Years.THREE.isGreaterThan(Years.TWO));
        assertFalse(Years.THREE.isGreaterThan(Years.THREE));
        assertFalse(Years.TWO.isGreaterThan(Years.THREE));
    }

    @Test
    public void isGreaterThan_shouldTreatNullAsZero() {
        assertTrue("1 > null (0)", Years.ONE.isGreaterThan(null));
        assertFalse("-1 > null (0)", Years.years(-1).isGreaterThan(null));
    }

    @Test
    public void isLessThan_shouldReturnCorrectly() {
        assertTrue(Years.TWO.isLessThan(Years.THREE));
        assertFalse(Years.THREE.isLessThan(Years.THREE));
        assertFalse(Years.THREE.isLessThan(Years.TWO));
    }

    @Test
    public void isLessThan_shouldTreatNullAsZero() {
        assertFalse("1 < null (0)", Years.ONE.isLessThan(null));
        assertTrue("-1 < null (0)", Years.years(-1).isLessThan(null));
    }

    //-----------------------------------------------------------------------
    // Test toString()
    //-----------------------------------------------------------------------
    @Test
    public void toString_shouldReturnISOFormat() {
        assertEquals("P20Y", Years.years(20).toString());
        assertEquals("P-20Y", Years.years(-20).toString());
    }

    //-----------------------------------------------------------------------
    // Test Serialization
    //-----------------------------------------------------------------------
    @Test
    public void serialization_shouldPreserveSingletonInstance() throws Exception {
        // Arrange
        Years original = Years.THREE;
        
        // Act
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Years deserialized = (Years) ois.readObject();
        ois.close();
        
        // Assert
        assertSame(original, deserialized);
    }

    //-----------------------------------------------------------------------
    // Test Mathematical Operations
    //-----------------------------------------------------------------------
    @Test
    public void plus_int_shouldAddYearsCorrectly() {
        // Arrange
        final Years twoYears = Years.years(2);
        
        // Act
        final Years fiveYears = twoYears.plus(3);
        
        // Assert
        assertEquals(5, fiveYears.getYears());
        assertEquals(1, Years.ONE.plus(0).getYears());
    }

    @Test
    public void plus_int_shouldBeImmutable() {
        // Arrange
        final Years twoYears = Years.years(2);
        
        // Act
        twoYears.plus(3);
        
        // Assert
        assertEquals("Original instance should not be modified", 2, twoYears.getYears());
    }

    @Test(expected = ArithmeticException.class)
    public void plus_int_shouldThrowExceptionOnOverflow() {
        Years.MAX_VALUE.plus(1);
    }

    @Test
    public void plus_years_shouldAddYearsCorrectly() {
        // Arrange
        final Years twoYears = Years.years(2);
        final Years threeYears = Years.years(3);
        
        // Act
        final Years fiveYears = twoYears.plus(threeYears);
        
        // Assert
        assertEquals(5, fiveYears.getYears());
        assertEquals(1, Years.ONE.plus(Years.ZERO).getYears());
    }

    @Test
    public void plus_years_shouldBeImmutable() {
        // Arrange
        final Years twoYears = Years.years(2);
        final Years threeYears = Years.years(3);
        
        // Act
        twoYears.plus(threeYears);
        
        // Assert
        assertEquals("Original instance should not be modified", 2, twoYears.getYears());
        assertEquals("Parameter instance should not be modified", 3, threeYears.getYears());
    }

    @Test
    public void plus_years_shouldTreatNullAsZero() {
        assertEquals(1, Years.ONE.plus((Years) null).getYears());
    }

    @Test(expected = ArithmeticException.class)
    public void plus_years_shouldThrowExceptionOnOverflow() {
        Years.MAX_VALUE.plus(Years.ONE);
    }

    @Test
    public void minus_int_shouldSubtractYearsCorrectly() {
        assertEquals(-1, Years.years(2).minus(3).getYears());
        assertEquals(1, Years.ONE.minus(0).getYears());
    }

    @Test(expected = ArithmeticException.class)
    public void minus_int_shouldThrowExceptionOnUnderflow() {
        Years.MIN_VALUE.minus(1);
    }

    @Test
    public void minus_years_shouldSubtractYearsCorrectly() {
        assertEquals(-1, Years.years(2).minus(Years.years(3)).getYears());
        assertEquals(1, Years.ONE.minus(Years.ZERO).getYears());
    }

    @Test
    public void minus_years_shouldTreatNullAsZero() {
        assertEquals(1, Years.ONE.minus((Years) null).getYears());
    }

    @Test(expected = ArithmeticException.class)
    public void minus_years_shouldThrowExceptionOnUnderflow() {
        Years.MIN_VALUE.minus(Years.ONE);
    }

    @Test
    public void multipliedBy_shouldScaleCorrectly() {
        assertEquals(6, Years.years(2).multipliedBy(3).getYears());
        assertEquals(-6, Years.years(2).multipliedBy(-3).getYears());
    }

    @Test
    public void multipliedBy_shouldReturnSameInstanceWhenScalingByOne() {
        Years twoYears = Years.years(2);
        assertSame(twoYears, twoYears.multipliedBy(1));
    }

    @Test(expected = ArithmeticException.class)
    public void multipliedBy_shouldThrowExceptionOnOverflow() {
        Years.years(Integer.MAX_VALUE / 2 + 1).multipliedBy(2);
    }

    @Test
    public void dividedBy_shouldScaleCorrectlyWithIntegerDivision() {
        assertEquals(6, Years.years(12).dividedBy(2).getYears());
        assertEquals(4, Years.years(12).dividedBy(3).getYears());
        assertEquals(2, Years.years(12).dividedBy(5).getYears());
    }

    @Test
    public void dividedBy_shouldReturnSameInstanceWhenDividingByOne() {
        Years twelveYears = Years.years(12);
        assertSame(twelveYears, twelveYears.dividedBy(1));
    }

    @Test(expected = ArithmeticException.class)
    public void dividedBy_shouldThrowExceptionForDivisionByZero() {
        Years.ONE.dividedBy(0);
    }

    @Test
    public void negated_shouldFlipSign() {
        assertEquals(-12, Years.years(12).negated().getYears());
        assertEquals(0, Years.ZERO.negated().getYears());
    }

    @Test(expected = ArithmeticException.class)
    public void negated_shouldThrowExceptionOnMinValue() {
        Years.MIN_VALUE.negated();
    }

    //-----------------------------------------------------------------------
    // Test Integration with other Joda-Time classes
    //-----------------------------------------------------------------------
    @Test
    public void plus_onLocalDate_shouldAddYearsCorrectly() {
        // Arrange
        Years threeYears = Years.years(3);
        LocalDate startDate = new LocalDate(2006, 6, 1);
        LocalDate expectedDate = new LocalDate(2009, 6, 1);
        
        // Act
        LocalDate resultDate = startDate.plus(threeYears);
        
        // Assert
        assertEquals(expectedDate, resultDate);
    }
}