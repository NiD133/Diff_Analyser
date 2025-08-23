package com.google.gson.internal.bind.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Test for {@link ISO8601Utils}.
 * Note: The original class name "ISO8601Utils_ESTestTest22" suggests it was auto-generated.
 * A more conventional name would be "ISO8601UtilsTest".
 */
public class ISO8601Utils_ESTestTest22 {

    /**
     * Tests that formatting a date with a timezone that has a sub-minute offset
     * results in a zero-minute offset in the output string (e.g., "+00:00").
     */
    @Test
    public void format_withTimezoneHavingSubMinuteOffset_rendersOffsetAsZero() {
        // Arrange
        // This epoch time corresponds to the instant 3200-06-23T00:00:00.000Z.
        // Using a fixed epoch long makes the test deterministic and independent of the
        // system's default timezone, unlike the original test's use of new MockDate(...).
        Date dateToFormat = new Date(38822688000000L);

        // Create a timezone with a raw offset of -141 milliseconds. This is a corner
        // case to test how the formatter handles offsets that are not whole minutes.
        final int subMinuteOffsetInMillis = -141;
        TimeZone timezoneWithSubMinuteOffset = new SimpleTimeZone(subMinuteOffsetInMillis, "TestZone");

        // The instant in time is 3200-06-23T00:00:00.000Z.
        // The local time in a timezone with an offset of -141ms is 141ms *earlier*.
        // So, the expected local time is 3200-06-22T23:59:59.859.
        // This test verifies that the timezone offset part "[-|+]hh:mm" is formatted
        // as "-00:00" because the absolute offset is less than a full minute.
        String expectedFormattedString = "3200-06-22T23:59:59.859-00:00";

        // Act
        String actualFormattedString = ISO8601Utils.format(dateToFormat, true, timezoneWithSubMinuteOffset);

        // Assert
        assertEquals(
            "The formatted timezone offset should be zero when the actual offset is less than a minute.",
            expectedFormattedString,
            actualFormattedString
        );
    }
}