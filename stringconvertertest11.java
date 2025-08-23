package org.joda.time.convert;

import java.util.Locale;
import junit.framework.TestCase;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Tests the creation of a DateTime object from a String.
 * This class focuses on verifying the behavior of the String-based constructor,
 * which implicitly uses the StringConverter.
 */
public class DateTimeFromStringConversionTest extends TestCase {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private DateTimeZone originalDefaultZone;
    private Locale originalDefaultLocale;

    @Override
    protected void setUp() throws Exception {
        // Save the original default time zone and locale to ensure that this test
        // does not affect other tests.
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();

        // Set a specific default time zone for this test. This is crucial for
        // the behavior being verified.
        DateTimeZone.setDefault(LONDON);
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore the original default time zone and locale.
        DateTimeZone.setDefault(originalDefaultZone);
        Locale.setDefault(originalDefaultLocale);
    }

    /**
     * Tests that a DateTime is correctly parsed from an ISO-8601 formatted string.
     *
     * <p>This test also verifies a specific behavior of the DateTime(String) constructor:
     * when the time zone offset in the parsed string (+01:00) matches the offset of the
     * default time zone for that instant (which is true for London in summer), the
     * resulting DateTime object adopts the full default time zone (Europe/London),
     * not just a fixed-offset zone.
     */
    public void testCreationFromIsoStringWithMatchingZoneOffset() {
        // Arrange
        // The input string includes a time zone offset of +01:00.
        String dateTimeString = "2004-06-09T12:24:48.501+01:00";

        // On 2004-06-09, the "Europe/London" timezone (set as default in setUp)
        // was on British Summer Time (BST), which has a +01:00 offset.
        // We therefore expect the parsed DateTime to be created with the full "Europe/London" zone.
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501, LONDON);

        // Act
        // The DateTime constructor uses the StringConverter under the hood to parse the string.
        DateTime actualDateTime = new DateTime(dateTimeString);

        // Assert
        // A single equality check on the DateTime objects is more readable and comprehensive
        // than asserting each field individually. It verifies the instant, chronology, and time zone.
        assertEquals(expectedDateTime, actualDateTime);
    }
}