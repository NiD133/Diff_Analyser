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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.joda.time.Minutes.*;
import static org.junit.Assert.*;

/**
 * This class is a Junit unit test for Minutes.
 *
 * @author Stephen Colebourne
 */
public class TestMinutes {
    // Test in 2002/03 as time zones are more well known
    // (before the late 90's they were all over the place)
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    //-----------------------------------------------------------------------
    @Test
    public void constants_shouldHaveCorrectValues() {
        assertEquals(0, ZERO.getMinutes());
        assertEquals(1, ONE.getMinutes());
        assertEquals(2, TWO.getMinutes());
        assertEquals(3, THREE.getMinutes());
        assertEquals(Integer.MAX_VALUE, MAX_VALUE.getMinutes());
        assertEquals(Integer.MIN_VALUE, MIN_VALUE.getMinutes());
    }

    //-----------------------------------------------------------------------
    @Test
    public void minutes_factory_shouldReturnCorrectly() {
        assertSame(ZERO, Minutes.minutes(0));
        assertSame(ONE, Minutes.minutes(1));
        assertSame(TWO, Minutes.minutes(2));
        assertSame(THREE, Minutes.minutes(3));
        assertSame(MAX_VALUE, Minutes.minutes(Integer.MAX_VALUE));
        assertSame(MIN_VALUE, Minutes.minutes(Integer.MIN_VALUE));
        
        assertEquals(-1, Minutes.minutes(-1).getMinutes());
        assertEquals(4, Minutes.minutes(4).getMinutes());
    }

    //-----------------------------------------------------------------------
    @Test
    public void minutesBetween_twoInstants_shouldCalculateDifference() {
        DateTime start = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS);
        DateTime endAfterThreeMinutes = new DateTime(2006, 6, 9, 12, 6, 0, 0, PARIS);
        DateTime endAfterSixMinutes = new DateTime(2006, 6, 9, 12, 9, 0, 0, PARIS);
        
        assertEquals(3, Minutes.minutesBetween(start, endAfterThreeMinutes).getMinutes());
        assertEquals(0, Minutes.minutesBetween(start, start).getMinutes());
        assertEquals(0, Minutes.minutesBetween(endAfterThreeMinutes, endAfterThreeMinutes).getMinutes());
        assertEquals(-3, Minutes.minutesBetween(endAfterThreeMinutes, start).getMinutes());
        assertEquals(6, Minutes.minutesBetween(start, endAfterSixMinutes).getMinutes());
    }

    @Test
    public void minutesBetween_twoPartials_shouldCalculateDifference() {
        LocalTime start = new LocalTime(12, 3);
        LocalTime endAfterThreeMinutes = new LocalTime(12, 6);
        @SuppressWarnings("deprecation")
        TimeOfDay endAfterSixMinutes = new TimeOfDay(12, 9);
        
        assertEquals(3, Minutes.minutesBetween(start, endAfterThreeMinutes).getMinutes());
        assertEquals(0, Minutes.minutesBetween(start, start).getMinutes());
        assertEquals(0, Minutes.minutesBetween(endAfterThreeMinutes, endAfterThreeMinutes).getMinutes());
        assertEquals(-3, Minutes.minutesBetween(endAfterThreeMinutes, start).getMinutes());
        assertEquals(6, Minutes.minutesBetween(start, endAfterSixMinutes).getMinutes());
    }

    @Test
    public void minutesIn_interval_shouldCalculateContainedMinutes() {
        DateTime start = new DateTime(2006, 6, 9, 12, 3, 0, 0, PARIS);
        DateTime endAfterThreeMinutes = new DateTime(2006, 6, 9, 12, 6, 0, 0, PARIS);
        DateTime endAfterSixMinutes = new DateTime(2006, 6, 9, 12, 9, 0, 0, PARIS);
        
        assertEquals(0, Minutes.minutesIn(null).getMinutes());
        assertEquals(3, Minutes.minutesIn(new Interval(start, endAfterThreeMinutes)).getMinutes());
        assertEquals(0, Minutes.minutesIn(new Interval(start, start)).getMinutes());
        assertEquals(6, Minutes.minutesIn(new Interval(start, endAfterSixMinutes)).getMinutes());
    }

    @Test
    public void standardMinutesIn_fromPeriod_shouldConvertStandardPeriods() {
        assertEquals(0, Minutes.standardMinutesIn(null).getMinutes());
        assertEquals(0, Minutes.standardMinutesIn(Period.ZERO).getMinutes());
        assertEquals(1, Minutes.standardMinutesIn(new Period(0, 0, 0, 0, 0, 1, 0, 0)).getMinutes());
        assertEquals(123, Minutes.standardMinutesIn(Period.minutes(123)).getMinutes());
        assertEquals(-987, Minutes.standardMinutesIn(Period.minutes(-987)).getMinutes());
        assertEquals(1, Minutes.standardMinutesIn(Period.seconds(119)).getMinutes());
        assertEquals(2, Minutes.standardMinutesIn(Period.seconds(120)).getMinutes());
        assertEquals(2, Minutes.standardMinutesIn(Period.seconds(121)).getMinutes());
        assertEquals(120, Minutes.standardMinutesIn(Period.hours(2)).getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void standardMinutesIn_fromPeriodWithImpreciseFields_shouldThrowException() {
        Minutes.standardMinutesIn(Period.months(1));
    }

    @Test
    public void parseMinutes_fromString_shouldParseISOFormat() {
        assertEquals(0, Minutes.parseMinutes(null).getMinutes());
        assertEquals(0, Minutes.parseMinutes("PT0M").getMinutes());
        assertEquals(1, Minutes.parseMinutes("PT1M").getMinutes());
        assertEquals(-3, Minutes.parseMinutes("PT-3M").getMinutes());
        assertEquals(2, Minutes.parseMinutes("P0Y0M0DT2M").getMinutes());
        assertEquals(2, Minutes.parseMinutes("PT0H2M").getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseMinutes_fromString_withInvalidNonMinuteFields_shouldThrowException() {
        Minutes.parseMinutes("P1Y1M"); // Years and Months are not precise
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseMinutes_fromString_withInvalidDayField_shouldThrowException() {
        Minutes.parseMinutes("P1DT1M"); // Days are not allowed in a Minutes-only parse
    }

    //-----------------------------------------------------------------------
    @Test
    public void getters_shouldReturnCorrectValues() {
        Minutes test = Minutes.minutes(20);
        assertEquals(20, test.getMinutes());
        assertEquals(DurationFieldType.minutes(), test.getFieldType());
        assertEquals(PeriodType.minutes(), test.getPeriodType());
    }

    //-----------------------------------------------------------------------
    @Test
    public void isGreaterThan_shouldWorkCorrectly() {
        assertTrue(THREE.isGreaterThan(TWO));
        assertFalse(THREE.isGreaterThan(THREE));
        assertFalse(TWO.isGreaterThan(THREE));
        assertTrue(ONE.isGreaterThan(null)); // null is treated as zero
        assertFalse(minutes(-1).isGreaterThan(null));
    }

    @Test
    public void isLessThan_shouldWorkCorrectly() {
        assertFalse(THREE.isLessThan(TWO));
        assertFalse(THREE.isLessThan(THREE));
        assertTrue(TWO.isLessThan(THREE));
        assertFalse(ONE.isLessThan(null)); // null is treated as zero
        assertTrue(minutes(-1).isLessThan(null));
    }

    //-----------------------------------------------------------------------
    @Test
    public void toString_shouldReturnISOFormattedString() {
        assertEquals("PT20M", Minutes.minutes(20).toString());
        assertEquals("PT-20M", Minutes.minutes(-20).toString());
    }

    //-----------------------------------------------------------------------
    @Test
    public void serialization_shouldPreserveSingletonInstances() throws Exception {
        // Arrange
        Minutes original = THREE;
        
        // Act
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Minutes deserialized = (Minutes) ois.readObject();
        ois.close();
        
        // Assert
        assertSame(original, deserialized);
    }

    //-----------------------------------------------------------------------
    @Test
    public void toStandardWeeks_shouldConvertFromMinutes() {
        int minutesInTwoWeeks = DateTimeConstants.MINUTES_PER_WEEK * 2;
        Minutes test = Minutes.minutes(minutesInTwoWeeks);
        assertEquals(Weeks.weeks(2), test.toStandardWeeks());
    }

    @Test
    public void toStandardDays_shouldConvertFromMinutes() {
        int minutesInTwoDays = DateTimeConstants.MINUTES_PER_DAY * 2;
        Minutes test = Minutes.minutes(minutesInTwoDays);
        assertEquals(Days.days(2), test.toStandardDays());
    }

    @Test
    public void toStandardHours_shouldConvertFromMinutes() {
        int minutesInThreeHours = DateTimeConstants.MINUTES_PER_HOUR * 3;
        Minutes test = Minutes.minutes(minutesInThreeHours);
        assertEquals(Hours.hours(3), test.toStandardHours());
    }

    @Test
    public void toStandardSeconds_shouldConvertFromMinutes() {
        Minutes test = Minutes.minutes(3);
        int secondsInThreeMinutes = 3 * DateTimeConstants.SECONDS_PER_MINUTE;
        assertEquals(Seconds.seconds(secondsInThreeMinutes), test.toStandardSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void toStandardSeconds_forMaxValue_shouldThrowExceptionOnOverflow() {
        MAX_VALUE.toStandardSeconds();
    }

    @Test
    public void toStandardDuration_shouldConvertToCorrectDuration() {
        Minutes test = Minutes.minutes(20);
        long millisIn20Mins = 20L * DateTimeConstants.MILLIS_PER_MINUTE;
        assertEquals(new Duration(millisIn20Mins), test.toStandardDuration());
        
        long maxMillis = ((long) Integer.MAX_VALUE) * DateTimeConstants.MILLIS_PER_MINUTE;
        assertEquals(new Duration(maxMillis), MAX_VALUE.toStandardDuration());
    }

    //-----------------------------------------------------------------------
    @Test
    public void plus_int_shouldAddMinutes() {
        Minutes twoMinutes = Minutes.minutes(2);
        Minutes result = twoMinutes.plus(3);
        
        assertEquals("Original instance should be immutable", 2, twoMinutes.getMinutes());
        assertEquals(5, result.getMinutes());
        assertEquals(1, ONE.plus(0).getMinutes());
    }

    @Test(expected = ArithmeticException.class)
    public void plus_int_forMaxValue_shouldThrowExceptionOnOverflow() {
        MAX_VALUE.plus(1);
    }

    @Test
    public void plus_minutes_shouldAddMinutes() {
        Minutes twoMinutes = Minutes.minutes(2);
        Minutes threeMinutes = Minutes.minutes(3);
        Minutes result = twoMinutes.plus(threeMinutes);

        assertEquals("Original instance should be immutable", 2, twoMinutes.getMinutes());
        assertEquals(5, result.getMinutes());
        assertEquals(1, ONE.plus(ZERO).getMinutes());
        assertEquals(1, ONE.plus((Minutes) null).getMinutes());
    }

    @Test(expected = ArithmeticException.class)
    public void plus_minutes_forMaxValue_shouldThrowExceptionOnOverflow() {
        MAX_VALUE.plus(ONE);
    }

    @Test
    public void minus_int_shouldSubtractMinutes() {
        Minutes twoMinutes = Minutes.minutes(2);
        Minutes result = twoMinutes.minus(3);

        assertEquals("Original instance should be immutable", 2, twoMinutes.getMinutes());
        assertEquals(-1, result.getMinutes());
        assertEquals(1, ONE.minus(0).getMinutes());
    }

    @Test(expected = ArithmeticException.class)
    public void minus_int_forMinValue_shouldThrowExceptionOnOverflow() {
        MIN_VALUE.minus(1);
    }

    @Test
    public void minus_minutes_shouldSubtractMinutes() {
        Minutes twoMinutes = Minutes.minutes(2);
        Minutes threeMinutes = Minutes.minutes(3);
        Minutes result = twoMinutes.minus(threeMinutes);

        assertEquals("Original instance should be immutable", 2, twoMinutes.getMinutes());
        assertEquals(-1, result.getMinutes());
        assertEquals(1, ONE.minus(ZERO).getMinutes());
        assertEquals(1, ONE.minus((Minutes) null).getMinutes());
    }

    @Test(expected = ArithmeticException.class)
    public void minus_minutes_forMinValue_shouldThrowExceptionOnOverflow() {
        MIN_VALUE.minus(ONE);
    }

    @Test
    public void multipliedBy_shouldScaleMinutes() {
        Minutes twoMinutes = Minutes.minutes(2);
        
        assertEquals(6, twoMinutes.multipliedBy(3).getMinutes());
        assertEquals("Original instance should be immutable", 2, twoMinutes.getMinutes());
        assertEquals(-6, twoMinutes.multipliedBy(-3).getMinutes());
        assertSame(twoMinutes, twoMinutes.multipliedBy(1));
    }

    @Test(expected = ArithmeticException.class)
    public void multipliedBy_whenResultOverflows_shouldThrowException() {
        Minutes largeValue = Minutes.minutes(Integer.MAX_VALUE / 2 + 1);
        largeValue.multipliedBy(2);
    }

    @Test
    public void dividedBy_shouldScaleMinutes() {
        Minutes twelveMinutes = Minutes.minutes(12);
        
        assertEquals(6, twelveMinutes.dividedBy(2).getMinutes());
        assertEquals("Original instance should be immutable", 12, twelveMinutes.getMinutes());
        assertEquals(4, twelveMinutes.dividedBy(3).getMinutes());
        assertEquals(3, twelveMinutes.dividedBy(4).getMinutes());
        assertEquals(2, twelveMinutes.dividedBy(5).getMinutes());
        assertEquals(2, twelveMinutes.dividedBy(6).getMinutes());
        assertSame(twelveMinutes, twelveMinutes.dividedBy(1));
    }

    @Test(expected = ArithmeticException.class)
    public void dividedBy_zero_shouldThrowException() {
        ONE.dividedBy(0);
    }

    @Test
    public void negated_shouldReverseSign() {
        Minutes twelveMinutes = Minutes.minutes(12);
        assertEquals(-12, twelveMinutes.negated().getMinutes());
        assertEquals("Original instance should be immutable", 12, twelveMinutes.getMinutes());
    }

    @Test(expected = ArithmeticException.class)
    public void negated_forMinValue_shouldThrowExceptionOnOverflow() {
        MIN_VALUE.negated();
    }

    //-----------------------------------------------------------------------
    @Test
    public void addTo_localDateTime_shouldAddMinutes() {
        Minutes test = Minutes.minutes(26);
        LocalDateTime dateTime = new LocalDateTime(2006, 6, 1, 0, 0, 0, 0);
        LocalDateTime expected = new LocalDateTime(2006, 6, 1, 0, 26, 0, 0);
        assertEquals(expected, dateTime.plus(test));
    }
}