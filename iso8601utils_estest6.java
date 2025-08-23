package com.google.gson.internal.bind.util;

import org.junit.Test;
import java.util.Date;

/**
 * Tests for {@link ISO8601Utils}.
 */
public class ISO8601UtilsTest {

    /**
     * The format method should not accept a null Date object and must throw a
     * NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void format_withNullDate_throwsNullPointerException() {
        // Call the format method with a null Date, which is expected to fail.
        // The second argument (include milliseconds) is irrelevant for this test case.
        ISO8601Utils.format(null, true);
    }
}