package org.joda.time.convert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the StringConverter, focusing on its use via the DateTime constructor.
 * This test verifies the round-trip conversion of a DateTime object to a String and back.
 */
public class StringConverterTest {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    // Store original default zone and locale to restore them after tests
    private DateTimeZone originalDefaultZone;
    private Locale originalDefaultLocale;

    @Before
    public void setUp() {
        // Save the original defaults to ensure test isolation
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();

        // Set a known default for test consistency
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);
    }

    @After
    public void tearDown() {
        // Restore the original defaults to avoid side effects on other tests
        DateTimeZone.setDefault(originalDefaultZone);
        Locale.setDefault(originalDefaultLocale);
    }

    /**
     * Tests that a DateTime object can be successfully reconstructed from its
     * standard string representation, preserving the original value and time zone.
     * This implicitly tests the StringConverter's ability to parse a standard
     * ISO8601 date-time string, which is used by the DateTime(String) constructor.
     */
    @Test
    public void dateTimeConstructor_shouldReconstructObjectFromItsOwnStringRepresentation() {
        // Arrange: Create a reference DateTime object and its string representation.
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501, PARIS);
        String dateTimeString = expectedDateTime.toString();

        // Act: Re-create the DateTime by parsing its string representation.
        // The StringConverter is used internally by this constructor.
        DateTime actualDateTime = new DateTime(dateTimeString, PARIS);

        // Assert: The reconstructed object should be equal to the original.
        assertEquals(expectedDateTime, actualDateTime);
    }
}