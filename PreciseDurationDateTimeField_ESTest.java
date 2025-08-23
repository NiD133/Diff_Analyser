package org.joda.time.field;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class PreciseDurationDateTimeFieldTest extends PreciseDurationDateTimeField_ESTest_scaffolding {

    private static final long MILLIS_IN_A_DAY = 86400000L;

    @Test(timeout = 4000)
    public void testRemainderCalculation() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.clockhourOfHalfday();
        DateTimeZone timeZone = DateTimeZone.getDefault();
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance();
        ZonedChronology zonedChronology = ZonedChronology.getInstance(ethiopicChronology, timeZone);
        DurationField days = zonedChronology.days();
        DurationField seconds = zonedChronology.seconds();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, seconds, days);

        long remainder = preciseField.remainder(2946L);
        assertEquals(946L, remainder);
    }

    @Test(timeout = 4000)
    public void testRoundCeilingWithJulianChronology() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.UTC;
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        Days minDays = Days.MIN_VALUE;
        DurationFieldType durationType = minDays.getFieldType();
        PreciseDurationField preciseDurationField = new PreciseDurationField(durationType, 21859200000L);
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, days, preciseDurationField);

        long rounded = preciseField.roundCeiling(1902L);
        assertEquals(MILLIS_IN_A_DAY, rounded);
    }

    @Test(timeout = 4000)
    public void testRoundCeilingAtEpoch() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        MillisDurationField millisField = MillisDurationField.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.UTC;
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, millisField, days);

        long rounded = preciseField.roundCeiling(0L);
        assertEquals(0L, rounded);
    }

    @Test(timeout = 4000)
    public void testRoundFloorAtEpoch() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        MillisDurationField millisField = MillisDurationField.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.getDefault();
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, millisField, days);

        long rounded = preciseField.roundFloor(0L);
        assertEquals(0L, rounded);
    }

    @Test(timeout = 4000)
    public void testLocalDateWithDayOfYear() throws Throwable {
        LocalDate today = LocalDate.now();
        LocalDate endOfYear = today.withDayOfYear(365);
        assertNotSame(today, endOfYear);
    }

    @Test(timeout = 4000)
    public void testSetFieldValueToZero() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        MillisDurationField millisField = MillisDurationField.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.UTC;
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, millisField, days);

        long result = preciseField.set(2979L, 0);
        assertEquals(0L, result);
    }

    @Test(timeout = 4000)
    public void testSetFieldValue() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        MillisDurationField millisField = MillisDurationField.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.UTC;
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, millisField, days);

        long result = preciseField.set(31536000000L, 86399850);
        assertEquals(31622399850L, result);
    }

    @Test(timeout = 4000)
    public void testSetNegativeFieldValue() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        MillisDurationField millisField = MillisDurationField.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.UTC;
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, millisField, days);

        long result = preciseField.set(-3012L, 82);
        assertEquals(-86399918L, result);
    }

    @Test(timeout = 4000)
    public void testRoundFloorWithLenientChronology() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
        LenientChronology lenientChronology = LenientChronology.getInstance(gregorianChronology);
        DateTimeZone timeZone = DateTimeZone.forID((String) null);
        ZonedChronology zonedChronology = ZonedChronology.getInstance(lenientChronology, timeZone);
        DurationField minutes = zonedChronology.minutes();
        DurationField halfdays = zonedChronology.halfdays();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, minutes, halfdays);

        long rounded = preciseField.roundFloor(992280585600000L);
        assertEquals(992280585600000L, rounded);
        assertFalse(preciseField.isLenient());
    }

    @Test(timeout = 4000)
    public void testRoundFloorWithNegativeInstant() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        MillisDurationField millisField = MillisDurationField.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.UTC;
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, millisField, days);

        long rounded = preciseField.roundFloor(-5225L);
        assertEquals(-5225L, rounded);
    }

    @Test(timeout = 4000)
    public void testRoundCeilingWithNegativeInstant() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.UTC;
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        Days minDays = Days.MIN_VALUE;
        DurationFieldType durationType = minDays.getFieldType();
        PreciseDurationField preciseDurationField = new PreciseDurationField(durationType, 21859200000L);
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, days, preciseDurationField);

        long rounded = preciseField.roundCeiling(-1481L);
        assertEquals(0L, rounded);
    }

    @Test(timeout = 4000)
    public void testRemainderWithNegativeInstant() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.UTC;
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        Days minDays = Days.MIN_VALUE;
        DurationFieldType durationType = minDays.getFieldType();
        PreciseDurationField preciseDurationField = new PreciseDurationField(durationType, 21859200000L);
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, days, preciseDurationField);

        long remainder = preciseField.remainder(-5225L);
        assertEquals(86394775L, remainder);
    }

    @Test(timeout = 4000)
    public void testIsLenient() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
        LenientChronology lenientChronology = LenientChronology.getInstance(gregorianChronology);
        DateTimeZone timeZone = DateTimeZone.forID((String) null);
        ZonedChronology zonedChronology = ZonedChronology.getInstance(lenientChronology, timeZone);
        DurationField minutes = zonedChronology.minutes();
        DurationField halfdays = zonedChronology.halfdays();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, minutes, halfdays);

        assertFalse(preciseField.isLenient());
    }

    @Test(timeout = 4000)
    public void testGetUnitMillis() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        MillisDurationField millisField = MillisDurationField.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.UTC;
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, millisField, days);

        long unitMillis = preciseField.getUnitMillis();
        assertEquals(1L, unitMillis);
    }

    @Test(timeout = 4000)
    public void testGetMinimumValue() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfWeek();
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.forTimeZone((TimeZone) null);
        ZonedChronology zonedChronology = ZonedChronology.getInstance(islamicChronology, timeZone);
        DurationField halfdays = zonedChronology.halfdays();
        DurationField days = zonedChronology.days();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, halfdays, days);

        int minValue = preciseField.getMinimumValue();
        assertEquals(0, minValue);
    }

    @Test(timeout = 4000)
    public void testGetMaximumValueForSet() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        MillisDurationField millisField = MillisDurationField.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.UTC;
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, millisField, days);

        int maxValue = preciseField.getMaximumValueForSet(-3870L, -2146901673);
        assertEquals(86399999, maxValue);
    }

    @Test(timeout = 4000)
    public void testGetDurationField() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        MillisDurationField millisField = MillisDurationField.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.UTC;
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, millisField, days);

        DurationField durationField = preciseField.getDurationField();
        assertTrue(durationField.isPrecise());
    }

    @Test(timeout = 4000)
    public void testLocalTimeToString() throws Throwable {
        LocalTime midnight = LocalTime.MIDNIGHT;
        String timeString = midnight.toString();
        assertEquals("00:00:00.000", timeString);
    }

    @Test(timeout = 4000)
    public void testRemainderWithNegativeInstantZeroResult() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        MillisDurationField millisField = MillisDurationField.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.UTC;
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, millisField, days);

        long remainder = preciseField.remainder(-1142L);
        assertEquals(0L, remainder);
    }

    @Test(timeout = 4000)
    public void testRoundCeilingWithLargeInstant() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        MillisDurationField millisField = MillisDurationField.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.getDefault();
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, millisField, days);

        long rounded = preciseField.roundCeiling(100000000000000L);
        assertEquals(100000000000000L, rounded);
    }

    @Test(timeout = 4000)
    public void testRoundCeilingWithNegativeInstantNoChange() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.yearOfCentury();
        MillisDurationField millisField = MillisDurationField.INSTANCE;
        JulianChronology julianChronology = JulianChronology.getInstance();
        DateTimeZone timeZone = DateTimeZone.UTC;
        ZonedChronology zonedChronology = ZonedChronology.getInstance(julianChronology, timeZone);
        DurationField days = zonedChronology.days();
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, millisField, days);

        long rounded = preciseField.roundCeiling(-1142L);
        assertEquals(-1142L, rounded);
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionForUnsupportedDurationField() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.clockhourOfDay();
        DurationFieldType durationType = DurationFieldType.centuries();
        UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(durationType);

        try {
            new PreciseDateTimeField(fieldType, unsupportedField, unsupportedField);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // The unit milliseconds must be at least 1
            verifyException("org.joda.time.field.PreciseDurationDateTimeField", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionForImpreciseDurationField() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.halfdayOfDay();
        DurationFieldType durationType = DurationFieldType.years();
        DurationField durationField = durationType.getField((Chronology) null);

        try {
            new PreciseDateTimeField(fieldType, durationField, durationField);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Unit duration field must be precise
            verifyException("org.joda.time.field.PreciseDurationDateTimeField", e);
        }
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionForInvalidValue() throws Throwable {
        DateTimeFieldType fieldType = DateTimeFieldType.minuteOfDay();
        Weeks oneWeek = Weeks.ONE;
        DurationFieldType durationType = oneWeek.getFieldType();
        GJChronology gjChronology = GJChronology.getInstance();
        DurationField durationField = durationType.getField(gjChronology);
        ScaledDurationField scaledField = new ScaledDurationField(durationField, durationType, 1058);
        PreciseDateTimeField preciseField = new PreciseDateTimeField(fieldType, durationField, scaledField);

        try {
            preciseField.set(1058L, 1058);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Value 1058 for minuteOfDay must be in the range [0,1057]
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }
}