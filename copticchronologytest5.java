package org.joda.time.chrono;

import static org.junit.Assert.assertSame;

import java.util.Locale;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the time zone handling in CopticChronology.
 * This test focuses on the {@link CopticChronology#withUTC()} method.
 */
public class CopticChronologyTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // Fields to store the original default settings
    private DateTimeZone originalDefaultZone;
    private Locale originalDefaultLocale;

    @Before
    public void setUp() {
        // Save the original default time zone and locale
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();

        // Set a specific default time zone and locale for the tests
        // to ensure consistent behavior regardless of the execution environment.
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);
    }



    @After
    public void tearDown() {
        // Restore the original default time zone and locale to avoid side effects
        DateTimeZone.setDefault(originalDefaultZone);
        Locale.setDefault(originalDefaultLocale);
    }

    /**
     * Tests that calling withUTC() on any CopticChronology instance
     * returns the canonical UTC singleton instance.
     */
    @Test
    public void withUTC_onAnyInstance_returnsSameUTCInstance() {
        // Arrange
        CopticChronology copticUTC = CopticChronology.getInstanceUTC();
        CopticChronology copticLondon = CopticChronology.getInstance(LONDON);
        CopticChronology copticTokyo = CopticChronology.getInstance(TOKYO);
        
        // getInstance() uses the default time zone, which is set to LONDON in setUp()
        CopticChronology copticDefault = CopticChronology.getInstance();

        // Act & Assert
        // The withUTC() method should always return the singleton UTC instance.
        // We use assertSame to verify that it's the exact same object, not just an equal one.

        assertSame("withUTC() on a London-zoned instance should return the UTC singleton",
                copticUTC, copticLondon.withUTC());

        assertSame("withUTC() on a Tokyo-zoned instance should return the UTC singleton",
                copticUTC, copticTokyo.withUTC());

        assertSame("withUTC() on a default-zoned instance should return the UTC singleton",
                copticUTC, copticDefault.withUTC());

        assertSame("withUTC() on the UTC instance itself should be idempotent",
                copticUTC, copticUTC.withUTC());
    }
}