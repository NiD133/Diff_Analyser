package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for DateTimeComparator focused on clarity and behavior.
 *
 * Intent:
 * - Make the purpose of each test obvious.
 * - Group related assertions.
 * - Avoid EvoSuite-specific scaffolding and brittle inputs.
 */
public class DateTimeComparatorTest {

    private static final long ONE_HOUR = 60L * 60L * 1000L;
    private static final long ONE_DAY = 24L * ONE_HOUR;

    // ---------------------------------------------------------------------
    // Factory methods and limits
    // ---------------------------------------------------------------------

    @Test
    public void allFieldsComparator_hasNoLimits_andReadableToString() {
        DateTimeComparator cmp = DateTimeComparator.getInstance();

        assertNull("No lower limit expected", cmp.getLowerLimit());
        assertNull("No upper limit expected", cmp.getUpperLimit());
        assertEquals("Readable toString", "DateTimeComparator[]", cmp.toString());
    }

    @Test
    public void dateOnlyComparator_hasDayOfYearAsLowerLimit() {
        DateTimeComparator cmp = DateTimeComparator.getDateOnlyInstance();

        assertEquals("Lower limit is dayOfYear", DateTimeFieldType.dayOfYear(), cmp.getLowerLimit());
        assertNull("No upper limit for date-only", cmp.getUpperLimit());
        assertEquals("Readable toString", "DateTimeComparator[dayOfYear-]", cmp.toString());
    }

    @Test
    public void timeOnlyComparator_hasDayOfYearAsUpperLimit() {
        DateTimeComparator cmp = DateTimeComparator.getTimeOnlyInstance();

        assertNull("No lower limit for time-only", cmp.getLowerLimit());
        assertEquals("Upper limit is dayOfYear", DateTimeFieldType.dayOfYear(), cmp.getUpperLimit());
        assertEquals("Readable toString", "DateTimeComparator[-dayOfYear]", cmp.toString());
    }

    @Test
    public void comparator_withExplicitLowerLimit() {
        DateTimeFieldType lower = DateTimeFieldType.yearOfEra();
        DateTimeComparator cmp = DateTimeComparator.getInstance(lower);

        assertEquals("Lower limit should be preserved", lower, cmp.getLowerLimit());
        assertNull("No upper limit set", cmp.getUpperLimit());
    }

    @Test
    public void comparator_withExplicitLowerAndUpperLimit_allowsSameField_andHasCompactToString() {
        DateTimeFieldType field = DateTimeFieldType.weekyearOfCentury();
        DateTimeComparator cmp = DateTimeComparator.getInstance(field, field);

        // When lower == upper, toString is compact (no dash)
        assertEquals("DateTimeComparator[weekyearOfCentury]", cmp.toString());
    }

    @Test
    public void comparator_withNullLimits_isSameBehaviorAsAllFields() {
        DateTimeComparator cmp = DateTimeComparator.getInstance(null, null);

        assertNull(cmp.getLowerLimit());
        assertNull(cmp.getUpperLimit());
        assertEquals("DateTimeComparator[]", cmp.toString());
    }

    // ---------------------------------------------------------------------
    // Equality and hashCode
    // ---------------------------------------------------------------------

    @Test
    public void equals_isReflexive_andConsistentWithHashCode_forSameConfiguration() {
        DateTimeComparator a = DateTimeComparator.getTimeOnlyInstance();
        // Construct the same configuration via the generic factory:
        DateTimeComparator b = DateTimeComparator.getInstance(null, DateTimeFieldType.dayOfYear());

        assertTrue(a.equals(a));           // reflexive
        assertTrue(a.equals(b));           // same configuration
        assertTrue(b.equals(a));           // symmetric
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_isFalse_forDifferentConfigurations_andForDifferentTypes() {
        DateTimeComparator all = DateTimeComparator.getInstance();
        DateTimeComparator dateOnly = DateTimeComparator.getDateOnlyInstance();
        DateTimeComparator timeOnly = DateTimeComparator.getTimeOnlyInstance();

        assertFalse(all.equals(dateOnly));
        assertFalse(all.equals(timeOnly));
        assertFalse(dateOnly.equals(timeOnly));
        assertFalse(timeOnly.equals(new Object()));
    }

    // ---------------------------------------------------------------------
    // Comparison behavior
    // ---------------------------------------------------------------------

    @Test
    public void compare_bothNull_returnsZero() {
        DateTimeComparator cmp = DateTimeComparator.getTimeOnlyInstance();
        assertEquals(0, cmp.compare(null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void compare_withUnparseableString_throwsIllegalArgumentException() {
        DateTimeComparator cmp = DateTimeComparator.getDateOnlyInstance();
        cmp.compare(null, "not-a-date");
    }

    @Test
    public void allFieldsComparator_comparesFullInstant() {
        DateTimeComparator cmp = DateTimeComparator.getInstance();

        long t0 = 0L;               // 1970-01-01T00:00:00Z
        long t1 = ONE_HOUR;         // 1970-01-01T01:00:00Z

        assertTrue(cmp.compare(t0, t1) < 0);
        assertTrue(cmp.compare(t1, t0) > 0);
        assertEquals(0, cmp.compare(t0, t0));
    }

    @Test
    public void dateOnlyComparator_ignoresTimeOfDay() {
        DateTimeComparator cmp = DateTimeComparator.getDateOnlyInstance();

        long midnight = 0L;         // 1970-01-01T00:00:00Z
        long oneAM = ONE_HOUR;      // 1970-01-01T01:00:00Z

        // Same day -> equal when time-of-day is ignored
        assertEquals(0, cmp.compare(midnight, oneAM));

        // Different days -> ordered by date
        long nextDayMidnight = ONE_DAY; // 1970-01-02T00:00:00Z
        assertTrue(cmp.compare(midnight, nextDayMidnight) < 0);
    }

    @Test
    public void timeOnlyComparator_ignoresDate() {
        DateTimeComparator cmp = DateTimeComparator.getTimeOnlyInstance();

        long day1Midnight = 0L;             // 00:00
        long day2Midnight = ONE_DAY;        // 00:00 next day -> equal time-of-day
        assertEquals(0, cmp.compare(day1Midnight, day2Midnight));

        long day1OneAM = ONE_HOUR;          // 01:00
        long day2MidnightAgain = ONE_DAY;   // 00:00
        assertTrue(cmp.compare(day1OneAM, day2MidnightAgain) > 0); // 01:00 > 00:00
    }

    // ---------------------------------------------------------------------
    // Hash code should be callable without side effects
    // ---------------------------------------------------------------------

    @Test
    public void hashCode_isStableAndDoesNotThrow() {
        DateTimeComparator a = DateTimeComparator.getInstance();
        DateTimeComparator b = DateTimeComparator.getTimeOnlyInstance();
        DateTimeComparator c = DateTimeComparator.getDateOnlyInstance();

        // Just ensure it doesn't throw and is stable across calls
        assertEquals(a.hashCode(), a.hashCode());
        assertEquals(b.hashCode(), b.hashCode());
        assertEquals(c.hashCode(), c.hashCode());
    }
}