package org.joda.time.convert;

import junit.framework.TestCase;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.JulianChronology;

/**
 * Test class for {@link StringConverter}.
 * This test focuses on creating a DateTime object from a String
 * with a specific Chronology.
 */
public class StringConverterTest extends TestCase {

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");

    /**
     * Tests that a DateTime object can be constructed from an ISO-formatted string
     * using an explicit Julian chronology. The DateTime constructor delegates this
     * conversion to the StringConverter.
     */
    public void testDateTimeCreation_fromStringWithExplicitJulianChronology() {
        // Arrange
        final String inputDateTimeString = "2004-06-09T12:24:48.501";
        final Chronology julianChronologyInParis = JulianChronology.getInstance(PARIS);

        // Construct the expected DateTime object for a clear and direct comparison.
        final DateTime expected = new DateTime(2004, 6, 9, 12, 24, 48, 501, julianChronologyInParis);

        // Act
        // The DateTime constructor uses the ConverterManager, which in turn selects
        // the StringConverter to handle the string input.
        final DateTime actual = new DateTime(inputDateTimeString, julianChronologyInParis);

        // Assert
        // A single assertion on the object is more concise and readable than
        // asserting each component field individually. It relies on a correct
        // implementation of DateTime.equals().
        assertEquals(expected, actual);
    }
}