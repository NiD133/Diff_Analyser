package org.joda.time.tz;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A test suite for the {@link CachedDateTimeZone} class.
 */
public class CachedDateTimeZoneTest {

    /**
     * Tests that {@link CachedDateTimeZone#getName(long)} returns the correct long name for a given instant.
     * This test uses the "WET" time zone, which is a non-fixed zone that Joda-Time wraps
     * with a {@link CachedDateTimeZone} instance by default.
     */
    @Test
    public void getNameForWETZoneShouldReturnCorrectLongName() {
        // Arrange
        // The factory method DateTimeZone.forID() returns a CachedDateTimeZone for non-fixed zones like "WET".
        final DateTimeZone wetZone = DateTimeZone.forID("WET");
        final String expectedName = "Western European Time";

        // Define a specific instant in time to test against.
        // This corresponds to the magic number 212544000000L in the original test.
        final long instant = new DateTime(1976, 9, 26, 0, 0, DateTimeZone.UTC).getMillis();

        // Act
        final String actualName = wetZone.getName(instant);

        // Assert
        // First, confirm we are indeed testing a CachedDateTimeZone instance.
        assertTrue("The zone for 'WET' should be a CachedDateTimeZone instance.",
                   wetZone instanceof CachedDateTimeZone);
        
        // Second, verify that the name is correct for the given instant.
        assertEquals("The long name for the WET zone should be correct.",
                     expectedName, actualName);
    }
}