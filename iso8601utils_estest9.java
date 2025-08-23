package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ISO8601Utils}.
 */
public class ISO8601UtilsTest {

    @Test
    public void format_withTimezoneHavingMillisecondOffset_formatsDateAndOffsetCorrectly() {
        // Arrange
        // The Unix epoch: 1970-01-01T00:00:00.000Z
        Date epochDate = new Date(0L);

        // A custom timezone with a raw offset of 1255 milliseconds. This tests an edge case
        // where the offset is not a round number of minutes or seconds.
        int millisecondOffset = 1255;
        TimeZone timezoneWithMillisecondOffset = new SimpleTimeZone(millisecondOffset, "TestZone");

        // The expected result is calculated as follows:
        // 1. The date-time is adjusted by the offset: 0ms (epoch) + 1255ms = 1255ms.
        // 2. Since milliseconds are not included (the second argument to format() is false),
        //    1255ms is represented as 1 second ("01").
        // 3. The timezone offset itself (1255ms) is less than a full minute, so it is
        //    truncated to "+00:00" in the final string.
        String expectedFormattedString = "1970-01-01T00:00:01+00:00";

        // Act
        String actualFormattedString = ISO8601Utils.format(epochDate, false, timezoneWithMillisecondOffset);

        // Assert
        assertEquals(expectedFormattedString, actualFormattedString);
    }
}