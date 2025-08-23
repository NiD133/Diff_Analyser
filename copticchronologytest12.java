package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Test;

/**
 * Tests the era-specific behavior of CopticChronology.
 * This includes verifying the era constant and ensuring that dates before the first Coptic year are not allowed.
 */
public class CopticChronologyEraTest {

    private static final Chronology COPTIC_UTC = CopticChronology.getInstanceUTC();

    @Test
    public void amEra_hasSameValueAsCe() {
        // The Coptic 'Anno Martyrum' (AM) era is defined to be equivalent to the Common Era (CE).
        // In Joda-Time, DateTimeConstants.CE has a value of 1.
        assertEquals("The Coptic AM era value should match the CE value",
                DateTimeConstants.CE, CopticChronology.AM);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_throwsExceptionForYearZero() {
        // The Coptic chronology is not proleptic and does not support dates before year 1 AM.
        // Therefore, attempting to create a DateTime in year 0 should fail.
        new DateTime(0, 1, 1, 0, 0, 0, 0, COPTIC_UTC);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_throwsExceptionForNegativeYear() {
        // The Coptic chronology is not proleptic and does not support dates before year 1 AM.
        // Therefore, attempting to create a DateTime in a negative year should fail.
        new DateTime(-1, 1, 1, 0, 0, 0, 0, COPTIC_UTC);
    }
}