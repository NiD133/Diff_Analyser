/*
 * Refactored test suite for AbstractPartial for better readability and maintainability.
 */
package org.joda.time.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.Date;
import java.util.Locale;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.util.MockGregorianCalendar;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MonthDay;
import org.joda.time.Partial;
import org.joda.time.ReadablePartial;
import org.joda.time.Weeks;
import org.joda.time.YearMonth;
import org.joda.time.Years;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class AbstractPartial_ESTest extends AbstractPartial_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void testIsEqual_afterAddingMonths_returnsFalse()  {
      YearMonth yearMonth0 = new YearMonth();
      YearMonth yearMonth1 = yearMonth0.plusMonths(4989);
      boolean boolean0 = yearMonth1.isEqual(yearMonth0);
      assertFalse(boolean0);
      assertEquals(11, yearMonth1.getMonthOfYear());
  }

  @Test(timeout = 4000)
  public void testIsAfter_withLargeMillisOfDay_returnsFalse()  {
      IslamicChronology islamicChronology0 = IslamicChronology.getInstanceUTC();
      LocalTime localTime0 = LocalTime.fromMillisOfDay(9223372036854775807L, (Chronology) islamicChronology0);
      LocalDateTime localDateTime0 = LocalDateTime.now();
      LocalDateTime localDateTime1 = localDateTime0.withFields(localTime0);
      boolean boolean0 = localDateTime1.isAfter(localDateTime0);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void testEquals_differentPartialTypes_returnsFalse()  {
      DateTimeZone dateTimeZone0 = DateTimeZone.forOffsetMillis((-326));
      IslamicChronology.LeapYearPatternType islamicChronology_LeapYearPatternType0 = IslamicChronology.LEAP_YEAR_INDIAN;
      IslamicChronology islamicChronology0 = IslamicChronology.getInstance(dateTimeZone0, islamicChronology_LeapYearPatternType0);
      LocalDateTime localDateTime0 = LocalDateTime.now((Chronology) islamicChronology0);
      LocalTime localTime0 = new LocalTime(localDateTime0, dateTimeZone0);
      boolean boolean0 = localTime0.equals(localDateTime0);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void testEquals_localDateTimeAndLocalDate_returnsFalse()  {
      LocalDateTime localDateTime0 = new LocalDateTime();
      LocalDate localDate0 = new LocalDate(localDateTime0);
      boolean boolean0 = localDateTime0.equals(localDate0);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void testToString_withMockFormatter_returnsEmptyString()  {
      Instant instant0 = Instant.now();
      DateTimeZone dateTimeZone0 = instant0.getZone();
      LocalDate localDate0 = new LocalDate(dateTimeZone0);
      DateTimePrinter dateTimePrinter0 = mock(DateTimePrinter.class, new ViolatedAssumptionAnswer());
      doReturn(0, 0).when(dateTimePrinter0).estimatePrintedLength();
      DateTimeParser dateTimeParser0 = mock(DateTimeParser.class, new ViolatedAssumptionAnswer());
      DateTimeFormatter dateTimeFormatter0 = new DateTimeFormatter(dateTimePrinter0, dateTimeParser0);
      String string0 = localDate0.toString(dateTimeFormatter0);
      assertEquals("", string0);
  }

  @Test(timeout = 4000)
  public void testToDateTime_withInstantNow_setsMillisCorrectly()  {
      YearMonth yearMonth0 = YearMonth.now();
      Instant instant0 = Instant.now();
      DateTime dateTime0 = yearMonth0.toDateTime(instant0);
      assertEquals(1392409281320L, dateTime0.getMillis());
  }

  @Test(timeout = 4000)
  public void testToDateTime_withBaseDateTime_setsMillisCorrectly()  {
      MonthDay monthDay0 = new MonthDay();
      CopticChronology copticChronology0 = CopticChronology.getInstanceUTC();
      DateTime dateTime0 = new DateTime((-1448L), (Chronology) copticChronology0);
      DateTime dateTime1 = monthDay0.toDateTime(dateTime0);
      assertEquals((-5875201448L), dateTime1.getMillis());
  }

  @Test(timeout = 4000)
  public void testIsSupported_yearOfEra_returnsTrue()  {
      LocalDate localDate0 = new LocalDate();
      DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.yearOfEra();
      boolean boolean0 = localDate0.isSupported(dateTimeFieldType0);
      assertTrue(boolean0);
  }

  @Test(timeout = 4000)
  public void testIndexOf_monthOfYear_returns1()  {
      YearMonth yearMonth0 = new YearMonth();
      DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.monthOfYear();
      int int0 = yearMonth0.indexOf(dateTimeFieldType0);
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void testGetFields_emptyPartial_returnsEmptyArray()  {
      Partial partial0 = new Partial();
      DateTimeField[] dateTimeFieldArray0 = partial0.getFields();
      assertEquals(0, dateTimeFieldArray0.length);
  }

  @Test(timeout = 4000)
  public void testGetFieldType_monthDayIndex1_returnsDayOfMonth()  {
      MonthDay monthDay0 = MonthDay.now();
      DateTimeFieldType dateTimeFieldType0 = monthDay0.getFieldType(1);
      assertEquals("dayOfMonth", dateTimeFieldType0.toString());
  }

  @Test(timeout = 4000)
  public void testGetField_yearMonthIndex0_returnsNotNull()  {
      YearMonth yearMonth0 = YearMonth.now();
      DateTimeField dateTimeField0 = yearMonth0.getField(0);
      assertNotNull(dateTimeField0);
  }

  @Test(timeout = 4000)
  public void testGet_dayOfMonth_returns1()  {
      GregorianChronology gregorianChronology0 = GregorianChronology.getInstanceUTC();
      MonthDay monthDay0 = new MonthDay(1516L, (Chronology) gregorianChronology0);
      DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.dayOfMonth();
      int int0 = monthDay0.get(dateTimeFieldType0);
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void testToString_nullPrinter_throwsUnsupportedOperationException()  {
      GJChronology gJChronology0 = GJChronology.getInstanceUTC();
      LocalDate localDate0 = new LocalDate((long) 0, (Chronology) gJChronology0);
      DateTimeFormatter dateTimeFormatter0 = new DateTimeFormatter((DateTimePrinter) null, (DateTimeParser) null);
      try { 
        localDate0.toString(dateTimeFormatter0);
        fail("Expecting exception: UnsupportedOperationException");
      } catch(UnsupportedOperationException e) {
         verifyException("org.joda.time.format.DateTimeFormatter", e);
      }
  }

  @Test(timeout = 4000)
  public void testToString_negativePrintedLength_throwsNegativeArraySizeException()  {
      DateTimePrinter dateTimePrinter0 = mock(DateTimePrinter.class, new ViolatedAssumptionAnswer());
      doReturn(0, (-689)).when(dateTimePrinter0).estimatePrintedLength();
      DateTimeParser dateTimeParser0 = mock(DateTimeParser.class, new ViolatedAssumptionAnswer());
      DateTimeFormatter dateTimeFormatter0 = new DateTimeFormatter(dateTimePrinter0, dateTimeParser0);
      DateTimeZone dateTimeZone0 = DateTimeZone.getDefault();
      LocalDate localDate0 = new LocalDate((-1279L), dateTimeZone0);
      try { 
        localDate0.toString(dateTimeFormatter0);
        fail("Expecting exception: NegativeArraySizeException");
      } catch(NegativeArraySizeException e) {
         verifyException("java.lang.AbstractStringBuilder", e);
      }
  }

  @Test(timeout = 4000)
  public void testIsEqual_mismatchedFieldTypes_throwsClassCastException()  {
      YearMonth yearMonth0 = YearMonth.now();
      LocalTime localTime0 = LocalTime.fromMillisOfDay((long) 1);
      try { 
        yearMonth0.isEqual(localTime0);
        fail("Expecting exception: ClassCastException");
      } catch(ClassCastException e) {
         verifyException("org.joda.time.base.AbstractPartial", e);
      }
  }

  @Test(timeout = 4000)
  public void testIsAfter_mismatchedFieldTypes_throwsClassCastException()  {
      GregorianChronology gregorianChronology0 = GregorianChronology.getInstance();
      YearMonth yearMonth0 = YearMonth.now();
      LocalDateTime localDateTime0 = new LocalDateTime((Chronology) gregorianChronology0);
      try { 
        localDateTime0.isAfter(yearMonth0);
        fail("Expecting exception: ClassCastException");
      } catch(ClassCastException e) {
         verifyException("org.joda.time.base.AbstractPartial", e);
      }
  }

  @Test(timeout = 4000)
  public void testGetFieldType_negativeIndex_throwsIndexOutOfBoundsException()  {
      LocalDate localDate0 = new LocalDate();
      try { 
        localDate0.getFieldType((-3285));
        fail("Expecting exception: IndexOutOfBoundsException");
      } catch(IndexOutOfBoundsException e) {
         verifyException("org.joda.time.LocalDate", e);
      }
  }

  @Test(timeout = 4000)
  public void testGetFieldType_outOfBoundsIndex_throwsArrayIndexOutOfBoundsException()  {
      YearMonth yearMonth0 = YearMonth.now();
      try { 
        yearMonth0.getFieldType(1040);
        fail("Expecting exception: ArrayIndexOutOfBoundsException");
      } catch(ArrayIndexOutOfBoundsException e) {
         verifyException("org.joda.time.YearMonth", e);
      }
  }

  @Test(timeout = 4000)
  public void testGetField_negativeIndex_throwsIndexOutOfBoundsException()  {
      YearMonth yearMonth0 = YearMonth.now();
      try { 
        yearMonth0.getField((-817));
        fail("Expecting exception: IndexOutOfBoundsException");
      } catch(IndexOutOfBoundsException e) {
         verifyException("org.joda.time.YearMonth", e);
      }
  }

  @Test(timeout = 4000)
  public void testCompareTo_null_throwsNullPointerException()  {
      YearMonth yearMonth0 = YearMonth.now();
      try { 
        yearMonth0.compareTo((ReadablePartial) null);
        fail("Expecting exception: NullPointerException");
      } catch(NullPointerException e) {
      }
  }

  @Test(timeout = 4000)
  public void testCompareTo_yearMonthAfter_returnsPositive()  {
      YearMonth yearMonth0 = YearMonth.now();
      YearMonth yearMonth1 = yearMonth0.minusYears((-2306));
      int int0 = yearMonth1.compareTo((ReadablePartial) yearMonth0);
      assertEquals(4320, yearMonth1.getYear());
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void testCompareTo_samePartial_returnsZero()  {
      MockGregorianCalendar mockGregorianCalendar0 = new MockGregorianCalendar();
      MonthDay monthDay0 = MonthDay.fromCalendarFields(mockGregorianCalendar0);
      Partial partial0 = new Partial(monthDay0);
      int int0 = partial0.compareTo((ReadablePartial) monthDay0);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void testCompareTo_mismatchedFieldTypes_throwsClassCastException()  {
      IslamicChronology islamicChronology0 = IslamicChronology.getInstanceUTC();
      LocalTime localTime0 = LocalTime.fromMillisOfDay(9223372036854775807L, (Chronology) islamicChronology0);
      LocalDateTime localDateTime0 = LocalDateTime.now();
      Partial partial0 = new Partial(localDateTime0);
      try { 
        partial0.compareTo((ReadablePartial) localTime0);
        fail("Expecting exception: ClassCastException");
      } catch(ClassCastException e) {
         verifyException("org.joda.time.base.AbstractPartial", e);
      }
  }

  @Test(timeout = 4000)
  public void testCompareTo_yearMonthBefore_returnsNegative()  {
      YearMonth yearMonth0 = new YearMonth(4, 4);
      YearMonth yearMonth1 = yearMonth0.minusYears(4);
      int int0 = yearMonth1.compareTo((ReadablePartial) yearMonth0);
      assertEquals((-1), int0);
      assertEquals(0, yearMonth1.getYear());
  }

  @Test(timeout = 4000)
  public void testCompareTo_sameYearMonth_returnsZero()  {
      YearMonth yearMonth0 = new YearMonth(4, 4);
      int int0 = yearMonth0.compareTo((ReadablePartial) yearMonth0);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void testMinus_years_subtractsYears()  {
      Years years0 = Years.TWO;
      YearMonth yearMonth0 = new YearMonth(4, 4);
      YearMonth yearMonth1 = yearMonth0.minus(years0);
      assertEquals(4, yearMonth1.getMonthOfYear());
  }

  @Test(timeout = 4000)
  public void testMinus_weeks_hasNoEffectOnMonthDay()  {
      MonthDay monthDay0 = new MonthDay();
      Weeks weeks0 = Weeks.TWO;
      MonthDay monthDay1 = monthDay0.minus(weeks0);
      assertTrue(monthDay1.equals((Object)monthDay0));
  }

  @Test(timeout = 4000)
  public void testIndexOf_year_returns0()  {
      LocalDate localDate0 = new LocalDate();
      DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.year();
      int int0 = localDate0.indexOf(dateTimeFieldType0);
      assertEquals(0, int0);
  }

  @Test(timeout = 4000)
  public void testIndexOf_era_returnsMinus1()  {
      YearMonth yearMonth0 = new YearMonth(4, 4);
      DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.era();
      int int0 = yearMonth0.indexOf(dateTimeFieldType0);
      assertEquals((-1), int0);
  }

  @Test(timeout = 4000)
  public void testToString_monthDay_returnsExpectedFormat()  {
      MonthDay monthDay0 = new MonthDay();
      String string0 = monthDay0.toString();
      assertEquals("--02-14", string0);
  }

  @Test(timeout = 4000)
  public void testToString_nullFormatter_returnsNotNull()  {
      YearMonth yearMonth0 = YearMonth.now();
      String string0 = yearMonth0.toString((DateTimeFormatter) null);
      assertNotNull(string0);
  }

  @Test(timeout = 4000)
  public void testIsEqual_differentYearMonths_returnsFalse()  {
      YearMonth yearMonth0 = new YearMonth(4, 4);
      YearMonth yearMonth1 = yearMonth0.minusYears(4);
      boolean boolean0 = yearMonth1.isEqual(yearMonth0);
      assertFalse(boolean0);
      assertEquals(4, yearMonth1.getMonthOfYear());
  }

  @Test(timeout = 4000)
  public void testIsEqual_null_throwsIllegalArgumentException()  {
      LocalDateTime localDateTime0 = LocalDateTime.now();
      try { 
        localDateTime0.isEqual((ReadablePartial) null);
        fail("Expecting exception: IllegalArgumentException");
      } catch(IllegalArgumentException e) {
         verifyException("org.joda.time.base.AbstractPartial", e);
      }
  }

  @Test(timeout = 4000)
  public void testIsEqual_sameLocalDateTime_returnsTrue()  {
      LocalDateTime localDateTime0 = LocalDateTime.now();
      boolean boolean0 = localDateTime0.isEqual(localDateTime0);
      assertTrue(boolean0);
  }

  @Test(timeout = 4000)
  public void testIsBefore_null_throwsIllegalArgumentException()  {
      MonthDay monthDay0 = new MonthDay();
      try { 
        monthDay0.isBefore((ReadablePartial) null);
        fail("Expecting exception: IllegalArgumentException");
      } catch(IllegalArgumentException e) {
         verifyException("org.joda.time.base.AbstractPartial", e);
      }
  }

  @Test(timeout = 4000)
  public void testIsAfter_afterAddingWeeks_returnsTrue()  {
      LocalDateTime localDateTime0 = new LocalDateTime();
      LocalDateTime localDateTime1 = localDateTime0.minusWeeks((-4607));
      boolean boolean0 = localDateTime1.isAfter(localDateTime0);
      assertTrue(boolean0);
  }

  @Test(timeout = 4000)
  public void testIsAfter_null_throwsIllegalArgumentException()  {
      YearMonth yearMonth0 = YearMonth.now();
      try { 
        yearMonth0.isAfter((ReadablePartial) null);
        fail("Expecting exception: IllegalArgumentException");
      } catch(IllegalArgumentException e) {
         verifyException("org.joda.time.base.AbstractPartial", e);
      }
  }

  @Test(timeout = 4000)
  public void testIsAfter_same_returnsFalse()  {
      LocalDateTime localDateTime0 = LocalDateTime.now();
      boolean boolean0 = localDateTime0.isAfter(localDateTime0);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void testIsBefore_plusOneDay_returnsTrue()  {
      MonthDay monthDay0 = new MonthDay();
      MonthDay monthDay1 = monthDay0.plusDays(1);
      boolean boolean0 = monthDay0.isBefore(monthDay1);
      assertTrue(boolean0);
      assertEquals(2, monthDay1.getMonthOfYear());
  }

  @Test(timeout = 4000)
  public void testIsBefore_mismatchedFieldTypes_throwsClassCastException()  {
      YearMonth yearMonth0 = new YearMonth();
      MonthDay monthDay0 = MonthDay.parse("");
      try { 
        monthDay0.isBefore(yearMonth0);
        fail("Expecting exception: ClassCastException");
      } catch(ClassCastException e) {
         verifyException("org.joda.time.base.AbstractPartial", e);
      }
  }

  @Test(timeout = 4000)
  public void testCompareTo_yearMonthWithLocalTime_throwsClassCastException()  {
      YearMonth yearMonth0 = new YearMonth();
      LocalTime localTime0 = LocalTime.now();
      try { 
        yearMonth0.compareTo((ReadablePartial) localTime0);
        fail("Expecting exception: ClassCastException");
      } catch(ClassCastException e) {
         verifyException("org.joda.time.base.AbstractPartial", e);
      }
  }

  @Test(timeout = 4000)
  public void testIsBefore_sameYearMonth_returnsFalse()  {
      YearMonth yearMonth0 = new YearMonth(4, 4);
      boolean boolean0 = yearMonth0.isBefore(yearMonth0);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void testHashCode_localDate()  {
      GregorianChronology gregorianChronology0 = GregorianChronology.getInstanceUTC();
      LocalDate localDate0 = new LocalDate((long) 1, (Chronology) gregorianChronology0);
      localDate0.hashCode();
  }

  @Test(timeout = 4000)
  public void testEquals_differentYearMonths_returnsFalse()  {
      YearMonth yearMonth0 = new YearMonth();
      YearMonth yearMonth1 = yearMonth0.plusYears(1);
      boolean boolean0 = yearMonth1.equals(yearMonth0);
      assertFalse(boolean0);
      assertEquals(2, yearMonth1.getMonthOfYear());
      assertFalse(yearMonth0.equals((Object)yearMonth1));
  }

  @Test(timeout = 4000)
  public void testEquals_monthDayAndLocalDateTime_returnsFalse()  {
      MonthDay monthDay0 = MonthDay.now();
      LocalDateTime localDateTime0 = new LocalDateTime((long) 1);
      boolean boolean0 = monthDay0.equals(localDateTime0);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void testEquals_yearMonthAndLocale_returnsFalse()  {
      YearMonth yearMonth0 = YearMonth.now();
      Locale locale0 = Locale.GERMANY;
      boolean boolean0 = yearMonth0.equals(locale0);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void testEquals_sameYearMonth_returnsTrue()  {
      YearMonth yearMonth0 = new YearMonth();
      boolean boolean0 = yearMonth0.equals(yearMonth0);
      assertTrue(boolean0);
  }

  @Test(timeout = 4000)
  public void testToDate_returnsExpected()  {
      GregorianChronology gregorianChronology0 = GregorianChronology.getInstanceUTC();
      LocalDate localDate0 = new LocalDate((long) 1, (Chronology) gregorianChronology0);
      Date date0 = localDate0.toDate();
      assertEquals("Thu Jan 01 00:00:00 GMT 1970", date0.toString());
  }

  @Test(timeout = 4000)
  public void testIsBefore_minusOneDay_returnsFalse()  {
      MonthDay monthDay0 = new MonthDay();
      MonthDay monthDay1 = monthDay0.minusDays(1);
      boolean boolean0 = monthDay0.isBefore(monthDay1);
      assertFalse(boolean0);
      assertEquals(2, monthDay1.getMonthOfYear());
  }

  @Test(timeout = 4000)
  public void testWithFieldAdded_unsupportedDurationField_throwsIllegalArgumentException()  {
      YearMonth yearMonth0 = new YearMonth();
      DurationFieldType durationFieldType0 = DurationFieldType.halfdays();
      try { 
        yearMonth0.withFieldAdded(durationFieldType0, 0);
        fail("Expecting exception: IllegalArgumentException");
      } catch(IllegalArgumentException e) {
         verifyException("org.joda.time.base.AbstractPartial", e);
      }
  }

  @Test(timeout = 4000)
  public void testIsSupported_nullFieldType_returnsFalse()  {
      YearMonth yearMonth0 = YearMonth.now();
      boolean boolean0 = yearMonth0.isSupported((DateTimeFieldType) null);
      assertFalse(boolean0);
  }

  @Test(timeout = 4000)
  public void testGetValues_localDateTime_returnsExpected()  {
      LocalDateTime localDateTime0 = LocalDateTime.now();
      int[] intArray0 = localDateTime0.getValues();
      assertArrayEquals(new int[] {2014, 2, 14, 73281320}, intArray0);
  }

  @Test(timeout = 4000)
  public void testGetFieldTypes_localDate_returnsThree()  {
      LocalDateTime localDateTime0 = new LocalDateTime();
      LocalDate localDate0 = new LocalDate(localDateTime0);
      DateTimeFieldType[] dateTimeFieldTypeArray0 = localDate0.getFieldTypes();
      assertEquals(3, dateTimeFieldTypeArray0.length);
  }

  @Test(timeout = 4000)
  public void testGet_unsupportedFieldType_throwsIllegalArgumentException()  {
      MonthDay monthDay0 = MonthDay.now();
      DateTimeFieldType dateTimeFieldType0 = DateTimeFieldType.clockhourOfDay();
      try { 
        monthDay0.get(dateTimeFieldType0);
        fail("Expecting exception: IllegalArgumentException");
      } catch(IllegalArgumentException e) {
         verifyException("org.joda.time.base.AbstractPartial", e);
      }
  }

  @Test(timeout = 4000)
  public void testGetFields_yearMonth_returnsTwo()  {
      YearMonth yearMonth0 = YearMonth.now();
      DateTimeField[] dateTimeFieldArray0 = yearMonth0.getFields();
      assertEquals(2, dateTimeFieldArray0.length);
  }

  @Test(timeout = 4000)
  public void testToDateTime_withBaseInstant_setsZone()  {
      GJChronology gJChronology0 = GJChronology.getInstanceUTC();
      LocalDate localDate0 = new LocalDate((long) 0, (Chronology) gJChronology0);
      DateTimeZone dateTimeZone0 = gJChronology0.getZone();
      DateTime dateTime0 = new DateTime(0L, dateTimeZone0);
      DateTime dateTime1 = localDate0.toDateTime(dateTime0);
      assertNotSame(dateTime0, dateTime1);
  }
}