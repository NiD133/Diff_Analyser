package org.joda.time.convert;

import junit.framework.TestCase;
import org.joda.time.MutablePeriod;

/**
 * Unit tests for {@link StringConverter} focusing on setting values into a Period.
 */
public class StringConverterTest extends TestCase {

    /**
     * Tests that setInto() for a Period throws an IllegalArgumentException
     * when provided with malformed or invalid string formats.
     */
    public void testSetIntoPeriod_throwsExceptionForInvalidFormat() {
        // A collection of strings that are not valid ISO8601 period formats.
        String[] invalidPeriodStrings = {
            "",                      // Empty string is invalid.
            "PXY",                   // Contains invalid characters 'X' and 'Y'.
            "PT0SXY",                // Contains trailing invalid characters.
            "P2Y4W3DT12H24M48SX"     // Contains a trailing invalid character 'X'.
        };

        for (String invalidString : invalidPeriodStrings) {
            MutablePeriod period = new MutablePeriod();
            try {
                // Attempt to parse the invalid string into a period.
                StringConverter.INSTANCE.setInto(period, invalidString, null);
                fail("Expected IllegalArgumentException for invalid period string: \"" + invalidString + "\"");
            } catch (IllegalArgumentException expected) {
                // This is the expected outcome.
            }
        }
    }
}