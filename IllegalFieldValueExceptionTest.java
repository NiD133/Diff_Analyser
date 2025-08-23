package org.joda.time;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.SkipDateTimeField;

/**
 * Tests IllegalFieldValueException by triggering it from other methods.
 *
 * The tests use a small assertion helper to avoid repetitive boilerplate and to
 * make the expected details of the exception explicit at the call site.
 */
public class TestIllegalFieldValueException extends TestCase {

    // Common, readable constants used across tests.
    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final ISOChronology ISO_UTC = ISOChronology.getInstanceUTC();
    private static final DateTimeZone LA = DateTimeZone.forID("America/Los_Angeles");
    private static final GJChronology GJ_UTC = GJChronology.getInstanceUTC();
    private static final JulianChronology JULIAN_UTC = JulianChronology.getInstanceUTC();

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestIllegalFieldValueException.class);
    }

    public TestIllegalFieldValueException(String name) {
        super(name);
    }

    // Convenience for boxing.
    private static Integer N(int value) {
        return Integer.valueOf(value);
    }

    /**
     * Centralized assertions for IllegalFieldValueException to keep individual test
     * cases focused on the scenario, not on the mechanics of asserting.
     */
    private void assertIllegal(IllegalFieldValueException e,
                               DateTimeFieldType expectedDateTimeFieldType,
                               DurationFieldType expectedDurationFieldType,
                               String expectedFieldName,
                               Integer expectedNumberValue,
                               String expectedStringValue,
                               Integer expectedLowerBound,
                               Integer expectedUpperBound) {
        if (expectedDateTimeFieldType == null) {
            assertNull(e.getDateTimeFieldType());
        } else {
            assertSame(expectedDateTimeFieldType, e.getDateTimeFieldType());
        }

        if (expectedDurationFieldType == null) {
            assertNull(e.getDurationFieldType());
        } else {
            assertSame(expectedDurationFieldType, e.getDurationFieldType());
        }

        assertEquals(expectedFieldName, e.getFieldName());
        assertEquals(expectedNumberValue, e.getIllegalNumberValue());
        assertEquals(expectedStringValue, e.getIllegalStringValue());

        // getIllegalValueAsString() is always non-null.
        String expectedAsString;
        if (expectedStringValue != null) {
            expectedAsString = expectedStringValue;
        } else if (expectedNumberValue != null) {
            expectedAsString = expectedNumberValue.toString();
        } else {
            expectedAsString = "null";
        }
        assertEquals(expectedAsString, e.getIllegalValueAsString());

        assertEquals(expectedLowerBound, e.getLowerBound());
        assertEquals(expectedUpperBound, e.getUpperBound());
    }

    public void testVerifyValueBounds() {
        // Value below lower bound for monthOfYear.
        try {
            FieldUtils.verifyValueBounds(ISOChronology.getInstance().monthOfYear(), -5, 1, 31);
            fail("Expected IllegalFieldValueException for monthOfYear = -5");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.monthOfYear(), null, "monthOfYear",
                    N(-5), null, N(1), N(31));
        }

        // Value above upper bound for hourOfDay.
        try {
            FieldUtils.verifyValueBounds(DateTimeFieldType.hourOfDay(), 27, 0, 23);
            fail("Expected IllegalFieldValueException for hourOfDay = 27");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.hourOfDay(), null, "hourOfDay",
                    N(27), null, N(0), N(23));
        }

        // Using a raw field name (no type) with out-of-range value.
        try {
            FieldUtils.verifyValueBounds("foo", 1, 2, 3);
            fail("Expected IllegalFieldValueException for custom field 'foo' = 1");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, null, null, "foo",
                    N(1), null, N(2), N(3));
        }
    }

    public void testSkipDateTimeField() {
        // Year 1970 is skipped, so setting it should be illegal.
        DateTimeField skippedYear = new SkipDateTimeField(ISO_UTC, ISO_UTC.year(), 1970);
        try {
            skippedYear.set(0, 1970);
            fail("Expected IllegalFieldValueException for skipped year 1970");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.year(), null, "year",
                    N(1970), null, null, null);
        }
    }

    public void testSetText() {
        // Null text for a numeric-only field (year).
        try {
            ISO_UTC.year().set(0, null, java.util.Locale.US);
            fail("Expected IllegalFieldValueException for year set to null text");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.year(), null, "year",
                    null, null, null, null);
        }

        // Non-parsable text for year.
        try {
            ISO_UTC.year().set(0, "nineteen seventy", java.util.Locale.US);
            fail("Expected IllegalFieldValueException for unparsable year text");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.year(), null, "year",
                    null, "nineteen seventy", null, null);
        }

        // Non-parsable text for era.
        try {
            ISO_UTC.era().set(0, "long ago", java.util.Locale.US);
            fail("Expected IllegalFieldValueException for unparsable era text");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.era(), null, "era",
                    null, "long ago", null, null);
        }

        // Non-parsable month-of-year text.
        try {
            ISO_UTC.monthOfYear().set(0, "spring", java.util.Locale.US);
            fail("Expected IllegalFieldValueException for unparsable monthOfYear text");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.monthOfYear(), null, "monthOfYear",
                    null, "spring", null, null);
        }

        // Non-parsable day-of-week text.
        try {
            ISO_UTC.dayOfWeek().set(0, "yesterday", java.util.Locale.US);
            fail("Expected IllegalFieldValueException for unparsable dayOfWeek text");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.dayOfWeek(), null, "dayOfWeek",
                    null, "yesterday", null, null);
        }

        // Non-parsable halfday-of-day text.
        try {
            ISO_UTC.halfdayOfDay().set(0, "morning", java.util.Locale.US);
            fail("Expected IllegalFieldValueException for unparsable halfdayOfDay text");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.halfdayOfDay(), null, "halfdayOfDay",
                    null, "morning", null, null);
        }
    }

    public void testZoneTransition() {
        // Spring DST gap in America/Los_Angeles on 2005-04-03: 02:00 does not exist.
        DateTime dt = new DateTime(2005, 4, 3, 1, 0, 0, 0, LA);
        try {
            dt.hourOfDay().setCopy(2);
            fail("Expected IllegalFieldValueException for nonexistent hour during DST transition");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.hourOfDay(), null, "hourOfDay",
                    N(2), null, null, null);
        }
    }

    public void testJulianYearZero() {
        // Year zero is not supported in the Julian chronology.
        DateTime dt = new DateTime(JULIAN_UTC);
        try {
            dt.year().setCopy(0);
            fail("Expected IllegalFieldValueException for year 0 in Julian chronology");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.year(), null, "year",
                    N(0), null, null, null);
        }
    }

    public void testGJCutover() {
        // GJ cutover: dates skipped in October 1582 (days 5-14).
        DateTime dt = new DateTime("1582-10-04", GJ_UTC);
        try {
            dt.dayOfMonth().setCopy(5);
            fail("Expected IllegalFieldValueException for missing day 5 at GJ cutover");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.dayOfMonth(), null, "dayOfMonth",
                    N(5), null, null, null);
        }

        dt = new DateTime("1582-10-15", GJ_UTC);
        try {
            dt.dayOfMonth().setCopy(14);
            fail("Expected IllegalFieldValueException for missing day 14 at GJ cutover");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.dayOfMonth(), null, "dayOfMonth",
                    N(14), null, null, null);
        }
    }

    @SuppressWarnings("deprecation")
    public void testReadablePartialValidate() {
        // Month below lower bound when constructing a partial.
        try {
            new YearMonthDay(1970, -5, 1);
            fail("Expected IllegalFieldValueException for monthOfYear = -5 in partial");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.monthOfYear(), null, "monthOfYear",
                    N(-5), null, N(1), null);
        }

        // Month above upper bound when constructing a partial.
        try {
            new YearMonthDay(1970, 500, 1);
            fail("Expected IllegalFieldValueException for monthOfYear = 500 in partial");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.monthOfYear(), null, "monthOfYear",
                    N(500), null, null, N(12));
        }

        // Day above upper bound for February 1970.
        try {
            new YearMonthDay(1970, 2, 30);
            fail("Expected IllegalFieldValueException for dayOfMonth = 30 in February 1970");
        } catch (IllegalFieldValueException e) {
            assertIllegal(e, DateTimeFieldType.dayOfMonth(), null, "dayOfMonth",
                    N(30), null, null, N(28));
        }
    }

    // Test extra constructors not currently called by anything
    public void testOtherConstructors() {
        IllegalFieldValueException e =
                new IllegalFieldValueException(DurationFieldType.days(), N(1), N(2), N(3));
        assertIllegal(e, null, DurationFieldType.days(), "days",
                N(1), null, N(2), N(3));

        e = new IllegalFieldValueException(DurationFieldType.months(), "five");
        assertIllegal(e, null, DurationFieldType.months(), "months",
                null, "five", null, null);

        e = new IllegalFieldValueException("months", "five");
        assertIllegal(e, null, null, "months",
                null, "five", null, null);
    }
}