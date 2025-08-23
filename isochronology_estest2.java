package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

// Note: The original test class name and inheritance from a scaffolding class
// are preserved. In a typical refactoring, these would be simplified.
public class ISOChronology_ESTestTest2 extends ISOChronology_ESTest_scaffolding {

    /**
     * Verifies that `withZone()` returns a new, distinct `ISOChronology` instance
     * when provided with a time zone different from the original.
     */
    @Test(timeout = 4000)
    public void withZone_whenGivenDifferentZone_shouldReturnNewInstance() {
        // Arrange
        // Get an ISOChronology instance in the default time zone.
        ISOChronology initialChronology = ISOChronology.getInstance();
        DateTimeZone initialZone = initialChronology.getZone();

        // Create a custom time zone that is highly unlikely to be the default.
        // The offset (+02:06:33.750) is arbitrary and chosen for its uniqueness.
        DateTimeZone customZone = DateTimeZone.forOffsetMillis(7593750);

        // Pre-condition: Ensure the test is valid by confirming the zones are different.
        assertNotEquals("Test setup error: custom zone is the same as the initial zone.",
                initialZone, customZone);

        // Act
        // Request a new chronology instance with the custom time zone.
        Chronology newChronology = initialChronology.withZone(customZone);

        // Assert
        // 1. The returned object must be a new instance, as chronologies are immutable.
        assertNotSame("A new Chronology instance should be returned for a different zone.",
                initialChronology, newChronology);

        // 2. The new instance must be of the correct type.
        assertTrue("The new instance should be an ISOChronology.",
                newChronology instanceof ISOChronology);

        // 3. The new instance must be configured with the specified time zone.
        assertEquals("The new chronology should have the custom time zone.",
                customZone, newChronology.getZone());

        // 4. The original instance must remain unchanged.
        assertEquals("The original chronology's time zone should not be modified.",
                initialZone, initialChronology.getZone());
    }
}