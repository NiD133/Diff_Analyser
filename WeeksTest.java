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

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class TestWeeks {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    //-----------------------------------------------------------------------
    // Test Constants
    //-----------------------------------------------------------------------
    @Test
    public void testConstants() {
        assertEquals(0, Weeks.ZERO.getWeeks());
        assertEquals(1, Weeks.ONE.getWeeks());
        assertEquals(2, Weeks.TWO.getWeeks());
        assertEquals(3, Weeks.THREE.getWeeks());
        assertEquals(Integer.MAX_VALUE, Weeks.MAX_VALUE.getWeeks());
        assertEquals(Integer.MIN_VALUE, Weeks.MIN_VALUE.getWeeks());
    }

    //-----------------------------------------------------------------------
    // Test Factory Methods
    //-----------------------------------------------------------------------
    @Test
    public void weeks_int_shouldReturnCachedAndNewInstances() {
        assertSame(Weeks.ZERO, Weeks.weeks(0));
        assertSame(Weeks.ONE, Weeks.weeks(1));
        assertSame(Weeks.TWO, Weeks.weeks(2));
        assertSame(Weeks.THREE, Weeks.weeks(3));
        assertSame(Weeks.MAX_VALUE, Weeks.weeks(Integer.MAX_VALUE));
        assertSame(Weeks.MIN_VALUE, Weeks.weeks(Integer.MIN_VALUE));
        
        assertEquals(-1, Weeks.weeks(-1).getWeeks());
        assertEquals(4, Weeks.weeks(4).getWeeks());
    }

    @Test
    public void weeksBetween_instants_shouldCalculateWeeks() {
        // Arrange
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime threeWeeksLater = new DateTime(2006, 6, 30, 12, 0, 0, 0, PARIS);
        DateTime sixWeeksLater = new DateTime(2006, 7, 21, 12, 0, 0, 0, PARIS);

        // Act & Assert
        assertEquals(3, Weeks.weeksBetween(start, threeWeeksLater).getWeeks());
        assertEquals(0, Weeks.weeksBetween(start, start).getWeeks());
        assertEquals(-3, Weeks.weeksBetween(threeWeeksLater, start).getWeeks());
        assertEquals(6, Weeks.weeksBetween(start, sixWeeksLater).getWeeks());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void weeksBetween_partials_shouldCalculateWeeks() {
        // Arrange
        LocalDate start = new LocalDate(2006, 6, 9);
        LocalDate threeWeeksLater = new LocalDate(2006, 6, 30);
        YearMonthDay sixWeeksLater = new YearMonthDay(2006, 7, 21);

        // Act & Assert
        assertEquals(3, Weeks.weeksBetween(start, threeWeeksLater).getWeeks());
        assertEquals(0, Weeks.weeksBetween(start, start).getWeeks());
        assertEquals(-3, Weeks.weeksBetween(threeWeeksLater, start).getWeeks());
        assertEquals(6, Weeks.weeksBetween(start, sixWeeksLater).getWeeks());
    }

    @Test
    public void weeksIn_interval_shouldCalculateWeeks() {
        // Arrange
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 0, 0, PARIS);
        DateTime threeWeeksLater = new DateTime(2006, 6, 30, 12, 0, 0, 0, PARIS);
        DateTime sixWeeksLater = new DateTime(2006, 7, 21, 12, 0, 0, 0, PARIS);

        // Act & Assert
        assertEquals(0, Weeks.weeksIn(null).getWeeks());
        assertEquals(3, Weeks.weeksIn(new Interval(start, threeWeeksLater)).getWeeks());
        assertEquals(0, Weeks.weeksIn(new Interval(start, start)).getWeeks());
        assertEquals(6, Weeks.weeksIn(new Interval(start, sixWeeksLater)).getWeeks());
    }
    
    @Test
    public void standardWeeksIn_period_shouldCalculateWeeks() {
        assertEquals(0, Weeks.standardWeeksIn(null).getWeeks());
        assertEquals(0, Weeks.standardWeeksIn(Period.ZERO).getWeeks());
        assertEquals(1, Weeks.standardWeeksIn(Period.weeks(1)).getWeeks());
        assertEquals(123, Weeks.standardWeeksIn(Period.weeks(123)).getWeeks());
        assertEquals(1, Weeks.standardWeeksIn(Period.days(13)).getWeeks()); // 1 week and 6 days
        assertEquals(2, Weeks.standardWeeksIn(Period.days(14)).getWeeks());
        assertEquals(2, Weeks.standardWeeksIn(Period.days(15)).getWeeks()); // 2 weeks and 1 day
    }

    @Test(expected = IllegalArgumentException.class)
    public void standardWeeksIn_periodWithMonths_shouldThrowException() {
        Weeks.standardWeeksIn(Period.months(1));
    }

    @Test
    public void parseWeeks_shouldParseValidStrings() {
        assertEquals(0, Weeks.parseWeeks(null).getWeeks());
        assertEquals(0, Weeks.parseWeeks("P0W").getWeeks());
        assertEquals(1, Weeks.parseWeeks("P1W").getWeeks());
        assertEquals(-3, Weeks.parseWeeks("P-3W").getWeeks());
        assertEquals(2, Weeks.parseWeeks("P0Y0M2W").getWeeks());
        assertEquals(2, Weeks.parseWeeks("P2WT0H0M").getWeeks());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseWeeks_invalidStringWithDays_shouldThrowException() {
        Weeks.parseWeeks("P1Y1D");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseWeeks_invalidStringWithTime_shouldThrowException() {
        Weeks.parseWeeks("P1WT1H");
    }

    //-----------------------------------------------------------------------
    // Test Getters
    //-----------------------------------------------------------------------
    @Test
    public void getWeeks_shouldReturnCorrectValue() {
        assertEquals(20, Weeks.weeks(20).getWeeks());
    }

    @Test
    public void getFieldType_shouldReturnWeeks() {
        assertEquals(DurationFieldType.weeks(), Weeks.weeks(20).getFieldType());
    }

    @Test
    public void getPeriodType_shouldReturnWeeks() {
        assertEquals(PeriodType.weeks(), Weeks.weeks(20).getPeriodType());
    }

    //-----------------------------------------------------------------------
    // Test Comparison Methods
    //-----------------------------------------------------------------------
    @Test
    public void isGreaterThan_shouldCompareCorrectly() {
        assertTrue(Weeks.THREE.isGreaterThan(Weeks.TWO));
        assertFalse(Weeks.THREE.isGreaterThan(Weeks.THREE));
        assertFalse(Weeks.TWO.isGreaterThan(Weeks.THREE));
        assertTrue(Weeks.ONE.isGreaterThan(null)); // null is treated as zero
        assertFalse(Weeks.weeks(-1).isGreaterThan(null));
    }

    @Test
    public void isLessThan_shouldCompareCorrectly() {
        assertFalse(Weeks.THREE.isLessThan(Weeks.TWO));
        assertFalse(Weeks.THREE.isLessThan(Weeks.THREE));
        assertTrue(Weeks.TWO.isLessThan(Weeks.THREE));
        assertFalse(Weeks.ONE.isLessThan(null)); // null is treated as zero
        assertTrue(Weeks.weeks(-1).isLessThan(null));
    }

    //-----------------------------------------------------------------------
    // Test toString()
    //-----------------------------------------------------------------------
    @Test
    public void toString_shouldReturnISOFormat() {
        assertEquals("P20W", Weeks.weeks(20).toString());
        assertEquals("P-20W", Weeks.weeks(-20).toString());
    }

    //-----------------------------------------------------------------------
    // Test Serialization
    //-----------------------------------------------------------------------
    @Test
    public void serialization_shouldPreserveInstance() throws Exception {
        // Arrange
        Weeks original = Weeks.THREE;
        
        // Act
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Weeks deserialized = (Weeks) ois.readObject();
        ois.close();
        
        // Assert
        assertSame(original, deserialized);
    }

    //-----------------------------------------------------------------------
    // Test Standard Conversions
    //-----------------------------------------------------------------------
    @Test
    public void toStandardDays_shouldConvertCorrectly() {
        assertEquals(Days.days(14), Weeks.weeks(2).toStandardDays());
    }

    @Test(expected = ArithmeticException.class)
    public void toStandardDays_shouldThrowExceptionOnOverflow() {
        Weeks.MAX_VALUE.toStandardDays();
    }

    @Test
    public void toStandardHours_shouldConvertCorrectly() {
        assertEquals(Hours.hours(2 * 7 * 24), Weeks.weeks(2).toStandardHours());
    }

    @Test(expected = ArithmeticException.class)
    public void toStandardHours_shouldThrowExceptionOnOverflow() {
        Weeks.MAX_VALUE.toStandardHours();
    }

    @Test
    public void toStandardMinutes_shouldConvertCorrectly() {
        assertEquals(Minutes.minutes(2 * 7 * 24 * 60), Weeks.weeks(2).toStandardMinutes());
    }

    @Test(expected = ArithmeticException.class)
    public void toStandardMinutes_shouldThrowExceptionOnOverflow() {
        Weeks.MAX_VALUE.toStandardMinutes();
    }

    @Test
    public void toStandardSeconds_shouldConvertCorrectly() {
        assertEquals(Seconds.seconds(2 * 7 * 24 * 60 * 60), Weeks.weeks(2).toStandardSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void toStandardSeconds_shouldThrowExceptionOnOverflow() {
        Weeks.MAX_VALUE.toStandardSeconds();
    }

    @Test
    public void toStandardDuration_shouldConvertCorrectly() {
        // Arrange
        Weeks twentyWeeks = Weeks.weeks(20);
        Duration expected = new Duration(20L * DateTimeConstants.MILLIS_PER_WEEK);
        
        // Act & Assert
        assertEquals(expected, twentyWeeks.toStandardDuration());
        
        // Arrange
        Duration expectedMax = new Duration((long) Integer.MAX_VALUE * DateTimeConstants.MILLIS_PER_WEEK);
        
        // Act & Assert
        assertEquals(expectedMax, Weeks.MAX_VALUE.toStandardDuration());
    }

    //-----------------------------------------------------------------------
    // Test Math Operations
    //-----------------------------------------------------------------------
    @Test
    public void plus_int_shouldAddWeeks() {
        // Arrange
        Weeks twoWeeks = Weeks.weeks(2);
        
        // Act
        Weeks result = twoWeeks.plus(3);
        
        // Assert
        assertEquals(2, twoWeeks.getWeeks()); // Original is immutable
        assertEquals(5, result.getWeeks());
        assertEquals(1, Weeks.ONE.plus(0).getWeeks());
    }

    @Test(expected = ArithmeticException.class)
    public void plus_int_shouldThrowExceptionOnOverflow() {
        Weeks.MAX_VALUE.plus(1);
    }

    @Test
    public void plus_Weeks_shouldAddWeeks() {
        // Arrange
        Weeks twoWeeks = Weeks.weeks(2);
        Weeks threeWeeks = Weeks.weeks(3);
        
        // Act
        Weeks result = twoWeeks.plus(threeWeeks);
        
        // Assert
        assertEquals(2, twoWeeks.getWeeks()); // Original is immutable
        assertEquals(3, threeWeeks.getWeeks()); // Parameter is immutable
        assertEquals(5, result.getWeeks());
        assertEquals(1, Weeks.ONE.plus(Weeks.ZERO).getWeeks());
    }
    
    @Test
    public void plus_Weeks_withNull_shouldTreatAsZero() {
        assertEquals(1, Weeks.ONE.plus((Weeks) null).getWeeks());
    }

    @Test(expected = ArithmeticException.class)
    public void plus_Weeks_shouldThrowExceptionOnOverflow() {
        Weeks.MAX_VALUE.plus(Weeks.ONE);
    }

    @Test
    public void minus_int_shouldSubtractWeeks() {
        // Arrange
        Weeks twoWeeks = Weeks.weeks(2);
        
        // Act
        Weeks result = twoWeeks.minus(3);
        
        // Assert
        assertEquals(2, twoWeeks.getWeeks()); // Original is immutable
        assertEquals(-1, result.getWeeks());
        assertEquals(1, Weeks.ONE.minus(0).getWeeks());
    }

    @Test(expected = ArithmeticException.class)
    public void minus_int_shouldThrowExceptionOnOverflow() {
        Weeks.MIN_VALUE.minus(1);
    }

    @Test
    public void minus_Weeks_shouldSubtractWeeks() {
        // Arrange
        Weeks twoWeeks = Weeks.weeks(2);
        Weeks threeWeeks = Weeks.weeks(3);
        
        // Act
        Weeks result = twoWeeks.minus(threeWeeks);
        
        // Assert
        assertEquals(2, twoWeeks.getWeeks()); // Original is immutable
        assertEquals(3, threeWeeks.getWeeks()); // Parameter is immutable
        assertEquals(-1, result.getWeeks());
        assertEquals(1, Weeks.ONE.minus(Weeks.ZERO).getWeeks());
    }
    
    @Test
    public void minus_Weeks_withNull_shouldTreatAsZero() {
        assertEquals(1, Weeks.ONE.minus((Weeks) null).getWeeks());
    }

    @Test(expected = ArithmeticException.class)
    public void minus_Weeks_shouldThrowExceptionOnOverflow() {
        Weeks.MIN_VALUE.minus(Weeks.ONE);
    }

    @Test
    public void multipliedBy_shouldMultiplyCorrectly() {
        // Arrange
        Weeks twoWeeks = Weeks.weeks(2);
        
        // Act & Assert
        assertEquals(6, twoWeeks.multipliedBy(3).getWeeks());
        assertEquals(2, twoWeeks.getWeeks()); // Original is immutable
        assertEquals(-6, twoWeeks.multipliedBy(-3).getWeeks());
        assertSame(twoWeeks, twoWeeks.multipliedBy(1));
    }

    @Test(expected = ArithmeticException.class)
    public void multipliedBy_shouldThrowExceptionOnOverflow() {
        Weeks.weeks(Integer.MAX_VALUE / 2 + 1).multipliedBy(2);
    }

    @Test
    public void dividedBy_shouldDivideCorrectly() {
        // Arrange
        Weeks twelveWeeks = Weeks.weeks(12);
        
        // Act & Assert
        assertEquals(6, twelveWeeks.dividedBy(2).getWeeks());
        assertEquals(12, twelveWeeks.getWeeks()); // Original is immutable
        assertEquals(4, twelveWeeks.dividedBy(3).getWeeks());
        assertEquals(3, twelveWeeks.dividedBy(4).getWeeks());
        assertEquals(2, twelveWeeks.dividedBy(5).getWeeks()); // Integer division
        assertEquals(2, twelveWeeks.dividedBy(6).getWeeks());
        assertSame(twelveWeeks, twelveWeeks.dividedBy(1));
    }

    @Test(expected = ArithmeticException.class)
    public void dividedBy_zero_shouldThrowException() {
        Weeks.ONE.dividedBy(0);
    }

    @Test
    public void negated_shouldNegateCorrectly() {
        // Arrange
        Weeks twelveWeeks = Weeks.weeks(12);
        
        // Act & Assert
        assertEquals(-12, twelveWeeks.negated().getWeeks());
        assertEquals(12, twelveWeeks.getWeeks()); // Original is immutable
    }

    @Test(expected = ArithmeticException.class)
    public void negated_minValue_shouldThrowExceptionOnOverflow() {
        Weeks.MIN_VALUE.negated();
    }

    //-----------------------------------------------------------------------
    // Test Integration
    //-----------------------------------------------------------------------
    @Test
    public void addToLocalDate_shouldAddWeeksCorrectly() {
        // Arrange
        Weeks threeWeeks = Weeks.weeks(3);
        LocalDate date = new LocalDate(2006, 6, 1);
        LocalDate expected = new LocalDate(2006, 6, 22);
        
        // Act
        LocalDate result = date.plus(threeWeeks);
        
        // Assert
        assertEquals(expected, result);
    }
}