package org.joda.time.convert;

import org.joda.time.*;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * A clear, understandable test suite for the {@link StringConverter}.
 */
public class StringConverterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private StringConverter converter;
    private Chronology isoChronologyUTC;

    @Before
    public void setUp() {
        converter = StringConverter.INSTANCE;
        isoChronologyUTC = ISOChronology.getInstanceUTC();
    }

    //-----------------------------------------------------------------------
    // getSupportedType()
    //-----------------------------------------------------------------------

    @Test
    public void getSupportedType_shouldReturnStringClass() {
        assertEquals(String.class, converter.getSupportedType());
    }

    //-----------------------------------------------------------------------
    // getInstantMillis()
    //-----------------------------------------------------------------------

    @Test
    public void getInstantMillis_shouldParseFullIsoDateTime() {
        DateTime expected = new DateTime(2023, 10, 26, 10, 30, 0, 0, isoChronologyUTC);
        long actualMillis = converter.getInstantMillis("2023-10-26T10:30:00Z", isoChronologyUTC);
        assertEquals(expected.getMillis(), actualMillis);
    }

    @Test
    public void getInstantMillis_shouldParseYearZeroForIsoChronology() {
        // Year 0 is valid in ISO chronology but not in others like Coptic.
        long expectedMillis = new DateTime(0, 1, 1, 0, 0, 0, 0, isoChronologyUTC).getMillis();
        long actualMillis = converter.getInstantMillis("0000", isoChronologyUTC);
        assertEquals(expectedMillis, actualMillis);
    }

    @Test
    public void getInstantMillis_shouldThrowException_forInvalidYearInCopticChronology() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Value 0 for year is not supported");
        converter.getInstantMillis("0000", CopticChronology.getInstanceUTC());
    }

    @Test
    public void getInstantMillis_shouldThrowException_forInvalidFormat() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid format: \"not a date\"");
        converter.getInstantMillis("not a date", isoChronologyUTC);
    }

    @Test
    public void getInstantMillis_shouldThrowException_forNonStringInput() {
        thrown.expect(ClassCastException.class);
        converter.getInstantMillis(new Object(), isoChronologyUTC);
    }

    //-----------------------------------------------------------------------
    // getDurationMillis()
    //-----------------------------------------------------------------------

    @Test
    public void getDurationMillis_shouldParseValidIsoDurationString() {
        assertEquals(2000L, converter.getDurationMillis("PT2S"));
    }

    @Test
    public void getDurationMillis_shouldParseIsoDurationStringWithFraction() {
        // The parser correctly handles a period at the end of the seconds field.
        assertEquals(2000L, converter.getDurationMillis("Pt2.s"));
    }
    
    @Test
    public void getDurationMillis_shouldParseIsoDurationStringWithDecimalSeconds() {
        assertEquals(2500L, converter.getDurationMillis("PT2.5S"));
    }

    @Test
    public void getDurationMillis_shouldThrowException_forInvalidDurationFormat() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid format: \"P!T2S\"");
        converter.getDurationMillis("P!T2S");
    }

    @Test
    public void getDurationMillis_shouldThrowException_forIntervalFormat() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid format: \"2023-01-01/P1M\"");
        converter.getDurationMillis("2023-01-01/P1M");
    }

    @Test
    public void getDurationMillis_shouldThrowException_forEmptyString() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid format: \"\"");
        converter.getDurationMillis("");
    }

    @Test
    public void getDurationMillis_shouldThrowException_forNonStringInput() {
        thrown.expect(ClassCastException.class);
        converter.getDurationMillis(new Object());
    }

    //-----------------------------------------------------------------------
    // setInto(ReadWritablePeriod)
    //-----------------------------------------------------------------------

    @Test
    public void setIntoPeriod_shouldSetPeriodValuesFromString() {
        MutablePeriod period = new MutablePeriod();
        converter.setInto(period, "P1Y2M3DT4H5M6S", isoChronologyUTC);

        Period expected = new Period(1, 2, 0, 3, 4, 5, 6, 0);
        assertEquals(expected, period.toPeriod());
    }

    @Test
    public void setIntoPeriod_shouldThrowException_forInvalidFormat() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Invalid format: \"invalid-period\"");
        converter.setInto(new MutablePeriod(), "invalid-period", isoChronologyUTC);
    }

    @Test
    public void setIntoPeriod_shouldThrowException_forNonStringInput() {
        thrown.expect(ClassCastException.class);
        converter.setInto(new MutablePeriod(), new Object(), isoChronologyUTC);
    }

    //-----------------------------------------------------------------------
    // setInto(ReadWritableInterval)
    //-----------------------------------------------------------------------

    @Test
    public void setIntoInterval_shouldSetIntervalFromInstantToInstantString() {
        MutableInterval interval = new MutableInterval();
        converter.setInto(interval, "2023-01-01T00:00:00Z/2023-02-01T00:00:00Z", isoChronologyUTC);

        assertEquals(new DateTime("2023-01-01T00:00:00Z", isoChronologyUTC), interval.getStart());
        assertEquals(new DateTime("2023-02-01T00:00:00Z", isoChronologyUTC), interval.getEnd());
    }

    @Test
    public void setIntoInterval_shouldSetIntervalFromInstantToPeriodString() {
        MutableInterval interval = new MutableInterval();
        converter.setInto(interval, "2023-01-01T00:00:00Z/P1M", isoChronologyUTC);

        assertEquals(new DateTime("2023-01-01T00:00:00Z", isoChronologyUTC), interval.getStart());
        assertEquals(new DateTime("2023-02-01T00:00:00Z", isoChronologyUTC), interval.getEnd());
    }

    @Test
    public void setIntoInterval_shouldThrowException_forInvalidFormat() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Format requires a '/' separator: no-separator");
        converter.setInto(new MutableInterval(), "no-separator", isoChronologyUTC);
    }

    @Test
    public void setIntoInterval_shouldThrowException_forNonStringInput() {
        thrown.expect(ClassCastException.class);
        converter.setInto(new MutableInterval(), new Object(), isoChronologyUTC);
    }

    //-----------------------------------------------------------------------
    // getPartialValues()
    //-----------------------------------------------------------------------

    @Test
    public void getPartialValues_shouldParsePartialWithDefaultFormatter() {
        LocalDate partial = new LocalDate();
        int[] values = converter.getPartialValues(partial, "2023-10-27", isoChronologyUTC, null);

        assertArrayEquals(new int[]{2023, 10, 27}, values);
    }

    @Test
    public void getPartialValues_shouldParsePartialWithCustomFormatter() {
        DateTimeFormatter customFormatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        LocalDate partial = new LocalDate();
        int[] values = converter.getPartialValues(partial, "10/27/2023", isoChronologyUTC, customFormatter);

        assertArrayEquals(new int[]{2023, 10, 27}, values);
    }

    @Test
    public void getPartialValues_shouldThrowException_forInvalidFormatWithCustomFormatter() {
        thrown.expect(IllegalArgumentException.class);
        DateTimeFormatter customFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        converter.getPartialValues(new LocalDate(), "27/10/2023", isoChronologyUTC, customFormatter);
    }
    
    @Test
    public void getPartialValues_shouldThrowException_whenFormatterDoesNotSupportParsing() {
        // Create a formatter that can only print, not parse.
        DateTimeFormatter printOnlyFormatter = new DateTimeFormatter(ISODateTimeFormat.date().getPrinter(), null);
        
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Parsing not supported");
        converter.getPartialValues(new LocalDate(), "2023-10-27", isoChronologyUTC, printOnlyFormatter);
    }

    @Test
    public void getPartialValues_shouldThrowException_forNonStringInput() {
        thrown.expect(ClassCastException.class);
        converter.getPartialValues(new LocalDate(), new Object(), isoChronologyUTC, null);
    }
}