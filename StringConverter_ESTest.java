package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.MutableInterval;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, focused tests for StringConverter.
 * 
 * These tests aim to demonstrate typical, real-world uses of the converter
 * and the kinds of failures developers are likely to encounter.
 */
public class StringConverterTest {

    private final StringConverter converter = StringConverter.INSTANCE;
    private final Chronology ISO_UTC = ISOChronology.getInstance(DateTimeZone.UTC);

    // ---------------------------------------------------------------------
    // Supported type
    // ---------------------------------------------------------------------

    @Test
    public void supportedTypeIsString() {
        assertEquals(String.class, converter.getSupportedType());
    }

    // ---------------------------------------------------------------------
    // getDurationMillis(Object)
    // ---------------------------------------------------------------------

    @Test
    public void getDurationMillis_parsesIsoSeconds() {
        // PT2S = 2 seconds
        assertEquals(2000L, converter.getDurationMillis("PT2S"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDurationMillis_rejectsInvalidFormat() {
        converter.getDurationMillis("not-a-duration");
    }

    @Test(expected = NullPointerException.class)
    public void getDurationMillis_nullInputThrows() {
        converter.getDurationMillis(null);
    }

    // ---------------------------------------------------------------------
    // getInstantMillis(Object, Chronology)
    // ---------------------------------------------------------------------

    @Test
    public void getInstantMillis_parsesIsoInstantAtUTC() {
        // 1970-01-01T00:00:00Z => epoch millis 0
        long millis = converter.getInstantMillis("1970-01-01T00:00:00Z", ISO_UTC);
        assertEquals(0L, millis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getInstantMillis_invalidInstantThrows() {
        converter.getInstantMillis("not-a-date", ISO_UTC);
    }

    @Test(expected = NullPointerException.class)
    public void getInstantMillis_nullObjectThrows() {
        converter.getInstantMillis(null, ISO_UTC);
    }

    // ---------------------------------------------------------------------
    // setInto(ReadWritableInterval, Object, Chronology)
    // ---------------------------------------------------------------------

    @Test
    public void setInto_interval_parsesSlashSeparatedInstants() {
        // Expect a 1-day interval at UTC
        MutableInterval interval = new MutableInterval(0L, 0L, ISO_UTC);
        converter.setInto(
                interval,
                "1970-01-01T00:00:00Z/1970-01-02T00:00:00Z",
                ISO_UTC
        );

        assertEquals(0L, interval.getStartMillis());
        assertEquals(24 * 60 * 60 * 1000L, interval.getEndMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setInto_interval_rejectsMissingSlash() {
        MutableInterval interval = new MutableInterval(0L, 0L, ISO_UTC);
        converter.setInto(interval, "1970-01-01T00:00:00Z", ISO_UTC);
    }

    @Test(expected = NullPointerException.class)
    public void setInto_interval_nullWritableThrows() {
        converter.setInto(null, "1970-01-01T00:00:00Z/1970-01-02T00:00:00Z", ISO_UTC);
    }

    // ---------------------------------------------------------------------
    // setInto(ReadWritablePeriod, Object, Chronology)
    // ---------------------------------------------------------------------

    @Test
    public void setInto_period_parsesIsoSeconds() {
        MutablePeriod period = new MutablePeriod(PeriodType.standard());
        converter.setInto(period, "PT2S", ISO_UTC);
        assertEquals(2, period.getSeconds());
        assertEquals(0, period.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setInto_period_invalidFormatThrows() {
        MutablePeriod period = new MutablePeriod();
        converter.setInto(period, "not-a-period", ISO_UTC);
    }

    @Test(expected = NullPointerException.class)
    public void setInto_period_nullWritableThrows() {
        converter.setInto(null, "PT2S", ISO_UTC);
    }

    // ---------------------------------------------------------------------
    // getPartialValues(ReadablePartial, Object, Chronology, DateTimeFormatter)
    // ---------------------------------------------------------------------

    @Test
    public void getPartialValues_parsesLocalDateFields() {
        // Given a LocalDate field source, parse a date string into its fields
        ReadablePartial fieldSource = new LocalDate(ISO_UTC); // field types: year, month, day
        DateTimeFormatter parser = ISODateTimeFormat.localDateParser();

        int[] values = converter.getPartialValues(fieldSource, "2023-10-05", ISO_UTC, parser);

        assertArrayEquals(new int[] { 2023, 10, 5 }, values);
    }

    @Test(expected = ClassCastException.class)
    public void getPartialValues_nonStringObjectThrowsClassCast() {
        ReadablePartial fieldSource = new LocalDate(ISO_UTC);
        DateTimeFormatter parser = ISODateTimeFormat.localDateParser();

        // Passing a non-String object should fail fast
        converter.getPartialValues(fieldSource, new Object(), ISO_UTC, parser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPartialValues_invalidDateStringThrows() {
        ReadablePartial fieldSource = new LocalDate(ISO_UTC);
        DateTimeFormatter parser = ISODateTimeFormat.localDateParser();

        converter.getPartialValues(fieldSource, "invalid-date", ISO_UTC, parser);
    }
}