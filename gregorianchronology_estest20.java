package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Unit tests for the GregorianChronology class.
 */
public class GregorianChronologyTest {

    /**
     * Tests that calling withZone() on the UTC GregorianChronology instance with the UTC zone
     * returns the same instance. This verifies a common optimization path.
     */
    @Test
    public void withZone_onUtcInstanceWithUtcZone_returnsSameInstance() {
        // Arrange: Get the singleton instance of GregorianChronology for the UTC time zone.
        GregorianChronology utcChronology = GregorianChronology.getInstanceUTC();

        // Act: Request the chronology for the same UTC zone.
        Chronology resultChronology = utcChronology.withZone(DateTimeZone.UTC);

        // Assert: The returned object should be the exact same instance, not a new one.
        assertSame("Expected withZone(UTC) on the UTC instance to return itself",
                utcChronology, resultChronology);
    }
}