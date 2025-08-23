package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the GregorianChronology class.
 */
public class GregorianChronologyTest {

    /**
     * Tests that calling withZone() with a null argument returns a Chronology
     * instance configured with the default system time zone.
     */
    @Test
    public void withZone_givenNull_returnsChronologyInDefaultTimeZone() {
        // Arrange: Create a base chronology in a non-default zone (UTC) to ensure
        // the test is meaningful. We also retrieve the system's default time zone.
        Chronology baseChronologyInUTC = GregorianChronology.getInstanceUTC();
        DateTimeZone systemDefaultZone = DateTimeZone.getDefault();

        // Act: Call the method under test with a null time zone.
        Chronology resultChronology = baseChronologyInUTC.withZone(null);

        // Assert: The resulting chronology's time zone should match the system's default.
        // This correctly verifies the documented behavior that 'null' means 'default zone'.
        assertEquals("The chronology should be in the default time zone",
                systemDefaultZone, resultChronology.getZone());
    }
}