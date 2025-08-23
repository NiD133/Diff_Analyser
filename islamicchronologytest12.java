package org.joda.time.chrono;

import junit.framework.TestCase;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;

/**
 * This class contains tests for the era-related functionality of the IslamicChronology.
 * It focuses on verifying the era constant and the handling of dates before the first Islamic year.
 */
public class IslamicChronologyEraTest extends TestCase {

    private static final Chronology ISLAMIC_UTC = IslamicChronology.getInstanceUTC();

    /**
     * Tests that the Anno Hegirae (AH) era constant has the expected value.
     */
    public void testEraConstant_isAH() {
        // The Islamic calendar has only one era, AH (Anno Hegirae).
        // Its value is expected to be 1, consistent with the CE era in other chronologies.
        assertEquals("IslamicChronology.AH should have a value of 1", 1, IslamicChronology.AH);
    }

    /**
     * Tests that creating a DateTime for a year before 1 AH throws an exception.
     * The IslamicChronology is not proleptic and does not support years before the Hijrah.
     */
    public void testDateTimeCreation_throwsExceptionForYearsBeforeEra() {
        // Year 1 is the first year in the Islamic calendar.
        // Attempts to create a date in year 0 or a negative year should fail.

        // Test with year 0
        try {
            new DateTime(0, 1, 1, 0, 0, 0, 0, ISLAMIC_UTC);
            fail("Creating a DateTime in year 0 should have thrown an exception.");
        } catch (IllegalFieldValueException ex) {
            // This is the expected behavior.
        }

        // Test with a negative year (-1)
        try {
            // The original test used an invalid month (13), which could mask the true reason for failure.
            // Using a valid month (1) ensures we are testing the year validation logic specifically.
            new DateTime(-1, 1, 5, 0, 0, 0, 0, ISLAMIC_UTC);
            fail("Creating a DateTime in a negative year should have thrown an exception.");
        } catch (IllegalFieldValueException ex) {
            // This is the expected behavior.
        }
    }
}