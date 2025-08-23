package org.threeten.extra.scale;

import org.junit.Test;
import java.time.format.DateTimeParseException;

/**
 * Tests the parsing functionality of {@link TaiInstant}.
 */
public class TaiInstantParseTest {

    /**
     * Tests that parsing a string with an incorrect number of nanosecond digits
     * fails with a DateTimeParseException. The format requires exactly nine digits.
     */
    @Test(expected = DateTimeParseException.class)
    public void parse_invalidStringWithTooFewNanoDigits_throwsException() {
        // The TAI format requires exactly nine digits for the nanosecond part.
        // This input string is invalid because it has only eight.
        final String textWithEightNanoDigits = "0.00000000s(TAI)";
        
        TaiInstant.parse(textWithEightNanoDigits);
    }
}