package org.joda.time.convert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.MutableInterval;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the behavior of {@link StringConverter#setInto(ReadWritableInterval, Object, Chronology)}
 * when parsing an interval string.
 */
public class StringConverterIntervalTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    @Test
    public void setInto_shouldParseIsoIntervalStringAndUseDefaultChronology_whenChronologyIsNull() {
        // The StringConverter uses the default Chronology (and thus default time zone)
        // when a null Chronology is passed. This test verifies this behavior by
        // setting a specific default time zone and checking the result.
        DateTimeZone originalDefaultZone = DateTimeZone.getDefault();
        try {
            // Arrange: Set a specific default time zone for this test.
            DateTimeZone.setDefault(LONDON);

            final String intervalString = "2003-08-09/2004-06-09";
            // An empty interval that will be updated by the method under test.
            final MutableInterval intervalToUpdate = new MutableInterval(0L, 0L);

            // Define the expected result explicitly, using the LONDON time zone.
            final DateTime expectedStart = new DateTime(2003, 8, 9, 0, 0, 0, 0, LONDON);
            final DateTime expectedEnd = new DateTime(2004, 6, 9, 0, 0, 0, 0, LONDON);
            final Interval expectedInterval = new Interval(expectedStart, expectedEnd);

            // Act: Call the method under test with a null chronology.
            StringConverter.INSTANCE.setInto(intervalToUpdate, intervalString, null);

            // Assert: Verify that the interval was updated correctly.
            assertEquals("The updated interval should match the parsed string.",
                    expectedInterval, intervalToUpdate);
            
            // Also, explicitly verify that the chronology is the default (ISO in the LONDON zone).
            assertEquals("The interval's chronology should be the default.",
                    ISOChronology.getInstance(LONDON), intervalToUpdate.getChronology());

        } finally {
            // Teardown: Restore the original default time zone to avoid side effects.
            DateTimeZone.setDefault(originalDefaultZone);
        }
    }
}