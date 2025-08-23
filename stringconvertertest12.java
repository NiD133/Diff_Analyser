package org.joda.time.convert;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link StringConverter} focusing on its use in DateTime parsing.
 */
public class StringConverterTest {

    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    private DateTimeZone originalDefaultZone;
    private Locale originalDefaultLocale;

    @Before
    public void setUp() {
        // Save the original default time zone and locale
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();

        // Set a specific time zone and locale for predictable test results
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
     * Tests that creating a DateTime from a string without a time zone
     * correctly uses the default time zone.
     */
    @Test
    public void dateTimeFromString_whenNoZoneSpecified_usesDefaultTimeZone() {
        // Arrange
        String dateTimeString = "2004-06-09T12:24:48.501";
        DateTime expectedDateTime = new DateTime(2004, 6, 9, 12, 24, 48, 501, LONDON);

        // Act
        // The StringConverter is used implicitly by the DateTime(Object) constructor
        // to convert the string into a DateTime object.
        DateTime actualDateTime = new DateTime(dateTimeString);

        // Assert
        assertEquals(expectedDateTime, actualDateTime);
    }
}