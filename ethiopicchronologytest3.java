package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for the factory methods of {@link EthiopicChronology}.
 * This test class focuses on how the chronology is instantiated with different time zones.
 */
public class EthiopicChronologyFactoryTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    private DateTimeZone originalSystemDateTimeZone;
    private TimeZone originalSystemTimeZone;
    private Locale originalSystemLocale;

    @Before
    public void setUp() {
        // The getInstance(null) factory method relies on the default zone.
        // We must set it to a known value (LONDON) for a predictable test outcome.
        originalSystemDateTimeZone = DateTimeZone.getDefault();
        originalSystemTimeZone = TimeZone.getDefault();
        originalSystemLocale = Locale.getDefault();

        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore original system defaults to prevent side-effects on other tests.
        DateTimeZone.setDefault(originalSystemDateTimeZone);
        TimeZone.setDefault(originalSystemTimeZone);
        Locale.setDefault(originalSystemLocale);
    }

    @Test
    public void getInstance_withSpecificZone_returnsChronologyInThatZone() {
        // Act & Assert
        assertEquals("Chronology should be created in the specified TOKYO zone",
                TOKYO, EthiopicChronology.getInstance(TOKYO).getZone());
        assertEquals("Chronology should be created in the specified PARIS zone",
                PARIS, EthiopicChronology.getInstance(PARIS).getZone());
    }

    @Test
    public void getInstance_withNullZone_returnsChronologyInDefaultZone() {
        // The default zone is set to LONDON in the setUp() method.
        // Act
        EthiopicChronology chronology = EthiopicChronology.getInstance(null);

        // Assert
        assertEquals("A null zone should result in the default time zone",
                LONDON, chronology.getZone());
    }

    @Test
    public void getInstance_returnsCorrectClassType() {
        // This test ensures the factory returns an instance of the correct class,
        // not a subclass or other unexpected type.
        
        // Act & Assert
        assertSame("The factory should return an instance of EthiopicChronology",
                EthiopicChronology.class, EthiopicChronology.getInstance(TOKYO).getClass());
    }
}