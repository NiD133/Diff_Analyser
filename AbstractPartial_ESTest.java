package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MonthDay;
import org.joda.time.Partial;
import org.joda.time.ReadablePartial;
import org.joda.time.YearMonth;
import org.junit.Test;

/**
 * Focused, readable tests for AbstractPartial behavior via concrete implementations
 * such as YearMonth, LocalDate, LocalTime, LocalDateTime and MonthDay.
 *
 * All tests use fixed, explicit values and avoid current-time dependencies.
 */
public class AbstractPartial_UnderstandableTest {

    // ----------------------
    // Comparison semantics
    // ----------------------

    @Test
    public void compareTo_requiresMatchingFieldTypes() {
        YearMonth ym = new YearMonth(2020, 6);
        LocalTime time = new LocalTime(12, 0, 0);

        try {
            ym.compareTo(time);
            fail("Expected ClassCastException for mismatched field types");
        } catch (ClassCastException expected) {
            // ok
        }
    }

    @Test
    public void compareTo_null_throwsNPE() {
        YearMonth ym = new YearMonth(2020, 6);

        try {
            ym.compareTo((ReadablePartial) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    @Test
    public void isAfter_isBefore_isEqual_onMatchingTypes() {
        YearMonth a = new YearMonth(2020, 6);
        YearMonth b = new YearMonth(2020, 7);
        YearMonth c = new YearMonth(2020, 6);

        assertTrue(b.isAfter(a));
        assertTrue(a.isBefore(b));
        assertTrue(a.isEqual(c));

        assertFalse(a.isAfter(b));
        assertFalse(b.isBefore(a));
        assertFalse(a.isEqual(b));
    }

    @Test
    public void isAfter_isBefore_isEqual_null_throwsIAE() {
        YearMonth ym = new YearMonth(2020, 6);

        try {
            ym.isAfter(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}

        try {
            ym.isBefore(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}

        try {
            ym.isEqual(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {}
    }

    @Test
    public void isAfter_isBefore_isEqual_mismatchedTypes_throwCCE() {
        YearMonth ym = new YearMonth(2020, 6);
        LocalTime lt = new LocalTime(10, 0);

        try {
            ym.isAfter(lt);
            fail("Expected ClassCastException");
        } catch (ClassCastException expected) {}

        try {
            ym.isBefore(lt);
            fail("Expected ClassCastException");
        } catch (ClassCastException expected) {}

        try {
            ym.isEqual(lt);
            fail("Expected ClassCastException");
        } catch (ClassCastException expected) {}
    }

    // ----------------------
    // Equality and hashCode
    // ----------------------

    @Test
    public void equals_and_hashCode_consistent() {
        YearMonth a = new YearMonth(2020, 6);
        YearMonth b = new YearMonth(2020, 6);
        YearMonth c = new YearMonth(2021, 6);

        assertTrue(a.equals(b));
        assertEquals(a.hashCode(), b.hashCode());

        assertFalse(a.equals(c));
        assertNotEquals(a.hashCode(), c.hashCode());

        assertFalse(a.equals("not a partial"));
        assertTrue(a.equals(a)); // reflexive
    }

    // ----------------------
    // Field access and support
    // ----------------------

    @Test
    public void getFieldType_and_getField_validIndices() {
        YearMonth ym = new YearMonth(2020, 12);

        assertEquals(DateTimeFieldType.year(), ym.getFieldType(0));
        assertEquals(DateTimeFieldType.monthOfYear(), ym.getFieldType(1));

        DateTimeField yearField = ym.getField(0);
        DateTimeField monthField = ym.getField(1);
        assertNotNull(yearField);
        assertNotNull(monthField);
    }

    @Test
    public void getFieldType_and_getField_outOfBounds_throw() {
        YearMonth ym = new YearMonth(2020, 12);

        try {
            ym.getFieldType(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {}

        try {
            ym.getFieldType(2);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {}

        try {
            ym.getField(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {}

        try {
            ym.getField(99);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {}
    }

    @Test
    public void indexOf_isSupported_forFieldTypes() {
        YearMonth ym = new YearMonth(2020, 12);

        assertEquals(0, ym.indexOf(DateTimeFieldType.year()));
        assertEquals(1, ym.indexOf(DateTimeFieldType.monthOfYear()));

        assertEquals(-1, ym.indexOf(DateTimeFieldType.era()));  // unsupported
        assertFalse(ym.isSupported((DateTimeFieldType) null));
        assertEquals(-1, ym.indexOf((DateTimeFieldType) null));
    }

    @Test
    public void get_supportedFieldValue() {
        MonthDay md = new MonthDay(5, 10);
        assertEquals(5, md.get(DateTimeFieldType.monthOfYear()));
        assertEquals(10, md.get(DateTimeFieldType.dayOfMonth()));
    }

    @Test
    public void get_unsupportedField_throwsIAE() {
        MonthDay md = new MonthDay(5, 10);

        try {
            md.get(DateTimeFieldType.clockhourOfDay());
            fail("Expected IllegalArgumentException for unsupported field");
        } catch (IllegalArgumentException expected) {}
    }

    @Test
    public void getFields_and_getValues_orderAndLength() {
        YearMonth ym = new YearMonth(2020, 12);
        DateTimeField[] fields = ym.getFields();
        assertEquals(2, fields.length);
        assertEquals(DateTimeFieldType.year(), ym.getFieldType(0));
        assertEquals(DateTimeFieldType.monthOfYear(), ym.getFieldType(1));

        LocalDateTime ldt = new LocalDateTime(2001, 2, 3, 4, 5, 6, 7);
        assertArrayEquals(new int[] {2001, 2, 3, 4, 5, 6, 7}, ldt.getValues());
    }

    // ----------------------
    // Field addition support
    // ----------------------

    @Test
    public void withFieldAdded_unsupportedDuration_throwsIAE() {
        YearMonth ym = new YearMonth(2020, 6);
        try {
            ym.withFieldAdded(DurationFieldType.halfdays(), 1);
            fail("Expected IllegalArgumentException for unsupported duration");
        } catch (IllegalArgumentException expected) {}
    }

    // ----------------------
    // toDateTime combination
    // ----------------------

    @Test
    public void toDateTime_onYearMonth_replacesYearAndMonth_only() {
        DateTime base = new DateTime(2001, 3, 15, 10, 20, 30, 0, DateTimeZone.UTC);
        YearMonth ym = new YearMonth(2020, 12);

        DateTime combined = ym.toDateTime(base);

        assertEquals(new DateTime(2020, 12, 15, 10, 20, 30, 0, DateTimeZone.UTC), combined);
    }

    @Test
    public void toDateTime_onMonthDay_replacesMonthAndDay_only() {
        DateTime base = new DateTime(2001, 6, 3, 1, 2, 3, 0, DateTimeZone.UTC);
        MonthDay md = new MonthDay(12, 25);

        DateTime combined = md.toDateTime(base);

        assertEquals(new DateTime(2001, 12, 25, 1, 2, 3, 0, DateTimeZone.UTC), combined);
    }

    @Test
    public void toDateTime_onLocalDate_replacesDate_only() {
        DateTime base = new DateTime(2001, 6, 3, 1, 2, 3, 4, DateTimeZone.forID("Europe/Paris"));
        LocalDate date = new LocalDate(2021, 4, 5);

        DateTime combined = date.toDateTime(base);

        assertEquals(new DateTime(2021, 4, 5, 1, 2, 3, 4, base.getZone()), combined);
    }

    // ----------------------
    // Formatting
    // ----------------------

    @Test
    public void toString_withNullFormatter_usesDefaultISOFormat() {
        YearMonth ym = new YearMonth(2020, 12);
        assertEquals("2020-12", ym.toString(null));
    }

    // ----------------------
    // Misc sanity checks
    // ----------------------

    @Test
    public void equals_returnsFalseForDifferentPartialImplementation() {
        MonthDay md = new MonthDay(6, 7);
        LocalDateTime ldt = new LocalDateTime(2000, 6, 7, 0, 0, 0, 0);
        assertFalse(md.equals(ldt));
    }

    @Test
    public void partialsCanBeWrappedIntoPartialAdapterForCompareTo() {
        // Partial is itself a ReadablePartial implementation; use it to compare identical content.
        MonthDay md = new MonthDay(8, 9);
        Partial p = new Partial(md);
        assertEquals(0, p.compareTo(md));
    }

    @Test
    public void localDate_toDate_matches_toDateTimeAtStartOfDay() {
        LocalDate date = new LocalDate(2022, 1, 2);
        // Compare absolute milliseconds to avoid locale/timezone dependent string assertions.
        assertEquals(date.toDateTimeAtStartOfDay().getMillis(), date.toDate().getTime());
    }
}