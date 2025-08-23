package org.joda.time.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class AbstractPartial_ESTest extends AbstractPartial_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testYearMonthPlusMonths() {
        YearMonth initialYearMonth = new YearMonth();
        YearMonth updatedYearMonth = initialYearMonth.plusMonths(4989);
        assertFalse(updatedYearMonth.isEqual(initialYearMonth));
        assertEquals(11, updatedYearMonth.getMonthOfYear());
    }

    @Test(timeout = 4000)
    public void testLocalTimeFromMillisOfDay() {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        LocalTime localTime = LocalTime.fromMillisOfDay(Long.MAX_VALUE, islamicChronology);
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime updatedDateTime = currentDateTime.withFields(localTime);
        assertFalse(updatedDateTime.isAfter(currentDateTime));
    }

    @Test(timeout = 4000)
    public void testLocalTimeEqualsLocalDateTime() {
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(-326);
        IslamicChronology islamicChronology = IslamicChronology.getInstance(dateTimeZone, IslamicChronology.LEAP_YEAR_INDIAN);
        LocalDateTime localDateTime = LocalDateTime.now(islamicChronology);
        LocalTime localTime = new LocalTime(localDateTime, dateTimeZone);
        assertFalse(localTime.equals(localDateTime));
    }

    @Test(timeout = 4000)
    public void testLocalDateTimeEqualsLocalDate() {
        LocalDateTime localDateTime = new LocalDateTime();
        LocalDate localDate = new LocalDate(localDateTime);
        assertFalse(localDateTime.equals(localDate));
    }

    @Test(timeout = 4000)
    public void testLocalDateToStringWithMockFormatter() {
        Instant instant = Instant.now();
        DateTimeZone dateTimeZone = instant.getZone();
        LocalDate localDate = new LocalDate(dateTimeZone);

        DateTimePrinter dateTimePrinter = mock(DateTimePrinter.class, new ViolatedAssumptionAnswer());
        doReturn(0, 0).when(dateTimePrinter).estimatePrintedLength();
        DateTimeParser dateTimeParser = mock(DateTimeParser.class, new ViolatedAssumptionAnswer());
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatter(dateTimePrinter, dateTimeParser);

        String formattedDate = localDate.toString(dateTimeFormatter);
        assertEquals("", formattedDate);
    }

    @Test(timeout = 4000)
    public void testYearMonthToDateTime() {
        YearMonth yearMonth = YearMonth.now();
        Instant instant = Instant.now();
        DateTime dateTime = yearMonth.toDateTime(instant);
        assertEquals(1392409281320L, dateTime.getMillis());
    }

    @Test(timeout = 4000)
    public void testMonthDayToDateTime() {
        MonthDay monthDay = new MonthDay();
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();
        DateTime dateTime = new DateTime(-1448L, copticChronology);
        DateTime resultDateTime = monthDay.toDateTime(dateTime);
        assertEquals(-5875201448L, resultDateTime.getMillis());
    }

    @Test(timeout = 4000)
    public void testLocalDateIsSupported() {
        LocalDate localDate = new LocalDate();
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfEra();
        assertTrue(localDate.isSupported(fieldType));
    }

    @Test(timeout = 4000)
    public void testYearMonthIndexOfFieldType() {
        YearMonth yearMonth = new YearMonth();
        DateTimeFieldType fieldType = DateTimeFieldType.monthOfYear();
        int index = yearMonth.indexOf(fieldType);
        assertEquals(1, index);
    }

    @Test(timeout = 4000)
    public void testPartialGetFields() {
        Partial partial = new Partial();
        DateTimeField[] fields = partial.getFields();
        assertEquals(0, fields.length);
    }

    @Test(timeout = 4000)
    public void testMonthDayGetFieldType() {
        MonthDay monthDay = MonthDay.now();
        DateTimeFieldType fieldType = monthDay.getFieldType(1);
        assertEquals("dayOfMonth", fieldType.toString());
    }

    @Test(timeout = 4000)
    public void testYearMonthGetField() {
        YearMonth yearMonth = YearMonth.now();
        DateTimeField field = yearMonth.getField(0);
        assertNotNull(field);
    }

    @Test(timeout = 4000)
    public void testMonthDayGetField() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
        MonthDay monthDay = new MonthDay(1516L, gregorianChronology);
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value = monthDay.get(fieldType);
        assertEquals(1, value);
    }

    @Test(timeout = 4000)
    public void testLocalDateToStringWithNullFormatter() {
        GJChronology gjChronology = GJChronology.getInstanceUTC();
        LocalDate localDate = new LocalDate(0L, gjChronology);
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);

        try {
            localDate.toString(formatter);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.joda.time.format.DateTimeFormatter", e);
        }
    }

    @Test(timeout = 4000)
    public void testLocalDateToStringWithNegativeArraySize() {
        DateTimePrinter dateTimePrinter = mock(DateTimePrinter.class, new ViolatedAssumptionAnswer());
        doReturn(0, -689).when(dateTimePrinter).estimatePrintedLength();
        DateTimeParser dateTimeParser = mock(DateTimeParser.class, new ViolatedAssumptionAnswer());
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatter(dateTimePrinter, dateTimeParser);

        DateTimeZone dateTimeZone = DateTimeZone.getDefault();
        LocalDate localDate = new LocalDate(-1279L, dateTimeZone);

        try {
            localDate.toString(dateTimeFormatter);
            fail("Expecting exception: NegativeArraySizeException");
        } catch (NegativeArraySizeException e) {
            verifyException("java.lang.AbstractStringBuilder", e);
        }
    }

    @Test(timeout = 4000)
    public void testYearMonthIsEqualWithDifferentTypes() {
        YearMonth yearMonth = YearMonth.now();
        LocalTime localTime = LocalTime.fromMillisOfDay(1L);

        try {
            yearMonth.isEqual(localTime);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.joda.time.base.AbstractPartial", e);
        }
    }

    @Test(timeout = 4000)
    public void testLocalDateTimeIsAfterWithDifferentTypes() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();
        YearMonth yearMonth = YearMonth.now();
        LocalDateTime localDateTime = new LocalDateTime(gregorianChronology);

        try {
            localDateTime.isAfter(yearMonth);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.joda.time.base.AbstractPartial", e);
        }
    }

    @Test(timeout = 4000)
    public void testLocalDateGetFieldTypeWithInvalidIndex() {
        LocalDate localDate = new LocalDate();

        try {
            localDate.getFieldType(-3285);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.joda.time.LocalDate", e);
        }
    }

    @Test(timeout = 4000)
    public void testYearMonthGetFieldTypeWithInvalidIndex() {
        YearMonth yearMonth = YearMonth.now();

        try {
            yearMonth.getFieldType(1040);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.joda.time.YearMonth", e);
        }
    }

    @Test(timeout = 4000)
    public void testYearMonthGetFieldWithInvalidIndex() {
        YearMonth yearMonth = YearMonth.now();

        try {
            yearMonth.getField(-817);
            fail("Expecting exception: IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            verifyException("org.joda.time.YearMonth", e);
        }
    }

    @Test(timeout = 4000)
    public void testYearMonthCompareToNull() {
        YearMonth yearMonth = YearMonth.now();

        try {
            yearMonth.compareTo(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.base.AbstractPartial", e);
        }
    }

    @Test(timeout = 4000)
    public void testYearMonthCompareToAnotherYearMonth() {
        YearMonth yearMonth = YearMonth.now();
        YearMonth updatedYearMonth = yearMonth.minusYears(-2306);
        int comparisonResult = updatedYearMonth.compareTo(yearMonth);
        assertEquals(4320, updatedYearMonth.getYear());
        assertEquals(1, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testPartialCompareToMonthDay() {
        MockGregorianCalendar mockGregorianCalendar = new MockGregorianCalendar();
        MonthDay monthDay = MonthDay.fromCalendarFields(mockGregorianCalendar);
        Partial partial = new Partial(monthDay);
        int comparisonResult = partial.compareTo(monthDay);
        assertEquals(0, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testPartialCompareToDifferentTypes() {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        LocalTime localTime = LocalTime.fromMillisOfDay(Long.MAX_VALUE, islamicChronology);
        LocalDateTime localDateTime = LocalDateTime.now();
        Partial partial = new Partial(localDateTime);

        try {
            partial.compareTo(localTime);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.joda.time.base.AbstractPartial", e);
        }
    }

    @Test(timeout = 4000)
    public void testYearMonthCompareToAnotherYearMonthWithDifferentYears() {
        YearMonth yearMonth = new YearMonth(4, 4);
        YearMonth updatedYearMonth = yearMonth.minusYears(4);
        int comparisonResult = updatedYearMonth.compareTo(yearMonth);
        assertEquals(-1, comparisonResult);
        assertEquals(0, updatedYearMonth.getYear());
    }

    @Test(timeout = 4000)
    public void testYearMonthCompareToSelf() {
        YearMonth yearMonth = new YearMonth(4, 4);
        int comparisonResult = yearMonth.compareTo(yearMonth);
        assertEquals(0, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testYearMonthMinusYears() {
        Years years = Years.TWO;
        YearMonth yearMonth = new YearMonth(4, 4);
        YearMonth updatedYearMonth = yearMonth.minus(years);
        assertEquals(4, updatedYearMonth.getMonthOfYear());
    }

    @Test(timeout = 4000)
    public void testMonthDayMinusWeeks() {
        MonthDay monthDay = new MonthDay();
        Weeks weeks = Weeks.TWO;
        MonthDay updatedMonthDay = monthDay.minus(weeks);
        assertTrue(updatedMonthDay.equals(monthDay));
    }

    @Test(timeout = 4000)
    public void testLocalDateIndexOfFieldType() {
        LocalDate localDate = new LocalDate();
        DateTimeFieldType fieldType = DateTimeFieldType.year();
        int index = localDate.indexOf(fieldType);
        assertEquals(0, index);
    }

    @Test(timeout = 4000)
    public void testYearMonthIndexOfUnsupportedFieldType() {
        YearMonth yearMonth = new YearMonth(4, 4);
        DateTimeFieldType fieldType = DateTimeFieldType.era();
        int index = yearMonth.indexOf(fieldType);
        assertEquals(-1, index);
    }

    @Test(timeout = 4000)
    public void testMonthDayToString() {
        MonthDay monthDay = new MonthDay();
        String monthDayString = monthDay.toString();
        assertEquals("--02-14", monthDayString);
    }

    @Test(timeout = 4000)
    public void testYearMonthToStringWithNullFormatter() {
        YearMonth yearMonth = YearMonth.now();
        String yearMonthString = yearMonth.toString(null);
        assertNotNull(yearMonthString);
    }

    @Test(timeout = 4000)
    public void testYearMonthIsEqualWithDifferentYears() {
        YearMonth yearMonth = new YearMonth(4, 4);
        YearMonth updatedYearMonth = yearMonth.minusYears(4);
        assertFalse(updatedYearMonth.isEqual(yearMonth));
        assertEquals(4, updatedYearMonth.getMonthOfYear());
    }

    @Test(timeout = 4000)
    public void testLocalDateTimeIsEqualWithNull() {
        LocalDateTime localDateTime = LocalDateTime.now();

        try {
            localDateTime.isEqual(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.base.AbstractPartial", e);
        }
    }

    @Test(timeout = 4000)
    public void testLocalDateTimeIsEqualWithSelf() {
        LocalDateTime localDateTime = LocalDateTime.now();
        assertTrue(localDateTime.isEqual(localDateTime));
    }

    @Test(timeout = 4000)
    public void testMonthDayIsBeforeWithNull() {
        MonthDay monthDay = new MonthDay();

        try {
            monthDay.isBefore(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.base.AbstractPartial", e);
        }
    }

    @Test(timeout = 4000)
    public void testLocalDateTimeIsAfterWithSelf() {
        LocalDateTime localDateTime = new LocalDateTime();
        LocalDateTime updatedDateTime = localDateTime.minusWeeks(-4607);
        assertTrue(updatedDateTime.isAfter(localDateTime));
    }

    @Test(timeout = 4000)
    public void testYearMonthIsAfterWithNull() {
        YearMonth yearMonth = YearMonth.now();

        try {
            yearMonth.isAfter(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.base.AbstractPartial", e);
        }
    }

    @Test(timeout = 4000)
    public void testLocalDateTimeIsAfterWithSelf() {
        LocalDateTime localDateTime = LocalDateTime.now();
        assertFalse(localDateTime.isAfter(localDateTime));
    }

    @Test(timeout = 4000)
    public void testMonthDayIsBeforeAnotherMonthDay() {
        MonthDay monthDay = new MonthDay();
        MonthDay updatedMonthDay = monthDay.plusDays(1);
        assertTrue(monthDay.isBefore(updatedMonthDay));
        assertEquals(2, updatedMonthDay.getMonthOfYear());
    }

    @Test(timeout = 4000)
    public void testMonthDayIsBeforeWithDifferentTypes() {
        YearMonth yearMonth = new YearMonth();
        MonthDay monthDay = MonthDay.parse("");

        try {
            monthDay.isBefore(yearMonth);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.joda.time.base.AbstractPartial", e);
        }
    }

    @Test(timeout = 4000)
    public void testYearMonthCompareToDifferentTypes() {
        YearMonth yearMonth = new YearMonth();
        LocalTime localTime = LocalTime.now();

        try {
            yearMonth.compareTo(localTime);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            verifyException("org.joda.time.base.AbstractPartial", e);
        }
    }

    @Test(timeout = 4000)
    public void testYearMonthIsBeforeWithSelf() {
        YearMonth yearMonth = new YearMonth(4, 4);
        assertFalse(yearMonth.isBefore(yearMonth));
    }

    @Test(timeout = 4000)
    public void testLocalDateHashCode() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
        LocalDate localDate = new LocalDate(1L, gregorianChronology);
        localDate.hashCode();
    }

    @Test(timeout = 4000)
    public void testYearMonthEqualsWithDifferentYears() {
        YearMonth yearMonth = new YearMonth();
        YearMonth updatedYearMonth = yearMonth.plusYears(1);
        assertFalse(updatedYearMonth.equals(yearMonth));
        assertEquals(2, updatedYearMonth.getMonthOfYear());
        assertFalse(yearMonth.equals(updatedYearMonth));
    }

    @Test(timeout = 4000)
    public void testMonthDayEqualsWithDifferentTypes() {
        MonthDay monthDay = MonthDay.now();
        LocalDateTime localDateTime = new LocalDateTime(1L);
        assertFalse(monthDay.equals(localDateTime));
    }

    @Test(timeout = 4000)
    public void testYearMonthEqualsWithLocale() {
        YearMonth yearMonth = YearMonth.now();
        Locale locale = Locale.GERMANY;
        assertFalse(yearMonth.equals(locale));
    }

    @Test(timeout = 4000)
    public void testYearMonthEqualsWithSelf() {
        YearMonth yearMonth = new YearMonth();
        assertTrue(yearMonth.equals(yearMonth));
    }

    @Test(timeout = 4000)
    public void testLocalDateToDate() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
        LocalDate localDate = new LocalDate(1L, gregorianChronology);
        Date date = localDate.toDate();
        assertEquals("Thu Jan 01 00:00:00 GMT 1970", date.toString());
    }

    @Test(timeout = 4000)
    public void testMonthDayIsBeforeWithPreviousDay() {
        MonthDay monthDay = new MonthDay();
        MonthDay updatedMonthDay = monthDay.minusDays(1);
        assertFalse(monthDay.isBefore(updatedMonthDay));
        assertEquals(2, updatedMonthDay.getMonthOfYear());
    }

    @Test(timeout = 4000)
    public void testYearMonthWithFieldAddedUnsupported() {
        YearMonth yearMonth = new YearMonth();
        DurationFieldType durationFieldType = DurationFieldType.halfdays();

        try {
            yearMonth.withFieldAdded(durationFieldType, 0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.base.AbstractPartial", e);
        }
    }

    @Test(timeout = 4000)
    public void testYearMonthIsSupportedWithNull() {
        YearMonth yearMonth = YearMonth.now();
        assertFalse(yearMonth.isSupported(null));
    }

    @Test(timeout = 4000)
    public void testLocalDateTimeGetValues() {
        LocalDateTime localDateTime = LocalDateTime.now();
        int[] values = localDateTime.getValues();
        assertArrayEquals(new int[] {2014, 2, 14, 73281320}, values);
    }

    @Test(timeout = 4000)
    public void testLocalDateGetFieldTypes() {
        LocalDateTime localDateTime = new LocalDateTime();
        LocalDate localDate = new LocalDate(localDateTime);
        DateTimeFieldType[] fieldTypes = localDate.getFieldTypes();
        assertEquals(3, fieldTypes.length);
    }

    @Test(timeout = 4000)
    public void testMonthDayGetUnsupportedField() {
        MonthDay monthDay = MonthDay.now();
        DateTimeFieldType fieldType = DateTimeFieldType.clockhourOfDay();

        try {
            monthDay.get(fieldType);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.base.AbstractPartial", e);
        }
    }

    @Test(timeout = 4000)
    public void testYearMonthGetFields() {
        YearMonth yearMonth = YearMonth.now();
        DateTimeField[] fields = yearMonth.getFields();
        assertEquals(2, fields.length);
    }

    @Test(timeout = 4000)
    public void testLocalDateToDateTime() {
        GJChronology gjChronology = GJChronology.getInstanceUTC();
        LocalDate localDate = new LocalDate(0L, gjChronology);
        DateTimeZone dateTimeZone = gjChronology.getZone();
        DateTime dateTime = new DateTime(0L, dateTimeZone);
        DateTime resultDateTime = localDate.toDateTime(dateTime);
        assertNotSame(dateTime, resultDateTime);
    }
}