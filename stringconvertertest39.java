package org.joda.time.convert;

import java.util.Locale;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableInterval;

/**
 * Unit tests for {@link StringConverter} focusing on exception scenarios when setting an interval.
 * This test ensures the converter correctly handles invalid or incomplete string formats.
 */
public class StringConverterSetIntoIntervalExceptionTest extends TestCase {

    private DateTimeZone originalDefaultZone = null;
    private Locale originalDefaultLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        // The suite should run the tests contained within this class.
        return new TestSuite(StringConverterSetIntoIntervalExceptionTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        // Save the original default timezone and locale to restore them after the test.
        // This ensures test isolation and predictable behavior.
        originalDefaultZone = DateTimeZone.getDefault();
        originalDefaultLocale = Locale.getDefault();

        // Set a specific default timezone and locale for consistent test results.
        DateTimeZone.setDefault(DateTimeZone.forID("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore the original default timezone and locale.
        DateTimeZone.setDefault(originalDefaultZone);
        Locale.setDefault(originalDefaultLocale);
    }

    /**
     * Tests that setInto() throws IllegalArgumentException for an incomplete interval string.
     *
     * An ISO8601 interval can be represented as <start>/<end>, <start>/<period>, or <period>/<end>.
     * The string "P1Y/" represents an interval defined by a period ending at a specific time.
     * However, the end instant is missing, making the string invalid for parsing.
     */
    public void testSetInto_whenIntervalStringIsMissingEndDate_throwsIllegalArgumentException() {
        // Arrange
        final String incompleteIntervalString = "P1Y/";
        final MutableInterval interval = new MutableInterval(0L, 1000L);
        final StringConverter converter = StringConverter.INSTANCE;

        // Act & Assert
        try {
            converter.setInto(interval, incompleteIntervalString, null);
            fail("Expected an IllegalArgumentException for an incomplete interval string.");
        } catch (IllegalArgumentException expected) {
            // This is the expected behavior; the test passes.
        }
    }
}