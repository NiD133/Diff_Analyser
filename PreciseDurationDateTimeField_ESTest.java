package org.joda.time.field;

import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for PreciseDurationDateTimeField behavior via its concrete subclass PreciseDateTimeField.
 *
 * The tests intentionally use the "secondOfMinute" field with UTC chronology to avoid DST
 * and calendar-related edge cases, keeping the numbers simple and expectations clear:
 * - unit = seconds (1000 ms)
 * - range = minutes (60,000 ms)
 */
public class PreciseDurationDateTimeFieldTest {

    private static final DateTimeZone UTC = DateTimeZone.UTC;

    private static class ZeroUnitDurationField extends BaseDurationField {
        ZeroUnitDurationField() {
            super(DurationFieldType.millis());
        }
        @Override public boolean isPrecise() { return true; }
        @Override public long getUnitMillis() { return 0L; } // triggers "unit millis must be at least 1"
        @Override public int getValue(long duration) { throw new UnsupportedOperationException(); }
        @Override public long getValueAsLong(long duration) { throw new UnsupportedOperationException(); }
        @Override public int getValue(long duration, long instant) { throw new UnsupportedOperationException(); }
        @Override public long getValueAsLong(long duration, long instant) { throw new UnsupportedOperationException(); }
        @Override public long getMillis(int value) { throw new UnsupportedOperationException(); }
        @Override public long getMillis(long value) { throw new UnsupportedOperationException(); }
        @Override public long getMillis(int value, long instant) { throw new UnsupportedOperationException(); }
        @Override public long getMillis(long value, long instant) { throw new UnsupportedOperationException(); }
        @Override public long add(long instant, int value) { throw new UnsupportedOperationException(); }
        @Override public long add(long instant, long value) { throw new UnsupportedOperationException(); }
        @Override public int getDifference(long minuendInstant, long subtrahendInstant) { throw new UnsupportedOperationException(); }
        @Override public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) { throw new UnsupportedOperationException(); }
    }

    private static PreciseDateTimeField secondOfMinuteFieldUTC(DurationField[] outUnitAndRange) {
        Chronology chrono = ISOChronology.getInstance(UTC);
        DurationField unit = chrono.seconds();   // 1,000 ms
        DurationField range = chrono.minutes();  // 60,000 ms
        if (outUnitAndRange != null) {
            outUnitAndRange[0] = unit;
            outUnitAndRange[1] = range;
        }
        return new PreciseDateTimeField(DateTimeFieldType.secondOfMinute(), unit, range);
    }

    @Test
    public void durationField_isTheUnitField_andIsPrecise() {
        DurationField[] fields = new DurationField[2];
        PreciseDateTimeField field = secondOfMinuteFieldUTC(fields);

        assertSame(fields[0], field.getDurationField());
        assertTrue(field.getDurationField().isPrecise());
    }

    @Test
    public void unitMillis_isOneSecond() {
        PreciseDateTimeField field = secondOfMinuteFieldUTC(null);
        assertEquals(1000L, field.getUnitMillis());
    }

    @Test
    public void minAndMaxValues_forSecondOfMinute_areExpected() {
        PreciseDateTimeField field = secondOfMinuteFieldUTC(null);
        assertEquals(0, field.getMinimumValue());
        assertEquals(59, field.getMaximumValue());
        assertFalse(field.isLenient());
    }

    @Test
    public void remainder_returnsMillisWithinCurrentSecond_positiveInstant() {
        PreciseDateTimeField field = secondOfMinuteFieldUTC(null);
        long instant = 70_250L; // 00:01:10.250 on 1970-01-01 UTC
        assertEquals(250L, field.remainder(instant));
    }

    @Test
    public void remainder_returnsFloorModMillisWithinSecond_negativeInstant() {
        PreciseDateTimeField field = secondOfMinuteFieldUTC(null);
        long instant = -250L;
        // Remainder is non-negative and less than 1000
        assertEquals(750L, field.remainder(instant));
    }

    @Test
    public void roundFloor_movesToPreviousSecondBoundary() {
        PreciseDateTimeField field = secondOfMinuteFieldUTC(null);
        long instant = 90_250L;
        assertEquals(90_000L, field.roundFloor(instant));
    }

    @Test
    public void roundCeiling_movesToNextSecondBoundary() {
        PreciseDateTimeField field = secondOfMinuteFieldUTC(null);
        long instant = 90_250L;
        assertEquals(91_000L, field.roundCeiling(instant));
    }

    @Test
    public void roundCeiling_onExactBoundary_returnsSameInstant() {
        PreciseDateTimeField field = secondOfMinuteFieldUTC(null);
        long instant = 91_000L;
        assertEquals(91_000L, field.roundCeiling(instant));
    }

    @Test
    public void roundCeiling_negativeInstant_movesUpToZero() {
        PreciseDateTimeField field = secondOfMinuteFieldUTC(null);
        long instant = -1L;
        assertEquals(0L, field.roundCeiling(instant));
    }

    @Test
    public void set_replacesSecondWithinMinute_andPreservesMillisOfSecond() {
        PreciseDateTimeField field = secondOfMinuteFieldUTC(null);
        long instant = 70_250L; // currently secondOfMinute = 10, millisOfSecond = 250
        long updated = field.set(instant, 5);
        // expected: minute start (60_000) + 5 seconds + 250 ms
        assertEquals(65_250L, updated);
    }

    @Test
    public void set_onBoundary_preservesMillisOfSecond() {
        PreciseDateTimeField field = secondOfMinuteFieldUTC(null);
        long instant = 60_000L; // exactly at minute boundary
        long updated = field.set(instant, 59);
        assertEquals(119_000L, updated); // 60_000 + 59,000
    }

    @Test
    public void set_outOfRange_throwsWithClearMessage() {
        PreciseDateTimeField field = secondOfMinuteFieldUTC(null);
        try {
            field.set(1234L, 60);
            fail("Expected IllegalArgumentException for value out of range");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Value 60 for secondOfMinute must be in the range [0,59]"));
        }
    }

    @Test
    public void constructor_rejectsImpreciseUnitDuration() {
        Chronology chrono = ISOChronology.getInstance(UTC);
        DurationField impreciseYears = chrono.years(); // length varies by leap years
        try {
            new PreciseDateTimeField(DateTimeFieldType.millisOfSecond(), impreciseYears, chrono.minutes());
            fail("Expected IllegalArgumentException for imprecise unit duration");
        } catch (IllegalArgumentException e) {
            assertEquals("Unit duration field must be precise", e.getMessage());
        }
    }

    @Test
    public void constructor_rejectsUnitMillisLessThanOne() {
        DurationField zeroUnitField = new ZeroUnitDurationField();
        try {
            new PreciseDateTimeField(DateTimeFieldType.millisOfSecond(), zeroUnitField, zeroUnitField);
            fail("Expected IllegalArgumentException for unit millis < 1");
        } catch (IllegalArgumentException e) {
            assertEquals("The unit milliseconds must be at least 1", e.getMessage());
        }
    }
}