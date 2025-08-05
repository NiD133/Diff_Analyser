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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Weeks;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class TestSeconds {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final int MILLIS_PER_SECOND = DateTimeConstants.MILLIS_PER_SECOND;

    @Before
    public void setUp() throws Exception {
    }

    //-----------------------------------------------------------------------
    @Test
    public void testConstants() {
        assertEquals("ZERO", 0, Seconds.ZERO.getSeconds());
        assertEquals("ONE", 1, Seconds.ONE.getSeconds());
        assertEquals("TWO", 2, Seconds.TWO.getSeconds());
        assertEquals("THREE", 3, Seconds.THREE.getSeconds());
        assertEquals("MAX_VALUE", Integer.MAX_VALUE, Seconds.MAX_VALUE.getSeconds());
        assertEquals("MIN_VALUE", Integer.MIN_VALUE, Seconds.MIN_VALUE.getSeconds());
    }

    //-----------------------------------------------------------------------
    public static class FactorySecondsIntTests {
        @Test
        public void factory_secondsInt_shouldReturnCachedInstancesForCommonValues() {
            assertSame("Zero should be cached", Seconds.ZERO, Seconds.seconds(0));
            assertSame("One should be cached", Seconds.ONE, Seconds.seconds(1));
            assertSame("Two should be cached", Seconds.TWO, Seconds.seconds(2));
            assertSame("Three should be cached", Seconds.THREE, Seconds.seconds(3));
            assertSame("MAX_VALUE should be cached", Seconds.MAX_VALUE, Seconds.seconds(Integer.MAX_VALUE));
            assertSame("MIN_VALUE should be cached", Seconds.MIN_VALUE, Seconds.seconds(Integer.MIN_VALUE));
        }

        @Test
        public void factory_secondsInt_shouldCreateNewInstanceForUncommonValues() {
            assertEquals("Negative value", -1, Seconds.seconds(-1).getSeconds());
            assertEquals("Positive value", 4, Seconds.seconds(4).getSeconds());
        }
    }

    //-----------------------------------------------------------------------
    public static class FactorySecondsBetweenTests {
        private final DateTime start = new DateTime(2006, 6, 9, 12, 0, 3, 0, PARIS);
        private final DateTime end1 = new DateTime(2006, 6, 9, 12, 0, 6, 0, PARIS);
        private final DateTime end2 = new DateTime(2006, 6, 9, 12, 0, 9, 0, PARIS);

        @Test
        public void secondsBetween_instants_shouldCalculateCorrectDifference() {
            assertEquals(3, Seconds.secondsBetween(start, end1).getSeconds());
            assertEquals(6, Seconds.secondsBetween(start, end2).getSeconds());
        }

        @Test
        public void secondsBetween_sameInstant_shouldReturnZero() {
            assertEquals(0, Seconds.secondsBetween(start, start).getSeconds());
            assertEquals(0, Seconds.secondsBetween(end1, end1).getSeconds());
        }

        @Test
        public void secondsBetween_reversedInstants_shouldReturnNegativeValue() {
            assertEquals(-3, Seconds.secondsBetween(end1, start).getSeconds());
        }
    }

    public static class FactorySecondsBetweenPartialTests {
        private final LocalTime start = new LocalTime(12, 0, 3);
        private final LocalTime end1 = new LocalTime(12, 0, 6);
        private final LocalTime end2 = new LocalTime(12, 0, 9);

        @Test
        public void secondsBetween_partials_shouldCalculateCorrectDifference() {
            assertEquals(3, Seconds.secondsBetween(start, end1).getSeconds());
            assertEquals(6, Seconds.secondsBetween(start, end2).getSeconds());
        }

        @Test
        public void secondsBetween_samePartial_shouldReturnZero() {
            assertEquals(0, Seconds.secondsBetween(start, start).getSeconds());
            assertEquals(0, Seconds.secondsBetween(end1, end1).getSeconds());
        }

        @Test
        public void secondsBetween_reversedPartials_shouldReturnNegativeValue() {
            assertEquals(-3, Seconds.secondsBetween(end1, start).getSeconds());
        }
    }

    public static class FactorySecondsInIntervalTests {
        private final DateTime start = new DateTime(2006, 6, 9, 12, 0, 3, 0, PARIS);
        private final DateTime end1 = new DateTime(2006, 6, 9, 12, 0, 6, 0, PARIS);
        private final DateTime end2 = new DateTime(2006, 6, 9, 12, 0, 9, 0, PARIS);

        @Test
        public void secondsIn_nullInterval_shouldReturnZero() {
            assertEquals(0, Seconds.secondsIn((ReadableInterval) null).getSeconds());
        }

        @Test
        public void secondsIn_interval_shouldCalculateCorrectSeconds() {
            assertEquals(3, Seconds.secondsIn(new Interval(start, end1)).getSeconds());
            assertEquals(6, Seconds.secondsIn(new Interval(start, end2)).getSeconds());
        }

        @Test
        public void secondsIn_zeroLengthInterval_shouldReturnZero() {
            assertEquals(0, Seconds.secondsIn(new Interval(start, start)).getSeconds());
            assertEquals(0, Seconds.secondsIn(new Interval(end1, end1)).getSeconds());
        }
    }

    public static class FactoryStandardSecondsInPeriodTests {
        @Test
        public void standardSecondsIn_nullPeriod_shouldReturnZero() {
            assertEquals(0, Seconds.standardSecondsIn(null).getSeconds());
        }

        @Test
        public void standardSecondsIn_zeroPeriod_shouldReturnZero() {
            assertEquals(0, Seconds.standardSecondsIn(Period.ZERO).getSeconds());
        }

        @Test
        public void standardSecondsIn_periodWithSeconds_shouldReturnCorrectValue() {
            assertEquals(1, Seconds.standardSecondsIn(Period.seconds(1)).getSeconds());
            assertEquals(123, Seconds.standardSecondsIn(Period.seconds(123)).getSeconds());
            assertEquals(-987, Seconds.standardSecondsIn(Period.seconds(-987)).getSeconds());
        }

        @Test
        public void standardSecondsIn_periodWithOtherFields_shouldConvertCorrectly() {
            assertEquals(2 * 24 * 60 * 60, Seconds.standardSecondsIn(Period.days(2)).getSeconds());
        }

        @Test(expected = IllegalArgumentException.class)
        public void standardSecondsIn_periodWithMonths_shouldThrowException() {
            Seconds.standardSecondsIn(Period.months(1));
        }
    }

    public static class FactoryParseSecondsTests {
        @Test
        public void parseSeconds_nullString_shouldReturnZero() {
            assertEquals(0, Seconds.parseSeconds(null).getSeconds());
        }

        @Test
        public void parseSeconds_validString_shouldParseCorrectly() {
            assertEquals(0, Seconds.parseSeconds("PT0S").getSeconds());
            assertEquals(1, Seconds.parseSeconds("PT1S").getSeconds());
            assertEquals(-3, Seconds.parseSeconds("PT-3S").getSeconds());
            assertEquals(2, Seconds.parseSeconds("P0Y0M0DT2S").getSeconds());
            assertEquals(2, Seconds.parseSeconds("PT0H2S").getSeconds());
        }

        @Test(expected = IllegalArgumentException.class)
        public void parseSeconds_invalidStringWithYearAndDay_shouldThrowException() {
            Seconds.parseSeconds("P1Y1D");
        }

        @Test(expected = IllegalArgumentException.class)
        public void parseSeconds_invalidStringWithDayAndSecond_shouldThrowException() {
            Seconds.parseSeconds("P1DT1S");
        }
    }

    //-----------------------------------------------------------------------
    public static class GetterTests {
        @Test
        public void getSeconds_shouldReturnCorrectValue() {
            Seconds test = Seconds.seconds(20);
            assertEquals(20, test.getSeconds());
        }

        @Test
        public void getFieldType_shouldReturnSecondsType() {
            Seconds test = Seconds.seconds(20);
            assertEquals(DurationFieldType.seconds(), test.getFieldType());
        }

        @Test
        public void getPeriodType_shouldReturnSecondsType() {
            Seconds test = Seconds.seconds(20);
            assertEquals(PeriodType.seconds(), test.getPeriodType());
        }
    }

    //-----------------------------------------------------------------------
    public static class ComparisonTests {
        @Test
        public void isGreaterThan_shouldReturnCorrectBoolean() {
            assertTrue("3 > 2", Seconds.THREE.isGreaterThan(Seconds.TWO));
            assertTrue("1 > null", Seconds.ONE.isGreaterThan(null));
        }

        @Test
        public void isLessThan_shouldReturnCorrectBoolean() {
            assertTrue("2 < 3", Seconds.TWO.isLessThan(Seconds.THREE));
            assertTrue("-1 < null", Seconds.seconds(-1).isLessThan(null));
        }
    }

    //-----------------------------------------------------------------------
    @Test
    public void testToString() {
        assertEquals("PT20S", Seconds.seconds(20).toString());
        assertEquals("PT-20S", Seconds.seconds(-20).toString());
    }

    //-----------------------------------------------------------------------
    @Test
    public void testSerialization() throws Exception {
        Seconds test = Seconds.THREE;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test);
        oos.close();
        byte[] bytes = baos.toByteArray();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Seconds result = (Seconds) ois.readObject();
        ois.close();
        
        assertSame(test, result);
    }

    //-----------------------------------------------------------------------
    public static class ConversionTests {
        @Test
        public void toStandardWeeks_shouldConvertCorrectly() {
            Seconds test = Seconds.seconds(60 * 60 * 24 * 7 * 2);
            assertEquals(Weeks.weeks(2), test.toStandardWeeks());
        }

        @Test
        public void toStandardDays_shouldConvertCorrectly() {
            Seconds test = Seconds.seconds(60 * 60 * 24 * 2);
            assertEquals(Days.days(2), test.toStandardDays());
        }

        @Test
        public void toStandardHours_shouldConvertCorrectly() {
            Seconds test = Seconds.seconds(60 * 60 * 2);
            assertEquals(Hours.hours(2), test.toStandardHours());
        }

        @Test
        public void toStandardMinutes_shouldConvertCorrectly() {
            Seconds test = Seconds.seconds(60 * 2);
            assertEquals(Minutes.minutes(2), test.toStandardMinutes());
        }

        @Test
        public void toStandardDuration_shouldConvertCorrectly() {
            Seconds test = Seconds.seconds(20);
            Duration expected = new Duration(20L * MILLIS_PER_SECOND);
            assertEquals(expected, test.toStandardDuration());
        }

        @Test
        public void toStandardDuration_maxValue_shouldConvertCorrectly() {
            Duration expected = new Duration((long) Integer.MAX_VALUE * MILLIS_PER_SECOND);
            assertEquals(expected, Seconds.MAX_VALUE.toStandardDuration());
        }
    }

    //-----------------------------------------------------------------------
    public static class ArithmeticOperationsTests {
        @Test
        public void plus_int_shouldAddCorrectly() {
            Seconds test2 = Seconds.seconds(2);
            Seconds result = test2.plus(3);
            assertEquals(2, test2.getSeconds());
            assertEquals(5, result.getSeconds());
        }

        @Test
        public void plus_intZero_shouldReturnSame() {
            assertSame(Seconds.ONE, Seconds.ONE.plus(0));
        }

        @Test(expected = ArithmeticException.class)
        public void plus_intOverflow_shouldThrowException() {
            Seconds.MAX_VALUE.plus(1);
        }

        @Test
        public void plus_seconds_shouldAddCorrectly() {
            Seconds test2 = Seconds.seconds(2);
            Seconds test3 = Seconds.seconds(3);
            Seconds result = test2.plus(test3);
            assertEquals(5, result.getSeconds());
        }

        @Test
        public void plus_nullSeconds_shouldTreatAsZero() {
            assertEquals(1, Seconds.ONE.plus((Seconds) null).getSeconds());
        }

        @Test(expected = ArithmeticException.class)
        public void plus_secondsOverflow_shouldThrowException() {
            Seconds.MAX_VALUE.plus(Seconds.ONE);
        }

        @Test
        public void minus_int_shouldSubtractCorrectly() {
            Seconds test2 = Seconds.seconds(2);
            Seconds result = test2.minus(3);
            assertEquals(-1, result.getSeconds());
        }

        @Test
        public void minus_intZero_shouldReturnSame() {
            assertSame(Seconds.ONE, Seconds.ONE.minus(0));
        }

        @Test(expected = ArithmeticException.class)
        public void minus_intUnderflow_shouldThrowException() {
            Seconds.MIN_VALUE.minus(1);
        }

        @Test
        public void minus_seconds_shouldSubtractCorrectly() {
            Seconds test2 = Seconds.seconds(2);
            Seconds test3 = Seconds.seconds(3);
            Seconds result = test2.minus(test3);
            assertEquals(-1, result.getSeconds());
        }

        @Test
        public void minus_nullSeconds_shouldTreatAsZero() {
            assertEquals(1, Seconds.ONE.minus((Seconds) null).getSeconds());
        }

        @Test(expected = ArithmeticException.class)
        public void minus_secondsUnderflow_shouldThrowException() {
            Seconds.MIN_VALUE.minus(Seconds.ONE);
        }

        @Test
        public void multipliedBy_positive_shouldMultiplyCorrectly() {
            Seconds test = Seconds.seconds(2);
            assertEquals(6, test.multipliedBy(3).getSeconds());
        }

        @Test
        public void multipliedBy_negative_shouldMultiplyCorrectly() {
            Seconds test = Seconds.seconds(2);
            assertEquals(-6, test.multipliedBy(-3).getSeconds());
        }

        @Test
        public void multipliedBy_one_shouldReturnSame() {
            Seconds test = Seconds.seconds(2);
            assertSame(test, test.multipliedBy(1));
        }

        @Test(expected = ArithmeticException.class)
        public void multipliedBy_overflow_shouldThrowException() {
            Seconds halfMax = Seconds.seconds(Integer.MAX_VALUE / 2 + 1);
            halfMax.multipliedBy(2);
        }

        @Test
        public void dividedBy_shouldDivideCorrectly() {
            Seconds test = Seconds.seconds(12);
            assertEquals(6, test.dividedBy(2).getSeconds());
            assertEquals(4, test.dividedBy(3).getSeconds());
            assertEquals(3, test.dividedBy(4).getSeconds());
            assertEquals(2, test.dividedBy(5).getSeconds());
            assertEquals(2, test.dividedBy(6).getSeconds());
        }

        @Test
        public void dividedBy_one_shouldReturnSame() {
            Seconds test = Seconds.seconds(12);
            assertSame(test, test.dividedBy(1));
        }

        @Test(expected = ArithmeticException.class)
        public void dividedBy_zero_shouldThrowException() {
            Seconds.ONE.dividedBy(0);
        }

        @Test
        public void negated_positive_shouldNegate() {
            Seconds test = Seconds.seconds(12);
            assertEquals(-12, test.negated().getSeconds());
        }

        @Test(expected = ArithmeticException.class)
        public void negated_minValue_shouldThrowException() {
            Seconds.MIN_VALUE.negated();
        }
    }

    //-----------------------------------------------------------------------
    @Test
    public void testAddToLocalDate() {
        Seconds test = Seconds.seconds(26);
        LocalDateTime date = new LocalDateTime(2006, 6, 1, 0, 0, 0, 0);
        LocalDateTime expected = new LocalDateTime(2006, 6, 1, 0, 0, 26, 0);
        assertEquals(expected, date.plus(test));
    }
}