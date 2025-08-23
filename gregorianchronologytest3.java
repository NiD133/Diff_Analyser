package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.TimeZone;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the factory methods of GregorianChronology.
 */
public class GregorianChronologyTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private DateTimeZone originalDefaultZone = null;

    @Before
    public void setUp() {
        // Save the original default time zone and set it to a known value for the test.
        // This is necessary for testing the behavior of getInstance(null).
        originalDefaultZone = DateTimeZone.getDefault();
        DateTimeZone.setDefault(LONDON);
    }

    @After
    public void tearDown() {
        // Restore the original default time zone.
        DateTimeZone.setDefault(originalDefaultZone);
    }

    @Test
    public void testGetInstance_withSpecificZone_returnsChronologyWithThatZone() {
        // The factory method should return a chronology with the specified time zone.
        assertEquals(PARIS, GregorianChronology.getInstance(PARIS).getZone());
        assertEquals(TOKYO, GregorianChronology.getInstance(TOKYO).getZone());
    }

    @Test
    public void testGetInstance_withNullZone_returnsChronologyWithDefaultZone() {
        // A null zone should result in a chronology with the default time zone.
        // The default zone is set to LONDON in the setUp method.
        assertEquals(LONDON, GregorianChronology.getInstance(null).getZone());
    }

    @Test
    public void testGetInstance_returnsInstanceOfGregorianChronology() {
        // The factory method should return an instance of the correct class.
        assertSame(GregorianChronology.class, GregorianChronology.getInstance(TOKYO).getClass());
    }
}