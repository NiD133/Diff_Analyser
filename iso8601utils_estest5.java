package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.util.Date;
import java.util.TimeZone;

/**
 * Tests for {@link ISO8601Utils}.
 */
public class ISO8601UtilsTest {

    @Test(expected = NullPointerException.class)
    public void formatWithNullTimeZoneThrowsException() {
        // The format method is expected to throw a NullPointerException when the TimeZone is null.
        Date anyDate = new Date();
        boolean includeMillis = false;

        ISO8601Utils.format(anyDate, includeMillis, null);
    }
}