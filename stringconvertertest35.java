package org.joda.time.convert;

import static org.junit.Assert.assertEquals;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.MutableInterval;
import org.joda.time.chrono.BuddhistChronology;
import org.junit.Test;

/**
 * Unit tests for {@link StringConverter}.
 * This class focuses on the setInto(ReadWritableInterval, Object, Chronology) method.
 */
public class StringConverterTest {

    /**
     * Tests that setInto correctly parses an ISO8601 interval string (start/end)
     * and updates a MutableInterval using a specific chronology.
     */
    @Test
    public void setInto_updatesIntervalFromString_usingProvidedChronology() {
        // Arrange
        final String intervalString = "2003-08-09/2004-06-09";
        final Chronology buddhistChronology = BuddhistChronology.getInstance();
        
        // The initial state of the interval is irrelevant, as setInto should overwrite it completely.
        MutableInterval interval = new MutableInterval(0L, 0L);

        // Act
        StringConverter.INSTANCE.setInto(interval, intervalString, buddhistChronology);

        // Assert
        final DateTime expectedStart = new DateTime(2003, 8, 9, 0, 0, 0, 0, buddhistChronology);
        final DateTime expectedEnd = new DateTime(2004, 6, 9, 0, 0, 0, 0, buddhistChronology);

        assertEquals("The start of the interval should match the parsed string.", expectedStart, interval.getStart());
        assertEquals("The end of the interval should match the parsed string.", expectedEnd, interval.getEnd());
        assertEquals("The chronology of the interval should be set to the provided chronology.", buddhistChronology, interval.getChronology());
    }
}