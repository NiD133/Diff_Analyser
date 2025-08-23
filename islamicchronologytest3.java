package org.joda.time.chrono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.TimeZone;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the factory methods of {@link IslamicChronology}.
 * This focuses on verifying the creation of chronologies with different time zones.
 */
class IslamicChronologyTestTest3 {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    @Test
    @DisplayName("getInstance(DateTimeZone) should return a chronology with the specified zone")
    void getInstance_withSpecificZone_returnsChronologyWithThatZone() {
        // Act: Create a chronology with a specific time zone
        Chronology chronology = IslamicChronology.getInstance(TOKYO);

        // Assert: The resulting chronology should have the exact same time zone
        assertEquals(TOKYO, chronology.getZone());
    }

    @Test
    @DisplayName("getInstance(null) should return a chronology with the default system zone")
    void getInstance_withNullZone_returnsChronologyWithDefaultZone() {
        // Arrange: Store the original default time zone and set a known one for the test
        DateTimeZone originalDefault = DateTimeZone.getDefault();
        try {
            DateTimeZone.setDefault(PARIS);

            // Act: Create a chronology with a null time zone
            Chronology chronology = IslamicChronology.getInstance(null);

            // Assert: The chronology should adopt the default time zone
            assertEquals(PARIS, chronology.getZone());
        } finally {
            // Teardown: Restore the original default time zone to avoid side effects
            DateTimeZone.setDefault(originalDefault);
        }
    }

    @Test
    @DisplayName("getInstance() should return an instance of IslamicChronology")
    void getInstance_returnsCorrectChronologyType() {
        // Act: Create a chronology instance
        Chronology chronology = IslamicChronology.getInstance(TOKYO);

        // Assert: The created object should be of the type IslamicChronology
        assertInstanceOf(IslamicChronology.class, chronology);
    }
}