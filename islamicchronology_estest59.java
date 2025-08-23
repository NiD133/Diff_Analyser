package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Test suite for the IslamicChronology class.
 */
public class IslamicChronologyTest {

    /**
     * Tests that calling withZone() with a null argument returns a new
     * Chronology instance configured with the default system time zone.
     */
    @Test
    public void withZone_givenNull_shouldReturnChronologyInDefaultZone() {
        // Arrange: Get a chronology in a specific zone (UTC) and the system's default zone.
        // The Javadoc for withZone() states that a null input should result in using the default zone.
        Chronology initialChronology = IslamicChronology.getInstanceUTC();
        DateTimeZone defaultZone = DateTimeZone.getDefault();

        // Act: Request a new chronology with a null time zone.
        Chronology newChronology = initialChronology.withZone(null);

        // Assert: The new chronology should have the default time zone and be a different instance.
        assertEquals("The new chronology should have the default time zone",
                defaultZone, newChronology.getZone());
        assertNotSame("A new chronology instance should be returned",
                initialChronology, newChronology);
    }
}