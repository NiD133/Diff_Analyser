package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Tests for the factory methods of {@link ISOChronology}.
 */
public class ISOChronologyTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    @Test
    public void getInstance_withSpecificZone_returnsChronologyWithThatZone() {
        // Act & Assert
        assertEquals("Chronology should have the specified TOKYO time zone",
                TOKYO, ISOChronology.getInstance(TOKYO).getZone());
        assertEquals("Chronology should have the specified PARIS time zone",
                PARIS, ISOChronology.getInstance(PARIS).getZone());
    }

    @Test
    public void getInstance_withNullZone_returnsChronologyWithDefaultZone() {
        // This test verifies that getInstance(null) correctly uses the default time zone.
        // We temporarily set the default zone to a known value to ensure the test is
        // deterministic and not dependent on the host system's configuration.
        DateTimeZone originalDefault = DateTimeZone.getDefault();
        try {
            DateTimeZone.setDefault(LONDON);

            // Act
            Chronology chronology = ISOChronology.getInstance(null);

            // Assert
            assertEquals("Chronology with null zone should use the default zone",
                    LONDON, chronology.getZone());
        } finally {
            // Clean up to prevent side effects on other tests
            DateTimeZone.setDefault(originalDefault);
        }
    }

    @Test
    public void getInstance_returnsInstanceOfISOChronology() {
        // Act & Assert
        assertSame("The returned object should be an instance of ISOChronology",
                ISOChronology.class, ISOChronology.getInstance(TOKYO).getClass());
        
        assertSame("The returned object for a null zone should also be an instance of ISOChronology",
                ISOChronology.class, ISOChronology.getInstance(null).getClass());
    }
}