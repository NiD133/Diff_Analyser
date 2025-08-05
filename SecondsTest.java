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

import static org.junit.Assert.*;

/**
 * This class is a Junit unit test for Seconds.
 *
 * @author Stephen Colebourne
 */
public class TestSeconds {
    // Test in 2002/03 as time zones are more well known
    // (before the late 90's they were all over the place)
    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    //-----------------------------------------------------------------------
    @Test
    public void constants_shouldHoldCorrectValues() {
        assertEquals(0, Seconds.ZERO.getSeconds());
        assertEquals(1, Seconds.ONE.getSeconds());
        assertEquals(2, Seconds.TWO.getSeconds());
        assertEquals(3, Seconds.THREE.getSeconds());
        assertEquals(Integer.MAX_VALUE, Seconds.MAX_VALUE.getSeconds());
        assertEquals(Integer.MIN_VALUE, Seconds.MIN_VALUE.getSeconds());
    }

    //-----------------------------------------------------------------------
    // Factory methods
    //-----------------------------------------------------------------------
    @Test
    public void secondsFactory_shouldReturnCachedInstancesForSmallValues() {
        assertSame(Seconds.ZERO, Seconds.seconds(0));
        assertSame(Seconds.ONE, Seconds.seconds(1));
        assertSame(Seconds.TWO, Seconds.seconds(2));
        assertSame(Seconds.THREE, Seconds.seconds(3));
        assertSame(Seconds.MAX_VALUE, Seconds.seconds(Integer.MAX_VALUE));
        assertSame(Seconds.MIN_VALUE, Seconds.seconds(Integer.MIN_VALUE));
    }

    @Test
    public void secondsFactory_shouldCreateNewInstancesForOtherValues() {
        assertEquals(-1, Seconds.seconds(-1).getSeconds());
        assertEquals(4, Seconds.seconds(4).getSeconds());
    }

    @Test
    public void secondsBetween_instant_shouldCalculateDifference() {
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 3, 0, PARIS);
        DateTime endThreeSecsLater = new DateTime(2006, 6, 9, 12, 0, 6, 0, PARIS);
        DateTime endSixSecsLater = new DateTime(2006, 6, 9, 12, 0, 9, 0, PARIS);

        assertEquals(3, Seconds.secondsBetween(start, endThreeSecsLater).getSeconds());
        assertEquals(0, Seconds.secondsBetween(start, start).getSeconds());
        assertEquals(0, Seconds.secondsBetween(endThreeSecsLater, endThreeSecsLater).getSeconds());
        assertEquals(-3, Seconds.secondsBetween(endThreeSecsLater, start).getSeconds());
        assertEquals(6, Seconds.secondsBetween(start, endSixSecsLater).getSeconds());
    }

    @Test
    public void secondsBetween_partial_shouldCalculateDifference() {
        LocalTime start = new LocalTime(12, 0, 3);
        LocalTime end = new LocalTime(12, 0, 6);
        
        assertEquals(3, Seconds.secondsBetween(start, end).getSeconds());
        assertEquals(0, Seconds.secondsBetween(start, start).getSeconds());
        assertEquals(-3, Seconds.secondsBetween(end, start).getSeconds());
    }

    @Test
    public void secondsIn_interval_shouldReturnSecondsOfInterval() {
        DateTime start = new DateTime(2006, 6, 9, 12, 0, 3, 0, PARIS);
        DateTime endThreeSecsLater = new DateTime(2006, 6, 9, 12, 0, 6, 0, PARIS);
        
        assertEquals(0, Seconds.secondsIn(null).getSeconds());
        assertEquals(3, Seconds.secondsIn(new Interval(start, endThreeSecsLater)).getSeconds());
        assertEquals(0, Seconds.secondsIn(new Interval(start, start)).getSeconds());
    }

    @Test
    public void standardSecondsIn_period_shouldReturnCorrectSeconds() {
        assertEquals(0, Seconds.standardSecondsIn(null).getSeconds());
        assertEquals(0, Seconds.standardSecondsIn(Period.ZERO).getSeconds());
        assertEquals(1, Seconds.standardSecondsIn(Period.seconds(1)).getSeconds());
        assertEquals(123, Seconds.standardSecondsIn(Period.seconds(123)).getSeconds());
        assertEquals(-987, Seconds.standardSecondsIn(Period.seconds(-987)).getSeconds());
        assertEquals(2 * DateTimeConstants.SECONDS_PER_DAY, Seconds.standardSecondsIn(Period.days(2)).getSeconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void standardSecondsIn_period_shouldThrowExceptionForImprecisePeriods() {
        Seconds.standardSecondsIn(Period.months(1));
    }

    @Test
    public void parseSeconds_shouldParseValidISOString() {
        assertEquals(0, Seconds.parseSeconds(null).getSeconds());
        assertEquals(0, Seconds.parseSeconds("PT0S").getSeconds());
        assertEquals(1, Seconds.parseSeconds("PT1S").getSeconds());
        assertEquals(-3, Seconds.parseSeconds("PT-3S").getSeconds());
        assertEquals(2, Seconds.parseSeconds("P0Y0M0DT2S").getSeconds());
        assertEquals(2, Seconds.parseSeconds("PT0H2S").getSeconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseSeconds_shouldThrowExceptionForYears() {
        Seconds.parseSeconds("P1Y");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseSeconds_shouldThrowExceptionForDays() {
        Seconds.parseSeconds("P1D");
    }

    //-----------------------------------------------------------------------
    // Getters
    //-----------------------------------------------------------------------
    @Test
    public void getters_shouldReturnCorrectValues() {
        Seconds test = Seconds.seconds(20);
        assertEquals(20, test.getSeconds());
        assertEquals(DurationFieldType.seconds(), test.getFieldType());
        assertEquals(PeriodType.seconds(), test.getPeriodType());
    }

    //-----------------------------------------------------------------------
    // Comparison methods
    //-----------------------------------------------------------------------
    @Test
    public void isGreaterThan_shouldCorrectlyCompareSeconds() {
        assertTrue(Seconds.THREE.isGreaterThan(Seconds.TWO));
        assertFalse(Seconds.THREE.isGreaterThan(Seconds.THREE));
        assertFalse(Seconds.TWO.isGreaterThan(Seconds.THREE));
        assertTrue(Seconds.ONE.isGreaterThan(null)); // null is treated as zero
        assertFalse(Seconds.seconds(-1).isGreaterThan(null));
    }

    @Test
    public void isLessThan_shouldCorrectlyCompareSeconds() {
        assertFalse(Seconds.THREE.isLessThan(Seconds.TWO));
        assertFalse(Seconds.THREE.isLessThan(Seconds.THREE));
        assertTrue(Seconds.TWO.isLessThan(Seconds.THREE));
        assertFalse(Seconds.ONE.isLessThan(null)); // null is treated as zero
        assertTrue(Seconds.seconds(-1).isLessThan(null));
    }

    //-----------------------------------------------------------------------
    @Test
    public void toString_shouldReturnISO8601Format() {
        assertEquals("PT20S", Seconds.seconds(20).toString());
        assertEquals("PT-20S", Seconds.seconds(-20).toString());
    }

    @Test
    public void serialization_shouldPreserveSingletonInstances() throws Exception {
        Seconds original = Seconds.THREE;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(original);
        oos.close();
        byte[] bytes = baos.toByteArray();

        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Seconds deserialized = (Seconds) ois.readObject();
        ois.close();

        assertSame("Deserialization should return the same singleton instance", original, deserialized);
    }

    //-----------------------------------------------------------------------
    // Converters
    //-----------------------------------------------------------------------
    @Test
    public void toStandardWeeks_shouldConvertSecondsToWeeks() {
        int twoWeeksInSeconds = 2 * DateTimeConstants.SECONDS_PER_WEEK;
        Seconds seconds = Seconds.seconds(twoWeeksInSeconds);
        assertEquals(Weeks.weeks(2), seconds.toStandardWeeks());
    }

    @Test
    public void toStandardDays_shouldConvertSecondsToDays() {
        int twoDaysInSeconds = 2 * DateTimeConstants.SECONDS_PER_DAY;
        Seconds seconds = Seconds.seconds(twoDaysInSeconds);
        assertEquals(Days.days(2), seconds.toStandardDays());
    }

    @Test
    public void toStandardHours_shouldConvertSecondsToHours() {
        int twoHoursInSeconds = 2 * DateTimeConstants.SECONDS_PER_HOUR;
        Seconds seconds = Seconds.seconds(twoHoursInSeconds);
        assertEquals(Hours.hours(2), seconds.toStandardHours());
    }

    @Test
    public void toStandardMinutes_shouldConvertSecondsToMinutes() {
        int twoMinutesInSeconds = 2 * DateTimeConstants.SECONDS_PER_MINUTE;
        Seconds seconds = Seconds.seconds(twoMinutesInSeconds);
        assertEquals(Minutes.minutes(2), seconds.toStandardMinutes());
    }

    @Test
    public void toStandardDuration_shouldReturnEquivalentDuration() {
        Seconds seconds = Seconds.seconds(20);
        Duration expected = new Duration(20L * DateTimeConstants.MILLIS_PER_SECOND);
        assertEquals(expected, seconds.toStandardDuration());

        Duration maxExpected = new Duration((long) Integer.MAX_VALUE * DateTimeConstants.MILLIS_PER_SECOND);
        assertEquals(maxExpected, Seconds.MAX_VALUE.toStandardDuration());
    }

    //-----------------------------------------------------------------------
    // Mathematical operations
    //-----------------------------------------------------------------------
    @Test
    public void plus_int_shouldAddValueAndReturnNewInstance() {
        Seconds twoSeconds = Seconds.seconds(2);
        Seconds fiveSeconds = twoSeconds.plus(3);

        assertEquals("Original instance should be immutable", 2, twoSeconds.getSeconds());
        assertEquals(5, fiveSeconds.getSeconds());
        assertEquals(1, Seconds.ONE.plus(0).getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void plus_int_shouldThrowExceptionOnOverflow() {
        Seconds.MAX_VALUE.plus(1);
    }

    @Test
    public void plus_seconds_shouldAddValueAndReturnNewInstance() {
        Seconds twoSeconds = Seconds.seconds(2);
        Seconds threeSeconds = Seconds.seconds(3);
        Seconds fiveSeconds = twoSeconds.plus(threeSeconds);

        assertEquals("Original instance should be immutable", 2, twoSeconds.getSeconds());
        assertEquals("Original instance should be immutable", 3, threeSeconds.getSeconds());
        assertEquals(5, fiveSeconds.getSeconds());
    }
    
    @Test
    public void plus_seconds_shouldHandleNullAsZero() {
        assertEquals(1, Seconds.ONE.plus(Seconds.ZERO).getSeconds());
        assertEquals(1, Seconds.ONE.plus((Seconds) null).getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void plus_seconds_shouldThrowExceptionOnOverflow() {
        Seconds.MAX_VALUE.plus(Seconds.ONE);
    }

    @Test
    public void minus_int_shouldSubtractValueAndReturnNewInstance() {
        Seconds twoSeconds = Seconds.seconds(2);
        Seconds result = twoSeconds.minus(3);

        assertEquals("Original instance should be immutable", 2, twoSeconds.getSeconds());
        assertEquals(-1, result.getSeconds());
        assertEquals(1, Seconds.ONE.minus(0).getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void minus_int_shouldThrowExceptionOnOverflow() {
        Seconds.MIN_VALUE.minus(1);
    }

    @Test
    public void minus_seconds_shouldSubtractValueAndReturnNewInstance() {
        Seconds twoSeconds = Seconds.seconds(2);
        Seconds threeSeconds = Seconds.seconds(3);
        Seconds result = twoSeconds.minus(threeSeconds);

        assertEquals("Original instance should be immutable", 2, twoSeconds.getSeconds());
        assertEquals("Original instance should be immutable", 3, threeSeconds.getSeconds());
        assertEquals(-1, result.getSeconds());
    }
    
    @Test
    public void minus_seconds_shouldHandleNullAsZero() {
        assertEquals(1, Seconds.ONE.minus(Seconds.ZERO).getSeconds());
        assertEquals(1, Seconds.ONE.minus((Seconds) null).getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void minus_seconds_shouldThrowExceptionOnOverflow() {
        Seconds.MIN_VALUE.minus(Seconds.ONE);
    }

    @Test
    public void multipliedBy_shouldScaleValueAndReturnNewInstance() {
        Seconds twoSeconds = Seconds.seconds(2);
        
        assertEquals(6, twoSeconds.multipliedBy(3).getSeconds());
        assertEquals(-6, twoSeconds.multipliedBy(-3).getSeconds());
        assertEquals("Original instance should be immutable", 2, twoSeconds.getSeconds());
        assertSame("Multiplying by 1 should return same instance", twoSeconds, twoSeconds.multipliedBy(1));
    }

    @Test(expected = ArithmeticException.class)
    public void multipliedBy_shouldThrowExceptionOnOverflow() {
        Seconds.seconds(Integer.MAX_VALUE / 2 + 1).multipliedBy(2);
    }

    @Test
    public void dividedBy_shouldScaleValueAndReturnNewInstance() {
        Seconds twelveSeconds = Seconds.seconds(12);
        
        assertEquals(6, twelveSeconds.dividedBy(2).getSeconds());
        assertEquals(4, twelveSeconds.dividedBy(3).getSeconds());
        assertEquals(3, twelveSeconds.dividedBy(4).getSeconds());
        assertEquals(2, twelveSeconds.dividedBy(5).getSeconds()); // integer division
        assertEquals("Original instance should be immutable", 12, twelveSeconds.getSeconds());
        assertSame("Dividing by 1 should return same instance", twelveSeconds, twelveSeconds.dividedBy(1));
    }

    @Test(expected = ArithmeticException.class)
    public void dividedBy_byZero_shouldThrowException() {
        Seconds.ONE.dividedBy(0);
    }

    @Test
    public void negated_shouldReverseSignAndReturnNewInstance() {
        Seconds twelveSeconds = Seconds.seconds(12);
        assertEquals(-12, twelveSeconds.negated().getSeconds());
        assertEquals("Original instance should be immutable", 12, twelveSeconds.getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void negated_shouldThrowExceptionOnOverflowForMinValue() {
        Seconds.MIN_VALUE.negated();
    }

    //-----------------------------------------------------------------------
    // Integration with other Joda-Time classes
    //-----------------------------------------------------------------------
    @Test
    public void localDateTimePlus_shouldAddSecondsCorrectly() {
        Seconds seconds = Seconds.seconds(26);
        LocalDateTime startDateTime = new LocalDateTime(2006, 6, 1, 0, 0, 0, 0);
        LocalDateTime expectedDateTime = new LocalDateTime(2006, 6, 1, 0, 0, 26, 0);
        
        assertEquals(expectedDateTime, startDateTime.plus(seconds));
    }
}