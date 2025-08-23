package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the toString() method of {@link ISOChronology}.
 */
public class ISOChronologyToStringTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private DateTimeZone originalDefaultZone;

    @Before
    public void setUp() {
        // Store the original default time zone before modifying it for the test.
        originalDefaultZone = DateTimeZone.getDefault();
        // Set a specific default time zone for tests that rely on it.
        DateTimeZone.setDefault(LONDON);
    }

    @After
    public void tearDown() {
        // Restore the original default time zone to prevent side effects on other tests.
        DateTimeZone.setDefault(originalDefaultZone);
    }

    @Test
    public void toString_shouldReturnNameAndExplicitZoneId() {
        assertEquals("ISOChronology[Europe/London]", ISOChronology.getInstance(LONDON).toString());
        assertEquals("ISOChronology[Asia/Tokyo]", ISOChronology.getInstance(TOKYO).toString());
    }

    @Test
    public void toString_shouldUseDefaultZoneWhenUnspecified() {
        // The setUp method sets the default time zone to LONDON.
        assertEquals("ISOChronology[Europe/London]", ISOChronology.getInstance().toString());
    }

    @Test
    public void toString_shouldReturnUTCForUTCInstance() {
        assertEquals("ISOChronology[UTC]", ISOChronology.getInstanceUTC().toString());
    }
}