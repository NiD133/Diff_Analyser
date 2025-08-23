package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link ISOChronology} class, focusing on instance creation.
 */
public class ISOChronologyTest {

    /**
     * Tests that getInstance(DateTimeZone) returns a valid chronology
     * configured with the specified time zone.
     */
    @Test
    public void getInstance_withSpecificTimeZone_returnsChronologyForThatZone() {
        // Arrange: Create a custom, non-UTC time zone.
        final DateTimeZone customZone = DateTimeZone.forOffsetMillis(1000); // Using a round number

        // Act: Get an ISOChronology instance for the custom time zone.
        final ISOChronology chronology = ISOChronology.getInstance(customZone);

        // Assert: The returned chronology should not be null and should be associated with the correct time zone.
        assertNotNull("The factory method should not return a null chronology.", chronology);
        assertEquals("The chronology's time zone should match the one provided.",
                customZone, chronology.getZone());
    }
}