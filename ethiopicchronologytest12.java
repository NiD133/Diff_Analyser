package org.joda.time.chrono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Era field of the EthiopicChronology.
 */
class EthiopicChronologyEraTest {

    private static final Chronology ETHIOPIC_UTC = EthiopicChronology.getInstanceUTC();

    /**
     * The Ethiopic Era (EE) constant should be equivalent to the Common Era (CE).
     */
    @Test
    void eraConstant_isCE() {
        assertEquals(DateTimeConstants.CE, EthiopicChronology.EE);
    }

    /**
     * The Ethiopic chronology is not proleptic and does not support years before 1.
     * This test verifies that attempting to create a date in a year before the
     * first Ethiopic year (e.g., year -1) throws an IllegalArgumentException.
     */
    @Test
    void constructor_throwsForYearBefore1() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Attempt to create a date in year -1, which is before the start of the Ethiopic calendar.
            new DateTime(-1, 13, 5, 0, 0, 0, 0, ETHIOPIC_UTC);
        });
    }
}